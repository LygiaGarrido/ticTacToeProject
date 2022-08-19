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
