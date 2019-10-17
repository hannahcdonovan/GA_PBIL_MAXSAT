import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
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

    public GeneticAlgorithm(ClauseList problem, int popSize, String selectionType, String crossoverType,
                            double crossoverProb, double mutationProb, int iterations) {
        this.problem = problem;
        this.popSize = popSize;
        this.selectionType = selectionType;
        this.crossoverType = crossoverType;
        this.crossoverProb = crossoverProb;
        this.mutationProb = mutationProb;
        this.iterations = iterations;
    }

    public List<Individual> crossoverHelper(Individual firstParent, Individual secondParent, int crossoverPoint) {
        List<Individual> children = new ArrayList<Individual>();

        //Two parents will produce four children - child1 and child 2 will be doubled;
        Individual child1;
        Individual child2;

        // need to account for the probability
        int crossoverInt = (int) Math.round(this.crossoverProb * 100);
        boolean[] crossoverRoulette = new boolean[100];
        for (int i = 0; i < 100; i++) {
            if (i < crossoverInt) {
                crossoverRoulette[i] = true;
            } else {
                crossoverRoulette[i] = false;
            }
        }

        Random generator = new Random();
        int crossoverPicker = generator.nextInt(100);
        boolean doCrossover = crossoverRoulette[crossoverPicker];
        
        if (doCrossover) {
            child1 = firstParent.subSet(0, crossoverPoint).add(secondParent.subSet(crossoverPoint, this.problem.getVariableNum()));
            child2 = secondParent.subSet(0, crossoverPoint).add(firstParent.subSet(crossoverPoint, this.problem.getVariableNum()));
        } else {
            child1 = firstParent;
            child2 = secondParent;
        }

        return children;
    }

    public Population onePointCrossover(Population pop) {
        Population newPop = new Population(pop.size() * 2);

        Random generator = new Random();

        // Using the length of the individual based on our problem, generate random crossover point
        int crossoverPoint = generator.nextInt(this.problem.getVariableNum());

        //Pick two candidates from the population, and then perform crossover
        List<Individual> populationList = pop.getPopulationList();

        Individual parent1;
        Individual parent2;

        for (int i = 0; i < populationList.size() - 1; i++) {
            parent1 = populationList.get(i);
            parent2 = populationList.get(i + 1);
            List<Individual> children = crossoverHelper(parent1, parent2, crossoverPoint);
            for (Individual child : children) {
                newPop.addIndividual(child);
            }  
        }

        return newPop;
        
    }

    public Population uniformCrossover(Population pop) {

    } 

    public Population recombine(Population pop) {
        switch(this.crossoverType) {
            case "1c":
                return onePointCrossover(pop);
            case "uc":
                return uniformCrossover(pop);
            default:
                System.out.println("Sorry, that crossover type doesn't exist.");
                Population defaultPop = new Population(0);
                return defaultPop;
        }
    }

    public Population rankSelection(Population pop) {
        
        List<Individual> offspring = pop.popList;

        for (Individual ind : offSpring) {
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
        for(int i = 0; i < offSpring.size() / 2; i++) {
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

    /**
     * To be called on an instance of Genetic Algorithms.
     */
    public void optimize() {
        for (int i = 0; i < this.iterations; i++) {

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