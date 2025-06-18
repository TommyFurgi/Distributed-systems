import java.io.IOException;
import java.util.logging.Logger;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        if (args.length < 3) {
            logger.severe("Invalid arguments. Expected: <host>:<port> <watched zNodeName> <execPath> <execArgs>*");
            System.exit(1);
        }

        String hostPort = args[0];
        String zNodeName = args[1];
        String[] exec = new String[args.length - 2];
        System.arraycopy(args, 2, exec, 0, exec.length);

        try {
            ZooKeeperWatcher watcher = new ZooKeeperWatcher(hostPort, zNodeName, exec);
            watcher.startWatching();
        } catch (IOException e) {
            logger.severe("Failed to start ZooKeeperWatcher: " + e.getMessage());
            System.exit(2);
        }
    }
}
