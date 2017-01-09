package groebner_basis;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import groebner_basis.monomial_order.MonomialOrder;

public class Polynomial {

    /**
     * 単項式のリスト
     */
    private LinkedList<Monomial> monomials = new LinkedList<>();

    /**
     * 単項式の順序
     */
    private MonomialOrder monomialOrder;

    /**
     * 変数の順序を管理する
     */
    private VariableComparator variableComparator;

    /**
     * コンストラクタ
     * 
     * @param monomials
     * @param monomialOrder
     */
    public Polynomial(List<Monomial> monomials, MonomialOrder monomialOrder) {

        // 空ならエラー
        if (monomials.isEmpty()) throw new Error("EmptyMonomialsError");
        // 指定された単項式順序で並べ替え
        monomials.sort(monomialOrder);

        // 変数と指数が同じならたす
        for (Monomial monomial : monomials) {
            // 何も入っていなかったらmonomialと変数の順序を入れる
            if (this.monomials.isEmpty()) {
                this.monomials.add(monomial);
                this.variableComparator = monomial.getVariableComparator();
                continue;
            }

            // 前に入っているものと変数・指数が一致するか
            if (this.monomials.getLast().isEqualVariables(monomial)) monomial = this.monomials.pollLast().add(monomial);

            // 0なら入れない
            if (monomial.isZero()) continue;

            this.monomials.add(monomial);
        }
        // 何も入っていなかったら0を入れる
        if (this.monomials.isEmpty()) this.monomials.add(new Monomial("0", this.variableComparator));

        this.monomialOrder = monomialOrder;
    }

    /**
     * コンストラクタ
     * 
     * @param monomial
     * @param monomialOrder
     */
    public Polynomial(Monomial monomial, MonomialOrder monomialOrder) {
        this(new ArrayList<>(Arrays.asList(monomial)), monomialOrder);
    }

    /**
     * コンストラクタ
     * 
     * @param val -2x^3y^2-x^4+2, +4/5xy, x^3*y*z^1, x^5, 2+x, 2*xy,
     *        xyのような形を受け付ける (少数は非対応)
     * @param variablesOrder 変数の順序
     */
    public Polynomial(String val, VariableComparator variableComparator, MonomialOrder monomialOrder) {
        this(stringToMonomials(val, variableComparator), monomialOrder);
    }

    /**
     * 文字列を+-で区切って Monomial のリストを生成して返す
     * 
     * @param val
     * @param variableComparator
     * @return Monomial のリスト
     */
    private static List<Monomial> stringToMonomials(String val, VariableComparator variableComparator) {
        String[] split = val.split("(?=[+-])");
        ArrayList<Monomial> monomials = new ArrayList<>();
        for (String value : split) {
            monomials.add(new Monomial(value, variableComparator));
        }
        return monomials;
    }

    /**
     * 多項式を文字列として取得
     * 
     * @return 多項式の文字列
     */
    @Override
    public String toString() {
        StringBuilder value = new StringBuilder();
        for (Monomial monomial : this.monomials) {
            if (!this.monomials.getFirst().equals(monomial) && monomial.signum() != -1) value.append("+");
            value.append(monomial);
        }

        return value.toString();
    }

    /**
     * 値が (this + val) である Polynomial を返す
     * 
     * @param val
     * @return this + val
     */
    public Polynomial add(Polynomial val) {
        // 順序が一致するかチェック
        if (!(this.monomialOrder.getClass() == val.monomialOrder.getClass()))
            throw new Error("monomialOrderNotMatchError");
        if (!this.variableComparator.equals(val.variableComparator)) throw new Error("VariableComparatorNotMatchError");
        LinkedList<Monomial> monomials = new LinkedList<>(this.monomials);
        monomials.addAll(val.monomials);

        return new Polynomial(monomials, this.monomialOrder);
    }

    /**
     * 値が (-this) の Polynomial を返す
     * 
     * @return -this
     */
    public Polynomial negate() {
        LinkedList<Monomial> monomials = new LinkedList<>();
        for (Monomial monomial : this.monomials) {
            monomials.add(monomial.negate());
        }
        return new Polynomial(monomials, this.monomialOrder);
    }

    /**
     * 値が (this - val) である Polynomial を返す
     * 
     * @param val
     * @return this - val
     */
    public Polynomial subtract(Polynomial val) {
        return this.add(val.negate());
    }

    /**
     * 値が (this * val) である Polynomial を返す
     * 
     * @param val
     * @return this * val
     */
    public Polynomial multiply(Polynomial val) {
        // 順序が一致するかチェック
        this.equalsMonomialOrderAndVariableComparator(val);

        LinkedList<Monomial> monomials = new LinkedList<>();
        for (Monomial valMonomial : val.monomials) {
            for (Monomial monomial : this.monomials) {
                monomials.add(monomial.multiply(valMonomial));
            }
        }

        return new Polynomial(monomials, this.monomialOrder);
    }

    /**
     * 値が (this / vals) である Polynomial の List を返す
     * 
     * @param vals
     * @return this / vals
     */
    public List<Polynomial> divide(List<Polynomial> vals) {
        return this.divideAndRemainder(vals).get(0);
    }

    /**
     * 値が (this % vals) である Polynomial の List を返す
     * 
     * @param vals
     * @return this % vals
     */
    public Polynomial remainder(List<Polynomial> vals) {
        return this.divideAndRemainder(vals).get(1).get(0);
    }

    /**
     * (this / vals) そして (this % vals) と続く、2 つの Polynomial の List を返す
     * 
     * @param vals
     * @return 2 つの Polynomial の List。商 (this / vals) は最初の要素で、剰余 (this % vals)
     *         は最後の要素
     */
    public List<List<Polynomial>> divideAndRemainder(List<Polynomial> vals) {
        if (vals.isEmpty()) throw new Error("zeroDivideError");
        // 順序が一致するかチェック
        this.equalsMonomialOrderAndVariableComparator(vals.get(0));

        int size = vals.size();
        List<Polynomial> divideResult = new ArrayList<>(size);
        List<Polynomial> remainderResult = new ArrayList<>(1);
        // 商初期化
        for (int i = 0; i < size; i++) {
            divideResult.add(new Polynomial(new Monomial("0", this.variableComparator), this.monomialOrder));
        }
        // 余り初期化
        remainderResult.add(new Polynomial(new Monomial("0", this.variableComparator), this.monomialOrder));

        Polynomial divided = new Polynomial(this.monomials, this.monomialOrder);
        while (!divided.isZero()) {
            boolean divideFlag = false;
            Monomial dividedLeadingHead = divided.leadingTerm();
            for (int i = 0; i < size; i++) {
                List<Monomial> divideAndRemainder = dividedLeadingHead.divideAndRemainder(vals.get(i).leadingTerm());
                // 割り切れた時
                if (divideAndRemainder.get(1).isZero()) {
                    Polynomial quotient = new Polynomial(divideAndRemainder.get(0), this.monomialOrder);

                    divideResult.set(i, divideResult.get(i).add(quotient));
                    divided = divided.subtract(vals.get(i).multiply(quotient));
                    divideFlag = true;
                    break;
                }
            }
            // 割れなかったので余りとする
            if (!divideFlag) {
                Polynomial polynomialDividedLeadingHead = new Polynomial(dividedLeadingHead, this.monomialOrder);
                remainderResult.set(0, remainderResult.get(0).add(polynomialDividedLeadingHead));
                divided = divided.subtract(polynomialDividedLeadingHead);
            }
        }

        return new ArrayList<List<Polynomial>>(Arrays.asList(divideResult, remainderResult));

    }

    /**
     * 値が (this / val) である Polynomial を返す
     * 
     * @param val
     * @return this / val
     */
    public Polynomial divide(Polynomial val) {
        return this.divideAndRemainder(val).get(0);
    }

    /**
     * 値が (this % val) である Polynomial を返す
     * 
     * @param val
     * @return this % val
     */
    public Polynomial remainder(Polynomial val) {
        return this.divideAndRemainder(val).get(1);
    }

    /**
     * (this / val) そして (this % val) と続く、2 つの Polynomial の List を返す
     * 
     * @param val
     * @return 2 つの Polynomial の List。商 (this / val) は最初の要素で、剰余 (this % val)
     *         は最後の要素
     */
    public List<Polynomial> divideAndRemainder(Polynomial val) {
        List<List<Polynomial>> divideAndRemainder = this.divideAndRemainder(new ArrayList<>(Arrays.asList(val)));
        Polynomial divideResult = divideAndRemainder.get(0).get(0);
        Polynomial remainderResult = divideAndRemainder.get(1).get(0);
        return new ArrayList<Polynomial>(Arrays.asList(divideResult, remainderResult));
    }

    /**
     * 0かどうか
     * 
     * @return 0ならtrue
     */
    public boolean isZero() {
        return (this.monomials.size() == 1 && this.leadingTerm().isZero());
    }

    /**
     * this と val の monomialOrder と variableComparator が一致するか
     * 
     * @param val
     * @return 一致すればtrue 一致しなければmonomialOrderNotMatchError |
     *         VariableComparatorNotMatchError
     */
    private boolean equalsMonomialOrderAndVariableComparator(Polynomial val) {
        if (!(this.monomialOrder.equals(val.monomialOrder))) throw new Error("monomialOrderNotMatchError");
        if (!this.variableComparator.equals(val.variableComparator)) throw new Error("VariableComparatorNotMatchError");

        return true;
    }

    /**
     * この Polynomial の先頭項を返す
     * 
     * @return この Polynomial の先頭項
     */
    public Monomial leadingTerm() {
        return this.monomials.getFirst();
    }

    /**
     * この Polynomial の多重次数を返す
     * 
     * @return この Polynomial の多重次数
     */
    public List<BigInteger> multiDegree() {
        return this.monomials.getFirst().multiDegree();
    }

    /**
     * この Polynomial の先頭係数を返す
     * 
     * @return この Polynomial の先頭係数
     */
    public Fraction leadingCoefficient() {
        return this.monomials.getFirst().coefficient();
    }

    /**
     * この Polynomial の先頭変数を返す
     * 
     * @return この Polynomial の先頭変数
     */
    public LinkedHashMap<String, BigInteger> leadingVariables() {
        return this.monomials.getFirst().variables();
    }

    /**
     * this と val のS多項式を返す
     * 
     * @param val
     * @return this と val のS多項式
     */
    public Polynomial sPolynomial(Polynomial val) {
        // 順序が一致するかチェック
        this.equalsMonomialOrderAndVariableComparator(val);

        Polynomial leastCommonMultiple = new Polynomial(this.leadingTerm().leastCommonMultiple(val.leadingTerm()), this.monomialOrder);

        return leastCommonMultiple.divide(new Polynomial(this.leadingTerm(), this.monomialOrder)).multiply(this).subtract(leastCommonMultiple.divide(new Polynomial(val.leadingTerm(), val.monomialOrder)).multiply(val));
    }

    /**
     * この Polynomial の単項式の順序を返す
     */
    public MonomialOrder getMonomialOrder() {
        return this.monomialOrder;
    }

    /**
     * この Polynomial の変数の順序を返す
     */
    public VariableComparator getVariableComparator() {
        return this.variableComparator;
    }

    /**
     * この Polynomial が変数を含んでいるかどうか
     * 
     * @return 含んでいないならtrue
     */
    public boolean isEmptyVariables() {
        return this.leadingTerm().isEmptyVariables();
    }

    /**
     * 変数を含む単項式のみの多項式を返す
     * 
     * @return 変数を含む単項式のみの多項式
     */
    public Polynomial getHasVariablesPolynomial() {
        ArrayList<Monomial> HaveVariablesMonomials = new ArrayList<>();
        for (Monomial monomial : this.monomials) {
            if (!monomial.isEmptyVariables()) {
                HaveVariablesMonomials.add(monomial);
            }
        }
        return new Polynomial(HaveVariablesMonomials, this.monomialOrder);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((monomialOrder == null) ? 0 : monomialOrder.hashCode());
        result = prime * result + ((monomials == null) ? 0 : monomials.hashCode());
        result = prime * result + ((variableComparator == null) ? 0 : variableComparator.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof Polynomial)) return false;
        Polynomial other = (Polynomial) obj;
        if (monomialOrder == null) {
            if (other.monomialOrder != null) return false;
        } else if (!monomialOrder.equals(other.monomialOrder)) return false;
        if (monomials == null) {
            if (other.monomials != null) return false;
        } else if (!monomials.equals(other.monomials)) return false;
        if (variableComparator == null) {
            if (other.variableComparator != null) return false;
        } else if (!variableComparator.equals(other.variableComparator)) return false;
        return true;
    }

}
