import org.apache.zookeeper.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.logging.Logger;

public class ZooKeeperWatcher implements Watcher {
    private static final Logger logger = Logger.getLogger(ZooKeeperWatcher.class.getName());
    private static final int SESSION_TIMEOUT_MS = 3000;
    private static final int CLOSE_TIMEOUT_MS = 5000;
    private final ZooKeeper zooKeeper;
    private final ZKGuiManager zkGuiManager;
    private Process execProcess;
    private final String zNodeName;
    private final String[] exec;

    public ZooKeeperWatcher(String hostPort, String zNodeName, String exec[]) throws IOException {
        this.zooKeeper = new ZooKeeper(hostPort, SESSION_TIMEOUT_MS, this);
        this.zkGuiManager = new ZKGuiManager(zooKeeper);
        this.zNodeName = zNodeName;
        updateChildrenQuantity();
        this.exec = exec;
    }

    public void startWatching() {
        try {
            zooKeeper.addWatch(this.zNodeName, AddWatchMode.PERSISTENT_RECURSIVE);
        } catch (KeeperException | InterruptedException e) {
            logger.severe("Failed to add watch: " + e.getMessage());
        }
        inputHandler();
    }

    private void inputHandler() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            while (true) {
                String input = br.readLine();
                if (Objects.equals("quit", input.toLowerCase()) || Objects.equals("q", input.toLowerCase())) {
                    zooKeeper.close(CLOSE_TIMEOUT_MS);
                    terminateExecProcess();
                    break;
                }
            }
        } catch (IOException | InterruptedException e) {
            logger.severe("Input loop interrupted: " + e.getMessage());
        }
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        String watchedEventPath = watchedEvent.getPath();

        switch (watchedEvent.getType()) {
            case NodeCreated -> handleNodeCreated(watchedEventPath);
            case NodeDeleted -> handleNodeDeleted(watchedEventPath);
            case NodeChildrenChanged -> updateChildrenQuantity();
            default -> System.out.println("Unhandled event type: " + watchedEvent.getType());
        }
    }

    private void handleNodeCreated(String watchedEventPath) {
        logger.info(String.format("Created zNode [%s]", watchedEventPath));
        if (watchedEventPath.equals(zNodeName)) {
            runExecProcess();
        } else if (watchedEventPath.startsWith(zNodeName)) {
            updateChildrenQuantity();
        }
    }

    private void handleNodeDeleted(String watchedEventPath) {
        logger.info(String.format("Deleted zNode [%s]", watchedEventPath));
        if (watchedEventPath.equals(zNodeName)) {
            terminateExecProcess();
            zkGuiManager.clearTree();
        } else if (watchedEventPath.startsWith(zNodeName)) {
            updateChildrenQuantity();
        }
    }

    private void updateChildrenQuantity() {
        try {
            int childrenQuantity = zooKeeper.getAllChildrenNumber(zNodeName);
            zkGuiManager.refreshChildCount(childrenQuantity);
            zkGuiManager.loadZkTree(zNodeName);

        } catch (KeeperException | InterruptedException e) {
            logger.severe("Failed to update children count: " + e.getMessage());
        }
    }

    private void runExecProcess() {
        logger.info("Running exec process: " + String.join(" ", exec));
        try {
            execProcess = Runtime.getRuntime().exec(exec);
        } catch (IOException e) {
            logger.severe("Failed to start exec process: " + e.getMessage());
        }
    }

    private void terminateExecProcess() {
        if (execProcess != null && execProcess.isAlive()) {
            logger.info("Terminating exec process");
            execProcess.destroy();
        } else {
            logger.info("Exec process is already terminated");
        }
    }

}
