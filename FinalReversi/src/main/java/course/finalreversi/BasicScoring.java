package course.finalreversi;/*


    Great Gilbert Soco
    2600248450

    Content:

    BasicScoring is an implementation of the Scorable class, so it defines the calculateScore and displayScore
    used in the main function


 */

public class BasicScoring implements Scorable {
    @Override
    // calculates the score
    public int calculateScore(char[][] board, char player) {
        int score = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == player)  // checks for every instance of player on the board
                {
                    score++;
                }
            }
        }
        return score;
    }

    @Override
    // displays the calculated score
    public void displayScore(char[][] board) {
        int blackScore = calculateScore(board, Reversi.BLACK);
        int whiteScore = calculateScore(board, Reversi.WHITE);

        System.out.println("Current Scores:");
        System.out.println("Black (⚫): " + blackScore);
        System.out.println("White (⚪): " + whiteScore);
    }
}

