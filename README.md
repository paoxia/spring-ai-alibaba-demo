# Spring AI Alibaba Chat Agent Demo

这是一个使用 Spring AI Alibaba 构建的 Chat Agent 示例项目。

## 功能特性

- 基于 Spring AI Alibaba 框架
- 支持对话式 AI 交互
- 提供 REST API 接口
- 集成通义千问模型
- 内置 Chat UI 界面（通过 spring-ai-alibaba-studio）

## 前置要求

- Java 17+
- Maven 3.6+
- 通义千问 API Key（从 [阿里云百炼](https://bailian.console.aliyun.com/) 获取）

## 配置说明

### 方式一：环境变量配置

```bash
# Linux/Mac
export AI_DASHSCOPE_API_KEY=your-api-key-here

# Windows PowerShell
$env:AI_DASHSCOPE_API_KEY="your-api-key-here"

# Windows CMD
set AI_DASHSCOPE_API_KEY=your-api-key-here
```

### 方式二：修改 application.yml

编辑 `src/main/resources/application.yml` 文件，设置你的 API Key：

```yaml
spring:
  ai:
    dashscope:
      api-key: your-actual-api-key
```

## 运行项目

### 1. 编译项目

```bash
mvn clean install
```

### 2. 启动应用

```bash
mvn spring-boot:run
```

或直接运行：

```bash
java -jar target/spring-ai-alibaba-demo-1.0.0.jar
```

## 访问应用

### Chat UI 界面

启动应用后，访问以下地址使用内置的 Chat UI：

```
http://localhost:8080/chatui/index.html
```

### REST API 接口

#### 发送聊天消息

```bash
curl -X POST http://localhost:8080/api/chat \
  -H "Content-Type: application/json" \
  -d '{"message": "你好，请介绍一下自己"}'
```

**响应示例：**

```json
{
  "reply": "你好！我是一个基于AI的聊天助手，使用Spring AI Alibaba框架构建..."
}
```

## 项目结构

```
src/main/java/com/example/demo/
├── DemoApplication.java          # 主应用类
├── agent/
│   └── SimpleChatAgent.java      # 聊天代理实现
├── controller/
│   └── ChatController.java       # REST API 控制器
└── config/
    └── ChatAgentConfig.java      # Chat Agent 配置
```

## 扩展功能

### 添加记忆功能

```java
@Bean
public ChatMemory chatMemory() {
    return new InMemoryChatMemory();
}
```

### 添加 RAG 支持

```java
@Bean
public VectorStore vectorStore(EmbeddingModel embeddingModel) {
    return new SimpleVectorStore(new SimpleVectorStoreConfig("vectorstore.json"));
}
```

### 自定义 Chat Agent

```java
public class CustomChatAgent {
    private final ChatClient chatClient;

    public CustomChatAgent(ChatModel chatModel) {
        this.chatClient = ChatClient.builder(chatModel)
                .defaultSystem("你是一个专业的技术顾问")
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(new InMemoryChatMemory())
                )
                .build();
    }
}
```

## 参考文档

- [Spring AI Alibaba 官方文档](https://java2ai.com/docs/frameworks/studio/quick-start)
- [通义千问 API 文档](https://help.aliyun.com/zh/dashscope/)
- [Spring AI 官方文档](https://docs.spring.io/spring-ai/reference/)

## 常见问题

**Q: 如何更换模型？**

A: 在 `application.yml` 中修改 `model` 属性：
```yaml
spring:
  ai:
    dashscope:
      chat:
        options:
          model: qwen-turbo  # 或 qwen-plus, qwen-max 等
```

**Q: 如何调整生成参数？**

A: 在 `application.yml` 中调整 temperature、top-p 等参数：
```yaml
spring:
  ai:
    dashscope:
      chat:
        options:
          temperature: 0.7  # 控制随机性，0-1
          top-p: 0.9       # 核采样参数
```

## 许可证

MIT License
