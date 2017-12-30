package uk.org.ird;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.concurrent.ThreadLocalRandom;


@Controller
public class PuzzleController {

    @RequestMapping("/puzzle")
    public String puzzle(
            @RequestParam(value="token", required=false) Integer token,
            @RequestParam(value="answer", required=false) String answer,
            Model model) {

        Puzzle puzzle = new KnightPuzzle();
        String message = "";

        if(answer != null) { // an answer has been submitted by the player
            if(puzzle.verify(answer, token)) {
                return "winner";
            }
            message = "Bzzzt. Nope (or too slow!)";
        }

        if(token == null) { // first time player, generate new random token
            token = ThreadLocalRandom.current().nextInt();
        }

        puzzle.generate(token);

        model.addAttribute("puzzle", puzzle.toString());
        model.addAttribute("timer", puzzle.getTimeRemaining());
        model.addAttribute("message", message);
        model.addAttribute("token", token);
        model.addAttribute("answer", answer);

        return "puzzle";
    }
}
