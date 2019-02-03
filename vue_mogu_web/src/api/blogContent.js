import request from '@/utils/request'

export function getBlogByUid (params) {
  return request({
    url: process.env.WEB_API + '/content/getBlogByUid',
    method: 'get',
    params
  })
}

export function getSameBlog (params) {
  return request({
    url: process.env.WEB_API + '/content/getSameBlog',
    method: 'get',
    params
  })
}

export function praiseBlogByUid (params) {
  return request({
    url: process.env.WEB_API + '/content/praiseBlogByUid',
    method: 'get',
    params
  })
}

export function getBlogPraiseCountByUid (params) {
  return request({
    url: process.env.WEB_API + '/content/getBlogPraiseCountByUid',
    method: 'get',
    params
  })
}