package uk.org.ird;

import java.math.BigInteger;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
    private List<Character> puzzle;
    private String startTime;
    private String answer;

    public KnightPuzzle(int roundsRemaining) {
        token = new BigInteger(16, ThreadLocalRandom.current());
        this.roundsRemaining = roundsRemaining; // number of times the puzzle must be solved to pass
        puzzle = null;
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
        answer = "NAZDO"; // TODO

        while(true) {
            puzzle = Arrays.asList(new Character[64]);
            int pos = (int)(Math.random()*63);
            // Randomly select a start point and enter the first letter
            puzzle.set(pos, answer.charAt(0));
            // For each subsequent letter; work out all possible next positions (max 8),
            // select one at random and place a letter
            List<Integer> nextMoves = null;
            for(int i=1; i<answer.length(); i++) {
                nextMoves = possibleKnightPositions(pos, puzzle);
                if(nextMoves.size() == 0)
                    break;
                pos = choose(nextMoves);
                puzzle.set(pos, answer.charAt(i));
            }
            if(nextMoves.size() == 0)
                continue;
            break;
        }

        // fill in the blanks
        Character[] letters = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
            'K', 'L', 'M', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        for(int i=0; i<puzzle.size(); i++)
            if(puzzle.get(i) == null)
                puzzle.set(i, letters[(int)(Math.random()*(letters.length - 1))]);

    }
    private static List<Integer> possibleKnightPositions(int startPos, List<Character> grid) {
        ArrayList<Integer> res = new ArrayList<>();
        List<Integer> relPos = Arrays.asList(-17, -15, -10, -6, 6, 10, 15, 17);
        for(int adjustment : relPos){
            int pos = startPos + adjustment;
            if(pos >= 0 && pos < 64 && grid.get(pos) == null)
                res.add(pos);
        }
        System.out.println("Possible positions from startPos=" + startPos + " : " + res);
        return res;
    }

    private static int choose(List<Integer> L) {
        return L.get((int)(Math.random()*L.size()));
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
