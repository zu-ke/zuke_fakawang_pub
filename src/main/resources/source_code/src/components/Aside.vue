<template>
  <!--解读：内容部分的侧边栏部分-->
  <div>
    <el-row class="tac">
      <el-col :span="12">
        <el-menu
            :default-active="activeMenu"
            class="el-menu-vertical-demo"
            :ellipsis="false"
            @select="handleSelect"
            :router="true"
            style="width: max-content"
        >
          <el-menu-item index="/products">
            <el-icon><document/></el-icon>
            <span>商品管理</span>
          </el-menu-item>

          <el-menu-item index="/orders">
            <el-icon><document/></el-icon>
            <span>订单记录</span>
          </el-menu-item>

          <el-menu-item index="/settings">
            <el-icon><setting/></el-icon>
            <span>公告设置</span>
          </el-menu-item>
        </el-menu>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: "Aside",
  computed: {
    // 计算属性用于获取当前路由的路径
    activeMenu() {
      return this.$route.path;
    }
  },
  methods: {
    handleSelect(selectedIndex) {
      // 处理菜单项的选择
      console.log("Selected menu item:", selectedIndex);
    }
  }
}
</script>
