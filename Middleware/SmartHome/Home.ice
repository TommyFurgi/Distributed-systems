
#ifndef SMART_HOME
#define SMART_HOME

module Home {
    struct Logger {
        string date;
        string message;
    };
    
    ["java:type:java.util.ArrayList<Logger>"]
    sequence<Logger> LoggerSeq;

    enum DangerOperation {
        OVERHEAT,
        EXHAUSTION,
        OVERFLOW
    }

    enum DeviceType {
        NONE,
        STOVE,
        CLEANER,
        FRIDGE
    }

    exception DangerException {
        DangerOperation whatOp;
        string message;
    }

    exception InvalidArgumentException {
        string message;
    }

    exception DeviceInUseException {
        string message;
    }

    enum StoveType {
        TILED,
        GAS,
        PUMP
    }

    struct StoveObject {
        int id;
        StoveType type;
	bool isWorking;
        double heat;
        string message;
    }

    struct CleanerObject {
        int id;
        bool onTask;
        double battery;
        double dustBagLevel;
        string message;
        LoggerSeq logger;
    }
	
    ["java:type:java.util.ArrayList<Double>"]
    sequence<double> TemperatureSeq;

    class FridgeObject {
        int id;
        double temperature;
        double energyUsage;
        string message;
        TemperatureSeq temperatureHistory;
    };

    struct DeviceInfo {
        string message;
    }

    sequence<StoveObject> StoveObjectSeq;
    sequence<CleanerObject> CleanerObjectSeq;
    sequence<FridgeObject> FridgeObjectSeq;

    struct DeviceSummary {
        StoveObjectSeq stoves;
        CleanerObjectSeq cleaners;
        FridgeObjectSeq fridges;
    }

    interface Device {
        idempotent DeviceInfo checkState(int deviceId);
    }

    interface Stove extends Device {
        void heatWater(int stoveId, double temp)
            throws DangerException, InvalidArgumentException, DeviceInUseException;

        idempotent bool isWorking(int stoveId);
        idempotent double getHeatLevel(int stoveId);
        void setHeatLevel(int stoveId, double heat)
            throws InvalidArgumentException, DeviceInUseException;
	idempotent StoveType getStoveType(int stoveId);
    }

    interface Cleaner extends Device {
        void vacuumRoom(int cleanerId)
            throws DangerException, DeviceInUseException;
        
        idempotent double getBatteryLevel(int cleanerId);
	    idempotent double getBagLevel(int cleanerId);
        void returnToBase(int cleanerId)
	        throws DeviceInUseException;

        void emptyBag(int cleanerId)
            throws DeviceInUseException, DangerException;

        idempotent bool hasTask(int cleanerId);
        idempotent LoggerSeq getLogs(int cleanerId);
    }

    interface Fridge extends Device {
        void changeTemperature(int fridgeId, double temp)
            throws InvalidArgumentException;

        idempotent double getCurrentTemperature(int fridgeId);
        idempotent double getEnergyUsage(int cleanerId);
        idempotent TemperatureSeq getTemperatureHistory(int fridgeId);
	void setEcoMode(int fridgeId, bool mode);
    }

    interface Controller {
        idempotent DeviceSummary getAllDevices();
        idempotent DeviceType getDeviceType(int deviceId);
    }
};

#endif
