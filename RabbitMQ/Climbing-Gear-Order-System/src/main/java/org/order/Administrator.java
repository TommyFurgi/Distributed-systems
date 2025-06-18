package org.order;

import com.rabbitmq.client.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

import static org.order.RabbitMQHelper.*;

public class Administrator {
    private static final Logger logger = Logger.getLogger(Administrator.class.getName());

    public static void main(String[] args) throws Exception {
        logger.info("Starting the administrator panel...");

        Connection connection = createConnection();
        Channel channel = createChannel(connection);

        declareExchanges(channel);

        String administratorQueue = declareQueue(channel);

        bindQueues(channel, administratorQueue);

        Consumer consumer = createConsumer(channel);
        startConsuming(channel, administratorQueue, consumer);

        panelLoop(channel, connection);
    }

    private static void declareExchanges(Channel channel) throws IOException {
        channel.exchangeDeclare(EXCHANGE_FOR_ORDER_TOPIC, BuiltinExchangeType.TOPIC);
        channel.exchangeDeclare(EXCHANGE_FOR_ALL_FANOUT, BuiltinExchangeType.FANOUT);
        channel.exchangeDeclare(EXCHANGE_FOR_SUPPLIER_FANOUT, BuiltinExchangeType.FANOUT);
    }

    private static String declareQueue(Channel channel) throws IOException {
        return channel.queueDeclare().getQueue();
    }

    private static void bindQueues(Channel channel, String queueName) throws IOException {
        channel.queueBind(queueName, EXCHANGE_FOR_ORDER_TOPIC, "Order.#");
//        channel.queueBind(queueName, EXCHANGE_FOR_ALL_FANOUT, "");
//        channel.queueBind(queueName, EXCHANGE_FOR_SUPPLIER_FANOUT, "");
//        channel.queueBind(queueName, EXCHANGE_FOR_CREW_FANOUT, "");
    }

    private static Consumer createConsumer(Channel channel) {
        return new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("Received: " + message);
            }
        };
    }

    private static void startConsuming(Channel channel, String queueName, Consumer consumer) throws IOException {
        logger.info("Waiting for messages...");
        channel.basicConsume(queueName, true, consumer);
    }

    private static void panelLoop(Channel channel, Connection connection) throws IOException, TimeoutException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            System.out.print("OPTIONS [Q as quit - exit the administrator panel, A as send - to send a message to all Crews and Suppliers, \n" +
                    "C as send - to send a message to all Crews, S as send - to send a message to all Suppliers \n");
            String operation = br.readLine();

            if (Objects.equals("q", operation.toLowerCase()) || Objects.equals("quit", operation.toLowerCase())) {
                logger.info("Closing the administrator panel.");
                channel.close();
                connection.close();
                break;
            } else if (!Objects.equals("s", operation.toLowerCase()) && !Objects.equals("c", operation.toLowerCase()) && !Objects.equals("a", operation.toLowerCase()))  {
                System.out.println("Unknown type");
                continue;
            }

            System.out.print("Enter message: ");
            String message = br.readLine();

            String messageToSend = String.format("Admin Panel: %s", message);
            if (Objects.equals("s", operation.toLowerCase())) {
                channel.basicPublish(EXCHANGE_FOR_SUPPLIER_FANOUT, "", null, messageToSend.getBytes("UTF-8"));
                logger.info("Message sent to all suppliers: " + message);
            } else if (Objects.equals("c", operation.toLowerCase())) {
                channel.basicPublish(EXCHANGE_FOR_CREW_FANOUT, "", null, messageToSend.getBytes("UTF-8"));
                logger.info("Message sent to all crews: " + message);
            } else if (Objects.equals("a", operation.toLowerCase())) {
                channel.basicPublish(EXCHANGE_FOR_ALL_FANOUT, "", null, messageToSend.getBytes("UTF-8"));
                logger.info("Message sent to all users: " + message);
            }
        }
    }
}