import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class Main {
    
    public static void readFile(String filename) {
        String clauseLine = null;
        Clause clause;
        int variables = 0;
        int totalClauses = 0;
        List<Clause> clauseList = new ArrayList<Clause>();

        try {
            File initialFile = new File(filename);
            initialFile.createNewFile();

            Reader reader = new FileReader(initialFile);
            
            BufferedReader buffReader = new BufferedReader(reader);

            boolean first = true;

            while ((clauseLine = buffReader.readLine()) != null) {

                if (first) {
                    String[] tempArray = clauseLine.split(" ");
                    variables = Integer.parseInt(tempArray[2]);
                    totalClauses = Integer.parseInt(tempArray[3]);
                    first = false;
                } else {
                    clause = new Clause(clauseLine);
                    clauseList.add(clause);

                    //System.out.println(clause.toString());
                }
                
            }
        } catch (IOException e) {
            System.out.println("IO Exception");
        }

        ClauseList clausePackage = new ClauseList(clauseList, totalClauses, variables);

        System.out.println(clausePackage.toString());
    }

    public static void main(String[] args) {
        String filename = args[0];
        readFile(filename);
    }
}