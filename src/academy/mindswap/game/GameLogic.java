package academy.mindswap.game;

import java.util.Objects;
import java.util.Random;

import static academy.mindswap.utils.Messages.*;

public class GameLogic {

    private String[][] board = new String[3][3];
    int numberOfPlays = 0;


    public String[][] getBoard() {
        return board;
    }

    public GameLogic() {
        createBoard();
    }

    public void createBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = " ";
            }
        }
    }

    public int checkWin(Game.PlayerHandler player) {
        numberOfPlays ++;
        for (int r = 0; r < 3; r++) {
            if (board[r][0] == player.getPlayerMove() && board[r][1] == player.getPlayerMove() && board[r][2] == player.getPlayerMove()) {

                return player.getId();
            }

            if (board[0][r] == player.getPlayerMove() && board[1][r] == player.getPlayerMove() && board[2][r] == player.getPlayerMove()) {

                return player.getId();
            }
        }
        //checks diagonals for win-condition
        if (board[0][0] == player.getPlayerMove() && board[1][1] == player.getPlayerMove() && board[2][2] == player.getPlayerMove()) {

            return player.getId();
        }
        if (board[0][2] == player.getPlayerMove() && board[1][1] == player.getPlayerMove() && board[2][0] == player.getPlayerMove()) {

            return player.getId();
        }
        if(numberOfPlays > 8){
            return 2;
        }
        return 3;
    }

    public void makeMove(Game.PlayerHandler player) {
        String userInput;
        player.sendMessageToPlayer(CHOOSE_POSITION);
        userInput = player.listenFromPlayer();
        if (!userInput.matches("[0-8]")) {
            System.out.println(INVALID_INPUT);
            player.sendMessageToPlayer(CHOOSE_POSITION);
            userInput = player.listenFromPlayer();
        }

        if (!submitMove(userInput, player)) {
            player.sendMessageToPlayer(CHOOSE_ANOTHER_ONE);
            makeMove(player);
        }


    }


    public boolean submitMove(String userInput, Game.PlayerHandler player) {
        Integer number = Integer.parseInt(userInput);

        if (board[number / 3][number % 3].equalsIgnoreCase(" ")) {
            board[number / 3][number % 3] = player.getPlayerMove();
           // Game.broadCastToAllPlayers(drawBoard());
            return true;
        }
        return false;
    }
    public void fillBoard(int num){
        board[num / 3][num % 3] = "O";
    }

}



