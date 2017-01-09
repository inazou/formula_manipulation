package test;

import java.util.ArrayList;
import java.util.Arrays;

import groebner_basis.Ideal;
import groebner_basis.Polynomial;
import groebner_basis.VariableComparator;
import groebner_basis.monomial_order.LexOrder;

public class Test {

    public static void main(String[] args) {

        VariableComparator variableComparator = new VariableComparator(new ArrayList<>(Arrays.asList("t", "x", "y", "z")));
        Polynomial polynomial1 = new Polynomial("t^4-x", variableComparator, new LexOrder());
        Polynomial polynomial2 = new Polynomial("t^3-y", variableComparator, new LexOrder());
        Polynomial polynomial3 = new Polynomial("t^2-z", variableComparator, new LexOrder());

        Ideal ideal = new Ideal(new ArrayList<>(Arrays.asList(polynomial1, polynomial2, polynomial3)));
        for (Polynomial polynomial : ideal.groebnerBasis()) {
            System.out.println(polynomial);
        }

    }

}
