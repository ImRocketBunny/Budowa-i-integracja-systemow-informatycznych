package b_Money;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AccountTest {
	Currency SEK, DKK;
	Bank Nordea;
	Bank DanskeBank;
	Bank SweBank;
	Account testAccount;
	
	@Before
	public void setUp() throws Exception {
		SEK = new Currency("SEK", 0.15);
		SweBank = new Bank("SweBank", SEK);
		SweBank.openAccount("Alice");
		testAccount = new Account("Hans", SEK);
		testAccount.deposit(new Money(10000000, SEK));
		SweBank.deposit("Alice", new Money(1000000, SEK));
	}
	
	@Test
	public void testAddRemoveTimedPayment() {
		assertFalse(testAccount.timedPaymentExists("id_01")); //sprawdzam czy metoda zwróci false przed dodaniem TimedPayment
		testAccount.addTimedPayment("id_01", 30, 0, new Money(10000, SEK), SweBank, "Alice"); //dodaję TimedPayment do konta testowego
		assertTrue(testAccount.timedPaymentExists("id_01")); //sprawdzam czy metoda zwróci true po dodaniu TimedPayment
		testAccount.removeTimedPayment("id_01"); //usuwam dodany TimedPayment
		assertFalse(testAccount.timedPaymentExists("rent")); ////sprawdzam czy metoda zwróci false po usunięciu TimedPayment
	}
	
	@Test
	public void testTimedPayment() throws AccountDoesNotExistException {
		/* przed zminami
			po uruchomieniu metody tick() odjęła się podwójna ilość pieniędzy względem wartość oczekiwanej
		 */
		assertEquals(Integer.valueOf(10000000), testAccount.getBalance().getAmount()); //sprawdzam początkowy stan konta testowego
		assertEquals(Integer.valueOf(1000000), SweBank.getBalance("Alice"));//sprawdzam początkowy stan konta Alice

		testAccount.addTimedPayment("id_01", 0, 0, new Money(100, SEK), SweBank, "Alice");
		assertTrue(testAccount.timedPaymentExists("id_01"));//sprawdzam czy metoda zwróci true po dodaniu TimedPayment
		testAccount.tick(); //uruchamiam pojedyńczy bieg czasu;
		assertEquals(Integer.valueOf(10000000 - 100),testAccount.getBalance().getAmount() ); //Sprawdzam czy stan konta testowego pomniejszył się o 1 przelew
		assertEquals(Integer.valueOf(1000000 + 100),SweBank.getBalance("Alice") ); //Sprawdzam czy stan konta Alice powiększył się się o 1  przelew
		for (int i = 0; i < 5; i++)
		{
			testAccount.tick(); //uruchamiam metodę tick() w pętli symulując kilkuktorne przesunięcie czasu;
		}

		assertEquals(Integer.valueOf(10000000 - 100-500),testAccount.getBalance().getAmount() ); //Sprawdzam czy stan konta testowego pomniejszył się o 5 przelewów
		assertEquals(Integer.valueOf(1000000 + 100+500),SweBank.getBalance("Alice") ); //Sprawdzam czy stan konta Alice powiększył się się o 5  przelewów
		testAccount.removeTimedPayment("id_01");
		testAccount.addTimedPayment("id_02", 0, 0, new Money(10000, SEK), SweBank, "Michał"); //dodaję TimedPayment dla nieistaniejącego konta
		assertTrue(testAccount.timedPaymentExists("id_02")); //sprawdzam czy istnieje dodany TimedPayment
		testAccount.tick(); //uruchamiam bieg czasu
		assertEquals(Integer.valueOf(10000000 - 100-500),testAccount.getBalance().getAmount() ); //sprawdzam czy stan konta pozstał ten sam


	}

	@Test
	public void testAddWithdraw() {
		testAccount.deposit(new Money(10000,SEK));//wpłacam 10000 SEK na konto testowe
		assertEquals(Integer.valueOf(10000000+10000),testAccount.getBalance().getAmount() ); //sprawdzam czy balans konta testowego powiększył się o wpłacocną ilość
		testAccount.withdraw(new Money(1000000, SEK)); //wypłacam 1000000 z konta testowego
		assertEquals(Integer.valueOf(10000000+10000-1000000),testAccount.getBalance().getAmount() ); //sprawdzam czy balans konta testowego pomniejszył się się o wypłacocną ilość

	}
	
	@Test
	public void testGetBalance() {
		assertTrue(new Money(10000000, SEK).equals(testAccount.getBalance())); //sprawdzam czy get balance zwróci tą samą wartość co pieniądze zainicjowane w metodzie setUp dla konta testowego

	}
}
