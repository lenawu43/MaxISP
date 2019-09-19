import axios from 'axios'
import {
  Message,
  MessageBox,
  Notification
} from 'element-ui'
import store from '@/store'
import {
  getToken,
  isMobilPage
} from '@/utils/auth'




// create an axios instance
const service = axios.create({
  // baseURL: process.env.BASE_API, // api的base_url
  timeout: 500000 // request timeout
})

// request拦截器
import hostsConfig from '@/hosts/config'
service.interceptors.request.use(config => {
  // Do something before request is sent
  // console.log('config==', process.env);
  config.headers['Application-Code'] = hostsConfig['Application-Code'];
  /* if(process.env.NODE_ENV === 'development') {// 本地开发环境写死Application-Code
    config.headers['Application-Code'] = 'www';
  } else {
    config.headers['Application-Code'] = hostsConfig['Application-Code'];
  } */
    
  if (store.getters.token) {
    config.headers.Authorization = getToken(); // 让每个请求携带token--['X-Token']为自定义key 请根据实际情况自行修改
  } else {
    // config.headers.Authorization = permission.authData[permission.authIndex].value; // 增加客户端认证
    config.headers.Authorization = 'Basic TWF4YmFzZTpNYXhiYXNl';
    // debugger
  }
  return config
}, error => {
  // Do something with request error
  // console.log(error) // for debug
  Promise.reject(error)
})

// respone拦截器
service.interceptors.response.use(
  response => {
    /**
     * 下面的注释为通过response自定义code来标示请求状态，当code返回如下情况为权限有问题，登出并返回到登录页
     * 如通过xmlhttprequest 状态码标识 逻辑可写在下面error中
     */
    const res = response.data
    // if (response.status === 401 || res.status === 40101) {
    //   MessageBox.confirm('你已被登出，可以取消继续留在该页面，或者重新登录', '确定登出', {
    //     confirmButtonText: '重新登录',
    //     cancelButtonText: '取消',
    //     type: 'warning'
    //   }).then(() => {
    //     store.dispatch('LogOut').then(() => {
    //       location.reload() // 为了重新实例化vue-router对象 避免bug
    //     })
    //   })
    //   return Promise.reject('error')
    // }
    // if (res.status === 30101) {
    //   Message({
    //     message: res.message,
    //     type: 'error',
    //     duration: 5 * 1000
    //   })
    //   return Promise.reject('error')
    // }
    // if (res.status === 40301) {
    //   Message({
    //     message: '当前用户无相关操作权限！',
    //     type: 'error',
    //     duration: 5 * 1000
    //   })
    //   return Promise.reject('error')
    // }
    if (response.status !== 200 && res.status !== 200) {
      Message({
        message: res.message,
        type: 'error',
        duration: 5 * 1000
      })
    } else {
      return response.data
    }
  },
  error => {
    // console.log('err' + error) // for debug
    const response = error.response
    if (response === undefined) {
      Message({
        message: '服务请求超时！',
        type: 'error',
        duration: 5 * 1000
      })
      return Promise.reject(error)
    }
    //登录相关代码不是正常错误返回，因此加个三元来判断
    let err = response.data.error? response.data.error_description : '错误的请求！';
    if (response.status === 400) {
      Message({
        message: err,
        type: 'error'
      })
      if (response.data.error_description && response.data.error_description === 'Bad credentials') {
        error = '用户名或密码错误';
      }
      return Promise.reject(error)
    }
    const info = response.data
    if (response.status === 401 && info.status === 40101) {
      MessageBox.confirm('你已被登出，可以取消继续留在该页面，或者重新登录', '确定登出', {
        confirmButtonText: '重新登录',
        cancelButtonText: '取消',
        type: 'error'
      }).then(() => {
        store.dispatch('LogOut').then(() => {
          if(isMobilPage()){
            window.location.href=window.localStorage.getItem('CivetUrl');  
            //location.reload() // 为了重新实例化vue-router对象 避免bug            
          }
          else{
            location.href = '/#/login';
            location.reload() // 为了重新实例化vue-router对象 避免bug
          }
        }).catch(err => {
          Message({
            message: err,
            type: 'error'
          })
        });
      })
    }
    //针对登录用户被删除而做的判断
    if (response.status === 401 && response.data.error) {
      Message({
        message: err,
        type: 'error'
      })
      return Promise.reject(err)
    }
    if (response.status === 401 && info.status === 40001) {
      Message.warning({
        title: '禁止',
        message: info.message,
        type: 'error',
        duration: 2 * 1000
      })
      return Promise.reject(err)
    }
    if (response.status === 403) {
      Message.warning({
        title: '禁止',
        message: info.message,
        type: 'error',
        duration: 2 * 1000
      })
      return Promise.reject('error')
    }
    if (info.status === 30101) {
      Message.warning({
        title: '失败',
        message: info.message,
        type: 'error',
        duration: 2 * 1000
      })
      return Promise.reject('error')
    }
    if (response.status === 504) {
      Message({
        message: '后端服务异常，请联系管理员！',
        type: 'error',
        duration: 5 * 1000
      })
      return Promise.reject(error)
    }
    Message({
      message: info.message,
      type: 'error',
      duration: 5 * 1000
    })
    return Promise.reject(error)
  }
)

export default service
