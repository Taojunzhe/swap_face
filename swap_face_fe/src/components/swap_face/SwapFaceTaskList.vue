<template>
  <el-table :data="tableData" style="width: 100%">
    <el-table-column prop="id" label="id" width="180" />
    <el-table-column prop="taskType" label="taskType" width="180" />
    <el-table-column prop="status" label="status" width="180" />
    <el-table-column label="ops" width="180">
      <template #default="props">
        {{ props.row["taskResultMap"]["resultImageName"] }}
        <el-button @click="getImage(props.row['taskResultMap']['resultImageName'])">显示结果</el-button>
      </template>
    </el-table-column>
   </el-table>
  <template v-if="ifShowImage">
    <el-image :src="imgPath"></el-image>
  </template>
</template>
  
<script>
import axios from 'axios'
export default {
  data() {
    return {
      tableData: [],
      imgPath: "",
      ifShowImage: false
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
  },
  methods: {
    getImage(imgName) {
        this.imgPath = new URL("../../../../resource/" + imgName, import.meta.url).href
        console.log(this.imgPath)
        this.ifShowImage = true
    }
  }
};
</script>
