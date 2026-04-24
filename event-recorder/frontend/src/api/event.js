import axios from 'axios'

const api = axios.create({
  baseURL: '/api',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器
api.interceptors.request.use(
  config => {
    return config
  },
  error => {
    console.error('Request error:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
api.interceptors.response.use(
  response => {
    return response.data
  },
  error => {
    console.error('Response error:', error)
    return Promise.reject(error)
  }
)

export const eventApi = {
  // 获取所有事件
  getAllEvents() {
    return api.get('/events')
  },

  // 获取单个事件
  getEventById(id) {
    return api.get(`/events/${id}`)
  },

  // 创建事件
  createEvent(event) {
    return api.post('/events', event)
  },

  // 更新事件
  updateEvent(id, event) {
    return api.put(`/events/${id}`, event)
  },

  // 删除事件
  deleteEvent(id) {
    return api.delete(`/events/${id}`)
  },

  // 按年月查询事件
  getEventsByMonth(year, month) {
    return api.get(`/events/month/${year}/${month}`)
  },

  // 按年份查询事件
  getEventsByYear(year) {
    return api.get(`/events/year/${year}`)
  },

  // 搜索事件
  searchEvents(keyword) {
    return api.get('/events/search', { params: { keyword } })
  },

  // 按日期范围查询事件
  getEventsByDateRange(startDate, endDate) {
    return api.get('/events/range', { 
      params: { 
        startDate: new Date(startDate).toISOString(),
        endDate: new Date(endDate).toISOString()
      }
    })
  }
}

export default api
