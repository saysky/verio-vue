import request from '@/utils/request'
// 新增收藏
export function addMark(data) {
  return request({
    url: '/verio/mark',
    method: 'post',
    data: data
  })
}
