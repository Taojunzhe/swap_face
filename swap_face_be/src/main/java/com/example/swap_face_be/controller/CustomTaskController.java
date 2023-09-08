package com.example.swap_face_be.controller;

import com.example.swap_face_be.model.custom_task.CustomTask;
import com.example.swap_face_be.service.CustomTaskService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
class UpdateCustomTaskReq {
    private long taskId;
    private Map<String, Object> paramsMap;
    private Map<String, Object> resultMap;
    private int status;
    private String modifier;
}

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/custom/task")
public class CustomTaskController {

    @Autowired
    CustomTaskService customTaskService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Object createCustomTask(@RequestBody CustomTask request) throws Exception {
        String taskType = request.getType();
        request.checkParams();
        return customTaskService.createCustomTask(taskType, request.getParamsMap(), "admin");
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Object updateCustomTask(@RequestBody UpdateCustomTaskReq request) throws Exception {
        return customTaskService.updateCustomTask(request.getTaskId(), request.getParamsMap(), request.getResultMap(), request.getStatus(), request.getModifier());
    }

    @RequestMapping(value = "/all" , method = RequestMethod.GET)
    public Object customTaskList() {
        return customTaskService.taskAll();
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public Object taskSelect(@RequestParam(value = "taskId", required = false) Integer taskId,
                             @RequestParam(value = "status", required = false) Integer status,
                             @RequestParam(value = "limit", required = false) Integer limit) {
        return customTaskService.selectTask(taskId, status, limit);
    }

}
