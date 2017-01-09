package groebner_basis.monomial_order;

import groebner_basis.Monomial;

public class GrlexOrder extends MonomialOrder {

    @Override
    public int compare(Monomial o1, Monomial o2) {
        super.checkVariableComparetor(o1, o2);
        return super.grlex(o1, o2);
    }

}
