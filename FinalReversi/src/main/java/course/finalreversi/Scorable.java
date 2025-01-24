package course.finalreversi;

/*


    Great Gilbert Soco
    2600248450

    Content:

    Scoring is an interface that has a contract to calculate and display the score.


 */
public interface Scorable {

    //calculates the current score for a player.
    int calculateScore(char[][] board, char player);

    //displays the current score for both players.
    void displayScore(char[][] board);
}
