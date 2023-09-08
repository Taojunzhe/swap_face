package com.example.swap_face_be.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.swap_face_be.dao.TaskProcessorMapper;
import com.example.swap_face_be.model.TaskProcessor;
import com.example.swap_face_be.service.TaskProcessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TaskProcessorServiceImpl implements TaskProcessorService {

    @Autowired
    TaskProcessorMapper taskProcessorMapper;

    @Override
    public Object createTaskProcessor(String code) {
        QueryWrapper<TaskProcessor> wrapper = new QueryWrapper<>();
        wrapper.eq("code", code);
        TaskProcessor taskProcessor = taskProcessorMapper.selectOne(wrapper);
        if (taskProcessor == null) {
            TaskProcessor newTaskProcessor = new TaskProcessor();
            newTaskProcessor.setCode(code);
            taskProcessorMapper.insert(newTaskProcessor);
            return newTaskProcessor;
        } else {
            return taskProcessor;
        }
    }

    @Override
    public void taskCreatorHeartBeat(String code) {
        QueryWrapper<TaskProcessor> wrapper = new QueryWrapper<>();
        wrapper.eq("code", code);
        TaskProcessor taskProcessor = taskProcessorMapper.selectOne(wrapper);
        taskProcessor.setStatus(3);
        taskProcessor.setMtime(LocalDateTime.now());
        taskProcessorMapper.updateById(taskProcessor);
    }

    @Override
    public Object accessTaskProcessorCnt() {
        QueryWrapper<TaskProcessor> wrapper = new QueryWrapper<>();
        wrapper.eq("status", 3);
        List<TaskProcessor> taskProcessors = taskProcessorMapper.selectList(wrapper);

        Map<String, Object> resp = new HashMap<>();
        resp.put("cnt", taskProcessors.size());
        return resp;
    }
}
