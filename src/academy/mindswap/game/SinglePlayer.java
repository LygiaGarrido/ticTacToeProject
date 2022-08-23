package academy.mindswap.game;

import static academy.mindswap.utils.Colors.*;

/**
 *
 * The SinglePlayer Class is a fundamental part of the Tic-Tac-Toe Project
 *
 * The software was developed as a group project for the MindSwap program.
 *
 * Copyright 2022 Lygia Garrido, Tiago Costa and Rui Vieira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


/**
 * This class holds the logic for the single player mode
 */
public class SinglePlayer {



    int numberOfPlays;
    int res1;

    int score;

    String playMove;

    /**
     * The constructor method, which initializes some parameters.
     */
    public SinglePlayer() {
        this.numberOfPlays = 0;
        this.res1 = -1;
        this.score = 0;
    }

    /**
     * This method checks whether the bot (or the player) is on the verge of winning
     * If the bot is one move away from winning, it will make the winning move,
     * if the player is about to win, it will prevent it.
     * In case neither is about to win, it will choose a random position to make the move.
     *
     * @param board
     * @param player
     * @return int
     */
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

    /**
     * This contains the logic to check for winners
     * It increments the moves' counter and checks rows,
     * columns and diagonals for the winning condition.
     * It also checks for ties
     * The returned number is used to consider winners/ losers, tie or next move.
     *
     * @param board
     * @param player
     * @return int
     */
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

    /**
     * This method allows the bot to check the board after the player's move
     * in order to pick a position to make the move.
     * It checks every possible pair of repeated symbols (rows, columns and diagonals)
     *
     * @param board
     * @param playerSymbol
     * @return int position to make the move
     */
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

    /**
     * This method is called in case there is no need to defend from the player's move
     * It chooses a random number from 0 to 8 and checks its availability
     *
     * @param board
     * @return int position to make the move
     */
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

    /**
     * This method is responsible for checking the symbol picked by the player
     * and to assign the opposite to the bot for the game to start.
     *
     * @param player
     */
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
