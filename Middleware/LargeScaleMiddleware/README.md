## Zadanie I3 - System middleware o dużej skali

Celem zadania jest przygotowanie infrastruktury middleware pozwalającej na na obsługę bardzo dużego ruchu (o skali przewyższającej możliwości jednej maszyny/procesu). W tym celu należy zadbać co najmniej o odpowiednie, przeźroczyste dla klientów routowanie i równoważenie ruchu wśród dostępnych w danym czasie usług, wielowątkowość przetwarzania, dla chętnych - mechanizmy backpressure. Należy przemyśleć i zrealizować różne strategie równoważenia obciążenia. 
Środowisko demonstracyjne może oczywiście działać na pojedynczym komputerze, ale liczba instancji serwerów powinna być spora (co najmniej pięć), eksponowane usługi powinny być opisane przynajmniej dwoma interfejsami IDL, a liczba ich instancji nie musi być ekstremalnie duża).  Kod aplikacji klienckiej powinien pozwolić sprawnie przetestować działanie systemu.

Demonstracja zadania: Omówienie interfejsów, omówienie infrastruktury, demonstracja działania systemu i przedstawienie wniosków (np. jak są obsługiwane wywołania strumieniowe w gRPC)?

gRPC: reverse-proxy (np. nginx).

Technologia middleware: gRPC
Języki programowania: dwa różne