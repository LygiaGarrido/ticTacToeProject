
package academy.mindswap.utils;

import static academy.mindswap.utils.Colors.*;
/**
 *
 * The Messages Class is a fundamental part of the Tic-Tac-Toe Project
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
 * The class responsible for holding all the strings with the messages sent from server to players.
 */

public class Messages {
    public static final String STARTING_A_NEW_GAME = "A new opponent connect, the game is about to start !";
    public static final String SERVER_IS_RUNNING = "Server is running.";

    public static final String WELCOME = "Be welcome %s";

    public static final String MULTIPLAYER_OR_SINGLEPLAYER = "Choose: " + "\n" + "1 - for single player" + "\n" + "2 - for multiplayer";
    public static final String WAITING_FOR_PLAYERS_TO_CONNECT = "Waiting for players to connect...";
    public static final String WAITING_FOR_OPPONENT = "Waiting for opponent to connect";
    public static final String DEAD_SERVER = "Hum... it looks like the server is dead. We're sorry...";
    public static final String NEW_USER_HAS_CONNECTED = "A new player has joined the room. ";
    public static final String ASK_FOR_NAME = "Please, enter your name" /*following the pattern:<name> <yournamehere>"*/;
    public static final String NEW_PLAYER_HAS_ARRIVED = "%s has joined the game";
    public static final String ASK_FOR_POSITION = "Where would you like to make your move?\n" +
            "Enter your move following the pattern:<move> <0-8>";
    public static final String INVALID_INPUT = "Invalid input, try again.";//, please choose a number from 0 to 8 to play."
    public static final String LOSER = "Oh no %s... you have lost :( \nBetter luck next time";
    public static final String WINNER = TEXT_YELLOW + ("___________\n" +
            "            '._==_==_=_.'\n" +
            "            .-\\:      /-.\n" +
            "           | (|:.     |) |\n" +
            "            '-|:.     |-'\n" +
            "              \\::.    /\n" +
            "               '::. .'\n" +
            "                 ) (\n" +
            "               _.' '._\n" +
            "              `\"\"\"\"\"\"\"`\n"+"\n CONGRATULATIONS %S YOU ARE THE WINNER!!!") + TEXT_RESET;
    public static final String TIE = "IT'S A TIE!!";
    public static final String ASK_PLAYER_MOVE = "Choose your player move : X  or O";
    public static final String WELCOME_TO_TICTACTOE = TEXT_YELLOW + ("\n" +
            "█░█░█ █▀▀ █░░ █▀▀ █▀█ █▀▄▀█ █▀▀\n" +
            "▀▄▀▄▀ ██▄ █▄▄ █▄▄ █▄█ █░▀░█ ██▄\n" +
            "\n" +
            "▀█▀ █▀█  \n" +
            "░█░ █▄█  \n" +
            "\n" +
            "▀█▀ █ █▀▀ ▄▄ ▀█▀ ▄▀█ █▀▀ ▄▄ ▀█▀ █▀█ █▀▀\n" +
            "░█░ █ █▄▄ ░░ ░█░ █▀█ █▄▄ ░░ ░█░ █▄█ ██▄")+ TEXT_RESET;

    public static final String CHOOSE_ANOTHER_ONE = "Already taken, choose another one!";

    public static final String CHOOSE_POSITION = "CHOOSE POSITION [0-8] :";

    public static final String THANK_YOU_FOR_PLAYING = "Thank you for playing, see you next time :)";
    public static final String THE_GAME_WILL_BEGIN_SHORTLY = "The game will begin in just a moment ..";

    public static final String PLAY_AGAIN = "Wanna play again? 'Y' to play again 'N' to exit game";
    public static final String NO_MORE_PLAYING = " someone don't wanna play ";

    public static final String BOT_WINS = "BOT : HAHAHA I'm the smartest";


}
