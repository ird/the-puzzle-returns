package uk.org.ird;

import java.util.concurrent.ThreadLocalRandom;

/**
 * KnightPuzzle
 * From the starting position of the placed Knight, find the word in a chessboard
 * sized grid that can be made from legal Knight movements.
 *
 * Stores :-
 *  | Token | Time started in ms | Rounds left |
 *  one row per token. Each time a new puzzle is generated that token's row is replaced
 */
public class KnightPuzzle implements Puzzle {
    private final int timeAllowedInSeconds[] = {1, 2, 4, 8, 16}; // eg 16s allowed when there are 4 rounds left
    /**
     * Generates a new grid for this user (token)
     * Stores the puzzle and time initiated in persistent storage
     * Clean up expired puzzles for any tokens
     * Replaces any existing puzzle for this user (ie only 1 puzzle per token at a time)
     * @return a valid HTML string representing the puzzle
     */
    @Override
    public String generate(Integer token) {
        Character[][] puz = new Character[8][8];
        String answer;

        return asHTMLString(puz);
    }
    private void store(Integer token, String answer, int time, int rounds) {}

    private String asHTMLString(Character[][] puzzle) {
        return "";
    }
    /**
     * Check the answer against the generated puzzle.
     * @param answer the player supplied answer string
     * @param token the id of the player
     * @return ALL_DONE, LAST_ANSWER_RIGHT or LAST_ANSWER_WRONG
     */
    @Override
    public Response verify(String answer, Integer token) {
        return Response.LAST_ANSWER_WRONG;
    }

    private String getAnswer(Integer token) {return null;}

    @Override
    public Integer getTimeRemaining(Integer token) {
        return 0;
    }
}
