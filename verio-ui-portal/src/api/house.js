import request from '@/utils/request'

export function portalHouseList(params) {
  return request({
    url: '/verio/portal/house/list',
    method: 'get',
    params: params
  })
}
export function portalHouseDetail(id) {
  return request({
    url: '/verio/portal/house/'+id,
    method: 'get'
  })
}

