package academy.mindswap.game;

import academy.mindswap.server.Server;


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
    private int numberOfPlays = 0;

    private String[][] board;
//    private GameLogic gameLogic;


    public Game(Server server) {
        this.server = server;
        threadPool = Executors.newFixedThreadPool(NUMBER_OF_PLAYER);
        listOfPlayers = new CopyOnWriteArrayList<>();
        board = new String[3][3];

    }

    public void startGame() {
        isGameStarted = true;
        //gameLogic = new GameLogic();
        initiateBoard();
        initiatePlayers();
        broadCastToAllPlayers(WELCOME_TO_TICTACTOE);
        broadCastToAllPlayers(drawBoard());
        while (isGameStarted) {
            for (int i = 0; i < listOfPlayers.size(); i++) {
                    doTurn(listOfPlayers.get(i));
                    if(checkWin(listOfPlayers.get(i))){
                        endGame();
                        break;
                }
            }
        }
        broadCastToAllPlayers(THANK_YOU_FOR_PLAYING);
    }

    public void endGame(){
        isGameStarted = false;
    }

    public synchronized List<PlayerHandler> getListOfPlayers() {
        return listOfPlayers;
    }

    private void initiateBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = " ";
            }
        }
    }

    private String drawBoard() {
        String bordDraw = board[0][0] + "|" + board[0][1] + "|" + board[0][2] + "\n"
                + "_______" + "\n" + board[1][0] + "|" + board[1][1] + "|" + board[1][2] + "\n" + "______"
                + "\n" + board[2][0] + "|" + board[2][1] + "|" + board[2][2];
        return bordDraw;
    }

    private void doTurn(PlayerHandler player) {
        String move;
        int row;
        int column;
        player.sendMessageToPlayer(CHOOSE_ROW);
        move = player.listenFromPlayer();
        while (!move.matches("[0-2]")) {
            player.sendMessageToPlayer(CHOOSE_ROW);
            move = player.listenFromPlayer();
        }
        row = Integer.parseInt(move);

        player.sendMessageToPlayer(CHOOSE_COLUMN);
        move = player.listenFromPlayer();
        while (!move.matches("[0-2]")) {
            player.sendMessageToPlayer(CHOOSE_COLUMN);
            move = player.listenFromPlayer();
        }
        column = Integer.parseInt(move);

        if (!submitMove(row, column, player)) {
            player.sendMessageToPlayer(CHOOSE_ANOTHER_ONE);
            doTurn(player);
        }
    }


    public boolean submitMove(int row, int column, PlayerHandler player) {
        if (board[row][column].equalsIgnoreCase(" ")) {
            board[row][column] = player.playerMove;
            broadCastToAllPlayers(drawBoard());
            return true;
        }
        return false;
    }

    private boolean checkWin(PlayerHandler player) {
        numberOfPlays ++;
        for (int r = 0; r < 3; r++) {
            if (board[r][0] == player.playerMove && board[r][1] == player.playerMove && board[r][2] == player.playerMove) {
                ;
                player.sendMessageToPlayer(String.format(WINNER,player.getName()));
                broadCast(LOSER,player);
                return true;
            }

            if (board[0][r] == player.playerMove && board[1][r] == player.playerMove && board[2][r] == player.playerMove) {
                player.sendMessageToPlayer(String.format(WINNER,player.getName()));
                broadCast(LOSER,player);
                return true;
            }
        }
        //checks diagonals for win-condition
        if (board[0][0] == player.playerMove && board[1][1] == player.playerMove && board[2][2] == player.playerMove) {
            player.sendMessageToPlayer(String.format(WINNER,player.getName()));
            broadCast(LOSER,player);
            return true;
        }
        if (board[0][2] == player.playerMove && board[1][1] == player.playerMove && board[2][0] == player.playerMove) {
            player.sendMessageToPlayer(String.format(WINNER, player.getName()));
            broadCast(LOSER, player);
            return true;
        }
        if(numberOfPlays > 8){
            broadCastToAllPlayers(TIE);
            return true;
        }
        return false;
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
    public void initiatePlayers(){
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

   /* public String gameStatus(int playerId) {
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
    }*/


    public class PlayerHandler implements Runnable {
        private Socket playerSocket;
        private String name;
        private int id;

        private String playerMove;

        private BufferedReader reader;
        private BufferedWriter writer;
        private String message;

        /*public void setId(int id) {
            this.id = id;
        }*/

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
                /*String[] splitted = message.split(" ");
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

                }*/
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return message;
        }

        public String getName() {
            return name;
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
            playerMove = listenFromPlayer();
            while (!playerMove.matches("[(X|O)]")) {
                sendMessageToPlayer(INVALID_INPUT);
                sendMessageToPlayer(ASK_PLAYER_MOVE);
                playerMove = listenFromPlayer();
            }
            while (listOfPlayers.get(0).playerMove.equalsIgnoreCase(listOfPlayers.get(1).playerMove)){
                sendMessageToPlayer(CHOOSE_ANOTHER_ONE);
                sendMessageToPlayer(ASK_PLAYER_MOVE);
                playerMove = listenFromPlayer();
            }
            sendMessageToPlayer(THE_GAME_WILL_BEGIN_SHORTLY);
            broadCast(String.format(NEW_PLAYER_HAS_ARRIVED, name),this);

        }

        @Override
        public void run() {
            sendMessageToPlayer(WAITING_FOR_OPPONENT);
            addPlayerToList(this);
           /* sendMessageToPlayer(ASK_FOR_NAME);
            name = listenFromPlayer();
            while (!name.matches("[a-zA-Z]+")) {
                sendMessageToPlayer(INVALID_INPUT);
                sendMessageToPlayer(ASK_FOR_NAME);
                name = listenFromPlayer();
            }
            sendMessageToPlayer(ASK_PLAYER_MOVE);
            playerMove = listenFromPlayer();
            while (!playerMove.matches("[(X|O)]")) {
                sendMessageToPlayer(INVALID_INPUT);
                sendMessageToPlayer(ASK_PLAYER_MOVE);
                playerMove = listenFromPlayer();
            }

            readyToStart = true;

            broadCast(String.format(NEW_PLAYER_HAS_ARRIVED, name),this);

            if (listOfPlayers.size() < 2) {
                sendMessageToPlayer(WAITING_FOR_PLAYERS_TO_CONNECT);
            }
            */
            while (true) {
                if (Thread.interrupted()) {
                    return;
                }

            }
        }
    }
}
