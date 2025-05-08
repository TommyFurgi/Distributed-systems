from generated import Home_ice
import time

def fridge_handler(obj, id):
    def print_help(id):
        print(f"\n🧊 Fridge with id: {id}")
        print("Available commands for the fridge:")
        print("- get-temp            ➤ Show current temperature")
        print("- set-temp            ➤ Set a new temperature")
        print("- EcoMode-on          ➤ Enable EcoMode")
        print("- EcoMode-off         ➤ Disable EcoMode")
        print("- get-energy-usage    ➤ Show current energy usage (%)")
        print("- get-history         ➤ Show temperature history")
        print("- state               ➤ Show fridge status")
        print("- exit                ➤ Exit fridge control mode\n")

    while True:
        print_help(id)
        op = input("==> ")

        if op == "get-temp":
            temp = obj.getCurrentTemperature(id)
            print(f"Current Temperature: {temp}°C")
            
        elif op == "set-temp":
            try:
                new_temp = float(input("Enter the new temperature: "))
                obj.changeTemperature(id, new_temp)
                print(f"New temperature set to: {new_temp}°C")
                
            except ValueError:
                print("Please enter a valid number.")
            except Home_ice._M_Home.InvalidArgumentException as e:
                print(f"Error: {e.message}")

        elif op == "EcoMode-on":
            obj.setEcoMode(id, True)
            print(f"EcoMode enabled")

        elif op == "EcoMode-off":
            obj.setEcoMode(id, False)
            print(f"EcoMode disabled")
        
        elif op == "get-energy-usage":
            usage = obj.getEnergyUsage(id)
            print(f"Energy usage: {usage}%")

        elif op == "get-history":
            history = obj.getTemperatureHistory(id)
            print(f"Temperature history: {history}")

        elif op == "state":
            state = obj.checkState(id)
            print(f"Fridge status: {state.message}")

        elif op == "exit":
            break
        
        else:
            print("???")

        time.sleep(1)
