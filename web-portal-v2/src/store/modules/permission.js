import { asyncRouterMap, constantRouterMap } from '@/router'
/**
 * 通过authority判断是否与当前用户权限匹配
 * @param menus
 * @param route
 */

/**
 * 递归过滤异步路由表，返回符合用户角色权限的路由表
 * @param asyncRouterMap
 * @param roles
 */
import inpage from '@/views/inpage'
import config from '@/hosts/config'
// import config from '@/hosts/config'
const _import = file => () => import('@/views' + file + '.vue')
let routeData = {
  path: '/',
  // name: 'home',
  component: inpage,
  children: [
    /* {
      name:'企业菜单管理', path: '/baseManager/menuManager', component: _import('/base/menu-list')
    },
    {
      name:'数据字典', path: '/baseManager/dataDictionary', component: _import('/base/data-dictionary')
    },
    {
      name:'企业列表', path: '/enterprise-list', component: _import('/base/enterprise/application-enterprise-list-manager')
    },
    {
      name:'网关管理', path: '/gateway-manager', component: _import('/service/gateway-manager')
    },
    {
      name:'swagger管理', path: '/swagger-manager', component: _import('/service/swagger-interface-doc-manager')
    },
    {
      path: '/demoPage', component: _import('/blank_page')
    }, 
    {
      name:'应用管理', path: '/application-manager', component: _import('/admin/application-manager')
    },
    {
      name:'白名单管理', path: '/white-list', component: _import('/admin/white-list')
    },
    {
      name:'用户管理', path: '/admin-user-manager', component: _import('/admin/user/admin-user-manager')
    },
    {
      name:'企业管理', path: '/admin-enterprise-manager', component: _import('/admin/enterprise/admin-enterprise-manager')
    },
    {
      name:'应用功能管理', path: '/admin-function-manager', component: _import('/admin/function/admin-function-manager')
    },
    {
      name:'应用管理', path: '/admin-application-manager', component: _import('/admin/application/admin-application-manager')
    }*/
    // {
    //   name:'企业组织', path: '/orgManager/enterprise/departManager', component: _import('/base/enterprise/organization-list')
    // }
  ]
};
function filterAsyncRouter(menuData, callback) {
  if (!menuData) {
    return;
  }
  for (var i = 0, len = menuData.length; i< len; i++) {
    if (menuData[i]['type'] == 1) {
      if(!menuData[i].functionResourceEntity.pagePath) {
        continue;
      }
      let obj = {};
      obj.id = menuData[i].id;
      obj.isMenu = true;  
       
      //临时写死
     
      // if(menuData[i].functionResourceEntity.routePath=="/web-navigation"){
      
      //   menuData[i].name="web导航配置";
      //   menuData[i].functionResourceEntity.pagePath="/portal-manager/web-navigation";
        
      // }
      // else if(menuData[i].functionResourceEntity.routePath=="/mobile-navigation"){
      //   menuData[i].name="移动导航配置";
      //   menuData[i].functionResourceEntity.pagePath="/portal-manager/navigation";
      // }

      obj.path = menuData[i].functionResourceEntity.routePath;
      

      obj.name = menuData[i].name;     

      

      obj.linkUrl=menuData[i].functionResourceEntity.pagePath;
      // obj.component = inpage;
      
      if(obj.linkUrl.indexOf('http')!=0){
        obj.component = _import(menuData[i].functionResourceEntity.pagePath);
      }      
      // console.log('pagePath == ', menuData[i].functionResourceEntity.name, menuData[i].functionResourceEntity.pagePath);
      callback(menuData[i]);
      // obj.component = r => require.ensure([], () => r(require('../../views/' + menuData[i].pagePath + '.vue')), 'demo')
      routeData.children.push(obj);
    } else {
      filterAsyncRouter(menuData[i]['children'], callback);
    }
  }
}
const permission = {
  state: {
    routers: constantRouterMap,
    addRouters: [],
    language: 'zh-Cn',
    //防止重复获取用户信息
    isInit: false,
    CivetUrl:'',
    menuIds: {
      '/orgManager/enterprise/departManager': 103
    },
    /* authIndex: 1,
    authData: [
      {
        id: 1,
        name: 'MaxBase',
        code: 'MaxBase',
        value: 'Basic TWF4YmFzZTpNYXhiYXNl'
      }, 
      {
        id: 2,
        name: 'MaxTPM',
        code: 'MaxTPM',
        value: 'Basic TWF4VFBNOk1heFRQTQ==',
        url: 'http://b.com:8082/#/'
      },
      {
        id: 3,
        name: 'MaxMES',
        code: 'MaxMES',
        value: 'Basic TWF4TUVTOk1heE1FUw==',
        url: 'http://b.com:8082/#/'
      },
      {
        id: 4,
        name: 'demo',
        code: 'demo',
        value: 'Basic TWF4TUVTOk1heE1FUw==',
        url: 'http://a.b.com:8080/#/'
      }
    ] */
  },
  mutations: {
    SET_ROUTERS: (state, data) => {
      state.addRouters = data;
    },
    SET_INIT: (state, flag = true) => {
      state.isInit = flag;
    },
    SET_MENU_IDS: (state, data) => {
      let entity = data.functionResourceEntity;
      state.menuIds[entity.routePath] = entity.id;
    },
    SET_CivetUrl(state, data) {    
      // https://sit.iisd.mts.com:701/admin/civet/getCivetUserInfo   
      //  http://civetinterface.foxconn.com/Open/oauth/?appid=Rr7kCJaIiyk1&redirect_uri=https%3a%2f%2fsit.iisd.mts.com%3a701%2fadmin%2fcivet%2fgetCivetUserInfo&scope=snsapi_userinfo
      //'http://civetinterface.foxconn.com/Open/oauth/?appid=Rr7kCJaIiyk1&redirect_uri=https%3a%2f%2f210.63.218.108%3a701%2fadmin%2fcivet%2fgetCivetUserInfo&scope=snsapi_userinfo'
      
      state.CivetUrl = config["CivetUrl"];
      window.localStorage.setItem('CivetUrl',state.CivetUrl);
    },
    
    /* ,
    CHANGE_AUTHIDX: (state, index) => {
      state.authIndex = index;
    } */
  },
  actions: {
    GenerateRoutes({
      commit
    }, obj) {
      return new Promise(resolve => {
        filterAsyncRouter(obj.menuData? obj.menuData : [], (data) => {
          commit('SET_MENU_IDS', data);
          // debugger
        });
        // console.log('routeData', [routeData]);
        commit('SET_ROUTERS', [routeData]);
        commit('SET_INIT');
        resolve(routeData)
      })
    }
  }
}

export default permission
