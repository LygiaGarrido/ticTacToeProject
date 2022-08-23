package academy.mindswap.game;

public class SinglePlayer {

    public static int botMovement(String[][] board){
        int res1 = botMovementToDefendOrWin(board, "O");
        if(res1 == -1){
            int res2 = botMovementToDefendOrWin(board, "X");
            if( res2 == -1){
                return chooseRandomEmptyPosition(board);
            }else{
                return res2;
            }
        }else {
            return res1;
        }
    }

    public static int botMovementToDefendOrWin (String[][] board, String playerSymbol){


        if(board[0][1].contains(playerSymbol)
                && board[0][2].contains(playerSymbol)
                && !board[0][0].contains(playerSymbol)){
            return 0;
        }

        if(board[0][0].contains(playerSymbol)
                && board[0][1].contains(playerSymbol)
                && !board[0][2].contains(playerSymbol)){
            return 2;
        }
        if(board[0][0].contains(playerSymbol)
                && board[1][0].contains(playerSymbol)
                && !board[2][0].contains(playerSymbol)){
            return 6;
        }
        if(board[1][0].contains(playerSymbol)
                && board[2][0].contains(playerSymbol)
                && !board[0][0].contains(playerSymbol)){
            return 0;
        }
        if(board[0][1].contains(playerSymbol)
                && board[1][1].contains(playerSymbol)
                && !board[2][1].contains(playerSymbol)){
            return 7;
        }
        if(board[1][1].contains(playerSymbol)
                && board[2][1].contains(playerSymbol)
                && !board[0][1].contains(playerSymbol)){
            return 1;
        }
        if(board[0][2].contains(playerSymbol)
                && board[1][2].contains(playerSymbol)
                && !board[2][2].contains(playerSymbol)){
            return 8;
        }
        if(board[1][2].contains(playerSymbol)
                && board[2][2].contains(playerSymbol)
                && !board[0][2].contains(playerSymbol)){
            return 2;
        }
        if(board[1][0].contains(playerSymbol)
                && board[1][1].contains(playerSymbol)
                && !board[1][2].contains(playerSymbol)){
            return 5;
        }
        if(board[1][2].contains(playerSymbol)
                && board[1][1].contains(playerSymbol)
                && !board[1][0].contains(playerSymbol)){
            return 3;
        }
        if(board[2][0].contains(playerSymbol)
                && board[2][1].contains(playerSymbol)
                && !board[2][2].contains(playerSymbol)){
            return 8;
        }
        if(board[2][2].contains(playerSymbol)
                && board[2][1].contains(playerSymbol)
                && !board[2][0].contains(playerSymbol)){
            return 6;
        }
        if(board[0][0].contains(playerSymbol)
                && board[1][1].contains(playerSymbol)
                && !board[2][2].contains(playerSymbol)){
            return 8;
        }
        if(board[1][1].contains(playerSymbol)
                && board[2][2].contains(playerSymbol)
                && !board[0][0].contains(playerSymbol)){
            return 0;
        }
        if(board[0][2].contains(playerSymbol)
                && board[1][1].contains(playerSymbol)
                && !board[2][0].contains(playerSymbol)){
            return 6;
        }
        if(board[2][0].contains(playerSymbol)
                && board[1][1].contains(playerSymbol)
                && !board[0][2].contains(playerSymbol)){
            return 2;
        }
        if(board[0][0].contains(playerSymbol)
                && board[2][0].contains(playerSymbol)
                && !board[1][0].contains(playerSymbol)){
            return 3;
        }
        if(board[0][1].contains(playerSymbol)
                && board[2][1].contains(playerSymbol)
                && !board[1][1].contains(playerSymbol)){
            return 4;
        }
        if(board[0][2].contains(playerSymbol)
                && board[2][2].contains(playerSymbol)
                && !board[1][2].contains(playerSymbol)){
            return 5;
        }
        if(board[0][0].contains(playerSymbol)
                && board[0][2].contains(playerSymbol)
                && !board[0][1].contains(playerSymbol)){
            return 1;
        }
        if(board[1][0].contains(playerSymbol)
                && board[1][2].contains(playerSymbol)
                && !board[1][1].contains(playerSymbol)){
            return 4;
        }
        if(board[2][0].contains(playerSymbol)
                && board[2][2].contains(playerSymbol)
                && !board[2][1].contains(playerSymbol)){
            return 7;
        }
        if(board[0][0].contains(playerSymbol)
                && board[2][2].contains(playerSymbol)
                && !board[1][1].contains(playerSymbol)){
            return 4;
        }
        if(board[0][2].contains(playerSymbol)
                && board[2][0].contains(playerSymbol)
                && !board[1][1].contains(playerSymbol)){
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
}
