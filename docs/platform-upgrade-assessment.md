# 平台升级评估（Spring Boot / JDK）

> 更新时间：2026-04-02  
> 目标：完成“平台升级”阶段的技术评估，给出可执行迁移路径与风险清单。

## 1. 当前基线

- 主线：Spring Boot `1.5.9.RELEASE` + JDK `8`
- 升级分支（进行中）：Spring Boot `3.3.5` + JDK `17`
- Spring Data JPA + 传统 Servlet API + WebSocket
- 前后端耦合较深，接口历史兼容要求较高

## 2. 升级目标

- 当前目标：Spring Boot `3.3.x` + JDK `17` 稳定化与回归封板
- 下一目标：持续清理兼容性边角并准备后续版本升级

## 3. 推荐路线（两跳迁移）

1. **第一跳（推荐先做）**  
   - JDK 8 -> JDK 17  
   - Spring Boot 1.5.x -> 2.7.x  
   - 处理 Spring Security/Spring MVC/Spring Data 的配置与 API 兼容问题
2. **第二跳（稳定后）**  
   - Spring Boot 2.7.x -> 3.x  
   - `javax.*` -> `jakarta.*` 全量改造  
   - 校验并回归 WebSocket、文件上传、JPA 相关能力

## 4. 主要风险点

- `javax` 到 `jakarta` 命名空间迁移导致编译错误（Boot 3 阶段）
- 旧版第三方依赖与 JDK17 兼容性问题
- 历史接口返回结构/异常处理兼容风险
- 前端脚本对错误码和字段名的隐式依赖

## 5. 验收建议

- 先锁定接口回归集（登录、用户、项目、团队、产品、角色、分类）
- 升级分支强制执行单测 + MockMvc 校验
- 每一跳都做灰度发布与回滚预案

## 6. 结论

- “平台升级”阶段**评估任务已完成**，建议按“两跳迁移”执行。  
- 下一里程碑建议从 **Boot 1.5 -> 2.7 + JDK17** 开始实施。

## 7. 当前就绪度（2026-04-02）

### 已完成的前置清障

- 接口入参 DTO 化和主要校验链路已落地（降低升级过程中的输入不确定性）
- 全局异常响应已经统一到结构化 `AjaxResult`（400/500 行为更稳定，便于升级后回归对比）
- 错误信息回传已做脱敏处理（避免在升级调试阶段暴露内部异常细节）

### 下一步（升级执行前）

1. 在当前升级分支上完成 Boot 3 + JDK17 的编译、测试与配置收敛  
2. 以登录/用户/项目/团队/产品/角色/分类回归集作为强制验收门槛  
3. 完成 full 回归并产出封板报告（风险、回滚方案、上线窗口）

## 8. Boot 3 迁移执行化（新增）

- 已新增脚本：`scripts/boot3-migration-plan.sh`
- 用途：基于仓库中 `javax.*` import 自动生成 Boot 3 / Jakarta 迁移计划与热点文件清单
- 已新增脚本：`scripts/boot3-task-board.sh`
- 用途：按迁移批次把 `javax.*` 文件自动映射到 owner（来自 `config/upgrade-owners.map`），生成可执行任务板
- 本地执行示例：

```bash
./scripts/boot3-migration-plan.sh build/boot3-jakarta-migration-plan.md
./scripts/boot3-task-board.sh build/boot3-jakarta-task-board.md config/upgrade-owners.map
```

- CI 中已上传 artifact：`boot3-jakarta-migration-plan`、`boot3-jakarta-task-board`（便于按批次推进下一阶段改造）

## 9. 迁移批次冻结状态（Task 1）

- 已完成批次范围冻结：见 `docs/boot3-batch-freeze.md`
- Batch A/B/C 的范围与验收门槛已固化，后续迭代按冻结规则推进
