package sr.ice.server.impl;

import Home.*;
import com.zeroc.Ice.Current;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ControllerI implements Controller {
    private int genId;
    public static final Map<Integer, StoveObject> stoves = new HashMap<>();
    public static final Map<Integer, CleanerObject> cleaners = new HashMap<>();
    public static final Map<Integer, FridgeObject> fridges = new HashMap<>();

    public ControllerI(int genId) {
        this.genId = genId * 100;
    }

    @Override
    public DeviceSummary getAllDevices(Current current)  {
        return new DeviceSummary(
                stoves.values().toArray(new StoveObject[0]),
                cleaners.values().toArray(new CleanerObject[0]),
                fridges.values().toArray(new FridgeObject[0])
        );
    }

    @Override
    public DeviceType getDeviceType(int deviceId, Current current) {
        if (stoves.containsKey(deviceId)) {
            return DeviceType.STOVE;
        } else if (cleaners.containsKey(deviceId)) {
            return DeviceType.CLEANER;
        } else if (fridges.containsKey(deviceId)) {
            return DeviceType.FRIDGE;
        } else {
            return DeviceType.NONE;
        }
    }

    public int addNewStove(StoveType stoveType) {
        StoveObject stove = new StoveObject(genId, stoveType, false, 0.0, "Ready to work");
        stoves.put(genId, stove);
        genId += 1;
        return stove.id;
    }

    public int addNewFridge() {
        FridgeObject fridge = new FridgeObject(genId, 4.0, 100.0, "Ready to work", new ArrayList<>(List.of(4.0)));
        fridges.put(genId, fridge);
        genId += 1;
        return fridge.id;
    }

    public int addNewCleaner() {
        String message = "Ready to work";
        String currentTime = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Logger log = new Logger(currentTime, message);
        CleanerObject cleaner = new CleanerObject(genId, false, 100.0, 0.0, message, new ArrayList<>(List.of(log)));
        cleaners.put(genId, cleaner);
        genId += 1;
        return cleaner.id;
    }
}
