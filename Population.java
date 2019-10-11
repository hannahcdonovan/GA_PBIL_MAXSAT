import java.util.ArrayList;
import java.util.List;

public class Population {

    public List<Individual> popList;
    public int populationNum;
    
    public Population(int populationNum) {
        this.populationNum = populationNum;
        popList = new ArrayList<Individual>();
    }

    //populates list with Individuals randmly
    //called to start GA 
    public List generateRandomPopulation(int variableNum) {
        Individual individual = new Individual(variableNum);
        for (int i = 0; i < this.populationNum; i++) {
            popList.add(individual.generateIndividual());
        }
        return popList;
    }

    //popualtes list of Inviduals according to pbil vector probabilties 
    //called for each pbil iteration
    public void generateRandomVectorPopulation(int variables, int[] pbilVec) {

    }

}