<template>
  <!-- <el-scrollbar style="height: 100%;" ref="wrap"
    class="menu-scrollbar hide-warp" 
    :wrap-style="{height: $store.getters.mainHeight + 'px'}"
    >
    <i class="element-ex-icons el-icon-ex-fold-menu" @click="changeCollapse"></i>
    <el-menu 
          router
          class="el-menu-vertical-demo" 
          :default-active="$route.path"
          background-color="#545c64"
          text-color="#fff"
          active-text-color="#ffd04b"
          :collapse="isCollapse"
          >
            <sidebar-item :routes='[menuData]'></sidebar-item>
    </el-menu>
  </el-scrollbar> -->
  <div :style="{height: $store.getters.mainHeight + 'px'}" class="clearfix">
    <i class="element-ex-icons el-icon-ex-close" :class="{plip: !isCollapse}" @click="changeCollapse"></i>
    <el-scrollbar style="height: 100%;" ref="wrap"
      class="menu-scrollbar hide-warp" 
      :wrap-style="{height: $store.getters.mainHeight + 'px'}"
      >
      <el-menu 
          class="el-sidebar-vertical" 
          :class="{'is-collapse': isCollapse}"
          :default-active="$route.path"
          text-color="#fff"
          active-text-color="#c0bf87"
          background-color="#004279"
          :collapse="isCollapse"
          @select="selectMenu"
        >
          <sidebar-item :routes='menuData'></sidebar-item>
      </el-menu>
    </el-scrollbar>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import SidebarItem from './SidebarItem'
//import request from '@/utils/request'

export default {
  components: { SidebarItem },
  data() {
    return {
      isCollapse: false
    }
  },
  computed: {
    menuData() {
      let _data = this.$store.state.user.menuData;
      return _data;
    },
    ...mapGetters([
      // 'permissionMenus',
      'sidebar'
    ])
  },
  mounted() {
    // console.log(this.$store.getters.mainHeight + 'px');
    // this.$refs.wrap.$el.style.height = this.$store.getters.mainHeight + 'px';
  },
  methods: {
    changeCollapse() {
      this.isCollapse = !this.isCollapse;
    },
    selectMenu(index, indexPath) {
      // console.log('indexPath === ', indexPath);
    }
  }
}
</script>

<style scoped>
.el-icon-ex-close{
  width: 60px;
  height: 60px;
  font-size: 30px;
  line-height: 60px;
  text-align: center;
  cursor: pointer;
  color: #fff;
  position: fixed;
  top: 0;
  left: 0;
  perspective-origin: center top;
  transition: transform 1s;
}
.plip{
  transform: rotateY(180deg);
}
</style>