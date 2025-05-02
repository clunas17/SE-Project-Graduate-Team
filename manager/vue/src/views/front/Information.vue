<template>
  <div class="main-content">
    <div style="width: 70%; margin: 30px auto">
      <div style="margin-bottom: 20px">
        <el-input placeholder="Search" style="width: 200px" size="mini" v-model="name"></el-input>
        <el-button type="info" plain style="margin-left: 10px" size="mini" @click="load(1)">Search</el-button>
        <el-button type="warning" plain style="margin-left: 10px" size="mini" @click="reset">Reset</el-button>
      </div>
      <div class="table">
        <el-table :data="tableData" stripe>
          <el-table-column prop="id" label="ID" width="80" align="center" sortable></el-table-column>
          <el-table-column prop="img" label="Cover" show-overflow-tooltip width="100">
            <template v-slot="scope">
              <div style="display: flex; align-items: center">
                <el-image style="width: 60px; height: 40px; border-radius: 5px; border: 1px solid #cccccc" v-if="scope.row.img"
                          :src="scope.row.img" :preview-src-list="[scope.row.img]"></el-image>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="name" label="Course Name" show-overflow-tooltip width="400">
            <template v-slot="scope">
              <a :href="'/front/informationDetail?id=' + scope.row.id">{{ scope.row.name }}</a>
            </template>
          </el-table-column>
          <el-table-column prop="userName" label="Poster"></el-table-column>
          <el-table-column prop="score" label="Price">
            <template v-slot="scope">
              <span style="color: #12b127; font-size: 15px" v-if="scope.row.score > 0">{{ scope.row.score }} Score</span>
              <span v-else style="color: #12b127; font-size: 15px">Free Course</span>
            </template>
          </el-table-column>
          <el-table-column prop="time" label="Post Date"></el-table-column>
        </el-table>

        <div class="pagination" style="margin-top: 20px">
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
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      tableData: [],
      pageNum: 1,
      pageSize: 10,
      total: 0,
      name: null,
    }
  },
  mounted() {
    this.load(1)
  },

  methods: {
    load(pageNum) {
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/information/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
          status: 'Approved'
        }
      }).then(res => {
        this.tableData = res.data?.list
        this.total = res.data?.total
      })
    },
    reset() {
      this.name = null
      this.load(1)
    },
    handleCurrentChange(pageNum) {
      this.load(pageNum)
    },
  }
}
</script>