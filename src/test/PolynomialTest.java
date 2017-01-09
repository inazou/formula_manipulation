package test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import groebner_basis.Monomial;
import groebner_basis.Polynomial;
import groebner_basis.VariableComparator;
import groebner_basis.monomial_order.GrlexOrder;
import groebner_basis.monomial_order.LexOrder;

public class PolynomialTest {

    // x > y > z
    private VariableComparator variableComparator1;

    // x > y > z
    private VariableComparator variableComparator2;

    // x^3*y^2-x^2*y^3+x grex
    private Polynomial polynomial1;

    // 3x^4*y+y^2 grex
    private Polynomial polynomial2;

    // x^2*y+xy^2+y^2 lex
    private Polynomial polynomial3;
    // x*y-1 lex
    private Polynomial polynomial4;
    // y^2-1 lex
    private Polynomial polynomial5;

    @Test
    public void testPolynomialListOfMonomialMonomialOrder() {

    }

    @Test
    public void testPolynomialMonomialMonomialOrder() {

    }

    public PolynomialTest() {
        this.variableComparator1 = new VariableComparator(new ArrayList<>(Arrays.asList("x", "y", "z")));
        this.variableComparator2 = new VariableComparator(new ArrayList<>(Arrays.asList("x", "y", "z")));

        this.polynomial1 = new Polynomial(new ArrayList<>(Arrays.asList(new Monomial("x^3y^2", this.variableComparator1), new Monomial("-x^2y^3", variableComparator1), new Monomial("x", variableComparator1))), new GrlexOrder());
        this.polynomial2 = new Polynomial(new ArrayList<>(Arrays.asList(new Monomial("3x^4y", this.variableComparator2), new Monomial("y^2", variableComparator1))), new GrlexOrder());

        this.polynomial3 = new Polynomial(new ArrayList<>(Arrays.asList(new Monomial("x^2y", this.variableComparator1), new Monomial("xy^2", this.variableComparator2), new Monomial("y^2", this.variableComparator1))), new LexOrder());
        this.polynomial4 = new Polynomial(new ArrayList<>(Arrays.asList(new Monomial("xy", this.variableComparator1), new Monomial("-1", this.variableComparator2))), new LexOrder());
        this.polynomial5 = new Polynomial(new ArrayList<>(Arrays.asList(new Monomial("y^2", this.variableComparator2), new Monomial("-1", this.variableComparator2))), new LexOrder());
    }

    @Test
    public void testToString() {

    }

    @Test
    public void testAdd() {

    }

    @Test
    public void testNegate() {

    }

    @Test
    public void testSubtract() {

    }

    @Test
    public void testMultiply() {

    }

    @Test
    public void testDivideListOfPolynomial() {

    }

    @Test
    public void testRemainderListOfPolynomial() {

    }

    @Test
    public void testDivideAndRemainderListOfPolynomial() {
        List<List<Polynomial>> divideAndRemainder1 = this.polynomial3.divideAndRemainder(new ArrayList<>(Arrays.asList(this.polynomial4, this.polynomial5)));
        assertEquals(divideAndRemainder1.size(), 2);
        assertEquals(divideAndRemainder1.get(0).size(), 2);
        assertEquals(divideAndRemainder1.get(1).size(), 1);
        assertEquals(divideAndRemainder1.get(0).get(0).toString(), "x+y");
        assertEquals(divideAndRemainder1.get(0).get(1).toString(), "1");
        assertEquals(divideAndRemainder1.get(1).get(0).toString(), "x+y+1");

        List<List<Polynomial>> divideAndRemainder2 = this.polynomial3.divideAndRemainder(new ArrayList<>(Arrays.asList(this.polynomial5, this.polynomial4)));
        assertEquals(divideAndRemainder2.size(), 2);
        assertEquals(divideAndRemainder2.get(0).size(), 2);
        assertEquals(divideAndRemainder2.get(1).size(), 1);
        assertEquals(divideAndRemainder2.get(0).get(0).toString(), "x+1");
        assertEquals(divideAndRemainder2.get(0).get(1).toString(), "x");
        assertEquals(divideAndRemainder2.get(1).get(0).toString(), "2x+1");
    }

    @Test
    public void testDividePolynomial() {

    }

    @Test
    public void testRemainderPolynomial() {

    }

    @Test
    public void testDivideAndRemainderPolynomial() {

    }

    @Test
    public void testIsZero() {

    }

    @Test
    public void testLeadingTerm() {

    }

    @Test
    public void testMultiDegree() {

    }

    @Test
    public void testLeadingCoefficient() {

    }

    @Test
    public void testLeadingVariables() {

    }

    @Test
    public void testSPolynomial() {
        assertEquals(this.polynomial1.sPolynomial(this.polynomial2).toString(), "-x^3y^3-1/3y^3+x^2");
    }

}
