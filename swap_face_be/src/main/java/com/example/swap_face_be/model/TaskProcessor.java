package com.example.swap_face_be.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskProcessor {
    private long id;
    private String code;
    private int status;
    private LocalDateTime ctime;
    private LocalDateTime mtime;
}
