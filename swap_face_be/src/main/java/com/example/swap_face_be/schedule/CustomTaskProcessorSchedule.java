package com.example.swap_face_be.schedule;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.swap_face_be.dao.CustomTaskDetailInfoMapper;
import com.example.swap_face_be.model.CustomTaskDetailInfo;
import com.example.swap_face_be.util.SpringUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;
import org.apache.http.client.utils.URIBuilder;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
@Slf4j
public class CustomTaskProcessorSchedule {

    @Value("${schedule.swap-face.work-dir}")
    private String WORK_DIR;
    @Value("${schedule.swap-face.script-path}")
    private String SCRIPT_PATH;
    @Value("${schedule.swap-face.remote-host}")
    private String REMOTE_HOST;
    @Value("${schedule.swap-face.enable}")
    private String SWAP_FACE_CRON;

    @Scheduled(cron = "0/10 * * * * ?")
    public void swapFaceProcessor() throws IOException, URISyntaxException {
        CustomTaskDetailInfoMapper customTaskDetailInfoMapper = SpringUtil.getBean(CustomTaskDetailInfoMapper.class);
        assert customTaskDetailInfoMapper != null;
        // 拉新创建的任务
        QueryWrapper<CustomTaskDetailInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1);
        CustomTaskDetailInfo customTaskDetailInfo = customTaskDetailInfoMapper.selectOne(queryWrapper);
        if (customTaskDetailInfo != null) {
            // 先把状态置为2
            customTaskDetailInfo.setStatus(2);
            try {
                customTaskDetailInfoMapper.updateById(customTaskDetailInfo); // todo 并发问题
                Map<String, Object> taskInfoMap = customTaskDetailInfo.getTaskInfoMap();
                // 添加处理器信息
                taskInfoMap.put("processor", "tjz_work_mac");
                customTaskDetailInfo.setTaskInfo(new ObjectMapper().writeValueAsString(taskInfoMap));
                customTaskDetailInfoMapper.updateById(customTaskDetailInfo);
                log.info(String.format("开始处理任务:%s", customTaskDetailInfo.getId()));
                // 下载资源
                String sourceImagePath = String.valueOf(taskInfoMap.get("sourceImagePath"));
                String sourceImageDownloadName = String.format("%s-%s.jpeg", "source", customTaskDetailInfo.getId());
                String sourceDownloadPath = String.format("%s/%s/%s", WORK_DIR, "resource", sourceImageDownloadName);
                downloadFile(sourceImagePath, sourceDownloadPath);
                log.info("下载资源成功");
                String resultImageName = String.format("%s-%s.jpeg", "result", customTaskDetailInfo.getId());
                String resultImagePath = String.format("%s/%s/%s", WORK_DIR, "resource", resultImageName);

                // 随机选取一张图
                File[] files = new File("/Users/tjz/project/swap_face/swap_face_model/小丑套图").listFiles();
                assert files != null;
                File choose = files[(int) (Math.random() * files.length)];

                String cmd = String.format("/usr/local/bin/python3.9 /users/tjz/project/swap_face/swap_face_model/swapFaceTest.py %s %s %s",choose.getAbsolutePath(), sourceDownloadPath, resultImagePath);
                System.out.println(cmd);
                Process process = Runtime.getRuntime().exec(cmd);
                int exitCode = process.waitFor();
                System.out.println("退出码:" + exitCode);
                log.info("执行脚本成功");
                doUploadFile(resultImagePath);
                log.info("上传结果文件成功");
                Map<String, Object> taskResultMap = customTaskDetailInfo.getTaskResultMap();
                taskResultMap.put("resultImageName", resultImageName);
                customTaskDetailInfo.setResult(new ObjectMapper().writeValueAsString(taskResultMap));
                customTaskDetailInfo.setStatus(3);
                customTaskDetailInfoMapper.updateById(customTaskDetailInfo);
                log.info("更新任务元数据成功");
            } catch (Exception e) {
                customTaskDetailInfo.setStatus(4);
                customTaskDetailInfoMapper.updateById(customTaskDetailInfo);
            }
        }
    }

    private void downloadFile(String path, String savePath) throws URISyntaxException, IOException {
        RestTemplate restTemplate = new RestTemplate();
        String url = String.format("%s/api/v1/file/download?filePath=%s", REMOTE_HOST, path);
        ResponseEntity<byte[]> entity = restTemplate.getForEntity(url, byte[].class);
        Files.write(Paths.get(savePath), Objects.requireNonNull(entity.getBody()));
    }

    private void doUploadFile(String localFilePath) {
        System.out.println(localFilePath);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        RestTemplate restTemplate = new RestTemplate();
        String uri = String.format("%s/api/v1/file/upload", REMOTE_HOST);
        MultiValueMap<String, Object> multiValueMap = new LinkedMultiValueMap<>();
        File localFile = new File(localFilePath);
        multiValueMap.add("file", new FileSystemResource(localFile));
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(multiValueMap, headers);
        Object resp = restTemplate.postForEntity(uri, requestEntity, Object.class);
    }


}
