# 销售分析技能

## 概述
本技能提供销售数据分析相关的数据库查询能力，帮助用户进行销售数据统计和分析。

## 数据库表结构

### orders 表（订单表）
| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | BIGINT | 主键 |
| order_no | VARCHAR(50) | 订单号 |
| customer_id | BIGINT | 客户ID |
| customer_name | VARCHAR(100) | 客户名称 |
| order_date | DATE | 订单日期 |
| total_amount | DECIMAL(12,2) | 订单总额 |
| status | VARCHAR(20) | 订单状态 |
| payment_method | VARCHAR(30) | 支付方式 |

### order_items 表（订单明细表）
| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | BIGINT | 主键 |
| order_id | BIGINT | 订单ID |
| product_id | BIGINT | 产品ID |
| product_name | VARCHAR(100) | 产品名称 |
| quantity | INT | 数量 |
| unit_price | DECIMAL(10,2) | 单价 |
| subtotal | DECIMAL(12,2) | 小计金额 |

### customers 表（客户表）
| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | BIGINT | 主键 |
| name | VARCHAR(100) | 客户名称 |
| phone | VARCHAR(20) | 联系电话 |
| email | VARCHAR(100) | 邮箱 |
| level | VARCHAR(20) | 客户等级 |
| registered_date | DATE | 注册日期 |

## 常用查询示例

### 1. 统计月度销售额
```sql
SELECT DATE_FORMAT(order_date, '%Y-%m') as month,
       COUNT(*) as order_count,
       SUM(total_amount) as total_sales
FROM orders
GROUP BY DATE_FORMAT(order_date, '%Y-%m')
ORDER BY month DESC;
```

### 2. 查询TOP10热销产品
```sql
SELECT oi.product_name,
       SUM(oi.quantity) as total_sold,
       SUM(oi.subtotal) as total_revenue
FROM order_items oi
GROUP BY oi.product_id, oi.product_name
ORDER BY total_sold DESC
LIMIT 10;
```

### 3. 客户消费分析
```sql
SELECT c.name, c.level,
       COUNT(o.id) as order_count,
       SUM(o.total_amount) as total_consumption
FROM customers c
LEFT JOIN orders o ON c.id = o.customer_id
GROUP BY c.id, c.name, c.level
ORDER BY total_consumption DESC;
```

## 订单状态说明
- `PENDING`: 待支付
- `PAID`: 已支付
- `SHIPPED`: 已发货
- `COMPLETED`: 已完成
- `CANCELLED`: 已取消

## 注意事项
- 订单金额已包含运费
- 时间范围查询使用 order_date 字段
- 退款订单状态为 CANCELLED，金额需扣除
