package com.example.swap_face_be.service;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.Map;

public interface CustomTaskService {

    Object createCustomTask(String taskType, Map<String, Object> paramsMap, String creator) throws JsonProcessingException;

    Object updateCustomTask(long taskId, Map<String, Object> paramsMap, Map<String, Object> resultMap, int status, String modifier) throws Exception;

    Object taskAll();

    Object selectTask(Integer taskId, Integer status, Integer limit);
}
