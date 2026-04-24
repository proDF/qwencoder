<template>
  <div class="home-container">
    <el-container>
      <el-header class="header">
        <h1>📝 事件记录器</h1>
        <el-menu mode="horizontal" background-color="#409EFF" text-color="#fff" active-text-color="#ffd04b">
          <el-menu-item index="1" @click="$router.push('/')">首页</el-menu-item>
          <el-menu-item index="2" @click="$router.push('/events')">事件列表</el-menu-item>
        </el-menu>
      </el-header>

      <el-main class="main-content">
        <el-card class="welcome-card">
          <template #header>
            <div class="card-header">
              <span>欢迎使用事件记录器</span>
            </div>
          </template>
          
          <div class="welcome-content">
            <p>记录您的每一天，留下美好的回忆</p>
            <div class="action-buttons">
              <el-button type="primary" size="large" @click="showCreateDialog = true">
                ➕ 新建事件
              </el-button>
              <el-button type="success" size="large" @click="$router.push('/events')">
                📋 查看事件
              </el-button>
            </div>
          </div>
        </el-card>

        <el-row :gutter="20" class="stats-row">
          <el-col :span="8">
            <el-card shadow="hover">
              <div class="stat-card">
                <div class="stat-icon">📅</div>
                <div class="stat-info">
                  <div class="stat-value">{{ totalEvents }}</div>
                  <div class="stat-label">总事件数</div>
                </div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="8">
            <el-card shadow="hover">
              <div class="stat-card">
                <div class="stat-icon">📆</div>
                <div class="stat-info">
                  <div class="stat-value">{{ thisMonthEvents }}</div>
                  <div class="stat-label">本月事件</div>
                </div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="8">
            <el-card shadow="hover">
              <div class="stat-card">
                <div class="stat-icon">🔍</div>
                <div class="stat-info">
                  <div class="stat-value">无限</div>
                  <div class="stat-label">搜索功能</div>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>

        <el-card class="recent-events-card">
          <template #header>
            <div class="card-header">
              <span>最近事件</span>
              <el-button type="primary" link @click="$router.push('/events')">查看全部</el-button>
            </div>
          </template>
          
          <el-table :data="recentEvents" style="width: 100%" v-loading="loading">
            <el-table-column prop="title" label="标题" />
            <el-table-column prop="category" label="分类" width="100" />
            <el-table-column prop="eventDate" label="日期" width="180">
              <template #default="{ row }">
                {{ formatDate(row.eventDate) }}
              </template>
            </el-table-column>
            <el-table-column label="操作" width="150">
              <template #default="{ row }">
                <el-button type="primary" size="small" @click="viewEvent(row.id)">查看</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-main>
    </el-container>

    <!-- 创建事件对话框 -->
    <el-dialog v-model="showCreateDialog" title="新建事件" width="600px">
      <el-form :model="newEvent" label-width="80px">
        <el-form-item label="标题">
          <el-input v-model="newEvent.title" placeholder="请输入事件标题" />
        </el-form-item>
        <el-form-item label="内容">
          <el-input 
            v-model="newEvent.content" 
            type="textarea" 
            :rows="5"
            placeholder="请输入事件内容或想法" 
          />
        </el-form-item>
        <el-form-item label="分类">
          <el-input v-model="newEvent.category" placeholder="如：工作、学习、生活等" />
        </el-form-item>
        <el-form-item label="日期">
          <el-date-picker
            v-model="newEvent.eventDate"
            type="datetime"
            placeholder="选择日期时间"
            value-format="YYYY-MM-DD HH:mm:ss"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showCreateDialog = false">取消</el-button>
        <el-button type="primary" @click="handleCreateEvent">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useEventStore } from '@/store/event'
import dayjs from 'dayjs'
import { ElMessage } from 'element-plus'

const router = useRouter()
const eventStore = useEventStore()

const showCreateDialog = ref(false)
const loading = ref(false)

const newEvent = ref({
  title: '',
  content: '',
  category: '',
  eventDate: dayjs().format('YYYY-MM-DD HH:mm:ss')
})

const totalEvents = computed(() => eventStore.events.length)

const thisMonthEvents = computed(() => {
  const now = dayjs()
  return eventStore.events.filter(e => 
    dayjs(e.eventDate).year() === now.year() && 
    dayjs(e.eventDate).month() === now.month()
  ).length
})

const recentEvents = computed(() => {
  return [...eventStore.events]
    .sort((a, b) => dayjs(b.eventDate).valueOf() - dayjs(a.eventDate).valueOf())
    .slice(0, 5)
})

const formatDate = (date) => {
  return dayjs(date).format('YYYY-MM-DD HH:mm')
}

const handleCreateEvent = async () => {
  if (!newEvent.value.title) {
    ElMessage.warning('请输入事件标题')
    return
  }
  
  try {
    await eventStore.createEvent(newEvent.value)
    ElMessage.success('事件创建成功')
    showCreateDialog.value = false
    newEvent.value = {
      title: '',
      content: '',
      category: '',
      eventDate: dayjs().format('YYYY-MM-DD HH:mm:ss')
    }
  } catch (error) {
    ElMessage.error('创建失败：' + error.message)
  }
}

const viewEvent = (id) => {
  router.push(`/events/${id}`)
}

onMounted(async () => {
  loading.value = true
  await eventStore.fetchAllEvents()
  loading.value = false
})
</script>

<style scoped>
.home-container {
  min-height: 100vh;
}

.header {
  background-color: #409EFF;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
}

.header h1 {
  color: white;
  font-size: 24px;
  margin: 0;
}

.main-content {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.welcome-card {
  margin-bottom: 20px;
}

.welcome-content {
  text-align: center;
  padding: 20px 0;
}

.welcome-content p {
  font-size: 18px;
  color: #666;
  margin-bottom: 30px;
}

.action-buttons {
  display: flex;
  justify-content: center;
  gap: 20px;
}

.stats-row {
  margin-bottom: 20px;
}

.stat-card {
  display: flex;
  align-items: center;
  padding: 10px;
}

.stat-icon {
  font-size: 48px;
  margin-right: 20px;
}

.stat-info {
  flex: 1;
}

.stat-value {
  font-size: 32px;
  font-weight: bold;
  color: #409EFF;
}

.stat-label {
  font-size: 14px;
  color: #999;
  margin-top: 5px;
}

.recent-events-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
