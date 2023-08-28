<template>


  <el-dialog
    v-model="dialogVisible"
    width="80%"
  >
  <template #header>
    <span>{{ parseDialogTitle(dialogContentType) }}</span>
  </template>
    <SingleUploader v-if="dialogContentType=='1'" :closeDialog="closeDialog" />
    <el-image v-if="dialogContentType=='2'" :src="imagePath" />
  </el-dialog>
  <el-row type="flex" align="center">
    <el-col :span="8">
      <el-button type="primary" @click="dialogVisible = true;dialogContentType='1'">
        创建任务
      </el-button>
    </el-col>
    <el-col :span="1" style="align-items: center; margin-right: 10px;">
      <el-icon :size="15" :color="accessProcessorCnt > 0 ? '#67C23A' : '#F56C6C'">
        <View />
      </el-icon>
    </el-col>
    <el-col :span="10">
      <span>当前可用节点数: {{ accessProcessorCnt }}</span>
    </el-col>
  </el-row>
  <el-row>
    <SwapFaceTaskList :showImage="showImage" />
  </el-row>
</template>

<script setup lang="ts">
import { ElMessage } from "element-plus";
import { fa } from "element-plus/es/locale";
import { ref } from "vue";
import SingleUploader from "./SingleUploader.vue";
import SwapFaceTaskList from "./SwapFaceTaskList.vue";
import axios from 'axios';

const dialogVisible=ref(false);
const imagePath=ref('');
const dialogContentType=ref('');
const accessProcessorCnt = ref(0);

const parseDialogTitle = (i) => {
  switch (i) {
    case "1": return "创建任务";
    case "2": return "结果展示";
  }
}

const handleClose = (done: () => void) => {
  // ElMessageBox.confirm('关闭对话框')
  //   .then(() => {
  //     done()
  //   })
  //   .catch(() => {
  //     // catch error
  //   })
}

const showImage = (imgPath) => {
  imagePath.value=imgPath;
  dialogContentType.value='2';
  dialogVisible.value=true;
  console.log("dialogVisible")
}

const closeDialog: Function = () => {
  console.log("closeDialog")
  ElMessage("创建成功")
  dialogVisible.value=false
}

const timer = setInterval(() => {
      axios({
        url: "http://81.68.187.103/api/v1/taskProcessor/access",
        method: "GET",
      }).then((res) => {
        accessProcessorCnt.value = res.data.cnt;
      });
    }, 1000);

</script>