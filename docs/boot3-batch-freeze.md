# Boot 3 Jakarta 迁移批次冻结（Task 1）

> 更新时间：2026-04-02  
> 目标：冻结 Boot 3 迁移批次范围，统一验收门槛，避免后续范围漂移。

## 1) 冻结范围

### Batch A（优先）
- 范围：`javax.validation.*`、`javax.annotation.*`
- 典型目录：`src/main/java/com/sy/travel/dto/*`、`src/main/java/com/sy/travel/rest/*`、`src/main/java/com/sy/travel/handler/*`
- 输出：参数校验链路 jakarta 化，控制器校验行为保持一致

### Batch B
- 范围：`javax.persistence.*`
- 典型目录：`src/main/java/com/sy/travel/entity/*`
- 输出：实体映射 jakarta 化，JPA 读写行为不变

### Batch C
- 范围：`javax.servlet.*`、`javax.websocket.*`
- 典型目录：`src/main/java/com/sy/travel/service/SYWebsocketService.java`、相关协议层类
- 输出：协议层 jakarta 化，WebSocket 生命周期行为不变

## 2) 冻结验收门槛

- Batch A 完成后：`smoke` 通过
- Batch B 完成后：`smoke + validation` 通过
- Batch C 完成后：`full` 通过

> 统一执行入口：`./scripts/test-gate.sh [smoke|validation|full]`

## 3) 执行依据（自动产物）

- `build/boot3-jakarta-migration-plan.md`（范围与热点）
- `build/boot3-jakarta-task-board.md`（owner 分配）

## 4) 变更控制规则

1. 本文档冻结后，新增/变更文件只能进入对应批次，不跨批插入。  
2. 若发现跨批强依赖，必须在 PR 说明中给出原因与回滚策略。  
3. 每个批次结束时更新一次“完成状态”与“剩余风险”。

## 5) 当前进度状态（滚动）

- Batch A：进行中（已完成 `dto/rest/handler` 的 `javax.validation.*` -> `jakarta.validation.*` import 切换）
- Batch B：未开始
- Batch C：未开始
