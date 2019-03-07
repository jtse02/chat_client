import org.jline.utils.InfoCmp;
import org.jline.terminal.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

public class ServerOutput extends Thread{
    private static final int MAX_MESSAGES = 1000;
    protected Socket server;
    protected String[] messages;
    public static volatile boolean welcomeAcknowledged = false;
    public ServerOutput(Socket server) {
        this.server = server;
        this.messages = new String[MAX_MESSAGES];
    }

    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(server.getInputStream()));
            System.out.println(in.readLine());
            String message = "";
            while(true){
                ChatClient.clientLock.acquire();
                if(recieve(in)){
                    ChatClient.serverLock.release();
                }
                else{
                    welcomeAcknowledged = true;
                    ChatClient.serverLock.release();
                    break;
                }
            }
            render();
            int index = 0;
            while ((message = in.readLine()) != null) {
                messages[index] = message;
                index = (index + 1) % MAX_MESSAGES;
                render();
            }
        } catch (IOException e) {

        } catch (InterruptedException e){

        }
    }

    public void render() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        try{
            String seperator = new String(new char[org.jline.terminal.TerminalBuilder.terminal().getWidth()]).replace("\0", "-");
            int numLines = org.jline.terminal.TerminalBuilder.terminal().getHeight() - 2;
            int offset;
            for(offset = 0; messages[offset] != null; offset++){
            }
            for (int i = (offset - numLines < 0 ? 0: offset - numLines); i < (offset - numLines < 0 ? numLines : offset); i++) {
                if(messages[i] != null){
                    System.out.println(messages[i]);
                } else {
                    System.out.println();
                }
            }
            System.out.println(seperator);
            System.out.print(ClientInput.username + ": ");
        } catch(IOException e){

        }

    }

    public boolean recieve(BufferedReader in) throws  IOException{
        String message;
        boolean isError = false;
        try{
            while((message = in.readLine()) != null){
                System.out.println(message);
                isError = message.startsWith("Enter");
                if(isError || message.startsWith(ClientInput.username))
                    break;
            }
            return isError;

        } catch (IOException e){
            throw new IOException(e);
        }
    }
}
