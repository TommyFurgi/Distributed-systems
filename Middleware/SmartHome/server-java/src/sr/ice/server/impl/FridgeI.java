package sr.ice.server.impl;

import Home.DeviceInfo;
import Home.Fridge;
import Home.FridgeObject;
import Home.InvalidArgumentException;
import com.zeroc.Ice.Current;

import java.util.List;

public class FridgeI implements Fridge {

    private static FridgeObject getFridgeData(int fridgeId) {
        FridgeObject fridge = ControllerI.fridges.get(fridgeId);

        if (fridge == null) {
            throw new RuntimeException("Fridge with ID " + fridgeId + " not found.");
        }
        return fridge;
    }

    @Override
    public void changeTemperature(int fridgeId, double temp, Current current) throws InvalidArgumentException {
        if (temp < 0.0 || temp > 6.0) {
            throw new InvalidArgumentException("Temperature must be between 0 and 6 degrees Celsius.");
        }

        FridgeObject fridge = getFridgeData(fridgeId);
        fridge.temperature = temp;
        fridge.temperatureHistory.add(temp);
        fridge.message = "Temperature changed to " + temp + " degrees.";

        System.out.println("Fridge #" + fridgeId + " temperature set to " + temp + " degrees.");
    }

    @Override
    public double getCurrentTemperature(int fridgeId, Current current) {
        FridgeObject fridge = getFridgeData(fridgeId);
        return fridge.temperature;
    }

    @Override
    public List<Double> getTemperatureHistory(int fridgeId, Current current) {
        FridgeObject fridge = getFridgeData(fridgeId);
        return fridge.temperatureHistory;
    }

    @Override
    public double getEnergyUsage(int fridgeId, Current current) {
        FridgeObject fridge = getFridgeData(fridgeId);
        return fridge.energyUsage;
    }

    @Override
    public void setEcoMode(int fridgeId, boolean mode, Current current) {
        FridgeObject fridge = getFridgeData(fridgeId);

        if (mode) {
            fridge.energyUsage = 70.0;
            fridge.message = "EcoMode has been enabled";
        } else {
            fridge.energyUsage = 100.0;
            fridge.message = "EcoMode has been disabled";
        }
    }

    @Override
    public DeviceInfo checkState(int deviceId, Current current) {
        FridgeObject fridge = getFridgeData(deviceId);

        DeviceInfo info = new DeviceInfo();
        info.message = fridge.message;
        return info;
    }
}
