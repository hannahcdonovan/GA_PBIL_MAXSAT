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
    String crossoverType;
	double crossoverProb;
    double mutationProb;
    int iterations;
    Population currentPopulation; 

    public GeneticAlgorithm(ClauseList problem, int popSize, String selectionType, String crossoverType,
                            double crossoverProb, double mutationProb, int iterations) {
        this.problem = problem;
        this.popSize = popSize;
        this.selectionType = selectionType;
        this.crossoverType = crossoverType;
        this.crossoverProb = crossoverProb;
        this.mutationProb = mutationProb;
        this.iterations = iterations;

        Population newPop = new Population(this.popSize);
        newPop.generateRandomPopulation(this.problem.getVariableNum());
        this.currentPopulation = newPop;
        System.out.println(this.currentPopulation);
    }


    public List<Individual> onePointCrossoverHelper(Individual firstParent, Individual secondParent) {
        List<Individual> children = new ArrayList<Individual>();

        Random generator = new Random();
        //Two parents will produce four children - child1 and child 2 will be doubled;
        Individual child1;
        Individual child2;

        int crossoverPoint = generator.nextInt(this.problem.getVariableNum()) + 1;

        int[] c1 = new int[this.problem.getVariableNum() + 1];
        int[] c2 = new int[this.problem.getVariableNum() + 1];


        for(int i = 1; i < crossoverPoint + 1; i ++) {
            c1[i] = firstParent.getValue(i);
            c2[i] = secondParent.getValue(i);
        }

        for(int i = crossoverPoint + 1; i < this.problem.getVariableNum() + 1; i++) {
            c1[i] = secondParent.getValue(i);
            c2[i] = firstParent.getValue(i);
        }

        child1 = new Individual(this.problem.getVariableNum());
        child2 = new Individual(this.problem.getVariableNum());
        child1.individual = c1;
        child2.individual = c2;

        children.add(child1);
        children.add(child2);

        return children;
    }


    public List<Individual> uniformCrossoverHelper(Individual firstParent, Individual secondParent) {
        List<Individual> children = new ArrayList<Individual>();

        Random generator = new Random();
        //Two parents will produce four children - child1 and child 2 will be doubled;
        Individual child1 = new Individual(this.problem.getVariableNum());
        Individual child2 = new Individual(this.problem.getVariableNum());

        int[] c1 = new int[this.problem.getVariableNum() + 1];
        int[] c2 = new int[this.problem.getVariableNum() + 1];

        for (int i = 1; i < this.problem.getVariableNum() + 1; i++)  {
            int randNum = generator.nextInt(2);
            if(randNum == 0) {
                c1[i] = firstParent.getValue(i);
                c2[i] = secondParent.getValue(i);
            } else {
                c1[i] = secondParent.getValue(i);
                c2[i] = firstParent.getValue(i);
            }
        } 

        child1.individual = c1;
        child2.individual = c2;

        children.add(child1);
        children.add(child2);

        return children; 

    }
 

    public void recombine() {
        Population newPop = new Population(this.currentPopulation.size());

        Random generator = new Random();

        //Pick two candidates from the population, and then perform crossover
        List<Individual> populationList = this.currentPopulation.getPopulationList();
        Collections.shuffle(populationList);

        Individual parent1;
        Individual parent2;

        for (int i = 0; i < populationList.size() - 1; i += 2) {
            parent1 = populationList.get(i);
            parent2 = populationList.get(i + 1);
            int cutoff = (int) Math.round(this.crossoverProb * 100); //using crossover probability 
            int randNum = generator.nextInt(100);
            if(randNum < cutoff) {
                if(this.crossoverType.equals("1c")) {
                    List<Individual> children = onePointCrossoverHelper(parent1, parent2);
                    for (Individual child : children) {
                        newPop.addIndividual(child);
                    }
                } else if (this.crossoverType.equals("uc")) {
                    List<Individual> children = uniformCrossoverHelper(parent1, parent2);
                    for(Individual child : children) {
                        newPop.addIndividual(child);
                    }
                } else {
                    System.out.println("something bad with crossover");
                }
            }
        }
        if (newPop.size() != this.currentPopulation.size()) {
            int difference = this.currentPopulation.size() - newPop.size();

            for(Individual ind: this.currentPopulation.getPopulationList()) {
                int fitness = ind.getFitness(this.problem);
            }

            Collections.reverse(this.currentPopulation.popList);
            for(int i = 0; i < difference; i++) {
                newPop.addIndividual(this.currentPopulation.getIndividual(i));
            }
        }

        this.currentPopulation = newPop;
    }


    public void rankSelection() {

        List<Individual> offSpring = this.currentPopulation.popList;

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
        for(int i = 0; i < offSpring.size(); i++) {
            int index = rand.nextInt(indList.size());
            newPopList.add(indList.get(index));
        }

        Population newPop = new Population(this.currentPopulation.size());
        newPop.popList = newPopList;
        this.currentPopulation = newPop;
    }



    public void tournamentSelection() {

        List<Individual> offSpring = this.currentPopulation.popList;
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
        this.currentPopulation = newPop;
    }


    public void selectionByGroups() {

        List<Individual> offSpring = this.currentPopulation.popList;
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

        this.currentPopulation = newPop;
    }


    public void select() {
        switch (this.selectionType) {
            case "rs":
                System.out.println("Rank Selection");
                rankSelection();
                break;
            case "ts":
                System.out.println("Tournament Selection");
                tournamentSelection();
                break;
            case "sbg":
                System.out.println("Selection By Groups");
                selectionByGroups();
                break;
            default:
                System.out.println("This selection type is not available.");
                //Population defaultPop = new Population(0);
                //return defaultPop;
                break;
        }
    }

    public Individual findBest() {
        Individual best = this.currentPopulation.popList.get(0);
        int min = best.getFitness(this.problem);

        for(int i = 1; i < this.currentPopulation.size(); i++) {
            Individual currentInd = this.currentPopulation.popList.get(i);
            int fitness = currentInd.getFitness(this.problem);
            if(fitness < min) {
                best = currentInd;
                min = fitness;
            }
        }
        return best;
    }

    /**
     * To be called on an instance of Genetic Algorithms.
     */
    public void optimize() {

        for (int i = 0; i < this.iterations; i++) {
            this.select();
            this.recombine();
            for(Individual ind : this.currentPopulation.popList) {
                ind.mutate(this.mutationProb);
                int fitness = ind.getFitness(this.problem);
                System.out.println(ind + " -> " + fitness);
            }
            System.out.println(this.currentPopulation.size());
            System.out.println("_______________");
        }
        Individual best = this.findBest();
        System.out.println(best + " - " + best.getFitness(this.problem));
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

        GeneticAlgorithm ga = new GeneticAlgorithm(cl, 20, "rs", "uc", 0.7, 0.01, 3);
        //Population newPop = ga.select(pop);
        //System.out.println("___________");
        ga.optimize();


    }
}
