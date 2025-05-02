<template>
  <div class="main-content">
    <el-card style="width: 50%; margin: 30px auto">
      <div style="text-align: right; margin-bottom: 20px">
        <el-button type="primary" @click="updatePassword">UpdatePassword</el-button>
        <el-button type="primary" @click="initRecharge">Recharge</el-button>
      </div>
      <el-form :model="user" label-width="80px" style="padding-right: 20px">
        <div style="margin: 15px; text-align: center">
          <el-upload
              class="avatar-uploader"
              :action="$baseUrl + '/files/upload'"
              :show-file-list="false"
              :on-success="handleAvatarSuccess"
          >
            <img v-if="user.avatar" :src="user.avatar" class="avatar" />
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </div>
        <el-form-item label="Username" prop="username">
          <el-input v-model="user.username" placeholder="Username" disabled></el-input>
        </el-form-item>
        <el-form-item label="Name" prop="name">
          <el-input v-model="user.name" placeholder="Name"></el-input>
        </el-form-item>
        <el-form-item label="Phone" prop="phone">
          <el-input v-model="user.phone" placeholder="phone"></el-input>
        </el-form-item>
        <el-form-item label="Email" prop="email">
          <el-input v-model="user.email" placeholder="email"></el-input>
        </el-form-item>
        <el-form-item label="Scores" prop="score">
          <el-input v-model="user.score" placeholder="score" disabled></el-input>
        </el-form-item>
        <div style="text-align: center; margin-bottom: 20px">
          <el-button type="primary" @click="update">Save</el-button>
        </div>
      </el-form>
    </el-card>
    <el-dialog title="Edit Password" :visible.sync="dialogVisible" width="40%" :close-on-click-modal="false" destroy-on-close>
      <el-form :model="user" label-width="140px" style="padding-right: 20px" :rules="rules" ref="formRef">
        <el-form-item label="Origin password" prop="password">
          <el-input show-password v-model="user.password" placeholder="Password"></el-input>
        </el-form-item>
        <el-form-item label="New password" prop="newPassword">
          <el-input show-password v-model="user.newPassword" placeholder="Password"></el-input>
        </el-form-item>
        <el-form-item label="ConfirmPassword" prop="confirmPassword">
          <el-input show-password v-model="user.confirmPassword" placeholder="ConfirmPassword"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="fromVisible = false">Cancel</el-button>
        <el-button type="primary" @click="save">Save</el-button>
      </div>
    </el-dialog>
    <el-dialog title="Recharge" :visible.sync="rechargeVisible" width="40%" :close-on-click-modal="false" destroy-on-close>
      <el-form label-width="130px" style="padding-right: 20px">
        <el-form-item label="Current balance:" >
          <span style="color: red"> {{user.score}}</span>
        </el-form-item>
        <el-form-item label="Recharge amount:">
          <el-input v-model="account" placeholder="Please enter"></el-input>
        </el-form-item>
        <el-form-item label="Payment method:">
          <el-radio v-model="type" label="Credit card">Pay by credit card</el-radio>
          <el-radio v-model="type" label="Google pay">Google pay</el-radio>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="rechargeVisible = false">Cancel</el-button>
        <el-button type="primary" @click="recharge">Save</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  data() {
    const validatePassword = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('Please Confirm Password'))
      } else if (value !== this.user.newPassword) {
        callback(new Error('Password do not match'))
      } else {
        callback()
      }
    }
    return {
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      dialogVisible: false,
      rechargeVisible: false,
      account: null,
      type: 'Credit card',
      rules: {
        password: [
          { required: true, message: 'Please enter origin password', trigger: 'blur' },
        ],
        newPassword: [
          { required: true, message: 'Please enter new password', trigger: 'blur' },
        ],
        confirmPassword: [
          { validator: validatePassword, required: true, trigger: 'blur' },
        ],
      }
    }
  },
  created() {

  },
  methods: {
    initRecharge() {
      this.account = 100
      this.rechargeVisible = true
    },
    recharge() {
      this.$request.get('/user/recharge?account=' + this.account).then(res => {
        if (res.code === '200') {
          this.$message.success('Successfully recharged, please log in again to see the change')

          this.rechargeVisible = false
        } else {
          this.$message.error(res.msg)
        }
      })
    },

    update() {

      this.$request.put('/user/update', this.user).then(res => {
        if (res.code === '200') {

          this.$message.success('Successfully updated')

          localStorage.setItem('xm-user', JSON.stringify(this.user))

          this.$emit('update:user')
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    handleAvatarSuccess(response, file, fileList) {

      this.$set(this.user, 'avatar', response.data)
    },

    updatePassword() {
      this.dialogVisible = true
    },
    save() {
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          this.$request.put('/updatePassword', this.user).then(res => {
            if (res.code === '200') {

              this.$message.success('Successfully updated')
              this.$router.push('/login')
            } else {
              this.$message.error(res.msg)
            }
          })
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