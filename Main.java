import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class Main {

    /**
     * Method for reading in a string filename from a user via the command line.
     * @param String filename The filename passed in by the user from the command line.
     * @return ClauseList containing problem information - i.e. the clauses and number of variables. Used
     *         to instantiate our GA and PBIL algorithms.
     */
    public static ClauseList readFile(String filename) {
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
                }

            }
            buffReader.close();
        } catch (IOException e) {
            System.out.println("IO Exception");
        }

        ClauseList clausePackage = new ClauseList(clauseList, totalClauses, variables);

        return clausePackage;

    }
    /**
     * Main method to run a Genetic Algorithm and PBIL algorithm.
     * @param String[] args Command line arguments for running the program. See the read me to understand
     *                      how to run both PBIL and Genetic Algorithm for MAXSAT.
     */
    public static void main(String[] args) {
        String filename = args[0];
        ClauseList clausePackage = readFile(filename);

        if (args[7].equals("p")) {
            int numIndividuals = Integer.parseInt(args[1]);
            double posLearnRate = Double.parseDouble(args[2]);
            double negLearnRate = Double.parseDouble(args[3]);
            double mutationProb = Double.parseDouble(args[4]);
            double mutationAmount = Double.parseDouble(args[5]);
            int iterations = Integer.parseInt(args[6]);

            PBIL pbil = new PBIL(clausePackage, posLearnRate, negLearnRate, mutationProb, mutationAmount, iterations, numIndividuals);
            pbil.optimize();

        } else if (args[7].equals("g")) {
            int numIndividuals = Integer.parseInt(args[1]);
            String selectionType = args[2];
            String crossoverType = args[3];
            Double crossoverProb = Double.parseDouble(args[4]);
            Double mutationProb = Double.parseDouble(args[5]);
            int iterations = Integer.parseInt(args[6]);

            GeneticAlgorithm ga = new GeneticAlgorithm(clausePackage, numIndividuals, selectionType, crossoverType, 
                                                        crossoverProb, mutationProb, iterations);
            ga.optimize();

        } else {
            System.out.println("Some error happened with the command line");
        }


    }
}
