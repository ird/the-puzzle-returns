package uk.org.ird;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.concurrent.ThreadLocalRandom;
import java.math.BigInteger;


@Controller
public class PuzzleController {

    @RequestMapping("/puzzle")
    public String puzzle(
            @RequestParam(value="token", required=false) BigInteger token,
            @RequestParam(value="answer", required=false) String answer,
            Model model) {

        Puzzle puzzle;
        String message = "";

        if (token == null) {
            // construct new puzzle with random token
            puzzle = new KnightPuzzle(5); // 5 rounds remaining
            token = puzzle.getToken();
        } else {
            // TODO: old token supplied, retrieve puzzle
            if(answer != null) { // an answer has been submitted by the player
                if(puzzle.verify(answer)) {
                    int rounds = puzzle.getRoundsRemaining();
                    if(rounds == 1) {
                        return "winner";
                    }
                    puzzle.setRoundsRemaining(rounds - 1);
                    message = "Next round!";

                } else {
                    message = "Bzzt. Go again";
                    puzzle.setRoundsRemaining(5);

                }
            }
        }

        // TODO: generate new puzzle
        puzzle.generate();

        model.addAttribute("puzzle", puzzle.toString());
        model.addAttribute("timer", puzzle.getTimeRemaining(token));
        model.addAttribute("message", message);
        model.addAttribute("token", token);
        // model.addAttribute("answer", answer);
        return "puzzle";
    }
}
