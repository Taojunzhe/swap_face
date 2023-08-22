package com.example.swap_face_be.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileItemIterator;
import org.apache.tomcat.util.http.fileupload.FileItemStream;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.http.fileupload.util.Streams;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/file")
@CrossOrigin(origins = "*")
@Slf4j
public class FileController {

    private String FILE_PATH_PREFIX = "/root/taojunzhe/fs";

    // save path
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public Object uploadFile(HttpServletRequest request) throws IOException {
        Map<String, Object> resp = new HashMap<>();
        // multipart
//        assert ServletFileUpload.isMultipartContent(request);
        ServletFileUpload servletFileUpload = new ServletFileUpload();
        FileItemIterator fileItemIterator = servletFileUpload.getItemIterator(request);
        while (fileItemIterator.hasNext()) {

            FileItemStream item = fileItemIterator.next();
            String fieldName = item.getFieldName();
            String name = item.getName();
            String contentType = item.getContentType();
            log.info(String.format("fieldName: %s, name: %s, contentType: %s", fieldName, name, contentType));
            if (!item.isFormField()) {
                log.info("获取到文件, " + fieldName);
                // 将文件写入磁盘
                String targetFilePath = String.format("%s/%s", FILE_PATH_PREFIX, name);
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(targetFilePath));
                InputStream inputStream = item.openStream();
                byte[] buffer = new byte[4096];
                int l = inputStream.read(buffer);
                while (l != -1) {
                    bufferedOutputStream.write(buffer, 0, l);
                    l = inputStream.read(buffer);
                }
                inputStream.close();
                bufferedOutputStream.close();
                resp.put(fieldName, name);
            }
        }
        // resp
        resp.put("code", 200);
        return resp;
    }

    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public void downloadFile(@RequestParam("filePath") String filePath, HttpServletResponse response) throws IOException {
        InputStream inputStream = new FileInputStream(filePath);
        OutputStream outputStream = response.getOutputStream();
        byte[] buffer = new byte[4096];
        int l = inputStream.read(buffer);
        while (l != -1) {
            outputStream.write(buffer, 0, l);
            l = inputStream.read(buffer);
        }
        inputStream.close();
        outputStream.close();
    }
}
