package com.example.demo.cli;

import com.alibaba.cloud.ai.graph.agent.ReactAgent;
import com.alibaba.cloud.ai.graph.exception.GraphRunnerException;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.util.Scanner;
import java.util.concurrent.Callable;

/**
 * Spring AI Alibaba 智能助手命令行工具
 * 支持交互式模式和单次消息模式
 */
@Component
@Command(
    name = "ai-chat",
    mixinStandardHelpOptions = true,
    version = "1.0.0",
    description = "Spring AI Alibaba 智能助手命令行工具",
    usageHelpAutoWidth = true
)
public class ChatCommand implements Callable<Integer> {

    @Autowired
    private ReactAgent reactAgent;

    @Option(
        names = {"-m", "--message"},
        description = "单次消息模式：指定要发送的消息，发送后退出"
    )
    private String message;

    @Option(
        names = {"-k", "--api-key"},
        description = "指定 OpenAI API Key（覆盖配置文件）"
    )
    private String apiKey;

    @Option(
        names = {"--model"},
        description = "指定使用的模型名称（覆盖配置文件）"
    )
    private String model;

    @Option(
        names = {"-q", "--quiet"},
        description = "安静模式：只输出 AI 回复，不显示提示信息"
    )
    private boolean quiet;

    @Override
    public Integer call() {
        try {
            if (apiKey != null && !apiKey.isEmpty()) {
                System.setProperty("OPENAI_MODE_API_KEY", apiKey);
            }

            if (message != null && !message.isEmpty()) {
                return runSingleMessage();
            } else {
                return runInteractiveMode();
            }
        } catch (Exception e) {
            System.err.println("错误: " + e.getMessage());
            if (!quiet) {
                e.printStackTrace(System.err);
            }
            return 1;
        }
    }

    private int runSingleMessage() {
        try {
            if (!quiet) {
                System.out.println("AI 正在思考...");
            }
            AssistantMessage response = reactAgent.call(message);
            System.out.println(response.toString());
            return 0;
        } catch (GraphRunnerException e) {
            System.err.println("Agent 执行失败: " + e.getMessage());
            return 1;
        }
    }

    private int runInteractiveMode() {
        if (!quiet) {
            System.out.println("========================================");
            System.out.println("  Spring AI Alibaba 智能助手");
            System.out.println("  输入 'exit' 或 'quit' 退出");
            System.out.println("========================================");
            System.out.println();
        }

        Scanner scanner = new Scanner(System.in);
        while (true) {
            if (!quiet) {
                System.out.print("你: ");
            }
            String input = scanner.nextLine();

            if (input == null || input.trim().isEmpty()) {
                continue;
            }
            String trimmedInput = input.trim().toLowerCase();
            if ("exit".equals(trimmedInput) || "quit".equals(trimmedInput)) {
                if (!quiet) {
                    System.out.println("再见！");
                }
                break;
            }

            try {
                if (!quiet) {
                    System.out.print("AI: ");
                }
                AssistantMessage response = reactAgent.call(input);
                System.out.println(response.toString());
                if (!quiet) {
                    System.out.println();
                }
            } catch (GraphRunnerException e) {
                System.err.println("执行失败: " + e.getMessage());
                if (!quiet) {
                    System.out.println();
                }
            }
        }
        scanner.close();
        return 0;
    }
}
