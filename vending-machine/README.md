# Vending Machine
This project is simply to demonstrate the vending machine basic operations.

**Required Tech-spec :**

	1. Java (v1.8 or later)
	3. Eclipse 
	4. Maven 

**Classes, interfaces, entities and exceptions :**

	 - IVendingMachineService : Interface with basic vending machine's operations
	 - VendingMachineService: Implementation of vending machine's operations
	 - Inventory: This is type-safe placeholder for vending machine objects
	 - Product: Type-safe enum for representing supported products in vending machine
	 - Coin: Type-safe enum for representing supported coins in vending machine
	 - Bucket: Vending machine objects holder together - Coin and Product
	 - LessPaidAmountException: Vending machine throws an exception when less amount inserted
	 - NotSufficientChangeException: Vending machine throws an exception when there less change to return
	 - ProductSoldOutException: Vending machine throws an exception when there is no product available to serve


 **Basic Operations**
 
    - selectItemAndGetPrice
    - insertCoin
    - refundAmount
    - collectProductAndRefund
    - reset
    - getTotalSales
    - printCurrentStatus