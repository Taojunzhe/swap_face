<template>


  <el-dialog
    v-model="dialogVisible"
    width="30%"
    :before-close="handleClose"
  >
    <span>创建任务</span>
    <SingleUploader />
  </el-dialog>
  <el-row>
    <el-col :span="8">
      <el-button type="primary" @click="dialogVisible = true">
        创建任务
      </el-button>
      
    </el-col>
    <el-col :span="8"></el-col>
  </el-row>
  <el-row>
    <SwapFaceTaskList />
  </el-row>
</template>

<script setup lang="ts">
import { ref } from "vue";
import SingleUploader from "./SingleUploader.vue";
import SwapFaceTaskList from "./SwapFaceTaskList.vue";
import { ElMessageBox } from "element-plus";

const dialogVisible=ref(false)
const handleClose = (done: () => void) => {
  ElMessageBox.confirm('关闭对话框')
    .then(() => {
      done()
    })
    .catch(() => {
      // catch error
    })
}

</script>