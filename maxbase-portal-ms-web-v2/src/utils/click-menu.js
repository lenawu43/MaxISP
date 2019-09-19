import Vue from 'vue';
import clickMenu from '@/components/click-menu'
Vue.component('click-menu', clickMenu);
Vue.directive('menu', {
  // 当被绑定的元素插入到 DOM 中时……
  inserted: function (el, binding) {
    // 聚焦元素
    el.addEventListener('mousedown', e=> {
      if (e.button) {
        e.preventDefault();
        return false;
      }
    });
    el.oncontextmenu = function() {
      return false;
    }
  }
})