<template>
  <el-row style="margin-bottom: 10pt" type="flex" align="middle">
    <el-col :span="4" style="color: red;">
      <span>任务类型</span>
    </el-col>
    <el-col :span="8">
      <el-select
        v-model="taskType"
        class="m-2"
        placeholder="选择任务类型"
        size="large"
      >
        <el-option
          v-for="item in taskTypeOptions"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        />
      </el-select>
    </el-col>
  </el-row>
  <el-row style="margin-bottom: 10pt" type="flex" align="middle">
    <el-col :span="4" style="color: red;">
      <span>任务主题</span>
    </el-col>
    <el-col :span="8">
      <el-select
        v-model="taskTopic"
        class="m-2"
        placeholder="选择任务主题"
        size="large"
      >
        <el-option
          v-for="item in taskTopicOptions"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        />
      </el-select>
    </el-col>
  </el-row>
  <el-row>
    <el-upload
      ref="upload"
      action="http://81.68.187.103/api/v1/file/upload"
      :limit="1"
      :on-exceed="handleExceed"
      :auto-upload="false"
      :on-success="successUpload"
      list-type="picture"
    >
      <template #trigger>
        <el-button type="primary" class="ml-3">选择文件</el-button>
      </template>
      <el-button class="ml-3" type="success" @click="submitUpload">
        创建任务
      </el-button>
      <template #tip>
        <div class="el-upload__tip text-red" style="color: red">
          限制提交一个文件
        </div>
      </template>
    </el-upload>
  </el-row>
</template>
  
<script setup lang="ts">
import { ref } from "vue";
import { genFileId } from "element-plus";
import type { UploadInstance, UploadProps, UploadRawFile } from "element-plus";
import axios from "axios";

const props = defineProps({
  closeDialog: {
    type: Function,
    required: true,
  },
});

const upload = ref<UploadInstance>();

const taskTypeOptions = [
  {
    value: "swap_face",
    label: "AI大头贴",
  },
];

const taskTopicOptions = [
  {
    value: "dark_clown",
    label: "小丑",
  },
];

const imageUrl = ref("");
const taskType = ref(taskTypeOptions[0]);
const taskTopic = ref("");



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
      type: taskType.value,
      sourceImagePath:
        "/root/taojunzhe/swap_face/bootstrap/resource/" + uploadFile.name,
      topic: taskTopic.value,
    })
    .then((res) => {
      console.log(typeof props.closeDialog);
      props.closeDialog();
      console.log(res);
    });
};
</script>

<style>
.ml-3 {
  margin-right: 10pt;
}

.text-red {
  color: red;
}
</style>