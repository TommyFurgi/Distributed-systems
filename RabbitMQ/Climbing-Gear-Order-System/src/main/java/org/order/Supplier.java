package org.order;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

import static org.order.RabbitMQHelper.*;

public class Supplier {
    private static final Logger logger = Logger.getLogger(Supplier.class.getName());

    public static void main(String[] args) throws Exception {
        logger.info("Starting the Supplier panel...");

        if (args.length < 2) {
            System.err.println("Usage: Supplier <name> <type1,type2,...>");
            return;
        }

        String supplierName = args[0];
        List<String> supportedTypes = Arrays.asList(args[1].split(","));
        System.out.println("Supply stuff types: " + supportedTypes);

        Connection connection = createConnection();
        Channel channel = createChannel(connection);

        declareExchanges(channel);

        String supplierQueue = declareQueuesAndBindings(channel, supportedTypes);

        Consumer consumer = createConsumer(channel, supplierName);
        startConsuming(channel, supportedTypes, supplierQueue, consumer);
    }

    private static void declareExchanges(Channel channel) throws IOException {
        channel.basicQos(3);
        channel.exchangeDeclare(EXCHANGE_FOR_ORDER_TOPIC, BuiltinExchangeType.TOPIC);
        channel.exchangeDeclare(EXCHANGE_FOR_ALL_FANOUT, BuiltinExchangeType.FANOUT);
        channel.exchangeDeclare(EXCHANGE_FOR_SUPPLIER_FANOUT, BuiltinExchangeType.FANOUT);
    }

    private static String declareQueuesAndBindings(Channel channel, List<String> products) throws IOException {
        String supplierQueue = channel.queueDeclare().getQueue();

        channel.queueBind(supplierQueue, EXCHANGE_FOR_ALL_FANOUT, "");
        channel.queueBind(supplierQueue, EXCHANGE_FOR_SUPPLIER_FANOUT, "");

        for (String product : products) {
            channel.queueDeclare(product, false, false, false, null);
            channel.queueBind(product, EXCHANGE_FOR_ORDER_TOPIC, String.format("Order.%s", product));
        }

        return supplierQueue;
    }

    private static Consumer createConsumer(Channel channel, String identifier) {
        return new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("Received message: " + message);

                if (!Objects.equals(message.split(" ")[0], "Admin")){
                    String[] parts = message.split(";");
                    String crewIdentifier = parts[0];
                    String stuff = parts[1];
                    processOrder();

                    String responseMessage = String.format("%s;%s - done", identifier, stuff);
                    logger.info("Message sent: " + responseMessage);
                    channel.basicPublish(EXCHANGE_FOR_ORDER_TOPIC,  makeOrderMessageKey(crewIdentifier), null, responseMessage.getBytes("UTF-8"));

                }
            }
        };
    }

    private static void startConsuming(Channel channel, List<String> products, String supplierQueue, Consumer consumer) throws IOException {
        logger.info("Waiting for messages from Crews...");
        for (String product : products) {
            channel.basicConsume(product, true, consumer);
        }
        channel.basicConsume(supplierQueue, true, consumer);
    }

    private static void processOrder() {
        System.out.println("Order processing...");
        try {
            int timeToProcess = new Random().nextInt(6) * 1000;
            Thread.sleep(timeToProcess);
        } catch (InterruptedException e) {
            logger.severe("Error during process order: " + e.getMessage());
        }
    }
}