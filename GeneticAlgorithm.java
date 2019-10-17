import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;

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

    public Population rankSelection(Population pop) {

        List<Individual> offSpring = pop.popList;

        for(Individual ind: offSpring) {
            int fitness = ind.getFitness(this.problem);
        }

        Collections.sort(offSpring);
        int totalSum = (offSpring.size() * (offSpring.size() + 1)) / 2;
        List<Individual> indList = new ArrayList<Individual>();

        for(int i = 0; i < offSpring.size() / 2; i++) {
            Individual currentInd = offSpring.get(i);
            for(int j = 0; j < i + 1; j++) {
                indList.add(currentInd);
            }
        }

        Random rand = new Random();
        List<Individual> newPopList = new ArrayList<Individual>();
        for(int i = 0; i < offSpring.size(); i++) {
            int index = rand.nextInt(indList.size());
            newPopList.add(indList.get(index));
        }

        Population newPop = new Population(pop.populationNum);
        newPop.popList = newPopList;
        return newPop;
    }



    public Population tournamentSelection(Population pop) {

        List<Individual> offSpring = pop.popList;
        Population newPop = new Population(offSpring.size());
        
        for (int i = 0; i < offSpring.size() - 1; i += 2) {
            if (offSpring.get(i).getFitness(this.problem) <= offSpring.get(i + 1).getFitness(this.problem)) {
                newPop.addIndividual(offSpring.get(i));
            } else {
                newPop.addIndividual(offSpring.get(i + 1));
            }
        }

        Collections.shuffle(offSpring);

        for (int i = 0; i < offSpring.size() - 1; i += 2) {
            if (offSpring.get(i).getFitness(this.problem) <= offSpring.get(i + 1).getFitness(this.problem)) {
                newPop.addIndividual(offSpring.get(i));
            } else {
                newPop.addIndividual(offSpring.get(i + 1));
            }
        }
        return newPop;
    }


    public Population selectionByGroups(Population pop) {
        List<Individual> offSpring = pop.popList;
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
        int j = offSpring.size() - 1;
        int group2Fitness = 0;
        while (j >= popNum) {
            group2Fitness += offSpring.get(j).getFitness(this.problem);
            j--;
        }

        if (group1Fitness <= group2Fitness) {
            for (int k = 0; k < popNum; k++) {
                newPop.addIndividual(offSpring.get(k));
                newPop.addIndividual(offSpring.get(k));
            }
        } else {
            for (int h = offSpring.size() - 1; h >= popNum; h--) {
                newPop.addIndividual(offSpring.get(h));
                newPop.addIndividual(offSpring.get(h));
            }
        }

        return newPop;
    }

    public Population select(Population pop) {
        switch (this.selectionType) {
            case "rs":
                System.out.println("Rank Selection");
                return rankSelection(pop);
            case "ts":
                System.out.println("Tournament Selection");
                return tournamentSelection(pop);
            case "sbg":
                System.out.println("Selection By Groups");
                return selectionByGroups(pop);
            default:
                System.out.println("This selection type is not available.");
                Population defaultPop = new Population(0);
                return defaultPop;
        }
    }

    public static void main(String[] args) { 
        Population pop = new Population(6);
        pop.generateRandomPopulation(4);

        Clause c1 = new Clause("1 0");
        Clause c2 = new Clause("2 0");
        Clause c3 = new Clause("3 0");
        Clause c4 = new Clause("4 0");

        List<Clause> clauseList = new ArrayList<Clause>();
        clauseList.add(c1);
        clauseList.add(c2);
        clauseList.add(c3);
        clauseList.add(c4);

        ClauseList cl = new ClauseList(clauseList, 4, 4);

        for(Individual ind : pop.popList) {
            int fitness = ind.getFitness(cl);
            System.out.println(ind + " -> " + fitness);

        }

        GeneticAlgorithm ga = new GeneticAlgorithm(cl, 6, "sbg", "1c", 0.01, 0.01, 1);
        Population newPop = ga.select(pop);
        System.out.println("___________");

        for(Individual ind : newPop.popList) {
            int fitness = ind.getFitness(cl);
            System.out.println(ind + " -> " + fitness);

        }
    }

}