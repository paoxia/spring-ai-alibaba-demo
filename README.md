# Spring AI Alibaba React Agent Demo

这是一个使用 Spring AI Alibaba ReactAgent 和 OpenAI 兼容 API 构建的智能代理示例项目。

## 功能特性

- 基于 Spring AI Alibaba ReactAgent 框架
- 支持 OpenAI 兼容 API（可接入任何 OpenAI 风格的模型）
- 支持多个专业化的 ReactAgent（通用对话、代码助手、天气助手）
- 提供内置 Chat UI（通过 spring-ai-alibaba-studio）
- 提供 REST API 接口
- 支持 Chat UI 绑定 ReactAgent

## 前置要求

- Java 17+
- Maven 3.6+
- OpenAI 兼容的 API Key（如 OpenAI、Azure OpenAI、或其他兼容服务）

## 配置说明

### 方式一：编辑 application.yml

编辑 `src/main/resources/application.yml`：

```yaml
spring:
  ai:
    openai:
      api-key: your-api-key-here
      base-url: https://api.openai.com/v1  # 或其他兼容的 API 地址
      chat:
        options:
          model: gpt-3.5-turbo
          temperature: 0.7
```

### 方式二：环境变量配置

```bash
# Windows PowerShell
$env:OPENAI_API_KEY="your-api-key-here"
$env:OPENAI_BASE_URL="https://api.openai.com/v1"
$env:OPENAI_MODEL="gpt-3.5-turbo"

# Linux/Mac
export OPENAI_API_KEY=your-api-key-here
export OPENAI_BASE_URL=https://api.openai.com/v1
export OPENAI_MODEL=gpt-3.5-turbo
```

## 可用的 ReactAgent

| Agent 名称 | 描述 | 特点 |
|------------|------|------|
| chat_agent | 通用对话助手 | 能够回答用户的各种问题 |
| code_assistant_agent | 代码助手 | 专门用于编写、调试和优化代码 |
| weather_agent | 天气查询助手 | 帮助查询天气信息 |

## ReactAgent 原理

ReactAgent 是基于 **ReAct (Reasoning + Acting)** 范式的智能代理：

```
用户输入 → 思考 → 行动 → 结果反馈 → 重新思考 → ... → 最终答案
```

ReactAgent 会：
1. **Reasoning**: 分析用户请求，思考需要执行什么操作
2. **Acting**: 调用工具或API执行相应操作
3. **Observation**: 观察操作结果
4. **Loop**: 如果需要，重复上述步骤直到获得最终答案

## 运行项目

### 1. 编译项目

```bash
mvn clean install
```

### 2. 启动应用

```bash
mvn spring-boot:run
```

或直接运行打包好的 jar：

```bash
java -jar target/spring-ai-alibaba-demo-1.0.0.jar
```

## 访问应用

### Chat UI 界面

启动应用后，访问以下地址使用内置的 Chat UI：

```
http://localhost:8080/chatui/index.html
```

Chat UI 会自动绑定到配置的 ReactAgent，可以直接进行交互。

### REST API 接口

#### 获取所有 Agent 列表

```bash
curl http://localhost:8080/api/agent/list
```

**响应示例：**

```json
[
  {
    "name": "chat_agent",
    "description": "通用对话助手"
  },
  {
    "name": "code_assistant_agent",
    "description": "代码助手"
  },
  {
    "name": "weather_agent",
    "description": "天气查询助手"
  }
]
```

#### 与指定 ReactAgent 对话

```bash
curl -X POST http://localhost:8080/api/agent/chat_agent \
  -H "Content-Type: application/json" \
  -d '{"message": "你好，请介绍一下你自己"}'
```

**响应示例：**

```json
{
  "reply": "你好！我是一个基于 ReactAgent 的智能助手，...",
  "agentName": "chat_agent"
}
```

#### 使用默认 Agent（chat_agent）

```bash
curl -X POST http://localhost:8080/api/agent \
  -H "Content-Type: application/json" \
  -d '{"message": "请帮我写一个快速排序算法"}'
```

## 项目结构

```
src/main/java/com/example/demo/
├── DemoApplication.java              # 主应用类
├── agent/
│   └── ReactAgentService.java        # ReactAgent 配置和管理
└── controller/
    └── AgentController.java           # REST API 控制器
```

## 支持 OpenAI 兼容的 API

本项目支持任何 OpenAI 兼容的 API，包括：

- **OpenAI**: `https://api.openai.com/v1`
- **Azure OpenAI**: `https://your-resource.openai.azure.com/`
- **通义千问**: 支持兼容模式
- **智谱 AI**: 支持兼容模式
- **DeepSeek**: `https://api.deepseek.com/v1`
- **Moonshot**: `https://api.moonshot.cn/v1`

配置示例：

```yaml
spring:
  ai:
    openai:
      # OpenAI
      api-key: sk-...
      base-url: https://api.openai.com/v1

      # DeepSeek
      # api-key: sk-...
      # base-url: https://api.deepseek.com/v1

      # Moonshot
      # api-key: sk-...
      # base-url: https://api.moonshot.cn/v1
```

## 自定义 ReactAgent

在 `ReactAgentService.java` 中添加新的 ReactAgent：

```java
@Bean
public ReactAgent customAgent(ChatModel chatModel) {
    return ReactAgent.builder()
            .name("custom_agent")
            .model(chatModel)
            .instruction("你是一个专业助手，擅长...")
            .description("自定义助手")
            .build();
}
```

### 添加工具支持

ReactAgent 支持添加工具函数，让 Agent 具备执行实际操作的能力：

```java
@Bean
public ReactAgent weatherAgentWithTools(ChatModel chatModel) {
    return ReactAgent.builder()
            .name("weather_agent")
            .model(chatModel)
            .instruction("你是一个天气助手，可以使用天气工具查询信息。")
            .tools(weatherTool())
            .build();
}

@Bean
public FunctionCallback weatherTool() {
    return FunctionCallback.builder()
            .function("getWeather", "查询天气信息")
            .inputType(WeatherRequest.class)
            .resultType(WeatherResponse.class)
            .build();
}
```

## 扩展功能

### 添加记忆功能

ReactAgent 支持状态记忆，可以跨对话保存信息：

```java
@Bean
public ReactAgent agentWithMemory(ChatModel chatModel) {
    return ReactAgent.builder()
            .name("memory_agent")
            .model(chatModel)
            .instruction("记住用户的偏好设置。")
            .saver(checkpointSaver())
            .build();
}
```

### 添加钩子（Hooks）

在 Agent 执行过程中插入自定义逻辑：

```java
@Bean
public ReactAgent agentWithHooks(ChatModel chatModel) {
    return ReactAgent.builder()
            .name("hooked_agent")
            .model(chatModel)
            .instruction("...")
            .hooks(
                new InstructionAgentHook(),
                new MessagesAgentHook()
            )
            .build();
}
```

## 参考文档

- [Spring AI Alibaba 官方文档](https://java2ai.com/docs/)
- [Spring AI Alibaba ReactAgent 文档](https://java2ai.com/docs/frameworks/react/)
- [Spring AI 官方文档](https://docs.spring.io/spring-ai/reference/)
- [OpenAI API 文档](https://platform.openai.com/docs/api-reference)

## 常见问题

**Q: ReactAgent 与普通 ChatClient 有什么区别？**

A: ReactAgent 基于 ReAct 范式，具有：
- 自主思考和行动能力
- 工具调用能力
- 状态管理和持久化
- 可观察的执行流程
- 支持中断和恢复

**Q: 如何更换模型？**

A: 在 `application.yml` 中修改 `model` 属性：
```yaml
spring:
  ai:
    openai:
      chat:
        options:
          model: gpt-4  # 或其他模型
```

**Q: 如何调整生成参数？**

A: 在 `application.yml` 中调整参数：
```yaml
spring:
  ai:
    openai:
      chat:
        options:
          temperature: 0.7  # 控制随机性，0-1
          top-p: 0.9       # 核采样参数
          max-tokens: 2000  # 最大生成 token 数
```

**Q: Chat UI 如何绑定到不同的 ReactAgent？**

A: 在 Chat UI 界面中，可以通过 API 端点指定不同的 ReactAgent：
- `POST /api/agent/chat_agent` - 使用通用对话 Agent
- `POST /api/agent/code_assistant_agent` - 使用代码助手
- `POST /api/agent/weather_agent` - 使用天气助手

## 许可证

MIT License
