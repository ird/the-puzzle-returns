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
            switch (puzzle.verify(answer, token)){
                case ALL_DONE:
                    return "winner";
                case LAST_ANSWER_RIGHT:
                    message = "Good work, next round";
                    break;
                case LAST_ANSWER_WRONG:
                    message = "Bzzt. Go again";
            }
        }

        if(token == null) { // first time player, generate new random token
            token = ThreadLocalRandom.current().nextInt();
        }

        model.addAttribute("puzzle", puzzle.generate(token));
        model.addAttribute("timer", puzzle.getTimeRemaining(token));
        model.addAttribute("message", message);
        model.addAttribute("token", token);
        model.addAttribute("answer", answer);

        return "puzzle";
    }
}
