package academy.mindswap.player;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Player {
    private Socket socket;
    public boolean isPlayerTurn;
    private BufferedWriter writer;
    private BufferedReader reader;
    private ExecutorService threadPool;

    public void connectToServer(String host, int port) throws IOException {
        socket = new Socket(host, port);
        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        threadPool = Executors.newFixedThreadPool(2);


    }
    public void readFromServer(BufferedReader reader) throws IOException {

        String line;
        while((line = reader.readLine()) != null){
            System.out.println(line);
        }
    }

}
