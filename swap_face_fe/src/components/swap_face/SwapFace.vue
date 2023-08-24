<template>


  <el-dialog
    v-model="dialogVisible"
    width="30%"
  >
  <template #header>
    <span>{{ parseDialogTitle(dialogContentType) }}</span>
  </template>
    <SingleUploader v-if="dialogContentType=='1'" />
    <img v-if="dialogContentType=='2'" :src="imagePath" />
  </el-dialog>
  <el-row>
    <el-col :span="8">
      <el-button type="primary" @click="dialogVisible = true;dialogContentType='1'">
        创建任务
      </el-button>
      
    </el-col>
  </el-row>
  <el-row>
    <SwapFaceTaskList :showImage="showImage" />
  </el-row>
</template>

<script setup lang="ts">
import { ref } from "vue";
import SingleUploader from "./SingleUploader.vue";
import SwapFaceTaskList from "./SwapFaceTaskList.vue";

const dialogVisible=ref(false);
const imagePath=ref('');
const dialogContentType=ref('');

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

</script>