import java.util.ArrayList;
import java.util.List;

public class Population {

    List<Individual> popList = ArrayList<Individual>();
    int populationNum;
    
    public Population(int individualNum) {
        this.populationNum = individualNum;
    }

    //populates list with Individuals randmly
    //called to start GA 
    public void generateRandomPopulation(int variables) {

    }

    //popualtes list of Inviduals according to pbil vector probabilties 
    //called for each pbil iteration
    public void generateRandomVectorPopulation(int variables, int[] pbilVec) {

    }



}