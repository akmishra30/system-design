package vm.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.stream.IntStream;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mahkir.designs.vm.exception.ProductSoldOutException;
import com.makhir.designs.vm.entity.Coin;
import com.makhir.designs.vm.entity.Product;
import com.makhir.designs.vm.service.IVendingMachineService;
import com.makhir.designs.vm.service.VendingMachineService;
import com.makhir.designs.vm.util.Bucket;

/**
 * This is the test class which test all the possible vending machine usage test cases.
 * */
public class VendingMachineTestCases {
	
	private static final Logger log = LoggerFactory.getLogger(VendingMachineTestCases.class);
	private static IVendingMachineService vendingService;
	
	@BeforeClass
	public static void setUp(){
		log.info("## Setting up neccessory components.");
		vendingService = new VendingMachineService();
	}
	
	@AfterClass
	public static void clear(){
		vendingService = null;
		log.info("## Cleared all the initialized components.");
	}
	
	@Test
	public void testBuyWithCorrectPrice(){
		log.info("buyWithCorrectPrice: Inserted exact amount to buy the product.");
		long price = vendingService.selectItemAndGetPrice(Product.FENTA);
		assertEquals(Product.FENTA.getPrice(), price);
		vendingService.insertCoin(Coin.QUARTER);
		
		Bucket<Product, List<Coin>> bucket = vendingService.collectProductAndRefund();
		
		assertEquals(Product.FENTA, bucket.getFirst());
		assertTrue(bucket.getSecond().isEmpty());
		log.info("buyWithCorrectPrice test case success.");
	}
	
	@Test
	public void testBuyWithMorePrice(){
		log.info("buyWithMorePrice: Inserted more amount to buy the product.");
		long price = vendingService.selectItemAndGetPrice(Product.FENTA);
		assertEquals(Product.FENTA.getPrice(), price);
		vendingService.insertCoin(Coin.HALF);
		
		Bucket<Product, List<Coin>> bucket = vendingService.collectProductAndRefund();
		
		assertEquals(Product.FENTA, bucket.getFirst());
		assertFalse(bucket.getSecond().isEmpty());
		
		log.info("buyWithMorePrice test case success.");
	}
	
	@Test
	public void testRefund(){
		log.info("testRefund.");
		long price = vendingService.selectItemAndGetPrice(Product.PEPSI);
		assertEquals(Product.PEPSI.getPrice(), price);
		
		vendingService.insertCoin(Coin.DIME);
		vendingService.insertCoin(Coin.NICKLE);
		vendingService.insertCoin(Coin.PENNY);
		vendingService.insertCoin(Coin.QUARTER);
		
		assertEquals(41, calculateTotal(vendingService.refundAmount()));

		log.info("testRefund test case success.");
	}
	
	@Ignore
	private long calculateTotal(List<Coin> changes){
		long total = changes.stream().mapToLong(f -> f.getValue()).sum();
		log.debug("## total: {}", total);
		return total;
	}
	
}
