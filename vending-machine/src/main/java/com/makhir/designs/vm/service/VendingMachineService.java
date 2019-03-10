package com.makhir.designs.vm.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mahkir.designs.vm.data.Inventory;
import com.mahkir.designs.vm.exception.LessPaidAmountException;
import com.mahkir.designs.vm.exception.NotSufficientChangeException;
import com.mahkir.designs.vm.exception.ProductSoldOutException;
import com.makhir.designs.vm.entity.Coin;
import com.makhir.designs.vm.entity.Product;
import com.makhir.designs.vm.util.Bucket;

/**
 * This is the implementation class for all the operation related to vending
 * machine.
 */
public class VendingMachineService implements IVendingMachineService {
	private static final Logger log = LoggerFactory.getLogger(VendingMachineService.class);

	private Inventory<Product> productInventory = new Inventory<Product>();
	private Inventory<Coin> coinsInventory = new Inventory<Coin>();

	private long totalSales;
	private Product currentProduct;
	private long currentBalance;

	public VendingMachineService() {
		initializeInventory();
	}

	/**
	 * This method initialize the inventory with some quantity of products and
	 * coins.
	 * 
	 */
	private void initializeInventory() {
		log.info("Enter: initializeInventory");
		for (Coin c : Coin.values())
			coinsInventory.put(c, 5);

		for (Product p : Product.values())
			productInventory.put(p, 5);

		log.info("Exit: initializeInventory");
	}

	@Override
	public long selectItemAndGetPrice(Product product) {
		log.info("Enter: selectItemAndGetPrice");
		if (productInventory.hasItem(product)) {
			currentProduct = product;
			log.info("Exit: selectItemAndGetPrice");
			return currentProduct.getPrice();
		}

		throw new ProductSoldOutException("Sold Out, Please buy another item");
	}

	@Override
	public void insertCoin(Coin coin) {
		log.info("Enter: insertCoin");
		log.info("## inserted coin : {}", coin);
		currentBalance = currentBalance + coin.getValue();
		log.info("## currentBalance : {}", currentBalance);
		coinsInventory.add(coin);
	}

	/**
	 * This method returns remaining coins once amount got deducted based on selected product.
	 * */
	private List<Coin> collectCoins(){
		long changeAmount = currentBalance - currentProduct.getPrice();
		List<Coin> change = getChange(changeAmount);
		updateCoinsInventory(change);
		currentBalance = 0;
		currentProduct = null;
		log.info("## Returned coins: {}", change);
		return change;
	}
	
	@Override
	public List<Coin> refundAmount() {
		log.info("Enter: refundAmount");
		List<Coin> refund = getChange(currentBalance);
		updateCoinsInventory(refund);
		currentBalance = 0;
		currentProduct = null;
		log.info("## Returned refund: {}", refund);
		return refund;
	}
	
	/**
	 * This method updates the coins inventory
	 * */
	private void updateCoinsInventory(List<Coin> change){
		log.info("## Updating coins inventory: {}", change);
		for(Coin c : change)
			coinsInventory.add(c);
	}

	@Override
	public Bucket<Product, List<Coin>> collectProductAndRefund() {
		
		Product product = collectProduct();
		
		log.info("## Collected product: {}", product);
		
		totalSales = totalSales  + currentProduct.getPrice();
		
		log.info("## totalSales: {}", totalSales);
		
		List<Coin> change = collectCoins();
		
		log.info("## change: {}", totalSales);
		
		return new Bucket<Product, List<Coin>>(product, change);
	}
	
	private Product collectProduct() throws NotSufficientChangeException, LessPaidAmountException{
		if(isFullyPaid()){
			log.info("## Amount is fully paid: {}", currentBalance);
			if(hasSufficientChange()){
				productInventory.deduct(currentProduct);
				return currentProduct;
			} else {
				log.error("## There is no sufficient change to return.");
				throw new NotSufficientChangeException("Not sufficient change in inventory.");
			}
		} else {
			long remainingAmount = currentProduct.getPrice() - currentBalance;
			log.error("## The paid amount is less. remaining amount : {}", remainingAmount);
			throw new LessPaidAmountException("Paid price is less, remaining :", remainingAmount);
		}
	}

	@Override
	public void reset() {
		log.info("Clearing vending machines inventory.");
		productInventory.clear(); 
		coinsInventory.clear(); 
		totalSales = 0; 
		currentProduct = null; 
		currentBalance = 0;
	}

	/**
	 * This will return the change after comparing the products with given
	 * amount.
	 */
	private List<Coin> getChange(long amount) throws NotSufficientChangeException {
		log.info("Enter: getChange");
		List<Coin> changes = Collections.EMPTY_LIST;
		log.info("## amount : {}", amount);
		if (amount > 0) {
			changes = new ArrayList<>();
			long balance = amount;

			while (balance > 0) {
				if (balance >= Coin.HALF.getValue() && coinsInventory.hasItem(Coin.HALF)) {
					changes.add(Coin.HALF);
					balance = balance - Coin.HALF.getValue();
					continue;
				} else if (balance >= Coin.QUARTER.getValue() && coinsInventory.hasItem(Coin.QUARTER)) {
					changes.add(Coin.QUARTER);
					balance = balance - Coin.QUARTER.getValue();
					continue;
				} else if (balance >= Coin.DIME.getValue() && coinsInventory.hasItem(Coin.DIME)) {
					changes.add(Coin.DIME);
					balance = balance - Coin.DIME.getValue();
					continue;
				} else if (balance >= Coin.NICKLE.getValue() && coinsInventory.hasItem(Coin.NICKLE)) {
					changes.add(Coin.NICKLE);
					balance = balance - Coin.NICKLE.getValue();
					continue;
				} else if (balance >= Coin.PENNY.getValue() && coinsInventory.hasItem(Coin.PENNY)) {
					changes.add(Coin.PENNY);
					balance = balance - Coin.PENNY.getValue();
					continue;
				} else {
					log.error("Not sufficient change.");
					throw new NotSufficientChangeException("Not sufficient change. Please try with another product.");
				}
			}

		}
		log.info("### Returned change: {}", changes);
		log.info("Exit: getChange");
		return changes;
	}

	/**
	 * This method returns the total sales.
	 * */
	@Override
	public long getTotalSales(){
		log.info("## Current total sales: {}", totalSales);
		return totalSales;
	}
	
	/**
	 * Returns true or false based on the current change.
	 * */
	private boolean hasSufficientChange(){
		return hasSufficientChangeForAmount();
	}
	
	/**
	 * Returns true or false based on the current change.
	 * */
	private boolean hasSufficientChangeForAmount(){
		boolean hasAmount = true;
		try{
			long amount = currentBalance - currentProduct.getPrice();
			log.info("## Amount to check: {}", amount);
			getChange(amount);
		} catch(Exception e){
			hasAmount = false;
		}
		
		log.info("## has change: {}", hasAmount);
		
		return hasAmount;
	}
	
	/**
	 * Prints the current vending machine status.
	 * */
	public void printCurrentStatus(){
		log.info("## Total sales: {}", totalSales);
		log.info("## Current sales inventory: {}", productInventory.toString());
		log.info("## Current sales inventory: {}", coinsInventory.toString());
		log.info("## Current balance: {}", currentBalance);
	}
	
	/**
	 * 
	 * */
	private boolean isFullyPaid(){
		if(currentBalance >= currentProduct.getPrice())
			return true;
		else
			return false;
	}
	
}
