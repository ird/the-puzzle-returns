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
    private PuzzleRepository puzzleRepository;
    public final int MAX_PUZZLES = 100;

    @RequestMapping("/puzzle")
    public String puzzle(
            @RequestParam(value="token", required=false) BigInteger token,
            @RequestParam(value="answer", required=false) String answer,
            Model model) {

        //TODO: If MAX_PUZZLES is reached, clean up all puzzles and reset progress

        Puzzle puzzle = null;
        String message = "";
        if (token == null) {
            puzzle = new KnightPuzzle(5); // 5 rounds remaining
            token = puzzle.getToken();
        } else {
            // old token supplied, retrieve puzzle instance (progress)
            for(Puzzle p : puzzleRepository.findAll()) {
                if(token == p.getToken()){
                    puzzle = p;
                    break;
                }
            }
            // If token isn't found, create a new puzzle and token
            if(puzzle == null){
                puzzle = new KnightPuzzle(5);
                token = puzzle.getToken();
            }
            if(answer != null) { // an answer has been submitted by the player
                if(puzzle.verify(answer)) {
                    if(puzzle.decreaseAndGetRoundsRemaining() == 0)
                        return "winner";
                    message = "Next round!";
                } else {
                    message = "Bzzt. Go again";
                }
            }
        }

        puzzle.generate();

        model.addAttribute("puzzle", puzzle.toString());
        model.addAttribute("timer", puzzle.getTimeRemaining(token));
        model.addAttribute("message", message);
        model.addAttribute("token", token);

        return "puzzle";
    }
}
