package academy.mindswap.game;

import java.util.Objects;
import java.util.Random;

import static academy.mindswap.utils.Messages.*;

public class GameLogic {

    private String[][] board = new String[3][3];


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



    public  int checkWinner(int playerId){
        for (int r = 0; r < 3; r++) {
            if (Objects.equals(board[r][0], board[r][1])
                    && Objects.equals(board[r][1], board[r][2])
                    && !Objects.equals(board[r][0], "    ")){
                if(board[r][0].equals("  X  ") && playerId == 1 || board[r][0].equals("  O  ") && playerId == 0){
                    return 1;
                } else {
                    return 0;
                }
            }
        }
        //loops through columns checking if win-condition exists
        for (int c = 0; c < 3; c++) {
            if (Objects.equals(board[0][c], board[1][c])
                    && Objects.equals(board[1][c], board[2][c])
                    && !Objects.equals(board[0][c], "    ")){
                if(board[0][c].equals("  X  ") && playerId == 1 || board[0][c].equals("  O  ") && playerId == 0){
                    return 1;
                } else {
                    return 0;
                }
            }
        }
        //checks diagonals for win-condition
        if (Objects.equals(board[0][0], board[1][1])
                && Objects.equals(board[1][1], board[2][2])
                && !Objects.equals(board[0][0], "    ")){
            if(board[0][0].equals("  X  ") && playerId == 1 || board[0][0].equals("  O  ") && playerId == 0){
                return 1;
            } else {
                return 0;
            }
        }

        if (Objects.equals(board[0][2], board[1][1])
                && Objects.equals(board[1][1], board[2][0])
                && !Objects.equals(board[0][2], "    ")){
            if(board[0][2].equals("  X  ") && playerId == 1 || board[0][2].equals("  O  ") && playerId == 0) {
                return 1;
            } else {
                return 0;
            }
        }

        return 2;

    }

    public void makeMove(String userInput, String symbol){

            if(userInput == null || !userInput.matches("[0-8]")){
                System.out.println(INVALID_INPUT);
            }
            Integer number = Integer.parseInt(userInput);
            board[number/3][number%3] = symbol;

        }

}



