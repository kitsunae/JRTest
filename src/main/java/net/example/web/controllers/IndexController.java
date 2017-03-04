package net.example.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by lashi on 24.02.2017.
 */
@Controller
public class IndexController {

    @RequestMapping("/")
    public String getIndexPage(){
        return "index";
    }

    @RequestMapping("/error")
    public String getErrorPage(HttpServletResponse response){
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        return "error-page";
    }
}
