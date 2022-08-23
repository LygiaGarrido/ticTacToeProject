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
    private SinglePlayer singlePlayer;

    private boolean isSinglePlayer;

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
        singlePlayer = new SinglePlayer();
        board = gameLogic.getBoard();
        isFirstGame = true;
        player1 = player;
        numberOfPlays = 0;
        isSinglePlayer = true;
    }


    public void startGame() {
        isGameStarted = true;

        if (isFirstGame) {
            isFirstGame = false;
            initiatePlayers();
        }

       if(isSinglePlayer){
           player1.sendMessageToPlayer(drawBoard());
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

    private void singlePlayerStart() {
        gameLogic.makeMove(player1);
        player1.sendMessageToPlayer(drawBoard());
        if(checkSinglePlayerWin(singlePlayer.singlePlayerCheckWin(board, player1)) != 3){
            isGameStarted = false;
            return;
        }
        gameLogic.fillBoard(singlePlayer.botMovement(board));
        player1.sendMessageToPlayer(drawBoard());
        if(checkSinglePlayerWin(singlePlayer.singlePlayerCheckWin(board, player1)) != 3){
            isGameStarted = false;
            return;
        }

    }

    public void playAgain() {//ver default henrique
        if(isSinglePlayer){
        player1.sendMessageToPlayer(PLAY_AGAIN);
        if (player1.listenFromPlayer().equalsIgnoreCase("Yes")){
            singlePlayer.resetNumberOfPlays();
        gameLogic.createBoard();
        startGame();
        }
         if (player1.listenFromPlayer().equalsIgnoreCase("No")){

        endGame();
    }
         return;
}
        broadCastToAllPlayers(PLAY_AGAIN);

        if (player1.listenFromPlayer().equalsIgnoreCase("Yes")
                && player2.listenFromPlayer().equalsIgnoreCase("Yes")) {
            gameLogic.createBoard();
            startGame();
        }
        if (player1.listenFromPlayer().equalsIgnoreCase("No")
                || player2.listenFromPlayer().equalsIgnoreCase("No")) {
            broadCastToAllPlayers(NO_MORE_PLAYING);
            endGame();
        }

    }

    public void endGame() {
       if(isSinglePlayer){
           player1.sendMessageToPlayer(THANK_YOU_FOR_PLAYING);
           player1.closeSocket();
           return;
       }
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
            player1.sendMessageToPlayer(TIE);
        }
        return playerID;
    }

    private int checkSinglePlayerWin(int playerID){
        if(playerID == 0){
            player1.sendMessageToPlayer(String.format(WINNER, player1.getName()));
        } else if (playerID == 1) {
            player1.sendMessageToPlayer(String.format(BOT_WINS));
        } else if (playerID == 2) {
            player1.sendMessageToPlayer(TIE);
        }
        return playerID;
    }

    public void initiatePlayers() {
        if(isSinglePlayer){
            player1.setPlayerMove();
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
