package com.example.demo.agent;

import com.alibaba.cloud.ai.agent.studio.loader.AgentLoader;
import com.alibaba.cloud.ai.graph.agent.Agent;
import com.alibaba.cloud.ai.graph.agent.ReactAgent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 自定义 Agent 加载器
 * 实现 AgentLoader 接口，用于向 Spring AI Alibaba Studio 提供可用的 Agent 列表
 * Studio UI 会通过此接口获取并展示可选择的 Agent
 */
@Component
public class CustomAgentLoader implements AgentLoader {

    /**
     * 研究助手 Agent 的唯一标识名称
     * Studio UI 中会显示此名称供用户选择
     */
    private static final String RESEARCH_AGENT = "research_agent";

    /**
     * SQL 助手 Agent 的唯一标识名称
     * 支持技能（Skills）功能，可以按需加载业务领域知识
     */
    private static final String SQL_ASSISTANT_AGENT = "sql_assistant";

    /**
     * 注入的 ReactAgent 实例
     * 这是实际处理用户请求的 AI Agent，通过 Spring 依赖注入获取
     */
    @Autowired
    private ReactAgent reactAgent;

    /**
     * 注入的 SQL 助手 Agent
     * 支持技能功能，能够根据用户查询按需加载相关领域知识
     */
    @Autowired
    @Qualifier("sqlAssistantAgent")
    private ReactAgent sqlAssistantAgent;

    /**
     * 列出所有可用的 Agent 名称
     * Studio UI 会调用此方法来展示可选择的 Agent 列表
     *
     * @return 包含所有可用 Agent 名称的 List 集合
     */
    @Override
    public List<String> listAgents() {
        return List.of(RESEARCH_AGENT, SQL_ASSISTANT_AGENT);
    }

    /**
     * 根据名称加载对应的 Agent 实例
     * 当用户在 Studio UI 中选择某个 Agent 并发送消息时，
     * Studio 会调用此方法获取对应的 Agent 实例来处理请求
     *
     * @param name 用户选择的 Agent 名称，应与 listAgents() 返回的名称一致
     * @return 对应的 Agent 实例
     * @throws IllegalArgumentException 如果请求的 Agent 名称不存在，则抛出此异常
     */
    @Override
    public Agent loadAgent(String name) {
        if (RESEARCH_AGENT.equals(name)) {
            return reactAgent;
        }
        if (SQL_ASSISTANT_AGENT.equals(name)) {
            return sqlAssistantAgent;
        }
        throw new IllegalArgumentException("Agent not found: " + name);
    }
}
