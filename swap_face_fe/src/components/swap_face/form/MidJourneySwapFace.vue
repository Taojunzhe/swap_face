<template>

  <el-form :model="form" label-position="left" label-width="80px">
    <el-form-item label="人脸底图">
      <el-col :span="10">
        <el-input v-model="form.facePictureName" disabled="true"/>
      </el-col>
      <el-col :span="4" style="margin-left: 15px">
        <el-button type="success" @click="dialogVisible1=true;uploadType='facePictureName'" :disabled="form.facePictureName!=''">上传图片</el-button>
      </el-col>
    </el-form-item>
  </el-form>
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
    <el-card v-for="(item, index) in form.conditionList" :key="index" style="margin-bottom: 20px;">
      <template #header>
        <el-row justify="space-between">
          <el-col :span="4">{{ "序号:" + (index + 1) }}</el-col>
          <el-col :span="4">
            <el-button @click="form.conditionList.splice(index, 1)" type="danger">
              删除
            </el-button>
          </el-col>
        </el-row>
      </template>
      <el-row style="margin-bottom: 20px;">
        <el-col :span="4" style="align-self: center;">
          提示词
        </el-col>
        <el-col :span="20" style="align-self: center;">
          <el-input disabled="true" :placeholder="item['prompt']"></el-input>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="4" style="align-self: center;">图片预览</el-col>
        <el-col :span="20" style="align-self: center;">
          <el-image :src="'http://81.68.187.103/resource/' + item['pictureName']" style="width: 200px;height: 200px;"></el-image>
        </el-col>
      </el-row>
    </el-card>
    <el-form inline>
    <el-form-item>
        <el-button @click="onSubmit();$emit('closeDialog')" :disabled="form.conditionList.length == 0">提交任务</el-button>
    </el-form-item>
    <el-form-item>
      <el-button @click="dialogVisible2=true;tempForm.prompt='';tempForm.pictureName=''" :disabled="form.conditionList.length >= 10">新增主题(上限10个)</el-button>
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
  <el-dialog v-model="dialogVisible2" destroy-on-close>
    <el-form-item label="提示词">
      <el-input v-model="tempForm.prompt"></el-input>
    </el-form-item>
    <el-form-item label="联想图">
      <el-col :span="10">
        <el-input v-model="tempForm.pictureName" disabled="true"/>
      </el-col>
      <el-col :span="4" style="margin-left: 15px">
        <el-button type="success" @click="dialogVisible1=true;uploadType='pictureName'">上传图片</el-button>
      </el-col>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="handleAddTask();dialogVisible2=false">确定</el-button>
    </el-form-item>
  </el-dialog>
</template>

<script lang="ts" setup>
import { reactive, ref } from "vue";
import { genFileId } from "element-plus";
import type { UploadInstance, UploadProps, UploadRawFile } from "element-plus";
import axios from "axios";

// do not use same name with ref
const form = reactive({
  facePictureName: "",
  conditionList: []
});

const tempForm = reactive({
  prompt: "",
    pictureName: ""
})

const dialogVisible1 = ref(false);
const dialogVisible2 = ref(false)
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
    tempForm.pictureName = uploadFile.name
  } else if (uploadType.value == 'facePictureName') {
    form.facePictureName = uploadFile.name
  }
  dialogVisible1.value=false
};

const onSubmit = () => {
  console.log("submit!");
  for (var f in form.conditionList) {
    const tmpForm = form.conditionList[f]
    axios
    .post("http://81.68.187.103/api/v1/custom/task/create", {
      type: "mid_journey",
      prompt: tmpForm["prompt"],
      pictureUrl: "/root/taojunzhe/swap_face/bootstrap/resource/" + tmpForm["pictureName"],
      facePictureUrl: "/root/taojunzhe/swap_face/bootstrap/resource/" + form.facePictureName
    //   topic: form.topic,
    })
    .then((res) => {
      console.log(res);
    });
  }
};

const handleAddTask = () => {
  console.log(tempForm)
  const tmp = {
    "prompt": tempForm.prompt,
    "pictureName": tempForm.pictureName
  }
  form.conditionList.push(tmp)
  console.log(form.conditionList)
}

</script>