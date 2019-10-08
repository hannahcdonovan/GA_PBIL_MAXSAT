import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.Reader;

public class Main {
    
    public static void readFile(String filename) {
        String line = null;
        Clause clause;

        try {
            File initialFile = new File(filename);
            initialFile.createNewFile();

            Reader reader = new FileReader(initialFile);
            
            BufferedReader buffReader = new BufferedReader(reader);

            while ((line = buffReader.readLine()) != null) {
                // System.out.println(line);
                clause = new Clause(line);
                System.out.println(clause.toString());
            }
            
        } catch (IOException e) {
            System.out.println("IO Exception");
        }
    }

    public static void main(String[] args) {
        String filename = args[0];
        readFile(filename);
    }
}