public class Test {

    private static int TESTNUM = 100;
    public static void main(String[] args) {

        ClauseList problem = Main.readFile("t3pm3-5555.spn.cnf");

        //Perform test for GA on the first file (100 times)
        GeneticAlgorithm ga = new GeneticAlgorithm(problem, 2000, "ts", "1c", 0.7, 0.01, 2000);

        int totalBestsGA = 0;
        int i = 0;
        while (i < TESTNUM) {
            ga.optimize();
            if (ga.currentPopulation.findBest(problem).getFitness(problem) == 17) {
                totalBestsGA += 1;
            }
            i++;
        }
        double successRateGA = totalBestsGA/TESTNUM;
        System.out.println("The success rate for Genetic Algorithm given these params is " + successRateGA);

        //Perform test for PBIL on first file (100 times)
        PBIL pbil = new PBIL(problem, 0.1, 0.075, 0.02, 0.05, 2000, 2000);

        int totalBestsPBIL = 0;
        int j = 0;
        while (j < TESTNUM) {
            pbil.optimize();
            if (pbil.currentPop.findBest(problem).getFitness(problem) == 17) {
                totalBestsPBIL += 1;
            }
            i++;
        }
        double successRatePBIL = totalBestsPBIL/TESTNUM;
        System.out.println("The success rate for Genetic Algorithm given these params is " + successRatePBIL);

    }
}