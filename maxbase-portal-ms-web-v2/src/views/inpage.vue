<template>
  <el-container direction="vertical">
    <vue-header v-if="isShow" :title="`${langText.header.title}`"></vue-header>
    <el-container :class="'inpage-container'">
      <el-aside v-if="isShow" :style="{'height': $store.getters.mainHeight + 'px', 'min-width': '65px', 'width': 'auto','overflow': 'visible'}">
        <el-row class="tac">
          <el-col :span="24">
            <side-menu class="side-menu"></side-menu>
          </el-col>
        </el-row>
      </el-aside>
      <el-main :style="{'height': $store.getters.mainHeight + 'px', 'overflow': 'hidden'}">
        <section v-if="isShow" class="tag-selector">
          <el-scrollbar style="height: 42px;" ref="wrap"
            class="hide-warp" 
          >
            <div>
              <span class="el-tag nav-tag el-tag--medium"
                :class="{'active-tag': $route.path === tag.path}"
                :key="tag.name"
                v-for="(tag, idx) in menuTags"
                @click="clickTag($event, tag)"
                @click.middle="closeTag(tag, idx, $event)"
                @contextmenu="showMenu($event, idx, tag)">
                {{tag.name}}
                <i class="el-tag__close el-icon-close" @click="closeTag(tag, idx)"></i>
              </span>
            </div>
          </el-scrollbar>
        </section>
        <!-- <div class="content-wrapper" :style="{height: ($store.getters.mainHeight - 35) + 'px'}"> -->
        <div class="content-wrapper" :style="{height: ($store.getters.mainHeight - 62) + 'px'}">          
          <keep-alive>
            <router-view />
          </keep-alive>               
           <iframe  
             :v-if="tag.linkUrl.indexOf('http')==0"
             :id="tag.path"
             :key="tag.name"          
              v-for="(tag) in menuTags.filter(r=>r.linkUrl.indexOf('http')==0)"
              :name="tag.name"             
              :style="'width:100%;height:100%'"  
              :src="tag.linkUrl"  
              v-show="$route.path===tag.path"
              frameborder="0" 
              scrolling="auto"             
            /> 
        </div>
      </el-main>
    </el-container>
    <vue-context-menu :contextMenuData="contextMenuData"
      @deleteCurrent="deleteCurrent"
      @deleteOthers="deleteOthers"
      @freshPage="freshPage">
    </vue-context-menu>
  </el-container>
</template>

<script>
import '@/assets/css/inpage.min.css'
import VueHeader from '@/components/header'
import SideMenu from '@/components/sidebar/index'
import config from '@/hosts/config'
export default {
  name: 'inpage',
  data() {
    return {
      config,
      // contextmenu data (菜单数据)
      langText: {
        header: this.$t('header')
      },    
      keepAlive:true,  
      contextMenuData: {
        tagIndex: 0,
        tagData: undefined,
        // the contextmenu name(@1.4.1 updated)
        menuName: 'light',
        // The coordinates of the display(菜单显示的位置)
        axios: {
          x: null,
          y: null
        },
        // Menu options (菜单选项)
        menulists: [
          {
            fnHandler: 'deleteCurrent', // Binding events(绑定事件)
            btnName: '删除当前' // The name of the menu option (菜单名称)
          },
          {
            fnHandler: 'deleteOthers',
            btnName: '删除其他'
          },
          {
            fnHandler: 'freshPage',
            btnName: '刷新'
          }
        ]
      }
    }
  },
  computed: {
    menuTags() {     
      return this.$store.state.menu.menuTags;
    },
    isShow(){
        let  ishow= sessionStorage.getItem("iframehiddle");
        if(ishow){
          return  false;
        }
        return  true;
    }   
  },   
  methods: { 
    clickTag(e, data) { 
      //点击到删除按钮时为删除，禁止掉路由跳转
      if (e.target.className === 'el-tag__close el-icon-close') {
        return false;
      }
      this.$router.push({path:data.path,query:data.query});
    },
    showMenu(e, idx, data) {      
      e.preventDefault()
      var x = e.clientX;
      var y = e.clientY;
      if(data.linkUrl.indexOf('http')!=0){
        this.contextMenuData.menulists=[
            {
              fnHandler: 'deleteCurrent', // Binding events(绑定事件)
              btnName: '删除当前' // The name of the menu option (菜单名称)
            },
            {
              fnHandler: 'deleteOthers',
              btnName: '删除其他'
            }   
          ];
      }
      else{
        this.contextMenuData.menulists=[
            {
              fnHandler: 'deleteCurrent', // Binding events(绑定事件)
              btnName: '删除当前' // The name of the menu option (菜单名称)
            },
            {
              fnHandler: 'deleteOthers',
              btnName: '删除其他'
            },
            {
              fnHandler: 'freshPage',
              btnName: '刷新'
            }   
          ];
      }
      // Get the current location
      this.contextMenuData.axios = {
        x, y
      };
      this.tagIndex = idx;
      this.tagData = data;
    },
    freshPage(){        
      let linkUrl=this.menuTags[this.tagIndex].linkUrl;   
      let routePath=this.menuTags[this.tagIndex].path;   
      if(linkUrl.indexOf('http')!=0){        
        this.keepAlive=false;  
        this.$router.push({path:routePath+'1',query:this.tagData.query}); 
        this.$router.push({path:routePath,query:this.tagData.query});
        this.$nextTick(() => (this.keepAlive = true));           
      }
      else{        
        let iframe=window.document.getElementById(routePath);
        if(iframe){
            iframe.setAttribute('src',linkUrl);
        }
      }
    },
    deleteCurrent() {
      this.closeTag(this.tagData, this.tagIndex);
    },
    deleteOthers() {
      if (this.menuTags.length <= 1) {
        return false;
      }
      this.$router.push(this.menuTags[this.tagIndex].fullPath);
      this.$store.commit('DEL_OTHERS_MENU_TAG', this.tagIndex);
    },
    closeTag(data, idx, ev) {
      ev && ev.preventDefault();
      this.$store.commit('DEL_MENU_TAG', idx);
      if (this.menuTags.length === 0 && this.$route.path !== '/') {
        this.$router.push('/');
        return false;
      }
      if (data.path === this.$route.path) {
        idx = (idx === 0? 0 : idx - 1);
        this.$router.push(this.menuTags[idx].fullPath);
      }
      return false;
    }
  },
  components: {
    VueHeader,
    SideMenu
  }
}
</script>

<style scoped>
.el-container{
  background: #004279;
}
.tag-selector{
  background: #fff;
  height: 31px;
  border-bottom: 1px solid hsl(222, 20%, 87%);
  /* box-shadow: 0 1px 3px 0 rgba(0,0,0,.12), 0 0 3px 0 rgba(0,0,0,.04); */
  padding: 14px 10px;
  overflow: hidden;
}
.el-main{
  padding: 0;
}
.nav-tag{
  margin: 3px 12px;
  cursor: pointer;
}
.content-wrapper{
  overflow: auto;
}
</style>