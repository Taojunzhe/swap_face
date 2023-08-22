<template>
  <el-upload
    ref="upload"
    class="upload-demo"
    action="http://81.68.187.103/api/v1/file/upload"
    :limit="1"
    :on-exceed="handleExceed"
    :auto-upload="false"
    :on-success="successUpload"
  >
    <template #trigger>
      <el-button type="primary">select file</el-button>
    </template>
    <el-button class="ml-3" type="success" @click="submitUpload">
      upload to server
    </el-button>
    <template #tip>
      <div class="el-upload__tip text-red">
        limit 1 file, new file will cover the old file
      </div>
    </template>
  </el-upload>
</template>
  
<script setup lang="ts">
import { ref } from "vue";
import { genFileId } from "element-plus";
import type { UploadInstance, UploadProps, UploadRawFile } from "element-plus";
import axios from "axios";

const upload = ref<UploadInstance>();

const handleExceed: UploadProps["onExceed"] = (files) => {
  upload.value!.clearFiles();
  const file = files[0] as UploadRawFile;
  file.uid = genFileId();
  upload.value!.handleStart(file);
};

const submitUpload = () => {
  upload.value!.submit();
};

const successUpload = (response: any, uploadFile: UploadRawFile) => {
  console.log(uploadFile.name);
  axios
    .post("http://81.68.187.103/api/v1/custom/task/create", {
      type: "swap_face",
      sourceImagePath: "/root/taojunzhe/fs/" + uploadFile.name,
      topic: "xiaochou",
    })
    .then((res) => {
      console.log(res);
    });
};
</script>