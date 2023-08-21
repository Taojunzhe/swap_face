package com.example.swap_face_be.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HealthzController {

    @RequestMapping(value = "/healthz", method = RequestMethod.GET)
    public Object healthz() {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        return result;
    }
}
