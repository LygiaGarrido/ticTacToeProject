package academy.mindswap.game;

import java.io.*;
import java.net.Socket;

import static academy.mindswap.utils.Colors.*;
import static academy.mindswap.utils.Messages.*;

public class PlayerHandler {


    private Socket playerSocket;
    private String name;
    private int id;

    private int score;

    private String playerMove;

    private BufferedReader reader;
    private BufferedWriter writer;
    private String message;
    private boolean isPlaying;

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

    public boolean isPlaying() {
        return isPlaying;
    }

    public void startGame(){
        isPlaying = true;
    }

    public void setName() {
        sendMessageToPlayer(ASK_FOR_NAME);
        name = listenFromPlayer();
        if(name == null){
            closeSocket();
        }
        while (!name.matches("[a-zA-Z]+")) {
            sendMessageToPlayer(INVALID_INPUT);
            sendMessageToPlayer(ASK_FOR_NAME);
            name = listenFromPlayer();
        }
    }

    public void setPlayerMove() {
        sendMessageToPlayer(ASK_PLAYER_MOVE);
        playerMove = listenFromPlayer().toUpperCase();
        while (!playerMove.matches("[(X|O)]")) {
            sendMessageToPlayer(INVALID_INPUT);
            sendMessageToPlayer(ASK_PLAYER_MOVE);
            playerMove = listenFromPlayer();
        }
        if(playerMove.equals("X")){
            playerMove = TEXT_RED + playerMove + TEXT_RESET;
        }
        if(playerMove.equals("O")){
            playerMove = TEXT_CYAN + playerMove + TEXT_RESET;
        }
    }




    public String getName () {
        return name;
    }

    public String getPlayerMove () {
        return playerMove;
    }

    public void setId ( int id){
        this.id = id;
    }

    public int getId () {
        return id;
    }

    public boolean isOffline(){
        if (playerSocket == null){
            return true;
        }
        return playerSocket.isClosed();
    }

    public int getScore() {
        return score;
    }

    public void setScore() {
        score++;
    }

    public void closeSocket() {
        try {
            playerSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

