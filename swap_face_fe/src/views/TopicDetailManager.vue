<template>
  <!-- {{ $route.query }}
    {{ state.pictureList }}
    {{ state.topicDetail }} -->
  <div>
    {{ "主题名称:" + state.topicDetail.cnName }}
    <el-button @click="dialogVisible = true" type="primary">添加图片</el-button>
  </div>

  <div>{{ "底图总数:" + state.pictureList.length }}</div>
  <div>底图预览，点击图片可以删除</div>
  <div class="demo-image__lazy">
    <el-image
      v-for="picture in state.pictureList"
      :key="picture.id"
      :src="genPictureUrl(picture.imagePath)"
      @click="openDeleteMessage(picture.id)"
      lazy
    ></el-image>
  </div>
  <el-dialog v-model="dialogVisible" destroy-on-close width="60%">
    <el-upload
      ref="upload"
      action="http://81.68.187.103/api/v1/file/upload"
      :limit="1"
      :on-exceed="handleExceed"
      :auto-upload="false"
      :on-success="successUpload"
      list-type="picture"
    >
      <template #trigger>
        <el-button type="primary" class="ml-3">选择文件</el-button>
      </template>
      <el-button class="ml-3" type="success" @click="submitUpload">
        创建任务
      </el-button>
      <template #tip>
        <div class="el-upload__tip text-red" style="color: red">
          限制提交一个文件
        </div>
      </template>
    </el-upload>
    <el-form label-width="80px" label-position="right" class="demo-ruleForm">
      <div>属性选择</div>
      <el-form-item label="性别">
        <el-select
          v-model="createTopicBasicImageForm.gender"
          placeholder="Select"
          style="width: 480px"
        >
          <el-option
            v-for="item in options1"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="年龄"><el-input v-model="createTopicBasicImageForm.age"></el-input></el-form-item>
    </el-form>
  </el-dialog>
</template>
    
  <script setup>
import { onMounted, reactive, ref } from "vue";
import { useRoute, useRouter } from "vue-router";
import axios from "@/utils/axios";
import { ElMessage, genFileId, ElMessageBox } from "element-plus";
import md5 from "js-md5";

const openDeleteMessage = (id) => {
  ElMessageBox.confirm("确认删除该图片", "Warning", {
    confirmButtonText: "确认",
    cancelButtonText: "取消",
    type: "warning",
  })
    .then(() => {
      axios
        .delete("/manager/swap_face/topic/base_image", {
          params: {
            imageId: id,
          },
        })
        .then((res) => {
          ElMessage({
            type: "success",
            message: "删除成功",
          });
          fetchTopicBaseImages();
        });
    })
    .catch(() => {
      ElMessage({
        type: "info",
        message: "取消",
      });
    });
};

const $route = useRoute();
const state = reactive({
  pictureList: [],
  topicDetail: {
    id: $route.query.id,
    enName: "",
    cnName: "",
  },
});
const dialogVisible = ref(false);

onMounted(() => {
  fetchTopicDetail();
  fetchTopicBaseImages();
});

const fetchTopicDetail = () => {
  console.log($route.query.id);
  axios
    .get("/manager/swap_face/topic", {
      params: {
        topicId: state.topicDetail.id,
      },
    })
    .then((res) => {
      state.topicDetail.enName = res.enName;
      state.topicDetail.cnName = res.cnName;
    });
};

const fetchTopicBaseImages = () => {
  axios
    .get("/manager/swap_face/topic/base_image/all", {
      params: {
        topicId: state.topicDetail.id,
      },
    })
    .then((res) => {
      state.pictureList = res;
    });
};

const genPictureUrl = (path) => {
  let suffix = path.split("/root/taojunzhe/swap_face/bootstrap")[1];
  return "http://81.68.187.103" + suffix;
};

const upload = ref();

const createTopicForm = ref({
  enName: "",
  cnName: "",
});
const data = reactive({
  topicList: [],
});

const fetchTopicList = () => {
  axios.get("/manager/swap_face/topic/all").then((res) => {
    data.topicList = res;
  });
};

onMounted(() => {
  fetchTopicList();
});

const createTopicBasicImageForm = reactive({
    gender: "",
    age: ""
});
const options1 = [
  {
    value: "female",
    label: "女性",
  },
  {
    value: "male",
    label: "男性",
  },
];

const handleExceed = (files) => {
  upload.value.clearFiles();
  const file = files[0];
  file.uid = genFileId();
  upload.value.handleStart(file);
};

const submitUpload = () => {
  upload.value.submit();
};

const successUpload = (response, uploadFile) => {
  console.log(uploadFile.name);
  axios
    .post("/manager/swap_face/topic/base_image", {
        topicId: $route.query.id,
        baseImagePath: "/root/taojunzhe/swap_face/bootstrap/resource/" + uploadFile.name,
        attrGender: createTopicBasicImageForm.gender,
        attrAge: createTopicBasicImageForm.age
    })
    .then((res) => {
      console.log(res);
      emit("closeDialog");
    });
};
</script>
    
    <style scoped>
.demo-image__lazy {
  height: 400px;
  overflow-y: auto;
  width: 50%;
}
.demo-image__lazy .el-image {
  display: block;
  min-height: 200px;
  margin-bottom: 10px;
}
.demo-image__lazy .el-image:last-child {
  margin-bottom: 0;
}
</style>

