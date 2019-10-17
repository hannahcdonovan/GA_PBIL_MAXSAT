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

        for(int i = 0; i < offSpring.size(); i++) {
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
                //return rankSelection(pop);
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

    public static void main(String[] args) { 
        Population pop = new Population(6);
        pop.generateRandomPopulation(4);
        System.out.println(pop);

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
        Collections.sort(pop.popList);
        System.out.println(pop.popList);

        GeneticAlgorithm ga = new GeneticAlgorithm(cl, 6, "rs", "1c", 0.01, 0.01, 1);
        Population newPop = ga.rankSelection(pop);
        System.out.println("___________");
        System.out.println(newPop);

        for(Individual ind : newPop.popList) {
            int fitness = ind.getFitness(cl);
            System.out.println(ind + " -> " + fitness);

        }
    }

}