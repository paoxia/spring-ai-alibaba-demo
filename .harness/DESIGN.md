# Design

设计决策记录。

## 关键设计决策

### 1. 使用 OpenAI 兼容接口而非 DashScope
**决策**: 排除 `spring-ai-alibaba-starter-dashscope`，使用 `spring-ai-starter-model-openai`
**原因**: 项目使用火山引擎 Ark (glm-4.7)，需要 OpenAI 兼容 API
**影响**: 需要 `@Qualifier("openAiChatModel")` 明确指定 Bean

### 2. ReactAgent 模式
**决策**: 使用 ReAct (Reasoning + Acting)
**原因**: 支持工具调用，适合复杂任务
**替代方案**: 纯聊天模式（无工具）

### 3. Plan & Exec 作为 Tool 实现
**决策**: 将计划执行能力作为 Agent 的工具集
**原因**: 与其他工具保持一致的集成方式
**位置**: `PlanTools.java`
