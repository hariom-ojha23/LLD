package problems.SnakeAndLadder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 
 * SnakeLadder design
 * 
 * Functional requirement
 * 
 * 1. It is board game.
 * 2. Multiple users can play at a time
 * 3. If users land on ladder cell. They will go up.
 * 4. If users land on snake cell. They will go down.
 * 5. Multiple users can be on same cell
 * 
 * 
 * Non-Functional requirement
 * 
 * 1. It should be extensible
 * 2. It should follow SOLID principle
 * 3. It should be maintainable
 * 4. Performance should be fast.
 * 
 * 
 * Core Classes/interfaces/enums
 * 
 * GameEngine
 * Board
 * SnakeAndLadderGameEngine
 * Player
 * Cell
 * Dice
 * SnakeAndLadder
 * CellType
 * GameStatus
 * 
 */

enum CellType {
    SNAKE,
    LADDER,
    NORMAL
}

enum GameStatus {
    WIN,
    IN_PROGRESS
}

abstract class GameEngine {
    List<Player> players = new ArrayList<>();

    // Index (FIFO) based player turn
    int currentTurn = 0;

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public boolean checkPlayerTurn(Player player) {
        int playerIndex = players.indexOf(player);

        if (playerIndex == currentTurn) {
            return true;
        }

        return false;
    }
}

abstract class Board {
    int gridSize;

    public void setGridSize(int size) {
        this.gridSize = size;
    }

    public int getGridSize() {
        return gridSize;
    }
}

class SnakeAndLadderBoard extends Board {
    private Map<Integer, Integer> snakePositionMap = new HashMap<>();
    private Map<Integer, Integer> ladderPositionMap = new HashMap<>();

    public void addSnake(int start, int end) {
        int size = getGridSize();

        if (start <= end) {
            System.out.println("Invalid: Snake start must be greater than end");
            return;
        }

        if (start > size || start < 1 || end > size || end < 1) {
            System.out.println("Invalid: Start and end position of snake");
            return;
        }

        if (snakePositionMap.containsKey(start) ||
                ladderPositionMap.containsKey(start)) {
            System.out.println("Invalid: Cell already contains a snake or ladder");
            return;
        }

        snakePositionMap.put(start, end);
    }

    public void addLadder(int start, int end) {
        int size = getGridSize();

        if (start >= end) {
            System.out.println("Invalid: Ladder start must be smaller than end");
            return;
        }

        if (start > size || start < 1 || end > size || end < 1) {
            System.out.println("Invalid: Start and end position of ladder");
            return;
        }

        if (snakePositionMap.containsKey(start) ||
                ladderPositionMap.containsKey(start)) {
            System.out.println("Invalid: Cell already contains a snake or ladder");
            return;
        }

        ladderPositionMap.put(start, end);
    }

    public int movePlayer(int diceRoll, int position) {
        int size = getGridSize();

        if (position + diceRoll > size) {
            System.out.println(
                    String.format("Cannot move. You need %s to win, you rolled %s", size - position, diceRoll));
            return position;
        }

        int newPosition = position + diceRoll;

        // check if lands on snake
        Integer destination = snakePositionMap.get(newPosition);

        if (destination != null) {
            newPosition = destination;
        }

        // check if lands on ladder
        destination = ladderPositionMap.get(newPosition);

        if (destination != null) {
            newPosition = destination;
        }

        return newPosition;
    }
}

class SnakeAndLadderGameEngine extends GameEngine {
    private final SnakeAndLadderBoard board;
    private final Dice dice;

    private GameStatus gameStatus;

    private Map<Player, Integer> positionMap = new HashMap<>();

    public void startGame() {
        System.out.println("\n========== Starting Snake & Ladder ==========\n");

        for (Player player : players) {
            positionMap.put(player, 0);
        }

        gameStatus = GameStatus.IN_PROGRESS;
    }

    public SnakeAndLadderGameEngine(SnakeAndLadderBoard board, Dice dice) {
        this.board = board;
        this.dice = dice;
    }

    /**
     * 
     * Check current player turn
     * Player will roll dice and get random no.
     * Verify dice roll
     * Player will move the number of cell
     * If player lands on snake or ladder cell
     * - Move player to the destination
     * 
     * First player to reach last cell will win
     * 
     * 
     */
    public synchronized void makeMove(Player player) {
        if (gameStatus == GameStatus.WIN) {
            System.out.println("Invalid: Game already ended");
            return;
        }

        if (!positionMap.containsKey(player)) {
            System.out.println("Invalid: Player is not part of this game");
            return;
        }

        boolean playerTurn = checkPlayerTurn(player);
        if (!playerTurn) {
            System.out.println(String.format(
                    "Invalid: Not %s turn",
                    player.getName()));
            return;
        }

        int currentPosition = positionMap.get(player);

        System.out.println("----------------------------------------");
        System.out.println(player.getName() + "'s turn");
        System.out.println("Current position: " + currentPosition);

        int diceRoll = dice.roll();

        System.out.println("Dice rolled: " + diceRoll);

        if (diceRoll > 6 || diceRoll < 1) {
            System.out.println("Invalid: Dice roll " + diceRoll);
            return;
        }

        int newPosition = board.movePlayer(
                diceRoll,
                currentPosition);

        positionMap.put(player, newPosition);

        if (newPosition == currentPosition) {
            System.out.println("Player could not move.");
        } else {
            System.out.println(
                    player.getName() +
                            " moved from " +
                            currentPosition +
                            " to " +
                            newPosition);
        }

        if (checkGameStatus(player) == GameStatus.WIN) {
            gameStatus = GameStatus.WIN;

            System.out.println("----------------------------------------");
            System.out.println(
                    "🏆 Result: " + player.getName() + " won the game!");
            System.out.println("----------------------------------------");

            return;
        }

        System.out.println(
                "Current position of " +
                        player.getName() +
                        ": " +
                        newPosition);

        currentTurn = (currentTurn + 1) % players.size();

        System.out.println("----------------------------------------");
    }

    private GameStatus checkGameStatus(Player player) {
        return positionMap.get(player) == board.getGridSize()
                ? GameStatus.WIN
                : GameStatus.IN_PROGRESS;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }
}

class Dice {
    public int roll() {
        return ThreadLocalRandom.current().nextInt(1, 7);
    }
}

class Player {
    private final String name;

    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

public class SnakeLadder {
    public static void main(String[] args) throws InterruptedException {

        // Create board
        SnakeAndLadderBoard board = new SnakeAndLadderBoard();
        board.setGridSize(50);

        // Add Snakes
        board.addSnake(45, 20);
        board.addSnake(38, 15);
        board.addSnake(30, 10);

        // Add Ladders
        board.addLadder(3, 22);
        board.addLadder(8, 35);
        board.addLadder(14, 42);

        // Create dice
        Dice dice = new Dice();

        // Create game engine
        SnakeAndLadderGameEngine game = new SnakeAndLadderGameEngine(board, dice);

        // Create players
        Player player1 = new Player("Akash");
        Player player2 = new Player("Abhishek");

        // Add players
        game.setPlayers(List.of(player1, player2));

        // Start game
        game.startGame();

        // Play the game
        while (game.getGameStatus() != GameStatus.WIN) {
            Thread.sleep(1000);

            game.makeMove(player1);

            Thread.sleep(1000);

            if (game.getGameStatus() != GameStatus.WIN) {
                game.makeMove(player2);
            }
        }
    }
}