package com.makhir.designs.vm.service;

import java.util.List;

import com.makhir.designs.vm.entity.Coin;
import com.makhir.designs.vm.entity.Product;
import com.makhir.designs.vm.util.Bucket;

/**
 * This interface holds the operations for vending machine.
 * 
 * */
public interface IVendingMachineService {
	public long selectItemAndGetPrice(Product product);

	public void insertCoin(Coin coin);

	public List<Coin> refundAmount();

	public Bucket<Product, List<Coin>> collectProductAndRefund();

	public void reset();
	
	public long getTotalSales();
	
	public void printCurrentStatus();
}
