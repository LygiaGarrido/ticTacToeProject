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
        numberOfPlays = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = " ";
            }
        }
    }

    public int checkWin(PlayerHandler player) {
        numberOfPlays ++;
        for (int r = 0; r < 3; r++) {
            if (board[r][0].equals( player.getPlayerMove())
                    && board[r][1].equals( player.getPlayerMove())
                    && board[r][2].equals( player.getPlayerMove())) {

                return player.getId();
            }

            if (board[0][r].equals( player.getPlayerMove())
                    && board[1][r].equals( player.getPlayerMove())
                    && board[2][r].equals( player.getPlayerMove())) {

                return player.getId();
            }
        }
        //checks diagonals for win-condition
        if (board[0][0].equals( player.getPlayerMove())
                && board[1][1].equals( player.getPlayerMove())
                && board[2][2].equals( player.getPlayerMove())) {

            return player.getId();
        }
        if (board[0][2].equals( player.getPlayerMove())
                && board[1][1].equals( player.getPlayerMove())
                && board[2][0].equals( player.getPlayerMove())) {

            return player.getId();
        }
        if(numberOfPlays > 8){
            return 2;
        }
        return 3;
    }

    public void makeMove(PlayerHandler player) {
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

    public boolean submitMove(String userInput, PlayerHandler player) {
        Integer number = Integer.parseInt(userInput);

        if (board[number / 3][number % 3].equalsIgnoreCase(" ")) {
            board[number / 3][number % 3] = player.getPlayerMove();
            return true;
        }
        return false;
    }

    public void fillBoard(int num){
        board[num / 3][num % 3] = "O";
    }

}



