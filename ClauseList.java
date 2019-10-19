import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class ClauseList {

    /**
     * List containing the clauses and other relevant MAXSAT problem information.
     */
    public List<Clause> clauseList;

    /**
     * Number of clauses in the MAXSAT problem. 
     */
    public int clauseNum;

    /**
     * The number of the variables in the particular MAXSAT problem.
     */
    public int variableNum;

    /**
     * Constructor for the ClauseList object. The object provides an easy way to access the relevant MAXSAT
     * problem info once it has been read in from the given file in the Main method.
     * @param List<Clause> clauseList A list containing all the clauses.
     * @param int clauseNum The total number of clauses for the given problem.
     * @param int variableNum The total number of variables for the given problem.
     */
    public ClauseList(List<Clause> clauseList, int clauseNum, int variableNum) {
        this.clauseList = clauseList;
        this.clauseNum = clauseNum;
        this.variableNum = variableNum;
    }

    /**
     * Get the clauseNum attribute from the instance of the ClauseList object.
     * @return int The number of clauses for the given MAXSAT problem.
     */
    public int getClauseNum() {
        return this.clauseNum;
    }

    /**
     * Get the variableNum attribute from the instance of the ClauseList object.
     * @return int The number of variables for the given MAXSAT problem.
     */
    public int getVariableNum() {
        return this.variableNum;
    }

    /**
     * Get the clauseList attribute from the instance of the ClauseList object.
     * @return List<Clause> The clauseList attribute - i.e. list containing all of the clauses.
     */
    public List<Clause> getList() {
        return this.clauseList;
    }

    /**
     * Method returning the string representation of the object. Only the clauseList, however.
     * @return String representation of ClauseList.
     */
    public String toString() {
        String stringRep = " ";
        for (int i = 0; i < this.clauseList.size(); i++) {
            stringRep += this.clauseList.get(i) + " | ";
        }
        return stringRep;
    }
}