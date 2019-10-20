import java.lang.*;

public class Test {

    private static int TESTNUM = 10;
    public static void main(String[] args) {

        ClauseList problem = Main.readFile("t5pm3-7777.spn.cnf");

        //how many clauses in the file?
        int clauses = 750;
        //best solution from website
        int soln = 78;

        int popSize = 100; // population size for GA, number of samples for PBIL
        int iterations = 100;

        String selectionType = "ts";
        String crossoverType = "1c";

        double probCross = 0.7;
        double[] probMutate = {0.0, 0.005, 0.01, 0.05, 0.1, 0.5, 1.0};

        //Perform test for GA on the first file (TESTNUM times)
        for(double probMut : probMutate) {
            //System.out.println("Probability of mutation = " + probMut);
            GeneticAlgorithm ga = new GeneticAlgorithm(problem, popSize , selectionType, crossoverType, probCross, probMut, iterations);

            int totalBestsGA = 0;
            int i = 0;
            long startTime = System.currentTimeMillis();
            while (i < TESTNUM) {
                ga.optimize();
                //Let's say a "good" result is at least 90% of the best possible
                if (clauses - ga.currentPopulation.findBest(problem).getFitness(problem) >= 0.90*(clauses - soln)) {
                    totalBestsGA += 1;
                }
                i++;
            }

            long totalTime = System.currentTimeMillis() - startTime;
            double successRateGA = totalBestsGA/TESTNUM;
            System.out.println("The success rate for Genetic Algorithm given these params is " + successRateGA);
            System.out.println("And it took " + totalTime + "ms to run " + TESTNUM + " tests");

        }

        //GeneticAlgorithm ga = new GeneticAlgorithm(problem, popSize , selectionType, crossoverType, probCross, 0.01, iterations);
//
        //int totalBestsGA = 0;
        //int i = 0;
        //long startTime = System.currentTimeMillis();
        //while (i < TESTNUM) {
        //    ga.optimize();
        //    //Let's say a "good" result is at least 90% of the best possible
        //    if (clauses - ga.currentPopulation.findBest(problem).getFitness(problem) >= 0.90*(clauses - soln)) {
        //        totalBestsGA += 1;
        //    }
        //    i++;
        //}
        //long totalTime = System.currentTimeMillis() - startTime;
        //double successRateGA = totalBestsGA/TESTNUM;
        //System.out.println("The success rate for Genetic Algorithm given these params is " + successRateGA);
        //System.out.println("And it took " + totalTime + "ms to run " + TESTNUM + " tests");
//
        ////Perform test for PBIL on first file (100 times)
        //PBIL pbil = new PBIL(problem, 0.1, 0.075, 0.02, 0.05, iterations, popSize);
//
        //int totalBestsPBIL = 0;
        //int j = 0;
        //startTime = System.currentTimeMillis();
        //while (j < TESTNUM) {
        //    pbil.optimize();
        //    //Let's say a "good" result is at least 90% of the best possible
        //    if (clauses - pbil.currentPop.findBest(problem).getFitness(problem) >= 0.90*(clauses - soln)) {
        //        totalBestsPBIL += 1;
        //    }
        //    j++;
        //}
        //totalTime = System.currentTimeMillis() - startTime;
        //double successRatePBIL = totalBestsPBIL/TESTNUM;
        //System.out.println("The success rate for PBIL given these params is " + successRatePBIL);
        //System.out.println("And it took " + totalTime + "ms to run " + TESTNUM + " tests");

    }
}