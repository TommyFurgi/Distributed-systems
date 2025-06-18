import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ZKGuiManager {
    private static final Logger log = Logger.getLogger(ZKGuiManager.class.getName());

    private final ZooKeeper zkClient;
    private final JLabel infoLabel;
    private final DefaultMutableTreeNode rootTreeNode;
    private final DefaultTreeModel treeStructure;

    public ZKGuiManager(ZooKeeper client) {
        this.zkClient = client;
        this.infoLabel = new JLabel("Children quantity: 0", SwingConstants.CENTER);
        this.rootTreeNode = new DefaultMutableTreeNode("ZK Root");
        this.treeStructure = new DefaultTreeModel(rootTreeNode);
        setupMainFrame();
    }

    private void setupMainFrame() {
        JFrame window = new JFrame("ZK Tree Viewer");
        window.setLayout(new BorderLayout());

        window.add(infoLabel, BorderLayout.NORTH);
        JTree zkTree = new JTree(treeStructure);
        JScrollPane scrollPane = new JScrollPane(zkTree);
        window.add(scrollPane, BorderLayout.CENTER);

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(500, 350);
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }

    public void refreshChildCount(int childCount) {
        infoLabel.setText("Children count: " + childCount);
    }

    public void loadZkTree(String path) {
        rootTreeNode.removeAllChildren();
        try {
            buildSubTree(path, rootTreeNode);
        } catch (Exception ex) {
            log.log(Level.WARNING, "Error retrieving nodes for path: " + path, ex);
        }
        treeStructure.reload();
    }

    private void buildSubTree(String nodePath, DefaultMutableTreeNode parent) {
        try {
            List<String> subNodes = zkClient.getChildren(nodePath, false);
            for (String node : subNodes) {
                String fullPath = nodePath.equals("/") ? "/" + node : nodePath + "/" + node;
                DefaultMutableTreeNode childTreeNode = new DefaultMutableTreeNode(node);
                parent.add(childTreeNode);
                buildSubTree(fullPath, childTreeNode);
            }
        } catch (KeeperException ke) {
            log.warning("Node not found or error accessing: " + nodePath);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            log.severe("Thread interrupted while accessing ZK node: " + nodePath);
        }
    }

    public void clearTree() {
        rootTreeNode.removeAllChildren();
        treeStructure.reload();
        infoLabel.setText("Children count: 0");
    }

}
