import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import EventListView from '../views/EventListView.vue'
import EventDetailView from '../views/EventDetailView.vue'

const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeView
  },
  {
    path: '/events',
    name: 'events',
    component: EventListView
  },
  {
    path: '/events/:id',
    name: 'event-detail',
    component: EventDetailView
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
