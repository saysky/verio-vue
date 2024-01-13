import request from '@/utils/request'

// 查询房屋列表
export function listHouse(query) {
  return request({
    url: '/verio/house/list',
    method: 'get',
    params: query
  })
}

// 查询房屋详细
export function getHouse(id) {
  return request({
    url: '/verio/house/' + id,
    method: 'get'
  })
}

// 新增房屋
export function addHouse(data) {
  return request({
    url: '/verio/house',
    method: 'post',
    data: data
  })
}

// 修改房屋
export function updateHouse(data) {
  return request({
    url: '/verio/house',
    method: 'put',
    data: data
  })
}

// 删除房屋
export function delHouse(id) {
  return request({
    url: '/verio/house/' + id,
    method: 'delete'
  })
}
