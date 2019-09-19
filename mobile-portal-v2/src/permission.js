import router from './router'
import store from './store'
import { getToken, removeToken,isMobilPage,setToken } from '@/utils/auth' // getToken from cookie

const whiteList = ['/login', '/authredirect', '/product-list','/browser-product-list','/browser-login','/mobile-unbind-user']// no redirect whitelist
import {
  getMenus
} from '@/api/login.js'

// let menuData = [];

router.beforeEach((to, from, next) => {  
  // console.log('beforeEach====', getToken());  
  if (to.query.disabled === '1') {
    return false
  } else {
    /* next();
    return; */
    if ( true ) {
      if (to.path === '/login' || to.path === '/') {
        next({ path: '/product-list' });
      } else {          
        /* console.log('username====', store.getters.name);
        next();
        return; */
        // console.log(store.getters.isInit);
        // debugger
        if (!store.getters.isInit || store.getters.addRouters.length === 0) {
          /* if (to.path !== '/product-list') {
            next({ ...to, replace: true })
            return;
          }  */
          // console.log('to.path === ', to.path);
          store.dispatch('GetInfo', to.path === '/product-list').then(res => {
            // let _data = {"data":[{"id":1,"pid":-1,"name":"MaxBase","order":0,"children":[{"id":2,"pid":1,"name":"基础配置管理","order":0,"children":[{"id":3,"pid":2,"name":"用户管理","order":0,"children":null,"icon":"ex-chevron-right","type":1,"enabled":true,"routePath":"/baseManager/userManager","pagePath":"/base/user/application-user-manager","description":"用户管理","subsql":null},{"id":4,"pid":2,"name":"角色管理","order":0,"children":null,"icon":"ex-chevron-right","type":1,"enabled":true,"routePath":"/baseManager/roleManager","pagePath":"/base/user/role-manager","description":"角色管理","subsql":null},{"id":5,"pid":2,"name":"企业信息管理","order":2,"children":null,"icon":"ex-chevron-right","type":1,"enabled":true,"routePath":"/orgManager/enterprise","pagePath":"/base/enterprise/application-enterprise-detail-manager","description":"维护企业基本信息和组织树","subsql":null}],"icon":"ex-base","type":0,"enabled":true,"routePath":"-","pagePath":"-","description":"基础配置管理","subsql":null},{"id":6,"pid":1,"name":"oauth2管理","order":1,"children":[{"id":7,"pid":6,"name":"Token管理","order":0,"children":null,"icon":"ex-chevron-right","type":1,"enabled":true,"routePath":"/authManager/tokenManger","pagePath":"/auth/token-manager","description":"Token管理","subsql":null},{"id":8,"pid":6,"name":"客户端管理","order":0,"children":null,"icon":"ex-chevron-right","type":1,"enabled":true,"routePath":"/authManager/clientManager","pagePath":"/auth/client-manger","description":"客户端管理","subsql":null}],"icon":"ex-org","type":0,"enabled":true,"routePath":"-","pagePath":"-","description":"基础配置管理","subsql":null},{"id":9,"pid":1,"name":"服务管理","order":2,"children":[{"id":10,"pid":9,"name":"网关路由管理","order":0,"children":null,"icon":"ex-chevron-right","type":1,"enabled":true,"routePath":"/serviceManager/gatewayManager","pagePath":"/service/gateway-manager","description":"网关路由管理","subsql":null},{"id":11,"pid":9,"name":"API文档查看","order":1,"children":null,"icon":"ex-chevron-right","type":1,"enabled":true,"routePath":"/serviceManager/swaggerServiceManager","pagePath":"/service/swagger-service-manager","description":"对Swagger接口服务进行管理","subsql":null},{"id":12,"pid":9,"name":"API文档接口管理","order":2,"children":null,"icon":"ex-chevron-right","type":1,"enabled":true,"routePath":"/serviceManager/swaggerInterfaceDocManager","pagePath":"/service/swagger-interface-doc-manager","description":"服务接口文档","subsql":null}],"icon":"ex-setting","type":0,"enabled":true,"routePath":"-","pagePath":"-","description":"服务管理","subsql":null},{"id":13,"pid":1,"name":"微服务治理","order":3,"children":[{"id":14,"pid":13,"name":"服务注册中心","order":0,"children":null,"icon":"ex-chevron-right","type":1,"enabled":true,"routePath":"/governanceManager/eurekaManager","pagePath":"/governance/eureka-manager","description":"服务注册管理","subsql":null},{"id":15,"pid":13,"name":"服务监控中心","order":1,"children":null,"icon":"ex-chevron-right","type":1,"enabled":true,"routePath":"/governanceManager/monitorManager","pagePath":"/governance/monitor-manager","description":"服务监控中心","subsql":null}],"icon":"ex-internet","type":0,"enabled":true,"routePath":"-","pagePath":"-","description":"监控运维管理","subsql":null}],"icon":"ex-cloud","type":0,"enabled":true,"routePath":"-","pagePath":"-","description":"Maxbase系统根菜单","subsql":null}],"status":200,"message":null};
            // 获取菜单
            if (to.path !== '/product-list') {// 产品大全不需要获取惨淡，以免报错
              
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
              next('/login');
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
      next();
    }
  }
  
});