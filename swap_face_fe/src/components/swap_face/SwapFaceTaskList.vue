<template>
  <el-table :data="tableData" style="width: 100%" height="500">
    <el-table-column fixed prop="id" label="序号" width="60" />
    <el-table-column prop="taskType" label="任务类型" width="120" />
    <el-table-column label="状态" width="120">
      <template #default="props">
        {{ parseStatus(props.row["status"]) }}
      </template>
    </el-table-column>
    <el-table-column prop="creator" label="创建人" width="120" />
    <el-table-column
      prop="ctime"
      label="创建时间"
      width="180"
    ></el-table-column>
    <el-table-column label="操作" width="360">
      <template #default="props">
        {{ props.row["taskResultMap"]["resultImageName"] }}
        <el-button
          v-if="props.row['status'] == 3"
          @click="
            dialogContentType = 2;
            showImage(props.row['taskResultMap']['resultImageName']);
          "
          >显示结果</el-button
        >
        <el-button
          v-if="props.row['status'] == 31"
          @click="
            handleProcess(props.row['id']);
            showImage(props.row['taskResultMap']['resultImageNameStep1']);
          "
          >处理</el-button
        >
      </template>
    </el-table-column>
  </el-table>
  <el-dialog v-model="dialogVisible" width="90%" destroy-on-close>
    <template #header>
      <span> {{ parseDialogTitle(dialogContentType) }}</span>
    </template>
    <el-image v-if="imgPath != ''" :src="imgPath" />
    <el-form v-if="dialogContentType == 1">
      <el-form-item label="选择喜欢的图片">
        <el-select v-model="chooseLikePic" placeholder="选择任务主题">
          <el-option
            v-for="item in likePicOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button @click="onSubmitChoosePic(chooseLikePic.value)"
          >提交</el-button
        >
      </el-form-item>
    </el-form>
  </el-dialog>
</template>
  
<script setup lang="ts">
import axios from "axios";
import { onBeforeMount, reactive, ref } from "vue";
const tableData = ref([]);

const dialogContentType = ref(0);
const dialogVisible = ref(false);
const imgPath = ref("");
const chooseTaskId = ref(0);
const chooseLikePic = ref(1);

const likePicOptions = [
  {
    value: 1,
    label: "P1",
  },
  {
    value: 2,
    label: "P2",
  },
  {
    value: 3,
    label: "P3",
  },
  {
    value: 4,
    label: "P4",
  },
];

const parseDialogTitle = (i) => {
  switch (i) {
    case 1:
      return "处理";
    case 2:
      return "结果展示";
  }
};
onBeforeMount(() => {
  axios({
    url: "http://81.68.187.103/api/v1/custom/task/all",
    method: "GET",
  }).then((res) => {
    tableData.value = res.data;
  });
});
const timer = setInterval(() => {
  axios({
    url: "http://81.68.187.103/api/v1/custom/task/all",
    method: "GET",
  }).then((res) => {
    tableData.value = res.data;
  });
}, 5000);

const showImage = (imgName: String) => {
  dialogVisible.value = true;
  imgPath.value = new URL(
    "../../../../resource/" + imgName,
    import.meta.url
  ).href;
  console.log(imgPath.value);
};

const parseStatus = (status) => {
  switch (status) {
    case 1:
      return "新建";
    case 2:
      return "运行中";
    case 3:
      return "成功";
    case 4:
      return "失败";
    case 31:
      return "一阶段完成";
  }
};
const getAllTasks = () => {
  axios({
    url: "http://81.68.187.103/api/v1/custom/task/all",
    method: "GET",
  }).then((res) => {
    console.log(res.data);
    tableData.value = res.data;
  });
};

const handleProcess = (taskId) => {
  console.log(taskId);
  chooseTaskId.value = taskId;
  dialogContentType.value = 1;
  console.log(dialogContentType.value);
  dialogVisible.value = true;
};

const onSubmitChoosePic = (picId) => {
  axios
    .post("http://81.68.187.103/api/v1/custom/task/update", {
      taskId: chooseTaskId.value,
      paramsMap: {
        choosePic: chooseLikePic.value,
      },
      status: 22,
    })
    .then((res) => {
      console.log(res.data);
      dialogVisible.value = false;
    });
  chooseLikePic.value = 1;
};
</script>
