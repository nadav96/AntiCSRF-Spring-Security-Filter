package com.csrf.anti.endpoints;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Nadav on 01/04/2017.
 */
@RestController
public class MainController {
    @RequestMapping(value = "/api/hello")
    public String check() {
        return "hello world!!@#";
    }
}
