import router from './router'
import store from './store'
import { getToken, removeToken,isMobilPage,setToken } from '@/utils/auth' // getToken from cookie

const whiteList = ['/login','/product-list', '/authredirect','/mobile-product-list','/mobile-login','/browser-product-list','/browser-login','/mobile-unbind-user']// no redirect whitelist
import {
  getMenus,
  getAppGroup,
  getResource
} from '@/api/login.js'

// let menuData = [];

router.beforeEach((to, from, next) => {  
  // console.log('beforeEach====', getToken());  
  if (to.query.disabled === '1') {
    return false
  } else {     
    if (!store.getters.initResource){
      getResource().then(res=>{        
        store.commit('setdataResource', res.data); 
        document.title = store.getters.dataResource.system_name||'system_name';    
      })
      //store.commit('setMenuData', menuRes.data);
    }
   
    /* next();
    return; */
    if ( true ) {
      if (to.path === '/login' || to.path==="/") {
        next({ path: '/product-list' });
      } else {        
        /* console.log('username====', store.getters.name);
        next();
        return; */
        // console.log(store.getters.isInit);
        // debugger
        if (!store.getters.isInit || store.getters.addRouters.length === 0) {          
          store.dispatch('GetInfo', to.path === '/product-list').then(res => {           
            if (to.path !== '/product-list') {// 产品大全不需要获取惨淡，以免报错              
              let  groupid="";
              if(to.path=="/index"){               
                if(to.query.id){
                  groupid=to.query.id;
                  sessionStorage.setItem('groupid', groupid);
                }                      
                sessionStorage.setItem('groupName', to.query.groupName||"");     
              }else{
                groupid=sessionStorage.getItem('groupid');
              }              
              if(groupid){
                getAppGroup(groupid).then(applist=>{
                  debugger
                  getMenus(applist.data).then(menuRes => {   
                    console.log("menuRes",menuRes);         
                    try {
                      store.dispatch('GenerateRoutes', {
                        menuData: menuRes.data
                        // menuData: _data.data
                      }).then(() => { // 生成可访问的路由表
                        
                        // console.log('GenerateRoutes', store.getters.addRouters);
                        router.addRoutes(store.getters.addRouters) // 动态添加可访问路由表
                        // console.log('router', router);
                        next({ ...to, replace: true }) // hack方法 确保addRoutes已完成 ,set the replace: true so the navigation will not leave a history record
                      });
                      store.commit('setMenuData', menuRes.data);
                    } catch (error) {
                      console.error(error);
                    }
                  }).catch(error => {
                    next('/product-list');
                  });
                })  
              }  
            } else {
              next();
            }
          }).catch(err => {
            if (err.status === 500201) {//登出
              removeToken();
              next('/login');
            } else if (err.response && err.response.data.status === 40302) {//过期
              // removeToken();
              // store.dispatch('LogOut');
              // next('/login');
              // location.reload();
              // 修改无权限处理方式
              next('/product-list');
            }
          });
        } else {          
          if (to.name) {              
            let sources={};
            Object.assign(sources,to);              
            let menuData= store.getters.addRouters[0].children.find(r=>{
               return r.path==to.path;
            });
            if(menuData){             
              sources.linkUrl=menuData.linkUrl;              
            }
            store.commit("ADD_MENU_TAG", sources);
          }
          next()
        }
      }
    } else {  
      if ( whiteList.indexOf(to.path) !== -1 ) {
        next();
      } else {
        //如果是手机端2
          if(isMobilPage()){
            window.location.href=window.localStorage.getItem('CivetUrl');  
            //next({ path: '/mobile-login' });
          }
          else {
            next('/login');
          }
      }
    }
  }
  
});