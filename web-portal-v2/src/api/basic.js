import request from '@/utils/request'
// import Vue from 'vue'
// 因为所有的接口调用模式都非常相似，因此在这里把api抽象提取
export function page(apiPath, query) {
  return request({
    url: '/' + apiPath + '/page',
    method: 'get',
    params: query
  })
}

export function getTree(apiPath, id, {apiDef} = {}) {
  return request({
    url: (apiDef? '/' + apiDef :'/' + apiPath + '/getTree') + (id ? '/' + id : ''),
    method: 'get'
  })
}

export function addObj(apiPath, obj, {apiDef} = {}) {
  return request({
    url: apiDef? '/' + apiDef + '/': '/' + apiPath + '/',
    method: 'post',
    data: obj
    // params: obj
  })
}

export function delObj(apiPath, id, {apiDef} = {}) {  
  return request({
    url: apiDef?  apiDef + '/' + id :'/' + apiPath + '/' + id,
    method: 'delete'
  })
}

export function getObj(apiPath, id) {
  return request({
    url: '/' + apiPath + '/' + id,
    method: 'get'
  })
}

export function enableObj(apiPath, id, isAble) {
  return request({
    url: '/' + apiPath + '/' + id + '/' + isAble,
    method: 'get'
  })
}

export function updObj(apiPath, id, obj, {apiDef} = {}) {
  return request({
    url: apiDef? '/' + apiDef + '/' + id : '/' + apiPath + '/' + id,
    method: 'put',
    data: obj
  })
}

export function search(apiPath, params = {}, id, {apiDef} = {}) {
  let url = (apiDef? apiDef : '/' + apiPath + '/search') + (id? '/' + id: '')
  return request({
    url,
    method: 'get',
    params   
  })
}

export function unique(apiPath, params, id, {apiDef} = {}) {
  return request({
    url: apiDef? '/' + apiDef + (id? '/' + id : '') : '/' + apiPath + '/unique' + (id? '/' + id : ''),
    method: 'get',
    params   
  })
}
