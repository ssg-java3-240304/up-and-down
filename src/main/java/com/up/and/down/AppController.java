package com.up.and.down;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AppController {
    @Value("${app.version}")
    private String appVersion;

    @GetMapping("/")
    @ResponseBody
    public String index() {
        return "Welcome to Up Down : version " + appVersion;
    }
}



