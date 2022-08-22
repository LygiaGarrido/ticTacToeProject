package academy.mindswap.game;

import academy.mindswap.server.Server;


import javax.sound.midi.Soundbank;
import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.*;

import static academy.mindswap.utils.Messages.*;


public class Game {

    private Server server;
    public List<PlayerHandler> listOfPlayers;
    private ExecutorService threadPool;
    private static final int NUMBER_OF_PLAYER = 2;

    private boolean isGameStarted;
    private boolean isFirstGame;

    private int numberOfPlays = 0;

    private GameLogic gameLogic;

    private static String[][] board;

    public Game(Server server) {
        this.server = server;
        threadPool = Executors.newFixedThreadPool(NUMBER_OF_PLAYER);
        listOfPlayers = new CopyOnWriteArrayList<>();
        gameLogic = new GameLogic();
        board = gameLogic.getBoard();
        isFirstGame = true;

    }

    public void startGame() {
        isGameStarted = true;

        if (isFirstGame) {
            isFirstGame = false;
            initiatePlayers();
        }

        broadCastToAllPlayers(WELCOME_TO_TICTACTOE);
        broadCastToAllPlayers(drawBoard());
        while (isGameStarted) {

            for (int i = 0; i < listOfPlayers.size(); i++) {
                PlayerHandler player = listOfPlayers.get(i);
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

    public void playAgain() {

        broadCastToAllPlayers(PLAY_AGAIN);
        PlayerHandler player1 = listOfPlayers.get(0);
        PlayerHandler player2 = listOfPlayers.get(1);

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

        for (int i = 0; i < listOfPlayers.size(); i++) {
            listOfPlayers.get(i).closeSocket();
        }
    }

    public synchronized List<PlayerHandler> getListOfPlayers() {
        return listOfPlayers;
    }


  /*  private String drawBoard() {  // previous  method
         String bordDraw = "\n" + board[0][0] + "|" + board[0][1] + "|" + board[0][2] + "\n"
                + "______" + "\n" + board[1][0] + "|" + board[1][1] + "|" + board[1][2] + "\n" + "______"
                + "\n" + board[2][0] + "|" + board[2][1] + "|" + board[2][2] + "\n";
        return bordDraw;
    }*/

    private String drawBoard() {
        String bordDraw = "\n"+"    0   1   2 "
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
                +"\n" + "    6   7   8   ";
        return bordDraw;
    }
   /* public static void drawBoard() {  // experimental  method
        for (int i = 0; i <board.length ; i++) {
            StringBuilder line = new StringBuilder("|");
            for (int j = 0; j < board.length ; j++) {
                line.append(board[i][j]).append("|");
            }

        }
    }*/


    private int checkWin(int playerID) {
        PlayerHandler player1 = listOfPlayers.get(0);
        PlayerHandler player2 = listOfPlayers.get(1);
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


    public void acceptPlayer(Socket playerSocket) {
        //if (listOfPlayers.size() < 2) {
        threadPool.submit(new PlayerHandler(playerSocket));
        // }
    }

    public void addPlayerToList(PlayerHandler playerHandler) {
        //playerHandler.setId(listOfPlayers.size());
        listOfPlayers.add(playerHandler);
    }

    public void initiatePlayers() {
        listOfPlayers.forEach(player -> player.setNameAndPlayerMove());
    }

    public void broadCast(String message, PlayerHandler playerHandler) {
        listOfPlayers.stream()
                .filter(player -> !playerHandler.equals(player))
                .forEach(player -> player.sendMessageToPlayer(message));
    }

    public void broadCastToAllPlayers(String message) {
        listOfPlayers.stream().forEach(player -> player.sendMessageToPlayer(message));
    }

    public class PlayerHandler implements Runnable {
        private Socket playerSocket;
        private String name;
        private int id;

        private String playerMove;

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

        public void sendMessageToPlayer(String message) {
            try {
                writer.write(message);
                writer.newLine();
                writer.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public String listenFromPlayer() {
            String message;
            try {
                message = reader.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return message;
        }

        public String getName() {
            return name;
        }

        public String getPlayerMove() {
            return playerMove;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public void setNameAndPlayerMove() {
            sendMessageToPlayer(ASK_FOR_NAME);
            name = listenFromPlayer();
            while (!name.matches("[a-zA-Z]+")) {
                sendMessageToPlayer(INVALID_INPUT);
                sendMessageToPlayer(ASK_FOR_NAME);
                name = listenFromPlayer();
            }

            sendMessageToPlayer(ASK_PLAYER_MOVE);
            playerMove = listenFromPlayer().toUpperCase();
            while (!playerMove.matches("[(X|O)]")) {
                sendMessageToPlayer(INVALID_INPUT);
                sendMessageToPlayer(ASK_PLAYER_MOVE);
                playerMove = listenFromPlayer();
            }
            while (listOfPlayers.get(0).playerMove.equalsIgnoreCase(listOfPlayers.get(1).playerMove)) {
                sendMessageToPlayer(CHOOSE_ANOTHER_ONE);
                sendMessageToPlayer(ASK_PLAYER_MOVE);
                playerMove = listenFromPlayer();
            }
            sendMessageToPlayer(THE_GAME_WILL_BEGIN_SHORTLY);
            broadCast(String.format(NEW_PLAYER_HAS_ARRIVED, name), this);

        }

        @Override
        public void run() {
            sendMessageToPlayer(WAITING_FOR_OPPONENT);
            addPlayerToList(this);
            setId(listOfPlayers.size() - 1);

            while (true) {
                if (Thread.interrupted()) {
                    return;
                }

            }
        }

        private void closeSocket() {
            try {
                playerSocket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
