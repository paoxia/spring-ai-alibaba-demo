# Spring AI Alibaba Demo 项目文档

## 项目概述

这是一个基于 **Spring AI Alibaba** 的 React Agent 演示项目，展示了如何集成 Spring AI Alibaba 框架来构建智能 AI 助手应用。

## 技术栈

- **Spring Boot**: 3.3.6
- **Spring AI**: 1.1.2
- **Spring AI Alibaba**: 1.1.2.0
- **Java**: 17
- **构建工具**: Maven (多模块)

## 模块结构

```
spring-ai-alibaba-demo/
├── pom.xml                      # 父POM，管理依赖版本
├── demo-common/                  # 公共模块
│   ├── pom.xml
│   └── src/main/java/com/example/demo/common/
│       └── tools/                # 工具类（计算器、时间、天气、字符串、计划等）
├── demo-service/                 # 服务模块
│   ├── pom.xml
│   ├── src/main/java/com/example/demo/service/
│   │   └── agent/                # Agent 相关业务逻辑
│   └── src/main/resources/
│       └── skills/               # 技能配置文件
└── demo-starter/                 # 启动模块
    ├── pom.xml
    └── src/main/
        ├── java/com/example/demo/
        │   ├── DemoApplication.java      # 应用入口
        │   └── controller/               # REST API 控制器
        └── resources/
            └── application.yml            # 配置文件
```

## 模块说明

| 模块 | 依赖 | 说明 |
|------|------|------|
| demo-common | spring-boot-starter, spring-ai-starter-model-openai | 公共工具类，被其他模块依赖 |
| demo-service | demo-common, spring-ai-alibaba-*, spring-ai-starter-model-openai | Agent 业务逻辑，技能配置 |
| demo-starter | demo-service, spring-boot-starter-web | 应用入口，Controller，端口配置 |

## 核心依赖

| 依赖 | 版本 | 用途 |
|------|------|------|
| spring-ai-alibaba-studio | 1.1.2.0 | 提供 Studio UI 界面 |
| spring-ai-alibaba-agent-framework | 1.1.2.0 | Agent 框架核心 |
| spring-ai-starter-model-openai | 1.1.2 | OpenAI 兼容模型支持 |

## 关键配置

### application.yml 主要配置

```yaml
server:
  port: 8081  # 应用端口

spring:
  ai:
    openai:
      api-key: ${OPENAI_API_KEY:your-api-key-here}
      base-url: https://ark.cn-beijing.volces.com/api/coding
      chat:
        options:
          model: glm-4.7
          temperature: 0.7
          top-p: 0.9
          max-tokens: 2000
```

## 访问方式

### 1. Studio UI (推荐)
- **URL**: `http://localhost:8081/chatui/index.html`
- **Agent 名称**: `research_agent`
- **功能**: 提供可视化的聊天界面，可直接与 AI 交互

### 2. REST API
- **端点**: `POST http://localhost:8081/api/agent`
- **请求体**:
  ```json
  {
    "message": "你的问题"
  }
  ```
- **响应**:
  ```json
  {
    "reply": "AI 回复内容"
  }
  ```

## 核心类说明

### CustomAgentLoader
- **作用**: 实现 `AgentLoader` 接口，向 Studio UI 提供可用的 Agent 列表
- **当前 Agent**: `research_agent`, `sql_assistant`
- **位置**: `com.example.demo.service.agent.CustomAgentLoader`

### ReactAgentService
- **作用**: 配置并创建 ReactAgent Bean
- **模型**: 使用 `openAiChatModel`（通过 `@Qualifier` 指定）
- **系统指令**: "你是一个有帮助的AI助手，能够回答用户的问题并提供有用的信息。"
- **位置**: `com.example.demo.service.agent.ReactAgentService`

### ReactAgentController
- **作用**: 提供 REST API 接口
- **端点**: `/api/agent` (POST)
- **位置**: `com.example.demo.controller.ReactAgentController`

## 构建与运行

```bash
# 构建所有模块
mvn clean package

# 运行启动模块
cd demo-starter
mvn spring-boot:run

# 或者直接运行 JAR
java -jar demo-starter/target/demo-starter-1.0.0.jar
```

## 环境变量

| 变量名 | 说明 | 默认值 |
|--------|------|--------|
| OPENAI_API_KEY | OpenAI 兼容 API 的密钥 | your-api-key-here |

## 使用说明

1. **设置 API Key**: 设置环境变量 `OPENAI_API_KEY` 或在 `application.yml` 中直接配置
2. **启动应用**: 运行 `DemoApplication` 主类
3. **访问 UI**: 浏览器打开 `http://localhost:8081/chatui/index.html`
4. **选择 Agent**: 在 Studio UI 中选择 `research_agent`
5. **开始聊天**: 在输入框中发送消息与 AI 交互

## 注意事项

1. **模型配置**: 当前使用火山引擎的 `glm-4.7` 模型，可根据需要修改 `application.yml` 中的 `base-url` 和 `model`
2. **排除 DashScope**: `spring-ai-alibaba-studio` 依赖已排除 `spring-ai-alibaba-starter-dashscope`，避免冲突
3. **端口**: 默认端口为 8081，可在 `application.yml` 中修改
4. **多模块依赖**: `demo-service` 依赖 `demo-common`，`demo-starter` 依赖 `demo-service`

## 版本兼容说明

- Spring AI Alibaba 1.1.2.0 需要配合 Spring AI 1.1.2 使用
- 最初使用 Spring AI 1.0.0-M5 会导致 `NoSuchMethodError`，已修复
