package com.example.demo.agent;

import com.alibaba.cloud.ai.graph.agent.ReactAgent;
import com.example.demo.tools.CalculatorTools;
import com.example.demo.tools.StringUtilsTools;
import com.example.demo.tools.TimeTools;
import com.example.demo.tools.WeatherTools;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.ArrayList;
import java.util.List;

/**
 * React Agent 配置类
 * 负责配置和创建 Spring AI Alibaba 的 ReactAgent 实例
 * 使用 OpenAI 兼容的聊天模型来提供 AI 对话能力
 */
@Configuration
public class ReactAgentService {

    @Autowired
    private CalculatorTools calculatorTools;

    @Autowired
    private TimeTools timeTools;

    @Autowired
    private WeatherTools weatherTools;

    @Autowired
    private StringUtilsTools stringUtilsTools;

    /**
     * 创建工具回调列表
     *
     * @return 工具回调列表
     */
    @Bean
    public List<ToolCallback> agentToolCallbacks() {
        List<ToolCallback> callbacks = new ArrayList<>();
        ToolCallbackProvider provider = MethodToolCallbackProvider.builder()
                .toolObjects(calculatorTools, timeTools, weatherTools, stringUtilsTools)
                .build();
        ToolCallback[] callbackArray = provider.getToolCallbacks();
        for (ToolCallback callback : callbackArray) {
            callbacks.add(callback);
        }
        return callbacks;
    }

    /**
     * 创建并配置 ReactAgent Bean
     * ReactAgent 是一种基于 ReAct (Reasoning + Acting) 模式的智能 Agent
     * 能够理解用户意图、进行推理并给出回应
     *
     * @param chatModel 聊天模型实例，通过 @Qualifier 明确指定使用 openAiChatModel
     *                  避免与 DashScope 的 chatModel 冲突
     * @param toolCallbacks 工具回调列表
     * @return 配置好的 ReactAgent 实例，标记为 @Primary 作为主要的 Agent
     */
    @Bean
    @Primary
    public ReactAgent reactAgent(@Qualifier("openAiChatModel") ChatModel chatModel,
                                 List<ToolCallback> toolCallbacks) {
        return ReactAgent.builder()
                // Agent 的内部标识名称
                .name("react_agent")
                // 使用的聊天模型，负责实际的 AI 对话生成
                .model(chatModel)
                // 系统指令，定义 Agent 的角色和行为准则
                .instruction("你是一个有帮助的AI助手，能够回答用户的问题并提供有用的信息。" +
                        "你可以使用以下工具来帮助用户：计算器、时间查询、天气查询和字符串处理。" +
                        "当用户需要计算数学问题、查询时间或天气、或者处理字符串时，请使用相应的工具。")
                // Agent 的描述信息，用于在 UI 中展示
                .description("智能助手（带工具调用）")
                // 配置 Agent 使用的工具
                .tools(toolCallbacks)
                .build();
    }
}
