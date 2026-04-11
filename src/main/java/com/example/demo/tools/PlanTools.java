package com.example.demo.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Plan & Exec 工具类
 * 提供复杂问题拆解和执行能力
 */
@Component
public class PlanTools {

    /**
     * 存储所有计划的内存结构
     * key: planId, value: 计划详情
     */
    private final Map<String, Plan> planStorage = new ConcurrentHashMap<>();

    /**
     * 计划 ID 生成器
     */
    private final AtomicInteger planIdGenerator = new AtomicInteger(1);

    /**
     * 计划数据结构
     */
    public static class Plan {
        public String planId;
        public String originalTask;
        public List<String> steps;
        public List<StepResult> stepResults;
        public boolean completed;

        public Plan(String planId, String originalTask, List<String> steps) {
            this.planId = planId;
            this.originalTask = originalTask;
            this.steps = steps;
            this.stepResults = new ArrayList<>();
            this.completed = false;
        }
    }

    /**
     * 步骤执行结果
     */
    public static class StepResult {
        public int stepIndex;
        public String stepDescription;
        public String result;
        public boolean success;

        public StepResult(int stepIndex, String stepDescription, String result, boolean success) {
            this.stepIndex = stepIndex;
            this.stepDescription = stepDescription;
            this.result = result;
            this.success = success;
        }
    }

    /**
     * 创建计划：将复杂任务拆解为多个步骤
     *
     * @param task 原始复杂任务描述
     * @param steps 拆解后的步骤列表（JSON 数组格式的字符串）
     * @return 计划创建结果，包含 planId
     */
    @Tool(name = "plan_create", description = "将复杂任务拆解为多个执行步骤，创建一个执行计划")
    public String createPlan(
            @ToolParam(description = "原始复杂任务的详细描述") String task,
            @ToolParam(description = "拆解后的步骤列表，JSON数组格式，例如：[\"步骤1描述\", \"步骤2描述\"]") String steps) {
        try {
            // 解析步骤列表
            List<String> stepList = parseSteps(steps);
            if (stepList.isEmpty()) {
                return "错误：步骤列表不能为空";
            }

            // 生成计划 ID
            String planId = "plan_" + planIdGenerator.getAndIncrement();

            // 存储计划
            Plan plan = new Plan(planId, task, stepList);
            planStorage.put(planId, plan);

            // 返回结果
            StringBuilder result = new StringBuilder();
            result.append("计划创建成功！\n");
            result.append("计划ID: ").append(planId).append("\n");
            result.append("原始任务: ").append(task).append("\n");
            result.append("执行步骤:\n");
            for (int i = 0; i < stepList.size(); i++) {
                result.append("  ").append(i + 1).append(". ").append(stepList.get(i)).append("\n");
            }
            result.append("\n请使用 plan_exec 工具逐个执行步骤，或使用 plan_exec_all 一次性执行所有步骤。");

            return result.toString();
        } catch (Exception e) {
            return "创建计划失败: " + e.getMessage();
        }
    }

    /**
     * 执行计划中的单个步骤
     *
     * @param planId 计划 ID
     * @param stepIndex 步骤索引（从 1 开始）
     * @param executionResult 步骤执行的结果描述
     * @return 执行结果
     */
    @Tool(name = "plan_exec", description = "执行计划中的单个步骤，并记录执行结果")
    public String executeStep(
            @ToolParam(description = "计划ID，由 plan_create 返回") String planId,
            @ToolParam(description = "要执行的步骤索引，从1开始") int stepIndex,
            @ToolParam(description = "该步骤的执行结果描述") String executionResult) {
        Plan plan = planStorage.get(planId);
        if (plan == null) {
            return "错误：找不到计划 ID: " + planId;
        }

        int actualIndex = stepIndex - 1;
        if (actualIndex < 0 || actualIndex >= plan.steps.size()) {
            return "错误：步骤索引 " + stepIndex + " 超出范围，总共有 " + plan.steps.size() + " 个步骤";
        }

        // 检查步骤是否已执行
        for (StepResult sr : plan.stepResults) {
            if (sr.stepIndex == stepIndex) {
                return "警告：步骤 " + stepIndex + " 已执行过，原结果: " + sr.result;
            }
        }

        // 记录执行结果
        String stepDesc = plan.steps.get(actualIndex);
        StepResult stepResult = new StepResult(stepIndex, stepDesc, executionResult, true);
        plan.stepResults.add(stepResult);

        // 检查是否所有步骤都已完成
        if (plan.stepResults.size() == plan.steps.size()) {
            plan.completed = true;
        }

        StringBuilder result = new StringBuilder();
        result.append("步骤 ").append(stepIndex).append(" 执行成功！\n");
        result.append("步骤描述: ").append(stepDesc).append("\n");
        result.append("执行结果: ").append(executionResult).append("\n");
        result.append("进度: ").append(plan.stepResults.size()).append("/").append(plan.steps.size());
        if (plan.completed) {
            result.append("\n🎉 恭喜！所有步骤已完成！");
        }

        return result.toString();
    }

    /**
     * 一次性执行计划中的所有步骤
     *
     * @param planId 计划 ID
     * @param resultsJson 所有步骤的执行结果，JSON 数组格式，每个元素包含 stepIndex 和 result
     * @return 执行结果汇总
     */
    @Tool(name = "plan_exec_all", description = "一次性执行计划中的所有步骤")
    public String executeAllSteps(
            @ToolParam(description = "计划ID，由 plan_create 返回") String planId,
            @ToolParam(description = "所有步骤的执行结果，JSON数组格式，例如：[{\"stepIndex\":1,\"result\":\"结果1\"},{\"stepIndex\":2,\"result\":\"结果2\"}]") String resultsJson) {
        Plan plan = planStorage.get(planId);
        if (plan == null) {
            return "错误：找不到计划 ID: " + planId;
        }

        try {
            List<Map<String, Object>> results = parseResults(resultsJson);
            StringBuilder summary = new StringBuilder();
            summary.append("批量执行计划: ").append(planId).append("\n");
            summary.append("原始任务: ").append(plan.originalTask).append("\n\n");

            int successCount = 0;
            for (Map<String, Object> item : results) {
                int stepIndex = ((Number) item.get("stepIndex")).intValue();
                String result = (String) item.get("result");

                int actualIndex = stepIndex - 1;
                if (actualIndex >= 0 && actualIndex < plan.steps.size()) {
                    String stepDesc = plan.steps.get(actualIndex);
                    StepResult stepResult = new StepResult(stepIndex, stepDesc, result, true);
                    plan.stepResults.add(stepResult);
                    successCount++;
                    summary.append("✓ 步骤 ").append(stepIndex).append(": ").append(stepDesc).append("\n");
                    summary.append("  结果: ").append(result).append("\n\n");
                }
            }

            plan.completed = (plan.stepResults.size() == plan.steps.size());
            summary.append("执行完成！成功: ").append(successCount).append("/").append(plan.steps.size());
            if (plan.completed) {
                summary.append("\n🎉 所有步骤已完成！");
            }

            return summary.toString();
        } catch (Exception e) {
            return "批量执行失败: " + e.getMessage();
        }
    }

    /**
     * 查询计划状态
     *
     * @param planId 计划 ID
     * @return 计划当前状态
     */
    @Tool(name = "plan_status", description = "查询计划的当前执行状态和进度")
    public String getPlanStatus(
            @ToolParam(description = "计划ID") String planId) {
        Plan plan = planStorage.get(planId);
        if (plan == null) {
            return "错误：找不到计划 ID: " + planId;
        }

        StringBuilder status = new StringBuilder();
        status.append("计划状态查询\n");
        status.append("============\n");
        status.append("计划ID: ").append(plan.planId).append("\n");
        status.append("原始任务: ").append(plan.originalTask).append("\n");
        status.append("状态: ").append(plan.completed ? "✅ 已完成" : "🔄 进行中").append("\n");
        status.append("进度: ").append(plan.stepResults.size()).append("/").append(plan.steps.size()).append("\n\n");

        status.append("步骤详情:\n");
        for (int i = 0; i < plan.steps.size(); i++) {
            int stepNum = i + 1;
            String stepDesc = plan.steps.get(i);
            StepResult stepResult = findStepResult(plan, stepNum);

            if (stepResult != null) {
                status.append("✓ [步骤 ").append(stepNum).append("] ").append(stepDesc).append("\n");
                status.append("  结果: ").append(stepResult.result).append("\n");
            } else {
                status.append("○ [步骤 ").append(stepNum).append("] ").append(stepDesc).append(" (待执行)\n");
            }
        }

        return status.toString();
    }

    /**
     * 列出所有计划
     *
     * @return 所有计划的概览
     */
    @Tool(name = "plan_list", description = "列出所有已创建的计划")
    public String listAllPlans() {
        if (planStorage.isEmpty()) {
            return "当前没有已创建的计划";
        }

        StringBuilder list = new StringBuilder();
        list.append("所有计划列表\n");
        list.append("============\n\n");

        for (Plan plan : planStorage.values()) {
            list.append("计划ID: ").append(plan.planId).append("\n");
            list.append("任务: ").append(plan.originalTask).append("\n");
            list.append("状态: ").append(plan.completed ? "✅ 已完成" : "🔄 进行中").append("\n");
            list.append("进度: ").append(plan.stepResults.size()).append("/").append(plan.steps.size()).append("\n");
            list.append("------------\n\n");
        }

        return list.toString();
    }

    /**
     * 解析步骤列表 JSON
     */
    private List<String> parseSteps(String stepsJson) {
        List<String> result = new ArrayList<>();
        // 简单的 JSON 数组解析，不依赖外部库
        String content = stepsJson.trim();
        if (content.startsWith("[") && content.endsWith("]")) {
            content = content.substring(1, content.length() - 1);
            String[] items = content.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
            for (String item : items) {
                String step = item.trim();
                if (step.startsWith("\"") && step.endsWith("\"")) {
                    step = step.substring(1, step.length() - 1);
                }
                if (!step.isEmpty()) {
                    result.add(step);
                }
            }
        }
        return result;
    }

    /**
     * 解析结果 JSON
     */
    private List<Map<String, Object>> parseResults(String resultsJson) {
        List<Map<String, Object>> result = new ArrayList<>();
        // 简单解析，实际项目中建议使用 Jackson/Gson
        // 这里处理格式: [{"stepIndex":1,"result":"xxx"},...]
        String content = resultsJson.trim();
        if (content.startsWith("[") && content.endsWith("]")) {
            content = content.substring(1, content.length() - 1);
            // 分割对象
            String[] objects = content.split("\\},\\s*\\{");
            for (String obj : objects) {
                obj = obj.replace("{", "").replace("}", "").trim();
                Map<String, Object> map = new ConcurrentHashMap<>();
                String[] pairs = obj.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                for (String pair : pairs) {
                    String[] kv = pair.split(":", 2);
                    if (kv.length == 2) {
                        String key = kv[0].trim().replace("\"", "");
                        String value = kv[1].trim();
                        if (value.startsWith("\"") && value.endsWith("\"")) {
                            value = value.substring(1, value.length() - 1);
                            map.put(key, value);
                        } else {
                            try {
                                map.put(key, Integer.parseInt(value));
                            } catch (NumberFormatException e) {
                                map.put(key, value);
                            }
                        }
                    }
                }
                if (!map.isEmpty()) {
                    result.add(map);
                }
            }
        }
        return result;
    }

    /**
     * 查找步骤结果
     */
    private StepResult findStepResult(Plan plan, int stepIndex) {
        for (StepResult sr : plan.stepResults) {
            if (sr.stepIndex == stepIndex) {
                return sr;
            }
        }
        return null;
    }
}
