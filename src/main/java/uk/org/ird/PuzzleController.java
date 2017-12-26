package uk.org.ird;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class PuzzleController {

    @RequestMapping("/puzzle")
    public String puzzle(
        @RequestParam(value="q", required=false) Integer q,
        @RequestParam(value="a1", required=false) Integer a1,
        @RequestParam(value="a2", required=false) Integer a2,
        Model model) {
        model.addAttribute("q", q);
        model.addAttribute("a1", a1);
        model.addAttribute("a2", a2);

        return "puzzle";
    }
}
