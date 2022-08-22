package academy.mindswap.server;

import academy.mindswap.game.Game;

import java.io.IOException;
import java.net.ServerSocket;

import static academy.mindswap.utils.Messages.*;

public class Server {
    private ServerSocket serverSocket;
    private Game game;

    private int numberOfPlayers;
    private static final int PORT = 8081;

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
        int numberOfPlayer = 0;
        acceptPlayer();
        if(numberOfPlayer<2){
            acceptPlayer();
        }
            game.startGame();
        }


    public void acceptPlayer() throws IOException {
        game.acceptPlayer(serverSocket.accept());
        numberOfPlayers++;
        System.out.println(NEW_USER_HAS_CONNECTED);


}
}

/* TODO

end game when there is a winner or a tie -> stops the game, needs to close the players socket
after end ask to play again
inform player that other player has left
pass commands in listen for player





 */