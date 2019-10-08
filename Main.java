import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.Reader;

public class Main {
    
    public static void main(String[] args) {
        String filename = args[0];

        try {
            File initialFile = new File(filename);
            initialFile.createNewFile();

            Reader reader = new FileReader(initialFile);
            
            BufferedReader buffReader = new BufferedReader(reader);

            System.out.println(buffReader.readLine());
        } catch (IOException e) {
            System.out.println("IO Exception");
        }
        
    }
}