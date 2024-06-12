<template>
  <div id="body">

    <!-- 只在PC端显示 -->
    <div id="pc" v-show="!isMobile">
      <div id="pcbody">
        <h2 style="color: black;margin-left: 20px;margin-top: 20px">租客的小商铺</h2>
        <p id="p1" style="width: 800px;height: 160px;font-size: 15px">{{ content }}</p>
        <div style="margin-left: 20px">
          <el-button size="large" type="info">请选择商品购买</el-button>
          <div v-for="(group, index) in groupedCtgr" :key="'group' + index">
            <div v-for="(item, idx) in group" :key="idx" style="width: 200px; height: 180px; background-color: white; position: relative; display: inline-block;margin-top: 30px" :style="{ marginRight: idx !== 2 ? '100px' : '0' }">
              <!--<img style="width: 198px; margin: 0; padding: 0" src="../assets/images/gpt.png">-->
              <p style="width: 198px; margin: 0; padding: 0;line-height: 87px;text-align:center">ZuKeChat兑换码</p>
              <p style="color: black; margin-top: 10px; height: 30px; line-height: 25px;padding: 5px">{{ item.name }}</p>
              <p style="margin-left: 8px;height: 15px;line-height: 15px;padding-left: 5px">库存：<span style="color: red">{{item.stock}}</span></p>
              <div style="margin-left: 5px;height: 35px; line-height: 35px;padding: 5px">
                <span style="position: absolute; bottom: 0;"><span style="color: red">{{ item.price }}.00</span>元人民币</span>
                <el-button style="position: absolute; right: 0; bottom: 0" type="primary" @click="purchase(item)">购买</el-button>
              </div>
            </div>
            <!-- 这里是正确的位置 -->
            <br/><br/>
          </div>
        </div>
      </div>
    </div>


    <!-- 只在移动端显示 -->
    <div id="mobile" v-show="isMobile">
      <div id="mbbody">
        <h2 style="color: black;margin-left: 20px;margin-top: 20px">租客的小商铺</h2>
        <p id="p1" style="width: 800px;height: 160px;font-size: 15px;width: 90%">{{ content }}</p>
        <div style="margin-left: 20px">
          <el-button size="large" type="info">请选择商品购买</el-button>
          <br/><br/>
          <div v-for="(group, index) in groupedCtgr" :key="'group' + index">
            <div v-for="(item, idx) in group" :key="idx" style="width: 200px; height: 180px; background-color: white; position: relative;margin-bottom: 50px">
              <!--<img style="width: 198px; margin: 0; padding: 0" src="../assets/images/gpt.png">-->
              <p style="width: 198px; margin: 0; padding: 0;line-height: 87px;text-align:center">ZuKeChat兑换码</p>
              <p style="color: black; margin-top: 10px; height: 30px; line-height: 25px;padding: 5px">{{ item.name }}</p>
              <p style="margin-left: 8px;height: 15px;line-height: 15px;padding-left: 5px">库存：<span style="color: red">{{item.stock}}</span></p>
              <div style="margin-left: 5px;height: 35px; line-height: 35px;padding: 5px">
                <span style="position: absolute; bottom: 0;"><span style="color: red">{{ item.price }}.00</span>元人民币</span>
                <el-button style="position: absolute; right: 0; bottom: 0" type="primary" @click="purchase(item)">购买</el-button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!--解读：下表单-->
    <el-dialog
        v-model="dialogVisible"
        title="请严格填写邮箱，兑换码将会以邮件的方式发送到邮箱！"
        :width="formWidth"
        :before-close="handleClose"
    >
      <el-form :model="order" label-width="120px">
        <el-form-item label="邮箱" prop="name">
          <el-input v-model="order.name" style="width: 80%"/>
        </el-form-item>

        <el-form-item label="价格" prop="amount">
          <el-input disabled v-model="order.amount" style="width: 80%"/>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="saveOrder">确定</el-button>
          <el-button @click="dialogVisible = false">取消</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>

    <!--解读：二维码-->
    <el-dialog
        v-model="dialogVisible1"
        title="请用微信扫码支付"
        :width="formWidth"
        :before-close="handleClose"
    >
      <el-form :model="order" label-width="120px">
        <!-- 如果有二维码URL，显示二维码 -->
        <img v-if="qrCodeUrl" :src="qrCodeUrl" alt="QR Code" style="display: block; margin: auto;">
        <el-form-item style="margin-top: 30px">
          <el-button type="primary" @click="over">支付完毕</el-button>
          <el-button @click="dialogVisible1 = false">取消</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
import request from '@/utils/request'
import Header from "@/components/Header.vue";
import Aside from "@/components/Aside.vue";
import {host} from "@/assets/js/value";

export default {
  name: "User",
  components: {Aside, Header},
  data() {
    return {
      formWidth: "30%",
      isMobile: false, // 默认不是移动端
      dialogVisible1: false,
      qrCodeUrl: "",
      order: {},
      dialogVisible: false,
      pd: [],
      ctgr: [],
      updateTime: "",
      content: "暂无公告"
      //host: "http://localhost:8088/"
    };
  },
  mounted() {
    this.checkDeviceType();
    this.getAn();
    this.getCtgr();
  },
  /*解读：实现遍历分组分类*/
  computed: {
    groupedCtgr() {
      return this.ctgr.reduce((acc, item, index) => {
        const groupIndex = Math.floor(index / 3);
        if (!acc[groupIndex]) {
          acc[groupIndex] = [];
        }
        acc[groupIndex].push(item);
        return acc;
      }, []);
    }
  },
  methods: {

    /*解读：判断访问设备类型*/
    checkDeviceType() {
      // 简单的移动端检测逻辑，可根据需要调整
      const userAgent = navigator.userAgent;
      this.isMobile = /iPhone|iPad|iPod|Android/i.test(userAgent);
      if(this.isMobile){
        this.formWidth = "90%";
      }
    },

    /*解读：支付完毕逻辑*/
    over() {
      this.dialogVisible1 = false;
      this.$message({
        type: "success",
        message: "请前往填写的邮箱查收兑换码，可能会有延迟，1分钟左右到帐"
      })
    },

    /*解读：下单*/
    saveOrder() {
      request.post(host + "saveO", this.order).then(res => {
        if (res.code == "200") {
          this.dialogVisible = false;
          this.dialogVisible1 = true;
          this.qrCodeUrl = res.data;
        } else {
          this.$message({
            type: "error",
            message: res.msg ? res.msg : "error"
          })
        }
      });
    },

    /*解读：处理商品点击事件*/
    purchase(item) {
      this.order = {};
      this.order.amount = item.price;
      this.dialogVisible = true;
    },

    /*解读：获取商品分类*/
    getCtgr() {
      request.get(host + "admin/listCtg").then(res => {
        if (res.code == "200") {
          this.ctgr = res.data;
        } else {
          this.$message({
            type: "error",
            message: res.msg ? res.msg : "error"
          })
        }
      });
    },

    /*解读：获取公告*/
    getAn() {
      request.get(host + "admin/getAncmt").then(res => {
        if (res.code == "200") {
          this.content = res.data.content;
          const date = new Date(res.data.updateTime);
          const formattedDate = date.getFullYear() + '年' +
              (date.getMonth() + 1) + '月' +
              date.getDate() + '日 ' +
              date.getHours() + ':' +
              date.getMinutes();
          this.updateTime = formattedDate;
        } else {
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

<style scoped>
#p1 {
  border: 2px solid rgb(255, 255, 255); /* 基础边框 */
  box-shadow: 0 0 10px rgb(128, 128, 128); /* 添加阴影效果 */
  padding: 20px;
  margin: 20px;
}

#ctgr {
  border: 2px solid rgb(255, 255, 255); /* 基础边框 */
  box-shadow: 0 0 10px rgb(128, 128, 128); /* 添加阴影效果 */
}

#pcbody {
  //background-image: url('../assets/images/background.png'); /* 替换为实际的图片URL */
  //background-size: cover; /* 背景图片覆盖整个div，保持图片比例 */
  //background-position: center; /* 背景图片居中显示 */
  display: flex;
  align-items: center;
  min-height: 100vh; /* 使得整个页面至少有视口的高度 */
  flex-direction: column; /* 子元素垂直排列，如果最外层div是唯一的子元素或你希望它垂直居中的话 */
  background-image: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
}

#mbbody {
  //background-image: url('../assets/images/mobile.jpg'); /* 替换为实际的图片URL */
  //background-size: cover; /* 背景图片覆盖整个div，保持图片比例 */
  //background-position: center; /* 背景图片居中显示 */
  display: flex;
  min-height: 100vh; /* 使得整个页面至少有视口的高度 */
  flex-direction: column; /* 子元素垂直排列，如果最外层div是唯一的子元素或你希望它垂直居中的话 */
  background-image: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
}

</style>
