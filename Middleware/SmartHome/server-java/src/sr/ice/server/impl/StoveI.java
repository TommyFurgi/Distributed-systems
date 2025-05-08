package sr.ice.server.impl;

import Home.*;
import com.zeroc.Ice.Current;

public class StoveI implements Stove {
    private static StoveObject getStoveData(int stoveId) {
        StoveObject stove = ControllerI.stoves.get(stoveId);

        if (stove == null) {
            throw new RuntimeException("Stove with ID " + stoveId + " not found");
        }
        return stove;
    }

    private long getHeatingTimePerDegree(StoveType type) {
        return switch (type) {
            case TILED -> 100;
            case GAS -> 50;
            case PUMP -> 20;
        };
    }

    @Override
    public void heatWater(int stoveId, double temp, Current current) throws DangerException, InvalidArgumentException, DeviceInUseException {
        StoveObject stove = getStoveData(stoveId);

        if (stove.isWorking) {
            throw new DeviceInUseException(String.format("The stove with id %d is busy now", stoveId));
        }

        if (temp < 20.0 || temp > 80.0) {
            throw new InvalidArgumentException("Temperature must be between 20 and 80 degrees Celsius");
        }

        if (stove.heat > 80.0) {
            throw new DangerException(DangerOperation.OVERHEAT, "Stove is currently too hot to heat water!");
        }

        stove.isWorking = true;
        stove.message = "Stove is heating water to " + temp + " degrees Celsius";
        System.out.println("Stove #" + stoveId + " is heating water to " + temp + " degrees Celsius.");

        double difference = Math.abs(temp - stove.heat);
        long delayPerDegree = getHeatingTimePerDegree(stove.type);
        long totalDelay = (long)(difference * delayPerDegree);
        try { Thread.sleep(totalDelay); } catch (InterruptedException ex) { Thread.currentThread().interrupt(); }

        stove.isWorking = false;
        stove.heat = temp;
        stove.message = "Water heated successfully to " + temp + " degrees Celsius.";
    }

    @Override
    public boolean isWorking(int stoveId, Current current) {
        StoveObject stove = getStoveData(stoveId);

        return stove.isWorking;
    }

    @Override
    public double getHeatLevel(int stoveId, Current current) {
        StoveObject stove = getStoveData(stoveId);

        return stove.heat;
    }

    @Override
    public void setHeatLevel(int stoveId, double heat, Current current) throws InvalidArgumentException, DeviceInUseException {
        StoveObject stove = getStoveData(stoveId);

        if (stove.isWorking) {
            throw new DeviceInUseException(String.format("The stove with id %d is busy now", stoveId));
        }

        if (heat < 0.0 || heat > 100.0) {
            throw new InvalidArgumentException("Heat level must be between 0 and 100 degrees Celsius");
        }

        stove.heat = heat;
        stove.message = "Heat level set to " + heat + " degrees Celsius";

        System.out.println("Stove #" + stoveId + ": Heat level set to " + heat + " degrees Celsius.");
    }

    @Override
    public StoveType getStoveType(int stoveId, Current current) {
        StoveObject stove = getStoveData(stoveId);

        return stove.type;
    }

    @Override
    public DeviceInfo checkState(int deviceId, Current current) {
        StoveObject stove = getStoveData(deviceId);

        DeviceInfo info = new DeviceInfo();
        info.message = stove.message;
        return info;
    }
}
