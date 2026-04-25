---
name: sales_analytics
description: 销售数据分析，包括客户、订单和收入等业务数据的查询和分析。
---

# 销售分析 Schema

## 数据表

### customers（客户表）
- customer_id (主键)
- name（客户姓名）
- email（邮箱）
- signup_date（注册日期）
- status（状态：active/inactive）
- customer_tier（客户等级：bronze/silver/gold/platinum）

### orders（订单表）
- order_id (主键)
- customer_id (外键 -> customers)
- order_date（订单日期）
- status（订单状态：pending/completed/cancelled/refunded）
- total_amount（订单总金额）
- sales_region（销售区域：north/south/east/west）

### order_items（订单明细表）
- item_id (主键)
- order_id (外键 -> orders)
- product_id（产品ID）
- quantity（数量）
- unit_price（单价）
- discount_percent（折扣百分比）

## 业务逻辑

**活跃客户**：status = 'active' 且 signup_date <= 当前日期 - 90天

**收入计算**：仅统计 status = 'completed' 的订单，使用 orders 表的 total_amount

**高价值订单**：订单总金额 > 1000

## 示例查询

```sql
-- 按季度查询前10名客户收入
SELECT c.customer_id, c.name, c.customer_tier, SUM(o.total_amount) as total_revenue
FROM customers c
JOIN orders o ON c.customer_id = o.customer_id
WHERE o.status = 'completed' AND o.order_date >= CURRENT_DATE - INTERVAL '3 months'
GROUP BY c.customer_id, c.name, c.customer_tier
ORDER BY total_revenue DESC LIMIT 10;
```
