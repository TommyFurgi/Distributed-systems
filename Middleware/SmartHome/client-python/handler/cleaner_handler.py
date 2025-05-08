from generated import Home_ice
import time

def cleaner_handler(obj, id):
    def print_help(id):
        print(f"\n🧼 Cleaner with id: {id}")
        print("Available commands for the vacuum cleaner:")
        print("- vacuum               ➤ Start cleaning")
        print("- get-battery-level    ➤ Show battery level (%)")
        print("- get-bag-level        ➤ Show dust bag fill level (%)")
        print("- return-to-base       ➤ Send the vacuum cleaner to base")
        print("- empty-bag            ➤ Empty the dust bag")
        print("- is-working           ➤ Check if the cleaner is currently working")
        print("- state                ➤ Check current cleaner status")
        print("- get-logs             ➤ Show cleaner activity logs")
        print("- exit                 ➤ Exit cleaner control mode\n")

    while True:
        print_help(id)
        op = input("==> ")

        if op == "vacuum":
            future = obj.vacuumRoomAsync(id)
            print(f"Vacuum cleaner with id {id} has started cleaning!")
     
            def on_complete(fut):
                try:
                    fut.result()
                    print(f"Vacuum cleaner with id {id} has finished the task!")
                except Home_ice._M_Home.DeviceInUseException as e:
                    print(f"Warning: {e.message}")
                except Home_ice._M_Home.DangerException as e:
                    print(f"Warning: {e.message}")
                 

            future.add_done_callback(on_complete)

        elif op == "get-battery-level":
            battery_level = obj.getBatteryLevel(id)
            print(f"Battery level: {battery_level}%")

        elif op == "get-bag-level":
            bag_level = obj.getBagLevel(id)
            print(f"Bag fill level: {bag_level}%")

        elif op == "return-to-base":
            future = obj.returnToBaseAsync(id)
            print(f"Vacuum cleaner with id {id} is being sent back to base!")
        
            def on_complete(fut):
                try:
                    fut.result()
                    print(f"Vacuum cleaner with id {id} has returned to base!")
                except Home_ice._M_Home.DeviceInUseException as e:
                    print(f"Warning: {e.message}")
                except Home_ice._M_Home.DangerException as e:
                    print(f"Warning: {e.message}")

            future.add_done_callback(on_complete)

        elif op == "empty-bag":

            future = obj.emptyBagAsync(id)
            print(f"Vacuum cleaner with id {id} has started emptying the bag!")
        
            def on_complete(fut):
                try:
                    fut.result()
                    print(f"Vacuum cleaner with id {id} has emptied the bag!")
                except Home_ice._M_Home.DeviceInUseException as e:
                    print(f"Warning: {e.message}")
                except Home_ice._M_Home.DangerException as e:
                    print(f"Warning: {e.message}")

            future.add_done_callback(on_complete)

        elif op == "is-working":
            is_working = obj.hasTask(id)
            print(f"Vacuum cleaner is currently working") if is_working else print("Vacuum cleaner is free") 
        
        elif op == "state":
            state = obj.checkState(id)
            print(f"Vacuum cleaner status: {state.message}")     

        elif op == "get-logs":
            logs = obj.getLogs(id)
            print(f"\n🧹 Vacuum Cleaner Logs (ID: {id}):\n" + "-" * 40)
            
            for log in logs:
                print(f"[{log.date}] {log.message}")
            
            print("-" * 40 + "\n")

        elif op == "exit":
            break
        
        else:
            print("???")
        
        time.sleep(1)