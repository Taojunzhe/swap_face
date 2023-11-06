<template>
  <el-form :model="form" label-position="left" label-width="80px">
    <el-form-item label="人脸底图">
      <el-col :span="10">
        <el-input v-model="form.facePictureName" disabled="true" />
      </el-col>
      <el-col :span="4" style="margin-left: 15px">
        <el-button
          type="success"
          @click="
            dialogVisible1 = true;
            uploadType = 'facePictureName';
          "
          :disabled="form.facePictureName != ''"
          >上传图片</el-button
        >
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
  <el-card
    v-for="(item, index) in form.conditionList"
    :key="index"
    style="margin-bottom: 20px"
  >
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
    <el-row style="margin-bottom: 20px">
      <el-col :span="4" style="align-self: center"> 主题 </el-col>
      <el-col :span="20" style="align-self: center">
        <el-input disabled="true" :placeholder="item['topicName']"></el-input>
      </el-col>
    </el-row>
    <!-- <el-row>
      <el-col :span="4" style="align-self: center">图片预览</el-col>
      <el-col :span="20" style="align-self: center">
        <el-image
          :src="'http://81.68.187.103/resource/' + item['pictureName']"
          :preview-src-list="[
            'http://81.68.187.103/resource/' + item['pictureName'],
          ]"
          style="width: 200px; height: 200px"
        ></el-image>
      </el-col>
    </el-row> -->
  </el-card>
  <el-form inline>
    <el-form-item>
      <el-button
        @click="
          onSubmit();
          $emit('closeDialog');
        "
        :disabled="form.conditionList.length == 0"
        >提交任务</el-button
      >
    </el-form-item>
    <el-form-item>
      <el-button
        @click="
          dialogVisible2 = true;
          tempForm.prompt = '';
          tempForm.pictureName = '';
        "
        :disabled="form.conditionList.length >= 10"
        >新增主题(上限10个)</el-button
      >
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
    <div v-if="false">
      <el-form-item label="提示词">
        <el-input v-model="tempForm.prompt"></el-input>
      </el-form-item>
      <el-form-item label="联想图">
        <el-col :span="10">
          <el-input v-model="tempForm.pictureName" disabled="true" />
        </el-col>
        <el-col :span="4" style="margin-left: 15px">
          <el-button
            type="success"
            @click="
              dialogVisible1 = true;
              uploadType = 'pictureName';
            "
            >上传图片</el-button
          >
        </el-col>
      </el-form-item>
    </div>
    <el-form-item label="主题">
      <el-select
        v-model="tempForm.topicId"
        placeholder="Select"
        style="width: 480px"
      >
        <el-option
          v-for="item in state.taskTopicOptions"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        />
      </el-select>
    </el-form-item>
    <div>
      属性选择
      <el-form>
        <el-form-item>
          <el-select
            v-model="tempForm.attrGender"
            class="m-2"
            placeholder="性别特征"
            size="large"
          >
            <el-option
              v-for="item in state.taskGenderOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-select
            v-model="tempForm.attrAge"
            class="m-2"
            placeholder="年龄特征"
            size="large"
            disabled
          >
            <el-option
              v-for="item in state.taskAgeOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-select
            v-model="tempForm.attrColor"
            class="m-2"
            placeholder="颜色特征"
            size="large"
            disabled
          >
            <el-option
              v-for="item in state.taskColorOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
      </el-form>
    </div>
    <el-form-item>
      <el-button
        type="primary"
        @click="
          handleAddTask();
          dialogVisible2 = false;
        "
        >确定</el-button
      >
    </el-form-item>
  </el-dialog>
</template>

<script setup>
import { onMounted, reactive, ref } from "vue";
import { genFileId } from "element-plus";
import axios from "@/utils/axios";

const state = reactive({
  taskTopicOptions: [],
  taskColorOptions: [
    {
      value: "white",
      label: "白",
    },
    {
      value: "black",
      label: "黑",
    },
    {
      value: "blue",
      label: "蓝",
    },
    {
      value: "brown",
      label: "棕",
    },
    {
      value: "all",
      label: "全部",
    },
  ],
  taskGenderOptions: [
    {
      value: "male",
      label: "男",
    },
    {
      value: "female",
      label: "女",
    },
  ],
  taskAgeOptions: [
    {
      value: "-18",
      label: "18岁以下",
    },
    {
      value: "18-25",
      label: "18-25岁",
    },
    {
      value: "25-30",
      label: "25-30岁",
    },
    {
      value: "30-",
      label: "30岁以上",
    },
  ],
});

// do not use same name with ref
const form = reactive({
  facePictureName: "",
  conditionList: [],
});

onMounted(() => {
  fetchAllTopic();
});

const fetchAllTopic = () => {
  axios
    .get("/manager/swap_face/topic/all", {
      params: {
        taskType: "mid_journey",
      },
    })
    .then((res) => {
      console.log(res);
      for (let i in res) {
        var op = {
          value: res[i].id,
          label: res[i].cnName,
        };
        console.log(op);
        state.taskTopicOptions.push(op);
      }
    });
};

const tempForm = reactive({
  prompt: "",
  pictureName: "",
  topicId: null,
  attrGender: "",
  attrAge: "",
  attrColor: "",
});

const dialogVisible1 = ref(false);
const dialogVisible2 = ref(false);
const upload = ref();
const uploadType = ref("");

const handleExceed = (files) => {
  upload.value.clearFiles();
  const file = files[0];
  file.uid = genFileId();
  upload.value.handleStart(file);
};

const submitUpload = () => {
  upload.value.submit();
};

const successUpload = (response, uploadFile) => {
  console.log(uploadFile.name);
  if (uploadType.value == "pictureName") {
    tempForm.pictureName = uploadFile.name;
  } else if (uploadType.value == "facePictureName") {
    form.facePictureName = uploadFile.name;
  }
  dialogVisible1.value = false;
};

const onSubmit = () => {
  console.log("submit!");
  for (var f in form.conditionList) {
    const tmpForm = form.conditionList[f];
    axios
      .post("/custom/task/create", {
        type: "mid_journey",
        prompt: tmpForm["prompt"] != null ? tmpForm["prompt"] : null,
        pictureUrl:
          tmpForm["pictureName"] != ""
            ? "/root/taojunzhe/swap_face/bootstrap/resource/" +
              tmpForm["pictureName"]
            : null,
        facePictureUrl:
          form.facePictureName != ""
            ? "/root/taojunzhe/swap_face/bootstrap/resource/" +
              form.facePictureName
            : null,
        topicId: tmpForm["topicId"],
        attrGender: tmpForm["attrGender"],
        attrAge: tmpForm["attrAge"],
        attrColor: tmpForm["attrColor"]
      })
      .then((res) => {
        console.log(res);
      });
  }
};

const handleAddTask = () => {
  console.log(tempForm);
  const tmp = {
    prompt: tempForm.prompt,
    pictureName: tempForm.pictureName,
    topicId: tempForm.topicId,
    attrGender: tempForm.attrGender,
    attrAge: tempForm.attrAge,
    attrColor: tempForm.attrColor,
    topicName: topicIdToName(tempForm.topicId)
  };
  form.conditionList.push(tmp);
  console.log(form.conditionList);
};

const topicIdToName = (idx) => {
  console.log(state.taskTopicOptions)
  for (var i in state.taskTopicOptions) {
    console.log(i)
    console.log(state.taskTopicOptions[i].value)
    if (String(state.taskTopicOptions[i].value) == String(idx)) {
      return state.taskTopicOptions[i].label
    }
  }
}
</script>