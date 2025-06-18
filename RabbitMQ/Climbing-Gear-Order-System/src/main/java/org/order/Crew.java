package org.order;

import com.rabbitmq.client.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

import static org.order.RabbitMQHelper.*;

public class Crew {
    private static final Logger logger = Logger.getLogger(Crew.class.getName());


    public static void main(String[] args) throws Exception {
        logger.info("Starting the Crew panel...");

        if (args.length < 1) {
            System.err.println("Pass crew name!");
            return;
        }

        String identifier = args[0];

        Connection connection = createConnection();
        Channel channel = createChannel(connection);

        declareExchanges(channel);

        declareQueueAndBindings(channel, identifier);

        Consumer consumer = createConsumer(channel);
        startConsuming(channel, identifier, consumer);

        panelLoop(channel, connection, identifier);
    }

    private static void declareExchanges(Channel channel) throws IOException {
        channel.exchangeDeclare(EXCHANGE_FOR_ORDER_TOPIC, BuiltinExchangeType.TOPIC);
        channel.exchangeDeclare(EXCHANGE_FOR_ALL_FANOUT, BuiltinExchangeType.FANOUT);
        channel.exchangeDeclare(EXCHANGE_FOR_CREW_FANOUT, BuiltinExchangeType.FANOUT);
    }


    private static void declareQueueAndBindings(Channel channel, String identifier) throws IOException {
        channel.queueDeclare(identifier, false, false, false, null);
        channel.queueBind(identifier, EXCHANGE_FOR_ORDER_TOPIC, String.format("Order.%s", identifier));
        channel.queueBind(identifier, EXCHANGE_FOR_ALL_FANOUT, "");
        channel.queueBind(identifier, EXCHANGE_FOR_CREW_FANOUT, "");

        channel.basicQos(1);
    }

    private static Consumer createConsumer(Channel channel) {
        return new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("Received message: " + message);
            }
        };
    }

    private static void startConsuming(Channel channel, String identifier, Consumer consumer) throws IOException {
        channel.basicConsume(identifier, true, consumer);
    }

    private static void panelLoop(Channel channel, Connection connection, String identifier) throws IOException, TimeoutException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        List<String> gears = List.of("Oxygen", "Shoes", "Backpack");

        while (true) {
            System.out.print("OPTIONS [Q as quit - exit crew panel or enter needed stuff " + gears + ": \n");
            String stuff = br.readLine();
            if (Objects.equals("q", stuff.toLowerCase()) || Objects.equals("quit", stuff.toLowerCase())) {
                logger.info("Closing the panel.");
                channel.close();
                connection.close();
                break;
            }

            if (!gears.contains(stuff)) {
                System.out.println("Invalid stuff");
                continue;
            }

            String message = String.format("%s;%s", identifier, stuff);

            channel.basicPublish(EXCHANGE_FOR_ORDER_TOPIC,  makeOrderMessageKey(stuff), null, message.getBytes("UTF-8"));
            logger.info("Message sent: " + message);
        }
    }
}