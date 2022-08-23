package academy.mindswap.game;

import static academy.mindswap.utils.Colors.*;

public class SinglePlayer {
    int numberOfPlays;
    int res1;

    int score;

    String playMove;

    public SinglePlayer() {
        this.numberOfPlays = 0;
        this.res1 = -1;
        this.score = 0;
    }

    public int botMovement(String[][] board, PlayerHandler player){
        String playerMove = player.getPlayerMove();
         res1 = botMovementToDefendOrWin(board, playMove);

        if(res1 == -1){
            int res2 = botMovementToDefendOrWin(board, playerMove);

            if( res2 == -1){
                return chooseRandomEmptyPosition(board);
            }else{
                return res2;
            }
        }else {
            return res1;
        }
    }
    public int singlePlayerCheckWin(String[][] board, PlayerHandler player){
        numberOfPlays++;
        for (int r = 0; r < 3; r++) {
            if (board[r][0].equals( player.getPlayerMove())
                    && board[r][1].equals( player.getPlayerMove())
                    && board[r][2].equals( player.getPlayerMove())) {
                return 0;
            }


            if (board[0][r].equals( player.getPlayerMove())
                    && board[1][r].equals( player.getPlayerMove())
                    && board[2][r].equals( player.getPlayerMove())) {

                return 0;
            }
        }
        //checks diagonals for win-condition
        if (board[0][0].equals( player.getPlayerMove())
                && board[1][1].equals(player.getPlayerMove())
                && board[2][2].equals(player.getPlayerMove())) {

            return 0;
        }
        if (board[0][2].equals( player.getPlayerMove())
                && board[1][1].equals( player.getPlayerMove())
                && board[2][0].equals( player.getPlayerMove())) {

            return 0;
        }
        if(numberOfPlays > 8){
            return 2;
        }
        if (res1 != -1){
            return 1;
        }
        return 3;
    }

    public static int botMovementToDefendOrWin (String[][] board, String playerSymbol){

        if(board[0][1].contains(playerSymbol)
                && board[0][2].contains(playerSymbol)
                && board[0][0].contains(" ")){
            return 0;
        }

        if(board[0][0].contains(playerSymbol)
                && board[0][1].contains(playerSymbol)
                && board[0][2].contains(" ")){
            return 2;
        }
        if(board[0][0].contains(playerSymbol)
                && board[1][0].contains(playerSymbol)
                && board[2][0].contains(" ")){
            return 6;
        }
        if(board[1][0].contains(playerSymbol)
                && board[2][0].contains(playerSymbol)
                && board[0][0].contains(" ")){
            return 0;
        }
        if(board[0][1].contains(playerSymbol)
                && board[1][1].contains(playerSymbol)
                && board[2][1].contains(" ")){
            return 7;
        }
        if(board[1][1].contains(playerSymbol)
                && board[2][1].contains(playerSymbol)
                && board[0][1].contains(" ")){
            return 1;
        }
        if(board[0][2].contains(playerSymbol)
                && board[1][2].contains(playerSymbol)
                && board[2][2].contains(" ")){
            return 8;
        }
        if(board[1][2].contains(playerSymbol)
                && board[2][2].contains(playerSymbol)
                && board[0][2].contains(" ")){
            return 2;
        }
        if(board[1][0].contains(playerSymbol)
                && board[1][1].contains(playerSymbol)
                && board[1][2].contains(" ")){
            return 5;
        }
        if(board[1][2].contains(playerSymbol)
                && board[1][1].contains(playerSymbol)
                && board[1][0].contains(" ")){
            return 3;
        }
        if(board[2][0].contains(playerSymbol)
                && board[2][1].contains(playerSymbol)
                && board[2][2].contains(" ")){
            return 8;
        }
        if(board[2][2].contains(playerSymbol)
                && board[2][1].contains(playerSymbol)
                && board[2][0].contains(" ")){
            return 6;
        }
        if(board[0][0].contains(playerSymbol)
                && board[1][1].contains(playerSymbol)
                && board[2][2].contains(" ")){
            return 8;
        }
        if(board[1][1].contains(playerSymbol)
                && board[2][2].contains(playerSymbol)
                && board[0][0].contains(" ")){
            return 0;
        }
        if(board[0][2].contains(playerSymbol)
                && board[1][1].contains(playerSymbol)
                && board[2][0].contains(" ")){
            return 6;
        }
        if(board[2][0].contains(playerSymbol)
                && board[1][1].contains(playerSymbol)
                && board[0][2].contains(" ")){
            return 2;
        }
        if(board[0][0].contains(playerSymbol)
                && board[2][0].contains(playerSymbol)
                && board[1][0].contains(" ")){
            return 3;
        }
        if(board[0][1].contains(playerSymbol)
                && board[2][1].contains(playerSymbol)
                && board[1][1].contains(" ")){
            return 4;
        }
        if(board[0][2].contains(playerSymbol)
                && board[2][2].contains(playerSymbol)
                && board[1][2].contains(" ")){
            return 5;
        }
        if(board[0][0].contains(playerSymbol)
                && board[0][2].contains(playerSymbol)
                && board[0][1].contains(" ")){
            return 1;
        }
        if(board[1][0].contains(playerSymbol)
                && board[1][2].contains(playerSymbol)
                && board[1][1].contains(" ")){
            return 4;
        }
        if(board[2][0].contains(playerSymbol)
                && board[2][2].contains(playerSymbol)
                && board[2][1].contains(" ")){
            return 7;
        }
        if(board[0][0].contains(playerSymbol)
                && board[2][2].contains(playerSymbol)
                && board[1][1].contains(" ")){
            return 4;
        }
        if(board[0][2].contains(playerSymbol)
                && board[2][0].contains(playerSymbol)
                && board[1][1].contains(" ")){
            return 4;
        }

        return -1;
    }

    public static int chooseRandomEmptyPosition(String[][] board) {
        int randomPosition = (int) (Math.random() * 9);
        while (!board[randomPosition / 3][randomPosition % 3].equals(" ")) {
            randomPosition = (int) (Math.random() * 9);
        }
        return randomPosition;
    }
    public void resetNumberOfPlays(){
        numberOfPlays = 0;
    }

    public void setPlayMove(PlayerHandler player){
        if (player.getPlayerMove().equals(TEXT_RED + "X" + TEXT_RESET)){
            this.playMove = TEXT_CYAN + "O" + TEXT_RESET;
            return;
        }
        this.playMove = TEXT_RED + "X" + TEXT_RESET;
    }

    public int getScore() {
        return score;
    }

    public void setScore() {
        score ++;
    }

    public String getPlayMove() {
        return playMove;
    }


    public void resetPlayBOT(){
        res1=-1;
    }
}
