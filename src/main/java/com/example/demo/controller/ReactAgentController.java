package com.example.demo.controller;

import com.alibaba.cloud.ai.graph.agent.ReactAgent;
import com.alibaba.cloud.ai.graph.exception.GraphRunnerException;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.web.bind.annotation.*;

/**
 * React Agent REST API 控制器
 * 提供 HTTP 接口，允许客户端通过 API 直接与 React Agent 进行交互
 * 除了 Studio UI 之外，也可以通过此 API 进行集成
 */
@RestController
@RequestMapping("/api/agent")
public class ReactAgentController {

    /**
     * 注入的 ReactAgent 实例，用于处理用户的聊天请求
     */
    private final ReactAgent reactAgent;

    /**
     * 构造函数注入 ReactAgent
     *
     * @param reactAgent 要使用的 ReactAgent 实例
     */
    public ReactAgentController(ReactAgent reactAgent) {
        this.reactAgent = reactAgent;
    }

    /**
     * 与 ReactAgent 进行聊天交互的 API 端点
     * 接收用户消息，传递给 Agent 处理，并返回 AI 的回复
     *
     * @param request 包含用户消息的请求体
     * @return 包含 AI 回复的响应对象
     * @throws RuntimeException 当 Agent 执行失败时抛出运行时异常
     */
    @PostMapping
    public AgentResponse chat(@RequestBody AgentRequest request) {
        try {
            // 调用 ReactAgent 处理用户消息
            AssistantMessage response = reactAgent.call(request.message());
            // 将响应转换为字符串并返回
            return new AgentResponse(response.toString());
        } catch (GraphRunnerException e) {
            // 捕获图执行异常并转换为运行时异常
            throw new RuntimeException("Agent execution failed: " + e.getMessage(), e);
        }
    }

    /**
     * 聊天请求 DTO (Data Transfer Object)
     * 用于封装客户端发送的用户消息
     *
     * @param message 用户输入的消息内容
     */
    public record AgentRequest(String message) {}

    /**
     * 聊天响应 DTO (Data Transfer Object)
     * 用于封装返回给客户端的 AI 回复
     *
     * @param reply AI 生成的回复内容
     */
    public record AgentResponse(String reply) {}
}
