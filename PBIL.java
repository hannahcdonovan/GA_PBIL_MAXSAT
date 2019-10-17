import java.util.Arrays;
import java.util.Random;

public class PBIL {

	double[] pbilVec;
	ClauseList problem;
	int popSize;
	double posLearnRate;
	double negLearnRate;
	double mutateProb;
	double mutateAmount;
	int iterations;
	Population currentPop;


/* 
    updateVec updates the PBIL vector components towards the best individual and away from the worst individual
   
    Using the information of the best and worst individuals, the components of the PBIL vector are modified
    such that they more closely resemble the best individual and diverge from the worst individual (wherever
    the worst and best individual do not match)

    @param best - Individual which was identified as the most fit in the population 
    @param worst - Individual which was indentified as the least fit in the population 

*/
    public void updateVec(Individual best, Individual worst) {

    	for(int i = 1; i < best.individual.length; i++) {
    		int bestLiteral = best.getValue(i);
    		int worstLiteral = worst.getValue(i);

    		if(bestLiteral != worstLiteral) {
    			double posIncrement = pbilVec[i - 1] * (1.0 - posLearnRate) + posLearnRate * bestLiteral;
    			double negIncrement = posIncrement * (1.0 - negLearnRate) + negLearnRate * bestLiteral;
    			pbilVec[i - 1] = negIncrement;
    		} else {
    			double posIncrement = pbilVec[i - 1] * (1.0 - posLearnRate) + posLearnRate * bestLiteral;
    			pbilVec[i - 1] = posIncrement;
    		}
    	}
    }



/*
    mutate() possibly changes the PBIL vector 
    According to the mutation probabilty, each component of the vector may be changed by adding or 
    subtracting the predetermined mutation amount. This change, however, will never allow the vector 
    component to be less than 0 or more than 1. 

*/
    public void mutate() {
    	Random generator = new Random();
    	int cutoff = (int) Math.round(100 * this.mutateProb);
    	for(int i = 0; i < pbilVec.length; i++) {
    		int result = generator.nextInt(100);
    		if(result < cutoff) {
    			if(result % 2 == 0) { //mutate up
    				pbilVec[i] = Math.min(pbilVec[i] + this.mutateAmount, 1.0);
    			} else { //mutate down
    				pbilVec[i] = Math.max(pbilVec[i] - this.mutateAmount, 0.0);
    			}
 			}
    	}
    }

/*  
    findBestWorst() calculates the fitness of each Individual in the population and identifies the 
    individual with the highest fitness and the individual with the lowest fitness. If several individuals
    in the population have the highest/lowest fitnesses, just one is returned. 

    @return Individual[] with the best Individual at index 0 and the worst Individual at index 1
*/
    public Individual[] findBestWorst() {
    	Individual bestInd = this.currentPop.popList.get(0);
    	Individual worstInd = this.currentPop.popList.get(0);

    	int max = 0;
    	int min = this.problem.getList().size();

    	for(int i = 0; i < currentPop.size(); i++) {
    		Individual currentInd = this.currentPop.popList.get(i);
    		int fitness = currentInd.getFitness(this.problem);
    		if(fitness > max) {
    			max = fitness;
    			worstInd = this.currentPop.popList.get(i);
    		} else if(fitness < min) {
    			min = fitness; 
    			bestInd = this.currentPop.popList.get(i);
    		}
    	}
    	Individual[] results = new Individual[]{bestInd, worstInd};
    	return results;
    }

/*
    optimize() executes the functions of the PBIL algorithm. For the specified number of iterations, 
    a population is created according to the specifications of the probaility vector, the best/worst 
    Individuals are identified, the probaility vector is adjusted according to these two individuals, 
    and the process is repeated. Afterwards, the algorithm should converge on the optimal solution. 

*/
    public void optimize() {

    	for(int i = 0; i < this.iterations; i ++) {
    		Individual[] results = this.findBestWorst();
    		this.updateVec(results[0], results[1]);
    		this.mutate();
    		System.out.println((i + 1) + " BEST IS 	" + results[0].getFitness(this.problem));
    		this.currentPop.generateRandomVectorPopulation(this.problem.getVariableNum(), this.pbilVec);
    	}
    	Individual suggestedBest = new Individual(this.problem.getVariableNum());
    	for(int i = 1; i <= this.problem.getVariableNum(); i++) {
    		int num = (int) Math.round(this.pbilVec[i-1]);
    		suggestedBest.setValue(i, num);
    	}
    	System.out.println("Suggests the best is " + suggestedBest.getFitness(this.problem) + ": " + suggestedBest);
    }

    public static String vecToString(double[] vec) {
    	String result = "";

    	for(int i = 0; i < vec.length; i++) {
    		result += vec[i];
    		result += " ";
    	}
    	return result;
    }

/*
    PBIL class represents an instance of the PBIL algorithm with the specific parameters. 

    @param problem - ClauseList storing the information of each clause for the MAXSAT problem
    @param posLearnRate - the positive learning rate
    @param negLearnRate - the negative learning rate
    @param mutateProb - the probaility that a mutation will occur to each vector component
    @param mutateAmount - the amount by which a vector component will change if it is mutated
    @param iterations - the number of iterations that this PBIL will run for 
    @param popSize - the number of individuals that will be generated in each iteration's population 
*/
    public PBIL(ClauseList problem, double posLearnRate, double negLearnRate, double mutateProb, double mutateAmount, int iterations, int popSize) {
    	this.problem = problem;
    	this.posLearnRate = posLearnRate;
    	this.negLearnRate = negLearnRate;
    	this.mutateProb = mutateProb;
    	this.mutateAmount = mutateAmount;
    	this.iterations = iterations;
    	this.popSize = popSize;

    	int variables = problem.getVariableNum();
    	double[] vec = new double[variables];
    	Arrays.fill(vec, 0.50);
    	this.pbilVec = vec;

    	Population pop = new Population(this.popSize);
    	pop.generateRandomVectorPopulation(problem.getVariableNum(), this.pbilVec);
    	this.currentPop = pop;
    }
}