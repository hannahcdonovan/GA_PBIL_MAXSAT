# GA_PBIL_MAXSAT
A nature-inspired approach to solving the MAXSAT problem using both Genetic Algorithm and Population-Based
Incremental Learning (PBIL). Used to compare both the upsides and downfalls of both algorithms in the context
of MAXSAT.

# How to use
1. Compile the Main class by running the following command `javac Main.java`
2. Running for Genetic Algorithm:
    a. Run the following command `java Main <filename> <populationNum> <selectionType> <crossoverType> <crossoverProb> <mutationProb> <numIterations> g`
    b. An example of this would be `java Main t3pm3-5555.spn.cnf 100 ts 1c 0.7 0.01 1000 g`
    c. The following selection types are avaiable: ts (Tournament Selection), rs (Rank Selection), sg (Selection      by Groups).
    d. The following crossover types are avaiable: 1c (One-Point Crossover), uc (Uniform Crossover)
3. Running for PBIL:
    a. Run the following command `java Main <filename> <populationNum> <positiveLearningRate> <negativeLearningRate> <mutationProb> <mutationAmount> <numIterationrs> p`
    b. An example of this would be `java Main t3pm3-5555.spn.cnf 100 0.1 0.075 0.02 0.05 1000 p`

# Understanding the results
For either algorithm, the algorithm will print the "best individual" on each iteration. Note that the "best" means
the number of clauses in the MAXSAT problem that are NOT satisfied. We are trying to minimize the best in this case.