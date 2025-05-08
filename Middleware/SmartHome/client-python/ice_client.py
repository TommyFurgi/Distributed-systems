import sys
import Ice
import time
from generated import Home_ice
from handler.fridge_handler import fridge_handler
from handler.cleaner_handler import cleaner_handler
from handler.stove_handler import stove_handler

def main():
    with Ice.initialize(sys.argv) as communicator:
        controller_prx1 = Home_ice._M_Home.ControllerPrx.checkedCast(communicator.stringToProxy("controller/controller:tcp -h 127.0.0.2 -p 10001"))
        controller_prx2 = Home_ice._M_Home.ControllerPrx.checkedCast(communicator.stringToProxy("controller/controller:tcp -h 127.0.0.3 -p 10002"))

        if not controller_prx1 or not controller_prx2:
            raise RuntimeError("Invalid proxy for one or more controllers.")

        controllers = [controller_prx1, controller_prx2]
        device_summary1 = controller_prx1.getAllDevices()
        device_summary2 = controller_prx2.getAllDevices()

        stove_ids = [stove.id for stove in device_summary1.stoves] + [stove.id for stove in device_summary2.stoves]
        cleaner_ids = [cleaner.id for cleaner in device_summary1.cleaners] + [cleaner.id for cleaner in device_summary2.cleaners]
        fridge_ids = [fridge.id for fridge in device_summary1.fridges] + [fridge.id for fridge in device_summary2.fridges]

        devices = {}

        for stove in device_summary1.stoves:
            devices[stove.id] = Home_ice._M_Home.StovePrx.checkedCast(
                communicator.stringToProxy(f"device/{stove.id}:tcp -h 127.0.0.2 -p 10001"))

        for stove in device_summary2.stoves:
            devices[stove.id] = Home_ice._M_Home.StovePrx.checkedCast(
                communicator.stringToProxy(f"device/{stove.id}:tcp -h 127.0.0.3 -p 10002"))

        for cleaner in device_summary1.cleaners:
            devices[cleaner.id] = Home_ice._M_Home.CleanerPrx.checkedCast(
                communicator.stringToProxy(f"device/{cleaner.id}:tcp -h 127.0.0.2 -p 10001"))

        for cleaner in device_summary2.cleaners:
            devices[cleaner.id] = Home_ice._M_Home.CleanerPrx.checkedCast(
                communicator.stringToProxy(f"device/{cleaner.id}:tcp -h 127.0.0.3 -p 10002"))

        for fridge in device_summary1.fridges:
            devices[fridge.id] = Home_ice._M_Home.FridgePrx.checkedCast(
                communicator.stringToProxy(f"device/{fridge.id}:tcp -h 127.0.0.2 -p 10001"))

        for fridge in device_summary2.fridges:
            devices[fridge.id] = Home_ice._M_Home.FridgePrx.checkedCast(
                communicator.stringToProxy(f"device/{fridge.id}:tcp -h 127.0.0.3 -p 10002"))
        try:
            while True:
                print("\nSelect a device ID to interact with:")
                print("Stoves: ", stove_ids)
                print("Fridges: ", fridge_ids)
                print("Cleaners: ", cleaner_ids)

                try:
                    choice = input("Enter device ID: ")
                    id = int(choice)
                except ValueError:
                    print("Invalid input. Please enter a valid number.")
                    time.sleep(1)
                    continue

                if id not in devices:
                    print("Invalid ID. Please select a valid device ID from the list.")
                    time.sleep(1)
                    continue

                device = devices[id]
                for controller in controllers:
                    device_type = controller.getDeviceType(id)

                    if device_type == Home_ice._M_Home.DeviceType.STOVE:
                        stove_handler(device, id)
                        break

                    elif device_type == Home_ice._M_Home.DeviceType.CLEANER:
                        cleaner_handler(device, id)
                        break

                    elif device_type == Home_ice._M_Home.DeviceType.FRIDGE:
                        fridge_handler(device, id)
                        break
                        
                else:
                    print("Unknown device ID. No matching controller found.")
                    time.sleep(1)

        except KeyboardInterrupt:
            print("\n👋 Exiting gracefully. Goodbye!")

if __name__ == "__main__":
    main()
