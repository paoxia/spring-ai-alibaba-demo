package com.example.demo.service.agent;

import com.alibaba.cloud.ai.graph.agent.ReactAgent;
import com.alibaba.cloud.ai.graph.agent.hook.skills.SkillsAgentHook;
import com.alibaba.cloud.ai.graph.skills.registry.SkillRegistry;
import com.alibaba.cloud.ai.graph.skills.registry.classpath.ClasspathSkillRegistry;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configures the SQL assistant agent using the framework's built-in Skills support:
 * {@link ClasspathSkillRegistry} loads skills from classpath {@code skills/} (SKILL.md per skill),
 * {@link SkillsAgentHook} provides the {@code read_skill} tool and injects skill descriptions
 * into the system prompt via {@link com.alibaba.cloud.ai.graph.agent.interceptor.skills.SkillsInterceptor}.
 */
@Configuration
public class SkillsConfig {

    private static final String SYSTEM_PROMPT = """
            你是一个SQL查询助手，帮助用户编写业务数据库查询语句。
            当你需要特定领域的详细schema或业务逻辑时，请使用read_skill工具。
            """;

    @Bean
    public SkillRegistry skillRegistry() {
        return ClasspathSkillRegistry.builder()
                .classpathPath("skills")
                .build();
    }

    @Bean
    public SkillsAgentHook skillsAgentHook(SkillRegistry skillRegistry) {
        return SkillsAgentHook.builder()
                .skillRegistry(skillRegistry)
                .build();
    }

    @Bean
    public ReactAgent sqlAssistantAgent(ChatModel chatModel, SkillsAgentHook skillsAgentHook) {
        return ReactAgent.builder()
                .name("sql_assistant")
                .systemPrompt(SYSTEM_PROMPT)
                .model(chatModel)
                .hooks(List.of(skillsAgentHook))
                .build();
    }
}
