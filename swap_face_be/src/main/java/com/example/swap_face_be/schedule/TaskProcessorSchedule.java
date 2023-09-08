package com.example.swap_face_be.schedule;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.swap_face_be.dao.TaskProcessorMapper;
import com.example.swap_face_be.model.TaskProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
@Slf4j
public class TaskProcessorSchedule {

    @Autowired
    TaskProcessorMapper taskProcessorMapper;

    private long EXCEED_HEART_BEAT_TIME = 300L;

//    @Scheduled(cron = "0/60 * * * * ?")
    public void taskProcessorHeartBeat() {
        QueryWrapper<TaskProcessor> wrapper = new QueryWrapper<>();
        wrapper.eq("status", 3);
        List<TaskProcessor> processorList = taskProcessorMapper.selectList(wrapper);
        log.info("当前活跃节点数为:" + processorList.size());
        for (TaskProcessor processor: processorList) {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime lastHeartBeat = processor.getMtime();
            log.info(String.valueOf(lastHeartBeat.until(now, ChronoUnit.SECONDS)));
            if (lastHeartBeat.until(now, ChronoUnit.SECONDS) > EXCEED_HEART_BEAT_TIME) {
                log.info(String.format("processor节点:%s,停止运行", processor.getId()));
                processor.setStatus(4);
                taskProcessorMapper.updateById(processor);
            } else {
                log.info(String.format("processor节点:%s,正常运行", processor.getId()));
            }
        }
    }

}
