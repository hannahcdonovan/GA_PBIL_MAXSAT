import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class Main {
    
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
        //Individual individual = new Individual(clausePackage.getVariableNum());
        //Population populationInstance = new Population(50);

        // System.out.println("Individual size " + individual.size());
        // System.out.println("Individual " + individual.generateIndividual());
        // System.out.println(clausePackage.toString());

        //Population population = populationInstance.generateRandomPopulation(clausePackage.getVariableNum());
        //System.out.println(population.getPopulationList());

/*
        for (int i=0; i < population.size(); i++) {
                population.getIndividual(i).mutate(0.5);
        }
        */

        return clausePackage;
        
    }

    public static void main(String[] args) {
        String filename = args[0];
        ClauseList clausePackage = readFile(filename);
        System.out.println(args[7]);

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

            //do whatever is necessary to run Genetic Algorithm with these parameters

        } else {
            System.out.println("Some error happened with the command line");
        }


    }
}