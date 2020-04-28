package com.vivebest.banking.core.aml.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
public class JenkinsDeployController {
	@RequestMapping(value="/home",method = RequestMethod.GET)
    public String home() {
        return "Hello Jenkins!";
    }
}
