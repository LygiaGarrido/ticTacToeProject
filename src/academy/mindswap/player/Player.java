package academy.mindswap.player;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static academy.mindswap.utils.Messages.*;
/**
 *
 * The Player Class is a fundamental part of the Tic-Tac-Toe Project
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
 * The Player Class holds the methods to create new clients to play the game
 */
public class Player {
    private Socket socket;
    private BufferedWriter writer;
    private BufferedReader reader;
    private Scanner consoleReader;

    /**
     * Player's main class which instantiates a new player, starts the console reader
     * and establishes a connection with the server.
     *
     * @param args
     * @throws IOException
     */

    public static void main(String[] args) throws IOException {
        Player player = new Player();
        player.startConsoleReader();
        player.handleServer();

    }

    /**
     * Starts a new Scanner to read from console.
     */
    private void startConsoleReader() {
        consoleReader = new Scanner(System.in);
    }

    /**
     * Informs the host and port to establish connection with server,
     * listens to and communicates with server.
     *
     * @throws IOException
     */
    private void handleServer() throws IOException {
        connectToServer("localhost", 8081);

        startListenToServer();

        communicateWithServer();

    }

    /**
     * Initializes the parameters: socket, writer, and reader
     *
     *
     * @param host
     * @param port
     * @throws IOException
     */
    private void connectToServer(String host, int port) throws IOException {
        socket = new Socket(host, port);
        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    /**
     * This method creates a new thread to listen to the server
     * @throws IOException
     */
    private void startListenToServer() throws IOException {
        new Thread(new ServerListener(reader)).start();
    }

    /**
     * This method allows the player to send messages to the
     * server by calling the sendMessages() method and itself again,
     * to leave the communication path open.
     *
     * @throws IOException
     */
    private void communicateWithServer() throws IOException {
        try {
            if (!socket.isClosed()) {
                sendMessages();
                communicateWithServer();
            }
        } catch (IOException e) {
            System.out.println(DEAD_SERVER);
        }
    }

    /**
     * This method allows the Player's writer to send messages to the server.
     * @throws IOException
     */
    private void sendMessages() throws IOException {
        String message = readFromConsole();

        writer.write(message);
        writer.newLine();
        writer.flush();
    }

    /**
     * This method gets the input from the console to send the message to the server.
     *
     * @return a String with the message
     */
    private String readFromConsole() {
        String message = null;

        message = consoleReader.nextLine();

        return message;
    }

    /**
     * The Server Listener Class allows the player to read messages from the server
     * This inner class implements the interface Runnable
     */
    private class ServerListener implements Runnable {
        BufferedReader reader;

        /**
         * Constructor method to initialize the parameter
         * @param reader
         */
        public ServerListener(BufferedReader reader) {
            this.reader = reader;
        }

        /**
         * The override of the run() method calls the readMessage() method
         */
        @Override
        public void run() {
            try {
                readMessage();
            } catch (IOException e) {
            }
        }

        /**
         * This method allows the reader to read line and prints it.
         * If the message sent from the server is null, it closes the connection,
         * otherwise it keeps the communication channel open.
         *
         * @throws IOException
         */
        private void readMessage() throws IOException {
            String readMessageFromServer = reader.readLine();
            if (readMessageFromServer == null) {
                writer.close();
                return;
            }
            System.out.println(readMessageFromServer);
            readMessage();
            }

        }

    }
