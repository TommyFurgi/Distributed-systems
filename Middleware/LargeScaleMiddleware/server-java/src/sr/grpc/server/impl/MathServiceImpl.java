package sr.grpc.server.impl;

import io.grpc.stub.StreamObserver;
import math.Math;
import math.MathServiceGrpc;
import sr.grpc.server.RequestCounter;

public class MathServiceImpl extends MathServiceGrpc.MathServiceImplBase {
    private final int port;
    private RequestCounter requestCounter;

    public MathServiceImpl(int port, RequestCounter counter) {
        this.port = port;
        this.requestCounter = counter;
    }
    @Override
    public void isPerfectNumber(Math.NumberRequest request, StreamObserver<Math.BoolResponse> responseObserver) {
        int current = requestCounter.incrementAndGet();
        System.out.println("Server " + port + " is processing perfect number for: " + request.getNumber() + ", requests count: " + current);
        long number = request.getNumber();
        boolean isPerfect = isPerfectNumber(number);

        try { Thread.sleep(5000); } catch (InterruptedException e) { e.printStackTrace(); }

        Math.BoolResponse response = Math.BoolResponse.newBuilder()
                .setIsPerfect(isPerfect)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();

        current = requestCounter.decrementAndGet();
        System.out.println(port + " FINISHED processing perfect number" + ", requests count: " + current);
    }

    @Override
    public void gcd(Math.GcdRequest request, StreamObserver<Math.GcdResponse> responseObserver) {
        int current = requestCounter.incrementAndGet();
        System.out.println("Server " + port + " is processing GCD for: " + request.getA() + " and " + request.getB() + ", requests count: " + current);
        long a = request.getA();
        long b = request.getB();
        long result = gcd(a, b);

        try { Thread.sleep(8000); } catch (InterruptedException e) { e.printStackTrace(); }

        Math.GcdResponse response = Math.GcdResponse.newBuilder()
                .setGcd(result)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();

        current = requestCounter.decrementAndGet();
        System.out.println(port + " FINISHED processing gdc" + ", requests count: " + current);
    }

    private long gcd(long a, long b) {
        while (b != 0) {
            long temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    private boolean isPerfectNumber(long number) {
        long sum = 1;
        for (long i = 2; i <= java.lang.Math.sqrt(number); i++) {
            if (number % i == 0) {
                sum += i;
                if (i != number / i) {
                    sum += number / i;
                }
            }
        }
        return sum == number && number != 1;
    }
}
