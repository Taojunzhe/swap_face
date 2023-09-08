package com.example.swap_face_be.model.custom_task;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

import java.util.Map;

@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", visible = true)
@JsonSubTypes(value = {
        @JsonSubTypes.Type(value = SwapFaceTask.class, name = "swap_face"),
        @JsonSubTypes.Type(value = MidJourneyTask.class, name = "mid_journey")
})
public abstract class CustomTask {
    private String type;

    public Map<String, Object> getParamsMap() {
        return null;
    }

    public void checkParams() throws Exception { }

}