package groebner_basis;

import java.util.AbstractMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

public class Ideal {

    /**
     * 基底となる多項式のリスト
     */
    private LinkedList<Polynomial> basicPolynomials;

    /**
     * コンストラクタ
     * 
     * @param basicPolynomials
     */
    public Ideal(List<Polynomial> basicPolynomials) {

        // 空ならエラー
        if (basicPolynomials.isEmpty()) throw new Error("BasicPolynomialsError");

        this.basicPolynomials = new LinkedList<>(basicPolynomials);
    }

    /**
     * この Ideal のグレブナ基底を返す
     * 
     * @return この Ideal のグレブナ基底
     */
    public List<Polynomial> groebnerBasis() {
        // グレブナ基底の計算
        List<Polynomial> groebnerBasis = this.upgradeGroebnerBasisMethod();
        // 簡約する
        return this.reduceGroebnerBasis(groebnerBasis);
    }

    /**
     * この Ideal のグレブナ基底を返す (基本のアルゴリズムで計算)
     * 
     * @return この Ideal のグレブナ基底
     */
    public List<Polynomial> basicGroebnerBasis() {
        // グレブナ基底の計算
        List<Polynomial> groebnerBasis = this.basicGroebnerBasisMethod();
        // 簡約する
        return this.reduceGroebnerBasis(groebnerBasis);
    }

    /**
     * 引数のグレブナ基底を簡約する
     * 
     * @param groebnerBasis
     * @return 簡約したグレブナ基底
     */
    private List<Polynomial> reduceGroebnerBasis(List<Polynomial> groebnerBasis) {
        LinkedList<Polynomial> reducedGroebnerBasis = new LinkedList<>();
        // 先頭項の係数を1に揃え、他に割られなかったものだけを入れる
        for (int i = 0; i < groebnerBasis.size(); i++) {
            Polynomial polynomial = groebnerBasis.get(i);
            Fraction leadingCoefficient = polynomial.leadingCoefficient();
            if (!leadingCoefficient.isOne())
                groebnerBasis.set(i, polynomial.multiply(new Polynomial(new Monomial(leadingCoefficient.reciprocal(), new LinkedHashMap<>(), polynomial.getVariableComparator()), polynomial.getMonomialOrder())));
        }

        // 自分を除いた先頭項のlistを作成
        for (int i = 0; i < groebnerBasis.size(); i++) {
            LinkedList<Polynomial> leadingTerms = new LinkedList<>();
            boolean leadingTermDivided = false;
            Polynomial dividedPolynomial = groebnerBasis.get(i).getHasVariablesPolynomial();
            for (int j = 0; j < groebnerBasis.size(); j++) {
                if (i == j) continue;
                Polynomial leadingTerm = new Polynomial(groebnerBasis.get(j).leadingTerm(), groebnerBasis.get(j).getMonomialOrder());
                if (dividedPolynomial.equals(leadingTerm)) continue;
                leadingTerms.add(leadingTerm);
                // 先頭項が一致しないものに割られたら入れない
                if (!groebnerBasis.get(i).leadingTerm().equals(groebnerBasis.get(j).leadingTerm())
                        && groebnerBasis.get(i).leadingTerm().remainder(groebnerBasis.get(j).leadingTerm()).isZero()) {
                    leadingTermDivided = true;
                }
            }
            // 自分が自分を除いた先頭項のlistで割って、余りが数字のみなら入れる
            if (!leadingTermDivided && !dividedPolynomial.remainder(leadingTerms).isEmptyVariables()) {
                boolean add = true;
                for (Polynomial polynomial : reducedGroebnerBasis) {
                    if (polynomial.equals(groebnerBasis.get(i))) {
                        add = false;
                        break;
                    }
                }
                if (add) reducedGroebnerBasis.add(groebnerBasis.get(i));
            }
        }

        return reducedGroebnerBasis;
    }

    /**
     * 基本のグレブナ基底を求めるアルゴリズム
     * 
     * @return グレブナ基底
     */
    private List<Polynomial> basicGroebnerBasisMethod() {
        LinkedList<Polynomial> groebnerBasis = new LinkedList<>(this.basicPolynomials);

        for (int i = 0; i < groebnerBasis.size(); i++) {
            for (int j = i + 1; j < groebnerBasis.size(); j++) {
                Polynomial sPolynomial = groebnerBasis.get(i).sPolynomial(groebnerBasis.get(j));
                if (!sPolynomial.remainder(groebnerBasis).isZero()) groebnerBasis.add(sPolynomial);
            }
        }
        return groebnerBasis;
    }

    /**
     * 応用したグレブナ基底を求めるアルゴリズム
     * 
     * @return グレブナ基底
     */
    private List<Polynomial> upgradeGroebnerBasisMethod() {
        LinkedList<Polynomial> groebnerBasis = new LinkedList<>(this.basicPolynomials);
        LinkedList<Entry<Integer, Integer>> pairs = new LinkedList<Entry<Integer, Integer>>();

        int size = groebnerBasis.size();
        for (int i = 0; i < size; i++) {
            for (int j = i + 1; j < size; j++) {
                pairs.add(new AbstractMap.SimpleEntry<>(i, j));
            }
        }
        int times = size;
        while (!pairs.isEmpty()) {
            Entry<Integer, Integer> pair = pairs.getFirst();

            Monomial lti = groebnerBasis.get(pair.getKey()).leadingTerm();
            Monomial ltj = groebnerBasis.get(pair.getValue()).leadingTerm();
            Monomial lcm = lti.leastCommonMultiple(ltj);
            if (!lcm.equals(ltj.multiply(lti)) && !this.criterion(groebnerBasis, lcm, pairs, pair)) {
                Polynomial sPolynomial = groebnerBasis.get(pair.getKey()).sPolynomial(groebnerBasis.get(pair.getValue()));
                Polynomial remainder = sPolynomial.remainder(groebnerBasis);
                for (Polynomial polynomial : groebnerBasis) {
                    if (polynomial.leadingVariables().equals(sPolynomial.leadingVariables())
                            && !polynomial.equals(sPolynomial) && remainder.isZero()) {
                        remainder = sPolynomial;
                        break;
                    }
                }
                if (!remainder.isZero()) {
                    times++;
                    groebnerBasis.add(remainder);
                    for (int i = 0; i < times - 1; i++) {
                        pairs.add(new AbstractMap.SimpleEntry<>(i, times - 1));
                    }
                }
            }
            pairs.removeFirst();
        }

        return groebnerBasis;
    }

    /**
     * pairに含まれないiがpairsに(pair.key, i), (i, pair.key), (pair.value, i), (i,
     * pair.value) も含まれず、lcmをf_iが割り切ったらtrue
     * 
     * @param groebnerBasis
     * @param lcm
     * @param pairs
     * @param pair
     * @return
     */
    private boolean criterion(LinkedList<Polynomial> groebnerBasis, Monomial lcm,
            LinkedList<Entry<Integer, Integer>> pairs, Entry<Integer, Integer> pair) {
        boolean result = false;
        int size = groebnerBasis.size();
        outsite: for (int i = 0; i < size; i++) {
            if (pair.getKey() == i || pair.getValue() == i) continue;

            for (Entry<Integer, Integer> entry : pairs) {
                if (entry.getKey() == pair.getKey() && entry.getValue() == i) continue outsite;
                if (entry.getKey() == i && entry.getValue() == pair.getKey()) continue outsite;

                if (entry.getKey() == pair.getValue() && entry.getValue() == i) continue outsite;
                if (entry.getKey() == i && entry.getValue() == pair.getValue()) continue outsite;
            }

            if (lcm.remainder(groebnerBasis.get(i).leadingTerm()).isZero()) {
                result = true;
                break;
            }
        }

        return result;

    }

}
