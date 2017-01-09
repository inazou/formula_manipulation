package groebner_basis.monomial_order;

import java.math.BigInteger;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;

import groebner_basis.Monomial;

abstract public class MonomialOrder implements Comparator<Monomial> {

    @Override
    abstract public int compare(Monomial o1, Monomial o2);

    /**
     * 引数の変数の順序が一致しているかチェック
     * 
     * @param o1
     * @param o2
     */
    protected void checkVariableComparetor(Monomial o1, Monomial o2) {
        if (!o1.getVariableComparator().equals(o2.getVariableComparator()))
            throw new Error("VariableComparatorNotMatchError");
    }

    /**
     * 引数をlex順序で比較する
     * 
     * @param o1
     * @param o2
     * @return 1,0,-1
     */
    protected int lex(Monomial o1, Monomial o2) {
        List<String> variableOrder = o1.getVariableComparator().getVariableOrder();
        for (String variable : variableOrder) {
            switch (o1.getVariableExponent(variable).subtract(o2.getVariableExponent(variable)).compareTo(BigInteger.ZERO)) {
            case 1:
                // o1が大きかった時
                return -1;
            case -1:
                // o2が大きかった時
                return 1;
            }
        }
        return 0;
    }

    /**
     * 引数をgrlex順序で比較する
     * 
     * @param o1
     * @param o2
     * @return 1,0,-1
     */
    protected int grlex(Monomial o1, Monomial o2) {
        int compared = this.compareDegree(o1, o2);
        if (compared != 0) return compared;

        return this.lex(o1, o2);
    }

    protected int grevlex(Monomial o1, Monomial o2) {
        int compared = this.compareDegree(o1, o2);
        if (compared != 0) return compared;

        List<String> variableOrder = o1.getVariableComparator().getVariableOrder();
        // 逆順で回す
        for (ListIterator<String> variableIterator = variableOrder.listIterator(variableOrder.size()); variableIterator.hasPrevious();) {
            String variable = variableIterator.previous();
            switch (o1.getVariableExponent(variable).subtract(o2.getVariableExponent(variable)).compareTo(BigInteger.ZERO)) {
            case 1:
                // o2が大きかった時
                return 1;
            case -1:
                // o1が大きかった時
                return -1;
            }
        }
        return 0;
    }

    /**
     * 引数の次数を比較する
     * 
     * @param o1
     * @param o2
     * @return 1,-1,0
     */
    private int compareDegree(Monomial o1, Monomial o2) {
        switch (o1.degree().subtract(o2.degree()).compareTo(BigInteger.ZERO)) {
        case 1:
            // o1が大きかった時
            return -1;
        case -1:
            // o2が大きかった時
            return 1;
        default:
            return 0;
        }
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        return true;
    }

}
