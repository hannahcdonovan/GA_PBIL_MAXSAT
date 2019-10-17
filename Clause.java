public class Clause {

    int[] clauseArray;

    public Clause(String clause) {
        String[] tempArray = clause.split(" ");
        clauseArray = new int[tempArray.length - 1];
        for (int i = 0; i < tempArray.length - 1; i++) {
            int num = Integer.parseInt(tempArray[i]);
            clauseArray[i] = num;
        }
    }

    public int[] getArray() {
        return this.clauseArray;
    }

    public String toString() {
        String rep = "";
        for (int i = 0; i < clauseArray.length; i++) {
            rep +=  clauseArray[i] + " ";
        }
        return rep;
    }

    public static void main(String[] args) {
        System.out.println("test");
    }
}