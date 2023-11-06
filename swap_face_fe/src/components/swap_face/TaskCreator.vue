<template>
  <el-tabs v-model="activeName" tabPosition="top">
    <el-tab-pane label="MD主题换脸" name="midjourney" disabled="true"
      ><mid-journey-swap-face @closeDialog="$emit('closeDialog')"
    /></el-tab-pane>
    <el-tab-pane label="主题换脸" name="swap_face"
      ><single-swap-face @closeDialog="$emit('closeDialog')"
    /></el-tab-pane>
  </el-tabs>
</template>

<script setup lang="ts">
import { ref } from "vue";
import { TabsPaneContext } from "element-plus";
import SingleSwapFace from "./form/SingleSwapFace.vue";
import MidJourneySwapFace from "./form/MidJourneySwapFace.vue";

const activeName = ref("swap_face");
</script>