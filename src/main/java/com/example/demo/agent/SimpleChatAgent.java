package com.example.demo.agent;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Description;

/**
 * 简单的聊天代理
 * 使用 Spring AI Alibaba 构建一个基本的对话式 AI 代理
 */
public class SimpleChatAgent {

    private final ChatClient chatClient;

    public SimpleChatAgent(ChatModel chatModel) {
        this.chatClient = ChatClient.builder(chatModel)
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(new InMemoryChatMemory())
                )
                .build();
    }

    /**
     * 聊天方法
     * @param message 用户消息
     * @return AI 回复
     */
    public String chat(String message) {
        return chatClient.prompt()
                .user(message)
                .call()
                .content();
    }

    /**
     * 流式聊天方法
     * @param message 用户消息
     * @return AI 回复流
     */
    public String chatStream(String message) {
        return chatClient.prompt()
                .user(message)
                .stream()
                .content()
                .collectList()
                .block()
                .stream()
                .collect(java.util.stream.Collectors.joining());
    }
}
