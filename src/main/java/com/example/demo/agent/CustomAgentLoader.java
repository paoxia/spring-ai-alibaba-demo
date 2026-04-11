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

    @Autowired
    private ReactAgent reactAgent;

    @Override
    public List<String> listAgents() {
        return List.of("react_agent", "research_agent");
    }

    @Override
    public Agent loadAgent(String name) {
        if ("react_agent".equals(name) || "research_agent".equals(name)) {
            return reactAgent;
        }
        throw new IllegalArgumentException("Agent not found: " + name);
    }
}
