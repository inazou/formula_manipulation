package test.demonstration;

import groebner_basis.Fraction;

public class FractionDemonstration {

    public static void main(String[] args) {

        Fraction fraction1 = new Fraction("1/4");
        Fraction fraction2 = new Fraction("1/2");

        System.out.println("加算 : " + fraction1.add(fraction2));
        System.out.println("減算 : " + fraction1.subtract(fraction2));
        System.out.println("乗算 : " + fraction1.multiply(fraction2));
        System.out.println("除算 : " + fraction1.divide(fraction2));

    }

}
