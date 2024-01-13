import request from '@/utils/request'

// 查询收藏列表
export function listMark(query) {
  return request({
    url: '/verio/mark/list',
    method: 'get',
    params: query
  })
}

// 查询收藏详细
export function getMark(id) {
  return request({
    url: '/verio/mark/' + id,
    method: 'get'
  })
}

// 新增收藏
export function addMark(data) {
  return request({
    url: '/verio/mark',
    method: 'post',
    data: data
  })
}

// 修改收藏
export function updateMark(data) {
  return request({
    url: '/verio/mark',
    method: 'put',
    data: data
  })
}

// 删除收藏
export function delMark(id) {
  return request({
    url: '/verio/mark/' + id,
    method: 'delete'
  })
}

