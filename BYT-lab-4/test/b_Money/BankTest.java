package b_Money;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class BankTest {
	Currency SEK, DKK;
	Bank SweBank, Nordea, DanskeBank;
	
	@Before
	public void setUp() throws Exception {
		DKK = new Currency("DKK", 0.20);
		SEK = new Currency("SEK", 0.15);
		SweBank = new Bank("SweBank", SEK);
		Nordea = new Bank("Nordea", SEK);
		DanskeBank = new Bank("DanskeBank", DKK);
		SweBank.openAccount("Ulrika");
		SweBank.openAccount("Bob");
		Nordea.openAccount("Bob");
		DanskeBank.openAccount("Gertrud");
	}

	@Test
	public void testGetName() {
		assertEquals("SweBank", SweBank.getName()); //pobieram nazwę banku i sprawdzam czy odpowiada znawie wcześniej przypisanej
		assertEquals("Nordea", Nordea.getName());
		assertEquals("DanskeBank", DanskeBank.getName());

	}

	@Test
	public void testGetCurrency() {
		assertEquals(SEK, SweBank.getCurrency()); //pobieram Currency Banku i sprawdzam czy odpowiada obiektowi Currency, który został mu wcześniej przypisany
		assertEquals(SEK, Nordea.getCurrency());
		assertEquals(DKK, DanskeBank.getCurrency());
	}

	@Test
	public void testOpenAccount() throws AccountExistsException, AccountDoesNotExistException {
		boolean bool = false;
		try {
			SweBank.openAccount("Ulrika"); //konto istnieje test Exception,  wartośc bool ulega zmianie na true
			fail("błąd");
		}catch (AccountExistsException e){
			bool=true;
		}
		assertTrue(bool);//sprawdzenie czy wartość bool wynosi true
		bool=false;
		try {
			DanskeBank.openAccount("Michał"); //konto nie istnieje wartość bool nie powinna ulec zmianie
		}catch (AccountExistsException e){
			bool=true;
		}
		assertFalse(bool); //sprawdzenie czy wartośc bool wynosi false
		assertEquals(Integer.valueOf(0),DanskeBank.getBalance("Michał")); //jeśli konto się utworzyło jego balans powinien wymosic 0
	}

	@Test
	public void testDeposit() throws AccountDoesNotExistException {
		boolean bool = false;
		try {
			SweBank.deposit("Ulrika",new Money(10000000, DKK)); //konto istnieje wartość bool nie powinna ulec zmianie a pieniądze dodac do konta
		}catch (AccountDoesNotExistException e){
			bool=true;
		}
		assertFalse(bool);//sprawdzam czy wartość bool wynosi false
		assertEquals(Integer.valueOf(13333333),SweBank.getBalance("Ulrika")); //sprawdzam czy stan konta Ulriki się powiększył
		bool = false;
		try {
			SweBank.deposit("Gertrud",new Money(10000000, SEK)); //konto nie istnieje wartość bool powinna ulec zmianie
		}catch (AccountDoesNotExistException e){
			bool=true;
		}
		assertTrue(bool); //sprawdzam czy wartość bool wynoasi true

	}

	@Test
	public void testWithdraw() throws AccountDoesNotExistException {
		boolean bool = false;
		try {
			SweBank.deposit("Ulrika",new Money(10000000, DKK)); //wpłacam pieniądze na konto "Ulrika
			SweBank.withdraw("Ulrika",new Money(500000,SEK)); // wypłacam część pieniędzy
		}catch (AccountDoesNotExistException e){
			bool=true;
		}
		assertFalse(bool);
		assertEquals(Integer.valueOf(12833333),SweBank.getBalance("Ulrika")); //sprawdzam czy stan konta Urlika pomniejszył się o ilość wypłaconych pieniędzy
		bool = false;
		try {
			SweBank.deposit("Gertrud",new Money(10000000, SEK)); //konto nie istnieje, test Exception
		}catch (AccountDoesNotExistException e){
			bool=true;
		}
		assertTrue(bool);
	}
	
	@Test
	public void testGetBalance() throws AccountDoesNotExistException {
		SweBank.deposit("Ulrika",new Money(10000000, SEK)); //do konta Ulrika dodaję pieniądze
		assertEquals(Integer.valueOf(10000000),SweBank.getBalance("Ulrika")); //pobieram balans konta Ulrika i sprawdzam czy wynosi tyle ile dodałem
		Nordea.deposit("Bob",new Money(1000000,DKK)); //to samo co powyżej dla konta Bob w banku Nordea
		assertEquals(Integer.valueOf(1333333),Nordea.getBalance("Bob"));
	}
	
	@Test
	public void testTransfer() throws AccountDoesNotExistException {
		/*przed zmianami w metidzie transfer dla tego samego banku
			Balans konta Ulrika oraz Bob nie uległ zmianie (fromaccount w miejscu toaccount)
		 */
		SweBank.deposit("Ulrika",new Money(10000000, SEK)); //do konta Ulrika dodaję pieniądze
		SweBank.transfer("Ulrika","Bob",new Money(500000,SEK)); //wykonuję transfer z konta ulrika na konto Bob w tym samym Banku
		assertEquals(Integer.valueOf(500000),SweBank.getBalance("Bob")); //sprawdzam czy balans konta Bob wynosi wartośc przelewu Ulriki
		assertEquals(Integer.valueOf(10000000-500000),SweBank.getBalance("Ulrika")); //Sprawdzam czy balans konta Ulrika został pomniejszony o wartośc przelwu

		SweBank.transfer("Ulrika",DanskeBank,"Gertrud",new Money(1000,DKK)); //wykonuję przelew z konta Ulrika z banku Swe na Konto "Gertud banku Danske
		assertEquals(Integer.valueOf(1000),DanskeBank.getBalance("Gertrud")); //sprawdzam czy balans konta Getrund wynosi wartość przelewu od Ulriki
		assertEquals(Integer.valueOf(9498667),SweBank.getBalance("Ulrika"));	//Sprawdzam czy balans konta Ulrika został pomniejszony o wartośc przelwu, obliczenia balansu wykonane ręcznie
	}
	
	@Test
	public void testTimedPayment() throws AccountDoesNotExistException {
		/* przed zmianami w klasie Bank
		addTimetPayment nie zwraca wyjątku AccountDoesNotExistException
		removeTimetPayment nie zwraca wyjątku AccountDoesNotExistException
		dodano obsługe wyjątku do obu metod
		 */
		try {
			SweBank.addTimedPayment("Michał","id_01",30,50,new Money(500,SEK),Nordea,"Bob"); //dpdaję TimedPayment do nieistniejącego konta
			fail("test failed");
		}catch (AccountDoesNotExistException e){

		}

		try {
			SweBank.removeTimedPayment("Michał","id_01"); //odejmuję TimedPayment do nieistniejącego konta
			fail("test failed");
		}catch (AccountDoesNotExistException e){

		}

		SweBank.addTimedPayment("Ulrika","id_01",30,50,new Money(500,SEK),Nordea,"Bob");
		SweBank.removeTimedPayment("Ulrika","id_01");

	}
}
