package org.order;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

public class RabbitMQHelper {

    private static final Logger logger = Logger.getLogger(RabbitMQHelper.class.getName());

    public static final String EXCHANGE_FOR_ORDER_TOPIC = "exchange_for_order_topic";
    public static final String EXCHANGE_FOR_ALL_FANOUT = "exchange_for_all_fanout";
    public static final String EXCHANGE_FOR_SUPPLIER_FANOUT = "exchange_for_supplier_fanout";
    public static final String EXCHANGE_FOR_CREW_FANOUT = "exchange_for_crew_fanout";
    public static Connection createConnection() throws IOException, TimeoutException {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            return factory.newConnection();
        } catch (IOException | TimeoutException e) {
            logger.severe("Error while creating RabbitMQ connection: " + e.getMessage());
            throw e;
        }
    }

    public static Channel createChannel(Connection connection) throws IOException {
        return connection.createChannel();
    }

    public static String makeOrderMessageKey(String key) {
        return String.format("Order.%s", key);
    }
}