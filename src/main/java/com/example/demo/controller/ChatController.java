package com.example.demo.controller;

import com.example.demo.agent.SimpleChatAgent;
import org.springframework.web.bind.annotation.*;

/**
 * 聊天控制器
 * 提供 REST API 接口与 Chat Agent 进行交互
 */
@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*")
public class ChatController {

    private final SimpleChatAgent chatAgent;

    public ChatController(SimpleChatAgent chatAgent) {
        this.chatAgent = chatAgent;
    }

    /**
     * 简单聊天接口
     * @param request 聊天请求
     * @return AI 回复
     */
    @PostMapping
    public ChatResponse chat(@RequestBody ChatRequest request) {
        String response = chatAgent.chat(request.message());
        return new ChatResponse(response);
    }

    /**
     * 聊天请求 DTO
     */
    public record ChatRequest(String message) {}

    /**
     * 聊天响应 DTO
     */
    public record ChatResponse(String reply) {}
}
