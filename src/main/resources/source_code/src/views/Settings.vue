<template>
  <div>
    <!--头部-->
    <!--解读：引用组件顶部导航栏-->
    <Header/>

    <!--内容-->
    <div style="display: flex">
      <!--解读：引用组件内容侧边栏-->
      <Aside/>
      <!--解读：展示的内容，从views/xxx 比如HomeView.vue组件来的（通过路由机制）-->
      <div style="flex: 1;margin: 10px">
        <el-input
            v-model="Ancmt.content"
            :rows="5"
            type="textarea"
            placeholder="Please input"
        />
        <el-button @click="upAncmt" type="primary" style="position:absolute;left: 94%;bottom: 47%">保存</el-button>
      </div>
    </div>
  </div>
</template>

<script>
import request from '@/utils/request'
import Header from "@/components/Header.vue";
import Aside from "@/components/Aside.vue";
import { host } from "@/assets/js/value";

export default {
  name: "Register",
  components: {Aside, Header},
  data() {
    return {
      Ancmt:{
        id: "1",
        content: ""
      }
    };
  },
  mounted() {
    request.get(host + "admin/getAncmt").then(res => {
      this.Ancmt.content = res.data.content;
    });
  },
  methods: {
    upAncmt(){
      request.post(host + "admin/upAncmt",this.Ancmt).then(res => {
        if (res.code === "200"){
          this.$message({
            type: "success",
            message: "修改成功"
          })
        }else {
          this.$message({
            type: "error",
            message: res.msg ? res.msg : "error"
          })
        }
      });
    }
  }
};
</script>

<style>
</style>
