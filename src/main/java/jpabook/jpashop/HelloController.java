package jpabook.jpashop;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {
    @GetMapping("welcome")
    public String welcome(Model model) {
        model.addAttribute("name", "입력값");
        return "welcome";
    }
}
