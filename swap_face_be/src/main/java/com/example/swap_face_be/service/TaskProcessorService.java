package com.example.swap_face_be.service;

public interface TaskProcessorService {
    Object createTaskProcessor(String code);

    void taskCreatorHeartBeat(String code);

    Object accessTaskProcessorCnt();
}
