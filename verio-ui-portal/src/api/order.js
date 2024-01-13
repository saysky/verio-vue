import request from '@/utils/request'

// 新增订单
export function addOrder(data) {
  return request({
    url: '/verio/order',
    method: 'post',
    data: data
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

// 查询订单详细
export function getOrder(id) {
  return request({
    url: '/verio/order/' + id,
    method: 'get'
  })
}
