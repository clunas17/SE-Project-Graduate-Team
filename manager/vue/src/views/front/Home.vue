<template>
  <div class="main-content">
<!--    <div style="display: flex">-->
<!--      <div style="flex: 8%; height: 350px; background-color: #d8d8d8"></div>-->

<!--      <div style="flex: 84%">-->
<!--        <el-carousel height="350px">-->
<!--          <el-carousel-item v-for="item in carouselData" >-->
<!--            <img :src="item" alt="" style="width: 100%; height: 100%; object-fit: fill;">-->
<!--          </el-carousel-item>-->
<!--        </el-carousel>-->
<!--      </div>-->
<!--      <div style="flex: 8%; height: 350px; background-color: #d8d8d8"></div>-->

<!--    </div>-->
    <div style="width: 84%; margin: 50px auto; min-height: 1000px; ">
      <div style="display: flex; background-color: #dfdfec">
        <div style="flex: 1">
          <span style="font-size: 24px; color: #333333; margin-right: 50px">Online Course</span>
          <el-button type="success" @click="initValue('DF')">DevOps Foundations</el-button>
          <el-button type="primary" @click="initValue('WD')">Web Development</el-button>
          <el-button type="warning" @click="initValue('PL')">Programming Language</el-button>
        </div>
        <div style="width: 300px">
<!--          <el-button type="info">Sign</el-button>-->
<!--          <span style="margin-left: 30px; color: #12b127;font-weight: 550">Last Signed</span>-->
<!--          <span style="color: gray"></span>-->
        </div>
      </div>
      <div style="display: flex; margin-top: 20px; height:240px; background-color: #f4f4f6">
<!--        <div style="flex:1; margin-right: 10px; width: 0">-->
<!--          <img :src="recommend.img" alt="" style="width: 100%; height: 270px; border-radius: 5px; cursor: pointer" @click="navTo(recommend.id)">-->
<!--          <div style="font-size: 15px;margin-top: 5px" class="overflowShow">{{ recommend.name }}</div>-->
<!--        </div>-->
        <div style="flex:2; margin-left: 10px; margin-right: 10px">
          <el-row :gutter="20">
            <el-col :span="6" style="margin-bottom: 35px" v-for="item in rightData">
              <img :src="item.img" alt="" style="width: 100%; height: 200px; border-radius: 5px; cursor: pointer" @click="navTo(item.id)">
              <div style="color: #333333; margin-top: 10px" class="overflowShow">{{ item.name }}</div>
            </el-col>

          </el-row>
        </div>
      </div>

      <div style="margin-top: 50px; background-color: #dfdfec">
          <span style="font-size: 24px; color: #333333; margin-right: 50px">Online Resource</span>
      </div>

      <div style="display: flex; margin-top: 20px; height: 240px; background-color: #f4f4f6">
        <div style="flex: 2; margin-left: 10px; margin-right: 10px">
          <el-row :gutter="20">
            <el-col :span="6" style="margin-bottom: 35px" v-for="item in leftData">
              <img :src="item.img" alt="" style="width: 100%; height: 200px; border-radius: 5px; border: 1px solid #cccccc; cursor: pointer" @click="navToInformation(item.id)">
              <div style="color: #333333; margin-top: 10px" class="overflowShow">{{ item.name }}</div>
            </el-col>
          </el-row>
        </div>
<!--        <div style="flex: 1; margin-left: 10px; width: 0">-->
<!--          <img :src="fileRecommend?.img" alt="" style="width: 100%; height: 270px; border-radius: 5px; cursor: pointer" @click="navToInformation(fileRecommend?.id)">-->
<!--          <div style="font-size: 15px; margin-top: 5px" class="overflowShow">{{ fileRecommend?.name }}</div>-->
<!--        </div>-->
      </div>

    </div>

  </div>
</template>

<script>

export default {

  data() {
    return {
      carouselData:[
          require('@/assets/imgs/1.png'),
      ],
      type: 'DF',
      recommend:{},
      rightData:{},
      fileRecommend: {},
      leftData:{},
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
    }
  },
  mounted() {
    this.loadRecommend()
    this.loadRightData()
    this.getData()
    this.getInformation()
  },

  methods: {
    navTo(id) {
      location.href = '/front/courseDetail?id=' + id
    },
    navToInformation(id) {
      location.href = '/front/informationDetail?id=' + id
    },
    initValue(type) {
      this.type = type
      this.getData()
    },
    getInformation() {
      this.$request.get('/information/getRecommend').then(res => {
        if (res.code === '200') {
          this.fileRecommend = res.data
        } else {
          this.$message.error(res.msg)
        }
      })
      this.$request.get('/information/selectTop8').then(res => {
        if (res.code === '200') {
          this.leftData = res.data
        } else {
          this.$message.error(res.msg)
        }
      })
    },

    getData() {
        this.getRecommend('/course/getRecommend?type=' + this.type)
        this.getRightData('/course/selectTop8?type=' + this.type)
    },

    getRecommend(url) {
      this.$request.get(url).then(res => {
        if (res.code === '200') {
          this.recommend = res.data
        } else {
           this.$message.error(res.msg)
        }
      })
    },
    getRightData(url) {
      this.$request.get(url).then(res => {
        if (res.code === '200') {
          this.rightData = res.data
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    loadRecommend(){
      this.$request.get("/course/getRecommend").then(res=>{
        if(res.code === '200'){
          this.recommend=res.data
        }else {
          this.$message.error(res.msg)
        }
      })
    },
    loadRightData() {
      this.$request.get("/course/selectTop8",{
        params:{
          recommend:'No'
        }
      }).then(res=>{
        if(res.code === '200'){
          this.rightData=res.data
        }else {
          this.$message.error(res.msg)
        }
      })
    },
  }
}

</script>
