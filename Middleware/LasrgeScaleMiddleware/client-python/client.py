import grpc
import gen.math_pb2 as math_pb2
import gen.math_pb2_grpc as math_pb2_grpc
import gen.stat_pb2 as stat_pb2
import gen.stat_pb2_grpc as stat_pb2_grpc
from concurrent.futures import ThreadPoolExecutor
import time
from random import randint, choice, sample

def spam_large_perfect_number_requests():
    error_counter = 0
    unavailables = 0
    total_requests = 10000
    large_numbers = [10**5, 10**6, 10**7, 10**8, 10**9]

    with ThreadPoolExecutor(max_workers=total_requests) as executor:
        tasks = []

        def make_large_request(request_num):
            nonlocal error_counter
            nonlocal unavailables
            with grpc.insecure_channel('localhost:50051') as channel:
                math_stub = math_pb2_grpc.MathServiceStub(channel)
                max_retries = 5
                retry_delay = 1

                for attempt in range(max_retries):
                    try:
                        number = choice(large_numbers)
                        print(f"Sending large perfect number request {request_num} with number {number}...")
                        response = math_stub.IsPerfectNumber(math_pb2.NumberRequest(number=number))
                        print(f"Result {request_num}: Number {number}, is perfect: {response.is_perfect}")
                        break
                    except grpc.RpcError as e:
                        if e.code() == grpc.StatusCode.UNAVAILABLE:
                            print(f"Request {request_num} failed (Attempt {attempt + 1}): {e.code()}. Retrying...")
                            time.sleep(retry_delay * (2 ** attempt))
                            unavailables += 1
                        else:
                            print(f"Request {request_num} failed: {e.code()}, {e.details()}")
                            break
                else:
                    print(f"Request {request_num} completely failed after retries.")
                    error_counter += 1

        for i in range(1, total_requests + 1):
            tasks.append(executor.submit(make_large_request, i))

        for task in tasks:
            task.result()

    print(f"Total requests: {total_requests}, Final errors: {error_counter}, Server was unavailable: {unavailables} times")


def make_request(request_num):
    with grpc.insecure_channel('localhost:50051') as channel:
        math_stub = math_pb2_grpc.MathServiceStub(channel)
        stat_stub = stat_pb2_grpc.StatServiceStub(channel)

        tasks = ["Gcd", "Average", "Median", "Perfect Number"]
        task = choice(tasks)

        max_retries = 10
        retry_delay = 1  
        for attempt in range(max_retries):
            try:
                print(f"Sending request {request_num} to task {task}...")
                
                if task == "Gcd":
                    a, b = randint(1, 1000), randint(1, 1000)
                    response = math_stub.Gcd(math_pb2.GcdRequest(a=a, b=b))
                    print(f"Result {request_num}, task: GCD({a}, {b}) = {response.gcd}")

                elif task == "Average":
                    values = sample(range(1, 100), k=randint(5, 10))
                    response = stat_stub.Average(stat_pb2.AverageRequest(numbers=values))
                    print(f"Result {request_num}, task: Average of {values} = {response.result:.2f}")

                elif task == "Median":
                    values = sample(range(1, 100), k=randint(5, 10))
                    response = stat_stub.Median(stat_pb2.MedianRequest(numbers=values))
                    print(f"Result {request_num}, task: Median of {values} = {response.result:.2f}")

                elif task == "Perfect Number":
                    a = randint(1, 1000)
                    response = math_stub.IsPerfectNumber (math_pb2.NumberRequest(number=a))
                    print(f"Result {request_num}, task: is Perfect Number {a} = {response.is_perfect}")

                break

            except grpc.RpcError as e:
                if e.code() == grpc.StatusCode.UNAVAILABLE:
                    print(f"Request {request_num} failed (Attempt {attempt + 1}): {e.code()}, {e.details()}. Retrying...")
                    time.sleep(retry_delay * (2 ** attempt))  # Backoff, 1s, 2s, 4s...
                else:
                    print(f"Request {request_num} failed: {e.code()}, {e.details()}")
                    break

        else:
            print(f"Request {request_num} failed after {max_retries} attempts.")


def spam_requests():
    with ThreadPoolExecutor(max_workers=30) as executor:
        tasks = []

        for i in range(1, 31):
            tasks.append(executor.submit(make_request, i))
            time.sleep(0.8)

        for task in tasks:
            task.result()

if __name__ == '__main__':
    print("Starting client...")
    spam_requests()
    # spam_large_perfect_number_requests()
