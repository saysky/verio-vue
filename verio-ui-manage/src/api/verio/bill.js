import request from '@/utils/request'

// 查询账单列表
export function listBill(query) {
  return request({
    url: '/verio/bill/list',
    method: 'get',
    params: query
  })
}

// 查询账单详细
export function getBill(id) {
  return request({
    url: '/verio/bill/' + id,
    method: 'get'
  })
}

// 新增账单
export function addBill(data) {
  return request({
    url: '/verio/bill',
    method: 'post',
    data: data
  })
}

// 修改账单
export function updateBill(data) {
  return request({
    url: '/verio/bill',
    method: 'put',
    data: data
  })
}

// 删除账单
export function delBill(id) {
  return request({
    url: '/verio/bill/' + id,
    method: 'delete'
  })
}
