import java.io.*;
import java.util.*;

public class Client {

    public static void main(String[] args) throws IOException {

        int userInput;
        Scanner scanner;
        ClientProtocol clientProtocol = new ClientProtocol();

        while(true){
            System.out.println("\nEnter 1 to fetch the file from server.");
            System.out.println("Enter 2 to fetch top ten list of the most common words in the file and the number of occurrences for these words.");
            System.out.println("Enter 3 to stop the client.\n");

            scanner = new Scanner(System.in);
            try {
                userInput = scanner.nextInt();
                switch (userInput) {
                    case 1: clientProtocol.getFileFromServer();
                        break;
                    case 2: clientProtocol.processFile();
                        break;
                    case 3: clientProtocol.stopClient();
                        break;
                    default: throw new InputMismatchException();
                }
            } catch (InputMismatchException e){
                System.out.println("\nValid inputs are 1, 2 and 3!");
            }
        }
    }
}
