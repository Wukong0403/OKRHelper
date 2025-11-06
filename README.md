# OKR助手 📊

一款简洁高效的季度OKR规划与任务管理Android应用，帮助你更好地制定和追踪目标。

## ✨ 功能特性

### 🎯 三级任务管理体系
- **季度OKR管理**：设定季度目标(Objectives)和关键结果(Key Results)
- **月度任务拆分**：将季度OKR拆解为每月具体任务
- **周任务细化**：进一步将月度任务细化到每周执行计划

### 📱 直观的用户界面
- **可展开列表**：点击即可展开/收起，层级关系一目了然
- **任务状态追踪**：一键标记任务完成状态
- **Material 3 设计**：现代化的UI设计，美观流畅
- **三层卡片设计**：不同颜色区分不同层级，视觉清晰

### 💾 数据持久化
- **本地存储**：使用Room数据库，数据安全可靠
- **自动保存**：所有操作即时保存，无需担心数据丢失
- **级联删除**：删除上层任务时自动清理关联的子任务

## 🖼️ 界面展示

应用采用三层卡片折叠设计：
- 🟦 蓝色卡片：季度OKR
- 🟩 青色卡片：月度任务
- 🟨 橙色卡片：周任务

点击卡片即可展开查看详细信息和下级任务。

## 🚀 使用方法

### 创建OKR
1. 点击主界面右下角的 ➕ 按钮
2. 填写年份、季度、目标和关键结果
3. 点击确定保存

### 添加月度任务
1. 点击OKR卡片展开
2. 在展开的月度任务区域点击"添加月度任务"
3. 填写月份、任务标题和描述

### 添加周任务
1. 展开OKR卡片
2. 点击对应的月度任务卡片展开
3. 在周任务区域点击"添加周任务"
4. 填写周数、任务标题和描述

### 标记完成状态
点击任务卡片右侧的 ✓ 图标即可标记/取消完成状态。

## 🛠️ 技术栈

- **语言**：Kotlin
- **UI框架**：Jetpack Compose
- **架构模式**：MVVM
- **数据库**：Room
- **导航**：Navigation Compose (已移除，采用单页面设计)
- **异步处理**：Kotlin Coroutines + Flow

## 📦 项目结构

```
app/src/main/java/com/example/myapplication/
├── data/                    # 数据层
│   ├── QuarterOKR.kt       # OKR实体
│   ├── MonthlyTask.kt      # 月度任务实体
│   ├── WeeklyTask.kt       # 周任务实体
│   ├── OKRDao.kt           # 数据访问对象
│   ├── OKRDatabase.kt      # 数据库
│   └── OKRRepository.kt    # 数据仓库
├── ui/
│   ├── screens/            # 界面层
│   │   ├── OKRListScreen.kt    # 主界面（可展开列表）
│   │   └── Dialogs.kt          # 对话框组件
│   └── theme/              # 主题配置
│       ├── Color.kt
│       ├── Theme.kt
│       └── Type.kt
├── viewmodel/              # 视图模型层
│   └── OKRViewModel.kt
└── MainActivity.kt         # 主Activity
```

## 📋 系统要求

- Android 7.0 (API 24) 或更高版本
- 支持Android 14 (API 34)

## 🔧 构建说明

```bash
# 克隆项目
git clone https://github.com/Wukong0403/OKRHelper.git

# 进入项目目录
cd OKRHelper

# 构建Debug版本
./gradlew assembleDebug

# 构建Release版本
./gradlew assembleRelease
```

生成的APK位于：`app/build/outputs/apk/debug/app-debug.apk`

## 📝 开发计划

- [ ] 添加数据导出功能（JSON/CSV）
- [ ] 支持任务编辑和删除
- [ ] 添加进度统计图表
- [ ] 支持任务提醒通知
- [ ] 添加任务标签分类
- [ ] 支持云端同步

## 🤝 贡献指南

欢迎提交Issue和Pull Request！

## 📄 开源协议

本项目采用 MIT 协议开源。

## 👨‍💻 作者

Generated with [Claude Code](https://claude.com/claude-code)

---

⭐ 如果这个项目对你有帮助，欢迎给个Star支持一下！
