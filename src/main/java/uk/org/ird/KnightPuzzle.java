package uk.org.ird;

/**
 * KnightPuzzle
 * From the starting position of the placed Knight, find the word in a chessboard
 * sized grid that can be made from legal Knight movements.
 *
 * Stores :-
 *  all tokens in use and the last successful round (1-5)
 *  all generated puzzles (+ token that initiated it) and expiry times
 */
public class KnightPuzzle implements Puzzle {
    /**
     * Generates a new grid which can be fetched by toString()
     * Stores the current puzzle and time initiated in persistent storage
     * Clean up expired puzzles for any tokens
     * Do nothing if a (non-expired) puzzle exists for the provided token already
     */
    @Override
    public void generate(Integer token) {

    }

    /**
     * @return the current in-play grid in as a HTML String
     */
    @Override
    public String toString() {
        return null;
    }

    /**
     * Check the answer against the generated puzzle. If a wrong answer is supplied,
     * deduct 1s from the time remaining (to prevent brute forcing).
     * @param answer the player supplied answer string
     * @param token the id of the player
     * @return true if correct, false if not.
     */
    @Override
    public boolean verify(String answer, Integer token) {
        return false;
    }

    @Override
    public Integer getTimeRemaining() {
        return 0;
    }
}
