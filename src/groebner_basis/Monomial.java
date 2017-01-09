package groebner_basis;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Monomial {

    /**
     * 変数の記号を管理する 指数は0より大きいものしか扱わない
     */
    private LinkedHashMap<String, BigInteger> variables = new LinkedHashMap<>();

    /**
     * 係数を管理する
     */
    private Fraction coefficient;

    /**
     * 変数の順序を管理する
     */
    private VariableComparator variableComparator;

    /**
     * コンストラクタ
     * 
     * @param val -2x^3y^2, +4/5xy, x^3*y*z^1, x^5, 2, 2*xy, xyのような形を受け付ける
     *        (少数は非対応)
     * @param variablesOrder 変数の順序
     */
    public Monomial(String val, VariableComparator variableComparator) {

        // 変数に順序を取得
        this.variableComparator = variableComparator;

        List<String> variableOrder = this.variableComparator.getVariableOrder();
        // *を除去
        StringBuilder value = new StringBuilder(Pattern.compile("\\*").matcher(val).replaceAll(""));

        // 変数を一つずつ取っていって残ったものを係数とする
        for (String variable : variableOrder) {
            Matcher variableMatcher = Pattern.compile(variable).matcher(value.toString());
            if (variableMatcher.find()) {
                Matcher exponentMatcher = Pattern.compile("^\\^\\d+").matcher(value.toString()).region(variableMatcher.end(), value.length());
                if (exponentMatcher.find()) {
                    this.addVariable(value.substring(variableMatcher.start(), variableMatcher.end()), new BigInteger(value.substring(exponentMatcher.start(), exponentMatcher.end()).replace("^", "")));
                    value.replace(exponentMatcher.start(), exponentMatcher.end(), "");
                } else {
                    this.addVariable(value.substring(variableMatcher.start(), variableMatcher.end()), new BigInteger("1"));
                }
                value.replace(variableMatcher.start(), variableMatcher.end(), "");
            }
        }
        // 残りを係数とする
        if (Pattern.compile("^[+-]$").matcher(value.toString()).find() || value.length() == 0) value.append("1");

        this.coefficient = new Fraction(value.toString());
    }

    /**
     * コンストラクタ
     * 
     * @param coefficient 係数
     * @param variables 変数
     * @param variableComparator 変数の順序
     */
    public Monomial(Fraction coefficient, LinkedHashMap<String, BigInteger> variables,
            VariableComparator variableComparator) {
        this.coefficient = coefficient;

        this.variableComparator = variableComparator;

        // ゼロなら変数と係数入れない
        if (coefficient.isZero()) return;

        for (Entry<String, BigInteger> variable : variables.entrySet()) {
            this.addVariable(variable.getKey(), variable.getValue());
        }
        // 順番がおかしいかもしれないので並び替え
        this.sortVariables();
    }

    /**
     * 単項式を文字列として取得
     * 
     * @return 単項式の文字列
     */
    @Override
    public String toString() {
        if (this.coefficient.isZero()) return this.coefficient.toString();
        StringBuilder value = new StringBuilder();
        for (Entry<String, BigInteger> variable : this.variables.entrySet()) {
            value.append(variable.getKey());
            if (variable.getValue().compareTo(BigInteger.ONE) != 0) value.append("^").append(variable.getValue());
        }

        return (this.coefficient.abs().isOne() && value.length() != 0)
                ? value.insert(0, (this.coefficient.isOne()) ? "" : "-").toString()
                : value.insert(0, this.coefficient.toString()).toString();
    }

    /**
     * 変数を追加する 指数と数を合わせるので、この関数を使用することを推奨
     * 
     * @param variable 変数
     * @param exponent その変数の指数
     */
    private void addVariable(String variable, BigInteger exponent) {
        // 指数が0なら追加しない
        if (exponent.compareTo(BigInteger.ZERO) == 0) return;

        // 負でないかチェックする
        if (exponent.compareTo(BigInteger.ZERO) < 0) throw new Error("minusExponentError");

        this.variables.put(variable, exponent);
    }

    /**
     * 変数を並び替える
     */
    private void sortVariables() {
        List<Entry<String, BigInteger>> entries = new ArrayList<Entry<String, BigInteger>>(this.variables.entrySet());
        entries.sort(this.variableComparator);
        LinkedHashMap<String, BigInteger> variables = new LinkedHashMap<>();
        for (Entry<String, BigInteger> entry : entries) {
            variables.put(entry.getKey(), entry.getValue());
        }
        this.variables = variables;
    }

    /**
     * 値が (-this) の Mononial を返す
     * 
     * @return -this
     */
    public Monomial negate() {
        return new Monomial(this.coefficient.negate(), this.variables, this.variableComparator);
    }

    /**
     * 値が (this + val) である Mononial を返す
     * 
     * @param val
     * @return this + val
     */
    public Monomial add(Monomial val) {
        // 変数と順序が一致するかチェック
        if (!this.isEqualVariables(val)) throw new Error("variablesNotMatchError");
        this.equalsVariableComparator(val);

        return new Monomial(this.coefficient.add(val.coefficient), this.variables, this.variableComparator);
    }

    /**
     * 値が (this - val) である Monomial を返す
     * 
     * @param val
     * @return this - val
     */
    public Monomial subtract(Monomial val) {
        return this.add(val.negate());
    }

    /**
     * 値が (this * val) である Monomial を返す
     * 
     * @param val
     * @return this * val
     */
    public Monomial multiply(Monomial val) {
        // 順序が一致するかチェック
        this.equalsVariableComparator(val);

        LinkedHashMap<String, BigInteger> variables = new LinkedHashMap<>();
        LinkedHashMap<String, BigInteger> valVariables = new LinkedHashMap<>();
        valVariables.putAll(val.variables);
        for (Entry<String, BigInteger> variable : this.variables.entrySet()) {
            BigInteger exponent = variable.getValue();
            // 同じ変数があった時
            if (valVariables.containsKey(variable.getKey())) {
                exponent = exponent.add(valVariables.get(variable.getKey()));
                valVariables.remove(variable.getKey());
            }
            variables.put(variable.getKey(), exponent);
        }
        // 同じ変数がなかったものを追加する
        for (Entry<String, BigInteger> variable : valVariables.entrySet()) {
            variables.put(variable.getKey(), variable.getValue());
        }
        return new Monomial(this.coefficient.multiply(val.coefficient), variables, this.variableComparator);
    }

    /**
     * 値が (this / val) である Monomial を返す
     * 
     * @param val
     * @return this / val
     */
    public Monomial divide(Monomial val) {
        return this.divideAndRemainder(val).get(0);
    }

    /**
     * 値が (this % val) である Monomial を返す
     * 
     * @param val
     * @return this % val
     */
    public Monomial remainder(Monomial val) {
        return this.divideAndRemainder(val).get(1);
    }

    /**
     * (this / val) そして (this % val) と続く、2 つの Monomial の List を返す
     * 
     * @param val
     * @return 2 つの Monomial の List 。商 (this / val) は最初の要素で、剰余 (this % val)
     *         は最後の要素
     */
    public List<Monomial> divideAndRemainder(Monomial val) {
        // 順序が一致するかチェック
        this.equalsVariableComparator(val);

        LinkedHashMap<String, BigInteger> variables = new LinkedHashMap<>();
        LinkedHashMap<String, BigInteger> valVariables = new LinkedHashMap<>(val.variables);
        for (Entry<String, BigInteger> variable : this.variables.entrySet()) {
            BigInteger exponent = variable.getValue();
            // 同じ変数があった時
            if (valVariables.containsKey(variable.getKey())) {
                exponent = exponent.subtract(valVariables.get(variable.getKey()));
                // 指数が負になったら割り切れないのでやめる
                if (exponent.compareTo(BigInteger.ZERO) < 0) break;
                valVariables.remove(variable.getKey());
            }

            variables.put(variable.getKey(), exponent);
        }

        Monomial divideResult = new Monomial("0", this.variableComparator);
        Monomial remainderResult = new Monomial("0", this.variableComparator);
        // 同じ変数がなかったら負になるので割り切れない
        if (!valVariables.isEmpty()) {
            remainderResult = new Monomial(this.coefficient, this.variables, this.variableComparator);
        } else {
            divideResult = new Monomial(this.coefficient.divide(val.coefficient), variables, this.variableComparator);
        }

        return new ArrayList<Monomial>(Arrays.asList(divideResult, remainderResult));
    }

    /**
     * this と val の variableComparator が一致するか
     * 
     * @param val
     * @return 一致すればtrue 一致しなければVariableComparatorNotMatchError
     */
    private boolean equalsVariableComparator(Monomial val) {
        if (!this.variableComparator.equals(val.variableComparator)) throw new Error("VariableComparatorNotMatchError");

        return true;
    }

    /**
     * 変数の順序を取得
     * 
     * @return VariableComparator
     */
    public VariableComparator getVariableComparator() {
        return this.variableComparator;
    }

    /**
     * 引数の変数の指数を返す
     * 
     * @param variable 変数
     * @return ないときはBigInteger.ZERO
     */
    public BigInteger getVariableExponent(String variable) {
        return (this.variables.containsKey(variable)) ? new BigInteger(this.variables.get(variable).toString())
                : BigInteger.ZERO;
    }

    /**
     * 引数の変数・指数とthisの変数・指数が一致するか
     * 
     * @param val
     * @return 一致=true 不一致=false
     */
    public boolean isEqualVariables(Monomial val) {
        return this.variables.equals(val.variables);
    }

    /**
     * この Monomial の符号要素を返す
     * 
     * @return この Monomial が負の場合は -1、ゼロの場合は 0、正の場合は 1
     */
    public int signum() {
        return this.coefficient.signum();
    }

    /**
     * この Monomial の次数を返す
     * 
     * @return この Monomial の次数
     */
    public BigInteger degree() {
        BigInteger degree = BigInteger.ZERO;
        for (Entry<String, BigInteger> variable : this.variables.entrySet()) {
            degree = degree.add(variable.getValue());
        }
        return degree;
    }

    /**
     * この Monomial の多重次数を返す
     * 
     * @return この Monomial の多重次数
     */
    public List<BigInteger> multiDegree() {
        List<String> variableOrder = this.variableComparator.getVariableOrder();
        List<BigInteger> multiDegree = new ArrayList<BigInteger>();
        for (String variable : variableOrder) {
            multiDegree.add(this.getVariableExponent(variable));
        }
        return multiDegree;
    }

    /**
     * 値が this と val の最小公倍元である Monomial を返す
     * 
     * @param val
     * @return this と val の最小公倍元
     */
    public Monomial leastCommonMultiple(Monomial val) {
        // 順序が一致するかチェック
        this.equalsVariableComparator(val);

        LinkedHashMap<String, BigInteger> leastCommonMultipleVariables = new LinkedHashMap<>();
        List<String> variableOrder = this.variableComparator.getVariableOrder();
        for (int i = 0; i < variableOrder.size(); i++) {
            if (this.variables.get(variableOrder.get(i)) != null && val.variables.get(variableOrder.get(i)) != null) {
                leastCommonMultipleVariables.put(variableOrder.get(i), (this.variables.get(variableOrder.get(i)).compareTo(val.variables.get(variableOrder.get(i))) > 0)
                        ? this.variables.get(variableOrder.get(i)) : val.variables.get(variableOrder.get(i)));
            }
            if (this.variables.get(variableOrder.get(i)) != null && val.variables.get(variableOrder.get(i)) == null)
                leastCommonMultipleVariables.put(variableOrder.get(i), this.variables.get(variableOrder.get(i)));
            if (this.variables.get(variableOrder.get(i)) == null && val.variables.get(variableOrder.get(i)) != null)
                leastCommonMultipleVariables.put(variableOrder.get(i), val.variables.get(variableOrder.get(i)));
        }
        return new Monomial(new Fraction("1"), leastCommonMultipleVariables, this.variableComparator);
    }

    /**
     * この Monomial の係数を返す
     * 
     * @return この Monomial の係数
     */
    public Fraction coefficient() {
        return this.coefficient;
    }

    /**
     * この Monomial の変数を返す
     * 
     * @return この Monomial の変数
     */
    public LinkedHashMap<String, BigInteger> variables() {
        return new LinkedHashMap<String, BigInteger>(this.variables);
    }

    /**
     * 0かどうか
     * 
     * @return 0ならtrue
     */
    public boolean isZero() {
        return (this.coefficient.isZero() && this.variables.isEmpty());
    }

    /**
     * 変数がないかどうか
     * 
     * @return ないならtrue
     */
    public boolean isEmptyVariables() {
        return this.variables.isEmpty();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((coefficient == null) ? 0 : coefficient.hashCode());
        result = prime * result + ((variableComparator == null) ? 0 : variableComparator.hashCode());
        result = prime * result + ((variables == null) ? 0 : variables.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof Monomial)) return false;
        Monomial other = (Monomial) obj;
        if (coefficient == null) {
            if (other.coefficient != null) return false;
        } else if (!coefficient.equals(other.coefficient)) return false;
        if (variableComparator == null) {
            if (other.variableComparator != null) return false;
        } else if (!variableComparator.equals(other.variableComparator)) return false;
        if (variables == null) {
            if (other.variables != null) return false;
        } else if (!variables.equals(other.variables)) return false;
        return true;
    }

}
