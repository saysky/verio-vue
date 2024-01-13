import request from '@/utils/request'


// 查询文章评论列表
export function listComment(query) {
  return request({
    url: '/verio/portal/comment/list',
    method: 'get',
    params: query
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
