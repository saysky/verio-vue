import request from '@/utils/request'

// 查询维修列表
export function listMaintain(query) {
  return request({
    url: '/verio/maintain/list',
    method: 'get',
    params: query
  })
}

// 查询维修详细
export function getMaintain(id) {
  return request({
    url: '/verio/maintain/' + id,
    method: 'get'
  })
}

// 新增维修
export function addMaintain(data) {
  return request({
    url: '/verio/maintain',
    method: 'post',
    data: data
  })
}

// 修改维修
export function updateMaintain(data) {
  return request({
    url: '/verio/maintain',
    method: 'put',
    data: data
  })
}

// 删除维修
export function delMaintain(id) {
  return request({
    url: '/verio/maintain/' + id,
    method: 'delete'
  })
}
