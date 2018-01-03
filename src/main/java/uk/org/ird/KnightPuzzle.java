package uk.org.ird;

import java.math.BigInteger;
import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.data.annotation.Id;

/**
 * KnightPuzzle
 * From the starting position of the placed Knight, find the word in a chessboard
 * sized grid that can be made from legal Knight moves.
 *
 * Stores :-
 *  | Token (int) | Time started in ms (string)| Rounds left (int) |
 *  one row per token. Each time a new puzzle is generated that token's row is replaced
 */
public class KnightPuzzle implements Puzzle {
    @Id
    private final BigInteger token;
    private int roundsRemaining = 5;
    private final long timeAllowedPerRound[] = {0, 2, 4, 8, 16, 32}; // eg 16s allowed when there are 4 rounds left
    private Character[][] puzzle;
    private String startTime;
    private String answer;

    public KnightPuzzle(int roundsRemaining) {
        token = new BigInteger(16, ThreadLocalRandom.current());
        this.roundsRemaining = roundsRemaining; // number of times the puzzle must be solved to pass
        puzzle = new Character[8][8];
        startTime = null;
        answer = null;
    }

    /**
     * Generates a new grid for this user (token) and store the answer and time started
     */
    @Override
    public void generate() {
        // TODO
        startTime = Instant.now().toString();
        answer = "WONGA";

    }
    /**
     * Returns a HTML representation of the current puzzle
     */
    @Override
    public String toString() {
        String s =
            "A <b>N</b> L W O R E A <br /> C H R O L O O Q <br/> Q W E R L K J H" +
            "<br /> D E X A G A B X <br/> S D X C R R W X <br/>  K M I U Q C A X" +
            "<br /> A F V O U A C D <br/> E R T B O I S S <br/>"; // Big-TODO
        return s;
    }

    /**
     * Check the answer against the generated puzzle.
     * @param answer the player supplied answer string
     * @return true or false
     */
    @Override
    public boolean verify(String answer) {
        if(answer == null)
            return false;
        if(answer.equals(this.answer))
            return true;
        return false;
    }

    @Override
    public final BigInteger getToken() { return token; }

    @Override
    public final boolean inTime() {
        if(startTime == null)
            return false; //puzzle hasn't been generated yet
        long timeAllowed = getTimeAllowed();
        Instant elapseTime = Instant.parse(startTime).plusSeconds(timeAllowed);
        switch (Instant.now().compareTo(elapseTime)) {
            case -1:
            case 0:
                return true;
            case 1:
                break;
        }
        return false;
    }

    @Override
    public final Long getTimeAllowed() {
        if(roundsRemaining < timeAllowedPerRound.length)
            return timeAllowedPerRound[roundsRemaining];
        return 0L;
    }

    @Override
    public final int decreaseAndGetRoundsRemaining() {
        return --roundsRemaining;
    }

    @Override
    public void setRoundsRemaining(int rounds) { roundsRemaining = rounds; }
}
