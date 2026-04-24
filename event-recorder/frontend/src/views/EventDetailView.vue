<template>
  <div class="detail-container">
    <el-container>
      <el-header class="header">
        <h1>📄 事件详情</h1>
        <el-menu mode="horizontal" background-color="#409EFF" text-color="#fff" active-text-color="#ffd04b">
          <el-menu-item index="1" @click="$router.push('/')">首页</el-menu-item>
          <el-menu-item index="2" @click="$router.push('/events')">事件列表</el-menu-item>
        </el-menu>
      </el-header>

      <el-main class="main-content">
        <div v-if="loading" class="loading-wrapper">
          <el-skeleton :rows="5" animated />
        </div>

        <el-card v-else-if="event" class="detail-card">
          <template #header>
            <div class="card-header">
              <span>{{ event.title }}</span>
              <div class="header-actions">
                <el-button type="primary" @click="handleEdit">编辑</el-button>
                <el-button type="danger" @click="handleDelete">删除</el-button>
                <el-button @click="$router.push('/events')">返回</el-button>
              </div>
            </div>
          </template>

          <div class="detail-content">
            <div class="meta-info">
              <div class="meta-item">
                <span class="label">📅 日期:</span>
                <span class="value">{{ formatDate(event.eventDate) }}</span>
              </div>
              <div class="meta-item">
                <span class="label">🏷️ 分类:</span>
                <span class="value">
                  <el-tag size="medium">{{ event.category || '未分类' }}</el-tag>
                </span>
              </div>
              <div class="meta-item">
                <span class="label">⏰ 创建时间:</span>
                <span class="value">{{ formatDate(event.createdAt) }}</span>
              </div>
              <div class="meta-item">
                <span class="label">✏️ 更新时间:</span>
                <span class="value">{{ formatDate(event.updatedAt) }}</span>
              </div>
            </div>

            <el-divider />

            <div class="content-section">
              <h3>内容</h3>
              <div class="event-content">
                {{ event.content || '暂无内容' }}
              </div>
            </div>
          </div>
        </el-card>

        <el-empty v-else description="事件不存在或已被删除" />
      </el-main>
    </el-container>

    <!-- 编辑对话框 -->
    <el-dialog v-model="showEditDialog" title="编辑事件" width="600px">
      <el-form :model="formData" label-width="80px">
        <el-form-item label="标题">
          <el-input v-model="formData.title" placeholder="请输入事件标题" />
        </el-form-item>
        <el-form-item label="内容">
          <el-input 
            v-model="formData.content" 
            type="textarea" 
            :rows="8"
            placeholder="请输入事件内容或想法" 
          />
        </el-form-item>
        <el-form-item label="分类">
          <el-input v-model="formData.category" placeholder="如：工作、学习、生活等" />
        </el-form-item>
        <el-form-item label="日期">
          <el-date-picker
            v-model="formData.eventDate"
            type="datetime"
            placeholder="选择日期时间"
            value-format="YYYY-MM-DD HH:mm:ss"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showEditDialog = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useEventStore } from '@/store/event'
import dayjs from 'dayjs'
import { ElMessage, ElMessageBox } from 'element-plus'

const route = useRoute()
const router = useRouter()
const eventStore = useEventStore()

const loading = ref(true)
const showEditDialog = ref(false)

const formData = ref({
  title: '',
  content: '',
  category: '',
  eventDate: ''
})

const event = ref(null)

const formatDate = (date) => {
  return dayjs(date).format('YYYY-MM-DD HH:mm:ss')
}

const handleEdit = () => {
  if (!event.value) return
  
  formData.value = {
    title: event.value.title,
    content: event.value.content || '',
    category: event.value.category || '',
    eventDate: dayjs(event.value.eventDate).format('YYYY-MM-DD HH:mm:ss')
  }
  showEditDialog.value = true
}

const handleSubmit = async () => {
  if (!formData.value.title) {
    ElMessage.warning('请输入事件标题')
    return
  }
  
  try {
    await eventStore.updateEvent(event.value.id, formData.value)
    ElMessage.success('更新成功')
    showEditDialog.value = false
    await loadEvent()
  } catch (error) {
    ElMessage.error('更新失败：' + error.message)
  }
}

const handleDelete = async () => {
  try {
    await ElMessageBox.confirm('确定要删除这个事件吗？此操作不可恢复', '警告', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await eventStore.deleteEvent(event.value.id)
    ElMessage.success('删除成功')
    router.push('/events')
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败：' + error.message)
    }
  }
}

const loadEvent = async () => {
  loading.value = true
  try {
    await eventStore.fetchEventById(route.params.id)
    event.value = eventStore.currentEvent
  } catch (error) {
    ElMessage.error('加载失败：' + error.message)
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadEvent()
})
</script>

<style scoped>
.detail-container {
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
  max-width: 900px;
  margin: 0 auto;
}

.loading-wrapper {
  padding: 40px;
}

.detail-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-actions {
  display: flex;
  gap: 10px;
}

.detail-content {
  padding: 10px 0;
}

.meta-info {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 15px;
  margin-bottom: 20px;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 10px;
}

.label {
  font-weight: bold;
  color: #666;
  min-width: 100px;
}

.value {
  color: #333;
}

.content-section h3 {
  margin-bottom: 15px;
  color: #409EFF;
}

.event-content {
  line-height: 1.8;
  color: #333;
  white-space: pre-wrap;
  background-color: #f5f7fa;
  padding: 20px;
  border-radius: 4px;
  min-height: 100px;
}
</style>
