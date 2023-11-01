<template>
  <el-row>
    <el-button @click="dialogVisible = true">新建主题</el-button>
  </el-row>
  <el-row>
    <el-col
      v-for="(o, index) in data.topicList.length"
      :key="o"
      :span="4"
      :offset="index % 4 > 0 ? 2 : 0"
    >
      <el-card :body-style="{ padding: '0px' }">
        <img
          src="https://shadow.elemecdn.com/app/element/hamburger.9cf7b091-55e9-11e9-a976-7f4d0b07eef6.png"
          class="image"
          @click="doClickCard(data.topicList[index].id)"
        />
        <div style="padding: 14px">
          <span>{{ data.topicList[index].cnName }}</span>
          <el-button
            type="danger"
            @click="doDeleteTopic(data.topicList[index].id)"
            >删除</el-button
          >
        </div>
      </el-card>
    </el-col>
  </el-row>
  <el-dialog v-model="dialogVisible" destroy-on-close>
    <el-form v-model="createTopicForm">
      <el-form-item label="英文名称" prop="enName">
        <el-input type="text" v-model="createTopicForm.enName"></el-input>
      </el-form-item>
      <el-form-item label="中文名称" prop="cnName">
        <el-input type="text" v-model="createTopicForm.cnName"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="doCreateTopic()">创建</el-button>
      </el-form-item>
    </el-form>
  </el-dialog>
  <!-- <el-form label-width="80px" label-position="right" class="demo-ruleForm">
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
    <el-form-item label="性别">
      <el-select
        v-model="chooseOption1"
        placeholder="Select"
        style="width: 480px"
      >
        <el-option
          v-for="item in options1"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        />
      </el-select>
    </el-form-item>
  </el-form> -->
</template>
  
<script setup lang="ts">
import { onMounted, reactive, ref } from "vue";
import { useRouter } from "vue-router";
import axios from "@/utils/axios";

import {
  ElMessage,
  UploadInstance,
  UploadProps,
  UploadRawFile,
  genFileId,
} from "element-plus";
import md5 from "js-md5";
const upload = ref<UploadInstance>();
const $router = useRouter();
const dialogVisible = ref(false);
const createTopicForm = ref({
  enName: "",
  cnName: "",
});
const data = reactive({
  topicList: [],
});

const fetchTopicList = () => {
  axios.get("/manager/swap_face/topic/all").then((res) => {
    data.topicList = res;
  });
};

onMounted(() => {
  fetchTopicList();
});

const chooseOption1 = ref();
const options1 = [
  {
    value: "female",
    label: "女性",
  },
  {
    value: "male",
    label: "男性",
  },
];

const state = reactive({});

const handleExceed: UploadProps["onExceed"] = (files) => {
  upload.value!.clearFiles();
  const file = files[0] as UploadRawFile;
  file.uid = genFileId();
  upload.value!.handleStart(file);
};

const submitUpload = () => {
  upload.value!.submit();
};

const doCreateTopic = () => {
  axios
    .post("/manager/swap_face/topic", {
      enName: createTopicForm.value.enName,
      cnName: createTopicForm.value.cnName,
    })
    .then((res) => {
      dialogVisible.value = false;
      fetchTopicList;
    });
};

const doDeleteTopic = (topicId) => {
  axios
    .delete("/manager/swap_face/topic", {
      params: {
        topicId: topicId,
      },
    })
    .then((res) => {
      fetchTopicList();
    });
};

const doClickCard = (idx) => {
  $router.push({path: '/topic_manager/detail', query: {id: idx}})
}

</script>
  
  <style>
.account-container {
  margin-bottom: 20px;
}
</style>