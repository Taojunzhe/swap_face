<template>
  <el-card
    class="box-card"
    v-for="taskInfo in childrenTaskInfo"
    style="margin-bottom: 10px"
  >
    <template #header>
      <div class="card-header">
        <el-row justify="space-between">
          <el-col :span="6">{{
            "类型: " + parseTaskType(taskInfo["taskType"])
          }}</el-col>
          <el-col :span="4">{{ taskInfo["ctime"] }}</el-col>
          <el-col :span="4">{{
            "状态: " + statusIntToStr(taskInfo["status"])
          }}</el-col>
        </el-row>
      </div>
    </template>

    <el-row>
      <el-col v-for="url in getAttachmentUrls(taskInfo)" :span="6">
        <el-image :src="url" fit="fit" style="padding: 5px;"></el-image>
      </el-col>
    </el-row>

    <el-form v-if="taskInfo['taskType'] == 'mid_journey_generate_swap_face'">
      <el-form-item label="选择喜欢的图片">
        <el-select
          v-model="chooseIndexs"
          multiple
          placeholder="Select"
          style="width: 480px"
        >
          <el-option
            v-for="item in options"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button @click="submitSelectResult(taskInfo)" :disabled="taskInfo['status'] != 3"
          >提交选图结果</el-button
        >
      </el-form-item>
    </el-form>
    <el-form v-if="taskInfo['taskType'] == 'mid_journey_upscale'">
        <el-form-item>
            <el-button :disabled="taskInfo['status'] != 3" @click="submitScaleSwapFace(taskInfo)">一键换脸</el-button>
        </el-form-item>
    </el-form>
  </el-card>
</template>

<script setup>
import { computed, onBeforeMount, onBeforeUnmount, ref, toRaw } from "vue";
import axios from "axios";

const props = defineProps({
  taskId: Number,
});

const mainTaskInfo = ref("");
const childrenTaskInfo = ref([]);

const chooseIndexs = ref([]);
const options = [
  {
    value: 1,
    label: "左上角",
  },
  {
    value: 2,
    label: "右上角",
  },
  {
    value: 3,
    label: "左下角",
  },
  {
    value: 4,
    label: "右下角",
  },
];

onBeforeMount(() => {
  axios({
    url: "http://81.68.187.103/api/v1/custom/task",
    params: {
      parentTaskId: props.taskId,
    },
  }).then((res) => {
    console.log(res.data);
    childrenTaskInfo.value = res.data;
  });
  axios({
    url: "http://81.68.187.103/api/v1/custom/task",
    params: {
        taskId: props.taskId,
    }
  }).then((res) => {
    console.log(res.data)
    mainTaskInfo.value = res.data[0]
  })
});

const updateChildrenTaskInfo = computed(
  axios({
    url: "http://81.68.187.103/api/v1/custom/task",
    params: {
      parentTaskId: props.taskId,
    },
  }).then((res) => {
    console.log(res.data);
    childrenTaskInfo.value = res.data;
  })
);

const updateChildrenTaskInfoTimer = setInterval(() => {
  axios({
    url: "http://81.68.187.103/api/v1/custom/task",
    params: {
      parentTaskId: props.taskId,
    },
  }).then((res) => {
    console.log(res.data);
    childrenTaskInfo.value = res.data;
  })}, 3000
);

onBeforeUnmount(() => {
    console.log("onBeforeUnmount");
    clearInterval(updateChildrenTaskInfoTimer)
})

const statusIntToStr = (status) => {
  switch (status) {
    case 1:
      return "新建";
    case 2:
      return "运行中";
    case 3:
    case 31:
    case 32:
      return "成功";
    case 4:
      return "失败";
  }
};

const parseTaskType = (taskType) => {
  switch (taskType) {
    case "mid_journey_upscale":
      return "选图放大";
    case "mid_journey_generate":
      return "mid-journey生成";
    case "mid_journey_generate_swap_face":
      return "底图自动换脸";
    case "mid_journey_upscale_swap_face":
        return "选图换脸";
  }
};

const getAttachmentUrls = (taskInfo) => {
  var result = [];
  // console.log(toRaw(taskInfo))
  const attachments = toRaw(taskInfo)["taskResultMap"]["attachments"];
  // console.log(attachments)
  for (var a in attachments) {
    result.push(attachments[a]["proxy_url"]);
  }
  return result;
};

const submitSelectResult = (taskInfo) => {
  const taskInfoRaw = toRaw(taskInfo);
  console.log(taskInfoRaw);
  const msgId = taskInfoRaw["taskResultMap"]["msgId"];
  let resultFileName =
    taskInfoRaw["taskResultMap"]["attachments"][0]["filename"];
  console.log(resultFileName);
  const msgHash = resultFileName.split("_").slice(-1)[0].split(".")[0];
  console.log(msgHash);
  const triggerId = taskInfoRaw["taskInfoMap"]["triggerId"];
  const parentTaskId = taskInfoRaw["parentTaskId"];
  const facePictureUrl = taskInfoRaw["facePictureUrl"]
  console.log(msgId);
  console.log(triggerId);
  console.log(parentTaskId);
  console.log(chooseIndexs.value);
  axios
    .post("http://81.68.187.103/api/v1/custom/task/create", {
      type: "mid_journey_upscale",
      msgId: msgId,
      msgHash: msgHash,
      triggerId: triggerId,
      indexList: chooseIndexs.value,
      parentTaskId: parentTaskId
    })
    .then((res) => {
      console.log(res);
    });
};

const submitScaleSwapFace = (taskInfo) => {
    const taskInfoRaw = toRaw(taskInfo);
    const facePictureUrl = mainTaskInfo.value["taskInfoMap"]["facePictureUrl"]
    const parentTaskId = taskInfoRaw["parentTaskId"];
    const baseImageList = []
    for (var t in taskInfoRaw["taskResultMap"]["attachments"]) {
        baseImageList.push(taskInfoRaw["taskResultMap"]["attachments"][t]["proxy_url"])
    }
    console.log(baseImageList)
    axios
    .post("http://81.68.187.103/api/v1/custom/task/create", {
      type: "mid_journey_upscale_swap_face",
      baseImageList: baseImageList,
      parentTaskId: parentTaskId,
      targetImageUrl: facePictureUrl
    })
    .then((res) => {
      console.log(res);
    });

}
</script>