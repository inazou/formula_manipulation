package groebner_basis.monomial_order;

import groebner_basis.Monomial;

public class GrevlexOrder extends MonomialOrder {

    @Override
    public int compare(Monomial o1, Monomial o2) {
        super.checkVariableComparetor(o1, o2);
        return super.grevlex(o1, o2);
    }

}
