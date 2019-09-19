import {
  loginByUsername,
  logout,
  getInfo,
  getMenus,
  getAuthories
} from '@/api/login.js'
import {
  Message
} from 'element-ui'
import { getToken, setToken, removeToken } from '@/utils/auth'
import config from '@/hosts/config'

let isGetMenu = false;
const user = {
  state: {
    user: '',
    token: getToken(),
    userInfo: {},
    name: '',
    roles: [],
    menuData: [],
    permissionMenus: undefined,
    setting: {
      articlePlatform: []
    },
    CivetUserCode:'',
    currentOrgPower:false//组织管理是否能新增删除修改

  },

  mutations: {
    SET_TOKEN: (state, token) => {
      state.token = token
    },
    SET_NAME: (state, name) => {
      state.name = name
      // console.log('state.name', state.name)
    },
    SET_ROLES: (state, roles) => {
      state.roles = roles
    },
    LOGOUT_USER: state => {
      state.user = ''
    },
    SET_PERMISSION_MENUS: (state, permissionMenus) => {
      state.permissionMenus = permissionMenus
    },
    SET_USEINFO: (state, data) => {
      state.userInfo = data;
      // state.userInfo.isPlatformAdmin = true;
    },
    setMenuData(state, data) {
      state.menuData = data || [];
    },
    setCivetUserCode(state, data) {
      state.CivetUserCode = data;
      window.localStorage.setItem('CivetUserCode',state.CivetUserCode);
    },
    setcurrentOrgPower(state,data){
      state.currentOrgPower=data;
    }
  },

  actions: {
    // 用户名登录
    LoginByUsername({ commit }, {userInfo, isSavePwd}) {
      const username = userInfo.username.trim()
      return new Promise((resolve, reject) => {
        //后端仍有问题，先写死token
        /* let token = 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1Mjk0Mzg4NTQsInVzZXJfbmFtZSI6ImFkbWluIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiIsIlJPTEVfVVNFUiJdLCJqdGkiOiJjODg3MTU3ZS0xYjA3LTQ3ZGItOTlhYi1kMzNjOTA0NTkzNTYiLCJjbGllbnRfaWQiOiJ0ZXN0X3Bhc3N3b3JkX2NsaWVudCIsInNjb3BlIjpbInRydXN0Il19.Q7PtMC7G6uh_7EXxoVqKfcd-Hk_D2atvSX9vnmjwvX8';
        setToken(token, {expires:30});
        commit('SET_TOKEN', token);
        resolve();
        return ; */
        loginByUsername(username, userInfo.password).then(response => {
          const data = response
          if (data.data === '') {
            Message({
              message: '账户或密码错误！',
              type: 'warning'
            })
            return Promise.reject('error')
          } else {
            // console.log('isSavePwd', isSavePwd)
            let obj = {};
            obj.domain = config['domain-name']; //所有子域都可以共享这个cookies
            if (isSavePwd) {
              obj.expires = 30;
            }
            setToken(data.access_token, obj)
            commit('SET_TOKEN', data.access_token)
            resolve();
            /* getAuthories().then(resposne => {
              let getkey = Object.getOwnPropertyNames || Object.keys;
              if (getkey(resposne).length !== 0) {
                resolve();
              } else {
                removeToken(data.access_token, obj)
                commit('SET_TOKEN', '')
                reject('没有权限！');
              }
            }) */
          }
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 获取用户信息
    GetInfo({ commit }  ) {
      commit('SET_INIT');
      return new Promise((resolve, reject) => {
        // console.log('SET_NAME');
        getInfo().then(response => {
          if (response.status === 500201) {
            Message.error('token过期或解析错误！请重新登录');
            reject(response);
          } else if (response.status === 200) {
            const data = response.data;
            commit('SET_ROLES', 'admin')
            commit('SET_NAME', data.name)
            commit('SET_USEINFO', data);
            resolve(response);
          } else {
            reject(response);
          }
        }).catch(error => {
          reject(error)
        })
        /* getMenus().then(response => {
          commit('SET_PERMISSION_MENUS', response)
        }) */
      })
    },

    // 第三方验证登录
    // LoginByThirdparty({ commit, state }, code) {
    //   return new Promise((resolve, reject) => {
    //     commit('SET_CODE', code)
    //     loginByThirdparty(state.status, state.email, state.code).then(response => {
    //       commit('SET_TOKEN', response.data.token)
    //       setToken(response.data.token)
    //       resolve()
    //     }).catch(error => {
    //       reject(error)
    //     })
    //   })
    // },

    // 登出
    LogOut({
      commit,
      state
    }) {
      return new Promise((resolve, reject) => {
        //api/oauth/oauth/tokens        
        logout(state.token).then((res) => {
          /* if (res.status !== 200) {
            reject(res.message);
          } else {
          } */
          commit('SET_TOKEN', '')
          commit('SET_ROLES', [])
          commit('SET_PERMISSION_MENUS', undefined);
          commit('setMenuData', [])
          commit('CLEAR_MENU_TAG');
          commit('SET_INIT', false);
          removeToken();
          resolve();
        }).catch(error => {
          reject(error)
        })
      })
    }/* ,
    // 前端 登出
    FedLogOut({
      commit
    }) {
      return new Promise(resolve => {
        commit('SET_TOKEN', '')
        commit('SET_ROLES', [])
        commit('SET_PERMISSION_MENUS', undefined)
        removeToken()
        resolve()
      })
    } */
  }
}

export default user
