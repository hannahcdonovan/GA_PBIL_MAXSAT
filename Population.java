import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Population {

    /**
     * List of Individual objects representing the total population.
     */
    public List<Individual> popList;

    /**
     * The number of individuals in the population.
     */
    public int populationNum;
    
    /**
     * Constructor for the Population object. Object that is a list of Individuals. Maintained in both
     * PBIL and Genetic Algorithms.
     * @param populationNum The number of individuals in the given population.
     */
    public Population(int populationNum) {
        this.populationNum = populationNum;
        popList = new ArrayList<Individual>();
    }

    //populates list with Individuals randmly
    //called to start GA
    /**
     * Randomly (i.e. with 0.5 probability) populates the popList attribute of Population object with random
     * individuals.
     * @param int variableNum The number of variables in the MAXSAT problem. Pertains to how we construct the
     *  indiviuals.
     */
    public void generateRandomPopulation(int variableNum) {
        Individual individual = new Individual(variableNum);
        for (int i = 0; i < this.populationNum; i++) {
            popList.add(individual.generateIndividual());
        }
        //return this;
    }

    /**
     * Given an individual, adds an individual to the Population's ArrayList.
     * @param Individual ind The Individual that we want to add to the Population.
     */
    public void addIndividual(Individual ind) {
        popList.add(ind);
    }

    /**
     * Returns the Population's popList attribute.
     * @return
     */
    public List<Individual> getPopulationList() {
        return popList;
    }

    /**
     * Given an index, returns an individual from the Population's popList.
     * @param int index The index of the popList that we want to get out.
     */
    public Individual getIndividual(int index) {
        return popList.get(index);
    }

    /**
     * Get the size of the populationList currently.
     * @return The size of the populationList.
     */
    public int size() {
        return popList.size();
    }

    /**
     * Populates list of Individuals according to the PBIL vector probabilities. Called for each
     * PBIL iteration.
     * @param variables The number of variables in the MAXSAT problem.
     * @param pbilVec The PBIL vector that determines how the random population is created. Initially all 0.5
     *  in value.
     */
    public void generateRandomVectorPopulation(int variables, double[] pbilVec) {

    	this.popList.clear();

    	Random generator = new Random();
    	for(int i = 0; i < this.populationNum; i++) {
    		Individual newInd = new Individual(variables);
    		for(int j = 1; j <= variables; j++) {
    			int cutoff = (int) Math.round(pbilVec[j-1] * 100);
    			int result = generator.nextInt(100);
    			if(result > cutoff) {
    				newInd.setValue(j, 0);
    			} else {
    				newInd.setValue(j, 1);
    			}
    		}
    		this.popList.add(newInd);
    	}
    }

    /**
     * Finds the best individual from the population.
     * @param ClauseList problem The ClauseList object representing the MAXSAT problem to be solved.
     * @return The "best" individual from the population - i.e. the one with the highest fitness, which is
     *  represented by the number of clauses NOT solved.
     */
	public Individual findBest(ClauseList problem) {
        Individual best = popList.get(0);
        int min = best.getFitness(problem);
        best.setFitness(problem);

//        System.out.println("the current best is: " + best.getFitness(problem));

        for (int i = 1; i < popList.size(); i++) {
            Individual currentInd = popList.get(i);
            int fitness = currentInd.getFitness(problem);
            if (fitness < min) {
                best = currentInd;
                best.setFitness(problem);
                min = fitness;
            }
        }
//        System.out.println("the new best is " + best.getFitness(problem));
        return best;
    }

    /**
     * Returns a string representation of the Population object.
     * @return String representation of the Population object.
     */
    public String toString() {
    	String result = "";
    	for(int i = 0; i < popList.size(); i++) {
    		Individual ind = this.popList.get(i);
    		String indString = ind.toString();
    		result += indString + "\n";
    	}
    	return result;
    }

}