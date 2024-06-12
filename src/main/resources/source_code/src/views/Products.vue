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
        <el-button style="margin: 5px 0px 5px 10px" type="primary" @click="getAllPd">全部商品</el-button>
        <el-dropdown style="margin: 5px 5px 5px 10px" split-button type="primary" @click="handleClick">
          {{ selectedCategoryName1 || '全部商品' }}
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item v-for="(option, index) in category" :key="index"
                                @click="handleDropdownItemClick(option)">
                <div style="display: flex; justify-content: space-between; align-items: center;">
                  <span>{{ option.name }}</span>
                  <span>
              <el-button @click.stop.prevent="handleEditCtgr(option)" type="text" size="small">编辑</el-button>
              <el-popconfirm title="你确定删除吗?" @confirm.stop.prevent="delCtgr(option.id)">
                <template #reference>
                  <el-button type="text" size="small">删除</el-button>
                </template>
              </el-popconfirm>
            </span>
                </div>
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
        <el-button style="margin: 5px" type="primary" @click="addCtgr">新增分类</el-button>
        <el-button style="margin: 5px" type="primary" @click="addPd">新增商品</el-button>

        <!--解读：引入搜索框-->
        <div style="margin: 5px 5px 5px 10px">
          <el-input v-model="search" placeholder="请输入商品名称" style="width: 30%;"></el-input>
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
            <el-table-column prop="name" label="商品名">
              <template v-slot="scope">
                <el-tooltip class="item" effect="dark" :content="scope.row.name" placement="top">
                  <div style="overflow: hidden; text-overflow: ellipsis; white-space: nowrap; max-width: 150px;">
                    {{ scope.row.name }}
                  </div>
                </el-tooltip>
              </template>
            </el-table-column>
            <el-table-column prop="categoryName" label="分类名">
              <template v-slot="scope">
                <el-tooltip class="item" effect="dark" :content="scope.row.categoryName" placement="top">
                  <div style="overflow: hidden; text-overflow: ellipsis; white-space: nowrap; max-width: 150px;">
                    {{ scope.row.categoryName }}
                  </div>
                </el-tooltip>
              </template>
            </el-table-column>
            <el-table-column prop="price" label="价格">
              <template v-slot="scope">
                <el-tooltip class="item" effect="dark" :content="scope.row.price" placement="top">
                  <div style="overflow: hidden; text-overflow: ellipsis; white-space: nowrap; max-width: 150px;">
                    {{ scope.row.price }}
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
            <el-table-column prop="gptKey" label="卡密">
              <template v-slot="scope">
                <el-tooltip class="item" effect="dark" :content="scope.row.gptKey" placement="top">
                  <div style="overflow: hidden; text-overflow: ellipsis; white-space: nowrap; max-width: 150px;">
                    {{ scope.row.gptKey }}
                  </div>
                </el-tooltip>
              </template>
            </el-table-column>

            <el-table-column fixed="right" label="操作" width="100">
              <template #default="scope">
                <el-button @click="handleEditPd(scope.row)" type="text">编辑</el-button>
                <!--解读：加入确认操作的确认框-->
                <el-popconfirm title="你确定删除吗?" @confirm="del(scope.row.id)">
                  <template #reference>
                    <el-button type="text">删除</el-button>
                  </template>
                </el-popconfirm>
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

    <!--解读：添加和编辑分类的表单-->
    <el-dialog
        v-model="dialogVisibleCtgr"
        title="分类"
        width="30%"
        :before-close="handleClose"
    >
      <el-form :model="ctgr" label-width="120px">
        <el-form-item label="分类名" prop="name">
          <el-input v-model="ctgr.name" style="width: 80%"/>
        </el-form-item>

        <el-form-item label="价格" prop="price">
          <el-input v-model="ctgr.price" style="width: 80%"/>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="saveCtgr">确定</el-button>
          <el-button @click="dialogVisibleCtgr = false">取消</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>

    <!--解读：添加和编辑商品的表单-->
    <el-dialog v-model="dialogVisiblPd" title="商品" width="30%" :before-close="handleClose">
      <el-form :model="pd" label-width="120px">

        <el-form-item label="分类" prop="categoryId">
          <el-dropdown>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item v-for="(option, index) in category" :key="index"
                                  @click="handleDropdownItemClick2(option)">
                  {{ option.name }}
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
            <el-button type="button">{{ selectedCategoryName || '选择分类' }}<i
                class="el-icon-arrow-down el-icon--right"></i></el-button>
          </el-dropdown>
        </el-form-item>

        <el-form-item label="商品名" prop="name">
          <el-input v-model="pd.name" style="width: 80%"/>
        </el-form-item>

        <el-form-item label="价格" prop="price">
          <el-input v-model="pd.price" style="width: 80%"/>
        </el-form-item>

        <el-form-item label="兑换码" prop="gptKey">
          <el-input v-model="pd.gptKey" style="width: 80%"/>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="savePd">确定</el-button>
          <el-button @click="dialogVisiblPd = false">取消</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>

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
      selectedCategoryName1: "",
      selectedCategoryName: "",
      pd: {},
      dialogVisiblPd: false,
      ctgr: {},
      dialogVisibleCtgr: false,
      search: "",
      categoryId: "",
      total: 0,
      pageSize: 10, //每页显示
      currentPage: 1, //当前页
      category: [],
      tableData: []
    };
  },
  mounted() {
    this.list();
    this.listCtgr();
  },
  methods: {
    /*解读：新增或者修改商品*/
    savePd() {
      if (this.pd.id) {
        /*解读：修改*/
        request.post(host + "admin/upPdct", this.pd).then(res => {
          this.dialogVisiblPd = false;
          this.listCtgr();
          if (res.code == "200") {
            this.$message({
              type: "success",
              message: res.msg ? res.msg : "success"
            })
          } else {
            this.$message({
              type: "error",
              message: res.msg ? res.msg : "error"
            })
          }
          this.list();
        })
      } else {
        /*解读：新增*/
        request.post(host + "admin/savePdct", this.pd).then(res => {
          this.dialogVisiblPd = false;
          this.listCtgr();
          if (res.code == "200") {
            this.$message({
              type: "success",
              message: res.msg ? res.msg : "success"
            })
          } else {
            this.$message({
              type: "error",
              message: res.msg ? res.msg : "error"
            })
          }
          this.list();
        })
      }
    },

    /*解读：编辑商品中查询当前商品信息*/
    handleEditPd(row) {
      request.get(host + "admin/listByidPdct?pid=" + row.id).then(res => {
        this.pd = res.data;
        this.dialogVisiblPd = true;
      });
    },

    /*解读：打开分类对话框*/
    addPd() {
      this.dialogVisiblPd = true;
      this.pd = {};
      this.selectedCategoryName = "";
    },

    /*解读：编辑分类中查询当前分类信息*/
    handleEditCtgr(row) {
      request.get(host + "admin/getByIdCtg?cid=" + row.id).then(res => {
        this.ctgr = res.data;
        this.dialogVisibleCtgr = true;
      });
    },

    /*解读：新增或者修改分类*/
    saveCtgr() {
      if (this.ctgr.id) {
        /*解读：修改*/
        request.post(host + "admin/upCtg", this.ctgr).then(res => {
          this.dialogVisibleCtgr = false;
          this.listCtgr();
          if (res.code == "200") {
            this.$message({
              type: "success",
              message: res.msg ? res.msg : "success"
            })
          } else {
            this.$message({
              type: "error",
              message: res.msg ? res.msg : "error"
            })
          }
          this.list();
        })
      } else {
        /*解读：新增*/
        request.post(host + "admin/saveCtg", this.ctgr).then(res => {
          this.dialogVisibleCtgr = false;
          this.listCtgr();
          if (res.code == "200") {
            this.$message({
              type: "success",
              message: res.msg ? res.msg : "success"
            })
          } else {
            this.$message({
              type: "error",
              message: res.msg ? res.msg : "error"
            })
          }
          this.list();
        })
      }
    },

    /*解读：打开分类对话框*/
    addCtgr() {
      this.dialogVisibleCtgr = true;
      this.ctgr = {};
    },

    /*解读：删除分类请求*/
    delCtgr(id) {
      request.post(host + "admin/delCtg?cid=" + id).then(res => {
        if (res.code == "200") {
          this.$message({
            type: "success",
            message: res.msg ? res.msg : "success"
          })
        } else {
          this.$message({
            type: "error",
            message: res.msg ? res.msg : "error"
          })
        }
        this.list();
      })
    },

    /*解读：删除商品请求*/
    del(id) {
      request.post(host + "admin/delPdct?pid=" + id).then(res => {
        if (res.code == "200") {
          this.$message({
            type: "success",
            message: res.msg ? res.msg : "success"
          })
        } else {
          this.$message({
            type: "error",
            message: res.msg ? res.msg : "error"
          })
        }
        this.list();
      })
    },

    /*获取所有分类的所有数据*/
    list() {
      request.get(host + "admin/listByPagePdct", {
        params: {
          pageSize: this.pageSize,
          pageNum: this.currentPage,
          categoryId: this.categoryId,
          searchName: this.search
        }
      }).then(res => {
        this.tableData = res.data.records;
        this.total = res.data.total;
      });
    },

    /*解读：获取所有商品，取消选择的分类*/
    getAllPd() {
      this.selectedCategoryName1 = "";
      this.categoryId = "";
      this.list();
    },

    /*获取下拉菜单的分类选项*/
    listCtgr() {
      request.get(host + "admin/listCtg").then(res => {
        this.category = res.data;
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

    /*解读：获取下拉框选中的部分*/
    handleDropdownItemClick(option) {
      this.selectedCategoryName1 = option.name;
      this.categoryId = option.id;
      this.list();
    },

    /*解读：获取新增或者修改商品表单中下拉框选中的部分*/
    handleDropdownItemClick2(option) {
      // 设置选中的分类名称
      this.selectedCategoryName = option.name;
      this.pd.categoryId = option.id;
      this.pd.name = option.name;
      this.pd.price = option.price;
    }
  }
};
</script>

<style>

</style>
