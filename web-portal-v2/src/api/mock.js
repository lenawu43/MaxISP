import request from '@/utils/request'
let baseUrl = 'https://easy-mock.com/mock/5b7e84c0b54e99056ad281db/maxbase/';
// 因为所有的接口调用模式都非常相似，因此在这里把api抽象提取
export function page(apiPath, query) {
  return request({
    url: baseUrl + apiPath + '/page',
    method: 'get',
    params: query
  })
}
// https://easy-mock.com/mock/5b7e84c0b54e99056ad281db/maxbase
export function getTree(apiPath, id) {
  return request({
    url: baseUrl + apiPath + '/getTree' + (id ? '/' + id : ''),
    method: 'get'
  })
}

export function addObj(apiPath, obj) {
  return request({
    url: baseUrl + apiPath + '/',
    method: 'post',
    data: obj
    // params: obj
  })
}

export function delObj(apiPath, id) {
  return request({
    url: baseUrl + apiPath + '/' + id,
    method: 'delete'
  })
}

export function getObj(apiPath, id) {
  return request({
    url: baseUrl + apiPath + '/' + id,
    method: 'get'
  })
}

export function enableObj(apiPath, id, isAble) {
  return request({
    url: baseUrl + apiPath + '/' + id + '/' + isAble,
    method: 'get'
  })
}

export function updObj(apiPath, id, obj) {
  return request({
    url: baseUrl + apiPath + '/' + id,
    method: 'put',
    data: obj
  })
}

export function search(apiPath, params = {}) {
  return request({
    url: baseUrl + apiPath + '/search',
    method: 'get',
    params   
  })
}

export function unique(apiPath, params, id) {
  return request({
    url: baseUrl + apiPath + '/unique' + (id? '/' + id : ''),
    method: 'get',
    params   
  })
}

export function getTemplate(type = '') {
  return request({
    url: baseUrl + 'admin/extendtemplate/selectByObjectTypeAndApplicationId/' + type
  });
}

export function getExtendEntityList() {
  return request({
    url: baseUrl + 'getExtendEntityList'
  });
}

export function getDialogTemplate(url) {
  return request({
    url: baseUrl + url
  });
}