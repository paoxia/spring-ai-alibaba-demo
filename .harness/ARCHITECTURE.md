# Architecture

Spring AI Alibaba Demo 项目架构文档

## 技术栈

| 组件 | 版本 |
|------|------|
| Spring Boot | 3.3.6 |
| Spring AI | 1.1.2 |
| Spring AI Alibaba | 1.1.2.0 |
| Java | 17 |

## 项目结构

```
src/main/java/com/example/demo/
├── DemoApplication.java          # 应用入口
├── agent/
│   ├── ReactAgentService.java   # Agent 配置
│   └── CustomAgentLoader.java    # Agent 加载器
├── controller/
│   └── ReactAgentController.java # REST API
└── tools/
    ├── CalculatorTools.java      # 计算器工具
    ├── TimeTools.java            # 时间工具
    ├── WeatherTools.java         # 天气工具
    ├── StringUtilsTools.java   # 字符串工具
    └── PlanTools.java          # 计划执行工具
```

## 核心架构

### 1. Agent 层
- **ReactAgent**: ReAct 模式智能 Agent
- **ToolCallbacks**: 工具回调机制
- **AgentLoader**: Studio 集成

### 2. 工具层
- **CalculatorTools**: 数学计算
- **TimeTools**: 时间查询
- **WeatherTools**: 天气查询
- **StringUtilsTools**: 字符串处理
- **PlanTools**: 复杂任务拆解与执行

### 3. API 层
- **ReactAgentController**: REST API 端点

## 数据流

```
用户请求 → REST API / Studio UI
    ↓
ReactAgent
    ↓
ChatModel (glm-4.7)
    ↓
工具调用 (可选)
    ↓
响应返回
```
