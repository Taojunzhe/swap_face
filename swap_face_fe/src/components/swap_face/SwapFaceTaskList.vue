<template>
  <el-table :data="state.tableData" style="width: 100%" height="500">
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
        <el-button
          v-if="
            props.row['status'] == 3
          "
          @click="
            dialogContentType = 2;
            showResult(props.row['id']);
          "
          >显示结果</el-button
        >
        <!-- <el-button
          v-if="props.row['taskType'] != 'swap_face'"
          @click="handleProcess(props.row['id'])"
          >查看详情</el-button
        > -->
      </template>
    </el-table-column>
  </el-table>

  <el-dialog
    v-model="dialogVisible"
    width="90%"
    destroy-on-close
    @closed="state.resultImageList=[]"
  >
    <template #header>
      <span>任务结果</span>
    </template>
    <el-row>
      {{ "共" + state.resultImageList.length + "张结果" }}
    </el-row>
    <el-row justify="space-between">
      <el-col v-for="image in state.resultImageList" :span="4">
        <el-image :src="genImageUrl(image)"></el-image>
      </el-col>
    </el-row>
    <!-- <MidJourneySwapFace v-if="dialogContentType == 1" :task-id="chooseTaskId" /> -->
  </el-dialog>
</template>
  
<script setup>
import axios from "@/utils/axios.js";
import { onBeforeMount, reactive, ref, getCurrentInstance, onMounted } from "vue";
import MidJourneySwapFace from "./detail/MidJourneySwapFace.vue";
const app = getCurrentInstance()
const { goTop } = app.appContext.config.globalProperties

const state = reactive({
  loading: false,
  tableData: [], // 数据列表
  resultImageList: []
});

const showResult = (idx) => {
  axios.get("/custom/task/result", {
    params: {
      taskId: idx
    }
  }).then((res) => {
    console.log(res)
    state.resultImageList = res
  }).then(() => {
    dialogVisible.value = true
  })
}

const dialogContentType = ref(0);
const dialogVisible = ref(false);
const imgPath = ref("");
const chooseTaskId = ref(0);
const chooseLikePic = ref(1);

const parseDialogTitle = (i) => {
  switch (i) {
    case 1:
      return "处理";
    case 2:
      return "结果展示";
  }
};
onMounted(() => {
  getAllTasks();
});
// const timer = setInterval(() => {
//   getAllTasks();
// }, 5000);

const genImageUrl = (imgName) => {
  return "http://81.68.187.103/resource/" + imgName
};

const parseStatus = (status) => {
  switch (status) {
    case 1:
      return "新建";
    case 13:
      return "选图换脸创建";
    case 2:
      return "运行中";
    case 21:
      return "md生成中";
    case 22:
      return "md换脸中";
    case 23:
      return "选图生成中";
    case 24:
      return "选图换脸中";
    case 3:
      return "成功";
    case 31:
      return "md底图完成";
    case 32:
      return "md换脸完成";
    case 33:
      return "选图生成完成";
    case 4:
      return "失败";
  }
};
const getAllTasks = () => {
  state.loading = true
  axios.get("/custom/task/all").then((res) => {
    console.log(res)
    state.tableData = res
    state.loading = false
    goTop && goTop()
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
    .post("/custom/task/update", {
      taskId: chooseTaskId.value,
      paramsMap: {
        choosePic: chooseLikePic.value,
      },
      status: 13,
    })
    .then((res) => {
      console.log(res.data);
      dialogVisible.value = false;
    });
  chooseLikePic.value = 1;
};
</script>
