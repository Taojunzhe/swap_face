package com.example.swap_face_be.model.custom_task;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
public class SwapFaceTask extends CustomTask{
    private String sourceImagePath;
    private String targetImagePath;

    @Override
    public Map<String, Object> getParamsMap() {
        System.out.println(sourceImagePath);
        System.out.println(targetImagePath);
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("sourceImagePath", sourceImagePath);
        paramsMap.put("targetImagePath", targetImagePath);
        return paramsMap;
    }
}
