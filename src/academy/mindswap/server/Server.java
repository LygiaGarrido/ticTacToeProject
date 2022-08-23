package academy.mindswap.server;

import academy.mindswap.game.Game;
import academy.mindswap.game.PlayerHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static academy.mindswap.utils.Messages.*;

public class Server {
    private ServerSocket serverSocket;
    private Game game;

    private List<PlayerHandler> playerList;
    private ExecutorService threadPool;
    private static final int PORT = 8081;

    public static void main(String[] args) {
        Server server = new Server();
        try {
            server.start(PORT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

  /*  public void createGame(){
        game = new Game(this);
    }*/

    public void start(int port) throws IOException {
        System.out.println(SERVER_IS_RUNNING);
        System.out.println(WAITING_FOR_PLAYERS_TO_CONNECT);
        serverSocket = new ServerSocket(port);
        playerList = new ArrayList<PlayerHandler>();
        threadPool = Executors.newCachedThreadPool();
        acceptPlayer();

    }

    public void acceptPlayer() throws IOException {



        try {
            Socket clientSocket = serverSocket.accept();

            PlayerHandler playerHandler = new PlayerHandler(clientSocket);

            playerHandler.setName();

            playerHandler.sendMessageToPlayer(String.format(WELCOME, playerHandler.getName()));



            System.out.println(NEW_USER_HAS_CONNECTED);
            playerHandler.sendMessageToPlayer(MULTIPLAYER_OR_SINGLEPLAYER);
            String gameMode = playerHandler.listenFromPlayer();

            while (!gameMode.matches("[1|2]+")) {
                playerHandler.sendMessageToPlayer(INVALID_INPUT);
                playerHandler.sendMessageToPlayer(MULTIPLAYER_OR_SINGLEPLAYER);
                gameMode = playerHandler.listenFromPlayer();
            }

            if(gameMode.equals("2")){
                playerList.add(playerHandler);
                playerHandler.sendMessageToPlayer(WAITING_FOR_OPPONENT);
                findPlayer();
                acceptPlayer();
            } else {
                System.out.println(STARTING_A_NEW_GAME);
                playerHandler.startGame();
                Game game = new Game(this, playerHandler);
                threadPool.execute(game);
                playerHandler.startGame();
                acceptPlayer();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void findPlayer() {
        PlayerHandler[] playerArray = new PlayerHandler[2];
        int playerCounter = 0;


        for (PlayerHandler player : playerList) {
            if (!player.isOffline() && player != playerArray[0] && !player.isPlaying()) {
                playerArray[playerCounter] = player;
                playerCounter++;
            }
        }

        if(playerArray[1] != null) {
            Arrays.stream(playerArray).forEach(player -> player.sendMessageToPlayer(STARTING_A_NEW_GAME));
            Game game = new Game(this, playerArray);
            threadPool.execute(game);
            Arrays.stream(playerArray).forEach(PlayerHandler::startGame);
        }

    }
}


/* TODO

inform player that other player has left

 */
