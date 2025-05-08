package sr.ice.server.impl;

import Home.*;
import com.zeroc.Ice.Current;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CleanerI implements Cleaner {

    private static CleanerObject getCleanerData(int cleanerId) {
        CleanerObject cleaner = ControllerI.cleaners.get(cleanerId);

        if (cleaner == null) {
            throw new RuntimeException("Cleaner with ID " + cleanerId + " not found");
        }
        return cleaner;
    }

    public void addLog(CleanerObject cleaner, String message) {
        Logger log = new Logger();
        log.date = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        log.message = message;
        cleaner.logger.add(log);
    }

    @Override
    public void vacuumRoom(int cleanerId, Current current) throws DangerException, DeviceInUseException {
        CleanerObject cleaner = getCleanerData(cleanerId);

        if (cleaner.onTask) {
            throw new DeviceInUseException(String.format("The Cleaner with id %d is busy now", cleanerId));
        }

        if (cleaner.battery < 20.0) {
            throw new DangerException(DangerOperation.EXHAUSTION, "Battery too low to vacuum!");
        }

        if (cleaner.dustBagLevel > 90.0) {
            throw new DangerException(DangerOperation.OVERFLOW, "Dust bag is almost full!");
        }

        System.out.println("Cleaner #" + cleanerId + " is vacuuming...");

        cleaner.onTask = true;
        cleaner.message = "Cleaner is vacuuming now";
        addLog(cleaner, cleaner.message);

        try { Thread.sleep(10000); } catch (InterruptedException ex) { Thread.currentThread().interrupt(); }

        cleaner.battery = (cleaner.battery - 20.0);
        cleaner.dustBagLevel = (cleaner.dustBagLevel + 10.0);
        cleaner.onTask = false;
        cleaner.message = "Vacuumed successfully";
        addLog(cleaner, cleaner.message);

        System.out.println("Cleaner #" + cleanerId + " vacuumed successfully.");
    }

    @Override
    public double getBatteryLevel(int cleanerId, Current current) {
        CleanerObject cleaner = getCleanerData(cleanerId);

        return cleaner.battery;
    }

    @Override
    public double getBagLevel(int cleanerId, Current current) {
        CleanerObject cleaner = getCleanerData(cleanerId);

        return cleaner.dustBagLevel;
    }

    @Override
    public void returnToBase(int cleanerId, Current current) throws DeviceInUseException {
        CleanerObject cleaner = getCleanerData(cleanerId);

        if (cleaner.onTask) {
            throw new DeviceInUseException(String.format("The Cleaner with id %d is busy now", cleanerId));
        }


        cleaner.message = "Returning to base";
        addLog(cleaner, cleaner.message);
        cleaner.onTask = true;

        try { Thread.sleep(2000); } catch (InterruptedException ex) { Thread.currentThread().interrupt(); }

        cleaner.onTask = false;
        cleaner.battery = 100.0;
        cleaner.message = "Cleaner in base, ready to work";
        addLog(cleaner, cleaner.message);

        System.out.println("Cleaner #" + cleanerId + " is now at the base.");
    }

    @Override
    public void emptyBag(int cleanerId, Current current) throws DeviceInUseException, DangerException {
        CleanerObject cleaner = getCleanerData(cleanerId);

        if (cleaner.onTask) {
            throw new DeviceInUseException(String.format("The Cleaner with id %d is busy now", cleanerId));
        }

        if (cleaner.battery < 10.0) {
            throw new DangerException(DangerOperation.EXHAUSTION, "Battery too low to empty dust bag!");
        }


        cleaner.message = "Emptying dust bag";
        addLog(cleaner, cleaner.message);
        cleaner.onTask = true;

        try { Thread.sleep(3000); } catch (InterruptedException ex) { Thread.currentThread().interrupt(); }

        cleaner.dustBagLevel = 0.0;
        cleaner.battery -= 10.0;
        cleaner.message = "Dust bag has been emptied";
        addLog(cleaner, cleaner.message);

        System.out.println("Cleaner #" + cleanerId + ": bag emptied successfully.");
    }

    @Override
    public boolean hasTask(int cleanerId, Current current) {
        CleanerObject cleaner = getCleanerData(cleanerId);

        return cleaner.onTask;
    }

    @Override
    public List<Logger> getLogs(int cleanerId, Current current) {
        CleanerObject cleaner = getCleanerData(cleanerId);

        return cleaner.logger;
    }

    @Override
    public DeviceInfo checkState(int deviceId, Current current) {
        CleanerObject cleaner = getCleanerData(deviceId);

        DeviceInfo info = new DeviceInfo();
        info.message = cleaner.message;
        return info;
    }
}
