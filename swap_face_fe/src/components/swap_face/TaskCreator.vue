<template>
  <el-tabs v-model="activeName" tabPosition="top">
    <el-tab-pane label="主题换脸" name="swap_face"
      ><SinglgSwapFace
    /></el-tab-pane>
    <el-tab-pane label="MD主题换脸" name="midjourney"
      ><mid-journey-swap-face
    /></el-tab-pane>
  </el-tabs>
</template>

<script lang="ts" setup>
import { ref } from "vue";
import type { TabsPaneContext } from "element-plus";
import SinglgSwapFace from "./form/SingleSwapFace.vue";
import MidJourneySwapFace from "./form/MidJourneySwapFace.vue";
const activeName = ref("swap_face");
</script>