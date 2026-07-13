# WeatherReport

一个基于Android平台的天气预报应用，支持实时天气查询、城市搜索、天气预测和生活指数等功能。

## 🚀 功能特性

- **实时天气** - 显示当前温度、天气状况、湿度、风力等信息
- **城市搜索** - 支持搜索全国城市，快速切换查看不同城市天气
- **天气预报** - 提供多日天气预测数据
- **生活指数** - 展示穿衣、洗车、紫外线等生活建议
- **定位服务** - 自动获取当前位置天气
- **下拉刷新** - 支持下拉刷新更新天气数据

## 🛠️ 技术栈

- **语言**: Kotlin
- **框架**: Android Jetpack
  - ViewModel
  - LiveData
  - ViewBinding
- **网络**: Retrofit + OkHttp
- **JSON解析**: Gson
- **定位**: Google Play Services Location
- **UI组件**: Material Design

## 📱 界面预览

### 主界面
- 城市列表页面，支持搜索和定位

### 天气详情
- 实时天气信息展示
- 天气预报列表
- 生活指数卡片

## 📦 项目结构

```
app/src/main/java/com/rql/weatherreport/
├── common/          # 公共工具类
│   ├── db/          # 数据库操作
│   ├── ActivityMangeUtils.kt
│   ├── BaseActivity.kt
│   ├── LocationHelper.kt
│   ├── MyApplication.kt
│   ├── PlaceUtils.kt
│   ├── RetrofitManager.kt
│   └── Sky.kt
├── network/         # 网络层
│   ├── api/         # API接口定义
│   ├── dto/         # 数据传输对象
│   └── repository/  # 数据仓库
└── ui/              # 界面层
    ├── activity/    # 活动
    ├── adapter/     # 适配器
    ├── fragment/    # 碎片
    └── viewmodel/   # 视图模型
```

## ⚙️ 环境要求

- Android Studio Jellyfish | 2023.3.1+
- Gradle 8.13+
- Kotlin 1.9+
- Android SDK 29+

## 📋 安装步骤

1. **克隆项目**
   ```bash
   git clone https://github.com/HermitIX-I/Weather-Report.git
   ```

2. **打开项目**
   - 使用 Android Studio 打开项目根目录
   - 等待 Gradle 同步完成

3. **配置API密钥**
   - 在 `RetrofitManager.kt` 中配置天气API密钥

4. **运行应用**
   - 连接Android设备或启动模拟器
   - 点击运行按钮

## 🔑 权限说明

应用需要以下权限：

| 权限 | 用途 |
|------|------|
| INTERNET | 访问天气API |
| ACCESS_FINE_LOCATION | 精确定位 |
| ACCESS_COARSE_LOCATION | 粗略定位 |
| ACCESS_BACKGROUND_LOCATION | 后台定位 |

## 📝 License

MIT License

## 🤝 贡献

欢迎提交Issue和Pull Request！
