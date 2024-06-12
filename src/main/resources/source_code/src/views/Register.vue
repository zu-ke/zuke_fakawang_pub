<template>
  <div id="body_div">
    <div id="max_div">
      <el-form
          :label-position="Top"
          label-width="100px"
          :model="register"
          style="max-width: 460px;"
      >
        <el-form-item label="账号">
          <el-input v-model="register.username"/>
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="register.password"/>
        </el-form-item>
        <el-form-item label="邀请码（必填）">
          <el-input v-model="register.yqm"/>
        </el-form-item>
      </el-form>
      <el-button @click="rgst" class="btn" type="primary">注册</el-button>
      <el-button @click="this.$router.push('/')" class="btn" type="default">返回</el-button>
    </div>
  </div>
</template>

<script>
import request from '@/utils/request'
import { host } from "@/assets/js/value";
export default {
  name: "Register",
  data() {
    return {
      register: {},
    };
  },
  methods: {
    rgst() {
      if (!this.register.username || !this.register.password || !this.register.yqm){
        this.$message({
          type: "error",
          message: "请正确填写账号密码以及邀请码"
        })
        return false;
      }
      request.post(host + "admin/register",this.register).then(res => {
        if (res.code === "200"){
          this.$router.push('/');
          this.$message({
            type: "success",
            message: "注册成功，请登录"
          })
        }else {
          this.$message({
            type: "error",
            message: res.msg ? res.msg : "error"
          })
        }
      })
    }
  }
};
</script>

<style scoped>
#body_div {
  position: absolute;
  width: 100%;
  height: 100%;
  background-image: url('@/assets/images/background.png'); /* 设置背景图片 */
  background-size: cover; /* 覆盖整个容器 */
  background-repeat: no-repeat; /* 防止图片重复 */
}

#max_div {
  position: absolute;
  left: 38%;
  top: 40%;
}

#max_div .el-form-item__label {
  color: black !important;
}

.btn {
  margin-left: 150px;
}
</style>
