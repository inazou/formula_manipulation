package test.demonstration;

import java.util.ArrayList;
import java.util.Arrays;

import groebner_basis.Monomial;
import groebner_basis.VariableComparator;

public class MonomialDemonstration {

    public static void main(String[] args) {

        VariableComparator variableComparator = new VariableComparator(new ArrayList<>(Arrays.asList("x", "y")));
        Monomial monomial1 = new Monomial("1/2xy", variableComparator);
        Monomial monomial2 = new Monomial("xy", variableComparator);

        System.out.println("加算 : " + monomial1.add(monomial2));
        System.out.println("減算 : " + monomial1.subtract(monomial2));
        System.out.println("乗算 : " + monomial1.multiply(monomial2));
        System.out.println("除算 : " + monomial1.divide(monomial2));

    }

}
