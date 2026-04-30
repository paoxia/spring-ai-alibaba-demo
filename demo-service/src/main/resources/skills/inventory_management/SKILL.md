# 库存管理技能

## 概述
本技能提供库存管理相关的数据库查询能力，帮助用户快速查询库存数据。

## 数据库表结构

### inventory 表（库存表）
| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | BIGINT | 主键 |
| product_id | BIGINT | 产品ID |
| product_name | VARCHAR(100) | 产品名称 |
| warehouse_id | BIGINT | 仓库ID |
| warehouse_name | VARCHAR(100) | 仓库名称 |
| quantity | INT | 库存数量 |
| unit | VARCHAR(20) | 单位 |
| update_time | DATETIME | 更新时间 |

### product 表（产品表）
| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | BIGINT | 主键 |
| name | VARCHAR(100) | 产品名称 |
| category | VARCHAR(50) | 产品类别 |
| price | DECIMAL(10,2) | 单价 |
| supplier | VARCHAR(100) | 供应商 |

## 常用查询示例

### 1. 查询某产品的总库存
```sql
SELECT product_name, SUM(quantity) as total_quantity
FROM inventory
GROUP BY product_id, product_name
WHERE product_name = '产品名称';
```

### 2. 查询某仓库的库存详情
```sql
SELECT i.*, p.category, p.price
FROM inventory i
LEFT JOIN product p ON i.product_id = p.id
WHERE i.warehouse_id = ?;
```

### 3. 查询库存不足的产品
```sql
SELECT product_name, SUM(quantity) as total
FROM inventory
GROUP BY product_id, product_name
HAVING SUM(quantity) < 10;
```

## 注意事项
- 库存数量单位默认为"件"
- 库存为0表示已售罄或缺货
- 建议设置库存预警值为20件
