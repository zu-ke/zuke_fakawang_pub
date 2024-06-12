<template>
  <!--解读：顶部导航栏-->
  <div style="height: 50px;line-height: 50px;border-bottom: 1px solid #ccc;display: flex">
    <div @click="this.$router.push('/admin')" style="width: 200px; padding-left: 30px;font-weight: bold;color: dodgerblue">zk 商品后台管理</div>
    <div style="flex: 1"></div>
  </div>
</template>
<style>
</style>
<script>
export default {
  name: "Header"
}
</script>