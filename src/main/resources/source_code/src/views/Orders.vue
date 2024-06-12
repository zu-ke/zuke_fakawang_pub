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
      <div style="flex: 1">
        <!--解读：引入搜索框-->
        <div style="margin: 5px 5px 5px 10px">
          <el-input v-model="search" placeholder="请输入邮箱" style="width: 30%;"></el-input>
          <el-button @click="list" style="margin-left: 10px" type="primary">查询</el-button>
        </div>

        <div style="width: 100%;">
          <!--解读：表格部分-->
          <el-table :data="tableData" height="500" style="width: 100%">
            <el-table-column prop="id" label="ID">
              <template v-slot="scope">
                <el-tooltip class="item" effect="dark" :content="scope.row.id" placement="top">
                  <div style="overflow: hidden; text-overflow: ellipsis; white-space: nowrap; max-width: 150px;">
                    {{ scope.row.id }}
                  </div>
                </el-tooltip>
              </template>
            </el-table-column>
            <el-table-column prop="tradeNo" label="订单号">
              <template v-slot="scope">
                <el-tooltip class="item" effect="dark" :content="scope.row.tradeNo" placement="top">
                  <div style="overflow: hidden; text-overflow: ellipsis; white-space: nowrap; max-width: 150px;">
                    {{ scope.row.tradeNo }}
                  </div>
                </el-tooltip>
              </template>
            </el-table-column>
            <el-table-column prop="outTradeNo" label="商户订单号">
              <template v-slot="scope">
                <el-tooltip class="item" effect="dark" :content="scope.row.outTradeNo" placement="top">
                  <div style="overflow: hidden; text-overflow: ellipsis; white-space: nowrap; max-width: 150px;">
                    {{ scope.row.outTradeNo }}
                  </div>
                </el-tooltip>
              </template>
            </el-table-column>
            <!-- 使用template插槽定义“邮箱”列 -->
            <el-table-column prop="name" label="邮箱">
              <template v-slot="scope">
                <el-tooltip class="item" effect="dark" :content="scope.row.name" placement="top">
                  <div style="overflow: hidden; text-overflow: ellipsis; white-space: nowrap; max-width: 150px;">
                    {{ scope.row.name }}
                  </div>
                </el-tooltip>
              </template>
            </el-table-column>
            <el-table-column prop="name" label="邮箱">
              <template v-slot="scope">
                <el-tooltip class="item" effect="dark" :content="scope.row.name" placement="top">
                  <div style="overflow: hidden; text-overflow: ellipsis; white-space: nowrap; max-width: 150px;">
                    {{ scope.row.name }}
                  </div>
                </el-tooltip>
              </template>
            </el-table-column>
            <el-table-column prop="amount" label="金额">
              <template v-slot="scope">
                <el-tooltip class="item" effect="dark" :content="scope.row.amount" placement="top">
                  <div style="overflow: hidden; text-overflow: ellipsis; white-space: nowrap; max-width: 150px;">
                    {{ scope.row.amount }}
                  </div>
                </el-tooltip>
              </template>
            </el-table-column>
            <el-table-column prop="productId" label="卡密">
              <template v-slot="scope">
                <el-tooltip class="item" effect="dark" :content="scope.row.productId" placement="top">
                  <div style="overflow: hidden; text-overflow: ellipsis; white-space: nowrap; max-width: 150px;">
                    {{ scope.row.productId }}
                  </div>
                </el-tooltip>
              </template>
            </el-table-column>
            <el-table-column prop="status" label="支付状态1">
              <template v-slot="scope">
                <el-tooltip class="item" effect="dark" :content="scope.row.status" placement="top">
                  <div style="overflow: hidden; text-overflow: ellipsis; white-space: nowrap; max-width: 150px;">
                    {{ scope.row.status }}
                  </div>
                </el-tooltip>
              </template>
            </el-table-column>
            <el-table-column prop="tradeStatus" label="支付状态2">
              <template v-slot="scope">
                <el-tooltip class="item" effect="dark" :content="scope.row.tradeStatus" placement="top">
                  <div style="overflow: hidden; text-overflow: ellipsis; white-space: nowrap; max-width: 150px;">
                    {{ scope.row.tradeStatus }}
                  </div>
                </el-tooltip>
              </template>
            </el-table-column>
            <el-table-column prop="createTime" label="创建时间">
              <template v-slot="scope">
                <el-tooltip class="item" effect="dark" :content="scope.row.createTime" placement="top">
                  <div style="overflow: hidden; text-overflow: ellipsis; white-space: nowrap; max-width: 150px;">
                    {{ scope.row.createTime }}
                  </div>
                </el-tooltip>
              </template>
            </el-table-column>
            <el-table-column prop="updateTime" label="更新时间">
              <template v-slot="scope">
                <el-tooltip class="item" effect="dark" :content="scope.row.updateTime" placement="top">
                  <div style="overflow: hidden; text-overflow: ellipsis; white-space: nowrap; max-width: 150px;">
                    {{ scope.row.updateTime }}
                  </div>
                </el-tooltip>
              </template>
            </el-table-column>
          </el-table>

        </div>
      </div>
    </div>

    <!--解读：分页-->
    <div style="margin-left: 1000px;margin-top: 10px">
      <el-pagination
          background
          layout="prev, pager, next,total,sizes,jumper"
          :total="total"
          @size-change="handlePageSizeChange"
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-sizes="[5,10]"
          :page-size=pageSize></el-pagination>
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
      search: "",
      tableData: [],
      total: 0,
      pageSize: 10, //每页显示
      currentPage: 1 //当前页
    };
  },
  mounted() {
    this.list();
  },
  methods: {
    /*解读：查询所有订单*/
    list() {
      request.get(host + "admin/listO", {
        params: {
          pageSize: this.pageSize,
          pageNum: this.currentPage,
          searchName: this.search
        }
      }).then(res => {
        console.log(res.data.records);
        this.tableData = res.data.records;
        this.total = res.data.total;
      });
    },

    /*解读：处理修改currentPage*/
    handleCurrentChange(currentPage) {
      this.currentPage = currentPage;
      this.list(); //刷新家居列表
    },

    /*解读：处理修改pageSize*/
    handlePageSizeChange(pageSize) {
      this.pageSize = pageSize;
      this.list(); //刷新家居列表
    },
  }
};
</script>

<style>

</style>
