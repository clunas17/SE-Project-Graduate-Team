<template>
  <div>
    <div class="search">
      <el-input placeholder="Search" style="width: 200px" v-model="name"></el-input>
      <el-select v-model="recommend" placeholder="Recommended" style="width: 200px; margin-left: 5px">
        <el-option label="Yes" value="Yes"></el-option>
        <el-option label="No" value="No"></el-option>
      </el-select>
      <el-button type="info" plain style="margin-left: 10px" @click="load(1)">Search</el-button>
      <el-button type="warning" plain style="margin-left: 10px" @click="reset">Reset</el-button>
    </div>

    <div class="table">
      <el-table :data="tableData" stripe>
        <el-table-column prop="id" label="ID" width="80" align="center" sortable></el-table-column>
        <el-table-column prop="img" label="Cover" width="100">
          <template v-slot="scope">
            <div style="display: flex; align-items: center">
              <el-image style="width: 60px; height: 40px; border-radius: 10px" v-if="scope.row.img"
                        :src="scope.row.img" :preview-src-list="[scope.row.img]"></el-image>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="name" label="Name" width="150" show-overflow-tooltip></el-table-column>
        <el-table-column prop="content" label="Description" width="100">
          <template v-slot="scope">
            <el-button type="success" size="mini" @click="viewDataInit(scope.row.content)">view</el-button>
          </template>
        </el-table-column>
        <el-table-column prop="userName" label="Poster"></el-table-column>
        <el-table-column prop="time" label="Post Date" width="100"></el-table-column>
        <el-table-column prop="file" label="Link" show-overflow-tooltip></el-table-column>
        <el-table-column prop="score" label="Score" show-overflow-tooltip></el-table-column>
        <el-table-column prop="recommend" label="Recommended"></el-table-column>
        <el-table-column prop="status" label="Audit Status"></el-table-column>
        <el-table-column prop="descr" label="Audit Comment"></el-table-column>

        <el-table-column label="Operation" width="180" align="center">
          <template v-slot="scope">
            <el-button plain type="primary" @click="handleEdit(scope.row)" size="mini">Audit</el-button>
          </template>
        </el-table-column>
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


    <el-dialog title="Course Information" :visible.sync="fromVisible" width="55%" :close-on-click-modal="false" destroy-on-close>
      <el-form label-width="100px" style="padding-right: 50px" :model="form" :rules="rules" ref="formRef">
        <el-form-item prop="file" label="Link">
          <a :href="form.file" target="_blank">{{form.file}}</a>
        </el-form-item>
        <el-form-item prop="recommend" label="Recommended">
          <el-select v-model="form.recommend" placeholder="select" style="width: 100%">
            <el-option label="Yes" value="Yes"></el-option>
            <el-option label="No" value="No"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item prop="status" label="Status">
          <el-select v-model="form.status" placeholder="select" style="width: 100%">
            <el-option label="Processing" value="Processing"></el-option>
            <el-option label="Approved" value="Approved"></el-option>
            <el-option label="Rejected" value="Rejected"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item prop="descr" label="Comment">
          <el-input v-model="form.descr" autocomplete="off" placeholder="Comment"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="fromVisible = false">Cancel</el-button>
        <el-button type="primary" @click="save">Save</el-button>
      </div>
    </el-dialog>

    <el-dialog title="Course Content" :visible.sync="editorVisible" width="50%" :close-on-click-modal="false" destroy-on-close>
      <div v-html="viewData" class="w-e-text w-e-text-container"></div>
    </el-dialog>

  </div>
</template>

<script>
import E from 'wangeditor'
export default {
  name: "Information",
  data() {
    return {
      tableData: [],
      pageNum: 1,
      pageSize: 10,
      total: 0,
      name: null,
      recommend: null,
      fromVisible: false,
      editorVisible: false,
      form: {},
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      rules: {
      },
      ids: [],
      viewData: null
    }
  },
  created() {
    this.load(1)
  },
  methods: {
    viewDataInit(data) {
      this.viewData = data
      this.editorVisible = true
    },
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.fromVisible = true
    },
    save() {
      this.$request({
        url: this.form.id ? '/information/update' : '/information/add',
        method: this.form.id ? 'PUT' : 'POST',
        data: this.form
      }).then(res => {
        if (res.code === '200') {
          this.$message.success('success')
          this.load(1)
          this.fromVisible = false
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    load(pageNum) {
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/information/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
          recommend: this.recommend,
        }
      }).then(res => {
        this.tableData = res.data?.list
        this.total = res.data?.total
      })
    },
    reset() {
      this.name = null
      this.recommend = null
      this.load(1)
    },
    handleCurrentChange(pageNum) {
      this.load(pageNum)
    },
  }
}
</script>

<style scoped>

</style>