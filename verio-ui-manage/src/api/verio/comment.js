import request from '@/utils/request'

// 查询文章评论列表
export function listComment(query) {
  return request({
    url: '/verio/comment/list',
    method: 'get',
    params: query
  })
}

// 查询文章评论详细
export function getComment(id) {
  return request({
    url: '/verio/comment/' + id,
    method: 'get'
  })
}

// 新增文章评论
export function addComment(data) {
  return request({
    url: '/verio/comment',
    method: 'post',
    data: data
  })
}

// 修改文章评论
export function updateComment(data) {
  return request({
    url: '/verio/comment',
    method: 'put',
    data: data
  })
}

// 删除文章评论
export function delComment(id) {
  return request({
    url: '/verio/comment/' + id,
    method: 'delete'
  })
}
