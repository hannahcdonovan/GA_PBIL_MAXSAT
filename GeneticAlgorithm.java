import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Arrays;
import java.util.ArrayList;

public class GeneticAlgorithm {

    ClauseList problem;
    int popSize;
    String selectionType;
    String crossOverType;
	double crossOverProb;
    double mutationProb;
    int iterations;

    public GeneticAlgorithm(ClauseList problem, int popSize, String selectionType, String crossOverType,
                            double crossOverProb, double mutationProb, int iterations) {
        this.problem = problem;
        this.popSize = popSize;
        this.selectionType = selectionType;
        this.crossOverType = crossOverType;
        this.crossOverProb = crossOverProb;
        this.mutationProb = mutationProb;
        this.iterations = iterations;
    }

    // public List<Individual> recombine(Population pop) {
    // }

    public Population rankSelection(List<Individual> offSpring) {
        int newPopSize = offSpring.size() / 2;
        Population newPop = new Population(newPopSize);

        Map<Individual, Integer> fitnessTable = new HashMap<Individual, Integer>();
        
        // Get the total fitness for the population
        int offSpringTotalFitness = 0;
        for (int i = 0; i < offSpring.size(); i++) {
            offSpringTotalFitness += offSpring.get(i).getFitness(problem);
        }

        List<Individual> probabilityPicker = new ArrayList<Individual>(offSpringTotalFitness);


        // Store different individuals and their respective ranked fitnesses in a hashtable
        for (int j = 0; j < offSpring.size(); j++) {
            fitnessTable.put(offSpring.get(j), offSpring.get(j).getFitness(problem));
        }

        for (Map.Entry<Individual, Integer> entry: fitnessTable.entrySet()) {
            Individual ind = entry.getKey();
            int fitness = entry.getValue();
            int k = 0;
            while (k < fitness) {
                probabilityPicker.add(ind);
                k++;
            }
        }
        System.out.println(probabilityPicker.toString());

        Random generator = new Random();

        int h = 0;
        while (h < newPopSize) {
            int randIndex = generator.nextInt(probabilityPicker.size());
            newPop.addIndividual(probabilityPicker.get(randIndex));
            h++; 
        }

        return newPop;
    }

    public Population tournamentSelection(List<Individual> offSpring) {
        Population newPop = new Population(offSpring.size() / 2);
        
        for (int i = 0; i < offSpring.size() - 1; i++) {
            if (offSpring.get(i).getFitness(this.problem) >= offSpring.get(i + 1).getFitness(this.problem)) {
                newPop.addIndividual(offSpring.get(i));
            } else {
                newPop.addIndividual(offSpring.get(i + 1));
            }
        }
        return newPop;
    }

    public Population selectionByGroups(List<Individual> offSpring) {
        int popNum = offSpring.size() / 2;

        Population newPop = new Population(popNum);

        //Make first array
        int i = 0;
        int group1Fitness = 0;
        while (i < popNum) {
            group1Fitness += offSpring.get(i).getFitness(this.problem);
            i++;
        }

        //Calculate fitness of last half of array
        int j = offSpring.size();
        int group2Fitness = 0;
        while (j >= popNum) {
            group2Fitness += offSpring.get(j).getFitness(this.problem);
            j--;
        }

        if (group1Fitness >= group2Fitness) {
            for (int k = 0; k < popNum; k++) {
                newPop.addIndividual(offSpring.get(k));
            }
        } else {
            for (int h = offSpring.size(); h >= popNum; h--) {
                newPop.addIndividual(offSpring.get(h));
            }
        }

        return newPop;
    }

    public Population select(List<Individual> offSpring) {
        switch (this.selectionType) {
            case "rs":
                return rankSelection(offSpring);
            case "ts":
                return tournamentSelection(offSpring);
            case "sbg":
                return selectionByGroups(offSpring);
            default:
                System.out.println("This selection type is not available.");
                Population defaultPop = new Population(0);
                return defaultPop;
        }
    }

}