package academy.mindswap.game;

import academy.mindswap.player.Player;

public class GameLogic {
    private Game.PlayerHandler player1;
    private Game.PlayerHandler player2;

    private String[][] board = new String[3][3];

    public void createBoard(){

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = "    ";
            }

        }

        System.out.println();
        System.out.println(board[0][0] + "|" + board[0][1] + "|" + board[0][2]);
        drawSeparator();
        System.out.println(board[1][0] + "|" + board[1][1] + "|" + board[1][2]);
        drawSeparator();
        System.out.println(board[2][0] + "|" + board[2][1] + "|" + board[2][2]);
        System.out.println();

    }

    public  boolean checkWinner(){
        for (int r = 0; r < 3; r++) {
            if (board[r][0] == board[r][1] && board[r][1] == board[r][2] && board[r][0] != "-")
                return true;
        }
        //loops through columns checking if win-condition exists
        for (int c = 0; c < 3; c++) {
            if (board[0][c] == board[1][c] && board[1][c] == board[2][c] && board[0][c] != "-" )
                return true;
        }
        //checks diagonals for win-condition
        if (board[0][0] == board[1][1] && board[1][1] == board[2][2] && board[0][0] != "-")
            return true;

        if (board[0][2] == board[1][1] && board[1][1] == board[2][0] && board[0][0] != "-")
            return true;

        return false;

    }

    public static void drawSeparator() {
        System.out.println("----------------");
    }
}



