// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import store from '@/store'
import ElementUI from 'element-ui'
import VueI18n from 'vue-i18n'
import '@/utils/common'
import './permission'
// import 'lib-flexible/flexible'
import 'element-ui/lib/theme-chalk/index.css'
import '@/assets/css/basic.min.css'
import '@/assets/css/style.min.css'
import '@/assets/font-icon/style.css'
import '@/assets/font-icon/ex-icons/iconfont.css'

import VueContextMenu from 'vue-contextmenu'
import 'vue-contextmenu/style/css/font-awesome.min.css'


Vue.use(VueContextMenu)
Vue.use(ElementUI);
Vue.use(VueI18n) // 通过插件的形式挂载

const i18n = new VueI18n({
  locale: 'zh-CN',    // 语言标识
  //this.$i18n.locale // 通过切换locale的值来实现语言切换
  messages: {
    'zh-CN': require('./lang/zh-CN.js').data,   // 中文语言包
    'zh-TW': require('./lang/zh-TW.js').data,   // 台湾繁体中文语言包
  }
});

Vue.config.productionTip = false

// let permission = JSON.parse(window.sessionStorage.getItem('permission'))  
// if (permission) {  
//   store.commit(ADD_MENU, permission)  
//   router.addRoutes(store.state.menu.items)  
// } 

/* eslint-disable no-new */
let timer = null;
window.addEventListener('resize', () => {
  clearTimeout(timer);
  timer = setTimeout(() => {
    store.commit('CHANGE_WINDOW_SIZE');
  }, 300);
});
new Vue({
  el: '#app',
  router,
  store,
  i18n,
  components: { App },
  template: '<App/>'
})
