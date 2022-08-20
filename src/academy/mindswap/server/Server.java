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


inform player's turn (and consider only the correct player's move - in case the other player does sth wrong)
check for winners after each move
end game when there is a winner or a tie
consider tie
inform player that other player has left
ask for game move only after 2 players have joined the room
check if move position is already taken and do not allow a new move over it



 */