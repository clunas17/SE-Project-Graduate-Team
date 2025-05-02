<template>
  <div>
    <div class="search">
      <el-input placeholder="Search" style="width: 200px" v-model="name"></el-input>
      <el-select v-model="recommend" placeholder="Recommended" style="width: 200px; margin-left: 10px">
        <el-option label="Yes" value="Yes"></el-option>
        <el-option label="No" value="No"></el-option>
      </el-select>
      <el-button type="info" plain style="margin-left: 10px" @click="load(1)">Search</el-button>
      <el-button type="warning" plain style="margin-left: 10px" @click="reset">Reset</el-button>
    </div>

    <div class="operation">
      <el-button type="primary" plain @click="handleAdd">New</el-button>
      <el-button type="danger" plain @click="delBatch">DeleteBatch</el-button>
    </div>

    <div class="table">
      <el-table :data="tableData" stripe  @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center"></el-table-column>
        <el-table-column prop="id" label="id" width="80" align="center" sortable></el-table-column>
        <el-table-column label="Course cover">
          <template v-slot="scope">
            <div style="display: flex; align-items: center">
              <el-image style="width: 60px; height: 40px; border-radius: 10px" v-if="scope.row.img"
                        :src="scope.row.img" :preview-src-list="[scope.row.img]"></el-image>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="name" label="Course name" show-overflow-tooltip></el-table-column>
        <el-table-column prop="content" label="Description" show-overflow-tooltip>
          <template v-slot="scope" >
            <el-button type="success" plain size="mini" @click="viewDataInit(scope.row.content)" >View</el-button>
          </template>
        </el-table-column>
        <el-table-column prop="type" label="Course type"></el-table-column>
        <el-table-column prop="price" label="Price"></el-table-column>

        <el-table-column prop="file" label="Profile" show-overflow-tooltip></el-table-column>
        <el-table-column prop="recommend" label="Recommended"></el-table-column>
        <el-table-column prop="level" label="Level"></el-table-column>



        <el-table-column label="Operation" width="180" align="center">
          <template v-slot="scope">
            <el-button plain type="primary" @click="handleEdit(scope.row)" size="mini">edit</el-button>
            <el-button plain type="danger" size="mini" @click=del(scope.row.id)>delete</el-button>
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


    <el-dialog title="New Course" :visible.sync="fromVisible" width="55%" :close-on-click-modal="false" destroy-on-close>
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
        <el-form-item prop="name" label="Course name">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item prop="type" label="Course type">
          <el-select v-model="form.type" placeholder="Select type" style="width: 100%">
            <el-option label="Web Development" value="WD"></el-option>
            <el-option label="Programming Language" value="PL"></el-option>
            <el-option label="DevOps Foundations" value="DF"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item prop="type" label="Course level">
          <el-select v-model="form.level" placeholder="Select level" style="width: 100%">
            <el-option label="Beginner" value="BEGINNER"></el-option>
            <el-option label="Intermediate" value="INTERMEDIATE"></el-option>
            <el-option label="Advanced" value="ADVANCED"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item prop="recommend" label="Recommend">
          <el-select v-model="form.recommend" placeholder="Select" style="width: 100%">
            <el-option label="Yes" value="Yes"></el-option>
            <el-option label="No" value="No"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item prop="price" label="Price" placeholder="Price(0 refers to opened)">
          <el-input v-model="form.price" autocomplete="off"></el-input>
        </el-form-item>

        <el-form-item label="Video">
          <el-upload
              class="avatar-uploader"
              :action="$baseUrl + '/files/upload'"
              :headers="{ token: user.token }"
              :on-success="handleVideoSuccess"
          >
            <el-button type="primary">Upload(if applicable)</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item prop="file" label="File link">
          <el-input v-model="form.file" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item prop="content" label="Description">
          <div id="editor">

          </div>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="fromVisible = false">cancel</el-button>
        <el-button type="primary" @click="save">save</el-button>
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
  name: "Course",
  data() {
    return {
      editor: null,
      tableData: [],
      pageNum: 1,
      pageSize: 5,
      total: 0,
      name: null,
      recommend: null,
      fromVisible: false,
      editorVisible: false,
      form: {},
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      rules: {
        name: [
          {required: true, message: 'A name is required', trigger: 'blur'},
        ],
        type: [
          {required: true, message: 'A type is required', trigger: 'blur'},
        ],
        recommend: [
          {required: true, message: 'Recommend status is required', trigger: 'blur'},
        ],
        level: [
          {required: true, message: 'Level is required', trigger: 'blur'},
        ],
        price: [
          {required: true, message: 'Price is required', trigger: 'blur'},
        ],
      },
      ids: [],
      viewData: null,
    }
  },
  created() {
    this.load(1)
  },
  methods: {
    initEditor(content) {
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
      this.form = {}
      this.fromVisible = true
      this.initEditor('')
    },
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.fromVisible = true
      this.initEditor(this.form.content||'')
    },
    save() {
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          this.form.content = this.editor.txt.html()
          this.$request({
            url: this.form.id ? '/course/update' : '/course/add',
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
        this.$request.delete('/course/delete/' + id).then(res => {
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
    handleSelectionChange(rows) {
      this.ids = rows.map(v => v.id)
    },
    delBatch() {
      if (!this.ids.length) {
        this.$message.warning('Select data')
        return
      }
      this.$confirm('Confirm', 'Confirm', {type: "warning"}).then(response => {
        this.$request.delete('/course/delete/batch', {data: this.ids}).then(res => {
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
      this.$request.get('/course/selectPage', {
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
    handleImgSuccess(res) {
      this.form.img = res.data
    },
    handleVideoSuccess(res) {
      this.form.video = res.data
    },
    down(url) {
      location.href = url
    },
    viewDataInit(data){
      this.viewData = data
      this.editorVisible = true
    },

  }
}
</script>

<style scoped>

</style>
