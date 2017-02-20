package com.github.akranga;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {

    @Autowired
    private FactGenerator factGenerator;

    @RequestMapping("/")
    @ResponseBody
    public String gimmeFact() {
        return factGenerator.gimme() + "\n";
    }
}
