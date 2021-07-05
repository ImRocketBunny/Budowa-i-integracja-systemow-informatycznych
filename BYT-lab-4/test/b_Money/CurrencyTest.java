package b_Money;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CurrencyTest {
	Currency SEK, DKK, NOK, EUR;
	
	@Before
	public void setUp() throws Exception {
		/* Setup currencies with exchange rates */
		SEK = new Currency("SEK", 0.15);
		DKK = new Currency("DKK", 0.20);
		EUR = new Currency("EUR", 1.5);
	}

	@Test
	public void testGetName() {
		assertEquals("SEK", SEK.getName()); //przyrównuje pobraną nazwę z nazwą nadaną w metodzie setUp
		assertEquals("DKK", DKK.getName());
		assertEquals("EUR", EUR.getName());
	}
	
	@Test
	public void testGetRate() {
		assertEquals( Double.valueOf(0.15), SEK.getRate());//przyrównuje pobrany rate z rate nadanym w metodzie setUp
		assertEquals( Double.valueOf(0.20), DKK.getRate());
		assertEquals( Double.valueOf(1.5), EUR.getRate());
	}
	
	@Test
	public void testSetRate() {
		SEK.setRate(0.30); //zmieniam rate dla SEK na 0.30
		assertEquals( Double.valueOf(0.30), SEK.getRate()); //sprawdzam czy nowa wartość rate wynosi 0.30
		DKK.setRate(0.40);
		assertEquals( Double.valueOf(0.40), DKK.getRate());
		EUR.setRate(3.0);
		assertEquals(Double.valueOf(3.0), EUR.getRate());
	}
	
	@Test
	public void testGlobalValue() {
		assertEquals(Integer.valueOf(7),SEK.universalValue(50)); //uruchamiam przeliczenie waluty i przyrównuję je do wartości oczekiwanej wyliczonych kalkulatorem
		assertEquals(Integer.valueOf(10),DKK.universalValue(50));
		assertEquals(Integer.valueOf(75),EUR.universalValue(50));
	}
	
	@Test
	public void testValueInThisCurrency() {
		assertEquals(Integer.valueOf(35),SEK.valueInThisCurrency(50,DKK)); //uruchamiam przeliczenie waluty i przyrównuję je do wartości oczekiwanej wyliczonych kalkulatorem
		assertEquals(Integer.valueOf(6),DKK.valueInThisCurrency(50,EUR));
		assertEquals(Integer.valueOf(500 ),EUR.valueInThisCurrency(50,SEK));
	}

}
