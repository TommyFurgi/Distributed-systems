package sr.grpc.server.impl;

import io.grpc.stub.StreamObserver;
import sr.grpc.server.RequestCounter;
import stat.Stat;
import stat.StatServiceGrpc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StatServiceImpl extends StatServiceGrpc.StatServiceImplBase {
    private final int port;
    private RequestCounter requestCounter;

    public StatServiceImpl(int port, RequestCounter counter) {
        this.port = port;
        this.requestCounter = counter;
    }
    @Override
    public void average(Stat.AverageRequest request, StreamObserver<Stat.AverageResponse> responseObserver) {
        int current = requestCounter.incrementAndGet();
        System.out.println("Server " + port + " is processing average for: " + request.getNumbersList() + ", requests count: " + current);

        List<Double> numbers = request.getNumbersList();
        double avg = numbers.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);

        try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }

        Stat.AverageResponse response = Stat.AverageResponse.newBuilder()
                .setResult(avg)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();

        current = requestCounter.decrementAndGet();
        System.out.println(port + " FINISHED processing average" + ", requests count: " + current);
    }

    @Override
    public void median(Stat.MedianRequest request, StreamObserver<Stat.MedianResponse> responseObserver) {
        int current = requestCounter.incrementAndGet();
        System.out.println("Server " + port + " is processing median for: " + request.getNumbersList() + ", requests count: " + current);

        List<Double> numbers = new ArrayList<>(request.getNumbersList());
        Collections.sort(numbers);

        double median;
        int size = numbers.size();
        if (size == 0) {
            median = 0.0;
        } else if (size % 2 == 1) {
            median = numbers.get(size / 2);
        } else {
            median = (numbers.get(size / 2 - 1) + numbers.get(size / 2)) / 2.0;
        }

        try { Thread.sleep(13000); } catch (InterruptedException e) { e.printStackTrace(); }

        Stat.MedianResponse response = Stat.MedianResponse.newBuilder()
                .setResult(median)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();

        current = requestCounter.decrementAndGet();
        System.out.println(port + " FINISHED processing median" + ", requests count: " + current);
    }
}

