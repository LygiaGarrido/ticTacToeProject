package academy.mindswap.game;

import academy.mindswap.server.Server;


import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.*;

import static academy.mindswap.utils.Messages.*;


public class Game implements Runnable {

    private Server server;
    private List<PlayerHandler> listOfPlayers;
    private ExecutorService threadPool;
    private static final int NUMBER_OF_PLAYER = 2;

    private boolean isGameStarted ;
    private GameLogic gameLogic;


    public Game(Server server) {
        this.server = server;
        threadPool = Executors.newFixedThreadPool(NUMBER_OF_PLAYER);
        listOfPlayers = new CopyOnWriteArrayList<>();

        startGame();
    }

    public void acceptPlayer(Socket playerSocket){
        if(listOfPlayers.size() < 2){
            threadPool.submit(new PlayerHandler(playerSocket));
        }
    }

    public void addPlayerToList(PlayerHandler playerHandler){
        playerHandler.setId(listOfPlayers.size());
        listOfPlayers.add(playerHandler);

    }

    public void broadCast(String message, PlayerHandler playerHandler){
        listOfPlayers.stream()
                .filter(player -> !playerHandler.equals(player))
                .forEach(player ->player.sendMessageToPlayer(message));
    }
    public void broadCastToAllPlayers(String message){
        listOfPlayers.stream().forEach(player ->player.sendMessageToPlayer(message));
    }

    public String gameStatus(int playerId){
        StringBuilder gameStatusMessage = new StringBuilder("board");
        String[][] x = gameLogic.getBoard();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                gameStatusMessage.append(",");
                gameStatusMessage.append(gameLogic.getBoard()[i][j]);
            }
        }
        gameStatusMessage.append(",");
        gameStatusMessage.append(gameLogic.checkWinner(playerId));
        return gameStatusMessage.toString();
    }

    public void broadCast(String message){
        listOfPlayers.forEach(playerHandler -> playerHandler.sendMessageToPlayer(message));
    }

    public void startGame() {
        isGameStarted = true;
        gameLogic = new GameLogic();

    }

    @Override
    public void run() {



    }

    public class PlayerHandler implements Runnable{
        private Socket playerSocket;
        private String name;
        private int id;

        private BufferedReader reader;
        private BufferedWriter writer;
        private String message;

        public void setId(int id){
            this.id = id;
        }

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
                String[] splitted = message.split(" ");
                switch (splitted[0]){
                    case "name":
                        name = splitted[1];
                        broadCast(String.format(NEW_PLAYER_HAS_ARRIVED, name),this);
                        sendMessageToPlayer(gameStatus(id));
                        sendMessageToPlayer(ASK_FOR_POSITION);
                        break;

                    case "move":
                        gameLogic.makeMove(splitted[1], id==0 ? "  O  " : "  X  ");
                        broadCastToAllPlayers(gameStatus(id));
                        broadCast(ASK_FOR_POSITION, this);
                        break;

                    case "quit":
                        playerSocket.close();
                        reader.close();
                        writer.close();
                        listOfPlayers.remove(id);
                        System.out.println(listOfPlayers.size());
                        break;

                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return message;
        }

        @Override
        public void run() {
            addPlayerToList(this);
            sendMessageToPlayer(ASK_FOR_NAME);
            while (true) {

                listenFromPlayer();
            }





        }
    }
}
