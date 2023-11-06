<template>
  <!-- {{ $route.query }}
    {{ state.modelList }}
    {{ state.topicDetail }} -->
  <div>
    {{ "主题名称:" + state.topicDetail.cnName }}
    <el-button @click="dialogVisible = true" type="primary">添加图片</el-button>
  </div>

  <div>{{ "底图总数:" + state.modelList.length }}</div>
  <div>底图预览，点击图片可以删除</div>
  <div>
    <el-table :data="state.modelList">
      <el-table-column label="序号" prop="id"></el-table-column>
      <el-table-column label="性别" prop="attrGender"></el-table-column>
      <el-table-column label="年龄" prop="attrAge"></el-table-column>
      <el-table-column label="颜色" prop="attrColor"></el-table-column>
      <el-table-column label="预览">
        <template #default="props">
          <el-image :src="genPictureUrl(props.row['imagePath'])"></el-image>
        </template>
      </el-table-column>
      <el-table-column label="操作">
        <template #default="props">
          <el-button type="primary" @click="getBaseModelDetail(props.row['id'])">编辑</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>

  <div class="demo-image__lazy">
    <el-image
      v-for="picture in state.modelList"
      :key="picture.id"
      :src="genPictureUrl(picture.imagePath)"
      @click="openDeleteMessage(picture.id)"
      lazy
    ></el-image>
  </div>
  <el-dialog v-model="dialogVisible2" destroy-on-close width="60%">
    <el-form label-width="80px" label-position="right" class="demo-ruleForm">
      <div>属性选择</div>
      <el-form-item label="性别">
        <el-select
          v-model="updateTopicBasicImageForm.gender"
          placeholder="Select"
          style="width: 480px"
        >
          <el-option
            v-for="item in selectOptions.gender"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="年龄">
        <el-select
          v-model="updateTopicBasicImageForm.age"
          placeholder="Select"
          style="width: 480px"
        >
          <el-option
            v-for="item in selectOptions.age"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          /> </el-select>
        </el-form-item>
        <el-form-item label="颜色">
        <el-select
          v-model="updateTopicBasicImageForm.color"
          placeholder="Select"
          style="width: 480px"
        >
          <el-option
            v-for="item in selectOptions.color"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          /> </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitUpdateModel()">确定</el-button>
        </el-form-item>
    </el-form>
  </el-dialog>

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
            v-for="item in selectOptions.gender"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="年龄">
        <el-select
          v-model="createTopicBasicImageForm.age"
          placeholder="Select"
          style="width: 480px"
        >
          <el-option
            v-for="item in selectOptions.age"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          /> </el-select>
        </el-form-item>
        <el-form-item label="年龄">
        <el-select
          v-model="createTopicBasicImageForm.color"
          placeholder="Select"
          style="width: 480px"
        >
          <el-option
            v-for="item in selectOptions.color"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          /> </el-select>
        </el-form-item>
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
        .delete("/manager/swap_face/topic/base_model", {
          params: {
            imageId: id,
          },
        })
        .then((res) => {
          ElMessage({
            type: "success",
            message: "删除成功",
          });
          fetchTopicBaseModels();
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
  modelList: [],
  topicDetail: {
    id: $route.query.id,
    taskType: "",
    enName: "",
    cnName: "",
  },
});
const dialogVisible = ref(false);
const dialogVisible2 = ref(false);

onMounted(() => {
  fetchTopicDetail();
  fetchTopicBaseModels();
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
      state.topicDetail.taskType = res.taskType;
    });
};

const fetchTopicBaseModels = () => {
  axios
    .get("/manager/swap_face/topic/base_model/all", {
      params: {
        topicId: state.topicDetail.id,
      },
    })
    .then((res) => {
      state.modelList = res;
    });
};

const genPictureUrl = (path) => {
  let suffix = path.split("/root/taojunzhe/swap_face/bootstrap")[1];
  return "http://81.68.187.103" + suffix;
};

const getBaseModelDetail = (idx) => {
  axios
    .get("/manager/swap_face/topic/base_model", {
      params: {
        modelId: idx,
        taskType: state.topicDetail.taskType
      },
    })
    .then((res) => {
      console.log(res)
      updateTopicBasicImageForm.id = idx,
      updateTopicBasicImageForm.gender = res.attrGender,
      updateTopicBasicImageForm.age = res.attrAge,
      updateTopicBasicImageForm.color = res.attrColor
    }).then(() => {
      console.log(updateTopicBasicImageForm.id)
      dialogVisible2.value=true
    });
}

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
  age: "",
  color: "",
});

const updateTopicBasicImageForm = reactive({
  id: 0,
  gender: "",
  age: "",
  color: "",
});

const selectOptions = {
  gender: [
    {
      value: "female",
      label: "女性",
    },
    {
      value: "male",
      label: "男性",
    },
  ],
  age: [
    {
      value: "-18",
      label: "18岁以下",
    },
    {
      value: "18-25",
      label: "18-25岁",
    },
    {
      value: "25-30",
      label: "25-30岁",
    },
    {
      value: "30-",
      label: "30岁以上",
    },
  ],
  color: [
    {
      value: "white",
      label: "白",
    },
    {
      value: "black",
      label: "黑",
    },
    {
      value: "blue",
      label: "蓝",
    },
    {
      value: "brown",
      label: "棕",
    },
  ],
};

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
    .post("/manager/swap_face/topic/base_model", {
      topicId: $route.query.id,
      baseImagePath:
        "/root/taojunzhe/swap_face/bootstrap/resource/" + uploadFile.name,
      attrGender: createTopicBasicImageForm.gender,
      attrAge: createTopicBasicImageForm.age,
    })
    .then((res) => {
      console.log(res);
      emit("closeDialog");
    });
};

const submitUpdateModel = () => {
  console.log(updateTopicBasicImageForm)
  axios
    .put("/manager/swap_face/topic/base_model", {
      modelId: updateTopicBasicImageForm.id,
      taskType: state.topicDetail.taskType,
      attrGender: updateTopicBasicImageForm.gender,
      attrAge: updateTopicBasicImageForm.age,
      attrColor: updateTopicBasicImageForm.color
    })
    .then((res) => {
      console.log(res);
      dialogVisible2.value=false;
      fetchTopicBaseModels();
    });
}
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

