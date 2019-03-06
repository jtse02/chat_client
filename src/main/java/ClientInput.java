import java.io.Console;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientInput extends Thread {
    protected Socket server;
    public static boolean welcomeRecieved = false;
    protected Console sc;
    protected String message;
    public static String username = "";

    public ClientInput(Socket server) {
        this.server = server;
    }

    public void run() {
        sc = System.console();
        try {
            PrintWriter out = new PrintWriter(server.getOutputStream());
            while(!ServerOutput.welcomeAcknowledged){
                username = sc.readLine();
                out.print(username + "\r\n");
                out.flush();
                ChatClient.clientLock.release();
                ChatClient.serverLock.acquire();
            }
            while (true) {
                message = sc.readLine();
                out.print(message + "\r\n");
                out.flush();
            }
        } catch (IOException e) {

        } catch (InterruptedException e){
        }
    }
}
