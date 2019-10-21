import java.util.Random;
import java.lang.Math;
import java.util.Arrays;

public class Individual implements Comparable<Individual> {

    /**
     * Number of variables in the MAXSAT problem.
     */
    public int individualNum;

    /**
     * Array object containing 0's and 1's to represent particular problem solution.
     */
    public int[] individual;

    /**
     * Random constant used multiple times to generate individuals.
     */
    private static Random rand;

    /**
     * The individual's fitness - originally initialized to -1 (so that it is not conflated with 0).
     */
    public int fitness = -1;
    
    /**
     * Constructor for the Individual object type. The Individual is represented as an array of zeros and 1s and
     * indexed occuring to the given variable numbers in the problem. 0's represent an assignment of a variable to
     * false, and 1's represent an assignment of a variable to true -- i.e. 0 at index 3 would represent a -3
     * within the MAXSAT problem, and so on.
     * 
     * The object has the following attributes that can be accessed - the individual num (i.e. number of variables
     * in the problem, and an Individual array, which contains 0's and 1's as described above).
     * 
     * @param int individualNum The number of variables in the maxsat problem.
     */
    public Individual(int individualNum) {
        this.individualNum = individualNum;
        individual = new int[individualNum+ 1];
        rand = new Random();
    }

    /**
     * Randomly generates 0's and 1's to be placed in an Individual object. Called everytime a Population is
     * randomly made. Indexing in the following way: 1 = 1, 2 = 2, etc. ignoring the zero index slot.
     * @return Individual A newly randomly generated Individual.
     */
    public Individual generateIndividual() {
        Individual randIndividual = new Individual(this.individualNum);
        for (int i = 1; i < individual.length; i++) {
            randIndividual.setValue(i, rand.nextInt(2));
        }
        return randIndividual;
    }

    /**
     * Helper method to set a value in and Individual array. Used when randomly generating Indidividuals.
     * @param int index The index we want to set the value to.
     * @param int value The value we want to set the index to.
     */
    public void setValue(int index, int value) {
        individual[index] = value;
    }

    /**
     * Getter to extract values from Individual arrays based on an index.
     * @param int index The index of the array that we want to get the value from.
     * @return int The particular value (either 0 or 1).
     */
    public int getValue(int index) {
        return this.individual[index];
    }

    /**
     * Returns the size of the Individual object -- i.e. the number of variables in the MAXSAT problem.
     * @return The number of variables in the MAXSAT problem.
     */
    public int size() {
        return this.individualNum;
    }

    /**
     * Getter to retrieve the Individual object's array.
     * @return int[] The Individual object's array.
     */
    public int[] getIndividualList() {
        return individual;
    }

    /**
     * Returns the number of clauses that the individual does not satisfy  (which is the answer to the
     * problem we are seeking to find).
     * @param ClauseList problem The MAXSAT problem at hand in the form of an object - contains relevant info
     *                   (see ClauseList.java).   
     * @return int An integer representing the number of clauses that are NOT satisfied by the particular
     *             Individual object (MAXSAT is looking to minimize this).
     */
    public int getFitness(ClauseList problem) {

        if (this.fitness != -1 ) {
            return this.fitness;
        }

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
        return notSatisfied;
    }

    /**
     * Using the getFitness function above, this sets and Individual's fitness attribute once it has been
     * calculated (that way it is easily accessible).
     * @param problem The MAXSAT problem at hand in the form of an object - contains relevant info 
     *                   (see ClauseList.java). 
     */
    public void setFitness(ClauseList problem) {
        int fitness = this.getFitness(problem);
        this.fitness = fitness;
    }

     /**
     * Helper method that given an index, swaps 0's to 1's and 1's to 0's within the Individual.indvidual.
     * Used in our mutate method.
     * @param int index Index at which we want to swap a 0 or 1.
     */
    public void swap(int index) {
        if (individual[index] == 0) {
            individual[index] = 1;
        } else {
            individual[index] = 0;
        }
    }

    /**
     * Given a probability, iterates over each value in the Individual array and decides whether or not to
     * swap their current value. This adds an element of slight variation within the Genetic Algorithm.
     * @param double probability The mutation probability value.
     */
    public void mutate(double probability) {
         int prob = (int) (probability * 100);
         boolean update = false;
         for (int i = 1; i < individual.length; i++) {
             update = (rand.nextInt(100) <= prob);
             if (update) {
                 this.swap(i);
             }
         }
     }

    /**
     * Individual implements the Comparable interface and this method overrides the compareTo method. Used
     * to compare fitnesses of other Individuals to sort Populations of individuals quickly.
     * @param Individual otherInd The other individual we want to compare ourselves to.
     * @return int A negative integer, zero, or a positive integer as this object is less than, equal to,
     *             or greater than the specified object.
     */
    public int compareTo(Individual otherInd) {
        return otherInd.fitness - this.fitness;
    }

    /**
     * Returns a String representation of the object.
     * @return String representation of Individual.
     */
    public String toString() {
        String representation = "";
        for (int i = 1; i < individual.length; i++) {
            representation += individual[i];
        }
        return representation;
    }
}