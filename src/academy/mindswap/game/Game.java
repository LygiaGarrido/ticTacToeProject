package academy.mindswap.game;

import academy.mindswap.server.Server;


import javax.sound.midi.Soundbank;
import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.*;

import static academy.mindswap.utils.Messages.*;


public class Game implements Runnable {

    private Server server;

    private PlayerHandler player1;
    private PlayerHandler player2;
    public PlayerHandler[] listOfPlayers;

    private boolean isGameStarted;
    private boolean isFirstGame;

    private int numberOfPlays;

    private GameLogic gameLogic;

    private static String[][] board;

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

    public Game(Server server, PlayerHandler player) {
        this.server = server;
        gameLogic = new GameLogic();
        board = gameLogic.getBoard();
        isFirstGame = true;
        player1 = player;
        numberOfPlays = 0;
    }


    public void startGame() {
        isGameStarted = true;

        if (isFirstGame) {
            isFirstGame = false;
            initiatePlayers();
        }

        broadCastToAllPlayers(WELCOME_TO_TICTACTOE);
        broadCastToAllPlayers(drawBoard());
        numberOfPlays = 0;

        while (isGameStarted) {

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
        playAgain();
    }

    public void playAgain() {//ver default henrique

        broadCastToAllPlayers(PLAY_AGAIN);

        if (player1.listenFromPlayer().equalsIgnoreCase("Yes") && player2.listenFromPlayer().equalsIgnoreCase("Yes")) {
            gameLogic.createBoard();
            startGame();
        }
        if (player1.listenFromPlayer().equalsIgnoreCase("No") || player2.listenFromPlayer().equalsIgnoreCase("No")) {
            broadCastToAllPlayers(NO_MORE_PLAYING);
            endGame();
        }

    }

    public void endGame() {
        broadCastToAllPlayers(THANK_YOU_FOR_PLAYING);

        for (int i = 0; i < listOfPlayers.length; i++) {
            listOfPlayers[i].closeSocket();
        }
    }


    private String drawBoard() {
        String bordDraw = "\n" + "    0   1   2 "
                + "\n" + "  ┌───┬───┬───┐"
                + "\n  | "
                + board[0][0] + " | "
                + board[0][1] + " | "
                + board[0][2] + " | "
                + "\n" + "  ├───┼───┼───┤"
                + "\n3 |"
                + board[1][0] + "  | "
                + board[1][1] + " | "
                + board[1][2] + " | 5"
                + "\n" + "  ├───┼───┼───┤"
                + "\n  |"
                + board[2][0] + "  | "
                + board[2][1] + " | "
                + board[2][2] + " | "
                + "\n" + "  └───┴───┴───┘"
                + "\n" + "    6   7   8   ";
        return bordDraw;
    }


    private int checkWin(int playerID) {
        PlayerHandler player1 = listOfPlayers[0];
        PlayerHandler player2 = listOfPlayers[1];
        if (playerID == 0) {
            player1.sendMessageToPlayer(String.format(WINNER, player1.getName()));
            player2.sendMessageToPlayer(String.format(LOSER, player2.getName()));
        } else if (playerID == 1) {
            player2.sendMessageToPlayer(String.format(WINNER, player2.getName()));
            player1.sendMessageToPlayer(String.format(LOSER, player1.getName()));
        } else if (playerID == 2) {
            broadCastToAllPlayers(TIE);
        }
        return playerID;
    }

    public void initiatePlayers() {
        Arrays.stream(listOfPlayers).forEach(player -> player.setPlayerMove());
        while (listOfPlayers[0].getPlayerMove().equalsIgnoreCase(listOfPlayers[1].getPlayerMove())) {
            player2.sendMessageToPlayer(CHOOSE_ANOTHER_ONE);
            player2.setPlayerMove();
        }
        player1.setId(0);
        player2.setId(1);
    }

    public void broadCast(String message, PlayerHandler playerHandler) {
        Arrays.stream(listOfPlayers)
                .filter(player -> !playerHandler.equals(player))
                .forEach(player -> player.sendMessageToPlayer(message));
    }

    public void broadCastToAllPlayers(String message) {
        Arrays.stream(listOfPlayers)
                .forEach(player -> player.sendMessageToPlayer(message));
    }

    @Override
    public void run() {
        startGame();
    }

}
