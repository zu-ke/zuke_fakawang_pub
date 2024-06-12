<template>
  <div id="body_div">
    <div id="max_div">
      <el-form
          :label-position="Top"
          label-width="100px"
          :model="formLabelAlign"
          style="max-width: 460px;"
      >
        <el-form-item label="账号">
          <el-input v-model="login.username"/>
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="login.password"/>
        </el-form-item>
      </el-form>
      <el-button @click="go" class="btn" type="primary">登录</el-button>
      <el-button @click="this.$router.push('/Register')" class="btn" type="default">注册</el-button>
    </div>
  </div>
</template>

<script>
import request from "@/utils/request";
import { host } from "@/assets/js/value";

export default {
  name: "Login",
  data() {
    return {
      login: {},
    };
  },
  methods: {
    go(){
      if (!this.login.username || !this.login.password){
        this.$message({
          type: "error",
          message: "请正确填写账号密码以及邀请码"
        })
        return false;
      }
      request.post(host + "admin/login",this.login).then(res => {
        if (res.code === "200"){
          this.$router.push('/products');
          this.$message({
            type: "success",
            message: "登陆成功"
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
  left: 35%;
  top: 40%;
}

.btn {
margin-left: 125px;
}
</style>
