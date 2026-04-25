package com.example.demo;

import com.example.demo.cli.ChatCommand;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import picocli.CommandLine;
import picocli.CommandLine.IFactory;

/**
 * Spring AI Alibaba 命令行工具
 * 纯命令行模式，使用 Picocli 提供交互式聊天
 */
@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(DemoApplication.class);
        app.setWebApplicationType(WebApplicationType.NONE);
        ConfigurableApplicationContext context = app.run(args);
        try {
            IFactory factory = context.getBean(IFactory.class);
            ChatCommand command = context.getBean(ChatCommand.class);
            CommandLine cmd = new CommandLine(command, factory);
            int exitCode = cmd.execute(args);
            System.exit(exitCode);
        } finally {
            context.close();
        }
    }
}
