package sr.grpc.server;

public class RequestCounter {
    private int counter = 0;
    public synchronized int incrementAndGet() {
        counter += 1;
        return counter;
    }

    public synchronized int decrementAndGet() {
        counter -= 1;
        return counter;
    }
}
