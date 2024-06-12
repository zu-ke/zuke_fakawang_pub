import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'

/*解读：引入css*/
import '@/assets/css/global.css'

/*解读：国际化（中文）*/
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'

/*解读：引入ElementPlus*/
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

createApp(App).use(store).use(router).use(ElementPlus,{locale: zhCn}).mount('#app')
