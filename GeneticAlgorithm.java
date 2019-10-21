import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.ArrayList;
import java.util.Collections;

public class GeneticAlgorithm {

    /**
     * The ClauseList object representing the MAXSAT problem we are trying to solve.
     */
    public ClauseList problem;

    /**
     * The population size.
     */
    public int popSize;

    /**
     * The selection type - ts, rs, sg.
     */
    public String selectionType;

    /**
     * The crossover type - 1c, uc.
     */
    public String crossoverType;
    
    /**
     * The probability of actually performing the crossover - whether 1c or uc.
     */
    public double crossoverProb;
    
    /**
     * The probability of mutating each value in the individual.
     */
    public double mutationProb;

    /**
     * The number of iterations.
     */
    public int iterations;

    /**
     * The current population.
     */
    public Population currentPopulation;

    /**
     * The best fitness we find after optimize
     */

    public int bestOverallFitness;

    /**
     * The GeneticAlgorithm constructor. Every time a new GeneticAlgorithm instance is created, it also
     * generates a random population to start the problem (i.e. the initial population that is updated each
     * time).
     * @param problem The ClauseList version of the MAXSAT problem.
     * @param popSize The number of individuals in the population.
     * @param selectionType The type of selection - either ts (Tournament Selection), rs (Rank Selection), sg
     *                      (Selection by Groups).
     * @param crossoverType The type of crossover - either 1c (One-Point Crossover),  uc (Uniform Crossover).
     * @param crossoverProb The probabiliity of actually performing the crossover.
     * @param mutationProb  The probability of swaping each individual bit in the Individual object.
     * @param iterations    The numbe of iterations to run the algorithm fro.
     */
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
        System.out.println("-------------------------" + 
                            "\nGenetic Algorithm for MAXSAT \n" + 
                            "Population Size: " + this.popSize + 
                            "\nSelection Type: " + selectionDeducer(this.selectionType) + 
                            "\nCrossover Type: " + crossOverDeducer(this.crossoverType) + 
                            "\nCrossover Probability: " + this.crossoverProb + 
                            "\nMutation Probability: " + this.mutationProb +
                            "\nNumber of Iterations: " + this.iterations + 
                            "\n-------------------------");
    }

    /**
     * Helper method used to clearly express the type of selection being used when creating 
     * an instance of GeneticAlgorithm.
     * @param selectionType The string inputed that indicates the selection type - ts, rs, sg.
     * @return  More readable string format that indicates the type of selection being used.
     */
    public String selectionDeducer(String selectionType) {
        String format = "";
        if (selectionType.equals("ts")) {
            format = "Tournament Selection";
        } else if (selectionType.equals("rs")) {
            format = "Rank Selection";
        } else {
            format = "Selection By Groups";
        }
        return format;
    }

    /**
     * Helper method used to clearly express the type of crossover being used when creating an
     * instance of GeneticAlgorithm.
     * @param crossoverType The String inputed that indicates the selection type - 1c, uc.
     * @return More readable string format that indicates the type of crossover being used.
     */
    public String crossOverDeducer(String crossoverType) {
        String format = "";
        if (crossoverType.equals("uc")) {
            format = "Uniform Crossover";
        } else if (crossoverType.equals("1c")) {
            format = "One-Point Crossover";
        }
        return format;
    }

    /**
     * Helper method for one point crossover. Does the actual 1 point crossover between two parents,
     * whereas the recombine method performs crossover for all individuals in the population.
     * @param firstParent The first parent we are looking to combine with the second.
     * @param secondParent The second parent we are looking to combine with first.
     * @return List<Individual> A list containing the newly formed children after crossover.
     */
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

    /**
     * Helper method for uniform crossover. Does the actual uniform crossover between two parents,
     * whereas the recombine method performs crossover for all individuals in the population.
     * @param firstParent The first parent we want to recombine with the second.
     * @param secondParent The second paretn that we want to recombine with the first.
     */
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
 
    /**
     * Using the GeneticAlgorithm's instance of the crossoverProb, which determines whether or
     * not to actually do the crossover, this method randomly chooses
     * individuals from the currentPopulation to recombine with using 1c or uc. At the end, the
     * method also picks the most fit inidivuals from the new population created and adds them to
     * the new population until it is the same size as the previously population.
     */
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
                ind.setFitness(this.problem);
            }

            Collections.reverse(this.currentPopulation.popList);
            for(int i = 0; i < difference; i++) {
                newPop.addIndividual(this.currentPopulation.getIndividual(i));
            }
        }

        this.currentPopulation = newPop;
    }

    /**
     * Method that performs rankSelection. Assigns each individual in the offSpring pool a fitness attribute.
     * Sorts the offSpring, and adds them to a list based on their "rank" - i.e. individuals with higher rank
     * appear more times (proportional to their rank). Then, randomly grab indices from this auxillary list and
     * add to newPopulation list (that is the same size as the offspring list).
     */
    public void rankSelection() {

        List<Individual> offSpring = this.currentPopulation.popList;

        for (Individual ind : offSpring) {
            ind.setFitness(this.problem);
        }

        Collections.sort(offSpring);

        List<Individual> indList = new ArrayList<Individual>();

        // Given that the offspring list is sorted
        // ranking
        for (int i = 0; i < offSpring.size(); i++) {
            Individual currentInd = offSpring.get(i);
            for (int j = 0; j < i + 1; j++) {
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

    /**
     * Method that performs tournament selection. Does tournament selection for half the offSpring size,
     * then shuffles the offSpring list and does it again so that we end up with a new population the
     * same size as the current population.
     */
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

    /**
     * Method that performs selection by groups. Takes half of the current population, calculates the fitness,
     * and does the same for the other half. Compares the fitness of both halves. For the more fit half, doubles
     * it and puts it in the newPopulation list.
     */
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

    /**
     * Given the above selection functions, chooses which selection to perform based on the inputted
     * selection type.
     */
    public void select() {
        switch (this.selectionType) {
            case "rs":
                rankSelection();
                break;
            case "ts":
                tournamentSelection();
                break;
            case "sbg":
                selectionByGroups();
                break;
            default:
                System.out.println("This selection type is not available.");
                break;
        }
    }

    /**
     * Pulls the solution information from an individual and returns it in the form of an 
     * List of integers
     */
    public static List<Integer> grabInfo(Individual ind) {
        int[] arr = ind.individual;
        List<Integer> result = new ArrayList<Integer>();

        for(int i = 1; i < arr.length; i++) {
            result.add(arr[i]);
        }
        return result;

    }

    /**
     * Takes a List of integers representing the solution of an Individual and creates an 
     * Individual according to this information. 
     */
    public static Individual makeInd(List<Integer> arr, int size) {
        int[] info = new int[size + 1];
        Individual result = new Individual(size);
        for(int i = 0; i < arr.size(); i++) {
            info[i + 1] = arr.get(i);
        }
        result.individual = info;
        return result;
    }

    /**
     * Executes the functions of the GeneticAlgorithm. For the specified number of iterations, 
	 * creates a randomly generated population of individuals, selects them, recombines them,
     * mutates them, and adjusts the fitness. Afterwards, the algorithm should converge on 
     * the optimal solution, but not always (contingent upon the parameters used). 
     */
    public void optimize() {

        Individual popBest = new Individual(this.problem.getVariableNum());

        Individual best = this.currentPopulation.findBest(this.problem);
        List<Integer> bestSol = grabInfo(best);
        int bestFitness = best.fitness;

        for (int i = 0; i < this.iterations; i++) {
            this.select();
            this.recombine();
            for (Individual ind : this.currentPopulation.popList) {
                ind.mutate(this.mutationProb);
                ind.setFitness(this.problem);
            }

            popBest = this.currentPopulation.findBest(this.problem);
            popBest.setFitness(this.problem);

            if (popBest.fitness < bestFitness) {
                best = popBest;
                bestSol = grabInfo(popBest);
                bestFitness = popBest.fitness;
            }
            System.out.println((i + 1) + " BEST IS " + bestFitness);
        }

        Individual result = makeInd(bestSol, this.problem.getVariableNum());
        
        System.out.println("Suggests the best is " + result.getFitness(this.problem) + ": " + result);
        //this.bestOverallFitness = result.getFitness(this.problem);
        //System.out.println("Solution is " + result.getFitness(this.problem) + ": " + result);
    }

}