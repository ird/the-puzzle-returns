package uk.org.ird;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.core.io.ClassPathResource;
import org.springframework.data.annotation.Id;
import java.nio.charset.Charset;
import java.io.BufferedReader;


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
    private final long timeAllowedPerRound[] = {0, 2, 8, 16, 32, 63}; // eg 16s allowed when there are 4 rounds left
    private List<Character> puzzle;
    private List<String> words;
    private String startTime;
    private String answer;
    private int attempts = 0;  // to prevent brute forcing

    public KnightPuzzle() throws IOException{
        token = new BigInteger(16, ThreadLocalRandom.current());
        this.roundsRemaining = 0; // number of times the puzzle must be solved to pass
        puzzle = null;
        startTime = null;
        answer = null;
        Charset cs = Charset.forName("US-ASCII");
        InputStream in = new ClassPathResource("words.txt").getInputStream();
        BufferedReader reader = new BufferedReader(
            new InputStreamReader(in, cs));
        String line;
        words = new LinkedList<>();
        while((line = reader.readLine()) != null) {
            if(line.lastIndexOf('n') == 0)
                words.add(line.trim().toUpperCase());
        }
    }

    /**
     * Generates a new grid for this user (token) and store the answer and time started
     */
    @Override
    public void generate(){
        // TODO
        startTime = Instant.now().toString();

        answer = choose(words);
        System.out.println("New Puzzle at " + startTime + " answer=" + answer);

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

        attempts = 0;

    }
    private static List<Integer> possibleKnightPositions(int startPos, List<Character> grid) {
        ArrayList<Integer> res = new ArrayList<>();
        List<Integer> relPos = Arrays.asList(-17, -15, -10, -6, 6, 10, 15, 17);
        // Use relCol and relRowOffset to ensure that move doesn't wrap round the grid
        List<Integer> relColOffset = Arrays.asList(-1, 1, -2, 2, -2, 2, -1, 1);
        List<Integer> relRowOffset = Arrays.asList(-2, -2, -1, -1, 1, 1, 2, 2);
        int colStart = startPos % 8;
        int rowStart = startPos / 8;
        int i=0;
        for(int adjustment : relPos){
            int pos = startPos + adjustment;
            int colDest = pos % 8;
            int rowDest = pos / 8;
            if(colDest - colStart!= relColOffset.get(i))
                continue; // wrap left or right
            if(rowDest - rowStart != relRowOffset.get(i))
                continue; // wrap up or down
            if(pos >= 0 && pos < 64 && grid.get(pos) == null){
                res.add(pos);
            }
            i++;
        }
        //System.out.println("Possible positions from startPos=" + startPos + " : " + res);
        return res;
    }

    private static <T> T choose(List<T> L) {
        return L.get((int)(Math.random()*L.size()));
    }
    /**
     * Returns a HTML representation of the current puzzle
     */
    @Override
    public String toString() {
        String s = new String();
        int i=0;
        for(Character c : puzzle) {
            if(i % 8 == 0)
                s += "<br/>";
            if(c == 'N'){
                s += "<span style='color:red;'>N</span> ";
            } else {
                s += c + " ";
            }
            i++;
        }
        return s + "<br/>";
    }

    /**
     * Check the answer against the generated puzzle.
     * @param answer the player supplied answer string
     * @return true or false
     */
    @Override
    public boolean verify(String answer) {
        attempts++;
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
    public void setRoundsRemaining(int rounds) {
        if (rounds < 1) {
            roundsRemaining = 1;
        } else if (rounds > 5) {
            roundsRemaining = 5;
        } else {
            roundsRemaining = rounds;
        }
    }

    @Override
    public int getAttempts() {
        return attempts;
    }
}
