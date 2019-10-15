import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Population {

    public List<Individual> popList;
    public int populationNum;
    
    public Population(int populationNum) {
        this.populationNum = populationNum;
        popList = new ArrayList<Individual>();
    }

    //populates list with Individuals randmly
    //called to start GA 
    public Population generateRandomPopulation(int variableNum) {
        Individual individual = new Individual(variableNum);
        for (int i = 0; i < this.populationNum; i++) {
            popList.add(individual.generateIndividual());
        }
        return this;
    }

    public List getPopulationList() {
        return popList;
    }

    public Individual getIndividual(int index) {
        return popList.get(index);
    }

    public int size() {
        return popList.size();
    }

    //popualtes list of Inviduals according to pbil vector probabilties 
    //called for each pbil iteration
    public void generateRandomVectorPopulation(int variables, double[] pbilVec) {

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

    public String toString() {
    	String result = "";
    	for(int i = 0; i < popList.size(); i++) {
    		Individual ind = this.popList.get(i);
    		String indString = ind.toString();
    		result += indString + "\n";
    	}
    	return result;
    }

/*
	public static void main(String[] args) {
        Population pop = new Population(10);
        double[] vec = new double[]{0.1, 0.1, 0.9, 0.9};
        pop.generateRandomVectorPopulation(4, vec);
        System.out.println(pop);

    }
*/
}