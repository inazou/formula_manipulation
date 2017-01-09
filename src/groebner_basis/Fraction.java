package groebner_basis;

import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Fraction {

    /**
     * 分子
     */
    private BigInteger numerator;

    /**
     * 分母
     */
    private BigInteger denominator;

    /**
     * コンストラクタ
     * 
     * @param numerator 分子
     * @param denominator 分母
     */
    public Fraction(BigInteger numerator, BigInteger denominator) {
        this.numerator = numerator;
        this.setDenominator(denominator);
        this.reduction();
    }

    /**
     * コンストラクタ 2, 1/2, +2/5, -2/5, 5/-2 = -2/5などを受け付ける
     * 
     * @param val
     */
    public Fraction(String val) {
        BigInteger numerator;
        BigInteger denominator = BigInteger.ONE;
        StringBuilder value = new StringBuilder(val);

        Matcher matcher = Pattern.compile("^[\\+-]?\\d+/").matcher(value.toString());
        if (matcher.find()) {
            // 分数の形の文字列の時
            numerator = new BigInteger(value.substring(matcher.start(), matcher.end() - 1));
            denominator = new BigInteger(value.substring(matcher.end()));
        } else {
            // 分数の形でない時
            numerator = new BigInteger(value.toString());
        }
        this.numerator = numerator;
        this.setDenominator(denominator);
        this.reduction();
    }

    /**
     * 分数を文字列として取得
     * 
     * @return 分数の文字列
     */
    @Override
    public String toString() {
        if (this.denominator.compareTo(BigInteger.ONE) == 0) return this.numerator.toString();
        return this.numerator.toString() + "/" + this.denominator.toString();
    }

    /**
     * 1かどうか
     * 
     * @return 1ならtrue
     */
    public boolean isOne() {
        return (this.numerator.compareTo(BigInteger.ONE) == 0 && this.denominator.compareTo(BigInteger.ONE) == 0);
    }

    /**
     * 0かどうか
     * 
     * @return 0ならtrue
     */
    public boolean isZero() {
        return (this.numerator.compareTo(BigInteger.ZERO) == 0);
    }

    /**
     * 分子を取得
     * 
     * @return 分子
     */
    public BigInteger getNumerator() {
        return new BigInteger(this.numerator.toString());
    }

    /**
     * 分母の取得
     * 
     * @return 分母
     */
    public BigInteger getDenominator() {
        return new BigInteger(this.denominator.toString());
    }

    /**
     * 値が (-this) の Fraction を返す
     * 
     * @return -this
     */
    public Fraction negate() {
        return new Fraction(this.numerator.negate(), this.denominator);
    }

    /**
     * 値がこの this の絶対値である Fraction を返す
     * 
     * @return abs(this)
     */
    public Fraction abs() {
        return new Fraction(this.numerator.abs(), this.denominator);
    }

    /**
     * 値がこの this の逆数である Fraction を返す 0の場合はzeroDivideError
     * 
     * @return 1 / this
     */
    public Fraction reciprocal() {
        return new Fraction(this.denominator, this.numerator);
    }

    /**
     * 値が (this + val) である Fraction を返す
     * 
     * @param val
     * @return this + val
     */
    public Fraction add(Fraction val) {
        return new Fraction(this.numerator.multiply(val.denominator).add(val.numerator.multiply(this.denominator)), this.denominator.multiply(val.denominator));
    }

    /**
     * 値が (this - val) である Fraction を返す
     * 
     * @param val
     * @return this - val
     */
    public Fraction subtract(Fraction val) {
        return this.add(val.negate());
    }

    /**
     * 値が (this * val) である Fraction を返す
     * 
     * @param val
     * @return this * val
     */
    public Fraction multiply(Fraction val) {
        return new Fraction(this.numerator.multiply(val.numerator), this.denominator.multiply(val.denominator));
    }

    /**
     * 値が (this / val) である Fraction を返す
     * 
     * @param val
     * @return this / val
     */
    public Fraction divide(Fraction val) {
        return new Fraction(this.numerator.multiply(val.denominator), this.denominator.multiply(val.numerator));
    }

    /**
     * この Fraction の符号要素を返します。
     * 
     * @return この Fraction が負の場合は -1、ゼロの場合は 0、正の場合は 1
     */
    public int signum() {
        return this.numerator.signum();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((denominator == null) ? 0 : denominator.hashCode());
        result = prime * result + ((numerator == null) ? 0 : numerator.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof Fraction)) return false;
        Fraction other = (Fraction) obj;
        if (denominator == null) {
            if (other.denominator != null) return false;
        } else if (!denominator.equals(other.denominator)) return false;
        if (numerator == null) {
            if (other.numerator != null) return false;
        } else if (!numerator.equals(other.numerator)) return false;
        return true;
    }

    /**
     * インスタンス変数の分母をセットする
     * 
     * @param denominator セットする分母
     */
    private void setDenominator(BigInteger denominator) {
        switch (denominator.compareTo(BigInteger.ZERO)) {
        case 0:
            throw new Error("zeroDivideError");
        case -1:
            denominator = denominator.abs();
            this.numerator = this.numerator.negate();
        default:
            break;
        }
        this.denominator = denominator;
    }

    /**
     * 自分自身の約分を行う
     */
    private void reduction() {
        BigInteger gcd = this.numerator.gcd(this.denominator);
        this.numerator = this.numerator.divide(gcd);
        this.denominator = this.denominator.divide(gcd);
    }

}
