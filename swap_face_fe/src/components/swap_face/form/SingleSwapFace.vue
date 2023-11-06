<template>
  <el-row style="margin-bottom: 10pt" type="flex" align="middle">
    <el-col style="color: red" :xs="6">
      <span>任务类型</span>
    </el-col>
    <el-col :xs="18">
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
    <el-col :xs="6" style="color: red">
      <span>任务主题</span>
    </el-col>
    <el-col :xs="18">
      <el-select
        v-model="taskTopic"
        class="m-2"
        placeholder="选择任务主题"
        size="large"
      >
        <el-option
          v-for="item in state.taskTopicOptions"
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
  <el-form v-model="createSwapFaceTaskForm">
    <el-form-item>
      <el-select
        v-model="createSwapFaceTaskForm.gender"
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
        v-model="createSwapFaceTaskForm.age"
        class="m-2"
        placeholder="年龄特征"
        size="large"
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
        v-model="createSwapFaceTaskForm.color"
        class="m-2"
        placeholder="颜色特征"
        size="large"
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
</template>
    
<script setup>
import { onMounted, reactive, ref } from "vue";
import { genFileId } from "element-plus";
import axios from "@/utils/axios";

const emit = defineEmits(['closeDialog'])
const upload = ref();

const taskTypeOptions = [
  {
    value: "swap_face",
    label: "AI大头贴",
  },
];

const state = reactive({
  taskTopicOptions: [],
  taskColorOptions: [
    {
      value: "white",
      label: "白"
    },
    {
      value: "black",
      label: "黑"
    },
    {
      value: "blue",
      label: "蓝"
    },
    {
      value: "brown",
      label: "棕"
    },
    {
      value: "all",
      label: "全部"
    }
  ],
  taskGenderOptions: [
    {
      value: "male",
      label: "男",
    },
    {
      value: "female",
      label: "女",
    }
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
      label: "25-30岁"
    },
    {
      value: "30-",
      label: "30岁以上"
    }
  ]

})

const createSwapFaceTaskForm = reactive({
  gender: "",
  age: "",
  color: ""
})

onMounted(() => {
  fetchAllTopic();
})

const fetchAllTopic = () => {
  axios.get("/manager/swap_face/topic/all").then((res) => {
    console.log(res)
    for (let i in res) {
      var op = {
        value: res[i].enName,
        label: res[i].cnName
      }
      console.log(op)
      state.taskTopicOptions.push(op)
    }
  })
}

const imageUrl = ref("");
const taskType = ref(taskTypeOptions[0]);
const taskTopic = ref("");

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
  axios
    .post("/custom/task/create", {
      type: taskType.value.value,
      sourceImagePath:
        "/root/taojunzhe/swap_face/bootstrap/resource/" + uploadFile.name,
      topic: taskTopic.value,
      attrGender: createSwapFaceTaskForm.gender,
      attrAge: createSwapFaceTaskForm.age,
      attrColor: createSwapFaceTaskForm.color
    })
    .then((res) => {
      console.log(res);
      emit('closeDialog')
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