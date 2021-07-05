package b_Money;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class MoneyTest {
	Currency SEK, DKK, NOK, EUR;
	Money SEK100, EUR10, SEK200, EUR20, SEK0, EUR0, SEKn100;
	
	@Before
	public void setUp() throws Exception {
		SEK = new Currency("SEK", 0.15);
		DKK = new Currency("DKK", 0.20);
		EUR = new Currency("EUR", 1.5);
		SEK100 = new Money(10000, SEK);
		EUR10 = new Money(1000, EUR);
		SEK200 = new Money(20000, SEK);
		EUR20 = new Money(2000, EUR);
		SEK0 = new Money(0, SEK);
		EUR0 = new Money(0, EUR);
		SEKn100 = new Money(-10000, SEK);
	}

	@Test
	public void testGetAmount() {
		assertEquals(Integer.valueOf(10000), SEK100.getAmount()); //pobieram wartość i przyrównuję do wartości przepisanych z metody setUp
		assertEquals(Integer.valueOf(1000), EUR10.getAmount());
		assertEquals(Integer.valueOf(20000), SEK200.getAmount());
		assertEquals(Integer.valueOf(2000), EUR20.getAmount());
		assertEquals(Integer.valueOf(0), SEK0.getAmount());
		assertEquals(Integer.valueOf(0), EUR0.getAmount());
		assertEquals(Integer.valueOf(-10000), SEKn100.getAmount());
	}

	@Test
	public void testGetCurrency() {
		assertEquals(SEK, SEK100.getCurrency()); //pobieram wartość i przyrównuję do wartości przepisanych z metody setUp
		assertEquals(EUR, EUR10.getCurrency());
		assertEquals(SEK, SEK200.getCurrency());
		assertEquals(EUR, EUR20.getCurrency());
		assertEquals(SEK, SEK0.getCurrency());
		assertEquals(EUR, EUR0.getCurrency());
		assertEquals(SEK, SEKn100.getCurrency());
	}

	@Test
	public void testToString() {
		assertEquals("10000", SEK100.toString()); //pobieram wartość i przyrównuję do wartości String przepisanych z metody setUp
		assertEquals("1000", EUR10.toString());
		assertEquals("20000", SEK200.toString());
		assertEquals("2000", EUR20.toString());
		assertEquals("0", SEK0.toString());
		assertEquals("0", EUR0.toString());
		assertEquals("-10000", SEKn100.toString());
	}

	@Test
	public void testGlobalValue() {
		assertEquals(Integer.valueOf(1500), SEK100.universalValue()); //pobieram wartość i przyrównuję do wartości oczxekiwanych obliczonych kalkulatorem
		assertEquals(Integer.valueOf(1500), EUR10.universalValue());
		assertEquals(Integer.valueOf(3000), SEK200.universalValue());
		assertEquals(Integer.valueOf(3000), EUR20.universalValue());
		assertEquals(Integer.valueOf(0), SEK0.universalValue());
		assertEquals(Integer.valueOf(0), EUR0.universalValue());
		assertEquals(Integer.valueOf(-1500), SEKn100.universalValue());
	}

	@Test
	public void testEqualsMoney() {
		assertEquals(Boolean.TRUE, SEK100.equals(EUR10)); //pobieram wartość i przyrównuję do wartości oczekiwanych obliczonych kalkulatorem
		assertEquals(Boolean.FALSE, EUR10.equals(SEK200));
		assertEquals(Boolean.TRUE, SEK200.equals(EUR20));
		assertEquals(Boolean.FALSE, EUR20.equals(SEK0));
		assertEquals(Boolean.TRUE, SEK0.equals(EUR0));
		assertEquals(Boolean.FALSE, EUR0.equals(SEK100));

	}

	@Test
	public void testAdd() {
		//Test dodawania różnych walut do siebie, wartości oczekiwane wyliczone ręcznie kalkulatorem
		assertEquals(Integer.valueOf(20000), SEK100.add(EUR10).getAmount());
		assertEquals(Integer.valueOf(3000), EUR10.add(SEK200).getAmount());
		assertEquals(Integer.valueOf(40000), SEK200.add(EUR20).getAmount());
		assertEquals(Integer.valueOf(2000), EUR20.add(SEK0).getAmount());
		assertEquals(Integer.valueOf(0), SEK0.add(EUR0).getAmount());
		assertEquals(Integer.valueOf(1000), EUR0.add(SEK100).getAmount());
	}

	@Test
	public void testSub() {
		//Test odejmowania różnych walut do siebie, wartości oczekiwane wyliczone ręcznie kalkulatorem
		assertEquals(Integer.valueOf(0), SEK100.sub(EUR10).getAmount());
		assertEquals(Integer.valueOf(10000), SEK200.sub(EUR10).getAmount());
		assertEquals(Integer.valueOf(0), SEK200.sub(EUR20).getAmount());
		assertEquals(Integer.valueOf(2000), EUR20.sub(SEK0).getAmount());
		assertEquals(Integer.valueOf(0), SEK0.sub(EUR0).getAmount());

	}

	@Test
	public void testIsZero() {
		assertEquals(Boolean.FALSE, SEK100.isZero());
		assertEquals(Boolean.FALSE, EUR10.isZero());
		assertEquals(Boolean.FALSE, SEK200.isZero());
		assertEquals(Boolean.FALSE, EUR20.isZero());
		assertEquals(Boolean.TRUE, SEK0.isZero());
		assertEquals(Boolean.TRUE, EUR0.isZero());
	}

	@Test
	public void testNegate() {
		assertEquals(Integer.valueOf(-10000), SEK100.negate().getAmount());  //uruchamiam metodę negującą i przyróenuję otrzymaną wartość na  zanegowanych wartości ustalonych w metodzie setUp
		assertEquals(Integer.valueOf(-1000), EUR10.negate().getAmount());
		assertEquals(Integer.valueOf(-20000), SEK200.negate().getAmount());
		assertEquals(Integer.valueOf(-2000), EUR20.negate().getAmount());
		assertEquals(Integer.valueOf(-0), SEK0.negate().getAmount());
		assertEquals(Integer.valueOf(-0), EUR0.negate().getAmount());
	}

	@Test
	public void testCompareTo() {
		assertEquals(0, SEK100.compareTo(EUR10)); //uruchamiam metodę compare to i przyrównuję jej wynik do wartości oczekiwanej wyliczonej z pomocą kalkulatora
		assertEquals(-1, EUR10.compareTo(SEK200));
		assertEquals(0, SEK200.compareTo(EUR20));
		assertEquals(1, EUR20.compareTo(SEK0));
		assertEquals(0, SEK0.compareTo(EUR0));
		assertEquals(-1, EUR0.compareTo(SEK200));
	}
}
