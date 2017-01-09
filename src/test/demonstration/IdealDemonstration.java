package test.demonstration;

import java.util.ArrayList;
import java.util.Arrays;

import groebner_basis.Ideal;
import groebner_basis.Polynomial;
import groebner_basis.VariableComparator;
import groebner_basis.monomial_order.LexOrder;

public class IdealDemonstration {

    public static void main(String[] args) {

        VariableComparator variableComparator = new VariableComparator(new ArrayList<>(Arrays.asList("x", "y", "z")));

        Polynomial polynomial1 = new Polynomial("x^2+y^2+z^2-1", variableComparator, new LexOrder());
        Polynomial polynomial2 = new Polynomial("x^2+z^2-y", variableComparator, new LexOrder());
        Polynomial polynomial3 = new Polynomial("x-z", variableComparator, new LexOrder());

        Ideal ideal = new Ideal(new ArrayList<>(Arrays.asList(polynomial1, polynomial2, polynomial3)));
        for (Polynomial polynomial : ideal.groebnerBasis()) {
            System.out.println(polynomial);
        }
    }

}
