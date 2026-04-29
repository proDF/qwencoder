<template>
  <div class="events-container">
    <el-container>
      <el-header class="header">
        <h1>📋 事件列表</h1>
        <el-menu mode="horizontal" background-color="#409EFF" text-color="#fff" active-text-color="#ffd04b">
          <el-menu-item index="1" @click="$router.push('/')">首页</el-menu-item>
          <el-menu-item index="2" @click="$router.push('/events')">事件列表</el-menu-item>
        </el-menu>
      </el-header>

      <el-main class="main-content">
        <!-- 搜索和筛选区域 -->
        <el-card class="filter-card">
          <el-row :gutter="20">
            <el-col :span="8">
              <el-input
                v-model="searchKeyword"
                placeholder="搜索标题或内容"
                clearable
                @keyup.enter="handleSearch"
              >
                <template #append>
                  <el-button @click="handleSearch">搜索</el-button>
                </template>
              </el-input>
            </el-col>
            <el-col :span="6">
              <el-select v-model="selectedYear" placeholder="选择年份" @change="handleYearChange">
                <el-option label="全部年份" :value="null" />
                <el-option v-for="year in yearOptions" :key="year" :label="year + '年'" :value="year" />
              </el-select>
            </el-col>
            <el-col :span="6">
              <el-select v-model="selectedMonth" placeholder="选择月份" @change="handleMonthChange" :disabled="!selectedYear">
                <el-option label="全部月份" :value="null" />
                <el-option v-for="month in 12" :key="month" :label="month + '月'" :value="month" />
              </el-select>
            </el-col>
            <el-col :span="4">
              <el-button type="primary" @click="resetFilters">重置</el-button>
            </el-col>
          </el-row>
        </el-card>

        <!-- 事件列表 -->
        <el-card class="events-card">
          <template #header>
            <div class="card-header">
              <span>事件列表 ({{ filteredEvents.length }})</span>
              <el-button type="primary" @click="showCreateDialog = true">➕ 新建事件</el-button>
            </div>
          </template>

          <el-table 
            :data="filteredEvents" 
            style="width: 100%" 
            v-loading="loading"
            @row-click="viewEvent"
            row-style="cursor: pointer;"
          >
            <el-table-column prop="title" label="标题" min-width="200" />
            <el-table-column prop="category" label="分类" width="100">
              <template #default="{ row }">
                <el-tag size="small" type="info">{{ row.category || '未分类' }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="eventDate" label="日期" width="180">
              <template #default="{ row }">
                {{ formatDate(row.eventDate) }}
              </template>
            </el-table-column>
            <el-table-column label="操作" width="200" fixed="right">
              <template #default="{ row }">
                <el-button type="primary" size="small" @click.stop="viewEvent(row.id)">查看</el-button>
                <el-button type="success" size="small" @click.stop="editEvent(row)">编辑</el-button>
                <el-button type="danger" size="small" @click.stop="confirmDelete(row.id)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-main>
    </el-container>

    <!-- 创建/编辑事件对话框 -->
    <el-dialog v-model="showCreateDialog" :title="editingEvent ? '编辑事件' : '新建事件'" width="600px">
      <el-form :model="formData" label-width="80px">
        <el-form-item label="标题">
          <el-input v-model="formData.title" placeholder="请输入事件标题" />
        </el-form-item>
        <el-form-item label="内容">
          <el-input 
            v-model="formData.content" 
            type="textarea" 
            :rows="5"
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
        <el-button @click="showCreateDialog = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">{{ editingEvent ? '更新' : '创建' }}</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useEventStore } from '@/store/event'
import dayjs from 'dayjs'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter()
const eventStore = useEventStore()

const loading = ref(false)
const searchKeyword = ref('')
const selectedYear = ref(null)
const selectedMonth = ref(null)
const showCreateDialog = ref(false)
const editingEvent = ref(null)

const formData = ref({
  title: '',
  content: '',
  category: '',
  eventDate: dayjs().format('YYYY-MM-DD HH:mm:ss')
})

const yearOptions = computed(() => {
  const years = new Set()
  const currentYear = dayjs().year()
  
  eventStore.events.forEach(event => {
    const year = dayjs(event.eventDate).year()
    years.add(year)
  })
  
  // 添加当前年和未来一年
  years.add(currentYear)
  years.add(currentYear + 1)
  
  return Array.from(years).sort((a, b) => b - a)
})

const filteredEvents = computed(() => {
  let events = [...eventStore.events]
  
  // 按年份筛选
  if (selectedYear.value !== null) {
    events = events.filter(e => dayjs(e.eventDate).year() === selectedYear.value)
  }
  
  // 按月份筛选
  if (selectedMonth.value !== null && selectedYear.value !== null) {
    events = events.filter(e => dayjs(e.eventDate).month() === selectedMonth.value - 1)
  }
  
  // 按关键词搜索
  if (searchKeyword.value) {
    const keyword = searchKeyword.value.toLowerCase()
    events = events.filter(e => 
      e.title.toLowerCase().includes(keyword) || 
      (e.content && e.content.toLowerCase().includes(keyword))
    )
  }
  
  // 按日期倒序排序
  return events.sort((a, b) => dayjs(b.eventDate).valueOf() - dayjs(a.eventDate).valueOf())
})

const formatDate = (date) => {
  return dayjs(date).format('YYYY-MM-DD HH:mm')
}

const handleSearch = () => {
  // 搜索逻辑已在 computed 中实现
}

const handleYearChange = () => {
  if (!selectedYear.value) {
    selectedMonth.value = null
  }
}

const handleMonthChange = () => {
  // 月份筛选逻辑已在 computed 中实现
}

const resetFilters = () => {
  searchKeyword.value = ''
  selectedYear.value = null
  selectedMonth.value = null
}

const viewEvent = (row) => {
  // 兼容两种情况：传入的是 row 对象或 id
  const id = typeof row === 'object' ? row.id : row
  router.push(`/events/${id}`)
}

const editEvent = (event) => {
  editingEvent.value = event
  formData.value = {
    title: event.title,
    content: event.content || '',
    category: event.category || '',
    eventDate: dayjs(event.eventDate).format('YYYY-MM-DD HH:mm:ss')
  }
  showCreateDialog.value = true
}

const handleSubmit = async () => {
  if (!formData.value.title) {
    ElMessage.warning('请输入事件标题')
    return
  }
  
  try {
    if (editingEvent.value) {
      await eventStore.updateEvent(editingEvent.value.id, formData.value)
      ElMessage.success('事件更新成功')
    } else {
      await eventStore.createEvent(formData.value)
      ElMessage.success('事件创建成功')
    }
    showCreateDialog.value = false
    resetForm()
  } catch (error) {
    ElMessage.error('操作失败：' + error.message)
  }
}

const confirmDelete = async (id) => {
  try {
    await ElMessageBox.confirm('确定要删除这个事件吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await eventStore.deleteEvent(id)
    ElMessage.success('删除成功')
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败：' + error.message)
    }
  }
}

const resetForm = () => {
  editingEvent.value = null
  formData.value = {
    title: '',
    content: '',
    category: '',
    eventDate: dayjs().format('YYYY-MM-DD HH:mm:ss')
  }
}

onMounted(async () => {
  loading.value = true
  await eventStore.fetchAllEvents()
  loading.value = false
})
</script>

<style scoped>
.events-container {
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

.filter-card {
  margin-bottom: 20px;
}

.events-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
