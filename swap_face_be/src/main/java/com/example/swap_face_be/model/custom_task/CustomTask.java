package com.example.swap_face_be.model.custom_task;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

import java.util.Map;

@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", visible = true)
@JsonSubTypes(@JsonSubTypes.Type(value = SwapFaceTask.class, name = "swap_face"))
public abstract class CustomTask {
    private String type;

    public Map<String, Object> getParamsMap() {
        return null;
    }
}