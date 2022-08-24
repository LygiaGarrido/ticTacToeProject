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
/**
 *
 * The Server Class is a fundamental part of the Tic-Tac-Toe Project
 *
 * The software was developed as a group project for the MindSwap program.
 *
 * Copyright 2022 Lygia Garrido, Tiago Costa and Rui Vieira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Class responsible for creating the server which will start a game and listen to players
 * When a player connects to it, it will either start a single player game
 * or wait for a new player to connect to start a new game
 *
 */
public class Server {
    private ServerSocket serverSocket;
    private Game game;

    private List<PlayerHandler> playerList;
    private ExecutorService threadPool;
    private static final int PORT = 8081;

    /**
     * Server's main method
     * It takes a port as an argument
     *
     */
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

    /**
     * The start method initializes the ServerSocket with a provided port
     * It creates an array list of PlayerHandlers and also a thread pool,
     * in order to manage the players in different games
     * It calls the acceptPlayer().
     *
     * @param port
     * @throws IOException
     */
    public void start(int port) throws IOException {
        System.out.println(SERVER_IS_RUNNING);
        System.out.println(WAITING_FOR_PLAYERS_TO_CONNECT);
        serverSocket = new ServerSocket(port);
        playerList = new ArrayList<PlayerHandler>();
        threadPool = Executors.newCachedThreadPool();
        acceptPlayer();

    }

    /**
     * The acceptPlayer method starts the connection with the players
     * It also allows connected players to set their names, choose between single or
     * multiplayer and begins the game according to player's choice.
     *
     *
     * @throws IOException
     */
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

    /**
     * The method findPlayer finds a new player to interact
     * with a previously connected player and starts a new game
     */

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



