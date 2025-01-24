/*


    Great Gilbert Soco

    Content:

    ReversiGameFX is responsible for the mechanics and visualization behind my
    Reversi Game based on JavaFX. This iteration of my Reversi game has better
    aesthetic and overall gameplay compared to my previous draft version.

    I hope you enjoy!



 */

package course.finalreversi;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.List;

public class ReversiGameFX extends Application {

    private static final int SIZE = 8;
    private static final double CELL_SIZE = 60.0;
    private static final char BLACK = '⚫';
    private static final char WHITE = '⚪';

    private char[][] board = new char[SIZE][SIZE];
    private char currentPlayer = BLACK;
    private SimpleBot bot = null;

    private GridPane boardGrid;
    private Label scoreLabel;
    private Label currentPlayerLabel; // New label to display the current player

    private VBox root;
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        /*

            This method is responsible for displaying the initial menu scene, composing of five nodes:

            -   The Title
            -   Human vs Human button
            -   Human vs DumbBot button
            -   Human vs MapleBot button
            -   Help

         */
        this.primaryStage = primaryStage;

        // layout setup
        VBox menuRoot = new VBox(10);
        menuRoot.setAlignment(Pos.CENTER);

        // NODES:
        Label titleLabel = new Label("Reversi Game");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Button humanVsHumanButton = new Button("Human vs Human");
        Button humanVsDumbBotButton = new Button("Human vs DumbBot");
        Button humanVsMapleBotButton = new Button("Human vs MapleBot");

        Button helpButton = new Button("Help");
        helpButton.setOnAction(e -> showHelp());

        humanVsHumanButton.setOnAction(e -> startGame(GameMode.HUMAN_VS_HUMAN));
        humanVsDumbBotButton.setOnAction(e -> startGame(GameMode.HUMAN_VS_DUMB_BOT));
        humanVsMapleBotButton.setOnAction(e -> startGame(GameMode.HUMAN_VS_MAPLE_BOT));

        menuRoot.getChildren().addAll(titleLabel, humanVsHumanButton, humanVsDumbBotButton, humanVsMapleBotButton, helpButton);

        //background color
        Color shogiWood = Color.rgb(222, 184, 135);
        menuRoot.setBackground(Background.fill(shogiWood));

        //stage setup
        Scene menuScene = new Scene(menuRoot, 600, 400);
        primaryStage.setTitle("Reversi Game");
        primaryStage.setScene(menuScene);
        primaryStage.show();
    }

    private void startGame(GameMode gameMode) {

        /*

            This method sets up the game board and UI for the selected game mode. Depending on the game mode chosen,
            if any of the bot modes are chosen, then the respective bot mode is applied.

            This method composes of four nodes:
            -   scoreLabel (shows the current score)
            -   currentPlayerLabel  (shows the current player)
            -   boardGrid
            -   Controls

            Controls composes of the following:
            -   dumbBotButton
            -   mapleBotButton
            -   resetButton
            -   helpButton

            Note that if during a game, if the dumbBotButton or mapleBotButton is pressed, the entire game is resetted.
            Behavior is explained in later parts of the documentation.
         */

        // initialize game board and sets the current player
        Reversi.initializeBoard(board);
        currentPlayer = BLACK;

        // scene layout setup
        root = new VBox(10);
        root.setAlignment(Pos.CENTER);

        // NODES:

        // board setup
        boardGrid = new GridPane();
        boardGrid.setAlignment(Pos.CENTER);
        updateBoard();

        scoreLabel = new Label("Black: 0 | White: 0");
        scoreLabel.setStyle("-fx-font-size: 16px;");

        currentPlayerLabel = new Label("Current Player: Black");
        currentPlayerLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        HBox controls = new HBox(10);
        controls.setAlignment(Pos.CENTER);

        ToggleButton dumbBotButton = new ToggleButton("Dumb Bot (Easy)");
        ToggleButton mapleBotButton = new ToggleButton("Maple Bot (Medium)");
        Button resetButton = new Button("Reset Game");

        Button helpButton = new Button("Help");
        helpButton.setOnAction(e -> showHelp());

        resetButton.setOnAction(e -> resetGame());
        dumbBotButton.setOnAction(e -> toggleDumbBotMode(dumbBotButton.isSelected()));
        mapleBotButton.setOnAction(e -> toggleMapleBotMode(mapleBotButton.isSelected()));

        controls.getChildren().addAll(dumbBotButton, mapleBotButton, resetButton, helpButton);

        root.getChildren().addAll(scoreLabel, currentPlayerLabel, boardGrid, controls);

        // display the game scene
        Scene gameScene = new Scene(root, 600, 700);
        primaryStage.setScene(gameScene);
        primaryStage.show();

        // set the bot mode based on the selected game mode
        if (gameMode == GameMode.HUMAN_VS_DUMB_BOT) {
            bot = new DumbBot();
        } else if (gameMode == GameMode.HUMAN_VS_MAPLE_BOT) {
            bot = new MapleBot();
        } else {
            bot = null;  // bot is set to null in the case for human vs human mode.
        }
    }

    private void showHelp() {
        /*
            This method displays a help dialog explaining the game modes and rules. It also provides a link to a
            YouTube tutorial to help people learn Reversi.
         */
        String helpMessage = "REVERSI GAME HELP:\n\n" +
                "1. ||Human v. Human||: Two human players take turns placing their pieces (⚫ or ⚪) on the board.\n" +
                "2. ||Human v. DumbBot||: A human player competes against a bot that makes random moves (EASY).\n" +
                "3. ||Human v. MapleBot||: A human player competes against with a basic strategy (MEDIUM).\n\n" +
                "GAMEPLAY RULES:\n" +
                "- The objective is to have the most pieces of your color on the board at the end of the game.\n" +
                "- Players take turns placing their pieces on the board, flipping opponent's pieces.\n" +
                "- A valid move must " +
                "surround one or more of the opponent's pieces with the player's own pieces.\n" +
                "If you never played Reversi before, please check this video out:\n https://www.youtube.com/watch?v=4XdyAZhzJW8";

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Help");
        alert.setHeaderText("How to Play Reversi");
        alert.setContentText(helpMessage);
        alert.showAndWait();
    }

    private void updateBoard() {
        /*

            This method updates the visualization of the Board:
            -   showing pieces
            -   valid moves
            -   click event handlers (when a piece is being put down)

         */


        boardGrid.getChildren().clear();        // clear a previous visualization of the board

        // creates a new board visualization
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                Rectangle cell = new Rectangle(CELL_SIZE, CELL_SIZE);
                cell.setFill(Color.SPRINGGREEN);
                cell.setStroke(Color.BLACK);

                if (board[row][col] == BLACK || board[row][col] == WHITE) {
                    cell.setFill(createPieceFill(Color.SPRINGGREEN, board[row][col] == BLACK ? Color.BLACK : Color.WHITE));
                }

                // place a Pale Violet Red circle to indicate valid moves
                String move = String.format("%c%d", 'A' + row, col + 1);
                if (Reversi.getValidMoves(board, currentPlayer).contains(move)) {
                    cell.setFill(createPieceFill(Color.SPRINGGREEN, Color.PALEVIOLETRED));
                }

                final int r = row, c = col;
                cell.setOnMouseClicked(e -> handleCellClick(r, c));   // when a player tries to put a valid piece

                boardGrid.add(cell, col, row);
            }
        }
    }

    private RadialGradient createPieceFill(Color baseColor, Color pieceColor) {
        /*

            This method is responsible for creating the illusion of putting a piece down. Instead of actually stacking
            a circle onto a rectangle, a whole unit with the designated color (Black or White) inside a green square
            is created.
         */
        return new RadialGradient(
                            //      Parameters explained:
                0,          //      focus angle
                0.1,        //      focus
                0.5,        //      center x
                0.5,        //      center y
                0.9,        //      radius
                true,       //      is true when proportional to the whole shape. False when absolute measurement is needed
                javafx.scene.paint.CycleMethod.NO_CYCLE,                // no reflection
                new Stop(0, pieceColor),
                new Stop(0.4, pieceColor),      // from 0% to 40% of the shape, use white/black
                new Stop(0.5, baseColor),       // from 40% to 50%, transition from white/black to the green board colro
                new Stop(1, baseColor)          // from 50% to 100%, use green board color
        );
    }

    private void handleCellClick(int row, int col) {
        /*

            This method is responsible for the following:
            -   Processesing cell clicks
            -   Validating moves
            -   Updating the board
            -   Handling game turns.
         */

        // gets valid moves and formats them by letter-number
        List<String> validMoves = Reversi.getValidMoves(board, currentPlayer);
        String move = String.format("%c%d", 'A' + row, col + 1);

        // checks if a move is not valid. In such case, display analert.
        if (!validMoves.contains(move)) {
            showAlert("Invalid Move", "This move is not valid. Try again.");
            return;
        }


        // game turn logic
        Reversi.makeMove(board, row, col, currentPlayer);
        updateScores();             //update score with every turn
        switchPlayer();
        updateBoard();              // update board with every turn

        if (isGameOver()) {
            endGame();
        } else if (bot != null && currentPlayer == WHITE) {
            handleBotMove();
        } else {
            checkForSkipTurn(); // check if the next player has valid moves
        }
    }

    private void checkForSkipTurn() {
        /*

            This method skips a player's turn if no valid moves are available, switching to the other player.
         */
        List<String> validMoves = Reversi.getValidMoves(board, currentPlayer);
        if (validMoves.isEmpty()) {
            showAlert("Skip Turn", currentPlayer + " has no valid moves. Turn skipped.");
            switchPlayer();
            updateBoard();
            checkForSkipTurn(); // check again for the next player
        }
    }

    private boolean isGameOver() {
        /*

            This method determines if the game has ended by checking valid moves and board state.
         */
        List<String> blackMoves = Reversi.getValidMoves(board, BLACK);
        List<String> whiteMoves = Reversi.getValidMoves(board, WHITE);

        // check if there are no valid moves for both players
        boolean noValidMoves = blackMoves.isEmpty() && whiteMoves.isEmpty();

        // check if there are no empty cells left on the board
        boolean noEmptySpaces = true;
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (board[row][col] == Reversi.EMPTY) {
                    noEmptySpaces = false;
                    break;
                }
            }
            if (!noEmptySpaces) break;
        }

        return noValidMoves || noEmptySpaces;
    }

    private void switchPlayer() {
        /*

            This method switches to the other player and updates the game visualization (changes the currentPlayer label)
         */
        currentPlayer = (currentPlayer == BLACK) ? WHITE : BLACK;
        currentPlayerLabel.setText("Current Player: " + (currentPlayer == BLACK ? "Black" : "White"));
    }

    private void updateScores() {
        /*

            This method calculates and updates the current scores for both players by counting the amount of cells
            is occupied by each color
         */
        int blackScore = 0, whiteScore = 0;
        for (char[] row : board) {
            for (char cell : row) {
                if (cell == BLACK) blackScore++;
                else if (cell == WHITE) whiteScore++;
            }
        }

        // update the score after counting
        scoreLabel.setText(String.format("Black: %d | White: %d", blackScore, whiteScore));
    }

    private void resetGame() {
        /*

            This method resets the game state, including the board, scores, and player turn.
         */
        System.out.println("Resetting game...");
        Reversi.initializeBoard(board);
        currentPlayer = BLACK;
        updateBoard();
        updateScores();
        currentPlayerLabel.setText("Current Player: Black");
        System.out.println("Game reset successfully.");
    }

    private void endGame() {
        /*

            This method ends the game, declares the winner via an alert message, saves move history into a txt file,
            and resets the game.
         */
        String winner = Reversi.determineWinner(board);
        Reversi.saveMovesToFile();

        showAlert("Game Over", "The game has ended! " + winner + " wins. Move history has been saved to 'moves_history.txt'.");
        resetGame();
    }

    private void handleBotMove() {
        /*
            Thie method executes the bot's move with a delay, updating the board and switching back to the player.
         */
        List<String> validMoves = Reversi.getValidMoves(board, WHITE); // note that since the player always goes first, the bot is always white
        if (validMoves.isEmpty()) {
            switchPlayer();
            return;
        }

        // add a delay before the bot makes its move. I added this because when I had people test the game,
        // a regular comment was that the bot was putting pieces too fast, which often lead to confusing
        // the human player.
        PauseTransition pause = new PauseTransition(Duration.seconds(4));       //bot does not place a piece until after 4 seconds
        pause.setOnFinished(event -> {
            int[] botMove = bot.getBotMove(board, WHITE);
            if (botMove != null) {
                Reversi.makeMove(board, botMove[0], botMove[1], WHITE);
                updateScores();
                switchPlayer();
                updateBoard();
            }
        });
        pause.play();
    }

    private void toggleDumbBotMode(boolean isBotMode) {
        /*
            This method toggles Dumb Bot mode and resets the game.
         */
        bot = isBotMode ? new DumbBot() : null;     // if true, a new instance of DumbBot is created.
        resetGame();                                // resets Game
    }

    private void toggleMapleBotMode(boolean isBotMode) {
        /*
            This method toggles Maple Bot mode and resets the game.
         */
        bot = isBotMode ? new MapleBot() : null;    // if true, a new instance of MapleBot is created
        resetGame();                                // resets Game
    }

    private void showAlert(String title, String message) {
        /*

            This method displays an informational alert with a specified message.
         */
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    private enum GameMode {
        // This enum defines constants that represents the three game modes.
        HUMAN_VS_HUMAN,
        HUMAN_VS_DUMB_BOT,
        HUMAN_VS_MAPLE_BOT
    }
}
