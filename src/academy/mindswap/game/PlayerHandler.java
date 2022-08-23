package academy.mindswap.game;

import java.io.*;
import java.net.Socket;

import static academy.mindswap.utils.Colors.*;
import static academy.mindswap.utils.Messages.*;
/**
 *
 * The PlayerHandler Class is a fundamental part of the Tic-Tac-Toe Project
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
 * The Player Handler class holds the methods to handle the players connected to the server
 */

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

    /**
     * The constructor method which initializes some parameters.
     * @param playerSocket
     */
    public PlayerHandler(Socket playerSocket) {
        this.playerSocket = playerSocket;
        try {
            reader = new BufferedReader(new InputStreamReader(playerSocket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(playerSocket.getOutputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * This method sends a given message to the player.
     *
     * @param message
     */
    public void sendMessageToPlayer(String message) {
        try {
            writer.write(message);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method reads the player's input and returns the message as a string.
     * @return
     */

    public String listenFromPlayer() {
        String message;
        try {
            message = reader.readLine();
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

    public void endGame(){
        isPlaying = false;
    }

    /**
     * This method allows the player to set their name in the beginning of the interactions with the server.
     * It ends the connection if the input is null
     */
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

    /**
     * This method asks for input from player to choose the move symbol.
     * It validates the input and sets the color for each symbol.
     */
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

    /**
     * This ends the connection between player and server
     */
    public void closeSocket() {
        try {
            playerSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

