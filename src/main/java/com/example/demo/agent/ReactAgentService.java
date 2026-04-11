package com.example.demo.agent;

import com.alibaba.cloud.ai.graph.agent.ReactAgent;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * React Agent 配置类
 * 负责配置和创建 Spring AI Alibaba 的 ReactAgent 实例
 * 使用 OpenAI 兼容的聊天模型来提供 AI 对话能力
 */
@Configuration
public class ReactAgentService {

    /**
     * 创建并配置 ReactAgent Bean
     * ReactAgent 是一种基于 ReAct (Reasoning + Acting) 模式的智能 Agent
     * 能够理解用户意图、进行推理并给出回应
     *
     * @param chatModel 聊天模型实例，通过 @Qualifier 明确指定使用 openAiChatModel
     *                  避免与 DashScope 的 chatModel 冲突
     * @return 配置好的 ReactAgent 实例，标记为 @Primary 作为主要的 Agent
     */
    @Bean
    @Primary
    public ReactAgent reactAgent(@Qualifier("openAiChatModel") ChatModel chatModel) {
        return ReactAgent.builder()
                // Agent 的内部标识名称
                .name("react_agent")
                // 使用的聊天模型，负责实际的 AI 对话生成
                .model(chatModel)
                // 系统指令，定义 Agent 的角色和行为准则
                .instruction("你是一个有帮助的AI助手，能够回答用户的问题并提供有用的信息。")
                // Agent 的描述信息，用于在 UI 中展示
                .description("智能助手")
                .build();
    }
}
