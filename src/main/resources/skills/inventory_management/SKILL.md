---
name: inventory_management
description: 库存管理，包括产品、仓库和库存水平的跟踪和查询。
---

# 库存管理 Schema

## 数据表

### products（产品表）
- product_id (主键)
- product_name（产品名称）
- sku（库存单位）
- category（产品类别）
- unit_cost（单位成本）
- reorder_point（再订货点，库存低于此值需要补货）
- discontinued（是否停产，布尔值）

### warehouses（仓库表）
- warehouse_id (主键)
- warehouse_name（仓库名称）
- location（位置）
- capacity（容量）

### inventory（库存表）
- inventory_id (主键)
- product_id (外键 -> products)
- warehouse_id (外键 -> warehouses)
- quantity_on_hand（现有数量）
- last_updated（最后更新时间）

### stock_movements（库存变动表）
- movement_id (主键)
- product_id (外键 -> products)
- warehouse_id (外键 -> warehouses)
- movement_type（变动类型：inbound/outbound/transfer/adjustment）
- quantity（数量，入库为正，出库为负）
- movement_date（变动日期）
- reference_number（参考编号）

## 业务逻辑

**可用库存**：inventory 表中 quantity_on_hand > 0 的记录

**需要补货的产品**：所有仓库的总库存 <= 产品的 reorder_point

**仅活跃产品**：除非分析停产产品，否则排除 discontinued = true 的产品

## 示例查询

```sql
-- 查询低于再订货点的产品
SELECT p.product_id, p.product_name, p.reorder_point, SUM(i.quantity_on_hand) as total_stock
FROM products p
JOIN inventory i ON p.product_id = i.product_id
WHERE p.discontinued = false
GROUP BY p.product_id, p.product_name, p.reorder_point
HAVING SUM(i.quantity_on_hand) <= p.reorder_point;
```
