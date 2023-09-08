package com.example.swap_face_be.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SwapFaceTaskTopicBaseImage {
    private int id;
    private int topicId;
    private String imagePath;
    private LocalDateTime ctime;
    private String creator;
    private LocalDateTime mtime;
    private String modifier;
}
