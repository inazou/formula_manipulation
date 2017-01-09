package groebner_basis;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map.Entry;

public class VariableComparator implements Comparator<Entry<String, BigInteger>> {

    /**
     * 変数の順序を管理する
     */
    private List<String> variableOrder;

    /**
     * コンストラクタ
     * 
     * @param variableOrder 変数の順序
     */
    public VariableComparator(List<String> variableOrder) {
        // 重複を除去
        this.variableOrder = new ArrayList<>(new LinkedHashSet<>(variableOrder));
    }

    /**
     * 変数の順序のリストを返す
     * 
     * @return 変数の順序のリスト
     */
    public List<String> getVariableOrder() {
        return this.variableOrder;
    }

    @Override
    public int compare(Entry<String, BigInteger> o1, Entry<String, BigInteger> o2) {
        int n1 = this.variableOrder.indexOf(o1.getKey());
        int n2 = this.variableOrder.indexOf(o2.getKey());

        if (n1 == -1 || n2 == -1) throw new Error("variablesIncludeUnknownStringError");

        if (n1 == n2) throw new Error("variablesIncludeSameValue");

        if (n1 > n2) {
            return 1;
        } else {
            return -1;
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((variableOrder == null) ? 0 : variableOrder.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof VariableComparator)) return false;
        VariableComparator other = (VariableComparator) obj;
        if (variableOrder == null) {
            if (other.variableOrder != null) return false;
        } else if (!variableOrder.equals(other.variableOrder)) return false;
        return true;
    }

}
