import { defineStore } from 'pinia'
import { eventApi } from '@/api/event'

export const useEventStore = defineStore('event', {
  state: () => ({
    events: [],
    currentEvent: null,
    loading: false,
    error: null
  }),

  getters: {
    allEvents: (state) => state.events,
    currentEventDetail: (state) => state.currentEvent
  },

  actions: {
    async fetchAllEvents() {
      this.loading = true
      try {
        const data = await eventApi.getAllEvents()
        this.events = data
        this.error = null
      } catch (error) {
        this.error = error.message
        console.error('Failed to fetch events:', error)
      } finally {
        this.loading = false
      }
    },

    async fetchEventById(id) {
      this.loading = true
      try {
        const data = await eventApi.getEventById(id)
        this.currentEvent = data
        this.error = null
      } catch (error) {
        this.error = error.message
        console.error('Failed to fetch event:', error)
      } finally {
        this.loading = false
      }
    },

    async createEvent(eventData) {
      this.loading = true
      try {
        const newEvent = await eventApi.createEvent(eventData)
        this.events.push(newEvent)
        this.error = null
        return newEvent
      } catch (error) {
        this.error = error.message
        console.error('Failed to create event:', error)
        throw error
      } finally {
        this.loading = false
      }
    },

    async updateEvent(id, eventData) {
      this.loading = true
      try {
        const updatedEvent = await eventApi.updateEvent(id, eventData)
        const index = this.events.findIndex(e => e.id === id)
        if (index !== -1) {
          this.events[index] = updatedEvent
        }
        if (this.currentEvent && this.currentEvent.id === id) {
          this.currentEvent = updatedEvent
        }
        this.error = null
        return updatedEvent
      } catch (error) {
        this.error = error.message
        console.error('Failed to update event:', error)
        throw error
      } finally {
        this.loading = false
      }
    },

    async deleteEvent(id) {
      this.loading = true
      try {
        await eventApi.deleteEvent(id)
        this.events = this.events.filter(e => e.id !== id)
        this.error = null
      } catch (error) {
        this.error = error.message
        console.error('Failed to delete event:', error)
        throw error
      } finally {
        this.loading = false
      }
    },

    async getEventsByMonth(year, month) {
      this.loading = true
      try {
        const data = await eventApi.getEventsByMonth(year, month)
        this.events = data
        this.error = null
        return data
      } catch (error) {
        this.error = error.message
        console.error('Failed to fetch events by month:', error)
        throw error
      } finally {
        this.loading = false
      }
    },

    async getEventsByYear(year) {
      this.loading = true
      try {
        const data = await eventApi.getEventsByYear(year)
        this.events = data
        this.error = null
        return data
      } catch (error) {
        this.error = error.message
        console.error('Failed to fetch events by year:', error)
        throw error
      } finally {
        this.loading = false
      }
    },

    async searchEvents(keyword) {
      this.loading = true
      try {
        const data = await eventApi.searchEvents(keyword)
        this.events = data
        this.error = null
        return data
      } catch (error) {
        this.error = error.message
        console.error('Failed to search events:', error)
        throw error
      } finally {
        this.loading = false
      }
    }
  }
})
