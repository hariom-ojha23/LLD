package problems;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 
 * Design a Tic-Tac-Toe game
 * 
 * 
 * Functional requirement:
 * 
 * 1. It is board game
 * 2. Board will be a grid
 * 3. At a time two users can play
 * 4. User will choose either X or O to mark
 * 5. User will choose a cell to mark
 * 6. User will win if either horizontal row or verical column or diagonal is
 * filled with same
 * 7. If all the cells are filled and no one has won, then it is a tie
 * 
 * 
 * No-functional Requirements:
 * 
 * 1. Only one user can mark the cell at a time
 * 2. Follow SOLID principle
 * 3. Each cell can be marked only once
 * 4. It should be thread-safe
 * 
 * 
 * Core Classes/Interfaces/Enums:
 * 
 * Board abstract class
 * TicTacToeBoard
 * Player
 * GameEngine abstract class
 * TicTacToeGameEngine
 * 
 */

enum GameState {
    WIN,
    TIE,
    IN_PROGRESS
}

abstract class Board {
    public List<List<String>> grid = new ArrayList<>();
    public int gridSize;

    public void setGrid(int gridSize) {
        this.gridSize = gridSize;
        this.setBoard();
    }

    private void setBoard() {
        for (int i = 0; i < gridSize; i++) {
            grid.add(new ArrayList<>(Collections.nCopies(gridSize, "")));
        }
    }

    public boolean checkInvalidCell(Cell cell) {
        boolean invalidRow = cell.row() < 0 || cell.row() >= gridSize;
        boolean invalidColumn = cell.column() < 0 || cell.column() >= gridSize;

        if (invalidRow || invalidColumn) {
            return true;
        }

        return false;
    }

    public void clearBoard() {
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                grid.get(i).set(j, "");
            }
        }
    }
}

abstract class GameEngine {
    public List<Player> players = new ArrayList<>();
    public int totalPlayers;
    public int currentTurn = 0;

    public void startGame() {
        System.out.println("\nGame started:");
        this.currentTurn = 0;
    }

    abstract public void restartGame();

    public void setPlayers(List<Player> players) {
        try {
            if (players.size() == 0) {
                throw new IllegalArgumentException("Players cannot be 0");
            }
        } catch (Exception e) {
            System.out.println("Invalid: " + e.getMessage());
        }

        this.players = players;
        this.totalPlayers = players.size();
    }

    public boolean checkPlayerTurn(Player player) {
        int playerIndex = players.indexOf(player);

        if (currentTurn != playerIndex) {
            return false;
        }

        return true;
    }

    public void setPlayerTurn() {
        if (currentTurn == totalPlayers - 1) {
            currentTurn = 0;
        } else {
            currentTurn++;
        }
    }
}

record Cell(int row, int column) {
}

class TicTacToeBoard extends Board {
    public TicTacToeBoard(int gridSize) {
        this.setGrid(gridSize);
    }

    public boolean checkCellMarked(Cell cell) {
        if (grid.get(cell.row()).get(cell.column()).isEmpty()) {
            return false;
        }
        return true;
    }

    public void markCell(Player player, String symbol, Cell cell) {
        grid.get(cell.row()).set(cell.column(), symbol);
    }

    public void printGrid() {
        for (List<String> row : grid) {
            System.out.println(String.join(" | ",
                    row.stream()
                            .map(symbol -> symbol == null ? " " : symbol.toString())
                            .toList()));
        }
    }

    public void clear() {
        this.clearBoard();
    }
}

class TicTacToeGameEngine extends GameEngine {
    private final TicTacToeBoard board;
    private int totalTurns = 0;

    public TicTacToeGameEngine(TicTacToeBoard board) {
        this.board = board;
    }

    @Override
    public void restartGame() {
        this.board.clear();
        this.totalTurns = 0;
        this.startGame();
    }

    public boolean checkIncorrectSymbol(Player player, String symbol) {
        if (player.getSymbol().equals(symbol)) {
            return false;
        }

        return true;
    }

    /**
     * Check player turn.
     * Check If cell is valid
     * Check if cell already marked
     * Check if symbol is correct
     * 
     * After mark check:
     * - Row & column of marked cell. If all same. Player wins
     * - If on diagonals, check diagonals. If all same. Player wins
     * - If no one wins, and board is full, match tie.
     * - If no one wins and board is not full. Next player turns
     */
    public synchronized void markCell(Player player, String symbol, Cell cell) {
        boolean isPlayerTurn = checkPlayerTurn(player);
        if (!isPlayerTurn) {
            System.out.println("Invalid: Not your turn.");
            return;
        }

        boolean isInvalidCell = board.checkInvalidCell(cell);
        if (isInvalidCell) {
            System.out.println("Invalid: Cell is out of bounds");
            return;
        }

        boolean isCellMarked = board.checkCellMarked(cell);
        if (isCellMarked) {
            System.out.println("Invalid: Cell is already marked");
            return;
        }

        boolean isIncorrenctSymbol = checkIncorrectSymbol(player, symbol);
        if (isIncorrenctSymbol) {
            System.out.println("Invalid: Player symbol mismatch");
            return;
        }

        // Mark cell and chek game status
        System.out.println(
                String.format("Player %s marked %s at (%s, %s)", player.getName(), symbol, cell.row(), cell.column()));

        board.markCell(player, symbol, cell);

        GameState state = checkGameStatus(board.grid, cell);

        switch (state) {
            case GameState.WIN:
                System.out.println(String.format("\nResult: Player %s won the match", player.getName()) + "\n");
                board.printGrid();
                break;

            case GameState.TIE:
                System.out.println("");
                System.out.println(String.format("Result: Match TIE", player.getName()) + '\n');
                board.printGrid();
                break;

            case GameState.IN_PROGRESS:
                break;

            default:
                System.out.println("Invalid game state");
                break;
        }
    }

    public GameState checkGameStatus(List<List<String>> grid, Cell cell) {
        totalTurns++;

        if (checkIfHorizontalSame(grid, cell.row())) {
            return GameState.WIN;
        }

        if (checkIfVerticalSame(grid, cell.column())) {
            return GameState.WIN;
        }

        if (checkIfDiagonalSame(grid)) {
            return GameState.WIN;
        }

        if (totalTurns == grid.size() * grid.get(0).size()) {
            return GameState.TIE;
        }

        this.setPlayerTurn();
        return GameState.IN_PROGRESS;
    }

    private boolean checkIfHorizontalSame(List<List<String>> grid, int row) {
        String symbol = grid.get(row).get(0);

        if (symbol.isEmpty()) {
            return false;
        }

        for (int i = 1; i < grid.get(row).size(); i++) {
            if (!symbol.equals(grid.get(row).get(i))) {
                return false;
            }
        }

        return true;
    }

    private boolean checkIfVerticalSame(List<List<String>> grid, int column) {
        String symbol = grid.get(0).get(column);

        if (symbol.isEmpty()) {
            return false;
        }

        for (int i = 1; i < grid.size(); i++) {
            if (!symbol.equals(grid.get(i).get(column))) {
                return false;
            }
        }

        return true;
    }

    private boolean checkIfDiagonalSame(List<List<String>> grid) {
        int n = grid.size();

        String symbol = grid.get(0).get(0);

        if (!symbol.isEmpty()) {
            boolean same = true;

            for (int i = 1; i < n; i++) {
                if (!symbol.equals(grid.get(i).get(i))) {
                    same = false;
                }
            }

            if (same == true) {
                return true;
            }
        }

        symbol = grid.get(0).get(n - 1);

        if (!symbol.isEmpty()) {
            boolean same = true;

            for (int i = 1; i < n; i++) {
                if (!symbol.equals(grid.get(i).get(n - 1 - i))) {
                    same = false;
                }
            }

            if (same == true) {
                return true;
            }
        }

        return false;
    }
}

class Player {
    private final String name;
    private final String symbol;

    public Player(String name, String symbol) {
        this.name = name;
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }
}

public class TicTacToe {

    public static void main(String[] args) {

        Player akash = new Player("Akash", "O");
        Player abhishek = new Player("Abhishek", "X");

        TicTacToeBoard board = new TicTacToeBoard(3);
        TicTacToeGameEngine gameEngine = new TicTacToeGameEngine(board);

        gameEngine.setPlayers(List.of(akash, abhishek));

        gameEngine.startGame();        

        // TIE Match
        gameEngine.markCell(akash, "O", new Cell(0, 0));
        gameEngine.markCell(abhishek, "X", new Cell(0, 1));
        gameEngine.markCell(akash, "O", new Cell(0, 2));
        gameEngine.markCell(abhishek, "X", new Cell(1, 1));
        gameEngine.markCell(akash, "O", new Cell(1, 0));
        gameEngine.markCell(abhishek, "X", new Cell(1, 2));
        gameEngine.markCell(akash, "O", new Cell(2, 1));
        gameEngine.markCell(abhishek, "X", new Cell(2, 0));
        gameEngine.markCell(akash, "O", new Cell(2, 2));

        gameEngine.restartGame();

        // Win Match
        gameEngine.markCell(akash, "O", new Cell(0, 0));
        gameEngine.markCell(abhishek, "X", new Cell(1, 1));
        gameEngine.markCell(akash, "O", new Cell(0, 1));
        gameEngine.markCell(abhishek, "X", new Cell(2, 2));
        gameEngine.markCell(akash, "O", new Cell(2, 0));
        gameEngine.markCell(abhishek, "X", new Cell(1, 2));
        gameEngine.markCell(akash, "O", new Cell(2, 1));
        gameEngine.markCell(abhishek, "X", new Cell(1, 0));
    }
}
