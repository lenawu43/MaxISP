import request from '@/utils/request'

const  menudata={
	"data": [{
		"id": 1,
		"pid": -1,
		"code": null,
		"name": "Portal管理",
		"order": 0,
		"children": [
      {
        "id": 6,
        "pid": 1,
        "code": null,
        "name": "个性化配置",
        "order": 6,
        "children": null,
        "menuShortcut": null,
        "icon": "ex-app",
        "type": 1,
        "functionResourceEntity": {
          "id": 10263,
          "deleteFlag": false,
          "createdBy": null,
          "createdDate": null,
          "lastModifiedBy": null,
          "lastModifiedDate": null,
          "subsql": null,
          "pid": 10529,
          "order": null,
          "type": null,
          "name": "个性化配置",
          "code": "personalization",
          "enabled": null,
          "scope": null,
          "routePath": "/personalization",
          "pagePath": "/portal-manager/personalization",
          "description": "",
          "owner": null,
          "isDefault": null,
          "permissionId": null,
          "functionTypeId": null,
          "functionTypeEntity": null,
          "elementResourceEntities": [],
          "applicationFunctionId": null,
          "icon": null
        },
        "enabled": true,
        "description": null,
        "subsql": null
      },  
      {
        "id": 4,
        "pid": 1,
        "code": null,
        "name": "业务类型维护",
        "order": 4,
        "children": null,
        "menuShortcut": null,
        "icon": "ex-app",
        "type": 1,
        "functionResourceEntity": {
          "id": 10263,
          "deleteFlag": false,
          "createdBy": null,
          "createdDate": null,
          "lastModifiedBy": null,
          "lastModifiedDate": null,
          "subsql": null,
          "pid": 10529,
          "order": null,
          "type": null,
          "name": "业务类型维护",
          "code": "businesses-type",
          "enabled": null,
          "scope": null,
          "routePath": "/businesses-type",
          "pagePath": "/portal-manager/bussiness-type",
          "description": "",
          "owner": null,
          "isDefault": null,
          "permissionId": null,
          "functionTypeId": null,
          "functionTypeEntity": null,
          "elementResourceEntities": [],
          "applicationFunctionId": null,
          "icon": null
        },
        "enabled": true,
        "description": null,
        "subsql": null
      },    
      {
			"id": 2,
			"pid": 1,
			"code": null,
			"name": "Web导航配置",
			"order": 0,
			"children": null,
			"menuShortcut": null,
			"icon": "ex-cloud",
			"type": 1,
			"functionResourceEntity": {
				"id": 10262,
				"deleteFlag": false,
				"createdBy": null,
				"createdDate": null,
				"lastModifiedBy": null,
				"lastModifiedDate": null,
				"subsql": null,
				"pid": 10529,
				"order": null,
				"type": null,
				"name": "Web导航配置",
				"code": "web-navigation",
				"enabled": null,
				"scope": null,
				"routePath": "/web-navigation",
				"pagePath": "/portal-manager/web-navigation",
				"description": "",
				"owner": null,
				"isDefault": null,
				"permissionId": null,
				"functionTypeId": null,
				"functionTypeEntity": null,
				"elementResourceEntities": [],
				"applicationFunctionId": null,
				"icon": null
			},
			"enabled": true,
			"description": null,
			"subsql": null
		}, {
			"id": 2,
			"pid": 1,
			"code": null,
			"name": "Mobile导航配置",
			"order": 2,
			"children": null,
			"menuShortcut": null,
			"icon": "ex-app",
			"type": 1,
			"functionResourceEntity": {
				"id": 10263,
				"deleteFlag": false,
				"createdBy": null,
				"createdDate": null,
				"lastModifiedBy": null,
				"lastModifiedDate": null,
				"subsql": null,
				"pid": 10529,
				"order": null,
				"type": null,
				"name": "Mobile导航配置",
				"code": "mobile-navigation",
				"enabled": null,
				"scope": null,
				"routePath": "/mobile-navigation",
				"pagePath": "/portal-manager/mobile-navigation",
				"description": "",
				"owner": null,
				"isDefault": null,
				"permissionId": null,
				"functionTypeId": null,
				"functionTypeEntity": null,
				"elementResourceEntities": [],
				"applicationFunctionId": null,
				"icon": null
			},
			"enabled": true,
			"description": null,
			"subsql": null
    },    
    {
			"id": 5,
			"pid": 1,
			"code": null,
			"name": "管理员维护",
			"order": 5,
			"children": null,
			"menuShortcut": null,
			"icon": "ex-app",
			"type": 1,
			"functionResourceEntity": {
				"id": 10263,
				"deleteFlag": false,
				"createdBy": null,
				"createdDate": null,
				"lastModifiedBy": null,
				"lastModifiedDate": null,
				"subsql": null,
				"pid": 10529,
				"order": null,
				"type": null,
				"name": "管理员维护",
				"code": "administrators-manager",
				"enabled": null,
				"scope": null,
				"routePath": "/administrators-manager",
				"pagePath": "/portal-manager/administrators-manager",
				"description": "",
				"owner": null,
				"isDefault": null,
				"permissionId": null,
				"functionTypeId": null,
				"functionTypeEntity": null,
				"elementResourceEntities": [],
				"applicationFunctionId": null,
				"icon": null
			},
			"enabled": true,
			"description": null,
			"subsql": null
    }  
     ],
		"menuShortcut": null,
		"icon": "ex-format-align-left",
		"type": 0,
		"functionResourceEntity": {
			"id": 10261,
			"deleteFlag": false,
			"createdBy": null,
			"createdDate": null,
			"lastModifiedBy": null,
			"lastModifiedDate": null,
			"subsql": null,
			"pid": -1,
			"order": null,
			"type": null,
			"name": "Portal管理",
			"code": "portal-ms",
			"enabled": null,
			"scope": null,
			"routePath": "",
			"pagePath": "",
			"description": "",
			"owner": null,
			"isDefault": null,
			"permissionId": null,
			"functionTypeId": null,
			"functionTypeEntity": null,
			"elementResourceEntities": [],
			"applicationFunctionId": null,
			"icon": null
		},
		"enabled": true,
		"description": null,
		"subsql": null
	}]	
}

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
  // return request({
  //   url: '/admin/user/getLoginUserInfo',
  //   method: 'get',
  //   params
  // });
  let  response={};
  response.status=200;
  response.data={};
  response.data.name="管理员";

    
  return new Promise((resolve, reject) => {
    resolve(response);
   }); 
}

export function getMenus() {
  // return request({
  //   url: '/admin/permission/selectUserFunctionResources',
  //   method: 'get'
  // });
  return new Promise((resolve, reject) => {
    resolve(menudata);
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