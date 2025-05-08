package sr.grpc.server;

import io.grpc.ServerBuilder;
import sr.grpc.server.impl.MathServiceImpl;
import sr.grpc.server.impl.StatServiceImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Server {

    public static void main(String[] args) throws InterruptedException {
        List<Integer> ports = List.of(50052, 50053, 50054, 50055, 50056);
        List<Thread> threads = new ArrayList<>();

        for (int port : ports) {
            final int serverPort = port;
            Thread serverThread = new Thread(() -> {
                try {
                    startServer(serverPort);
                } catch (IOException e) {
                    System.err.println("IO Error starting server on port " + serverPort + ": " + e.getMessage());
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    System.err.println("ServeFr on port " + serverPort + " was interrupted.");
                    Thread.currentThread().interrupt();
                } catch (Exception e) {
                    System.err.println("Unexpected error on server port " + serverPort + ": " + e.getMessage());
                    e.printStackTrace();
                }
            });
            threads.add(serverThread);
            serverThread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }
    }

    public static void startServer(int port) throws IOException, InterruptedException {
        RequestCounter counter = new RequestCounter();

        io.grpc.Server server = ServerBuilder.forPort(port)
                .addService(new MathServiceImpl(port, counter))
                .addService(new StatServiceImpl(port, counter))
                .build();

        System.out.println("Starting Server on port " + port + " ...");
        server.start();
        server.awaitTermination();
    }
}


