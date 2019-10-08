import java.util.Arrays;

public class Clause {

    String[] clauseArray;

    public Clause(String clause) {
        String[] tempArray = clause.split(" ");
        clauseArray = Arrays.copyOfRange(tempArray, 0, tempArray.length -1);
    }

    public String toString() {
        String rep = "";
        for (int i = 0; i < clauseArray.length; i++) {
            rep +=  clauseArray[i];
        }
        return rep;
    }
}