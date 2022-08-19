package academy.mindswap.player;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Player {
    public boolean isPlayerTurn;
    private Socket socket;
    private BufferedWriter writer;
    private BufferedReader reader;
    private Scanner consoleReader;

    public static void main(String[] args) throws IOException {
        Player player = new Player();
        player.startConsoleReader();
        player.handleServer();

    }

    private void startConsoleReader() {
        consoleReader = new Scanner(System.in);
    }

    private void handleServer() throws IOException {
        connectToServer("localhost", 8080);

        startListenToServer();

        communicateWithServer();

    }


    private void connectToServer(String host, int port) throws IOException {
        socket = new Socket(host, port);
        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));


    }


    private void startListenToServer() throws IOException {
        new Thread(new ServerListener(reader)).start();
    }

    private void communicateWithServer() throws IOException {
        try {
            if (!socket.isClosed()) {
                sendMessages();
                communicateWithServer();
            }
        } catch (IOException e) {
            System.out.println("Hum... seems that the server is dead");
            handleServer();
        }
    }

    private void sendMessages() throws IOException {
        String message = readFromConsole();

        writer.write(message);
        writer.newLine();
        writer.flush();

    }

    private String readFromConsole() {
        String message = null;

        message = consoleReader.nextLine();

        return message;
    }

    private void close() {
        try {
            System.out.println("Closing socket");
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private class ServerListener implements Runnable {
        BufferedReader reader;

        public ServerListener(BufferedReader reader) {
            this.reader = reader;
        }

        @Override
        public void run() {
            try {
                readMessage();
            } catch (IOException e) {
            }
        }

        private void readMessage() throws IOException {
            //     System.out.println("reading");
            String readMessageFromServer = reader.readLine();
            System.out.println(readMessageFromServer);
            readMessage();

        }
    }


}
