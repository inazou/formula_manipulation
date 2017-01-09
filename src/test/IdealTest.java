package test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import groebner_basis.Ideal;
import groebner_basis.Monomial;
import groebner_basis.Polynomial;
import groebner_basis.VariableComparator;
import groebner_basis.monomial_order.GrevlexOrder;
import groebner_basis.monomial_order.GrlexOrder;
import groebner_basis.monomial_order.LexOrder;

public class IdealTest {

    // {x^3-2x*y, x^2*y-2y^2+x} grlex => {x^2, x*y, y^2-1/2*x}
    private Ideal ideal1;

    // {x^3-2x*y, x^2*y-2y^2+x} lex => {x-2y^2, y^3}
    private Ideal ideal2;

    // {-t^2+z, t*y-z^2, t*z-y, x-z^2, y^2-z^3} lex => {t^2-z, t*y-z^2,
    // t*z-y, y^2-z^3}
    private Ideal ideal3;

    // {x*y-1, x^2+y^2-4} lex => {x+y^3-4y, y^4-4y^2+1}
    private Ideal ideal4;

    // {x*y-1, x^2+y^2-4} grevlex => {x+y^3-4y, y^4-4y^2+1}
    private Ideal ideal5;

    private Ideal ideal6;

    // {-x-y, 2x-1} grevlex => {x-1/2, y+1/2}
    private Ideal ideal7;

    // {-x-y, x} grevlex => {x, y}
    private Ideal ideal8;

    private Ideal ideal9;

    @Test
    public void testIdeal() {

    }

    public IdealTest() {
        VariableComparator variableComparator1 = new VariableComparator(new ArrayList<>(Arrays.asList("x", "y", "z")));
        Polynomial polynomial1 = new Polynomial(new ArrayList<>(Arrays.asList(new Monomial("x^3", variableComparator1), new Monomial("-2xy", variableComparator1))), new GrlexOrder());
        Polynomial polynomial2 = new Polynomial(new ArrayList<>(Arrays.asList(new Monomial("x^2y", variableComparator1), new Monomial("-2y^2", variableComparator1), new Monomial("x", variableComparator1))), new GrlexOrder());

        this.ideal1 = new Ideal(new ArrayList<>(Arrays.asList(polynomial1, polynomial2)));

        Polynomial polynomial3 = new Polynomial(new ArrayList<>(Arrays.asList(new Monomial("x^3", variableComparator1), new Monomial("-2xy", variableComparator1))), new LexOrder());
        Polynomial polynomial4 = new Polynomial(new ArrayList<>(Arrays.asList(new Monomial("x^2y", variableComparator1), new Monomial("-2y^2", variableComparator1), new Monomial("x", variableComparator1))), new LexOrder());

        this.ideal2 = new Ideal(new ArrayList<>(Arrays.asList(polynomial3, polynomial4)));

        VariableComparator variableComparator2 = new VariableComparator(new ArrayList<>(Arrays.asList("t", "x", "y", "z")));
        Polynomial polynomial5 = new Polynomial(new ArrayList<>(Arrays.asList(new Monomial("-t^2", variableComparator2), new Monomial("z", variableComparator2))), new LexOrder());
        Polynomial polynomial6 = new Polynomial(new ArrayList<>(Arrays.asList(new Monomial("ty", variableComparator2), new Monomial("-z^2", variableComparator2))), new LexOrder());
        Polynomial polynomial7 = new Polynomial(new ArrayList<>(Arrays.asList(new Monomial("tz", variableComparator2), new Monomial("-y", variableComparator2))), new LexOrder());
        Polynomial polynomial8 = new Polynomial(new ArrayList<>(Arrays.asList(new Monomial("x", variableComparator2), new Monomial("-z^2", variableComparator2))), new LexOrder());
        Polynomial polynomial9 = new Polynomial(new ArrayList<>(Arrays.asList(new Monomial("y^2", variableComparator2), new Monomial("-z^3", variableComparator2))), new LexOrder());

        this.ideal3 = new Ideal(new ArrayList<>(Arrays.asList(polynomial5, polynomial6, polynomial7, polynomial8, polynomial9)));

        Polynomial polynomial10 = new Polynomial(new ArrayList<>(Arrays.asList(new Monomial("xy", variableComparator1), new Monomial("-1", variableComparator1))), new LexOrder());
        Polynomial polynomial11 = new Polynomial(new ArrayList<>(Arrays.asList(new Monomial("x^2", variableComparator1), new Monomial("y^2", variableComparator1), new Monomial("-4", variableComparator1))), new LexOrder());

        this.ideal4 = new Ideal(new ArrayList<>(Arrays.asList(polynomial10, polynomial11)));

        Polynomial polynomial12 = new Polynomial(new ArrayList<>(Arrays.asList(new Monomial("x^2", variableComparator1), new Monomial("y^2", variableComparator1), new Monomial("z^2", variableComparator1), new Monomial("-1", variableComparator1))), new LexOrder());
        Polynomial polynomial13 = new Polynomial(new ArrayList<>(Arrays.asList(new Monomial("x^2", variableComparator1), new Monomial("z^2", variableComparator1), new Monomial("-y", variableComparator1))), new LexOrder());
        Polynomial polynomial14 = new Polynomial(new ArrayList<>(Arrays.asList(new Monomial("x", variableComparator1), new Monomial("-z", variableComparator1))), new LexOrder());

        this.ideal5 = new Ideal(new ArrayList<>(Arrays.asList(polynomial12, polynomial13, polynomial14)));

        VariableComparator variableComparator3 = new VariableComparator(new ArrayList<>(Arrays.asList("t", "u", "x", "y", "z")));
        Polynomial polynomial15 = new Polynomial("t+u-x", variableComparator3, new LexOrder());
        Polynomial polynomial16 = new Polynomial("t^2+2tu-y", variableComparator3, new LexOrder());
        Polynomial polynomial17 = new Polynomial("t^3+3t^2u-z", variableComparator3, new LexOrder());

        this.ideal6 = new Ideal(new ArrayList<>(Arrays.asList(polynomial15, polynomial16, polynomial17)));

        Polynomial polynomial18 = new Polynomial("-x-y", variableComparator1, new GrevlexOrder());
        Polynomial polynomial19 = new Polynomial("2x-1", variableComparator1, new GrevlexOrder());

        this.ideal7 = new Ideal(new ArrayList<>(Arrays.asList(polynomial18, polynomial19)));

        Polynomial polynomial20 = new Polynomial("-x-y", variableComparator1, new GrevlexOrder());
        Polynomial polynomial21 = new Polynomial("x", variableComparator1, new GrevlexOrder());

        this.ideal8 = new Ideal(new ArrayList<>(Arrays.asList(polynomial20, polynomial21)));

        Polynomial polynomial22 = new Polynomial("t^4-x", variableComparator2, new LexOrder());
        Polynomial polynomial23 = new Polynomial("t^3-y", variableComparator2, new LexOrder());
        Polynomial polynomial24 = new Polynomial("t^2-z", variableComparator2, new LexOrder());

        this.ideal9 = new Ideal(new ArrayList<>(Arrays.asList(polynomial22, polynomial23, polynomial24)));

    }

    @Test
    public void testGroebnerBasis() {
        List<Polynomial> groebnerBasis1 = this.ideal1.groebnerBasis();
        assertEquals(groebnerBasis1.size(), 3);
        assertEquals(groebnerBasis1.get(0).toString(), "x^2");
        assertEquals(groebnerBasis1.get(1).toString(), "xy");
        assertEquals(groebnerBasis1.get(2).toString(), "y^2-1/2x");

        List<Polynomial> groebnerBasis2 = this.ideal2.groebnerBasis();
        assertEquals(groebnerBasis2.size(), 2);
        assertEquals(groebnerBasis2.get(0).toString(), "x-2y^2");
        assertEquals(groebnerBasis2.get(1).toString(), "y^3");

        List<Polynomial> groebnerBasis3 = this.ideal3.groebnerBasis();
        assertEquals(groebnerBasis3.size(), 5);
        assertEquals(groebnerBasis3.get(0).toString(), "t^2-z");
        assertEquals(groebnerBasis3.get(1).toString(), "ty-z^2");
        assertEquals(groebnerBasis3.get(2).toString(), "tz-y");
        assertEquals(groebnerBasis3.get(3).toString(), "x-z^2");
        assertEquals(groebnerBasis3.get(4).toString(), "y^2-z^3");

        List<Polynomial> groebnerBasis4 = this.ideal4.groebnerBasis();
        assertEquals(groebnerBasis4.size(), 2);
        assertEquals(groebnerBasis4.get(0).toString(), "x+y^3-4y");
        assertEquals(groebnerBasis4.get(1).toString(), "y^4-4y^2+1");

        List<Polynomial> groebnerBasis5 = this.ideal5.groebnerBasis();
        assertEquals(groebnerBasis5.size(), 3);
        assertEquals(groebnerBasis5.get(0).toString(), "x-z");
        assertEquals(groebnerBasis5.get(1).toString(), "y-2z^2");
        assertEquals(groebnerBasis5.get(2).toString(), "z^4+1/2z^2-1/4");

        List<Polynomial> groebnerBasis6 = this.ideal6.groebnerBasis();
        VariableComparator variableComparator = new VariableComparator(new ArrayList<>(Arrays.asList("t", "u", "x", "y", "z")));
        assertEquals(groebnerBasis6.size(), 7);
        assertEquals(groebnerBasis6.get(0), new Polynomial("t+u-x", variableComparator, new LexOrder()));
        assertEquals(groebnerBasis6.get(1), new Polynomial("u^2-x^2+y", variableComparator, new LexOrder()));
        assertEquals(groebnerBasis6.get(2), new Polynomial("ux^2-x^3-uy+3/2xy-1/2z", variableComparator, new LexOrder()));
        assertEquals(groebnerBasis6.get(3), new Polynomial("uxy-x^2y+2y^2-uz-xz", variableComparator, new LexOrder()));
        assertEquals(groebnerBasis6.get(4), new Polynomial("-uy^2-1/2xy^2+uxz+x^2z-1/2yz", variableComparator, new LexOrder()));
        assertEquals(groebnerBasis6.get(5), new Polynomial("-3/4x^2y^2+y^3+x^3z-3/2xyz+1/4z^2", variableComparator, new LexOrder()));
        assertEquals(groebnerBasis6.get(6), new Polynomial("uy^3+1/2xy^3-2x^2yz+5/2y^2z-uz^2-xz^2", variableComparator, new LexOrder()));

        List<Polynomial> groebnerBasis7 = this.ideal7.groebnerBasis();
        assertEquals(groebnerBasis7.size(), 2);
        assertEquals(groebnerBasis7.get(0).toString(), "x-1/2");
        assertEquals(groebnerBasis7.get(1).toString(), "y+1/2");

        List<Polynomial> groebnerBasis8 = this.ideal8.groebnerBasis();
        assertEquals(groebnerBasis8.size(), 2);
        assertEquals(groebnerBasis8.get(0).toString(), "x");
        assertEquals(groebnerBasis8.get(1).toString(), "y");

        List<Polynomial> groebnerBasis9 = this.ideal9.groebnerBasis();
        assertEquals(groebnerBasis9.size(), 5);
        assertEquals(groebnerBasis9.get(0).toString(), "t^2-z");
        assertEquals(groebnerBasis9.get(1).toString(), "x-z^2");
        assertEquals(groebnerBasis9.get(2).toString(), "tz-y");
        assertEquals(groebnerBasis9.get(3).toString(), "y^2-z^3");
        assertEquals(groebnerBasis9.get(4).toString(), "ty-z^2");

    }

}
