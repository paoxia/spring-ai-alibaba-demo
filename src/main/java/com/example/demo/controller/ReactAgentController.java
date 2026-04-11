package com.example.demo.controller;

import com.alibaba.cloud.ai.graph.agent.ReactAgent;
import com.alibaba.cloud.ai.graph.exception.GraphRunnerException;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.web.bind.annotation.*;

/**
 * React Agent 控制器
 * 提供 REST API 接口与 React Agent 进行交互
 */
@RestController
@RequestMapping("/api/agent")
public class ReactAgentController {

    private final ReactAgent reactAgent;

    public ReactAgentController(ReactAgent reactAgent) {
        this.reactAgent = reactAgent;
    }

    /**
     * 与 ReactAgent 交互
     * @param request 聊天请求
     * @return AI 回复
     */
    @PostMapping
    public AgentResponse chat(@RequestBody AgentRequest request) {
        try {
            AssistantMessage response = reactAgent.call(request.message());
            return new AgentResponse(response.toString());
        } catch (GraphRunnerException e) {
            throw new RuntimeException("Agent execution failed: " + e.getMessage(), e);
        }
    }

    /**
     * Agent 请求 DTO
     */
    public record AgentRequest(String message) {}

    /**
     * Agent 响应 DTO
     */
    public record AgentResponse(String reply) {}
}
