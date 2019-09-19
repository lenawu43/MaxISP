<template>
  <div class="menu-wrapper">
    <template v-for="item in routes">
      <el-submenu :index="item.id +''" :key="item.name">
        <template slot="title">
          <i :class="'element-ex-icons el-icon-' + item.icon"></i>
          <span slot="title">{{item.name}}</span>
        </template>
        <template v-for="child in item.children">
          <sidebar-item :is-nest="true" class='nest-menu' v-if='child.children&&child.children.length>0' :routes='[child]' :key="child.path"> </sidebar-item>
          
          <router-link v-else :to="child.functionResourceEntity.routePath + (child.disabled? '?disabled=' + child.disabled : '')" :key="child.name">
            <el-menu-item :disabled="child.disabled === '1'" :index="child.functionResourceEntity.routePath + '' || ''" :route="child.functionResourceEntity.routePath || ''">
              <i :class="'element-ex-icons submenu-icon el-icon-' + child.icon"></i>
              <span slot="title">{{child.name}}</span>
            </el-menu-item>
          </router-link>
        </template>
      </el-submenu>
    </template>
  </div>
</template>
<script>
import {
  getAuthories
} from '@/api/login.js'
//import { getToken, setToken, removeToken } from '@/utils/auth'
// import { generateTitle } from '@/utils/i18n'
export default {
  name: 'SidebarItem',
  props: {
    routes: {
      type: Array
    },
    isNest: {
      type: Boolean,
      default: false
    }
  },
  created() {
    // getAuthories().then(resposne => {
    //   // debugger
    //   let getkey = Object.getOwnPropertyNames || Object.keys;
    //   if (getkey(resposne).length == 0) {
    //     // removeToken(data.access_token, obj)
    //     // this.$store.commit('SET_TOKEN', '');
    //     //this.$router.push('/product-list');
    //     this.$message.error('没有权限！');
    //   }
    // }).catch(error => {
    //   // debugger
    // });
  },
  data() {
    return {
      
    }
  },
  // created() {
  // }
}
</script>


