package com.example.demo.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

/**
 * 时间工具类
 * 提供日期和时间相关的工具功能
 */
@Component
public class TimeTools {

    private static final DateTimeFormatter DEFAULT_DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter DEFAULT_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DEFAULT_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    /**
     * 获取当前日期时间
     *
     * @return 当前日期时间字符串
     */
    @Tool(name = "time_get_current_datetime", description = "获取当前的日期和时间")
    public String getCurrentDateTime() {
        return LocalDateTime.now().format(DEFAULT_DATETIME_FORMATTER);
    }

    /**
     * 获取当前日期
     *
     * @return 当前日期字符串
     */
    @Tool(name = "time_get_current_date", description = "获取当前的日期")
    public String getCurrentDate() {
        return LocalDateTime.now().format(DEFAULT_DATE_FORMATTER);
    }

    /**
     * 获取当前时间
     *
     * @return 当前时间字符串
     */
    @Tool(name = "time_get_current_time", description = "获取当前的时间")
    public String getCurrentTime() {
        return LocalDateTime.now().format(DEFAULT_TIME_FORMATTER);
    }

    /**
     * 获取当前星期几
     *
     * @return 星期几的中文名称
     */
    @Tool(name = "time_get_day_of_week", description = "获取今天是星期几")
    public String getDayOfWeek() {
        return LocalDateTime.now().getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.CHINA);
    }

    /**
     * 获取指定时区的当前时间
     *
     * @param zoneId 时区ID，例如 "Asia/Shanghai"、"America/New_York"、"Europe/London"
     * @return 指定时区的当前时间
     */
    @Tool(name = "time_get_datetime_by_timezone", description = "获取指定时区的当前日期和时间")
    public String getDateTimeByTimezone(@ToolParam(description = "时区ID，例如 Asia/Shanghai、America/New_York、Europe/London") String zoneId) {
        try {
            ZoneId zone = ZoneId.of(zoneId);
            return LocalDateTime.now(zone).format(DEFAULT_DATETIME_FORMATTER) + " (" + zoneId + ")";
        } catch (Exception e) {
            return "无效的时区ID: " + zoneId + "。请使用有效的时区ID，例如 Asia/Shanghai、America/New_York、Europe/London";
        }
    }

    /**
     * 计算两个日期之间的天数差
     *
     * @param date1 第一个日期，格式为 yyyy-MM-dd
     * @param date2 第二个日期，格式为 yyyy-MM-dd
     * @return 两个日期之间的天数差
     */
    @Tool(name = "time_calculate_days_between", description = "计算两个日期之间的天数差")
    public String calculateDaysBetween(@ToolParam(description = "第一个日期，格式为 yyyy-MM-dd") String date1,
                                      @ToolParam(description = "第二个日期，格式为 yyyy-MM-dd") String date2) {
        try {
            LocalDateTime d1 = LocalDateTime.parse(date1 + " 00:00:00", DEFAULT_DATETIME_FORMATTER);
            LocalDateTime d2 = LocalDateTime.parse(date2 + " 00:00:00", DEFAULT_DATETIME_FORMATTER);
            long days = Math.abs(java.time.temporal.ChronoUnit.DAYS.between(d1, d2));
            return date1 + " 和 " + date2 + " 之间相差 " + days + " 天";
        } catch (Exception e) {
            return "日期格式错误，请使用 yyyy-MM-dd 格式";
        }
    }
}
