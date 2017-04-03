import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;


//  Implements client side functionality.
public class ClientProtocol {

    public final static int PORT_NUMBER = 9090;
    public final static int MAX_FILE_SIZE = 16 * 1024;
    public final static String SERVER_ADDRESS = "localhost";
    public final static String FILE_PATH = "../data/client/islands_in_the_stream.txt";

    // Establishes a connection and fetches the file.
    protected void getFileFromServer(){
        byte[] bytesArray = new byte[MAX_FILE_SIZE];

        try ( Socket socket = new Socket(SERVER_ADDRESS, PORT_NUMBER);
              InputStream is = socket.getInputStream();
              FileOutputStream fos = new FileOutputStream(FILE_PATH);
              BufferedOutputStream bos = new BufferedOutputStream(fos)
        ) {
            int bytesRead = is.read(bytesArray, 0, bytesArray.length);

            if(bytesRead == -1)
                System.out.println("\nThe server could not find the file specified");
            else {
                bos.write(bytesArray, 0, bytesRead);
                System.out.println("\nFile fetched from the server. File path: " + FILE_PATH);
            }
        } catch (UnknownHostException e) {
            System.out.println("Don't know about host " + SERVER_ADDRESS);
            stopClient();
        } catch (FileNotFoundException e) {
            System.out.println("Path is invalid. Make sure folder data/client/ exists.");
        } catch (IOException e) {
            System.out.println("Server is not running. Please start the server first.");
            System.out.println("Couldn't get I/O for the connection to " + SERVER_ADDRESS + ".");
        }
    }

    /*
     Creates a top ten list over the most common words in the file,
     and the number of occurrences for these words.
    */
    protected void processFile(){

        File file = new File(FILE_PATH);
        Scanner input = null;
        LinkedHashMap<String, Integer> wordCounts = new LinkedHashMap<>();

        try {
            input = new Scanner(file);
        }
        catch (FileNotFoundException e){
            System.out.println("Couldn't find the file. First, fetch it from the server.");
            return;
        }

        // Makes every letter lowercase, removes illegal symbols and puts them in a Map.
        while(input.hasNext()){
            String word = input.next().toLowerCase().replaceAll("[-+.^:,]","");

            if(wordCounts.containsKey(word))
                wordCounts.put(word, wordCounts.get(word) + 1);
            else
                wordCounts.put(word, 1);
        }

        if( wordCounts.size() != 0 ) {
            Map<String, Integer> sortedMap = sortByValue(wordCounts);
            printMap(sortedMap);
        } else
            System.out.println("File is empty, fetch it from the server");
    }

    // Sorts the Map by value.
    protected Map<String, Integer> sortByValue(Map<String, Integer> unsortedMap) {

        // Convert Map to List of Maps.
        List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer>>(unsortedMap.entrySet());

        // Sort!
        Collections.sort(list, (o1, o2) -> (o2.getValue()).compareTo(o1.getValue()));

        // Loops the sorted list and puts entries into a new LinkedHashMap.
        Map<String, Integer> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

    // Prints the sorted Map.
    protected <K, V> void printMap(Map<K, V> map) {
        System.out.println("\n\tOccurence \tWord");
        map.entrySet().stream().limit(10).forEach(e -> System.out.println("\t" + e.getValue() + " \t\t" + e.getKey()));
    }

    // Stops the client.
    protected void stopClient(){
        System.out.println("Shutting down the client...");
        System.exit(1);
    }
}
