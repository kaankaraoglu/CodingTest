import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Server {
    public final static int PORT_NUMBER = 9090;
    public final static String FILE_PATH = "../data/server/islands_in_the_stream.txt";
    private static volatile ServerSocket serverSocket;

    //  Reads from the file from filesystem and writes it to OutputStream..
    private static class ServerListenerThread extends Thread {
        public void run() {
            File file;
            OutputStream os;

            while (true) {
                try (Socket clientSocket = serverSocket.accept()) {
                    // Reads the file.
                    System.out.println("Reading the file...");
                    file = new File(FILE_PATH);
                    byte[] bytesArray = new byte[(int) file.length()];
                    FileInputStream fis = new FileInputStream(file);
                    BufferedInputStream bis = new BufferedInputStream(fis);
                    bis.read(bytesArray, 0, bytesArray.length);
                    System.out.println("Read the file successfully.\n");

                    // Writes the file to OutputStream.
                    System.out.println("Sending the file...");
                    os = clientSocket.getOutputStream();
                    os.write(bytesArray, 0, bytesArray.length);
                    os.flush();
                    System.out.println("Sent the file successfully.\n");

                    showControlMenu();
                } catch (IOException e) {
                    return;
                }
            }
        }
    }

    // Starts the server.
    private static void startServer(){
        if( getServerStatus() ){
            System.out.println("Server already running");
            return;
        }

        try {
            serverSocket = new ServerSocket(PORT_NUMBER);
            ServerListenerThread serverListener = new ServerListenerThread();
            serverListener.start();
            System.out.println("Started server.");
        } catch (IOException e) {
            System.out.println("Can not start the server. Port might already be in use.");
            System.exit(1);
        }
    }

    // Stops the server.
    private static void stopServer(boolean isTerminate ){
        if( !isTerminate && !getServerStatus() ){
            System.out.println("Server already stopped.");
            return;
        }

        try {
            serverSocket.close();
            System.out.println("Stopped server.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Returns server status.
    private static boolean getServerStatus(){
        return serverSocket != null && !serverSocket.isClosed();
    }

    // Prints out server control commands.
    private static void showControlMenu(){

        System.out.println("\nServer Status : " + (getServerStatus() ? "ON": "OFF"));
        System.out.println("Enter 1 to start the server");
        System.out.println("Enter 2 to stop the server.");
        System.out.println("Enter 3 to terminate the server.\n");
    }

    // Starts the server and creates threads.
    public static void main(String[] args){
        startServer();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            showControlMenu();
            try {
                switch (scanner.nextInt()) {
                    case 1: startServer(); break;
                    case 2: stopServer(false); break;
                    case 3: stopServer(true); return;
                    default: throw new InputMismatchException();
                }
            } catch (InputMismatchException e){
                System.out.println("Please enter either 1, 2 or 3!");
            }
        }
    }
}
