<template>
  <el-table :data="tableData" style="width: 100%" height="500">
    <el-table-column fixed prop="id" label="序号" width="60" />
    <el-table-column prop="taskType" label="任务类型" width="120" />
    <el-table-column label="状态" width="120">
      <template #default="props">
        {{ parseStatus(props.row["status"]) }}
      </template>
    </el-table-column>
    <el-table-column prop="creator" label="创建人" width="180" />
    <el-table-column prop="ctime" label="创建时间" width="180"></el-table-column>
    <el-table-column label="操作" width="180">
      <template #default="props">
        {{ props.row["taskResultMap"]["resultImageName"] }}
        <el-button
          v-if="props.row['status'] == 3"
          @click="getImage(props.row['taskResultMap']['resultImageName'])"
          >显示结果</el-button
        >
      </template>
    </el-table-column>
  </el-table>
</template>
  
<script>
import axios from "axios";
export default {
  props: ["showImage"],
  data() {
    return {
      tableData: [],
    };
  },
  created() {
    axios({
        url: "http://81.68.187.103/api/v1/custom/task/all",
        method: "GET",
      }).then((res) => {
        console.log(res.data);
        this.tableData = res.data;
      });
    this.timer = setInterval(() => {
      axios({
        url: "http://81.68.187.103/api/v1/custom/task/all",
        method: "GET",
      }).then((res) => {
        console.log(res.data);
        this.tableData = res.data;
      });
    }, 5000);
  },
  destroyed() {
    clearInterval(this.timer);
  },
  computed() {
    this.getAllTasks();
  },
  methods: {
    getImage(imgName) {
      const pth = new URL("../../../../resource/" + imgName, import.meta.url)
        .href;
      this.showImage(pth);
    },
    parseStatus(s) {
      switch (s) {
        case 1:
          return "新建";
        case 2:
          return "运行中";
        case 3:
          return "成功";
        case 4:
          return "失败";
      }
    },
    getAllTasks() {
      axios({
        url: "http://81.68.187.103/api/v1/custom/task/all",
        method: "GET",
      }).then((res) => {
        console.log(res.data);
        this.tableData = res.data;
      });
    },
  },
};
</script>
