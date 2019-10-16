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