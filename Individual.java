import java.util.Random;

public class Individual {

    public ClauseList problemInfo;

    public int[] individual;
    
    public Individual(ClauseList problemInfo) {
        this.problemInfo = problemInfo;
        individual = new int[this.problemInfo.getVariableNum() + 1];
    }

    // given the individual, we will then loop through with even probability on each iteration and place either
    // zeros or ones with an even probabiliy in the individual object
    // indexing in the following way: 1 = 1, 2 = 2, etc. ignoring the zero index slot
    public Individual generateIndividual() {
        Random rand = new Random();
        Individual randIndividual = new Individual(problemInfo);
        for (int i = 1; i < individual.length; i++) {
            randIndividual.setValue(i, rand.nextInt(2));
        }
        return randIndividual;
    }

    public void setValue(int index, int value) {
        individual[index] = value;
    }

    public int size() {
        return individual.length;
    }

    // public void mutate() {

    // }

    // public int fitness(ClauseList clause) {

    // }

    public String toString() {
        String representation = "";
        for (int i = 1; i < individual.length; i++) {
            representation += individual[i];
        }
        System.out.println(individual.length);
        return representation;
    }
}