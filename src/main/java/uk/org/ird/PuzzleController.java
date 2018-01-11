package uk.org.ird;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
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
            Model model) throws IOException {

        //TODO: If MAX_PUZZLES is reached, clean up all puzzles and reset progress
        KnightPuzzle kp;
        String message = "";
        if (token == null || (kp = knightPuzzleRepository.findByToken(token)) == null) {
            kp = new KnightPuzzle();
            token = kp.getToken();
            kp.setRoundsRemaining(5); // 5 rounds to win
            kp.generate();
        }
        if(kp.inTime()) {
            if (kp.verify(answer)) {
                int rounds;
                if ((rounds = kp.decreaseAndGetRoundsRemaining()) == 0)
                    return "winner";
                message = "Next round! " + rounds + " left.";
                kp.generate();
            } else {
                if(answer != null)
                    message = "Bzzzt.";
            }
        } else {
            // out of time
            kp.setRoundsRemaining(5);
            kp.generate();
            message = "Too slow";
        }

        //kp.generate();
        knightPuzzleRepository.save(kp);
        model.addAttribute("puzzle", kp.toString());
        model.addAttribute("timer", kp.getTimeAllowed());
        model.addAttribute("message", message);
        model.addAttribute("token", token);

        return "puzzle";
    }
}
