package academy.mindswap.game;

import academy.mindswap.server.Server;


import javax.sound.midi.Soundbank;
import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.*;

import static academy.mindswap.utils.Messages.*;

/**
 *
 * The Game Class is a fundamental part of the Tic-Tac-Toe Project
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
 *This Class implements the interface Runnable
 * It holds the game logic for both single and multiplayer modes,
 * and has the necessary methods to run each of them accordingly.
 */

public class Game implements Runnable {

    private Server server;

    private PlayerHandler player1;
    private PlayerHandler player2;
    public PlayerHandler[] listOfPlayers;

    private boolean isGameStarted;
    private boolean isFirstGame;

    private int numberOfPlays;

    private GameLogic gameLogic;
    private SinglePlayer singlePlayer;

    private boolean isSinglePlayer;

    private static String[][] board;

    /**
     * This constructor method is used in case of multiplayer mode.
     * It takes a list of players and the server to establish the connection.
     * It instantiates the necessary parameters to run the multiplayer game logic.
     *
     * @param server
     * @param listOfPlayers
     */
    public Game(Server server, PlayerHandler[] listOfPlayers) {
        this.server = server;
        this.listOfPlayers = listOfPlayers;
        gameLogic = new GameLogic();
        board = gameLogic.getBoard();
        isFirstGame = true;
        player1 = listOfPlayers[0];
        player2 = listOfPlayers[1];
        numberOfPlays = 0;

    }

    /**
     * This constructor method is used in case of single player mode.
     * It takes one player and the server to establish the connection.
     * It instantiates the necessary parameters to run the single player game logic.
     *
     *
     * @param server
     * @param player
     */
    public Game(Server server, PlayerHandler player) {
        this.server = server;
        gameLogic = new GameLogic();
        singlePlayer = new SinglePlayer();
        board = gameLogic.getBoard();
        isFirstGame = true;
        player1 = player;
        numberOfPlays = 0;
        isSinglePlayer = true;
    }

    /**
     * This method checks if it is the first game and also which mode is being played.
     * It starts the game according to the mode.
     * When the games are over, it calls the playAgain() method.
     */
    private void startGame() {
        isGameStarted = true;

        if (isFirstGame) {
            isFirstGame = false;
            initiatePlayers();
        }

       if(isSinglePlayer){
           while(isGameStarted){
               singlePlayerStart();
           }
       }

       if(!isSinglePlayer){
           while(isGameStarted){
               multiPlayerStart();
           }
       }

         playAgain();
    }

    /**
     * This is the logic to start the multiplayer mode.
     * it asks for players' moves and checks for winner after each iteration
     */
    private void multiPlayerStart() {
        for (int i = 0; i < listOfPlayers.length; i++) {
            PlayerHandler player = listOfPlayers[i];
            gameLogic.makeMove(player);
            broadCastToAllPlayers(drawBoard());
            if ((checkWin(gameLogic.checkWin(player))) != 3) {
                isGameStarted = false;
                break;
            }
        }
    }
    /**
     * This is the logic to start the single player mode.
     * It asks for player's and bot moves, and checks for winners after each round.
     */
    private void singlePlayerStart() {
        gameLogic.makeMove(player1);
        player1.sendMessageToPlayer(drawBoard());
        if(checkSinglePlayerWin(singlePlayer.singlePlayerCheckWin(board, player1)) != 3){
            isGameStarted = false;
            return;
        }
        gameLogic.fillBoard(singlePlayer.botMovement(board, player1), singlePlayer);
        player1.sendMessageToPlayer(drawBoard());
        if(checkSinglePlayerWin(singlePlayer.singlePlayerCheckWin(board, player1)) != 3){
            isGameStarted = false;
            return;
        }

    }

    /**
     * This method sends a message by the end of the matches to check whether the player
     * wants to play another round or to disconnect.
     * If the player wishes to play, it calls the startGame() method.
     * If there is a negative response from the player, it ends the connection with the player.
     * In case only one of the players wishes to continue playing,
     * the server looks for a new opponent before starting a new game.
     */
    private void playAgain() {

        if (isSinglePlayer) {

            player1.sendMessageToPlayer(PLAY_AGAIN);
            String player1Response = player1.listenFromPlayer().toUpperCase();

            while (!player1Response.matches("[(Y|N)]")) {
                player1.sendMessageToPlayer(INVALID_INPUT);
                player1Response = player1.listenFromPlayer();
            }
            switch (player1Response) {
                case "Y":

                    singlePlayer.resetNumberOfPlays();
                    gameLogic.createBoard();
                    singlePlayer.resetPlayBOT();
                    player1.sendMessageToPlayer(drawBoard());
                    startGame();

                case "N":
                    endGame(player1);

            }
            return;
        }

        broadCastToAllPlayers(PLAY_AGAIN);

        String player1Response = player1.listenFromPlayer().toUpperCase();

        while (!player1Response.matches("[(Y|N)]")) {
            player1.sendMessageToPlayer(INVALID_INPUT);
            player1Response = player1.listenFromPlayer();
        }

        String player2Response = player2.listenFromPlayer().toUpperCase();

        while (!player2Response.matches("[(Y|N)]")) {
            player2.sendMessageToPlayer(INVALID_INPUT);
            player2Response = player2.listenFromPlayer();
        }

        if (player1Response.equalsIgnoreCase("Y")
                && player1Response.equalsIgnoreCase(player2Response)) {
            gameLogic.createBoard();
            broadCastToAllPlayers(drawBoard());
            startGame();
            return;
        }
        if (player1Response.equalsIgnoreCase("N") &&
                player1Response.equalsIgnoreCase(player2Response)) {
            endGame(player1);
            endGame(player2);
            return;
        }

        switch (player1Response) {
            case "Y":
                    endGame(player2);
                    player1.endGame();
                    player1.sendMessageToPlayer(WAITING_FOR_OPPONENT);
                    server.findPlayer();

            case "N" :
                    endGame(player2);
                    player1.endGame();
                    player1.sendMessageToPlayer(WAITING_FOR_OPPONENT);
                    server.findPlayer();
                }

        }

    /**
     * The endGame() method closes the connection with a given player.
     *
     * @param player
     */
    private void endGame(PlayerHandler player) {
        player.sendMessageToPlayer(THANK_YOU_FOR_PLAYING);
        player.closeSocket();

    }

    /**
     * This method draws the board for the player
     * @return String
     */
    private String drawBoard() {

        String bordDraw =
                        drawScoreBoard()
                        + "    0   1   2 "
                        + "\n" + "  ┌───┬───┬───┐"
                        + "\n  | "
                        + board[0][0] + " | "
                        + board[0][1] + " | "
                        + board[0][2] + " | "
                        + "\n" + "  ├───┼───┼───┤"
                        + "\n3 | "
                        + board[1][0] + " | "
                        + board[1][1] + " | "
                        + board[1][2] + " | 5"
                        + "\n" + "  ├───┼───┼───┤"
                        + "\n  | "
                        + board[2][0] + " | "
                        + board[2][1] + " | "
                        + board[2][2] + " | "
                        + "\n" + "  └───┴───┴───┘"
                        + "\n" + "    6   7   8   ";
        return bordDraw;
    }

    /**
     * This is the method responsible for drawing the score board.
     * @return String
     */
    private String drawScoreBoard(){

        String player2Name = null;
        int player2Score = 0;

        if(!isSinglePlayer) {
            player2Name = player2.getName();
            player2Score = player2.getScore();
        }

        if(isSinglePlayer){
            player2Name = "Bot";
            player2Score = singlePlayer.getScore();
        }

        String scoreBoard =
                        "   ┌──────────┐\n" +
                        "    SCOREBOARD\n" +
                        "   └──────────┘\n" +
                        "───────────────\n" +
                        player1.getName() + " : " + player1.getScore() + "\n"
                        + "───────────────\n"
                        + player2Name+ " : " + player2Score + "\n"
                        + "───────────────\n";
      return scoreBoard;
    }

    /**
     * This is the logic to deal with the messages and score in case of winners/losers/tie.
     * @param playerID
     * @return int playerID
     */
    private int checkWin(int playerID) {
        PlayerHandler player1 = listOfPlayers[0];
        PlayerHandler player2 = listOfPlayers[1];
        if (playerID == 0) {
            player1.sendMessageToPlayer(String.format(WINNER, player1.getName()));
            player1.setScore();
            player2.sendMessageToPlayer(String.format(LOSER, player2.getName()));
        } else if (playerID == 1) {
            player2.sendMessageToPlayer(String.format(WINNER, player2.getName()));
            player2.setScore();
            player1.sendMessageToPlayer(String.format(LOSER, player1.getName()));
        } else if (playerID == 2) {
            player1.sendMessageToPlayer(TIE);
        }
        return playerID;
    }

    /**
     * This is the logic to deal with the messages and score in case of winners/losers/tie
     * when in single player mode.
     */

    private int checkSinglePlayerWin(int playerID){
        if(playerID == 0){
            player1.sendMessageToPlayer(String.format(WINNER, player1.getName()));
            player1.setScore();
        } else if (playerID == 1) {
            player1.sendMessageToPlayer(BOT_WINS);
            singlePlayer.setScore();
            player1.sendMessageToPlayer(String.format(LOSER, player1.getName()));
        } else if (playerID == 2) {
            player1.sendMessageToPlayer(TIE);
        }
        return playerID;
    }

    /**
     * The method asks for players to set their move symbol,
     * sends a welcome message and draws the board for the players
     * In case of multiplayer, it checks if the move symbol has already been chosen.
     */
    private void initiatePlayers() {
        if(isSinglePlayer){
            player1.setPlayerMove();
            singlePlayer.setPlayMove(player1);
            player1.sendMessageToPlayer(WELCOME_TO_TICTACTOE);
            player1.sendMessageToPlayer(drawBoard());
        } else{
            Arrays.stream(listOfPlayers).forEach(player -> player.setPlayerMove());
            while (listOfPlayers[0].getPlayerMove().equalsIgnoreCase(listOfPlayers[1].getPlayerMove())) {
                player2.sendMessageToPlayer(CHOOSE_ANOTHER_ONE);
                player2.setPlayerMove();
            }
            player1.setId(0);
            player2.setId(1);

            broadCastToAllPlayers(WELCOME_TO_TICTACTOE);
            broadCastToAllPlayers(drawBoard());
        }

    }

    /**
     * This method broadcasts the message from the server/game to all players at once.
     * It can only be used in multiplayer mode.
     * @param message
     */
    private void broadCastToAllPlayers(String message) {
        Arrays.stream(listOfPlayers)
                .forEach(player -> player.sendMessageToPlayer(message));
    }

    /**
     * override of the run() method, calls the startGame() method.
     */
    @Override
    public void run() {
        startGame();
    }

}
