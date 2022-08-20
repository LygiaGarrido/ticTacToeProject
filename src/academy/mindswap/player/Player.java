package academy.mindswap.player;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static academy.mindswap.utils.Messages.*;

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
            System.out.println(DEAD_SERVER);
            //handleServer();
        }
    }

    private void sendMessages() throws IOException {
        String message = readFromConsole();

        writer.write(message);
        if(message.equals("quit")){
            close();
        }
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

        private void drawBoard(String[] position){

            System.out.println();
            System.out.println(position[1] + "|" + position[2] + "|" + position[3]);
            drawSeparator();
            System.out.println(position[4] + "|" + position[5] + "|" + position[6]);
            drawSeparator();
            System.out.println(position[7] + "|" + position[8] + "|" + position[9]);
            System.out.println();
        }
        public void drawSeparator() {
            System.out.println("----------------");
        }

        private void readMessage() throws IOException {
            //     System.out.println("reading");
            String readMessageFromServer = reader.readLine();
            String[] splitted = readMessageFromServer.split(",");

            if(splitted[0].equals("board")){
                drawBoard(splitted);
                checkWinner(Integer.parseInt(splitted[10]));
            } else {
                System.out.println(readMessageFromServer);
            }
            readMessage();

        }

        private void checkWinner(int value){
            if(value == 0){
                System.out.println(LOSER);
            } else if (value == 1) {
                System.out.println(WINNER);
            }

        }
    }


}
