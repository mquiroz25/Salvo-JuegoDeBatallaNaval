package com.codeoftheweb.salvo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RouteController {

    @RequestMapping(value = "/")
    public String redictectPage(){
        return "redirect:/web/games.html";
    }
}