<template>
  <div class="main-content">
    <div style="width: 70%; margin:30px auto ">
      <div class="search" >
        <el-input placeholder="Search" style="width: 200px" v-model="name"></el-input>
        <el-select v-model="level" placeholder="Level" style="width: 200px; margin-left: 10px">
          <el-option label="Beginner" value="BEGINNER"></el-option>
          <el-option label="Intermediate" value="INTERMEDIATE"></el-option>
          <el-option label="Advanced" value="ADVANCED"></el-option>
        </el-select>
        <el-select v-model="type" placeholder="Type" style="width: 200px; margin-left: 10px">
          <el-option label="Web Development" value="WD"></el-option>
          <el-option label="Programming Language" value="PL"></el-option>
          <el-option label="DevOps Foundations" value="DF"></el-option>
        </el-select>
        <el-button type="info" plain style="margin-left: 10px" @click="load(1)">Search</el-button>
        <el-button type="warning" plain style="margin-left: 10px" @click="reset">Reset</el-button>
      </div>
      <div class="table" >
        <el-table :data="tableData" stripe >

          <el-table-column label="Course cover" show-overflow-tooltip width="100">
            <template v-slot="scope">
              <div style="display: flex; align-items: center">
                <el-image style="width: 60px; height: 40px; border-radius: 5px; border: 1px solid #cccccc" v-if="scope.row.img"
                          :src="scope.row.img" :preview-src-list="[scope.row.img]"></el-image>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="name" label="Course name" show-overflow-tooltip width="200">
            <template v-slot="scope">
              <a :href="'/front/courseDetail?id=' + scope.row.id">{{ scope.row.name }}</a>
            </template>
          </el-table-column>

          <el-table-column prop="type" label="Course type">
            <template v-slot="scope">
              <span v-if="scope.row.type === 'VIDEO'" style="color: #FE7843">Video Course</span>
              <span v-if="scope.row.type === 'DF'" style="color: #FE7843">DevOps Foundations</span>
              <span v-if="scope.row.type === 'PL'" style="color: #FE7843">Programming Language</span>
              <span v-if="scope.row.type === 'WD'" style="color: #FE7843">Web Development</span>
              <span v-if="scope.row.type === 'TEXT'" style="color: #2CBE56">Other Course</span>
            </template>
          </el-table-column>
          <el-table-column prop="price" label="Price">
            <template v-slot="scope">
            <span v-if="scope.row.price === 0">
              <el-button type="success" plain size="mini">Free</el-button>
            </span>
              <span v-else style="color: #e10000; ">Score {{scope.row.price}}</span>
            </template>
          </el-table-column>
          <el-table-column prop="level" label="Level"></el-table-column>
          <el-table-column prop="time" label="Post Date"></el-table-column>




        </el-table>

        <div class="pagination">
          <el-pagination
              background
              @current-change="handleCurrentChange"
              :current-page="pageNum"
              :page-sizes="[5, 10, 20]"
              :page-size="pageSize"
              layout="total, prev, pager, next"
              :total="total">
          </el-pagination>
        </div>
      </div>
    </div>


  </div>
</template>

<script>

export default {

  data() {
    return {
      tableData: [],
      pageNum: 1,
      pageSize: 10,
      total: 0,
      name: null,
      level: null,
      type: null,
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),

    }
  },
  mounted() {
    this.load(1)

  },

  methods: {
    load(pageNum) {
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/course/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
          level: this.level,
          type: this.type,
        }
      }).then(res => {
        this.tableData = res.data?.list
        this.total = res.data?.total
      })
    },
    reset() {
      this.name = null
      this.level = null
      this.type = null
      this.load(1)
    },
    handleCurrentChange(pageNum) {
      this.load(pageNum)
    },
  }
}

</script>
