<template>
  <el-row>
    <el-col :span="8">
      <SingleUploader />
    </el-col>
    <el-col :span="8"></el-col>
  </el-row>
  <el-row>
    <SwapFaceTaskList />
  </el-row>
</template>

<script setup lang="ts">
import SingleUploader from "./SingleUploader.vue"
import SwapFaceTaskList from "./SwapFaceTaskList.vue";
</script>