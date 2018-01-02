package uk.org.ird;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import java.math.BigInteger;


@Controller
public class PuzzleController {
    @Autowired
    private KnightPuzzleRepository knightPuzzleRepository;
    public final int MAX_PUZZLES = 100;

    @RequestMapping("/puzzle")
    public String puzzle(
            @RequestParam(value="token", required=false) BigInteger token,
            @RequestParam(value="answer", required=false) String answer,
            Model model) {

        //TODO: If MAX_PUZZLES is reached, clean up all puzzles and reset progress

        KnightPuzzle puzzle = null;
        String message = "";
        if (token == null) {
            puzzle = new KnightPuzzle(5); // 5 rounds to win
            token = puzzle.getToken();
            System.out.println("PuzzleController: No token supplied, creating new Puzzle with token " + token);
        } else {
            //TODO : Reorganise game logic
            puzzle = knightPuzzleRepository.findByToken(token);
            System.out.println("PuzzleController: Token supplied: " + token);
            if(puzzle == null){
                puzzle = new KnightPuzzle(5);
                token = puzzle.getToken();
                System.out.println("PuzzleController: Token not found, creating new Puzzle with token " + token);
            }
            if(answer != null) { // an answer has been submitted by the player
                if(!puzzle.inTime()) {
                    message = "Bzzt. Out of time. Start again";
                    puzzle.setRoundsRemaining(5);
                } else {
                    if (puzzle.verify(answer)) {
                        int roundsRemaining = puzzle.decreaseAndGetRoundsRemaining();
                        knightPuzzleRepository.save(puzzle);
                        if (roundsRemaining == 0)
                            return "winner";
                        message = "Next round! " + roundsRemaining + " left.";
                    } else {
                        message = "Bzzt. Wrong. Start again";
                        puzzle.setRoundsRemaining(5);
                    }
                }
            }
        }

        puzzle.generate();
        knightPuzzleRepository.save(puzzle);

        model.addAttribute("puzzle", puzzle.toString());
        model.addAttribute("timer", puzzle.getTimeAllowed());
        model.addAttribute("message", message);
        model.addAttribute("token", token);

        return "puzzle";
    }
}
