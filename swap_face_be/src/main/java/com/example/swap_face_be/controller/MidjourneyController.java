package com.example.swap_face_be.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/midJourney")
@Slf4j
public class MidjourneyController {

    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public Object test(@RequestBody Object req) throws JsonProcessingException {
        Map<String, String> result = new HashMap<>();
        result.put("status", "ok");
        log.info(new ObjectMapper().writeValueAsString(req));
        return result;
    }
}
