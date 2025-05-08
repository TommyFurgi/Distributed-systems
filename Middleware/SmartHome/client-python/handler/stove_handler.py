from generated import Home_ice
import time

def stove_handler(obj, id):
    def print_help(id):
        print(f"\n🔥 Stove with id: {id}")
        print("Available commands for the stove:")
        print("- heat-water        ➤ Start heating water to a target temperature")
        print("- get-heat          ➤ Show current heat level (°C)")
        print("- is-working        ➤ Check if the stove is currently working")
        print("- set-heat          ➤ Manually set the stove temperature")
        print("- get-type          ➤ Display stove type")
        print("- state             ➤ Show stove status message")
        print("- exit              ➤ Exit stove control mode\n")

    while True:
        print_help(id)
        op = input("==> ")

        if op == "heat-water":
            try:
                water_temp = float(input("Enter temperature: "))
                future = obj.heatWaterAsync(id, water_temp)
                
            except ValueError:
                print("Please enter a valid number.")
            
            def on_complete(fut, temp=water_temp):
                try:
                    fut.result()
                    print(f"Water heated to {temp} degrees!")
                except Home_ice._M_Home.DeviceInUseException as e:
                    print(f"Warning: {e.message}")
                except Home_ice._M_Home.InvalidArgumentException as e:
                    print(f"Error: {e.message}")
                except Home_ice._M_Home.DangerException as e:
                    print(f"Warning: {e.message}")

            future.add_done_callback(on_complete)
            print(f"Stove with id {id} has started heating water!")
        
        elif op == "get-heat":
            heat = obj.getHeatLevel(id)
            print(f"Current temperature: {heat}°C")

        elif op == "is-working":
            is_working = obj.isWorking(id)
            print(f"The stove is working now") if is_working else print("The stove is not working now") 
        
        elif op == "set-heat":
            try:
                new_heat = float(input("Enter new temperature: "))
                obj.setHeatLevel(id, new_heat)
                print(f"New temperature set to: {new_heat}°C")

            except ValueError:
                print("Please enter a valid number.")
            except Home_ice._M_Home.DeviceInUseException as e:
                print(f"Warning: {e.message}")
            except Home_ice._M_Home.InvalidArgumentException as e:
                print(f"Error: {e.message}")
            except Home_ice._M_Home.DangerException as e:
                print(f"Warning: {e.message}")

        elif op == "get-type":
            stove_type = obj.getStoveType(id)
            print(f"Stove type: {stove_type}")     

        elif op == "state":
            state = obj.checkState(id)
            print(f"Stove status: {state.message}")     

        elif op == "exit":
            break

        else:
            print("???")

        time.sleep(1)