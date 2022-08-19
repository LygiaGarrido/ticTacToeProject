package academy.mindswap.game;

import academy.mindswap.server.Server;


import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.*;

import static academy.mindswap.utils.Messages.ASK_FOR_NAME;
import static academy.mindswap.utils.Messages.NEW_PLAYER_HAS_ARRIVED;


public class Game implements Runnable {

    private Server server;
    private List<PlayerHandler> listOfPlayers;
    private ExecutorService threadPool;
    private static final int NUMBER_OF_PLAYER = 2;

    private boolean isGameStarted ;


    public Game(Server server) {
        this.server = server;
        threadPool = Executors.newFixedThreadPool(NUMBER_OF_PLAYER);
        listOfPlayers = new CopyOnWriteArrayList<>();

    }

    public void acceptPlayer(Socket playerSocket){
        threadPool.submit(new PlayerHandler(playerSocket));
    }

    public void addPlayerToList(PlayerHandler playerHandler){
        listOfPlayers.add(playerHandler);
    }

    public void broadCast(String message,PlayerHandler playerHandler){
        listOfPlayers.stream()
                .filter(player -> !playerHandler.equals(player))
                .forEach(player ->player.sendMessageToPlayer(message));
    }

    public void broadCast(String message){
        listOfPlayers.forEach(playerHandler -> playerHandler.sendMessageToPlayer(message));
    }

    public void startGame() {
        isGameStarted = true;

    }

    @Override
    public void run() {



    }

    public class PlayerHandler implements Runnable{
        private Socket playerSocket;
        private String name;
        private BufferedReader reader;
        private BufferedWriter writer;
        private String message;

        public PlayerHandler(Socket playerSocket) {
            this.playerSocket = playerSocket;
            try {
                reader = new BufferedReader(new InputStreamReader(playerSocket.getInputStream()));
                writer = new BufferedWriter(new OutputStreamWriter(playerSocket.getOutputStream()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

        public void sendMessageToPlayer(String message){
            try {
                writer.write(message);
                writer.newLine();
                writer.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public String listenFromPlayer(){
            String message;
            try {
                message = reader.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return message;
        }

        @Override
        public void run() {
            addPlayerToList(this);
            sendMessageToPlayer(ASK_FOR_NAME);
            name = listenFromPlayer();

            broadCast(String.format(NEW_PLAYER_HAS_ARRIVED, name),this);



        }
    }
}
