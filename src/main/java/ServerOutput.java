import org.jline.utils.InfoCmp;
import org.jline.terminal.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

public class ServerOutput extends Thread{
    protected Socket server;
    protected ArrayList<String> messages;
    public static boolean welcomeAcknowledged = false;
    public ServerOutput(Socket server) {
        this.server = server;
        this.messages = new ArrayList<>(1000);
    }

    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(server.getInputStream()));
            System.out.println(in.readLine());
            ClientInput.welcomeRecieved = true;
            String message;
            while ((message = in.readLine()).startsWith("Nickname")) {
                System.out.println(message);
            }
            welcomeAcknowledged = true;
//            render();
            int index = 0;
            while ((message = in.readLine()) != null) {
                messages.add(index, message);
                index = (index + 1) % 1000;
                render();
            }
        } catch (IOException e) {

        }
    }

    public void render() {
        System.out.print("\033[H\033[2J");
        System.out.flush();

        for (String message : messages) {
            System.out.println(message);
        }
        try{
            String seperator = new String(new char[org.jline.terminal.TerminalBuilder.terminal().getWidth()]).replace("\0", "-");
            System.out.println(seperator);
            System.out.print(ClientInput.username + ": ");
        } catch(IOException e){

        }

    }
}