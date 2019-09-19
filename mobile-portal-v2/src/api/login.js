import request from '@/utils/request'

export function loginByUsername(username, password) {
  const data = {
    username,
    password,
    grant_type: 'password'
  }
  
  // console.log('loginByUsername===', data);
  return request({
    url: '/api/oauth/oauth/token',
    method: 'post',
    data,
    transformRequest: [function(data) {
      // Do whatever you want to transform the data
      let ret = ''
      for (const it in data) {
        ret += encodeURIComponent(it) + '=' + encodeURIComponent(data[it]) + '&'
      }
      return ret
    }],
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded'
    }
  })
}

export function logout(access_token) {
  return request({
    url: '/api/oauth/oauth/tokens',
    method: 'delete',
    data: {
      access_token
    }
  });
}

export function getInfo(params) {
  let  response={};
  response.status=200;
  response.data={};
  response.data.name="管理员";
    
  return new Promise((resolve, reject) => {
    resolve(response);
   }); 
}

export function getMenus() {
  return request({
    url: '/admin/permission/selectUserFunctionResources',
    method: 'get'
  });
}

export function getAllMenus() {
  return request({
    url: '/admin/menu-resource/all',
    method: 'get'
  })
}

export function getAuthories() {
  return request({
    url: '/admin/user/authorities',
    method: 'get'
  })
  
}