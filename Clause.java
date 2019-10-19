public class Clause {

    /**
     * An array of ints, which are variables in the MAXSAT problem, making up a single clause.
     */
    public int[] clauseArray;

    /**
     * Constructor for the Clause object. Given a clause, which is a string from the file read in during the
     * Main method, we add each variable in the clause to the clauseArray as an integer.
     * @param String clause A string containing variables given a line in a MAXSAT file.
     */
    public Clause(String clause) {
        String[] tempArray = clause.split(" ");
        clauseArray = new int[tempArray.length - 1];
        for (int i = 0; i < tempArray.length - 1; i++) {
            int num = Integer.parseInt(tempArray[i]);
            clauseArray[i] = num;
        }
    }

    /**
     * Return the array representing the clause.
     * @return int[] The array representing the clause.
     */
    public int[] getArray() {
        return this.clauseArray;
    }

    /**
     * Creates a string representation of the clause and returns it.
     * @return String represenation of a Clause.
     */
    public String toString() {
        String rep = "";
        for (int i = 0; i < clauseArray.length; i++) {
            rep +=  clauseArray[i] + " ";
        }
        return rep;
    }
}