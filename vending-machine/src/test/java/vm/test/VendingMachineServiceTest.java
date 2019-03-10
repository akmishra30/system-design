package vm.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
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
 * This is the unit test cases for all the vending machine operation.
 * 
 * */
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class VendingMachineServiceTest {
	private static final Logger log = LoggerFactory.getLogger(VendingMachineServiceTest.class);
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
	public void testSelectItemAndGetPrice(){
		log.info("## Testing selectItemAndGetPrice");
		long price = vendingService.selectItemAndGetPrice(Product.COKE);
		log.info("## Returned price: {}", price);
		assertEquals(Product.COKE.getPrice(), price);
		log.info("## Test case pass for selectItemAndGetPrice");
		
	}
	
	@Test
	public void testRefundAmount(){
		log.info("## Testing refundAmount");
		vendingService.insertCoin(Coin.HALF);
		List<Coin> coins = vendingService.refundAmount();
		log.info("## Returned coins: {}", coins);
		assertEquals(Coin.HALF, coins.get(0).HALF);
		log.info("## Test case pass for refundAmount");
	}
	
	@Test
	public void testCollectProductAndRefund(){
		log.info("## Testing CollectProductAndRefund");
		try {
			vendingService.insertCoin(Coin.HALF);
			vendingService.selectItemAndGetPrice(Product.SEVENUP);
			Bucket<Product, List<Coin>> prodRefund = vendingService.collectProductAndRefund();
			log.info("## product refund : {}", prodRefund);
			log.info("## productName : {}" , prodRefund.getFirst().getName());
			
			assertEquals(prodRefund.getFirst(), Product.SEVENUP); //Because inserted half coin and selected product is sevenUp
			assertTrue(prodRefund.getSecond().size() > 0); //Because sevenUp cost to 35, hence expecting 15 cent in refund, 10 and 5
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testInsertCoin(){
		log.info("## Testing insertCoin");
		vendingService.insertCoin(Coin.HALF);  // I have inserted half dollar hence expected half dollar because didn't choose any product.
		
		List<Coin> refund = vendingService.refundAmount();
		
		log.info("## refund amount: {}", refund);
		assertTrue(refund.size() > 0);
		assertEquals(refund.get(0), Coin.HALF);
	}
	
	@Test
	public void testTotalSalesAndPrintStatus(){
		log.info("## Testing TotalSales");
		vendingService.insertCoin(Coin.HALF);  // I have inserted half dollar hence expected half dollar because didn't choose any product.
		List<Coin> refund = vendingService.refundAmount();
		log.info("## refund amount: {}", refund);
		assertTrue(refund.size() > 0);
		assertEquals(refund.get(0), Coin.HALF);
		Long amount = vendingService.getTotalSales();
		log.info("## Total sales: {}", amount);
		assertNotNull(amount);
		vendingService.printCurrentStatus();
	}
	
	@Test(expected = ProductSoldOutException.class)
	public void testReset(){
		log.info("## Testing reset");
		IVendingMachineService vms = new VendingMachineService();
		vms.insertCoin(Coin.HALF);  // I have inserted half dollar hence expected half dollar because didn't choose any product.
		List<Coin> refund = vms.refundAmount();
		log.info("## refund amount: {}", refund);
		vms.reset();
		vms.selectItemAndGetPrice(Product.COKE);
	}
}
