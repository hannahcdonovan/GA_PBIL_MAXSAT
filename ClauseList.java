import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class ClauseList {

    public List<Clause> clauseList;
    public int clauseNum;
    public int variableNum;

    public ClauseList(List<Clause> clauseList, int clauseNum, int variableNum) {
        this.clauseList = clauseList;
        this.clauseNum = clauseNum;
        this.variableNum = variableNum;
    }

    public int getClauseNum() {
        return this.clauseNum;
    }

    public int getVariableNum() {
        return this.variableNum;
    }

    public String toString() {
        String stringRep = " ";
        for (int i = 0; i < this.clauseList.size(); i++) {
            stringRep += this.clauseList.get(i) + " | ";
        }
        return stringRep;
    }
}