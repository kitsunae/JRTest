package net.example.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by lashi on 24.02.2017.
 */
@Controller
public class IndexController {

    @RequestMapping("/")
    public String getIndexPage(){
        return "index";
    }
}
