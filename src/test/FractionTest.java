package test;

import static org.junit.Assert.assertEquals;

import java.math.BigInteger;

import org.junit.Test;

import groebner_basis.Fraction;

public class FractionTest {

    // 1/2
    private Fraction fraction1;
    // 0
    private Fraction fraction2;
    // 1
    private Fraction fraction3;
    // 0
    private Fraction fraction4;

    @Test(expected = Error.class)
    public void testFractionBigIntegerBigInteger() {
        new Fraction(new BigInteger("5"), new BigInteger("0"));
        new Fraction(new BigInteger("0"), new BigInteger("0"));
    }

    @Test(expected = Error.class)
    public void testFractionString() {
        new Fraction("1/0");
        new Fraction("x");
        new Fraction("^");
    }

    public FractionTest() {
        this.fraction1 = new Fraction(new BigInteger("5"), new BigInteger("10"));
        this.fraction2 = new Fraction("0");
        this.fraction3 = new Fraction("1");
        this.fraction4 = new Fraction("0/57");
    }

    @Test
    public void testToString() {
        assertEquals(this.fraction1.toString(), "1/2");
        assertEquals(this.fraction2.toString(), "0");
        assertEquals(this.fraction3.toString(), "1");
        assertEquals(this.fraction2.toString(), this.fraction4.toString());
    }

    @Test
    public void testIsOne() {
        assertEquals(this.fraction1.isOne(), false);
        assertEquals(this.fraction2.isOne(), false);
        assertEquals(this.fraction3.isOne(), true);
    }

    @Test
    public void testIsZero() {
        assertEquals(this.fraction1.isZero(), false);
        assertEquals(this.fraction2.isZero(), true);
        assertEquals(this.fraction3.isZero(), false);
    }

    @Test
    public void testGetNumerator() {
        assertEquals(this.fraction1.getNumerator(), new BigInteger("1"));
        assertEquals(this.fraction2.getNumerator(), new BigInteger("0"));
        assertEquals(this.fraction3.getNumerator(), new BigInteger("1"));
        assertEquals(this.fraction4.getNumerator(), new BigInteger("0"));
    }

    @Test
    public void testGetDenominator() {
        assertEquals(this.fraction1.getDenominator(), new BigInteger("2"));
        assertEquals(this.fraction2.getDenominator(), new BigInteger("1"));
        assertEquals(this.fraction3.getDenominator(), new BigInteger("1"));
        assertEquals(this.fraction3.getDenominator(), new BigInteger("1"));
    }

    @Test
    public void testNegate() {
        assertEquals(this.fraction1.negate().toString(), "-1/2");
        assertEquals(this.fraction2.negate().toString(), "0");
        assertEquals(this.fraction3.negate().toString(), "-1");
        assertEquals(this.fraction2.negate(), new Fraction("0"));
    }

    @Test
    public void testAbs() {
        assertEquals(this.fraction1.abs().toString(), "1/2");
        assertEquals(this.fraction2.abs().toString(), "0");
        assertEquals(this.fraction3.abs().toString(), "1");
        assertEquals(this.fraction4.abs(), new Fraction("0"));
    }

    @Test
    public void testReciprocal() {
        assertEquals(this.fraction1.reciprocal(), new Fraction("2"));
        assertEquals(this.fraction3.reciprocal(), new Fraction("1"));
    }

    @Test(expected = Error.class)
    public void testReciprocalError() {
        this.fraction2.reciprocal();
    }

    @Test
    public void testAdd() {
        assertEquals(this.fraction1.add(this.fraction2).toString(), "1/2");
        assertEquals(this.fraction2.add(this.fraction2).toString(), "0");
        assertEquals(this.fraction1.add(this.fraction3), this.fraction3.add(this.fraction1));
        assertEquals(this.fraction3.add(this.fraction1).toString(), "3/2");
    }

    @Test
    public void testSubtract() {
        assertEquals(this.fraction1.subtract(this.fraction2).toString(), "1/2");
        assertEquals(this.fraction2.subtract(this.fraction2).toString(), "0");
        assertEquals(this.fraction1.subtract(this.fraction3).toString(), "-1/2");
        assertEquals(this.fraction3.subtract(this.fraction1).toString(), "1/2");
        assertEquals(this.fraction3.subtract(this.fraction1).toString(), this.fraction3.subtract(this.fraction1).toString());
    }

    @Test
    public void testMultiply() {

    }

    @Test
    public void testDivide() {

    }

    @Test
    public void testSignum() {

    }

}
