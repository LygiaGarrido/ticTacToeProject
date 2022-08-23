package academy.mindswap.game;

import java.util.Objects;
import java.util.Random;

import static academy.mindswap.utils.Messages.*;
/**
 *
 * The GameLogic Class is a fundamental part of the Tic-Tac-Toe Project
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
 * The Game Logic class holds the game logic for the tic-tac-toe game
 */
public class GameLogic {

    private String[][] board = new String[3][3];
    int numberOfPlays = 0;


    public String[][] getBoard() {
        return board;
    }

    /**
     * The constructor method which calls the createBoard() method
     */
    public GameLogic() {
        createBoard();
    }

    /**
     * The logic for the board creation, which fills the string
     * double array with empty spaces, in order to draw the board.
     */
    public void createBoard() {
        numberOfPlays = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = " ";
            }
        }
    }

    /**
     * This contains the logic to check for winners
     * It increments the moves' counter and checks rows,
     * columns and diagonals for the winning condition.
     * It also checks for ties
     *
     * The returned number is used to consider winners/ losers
     * (by returning their ids), tie or next move.
     *
     * @param player
     * @return int
     */
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

    /**
     * This method allows a given player to input their move
     * It sends a message to ask for the desired position and validates it.
     *
     * @param player
     */
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

    /**
     * This method receives the string userInput, turns it into an Integer
     * and gets the position in the matrix ([][])
     * It checks if the space is available and passes the player's move
     *
     * @param userInput
     * @param player
     * @return boolean
     */
    public boolean submitMove(String userInput, PlayerHandler player) {
        Integer number = Integer.parseInt(userInput);

        if (board[number / 3][number % 3].equalsIgnoreCase(" ")) {
            board[number / 3][number % 3] = player.getPlayerMove();
            return true;
        }
        return false;
    }

    /**
     * Used in Single Player mode only.
     * This is the method responsible for filling the board when the bot makes the move.
     *
     * @param num
     * @param singlePlayer
     */
    public void fillBoard(int num, SinglePlayer singlePlayer){

        board[num / 3][num % 3] = singlePlayer.playMove;
    }

}



