import request from '@/utils/request'

// 查询订单列表
export function listOrder(query) {
  return request({
    url: '/verio/order/list',
    method: 'get',
    params: query
  })
}

// 查询已生效的订单ID列表
export function activeList(query) {
  return request({
    url: '/verio/order/activeList',
    method: 'get',
    params: query
  })
}

// 查询订单详细
export function getOrder(id) {
  return request({
    url: '/verio/order/' + id,
    method: 'get'
  })
}

// 新增订单
export function addOrder(data) {
  return request({
    url: '/verio/order',
    method: 'post',
    data: data
  })
}

// 修改订单
export function updateOrder(data) {
  return request({
    url: '/verio/order',
    method: 'put',
    data: data
  })
}

// 删除订单
export function delOrder(id) {
  return request({
    url: '/verio/order/' + id,
    method: 'delete'
  })
}
export function cancelOrder(id) {
  return request({
    url: '/verio/order/cancel/' + id,
    method: 'post'
  })
}
export function signContractOrder(id) {
  return request({
    url: '/verio/order/signContract/' + id,
    method: 'post'
  })
}
export function payOrder(id) {
  return request({
    url: '/verio/order/pay/' + id,
    method: 'post'
  })
}
