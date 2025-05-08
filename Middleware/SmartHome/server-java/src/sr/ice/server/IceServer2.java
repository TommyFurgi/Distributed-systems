package sr.ice.server;

import Home.StoveType;
import com.zeroc.Ice.*;
import sr.ice.server.impl.CleanerI;
import sr.ice.server.impl.ControllerI;
import sr.ice.server.impl.FridgeI;
import sr.ice.server.impl.StoveI;

import java.lang.Exception;

public class IceServer2 {
    public void t2(String[] args) {
        int status = 0;
        Communicator communicator = null;

        try {
            communicator = Util.initialize(args);

            ObjectAdapter adapter = communicator.createObjectAdapter("Adapter2");
            String idString = communicator.getProperties().getProperty("Server.Id");
            int serverId = Integer.parseInt(idString);

            CleanerI cleanerServant = new CleanerI();
            FridgeI fridgeServant = new FridgeI();
            StoveI stoveServant = new StoveI();

            ControllerI controller = new ControllerI(serverId);
            adapter.add(controller, new Identity("controller", "controller"));
            int id;

            id = controller.addNewStove(StoveType.PUMP);
            adapter.add(stoveServant, new Identity(String.valueOf(id), "device"));

            id = controller.addNewStove(StoveType.GAS);
            adapter.add(stoveServant, new Identity(String.valueOf(id), "device"));

            id = controller.addNewFridge();
            adapter.add(fridgeServant, new Identity(String.valueOf(id), "device"));

            id = controller.addNewFridge();
            adapter.add(fridgeServant, new Identity(String.valueOf(id), "device"));

            id = controller.addNewCleaner();
            adapter.add(cleanerServant, new Identity(String.valueOf(id), "device"));

            id = controller.addNewCleaner();
            adapter.add(cleanerServant, new Identity(String.valueOf(id), "device"));

            adapter.activate();
            System.out.println("Entering event processing loop...");

            communicator.waitForShutdown();

        } catch (Exception e) {
            e.printStackTrace(System.err);
            status = 1;
        }
        if (communicator != null) {
            try {
                communicator.destroy();
            } catch (Exception e) {
                e.printStackTrace(System.err);
                status = 1;
            }
        }
        System.exit(status);
    }


    public static void main(String[] args) {
        IceServer2 app = new IceServer2();
        app.t2(args);
    }
}
