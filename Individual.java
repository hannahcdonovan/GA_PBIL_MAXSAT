import java.util.Random;

public class Individual implements Comparable<Individual> {

    // public ClauseList problemInfo;

    public int individualNum;

    public int[] individual;

    public Random rand;

    public int fitness = -1;
    
    public Individual(int individualNum) {
        this.individualNum = individualNum;
        individual = new int[individualNum+ 1];
        rand = new Random();
    }

    // given the individual, we will then loop through with even probability on each iteration and place either
    // zeros or ones with an even probabiliy in the individual object
    // indexing in the following way: 1 = 1, 2 = 2, etc. ignoring the zero index slot
    public Individual generateIndividual() {
        Individual randIndividual = new Individual(this.individualNum);
        for (int i = 1; i < individual.length; i++) {
            randIndividual.setValue(i, rand.nextInt(2));
        }
        return randIndividual;
    }

    public void setValue(int index, int value) {
        individual[index] = value;
    }

    public int getValue(int index) {
        return this.individual[index];
    }

    public int size() {
        return individual.length;
    }

    public void swap(int index) {
        if (individual[index] == 0) {
            individual[index] = 1;
        } else {
            individual[index] = 0;
        }
    }


    /*
        Returns the number of clauses that the individual does not satisfy 
    */
    public int getFitness(ClauseList problem) {

        int notSatisfied = 0;

        for (int i = 0; i < problem.getList().size(); i++) {
            boolean check = false;
            int[] clause = problem.getList().get(i).getArray();
            for(int j = 0; j < clause.length; j++) {
                int literal = clause[j];
                if (literal < 0) {
                    if (this.getValue(Math.abs(literal)) == 0) {
                        check = true;
                    }
                } else {
                    if (this.getValue(Math.abs(literal)) == 1) {
                        check = true;
                    }
                }
            }
            if(!check) {
                notSatisfied++;
            } 
        }
        this.fitness = notSatisfied;
        return notSatisfied;
    }

    // public void mutate(double probability) {
    //     int prob = (int) probability * 100;
    //     boolean update = false;
    //     System.out.println("Individual before mutation " + individual);
    //     for (int i = 0; i < individual.length; i++) {
    //         update = rand.nextInt(prob)==0;
    //         if (update) {
    //             this.swap(i);
    //         }
    //     }
    //     System.out.println("Individual after mutation " + individual);
    // }

    // public int fitness(ClauseList clause) {

    // }

    @Override
    public int compareTo(Individual otherInd) {
        return otherInd.fitness - this.fitness;
    }

    public String toString() {
        String representation = "";
        for (int i = 1; i < individual.length; i++) {
            representation += individual[i];
        }
        return representation;
    }
}