package com.example.swap_face_be.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SwapFaceTaskTopic {
    private int id;
    private String enName;
    private String cnName;
    private LocalDateTime ctime;
    private String creator;
    private LocalDateTime mtime;
    private String modifier;
}
