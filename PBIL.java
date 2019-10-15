import java.util.Arrays;

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



    }

    public int getFitness(Individual ind) {

    	int satisfied = 0;

    	for(int i = 0; i < this.problem.clauseList.size(); i++) {
    		boolean wrong = false;
    		int[] clause = this.problem.clauseList.get(i).clauseArray;
    		for(int j = 0; j < clause.length; j++) {
    			int literal = clause[j];
    			if(literal < 0) {
    				if(ind.getValue(Math.abs(literal)) == 1) {
    					wrong = true;
    				}
    			} else {
    				if(ind.getValue(Math.abs(literal)) == 0) {
    					wrong = true;
    				}
    			}
    		}
    		if(!wrong) {
    			satisfied++;
    		}
    	}
    	return satisfied;
    }
    

    public Individual[] findBestWorst() {
    	Individual bestInd = this.currentPop.popList.get(0);
    	Individual worstInd = this.currentPop.popList.get(0);

    	int max = 0;
    	int min = this.problem.clauseList.size();

    	for(int i = 0; i < currentPop.size(); i++) {
    		int fitness = this.getFitness(this.currentPop.popList.get(i));
    		System.out.println(fitness);

    		if(fitness > max) {
    			max = fitness;
    			bestInd = this.currentPop.popList.get(i);
    		} else if(fitness < min) {
    			min = fitness; 
    			worstInd = this.currentPop.popList.get(i);
    		}
    	}
    	Individual[] results = new Individual[]{bestInd, worstInd};
    	System.out.println("best is " + max);
    	System.out.println("worst is " + min);
    	return results;
    }

    public void optimize() {
    	Individual[] results = this.findBestWorst();
    	this.updateVec(results[0], results[1]);
    }

    public PBIL(ClauseList problem, double posLearnRate, double negLearnRate, double mutateProb, double mutateAmount, int iterations, int popSize) {
    	this.problem = problem;
    	this.posLearnRate = posLearnRate;
    	this.negLearnRate = negLearnRate;
    	this.mutateProb = mutateProb;
    	this.mutateAmount = mutateAmount;
    	this.iterations = iterations;
    	this.popSize = popSize;

    	int variables = problem.variableNum;
    	double[] vec = new double[variables];
    	Arrays.fill(vec, 0.50);
    	this.pbilVec = vec;

    	Population pop = new Population(this.popSize);
    	pop.generateRandomVectorPopulation(problem.variableNum, this.pbilVec);
    	this.currentPop = pop;
    	System.out.println(this.currentPop);
    }
}