package com.example.demo.agent;

import com.alibaba.cloud.ai.agent.studio.loader.AgentLoader;
import com.alibaba.cloud.ai.graph.agent.Agent;
import com.alibaba.cloud.ai.graph.agent.ReactAgent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 自定义 Agent Loader
 * 用于提供 ReactAgent 给 Studio 使用
 */
@Component
public class CustomAgentLoader implements AgentLoader {

    private static final String RESEARCH_AGENT = "research_agent";

    @Autowired
    private ReactAgent reactAgent;

    @Override
    public List<String> listAgents() {
        return List.of(RESEARCH_AGENT);
    }

    @Override
    public Agent loadAgent(String name) {
        if (RESEARCH_AGENT.equals(name)) {
            return reactAgent;
        }
        throw new IllegalArgumentException("Agent not found: " + name);
    }
}
