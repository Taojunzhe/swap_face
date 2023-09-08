package com.example.swap_face_be.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.swap_face_be.dao.CustomTaskDetailInfoMapper;
import com.example.swap_face_be.model.CustomTaskDetailInfo;
import com.example.swap_face_be.service.CustomTaskService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class CustomTaskServiceImpl implements CustomTaskService {

    @Autowired
    CustomTaskDetailInfoMapper customTaskDetailInfoMapper;

    @Override
    public Object createCustomTask(String taskType, Map<String, Object> paramsMap, String creator) throws JsonProcessingException {
        CustomTaskDetailInfo customTaskDetailInfo = new CustomTaskDetailInfo();
        customTaskDetailInfo.setTaskType(taskType);
        log.info(new ObjectMapper().writeValueAsString(paramsMap));
        customTaskDetailInfo.setTaskInfo(new ObjectMapper().writeValueAsString(paramsMap));
        customTaskDetailInfo.setStatus(1); // 新建
        customTaskDetailInfo.setCreator(creator);
        customTaskDetailInfo.setModifier(creator);
        customTaskDetailInfoMapper.insert(customTaskDetailInfo);
        return customTaskDetailInfo;
    }

    @Override
    public Object updateCustomTask(long taskId, Map<String, Object> paramsMap, Map<String, Object> resultMap, int status, String modifier) throws Exception {
        CustomTaskDetailInfo customTaskDetailInfo = customTaskDetailInfoMapper.selectById(taskId);
        if (customTaskDetailInfo == null) {
            throw new Exception("没找到任务");
        }
        if (paramsMap != null) {
            Map<String, Object> paramsMapOld = customTaskDetailInfo.getTaskInfoMap();
            paramsMapOld.putAll(paramsMap);
            customTaskDetailInfo.setTaskInfo(new ObjectMapper().writeValueAsString(paramsMapOld));
        }
        if (resultMap != null) {
            Map<String, Object> resultMapOld = customTaskDetailInfo.getTaskResultMap();
            resultMapOld.putAll(resultMap);
            customTaskDetailInfo.setResult(new ObjectMapper().writeValueAsString(resultMapOld));
        }
        if (status != 0) {
            customTaskDetailInfo.setStatus(status);
        }
        customTaskDetailInfoMapper.updateById(customTaskDetailInfo);
        return customTaskDetailInfo;
    }

    @Override
    public Object taskAll() {
        List<CustomTaskDetailInfo> customTaskDetailInfos = customTaskDetailInfoMapper.selectList(null);
        Collections.reverse(customTaskDetailInfos);
        return customTaskDetailInfos;
    }

    @Override
    public Object selectTask(Integer taskId, Integer status, Integer limit) {
        QueryWrapper<CustomTaskDetailInfo> queryWrapper = new QueryWrapper<>();
        if (taskId != null) {
            queryWrapper.eq("id", taskId);
        }
        if (status != null) {
            queryWrapper.eq("status", status);
        }
        List<CustomTaskDetailInfo> customTaskDetailInfoList = customTaskDetailInfoMapper.selectList(queryWrapper);
        if (limit != null) {
            customTaskDetailInfoList = customTaskDetailInfoList.subList(0, limit);
        }
        return customTaskDetailInfoList;
    }
}
