package com.example.swap_face_be.model.custom_task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.swap_face_be.dao.SwapFaceTaskTopicBaseImageMapper;
import com.example.swap_face_be.dao.SwapFaceTaskTopicMapper;
import com.example.swap_face_be.model.SwapFaceTaskTopic;
import com.example.swap_face_be.model.SwapFaceTaskTopicBaseImage;
import com.example.swap_face_be.util.SpringUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class SwapFaceTask extends CustomTask {
    private String sourceImagePath;
    private String targetImagePath;
    private String topic;

    @Override
    public Map<String, Object> getParamsMap() {
        System.out.println(sourceImagePath);
        System.out.println(targetImagePath);
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("sourceImagePath", sourceImagePath);
        paramsMap.put("targetImagePath", targetImagePath);
        paramsMap.put("topic", topic);
        return paramsMap;
    }

    @Override
    public void checkParams() throws Exception {
        if (sourceImagePath == null) {
            throw new Exception("不存在的原始图片");
        }
        if (targetImagePath == null && topic == null) {
            throw new Exception("目标和主题必须传入一个");
        }
        if (targetImagePath == null) {
            // 如果传入主题
            SwapFaceTaskTopic swapFaceTaskTopic = checkTopicExists(topic);
            // 随机选择一个图片作为目标图片
            SwapFaceTaskTopicBaseImageMapper swapFaceTaskTopicBaseImageMapper = SpringUtil.getBean(SwapFaceTaskTopicBaseImageMapper.class);
            QueryWrapper<SwapFaceTaskTopicBaseImage> wrapper = new QueryWrapper<>();
            wrapper.eq("topic_id", swapFaceTaskTopic.getId());
            List<SwapFaceTaskTopicBaseImage> images = swapFaceTaskTopicBaseImageMapper.selectList(wrapper);
            SwapFaceTaskTopicBaseImage image = images.get((int) (Math.random() * images.size()));
            targetImagePath = image.getImagePath();
        }
    }

    private SwapFaceTaskTopic checkTopicExists(String topic) throws Exception {
        SwapFaceTaskTopicMapper swapFaceTaskTopicMapper = SpringUtil.getBean(SwapFaceTaskTopicMapper.class);
        QueryWrapper<SwapFaceTaskTopic> wrapper = new QueryWrapper<>();
        wrapper.eq("en_name", topic);
        List<SwapFaceTaskTopic> chooseTopic = swapFaceTaskTopicMapper.selectList(wrapper);
        if (chooseTopic == null || chooseTopic.size() == 0) {
            throw new Exception("主题不存在");
        }
        return chooseTopic.get(0);

    }
}
