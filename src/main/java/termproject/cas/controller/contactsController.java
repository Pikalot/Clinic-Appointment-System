package termproject.cas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class contactsController {

    @GetMapping("/contacts")
    public String contacts() {
        return "contacts";
    }
}
