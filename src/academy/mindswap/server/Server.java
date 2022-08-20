package academy.mindswap.server;

import academy.mindswap.game.Game;

import java.io.IOException;
import java.net.ServerSocket;

import static academy.mindswap.utils.Messages.*;

public class Server {
    private ServerSocket serverSocket;
    private Game game;
    private static final int PORT = 8080;

    public static void main(String[] args) {
        Server server = new Server();
        try {
            server.start(PORT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void createGame(){
        game = new Game(this);
    }

    public void start(int port) throws IOException {
        System.out.println(WAITING_FOR_PLAYERS_TO_CONNECT);
        serverSocket = new ServerSocket(port);
        createGame();
        while (serverSocket.isBound()){
            game.acceptPlayer(serverSocket.accept());
            System.out.println(NEW_USER_HAS_CONNECTED);

        }


    }
}

/* TODO
send updated board to player after each interaction:
- after sending name -> empty board
- after each move -> updated board

inform player's turn (and consider only the correct player's move - in case the other player does sth wrong)
check for winners after each move
inform winner when there is one, and end game

find a place to print the board lol (player holds the function to print board)


inform player that other player has left



 */