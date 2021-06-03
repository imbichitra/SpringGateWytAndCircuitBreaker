package com.bichi.springcloudgateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GateWayController {
    @GetMapping("/defaultFallback")
    public String defaultMessage(){
        return "There is some error in connecting .Please try again letter.";
    }
}
