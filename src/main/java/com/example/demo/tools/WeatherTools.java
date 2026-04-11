package com.example.demo.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * 天气查询工具类
 * 提供模拟的天气查询功能
 */
@Component
public class WeatherTools {

    private static final String[] WEATHER_CONDITIONS = {"晴", "多云", "阴", "小雨", "中雨", "大雨", "雷阵雨", "小雪", "中雪", "大雪"};
    private static final String[] WIND_DIRECTIONS = {"东风", "南风", "西风", "北风", "东北风", "东南风", "西北风", "西南风"};

    private final Random random = new Random();

    // 模拟城市基础温度数据
    private static final Map<String, Integer> CITY_BASE_TEMPERATURE = new HashMap<>();
    static {
        CITY_BASE_TEMPERATURE.put("北京", 15);
        CITY_BASE_TEMPERATURE.put("上海", 20);
        CITY_BASE_TEMPERATURE.put("广州", 25);
        CITY_BASE_TEMPERATURE.put("深圳", 26);
        CITY_BASE_TEMPERATURE.put("杭州", 18);
        CITY_BASE_TEMPERATURE.put("成都", 17);
        CITY_BASE_TEMPERATURE.put("武汉", 19);
        CITY_BASE_TEMPERATURE.put("西安", 14);
        CITY_BASE_TEMPERATURE.put("南京", 17);
        CITY_BASE_TEMPERATURE.put("重庆", 18);
    }

    /**
     * 查询指定城市的当前天气
     *
     * @param city 城市名称
     * @return 天气信息
     */
    @Tool(name = "weather_get_current", description = "查询指定城市的当前天气")
    public String getCurrentWeather(@ToolParam(description = "城市名称，例如 北京、上海、广州") String city) {
        if (city == null || city.trim().isEmpty()) {
            return "请提供有效的城市名称";
        }

        int baseTemp = CITY_BASE_TEMPERATURE.getOrDefault(city, 20);
        int tempVariation = random.nextInt(11) - 5; // -5 到 +5 度的变化
        int currentTemp = baseTemp + tempVariation;
        int highTemp = currentTemp + random.nextInt(5) + 3;
        int lowTemp = currentTemp - random.nextInt(5) - 3;

        String condition = WEATHER_CONDITIONS[random.nextInt(WEATHER_CONDITIONS.length)];
        String windDirection = WIND_DIRECTIONS[random.nextInt(WIND_DIRECTIONS.length)];
        int windLevel = random.nextInt(4) + 1;
        int humidity = random.nextInt(40) + 40;

        StringBuilder sb = new StringBuilder();
        sb.append("【").append(city).append(" 天气预报】\n");
        sb.append("天气状况：").append(condition).append("\n");
        sb.append("当前温度：").append(currentTemp).append("℃\n");
        sb.append("最高温度：").append(highTemp).append("℃\n");
        sb.append("最低温度：").append(lowTemp).append("℃\n");
        sb.append("风向风力：").append(windDirection).append(" ").append(windLevel).append("级\n");
        sb.append("相对湿度：").append(humidity).append("%\n");
        sb.append("温馨提示：").append(getWeatherTip(condition, currentTemp));

        return sb.toString();
    }

    /**
     * 查询指定城市未来几天的天气预报
     *
     * @param city 城市名称
     * @param days 预报天数，最多7天
     * @return 天气预报信息
     */
    @Tool(name = "weather_get_forecast", description = "查询指定城市未来几天的天气预报")
    public String getWeatherForecast(@ToolParam(description = "城市名称，例如 北京、上海、广州") String city,
                                      @ToolParam(description = "预报天数，最多7天") int days) {
        if (city == null || city.trim().isEmpty()) {
            return "请提供有效的城市名称";
        }

        if (days <= 0 || days > 7) {
            days = 3; // 默认3天
        }

        int baseTemp = CITY_BASE_TEMPERATURE.getOrDefault(city, 20);

        StringBuilder sb = new StringBuilder();
        sb.append("【").append(city).append(" 未来").append(days).append("天天气预报】\n");

        for (int i = 1; i <= days; i++) {
            int tempVariation = random.nextInt(11) - 5;
            int currentTemp = baseTemp + tempVariation;
            int highTemp = currentTemp + random.nextInt(5) + 3;
            int lowTemp = currentTemp - random.nextInt(5) - 3;
            String condition = WEATHER_CONDITIONS[random.nextInt(WEATHER_CONDITIONS.length)];

            sb.append("\n第").append(i).append("天：\n");
            sb.append("  天气：").append(condition).append("\n");
            sb.append("  温度：").append(lowTemp).append("℃ ~ ").append(highTemp).append("℃\n");
        }

        return sb.toString();
    }

    /**
     * 根据天气状况和温度提供温馨提示
     */
    private String getWeatherTip(String condition, int temperature) {
        if (condition.contains("雨") || condition.contains("雪")) {
            return "今天有" + condition + "，请记得带伞！";
        } else if (temperature >= 30) {
            return "天气炎热，注意防暑降温，多喝水！";
        } else if (temperature <= 5) {
            return "天气寒冷，注意保暖！";
        } else if (temperature >= 15 && temperature <= 25) {
            return "天气宜人，适合外出活动！";
        } else {
            return "祝您有美好的一天！";
        }
    }
}
