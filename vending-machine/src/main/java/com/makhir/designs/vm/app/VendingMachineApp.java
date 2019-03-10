package com.makhir.designs.vm.app;

import com.makhir.designs.vm.service.IVendingMachineService;
import com.makhir.designs.vm.service.VendingMachineService;

/**
 * This class is the main class for vending machine. This provides the command
 * line interface to all the operation of vending machine.
 * 
 */
public class VendingMachineApp {
	
	private IVendingMachineService vendingMachine;
	
	public VendingMachineApp(){
		vendingMachine = new VendingMachineService();
	}
	
	public static void main(String[] args) {
		
	}
}
