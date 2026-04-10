package com.example.demo.config;

import com.example.demo.agent.SimpleChatAgent;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Chat Agent 配置类
 */
@Configuration
public class ChatAgentConfig {

    @Bean
    public SimpleChatAgent simpleChatAgent(ChatModel chatModel) {
        return new SimpleChatAgent(chatModel);
    }
}
