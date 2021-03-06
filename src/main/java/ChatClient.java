import javax.swing.*;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Semaphore;

public class ChatClient {
    public static Semaphore clientLock;
    public static Semaphore serverLock;
    public static void main(String[] args) {
        clientLock = new Semaphore(0);
        serverLock = new Semaphore(0);
//        JFrame frame = new JFrame("Chat Client");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        if (args.length != 2) {
            System.err.println("Usage: cmd ServerAddress ServerPort");
            System.exit(1);
        }
        String address = args[0];
        int port = 0;
        try {
            port = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.err.println(e);
            System.exit(1);
        }
        try {
            Socket chatServer = new Socket(address, port);
            new ClientInput(chatServer).start();
            new ServerOutput(chatServer).start();
        } catch (IOException e) {
        }
//        frame.setVisible(true);
    }
}
