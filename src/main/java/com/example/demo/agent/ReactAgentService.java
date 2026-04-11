package com.example.demo.agent;

import com.alibaba.cloud.ai.graph.agent.ReactAgent;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * React Agent 服务配置
 * 使用 Spring AI Alibaba ReactAgent 和 OpenAI 兼容 API
 */
@Configuration
public class ReactAgentService {

    /**
     * 创建 React Agent
     * 使用 @Qualifier 指定使用 openAiChatModel 而不是 dashScopeChatModel
     */
    @Bean
    @Primary
    public ReactAgent reactAgent(@Qualifier("openAiChatModel") ChatModel chatModel) {
        return ReactAgent.builder()
                .name("react_agent")
                .model(chatModel)
                .instruction("你是一个有帮助的AI助手，能够回答用户的问题并提供有用的信息。")
                .description("智能助手")
                .build();
    }
}
