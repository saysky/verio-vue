import request from '@/utils/request'

// 查询新闻列表
export function listNews(query) {
  return request({
    url: '/verio/portal/news/list',
    method: 'get',
    params: query
  })
}


// 查询新闻详细
export function getNews(id) {
  return request({
    url: '/verio/portal/news/' + id,
    method: 'get'
  })
}
