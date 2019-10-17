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
                }
                
            }
            buffReader.close();
        } catch (IOException e) {
            System.out.println("IO Exception");
        }

        ClauseList clausePackage = new ClauseList(clauseList, totalClauses, variables);
        Individual individual = new Individual(clausePackage.getVariableNum());
        Population populationInstance = new Population(50);

        // System.out.println("Individual size " + individual.size());
        // System.out.println("Individual " + individual.generateIndividual());
        // System.out.println(clausePackage.toString());

        PBIL pbil = new PBIL(clausePackage, 0.1, 0.075, 0.02, 0.05, 1000, 100);
        pbil.optimize();

        GeneticAlgorithm ga = new GeneticAlgorithm(clausePackage, 100, ts, 0, 0, 0, 100);
        System.out.println(ga.select())


        //Population population = populationInstance.generateRandomPopulation(clausePackage.getVariableNum());
        //System.out.println(population.getPopulationList());

/*
        for (int i=0; i < population.size(); i++) {
                population.getIndividual(i).mutate(0.5);
        }
        */
        
    }

    public static void main(String[] args) {
        String filename = args[0];
        readFile(filename);
    }
}