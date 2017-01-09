package test.demonstration;

import java.util.ArrayList;
import java.util.Arrays;

import groebner_basis.Polynomial;
import groebner_basis.VariableComparator;
import groebner_basis.monomial_order.LexOrder;

public class PolynomialDemonstration {

    public static void main(String[] args) {

        VariableComparator variableComparator = new VariableComparator(new ArrayList<>(Arrays.asList("x", "y")));
        Polynomial polynomial1 = new Polynomial("x-y", variableComparator, new LexOrder());
        Polynomial polynomial2 = new Polynomial("x+y", variableComparator, new LexOrder());

        System.out.println("加算 : " + polynomial1.add(polynomial2));
        System.out.println("減算 : " + polynomial1.subtract(polynomial2));
        System.out.println("乗算 : " + polynomial1.multiply(polynomial2));
        System.out.println("除算 : " + polynomial1.divide(polynomial2));
        System.out.println("余剰 : " + polynomial1.remainder(polynomial2));

    }

}
