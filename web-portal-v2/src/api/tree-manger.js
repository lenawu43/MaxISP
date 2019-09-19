import request from '@/utils/request'
// 因为所有的接口调用模式都非常相似，因此在这里把api抽象提取
export function page(apiPath, query) {
  return request({
    url: '/' + apiPath + '/page',
    method: 'get',
    params: query
  })
}

export function getTree(apiPath, id) {
  return request({
    url: '/' + apiPath + '/getTree' + (id ? '/' + id : ''),
    method: 'get'
  })
}

export function addObj(apiPath, obj) {
  return request({
    url: '/' + apiPath,
    method: 'post',
    data: obj
    // params: obj
  })
}

export function delObj(apiPath, id) {
  return request({
    url: '/' + apiPath + '/' + id,
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

export function updObj(apiPath, id, obj) {
  return request({
    url: '/' + apiPath + '/' + id,
    method: 'put',
    data: obj
  })
}