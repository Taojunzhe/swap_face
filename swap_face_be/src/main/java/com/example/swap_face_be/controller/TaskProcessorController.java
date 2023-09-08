package com.example.swap_face_be.controller;


import com.example.swap_face_be.service.TaskProcessorService;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
class CreateTaskProcessorReq {
    private String code;
}

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/taskProcessor")
public class TaskProcessorController {

    @Autowired
    TaskProcessorService taskProcessorService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Object createTaskProcessor(@RequestBody CreateTaskProcessorReq request) {
        return taskProcessorService.createTaskProcessor(request.getCode());
    }

    @RequestMapping(value = "/heartBeat", method = RequestMethod.GET)
    public Object taskProcessorHeartBeat(@RequestParam("code") String code) {
        taskProcessorService.taskCreatorHeartBeat(code);

        Map<String, Object> resp = new HashMap<>();
        resp.put("code", 200);
        return resp;
    }

    @RequestMapping(value = "/access", method = RequestMethod.GET)
    public Object accessTaskProcessorCnt() {
        return taskProcessorService.accessTaskProcessorCnt();
    }

}
