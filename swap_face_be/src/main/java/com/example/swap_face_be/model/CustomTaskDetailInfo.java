package com.example.swap_face_be.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
public class CustomTaskDetailInfo {
    private long id;
    private String taskType;
    private String taskInfo;
    private int status;
    private String result;
    private LocalDateTime ctime;
    private String creator;
    private LocalDateTime mtime;
    private String modifier;

    public Map<String, Object> getTaskInfoMap() throws JsonProcessingException {
        Map<String, Object> taskInfoMap = new HashMap<>();
        if (taskInfo != null) {
            taskInfoMap.putAll(new ObjectMapper().readValue(taskInfo, new TypeReference<Map<String, Object>>() {}));
        }
        return taskInfoMap;
    }

    public Map<String, Object> getTaskResultMap() throws JsonProcessingException {
        Map<String, Object> taskResultMap = new HashMap<>();
        if (result != null) {
            taskResultMap.putAll(new ObjectMapper().readValue(result, new TypeReference<Map<String, Object>>() {}));
        }
        return taskResultMap;
    }
}
