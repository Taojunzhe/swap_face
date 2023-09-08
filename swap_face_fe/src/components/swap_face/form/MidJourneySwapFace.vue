<template>
  <el-form :model="form" label-position="left" label-width="60px">
    <el-form-item label="提示词">
      <el-input v-model="form.prompt" />
    </el-form-item>
    <el-form-item label="原始图">
      <el-col :span="10">
        <el-input v-model="form.pictureName" disabled="true"/>
      </el-col>
      <el-col :span="4" style="margin-left: 15px">
        <el-button type="success" @click="dialogVisible1=true;uploadType='pictureName'" :disabled="form.pictureName!=''">上传图片</el-button>
      </el-col>
    </el-form-item>
    <el-form-item label="底图">
      <el-col :span="10">
        <el-input v-model="form.facePictureName" disabled="true"/>
      </el-col>
      <el-col :span="4" style="margin-left: 15px">
        <el-button type="success" @click="dialogVisible1=true;uploadType='facePictureName'" :disabled="form.facePictureName!=''">上传图片</el-button>
      </el-col>
    </el-form-item>
    <!-- <el-form-item label="主题">
      <el-select
        v-model="form.topic"
        placeholder="选择任务主题"
      >
        <el-option
          v-for="item in taskTopicOptions"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        />
      </el-select>
    </el-form-item> -->
    <el-form-item>
        <el-button @click="onSubmit">提交任务</el-button>
    </el-form-item>
  </el-form>
  <el-dialog v-model="dialogVisible1" width="90%" destroy-on-close>
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
        <el-button type="primary">选择图片</el-button>
      </template>
      <el-button type="success" @click="submitUpload" style="margin-left: 10px">
        上传图片
      </el-button>
      <template #tip>
        <div class="el-upload__tip text-red" style="color: red">
          限制提交一个文件
        </div>
      </template>
    </el-upload>
  </el-dialog>
</template>

<script lang="ts" setup>
import { reactive, ref } from "vue";
import { genFileId } from "element-plus";
import type { UploadInstance, UploadProps, UploadRawFile } from "element-plus";
import axios from "axios";

// do not use same name with ref
const form = reactive({
  prompt: "",
  pictureName: "",
  facePictureName: ""
//   topic: ""
});

const dialogVisible1 = ref(false);
const upload = ref<UploadInstance>();
const uploadType = ref('')

const taskTopicOptions = [
  {
    value: "dark_clown",
    label: "黑暗小丑风",
  },
  {
    value: "cert_photo",
    label: "证件照",
  },
  {
    value: "job_photo",
    label: "职业照",
  },
];

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
  if (uploadType.value == 'pictureName') {
    form.pictureName = uploadFile.name
  } else if (uploadType.value == 'facePictureName') {
    form.facePictureName = uploadFile.name
  }
  dialogVisible1.value=false
};

const onSubmit = () => {
  console.log("submit!");
  axios
    .post("http://81.68.187.103/api/v1/custom/task/create", {
      type: "mid_journey",
      prompt: form.prompt,
      pictureUrl: "/root/taojunzhe/swap_face/bootstrap/resource/" + form.pictureName,
      facePictureUrl: "/root/taojunzhe/swap_face/bootstrap/resource/" + form.facePictureName
    //   topic: form.topic,
    })
    .then((res) => {
      console.log(res);
    });
};
</script>