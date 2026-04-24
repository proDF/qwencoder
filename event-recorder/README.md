# 事件记录器 (Event Recorder)

一个用于记录每天事件、想法和历史的微信小程序风格 Web 应用。

## 技术栈

### 后端
- **Spring Boot** 2.7.18
- **Spring Data JPA** - 数据持久化
- **MySQL** 8.0 - 数据库
- **Maven** - 项目构建工具

### 前端
- **Vue 3** - 渐进式 JavaScript 框架
- **Vite** - 现代前端构建工具
- **Vue Router** - 路由管理
- **Pinia** - 状态管理
- **Element Plus** - UI 组件库
- **Axios** - HTTP 客户端
- **Day.js** - 日期处理

## 功能特性

- ✅ 记录每天的事件和想法
- ✅ 查看历史事件
- ✅ 按年/月查询事件
- ✅ 搜索功能（标题和内容）
- ✅ 事件的增删改查
- ✅ 事件分类
- ✅ 响应式设计

## 快速开始

### 环境要求
- JDK 11+
- MySQL 8.0+
- Node.js 16+
- Maven 3.6+

### 1. 数据库配置

创建 MySQL 数据库：

```sql
CREATE DATABASE event_recorder CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

修改后端配置文件 `backend/src/main/resources/application.properties`：

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/event_recorder?useSSL=false&serverTimezone=UTC&characterEncoding=utf8
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 2. 启动后端

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

后端服务将在 http://localhost:8080 启动

### 3. 启动前端

```bash
cd frontend
npm install
npm run dev
```

前端服务将在 http://localhost:3000 启动

## API 接口

| 方法 | 路径 | 描述 |
|------|------|------|
| GET | /api/events | 获取所有事件 |
| GET | /api/events/{id} | 获取单个事件 |
| POST | /api/events | 创建事件 |
| PUT | /api/events/{id} | 更新事件 |
| DELETE | /api/events/{id} | 删除事件 |
| GET | /api/events/month/{year}/{month} | 按年月查询 |
| GET | /api/events/year/{year} | 按年份查询 |
| GET | /api/events/search?keyword=xxx | 搜索事件 |
| GET | /api/events/range?startDate=xxx&endDate=xxx | 按日期范围查询 |

## 项目结构

```
event-recorder/
├── backend/                    # 后端项目
│   ├── src/main/java/com/eventrecorder/
│   │   ├── EventRecorderApplication.java
│   │   ├── controller/        # 控制器层
│   │   ├── service/           # 服务层
│   │   ├── repository/        # 数据访问层
│   │   ├── entity/            # 实体类
│   │   └── config/            # 配置类
│   └── src/main/resources/
│       └── application.properties
│
└── frontend/                   # 前端项目
    ├── src/
    │   ├── api/               # API 调用
    │   ├── components/        # 组件
    │   ├── views/             # 页面视图
    │   ├── router/            # 路由配置
    │   ├── store/             # 状态管理
    │   ├── App.vue
    │   └── main.js
    ├── index.html
    ├── package.json
    └── vite.config.js
```

## 注意事项

1. 确保 MySQL 服务已启动
2. 首次运行时，JPA 会自动创建数据库表
3. 前端开发服务器会代理 API 请求到后端
4. 生产环境部署时，需要构建前端并配置正确的 API 地址

## 后续扩展建议

- 🔐 添加用户认证系统
- 📱 开发微信小程序版本
- ☁️ 添加数据导出功能
- 📊 添加统计图表
- 🏷️ 支持标签系统
- 📸 支持图片上传
