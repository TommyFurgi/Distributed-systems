package sr.ice.server;

import Home.*;
import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.Identity;
import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.Util;
import sr.ice.server.impl.CleanerI;
import sr.ice.server.impl.ControllerI;
import sr.ice.server.impl.FridgeI;
import sr.ice.server.impl.StoveI;

public class IceServer1 {
	public void t1(String[] args) {
		int status = 0;
		Communicator communicator = null;

		try {
			communicator = Util.initialize(args);

			ObjectAdapter adapter = communicator.createObjectAdapter("Adapter1");
			String idString = communicator.getProperties().getProperty("Server.Id");
			int serverId = Integer.parseInt(idString);

			CleanerI cleanerServant = new CleanerI();
			FridgeI fridgeServant = new FridgeI();
			StoveI stoveServant = new StoveI();

			ControllerI controller = new ControllerI(serverId);
			adapter.add(controller, new Identity("controller", "controller"));
			int id;

			id = controller.addNewStove(StoveType.TILED);
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
		IceServer1 app = new IceServer1();
		app.t1(args);
	}
}