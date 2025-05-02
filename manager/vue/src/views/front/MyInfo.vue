<template>
  <div class="main-content">
    <div style="width: 80%; margin: 30px auto">
      <div style="margin: 20px 0">
        <el-input placeholder="Search" style="width: 200px" size="mini" v-model="name"></el-input>
        <el-button type="info" plain style="margin-left: 10px" size="mini" @click="load(1)">Search</el-button>
        <el-button type="warning" plain style="margin-left: 10px" size="mini" @click="reset">Reset</el-button>
        <el-button type="primary" plain size="mini" @click="handleAdd">Post Course</el-button>
        <el-button type="danger" plain size="mini" @click="delBatch">Delete</el-button>
      </div>

      <div class="table">
        <el-table :data="tableData" stripe  @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="55" align="center"></el-table-column>
<!--          <el-table-column prop="id" label="ID" width="80" align="center" sortable></el-table-column>-->
          <el-table-column prop="img" label="Cover" width="100">
            <template v-slot="scope">
              <div style="display: flex; align-items: center">
                <el-image style="width: 60px; height: 40px; border-radius: 10px" v-if="scope.row.img"
                          :src="scope.row.img" :preview-src-list="[scope.row.img]"></el-image>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="name" label="Name" width="200" show-overflow-tooltip></el-table-column>
          <el-table-column prop="userName" label="Poster"></el-table-column>
          <el-table-column prop="time" label="Post date"></el-table-column>
          <el-table-column prop="recommend" label="Recommend"></el-table-column>
          <el-table-column prop="score" label="Score"></el-table-column>
          <el-table-column prop="status" label="Status"></el-table-column>
          <el-table-column prop="descr" label="Respond"></el-table-column>

          <el-table-column label="Operation" width="180" align="center">
            <template v-slot="scope">
              <el-button plain type="primary" @click="handleEdit(scope.row)" size="mini">Edit</el-button>
              <el-button plain type="danger" size="mini" @click=del(scope.row.id)>Delete</el-button>
            </template>
          </el-table-column>
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

    <el-dialog title="Course Info" :visible.sync="fromVisible" width="55%" :close-on-click-modal="false" destroy-on-close>
      <el-form label-width="100px" style="padding-right: 50px" :model="form" :rules="rules" ref="formRef">
        <el-form-item label="Cover">
          <el-upload
              class="avatar-uploader"
              :action="$baseUrl + '/files/upload'"
              :headers="{ token: user.token }"
              list-type="picture"
              :on-success="handleImgSuccess"
          >
            <el-button type="primary">Upload</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item prop="name" label="Name">
          <el-input v-model="form.name" autocomplete="off" placeholder="Name"></el-input>
        </el-form-item>
        <el-form-item prop="score" label="Score">
          <el-input v-model="form.score" autocomplete="off" placeholder="Required Score"></el-input>
        </el-form-item>
        <el-form-item prop="file" label="File link">
          <el-input v-model="form.file" autocomplete="off" placeholder="Link"></el-input>
        </el-form-item>
        <el-form-item prop="content" label="Course content">
          <div id="editor"></div>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="fromVisible = false">Cancel</el-button>
        <el-button type="primary" @click="save">Save</el-button>
      </div>
    </el-dialog>

    <el-dialog title="Course Description" :visible.sync="editorVisible" width="55%" :close-on-click-modal="false" destroy-on-close>
      <div v-html="viewData"></div>
    </el-dialog>

  </div>
</template>
<script>
import E from 'wangeditor'
export default {
  data() {
    return {
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      editor: null,
      tableData: [],
      pageNum: 1,
      pageSize: 10,
      total: 0,
      name: null,
      recommend: null,
      fromVisible: false,
      editorVisible: false,
      form: {},
      rules: {
        name: [
          {required: true, message: 'Name is required', trigger: 'blur'},
        ]
      },
      ids: []
    }
  },
  mounted() {
    this.load(1)
  },
  methods: {
    initWangEditor(content) {
      this.$nextTick(() => {
        this.editor = new E('#editor')
        this.editor.config.placeholder = 'Please enter content'
        this.editor.config.uploadFileName = 'file'
        this.editor.config.uploadImgServer = 'http://localhost:8080/files/editor/upload'
        this.editor.create()
        setTimeout(() => {
          this.editor.txt.html(content)
        })
      })
    },
    handleAdd() {
      this.form = {
        userId: this.user.id,
        status: 'Processing'
      }
      this.fromVisible = true
      this.initWangEditor('')
    },
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.fromVisible = true
      this.initWangEditor(this.form.content || '')
    },
    save() {
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          this.form.content = this.editor.txt.html()
          this.$request({
            url: this.form.id ? '/information/update' : '/information/add',
            method: this.form.id ? 'PUT' : 'POST',
            data: this.form
          }).then(res => {
            if (res.code === '200') {
              this.$message.success('Success')
              this.load(1)
              this.fromVisible = false
            } else {
              this.$message.error(res.msg)
            }
          })
        }
      })
    },
    del(id) {
      this.$confirm('Confirm', 'Confirm', {type: "warning"}).then(response => {
        this.$request.delete('/information/delete/' + id).then(res => {
          if (res.code === '200') {
            this.$message.success('Success')
            this.load(1)
          } else {
            this.$message.error(res.msg)
          }
        })
      }).catch(() => {
      })
    },
    delBatch() {
      if (!this.ids.length) {
        this.$message.warning('Select data')
        return
      }
      this.$confirm('Confirm', 'Confirm', {type: "warning"}).then(response => {
        this.$request.delete('/information/delete/batch', {data: this.ids}).then(res => {
          if (res.code === '200') {
            this.$message.success('Success')
            this.load(1)
          } else {
            this.$message.error(res.msg)
          }
        })
      }).catch(() => {
      })
    },
    load(pageNum) {
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/information/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
          userId: this.user.id
        }
      }).then(res => {
        this.tableData = res.data?.list
        this.total = res.data?.total
      })
    },
    handleCurrentChange(pageNum) {
      this.load(pageNum)
    },
    handleSelectionChange(rows) {
      this.ids = rows.map(v => v.id)
    },
    handleImgSuccess(res) {
      this.form.img = res.data
    },
    reset() {
      this.name = null
      this.load(1)
    },
    viewDataInit(data){
      this.viewData = data
      this.editorVisible = true
    },
  }
}
</script>