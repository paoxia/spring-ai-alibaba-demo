package com.example.demo.common.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 字符串工具类
 * 提供常用的字符串处理功能
 */
@Component
public class StringUtilsTools {

    /**
     * 获取字符串长度
     *
     * @param text 输入文本
     * @return 字符串长度
     */
    @Tool(name = "string_length", description = "获取字符串的长度")
    public int length(@ToolParam(description = "输入文本") String text) {
        if (text == null) {
            return 0;
        }
        return text.length();
    }

    /**
     * 将字符串转换为大写
     *
     * @param text 输入文本
     * @return 大写字符串
     */
    @Tool(name = "string_to_uppercase", description = "将字符串转换为大写")
    public String toUpperCase(@ToolParam(description = "输入文本") String text) {
        if (text == null) {
            return "";
        }
        return text.toUpperCase();
    }

    /**
     * 将字符串转换为小写
     *
     * @param text 输入文本
     * @return 小写字符串
     */
    @Tool(name = "string_to_lowercase", description = "将字符串转换为小写")
    public String toLowerCase(@ToolParam(description = "输入文本") String text) {
        if (text == null) {
            return "";
        }
        return text.toLowerCase();
    }

    /**
     * 反转字符串
     *
     * @param text 输入文本
     * @return 反转后的字符串
     */
    @Tool(name = "string_reverse", description = "反转字符串")
    public String reverse(@ToolParam(description = "输入文本") String text) {
        if (text == null) {
            return "";
        }
        return new StringBuilder(text).reverse().toString();
    }

    /**
     * 统计字符串中某个字符出现的次数
     *
     * @param text 输入文本
     * @param character 要统计的字符
     * @return 字符出现的次数
     */
    @Tool(name = "string_count_character", description = "统计字符串中某个字符出现的次数")
    public int countCharacter(@ToolParam(description = "输入文本") String text,
                              @ToolParam(description = "要统计的字符") String character) {
        if (text == null || character == null || character.length() != 1) {
            return 0;
        }
        char target = character.charAt(0);
        int count = 0;
        for (char c : text.toCharArray()) {
            if (c == target) {
                count++;
            }
        }
        return count;
    }

    /**
     * 统计字符串中某个词出现的次数
     *
     * @param text 输入文本
     * @param word 要统计的词
     * @return 词出现的次数
     */
    @Tool(name = "string_count_word", description = "统计字符串中某个词出现的次数")
    public int countWord(@ToolParam(description = "输入文本") String text,
                         @ToolParam(description = "要统计的词") String word) {
        if (text == null || word == null || word.isEmpty()) {
            return 0;
        }
        int count = 0;
        int index = 0;
        while ((index = text.indexOf(word, index)) != -1) {
            count++;
            index += word.length();
        }
        return count;
    }

    /**
     * 替换字符串中的文本
     *
     * @param text 输入文本
     * @param oldStr 要替换的旧文本
     * @param newStr 替换后的新文本
     * @return 替换后的字符串
     */
    @Tool(name = "string_replace", description = "替换字符串中的文本")
    public String replace(@ToolParam(description = "输入文本") String text,
                          @ToolParam(description = "要替换的旧文本") String oldStr,
                          @ToolParam(description = "替换后的新文本") String newStr) {
        if (text == null) {
            return "";
        }
        return text.replace(oldStr, newStr);
    }

    /**
     * 检查字符串是否包含某个子串
     *
     * @param text 输入文本
     * @param substring 要检查的子串
     * @return 是否包含
     */
    @Tool(name = "string_contains", description = "检查字符串是否包含某个子串")
    public boolean contains(@ToolParam(description = "输入文本") String text,
                            @ToolParam(description = "要检查的子串") String substring) {
        if (text == null || substring == null) {
            return false;
        }
        return text.contains(substring);
    }

    /**
     * 分割字符串
     *
     * @param text 输入文本
     * @param delimiter 分隔符
     * @return 分割后的字符串列表
     */
    @Tool(name = "string_split", description = "使用指定分隔符分割字符串")
    public List<String> split(@ToolParam(description = "输入文本") String text,
                              @ToolParam(description = "分隔符") String delimiter) {
        if (text == null || delimiter == null) {
            return List.of();
        }
        return Arrays.asList(text.split(delimiter));
    }

    /**
     * 去除字符串首尾空白
     *
     * @param text 输入文本
     * @return 去除空白后的字符串
     */
    @Tool(name = "string_trim", description = "去除字符串首尾的空白字符")
    public String trim(@ToolParam(description = "输入文本") String text) {
        if (text == null) {
            return "";
        }
        return text.trim();
    }
}
