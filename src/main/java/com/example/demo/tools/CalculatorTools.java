package com.example.demo.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

/**
 * 计算器工具类
 * 提供基础的数学计算功能
 */
@Component
public class CalculatorTools {

    /**
     * 加法运算
     *
     * @param a 第一个加数
     * @param b 第二个加数
     * @return 两数之和
     */
    @Tool(name = "calculator_add", description = "执行加法运算，返回两个数的和")
    public double add(@ToolParam(description = "第一个加数") double a,
                       @ToolParam(description = "第二个加数") double b) {
        return a + b;
    }

    /**
     * 减法运算
     *
     * @param a 被减数
     * @param b 减数
     * @return 两数之差
     */
    @Tool(name = "calculator_subtract", description = "执行减法运算，返回两个数的差")
    public double subtract(@ToolParam(description = "被减数") double a,
                          @ToolParam(description = "减数") double b) {
        return a - b;
    }

    /**
     * 乘法运算
     *
     * @param a 第一个因数
     * @param b 第二个因数
     * @return 两数之积
     */
    @Tool(name = "calculator_multiply", description = "执行乘法运算，返回两个数的积")
    public double multiply(@ToolParam(description = "第一个因数") double a,
                          @ToolParam(description = "第二个因数") double b) {
        return a * b;
    }

    /**
     * 除法运算
     *
     * @param a 被除数
     * @param b 除数
     * @return 两数之商
     */
    @Tool(name = "calculator_divide", description = "执行除法运算，返回两个数的商")
    public double divide(@ToolParam(description = "被除数") double a,
                        @ToolParam(description = "除数，不能为0") double b) {
        if (b == 0) {
            throw new IllegalArgumentException("除数不能为0");
        }
        return a / b;
    }

    /**
     * 幂运算
     *
     * @param base 底数
     * @param exponent 指数
     * @return 幂运算结果
     */
    @Tool(name = "calculator_power", description = "执行幂运算，返回底数的指数次方")
    public double power(@ToolParam(description = "底数") double base,
                        @ToolParam(description = "指数") double exponent) {
        return Math.pow(base, exponent);
    }

    /**
     * 平方根运算
     *
     * @param number 被开方数
     * @return 平方根
     */
    @Tool(name = "calculator_sqrt", description = "执行平方根运算，返回一个数的平方根")
    public double sqrt(@ToolParam(description = "被开方数，必须大于等于0") double number) {
        if (number < 0) {
            throw new IllegalArgumentException("被开方数不能为负数");
        }
        return Math.sqrt(number);
    }
}
