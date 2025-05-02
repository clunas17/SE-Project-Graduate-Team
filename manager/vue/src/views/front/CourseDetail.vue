<template>
  <div class="main-content">
    <div style="width: 70%; margin: 30px auto; min-height: 1000px">
      <div style="text-align: center">
        <el-button type="success">{{ courseData.type }}</el-button>
        <span style="font-size: 20px; font-weight: 550; color: #333333; margin-left: 20px">{{ courseData.name }}</span>
      </div>
      <div style="text-align: center; margin-top: 15px">
        <span style="color: red" v-if="courseData.price > 0">$ {{ courseData.price }} </span>
        <span style="color: green" v-else>Start learning!</span>
        <span style="color: #666666; margin-left: 50px">Post Date：{{ courseData.time }}</span>
      </div>

      <div>
        <div style="font-size: 18px; margin: 10px 0">Course Content</div>
        <div v-if="courseData.price === 0">
          <video :src="courseData.video" v-if="courseData.video !== null & courseData.video !== ''" controls style="width: 65%; height: 400px"></video>
          <div style="margin-top: 10px">File link：<a :href="courseData.file" target="_blank">{{ courseData.file }}</a></div>
        </div>
        <div v-else>
          <span style="color: red; margin-right: 20px">Unlock the content after payment</span>
          <el-button type="warning" size="mini">Get the Course today!</el-button>
        </div>
      </div>
      <div style="margin-top: 20px">
        <div style="font-size: 18px">Course description</div>
        <div style="margin-top: 10px" v-html="courseData.content" class="w-e-text w-e-text-container"></div>
      </div>
    </div>
  </div>
</template>
<script>
import E from 'wangeditor'
export default {

  data() {
    let courseId = this.$route.query.id
    return {
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      courseId: courseId,
      courseData: {}
    }
  },
  mounted() {
    this.loadCourse()
  },

  methods: {
    loadCourse() {
      this.$request.get('/course/selectById/' + this.courseId).then(res => {
        if (res.code === '200') {
          this.courseData = res.data
        } else {
          this.$message.error(res.msg)
        }
      })
    }
  }
}
</script>

<style scoped>
/deep/.el-form-item__label {
  font-weight: bold;
}
/deep/.el-upload {
  border-radius: 50%;
}
/deep/.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  border-radius: 50%;
}
/deep/.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 120px;
  height: 120px;
  line-height: 120px;
  text-align: center;
  border-radius: 50%;
}
.avatar {
  width: 120px;
  height: 120px;
  display: block;
  border-radius: 50%;
}
</style>