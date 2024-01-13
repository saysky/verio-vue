import request from '@/utils/request'

// 查询文章列表
export function listPost(query) {
  return request({
    url: '/verio/portal/post/list',
    method: 'get',
    params: query
  })
}

// 查询文章详细
export function getPost(id) {
  return request({
    url: '/verio/portal/post/' + id,
    method: 'get'
  })
}
