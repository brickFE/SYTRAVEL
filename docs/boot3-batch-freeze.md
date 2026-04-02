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

- Batch A：已完成（`dto/rest/handler` 的 `javax.validation.*` -> `jakarta.validation.*` import 切换）
- Batch B：已完成（`entity` 的 `javax.persistence.*` -> `jakarta.persistence.*` import 切换）
- Batch C：已完成（`websocket` 的 `javax.websocket.*` -> `jakarta.websocket.*` import 切换）
- Task 5：阻塞中（Boot Parent 已切换到 3.x；本次执行 `./scripts/test-gate.sh full` 因 Maven 镜像网络不可达失败，需人工在可联网环境封板）
  - 备注：后续 **非手动** 流程可忽略该任务，待人工网络窗口再执行。

## 6) Task 1 执行指令（新增）

为避免“批次范围冻结”只停留在文档，仓库新增了自动检查脚本：

```bash
# 检查某个提交范围是否只改动当前批次允许目录
./scripts/boot3-freeze-guard.sh A origin/main HEAD
./scripts/boot3-freeze-guard.sh B origin/main HEAD
./scripts/boot3-freeze-guard.sh C origin/main HEAD
```

说明：
- A 仅允许改动 `dto/rest/handler`
- B 仅允许改动 `entity`
- C 仅允许改动 `SYWebsocketService.java`
- 通过后再执行对应验收门槛（`test-gate.sh`）

