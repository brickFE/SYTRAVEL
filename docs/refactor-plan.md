# SYTRAVEL 重构路线图（分阶段）

> 目标：在不中断业务功能的前提下，把项目从“可运行”升级为“可维护、可测试、可演进”。

## 0. 现状快照

- 后端主线曾基于 Spring Boot 1.5.9 + Java 8，升级分支已开始迁移到 Spring Boot 2.7.18 + Java 11。  
- Controller/Service 层存在大量重复样板代码（分页、参数校验、日志结果组装）。  
- 密码处理为 Base64 编码（非加密），存在明显安全风险。  
- 前端为传统 jQuery + 多版本脚本混用，静态资源结构分散。  
- 自动化测试非常薄弱（当前仅见基础启动测试）。

## 1. 重构原则

1. **先建立安全网，再动核心逻辑**：优先补测试与观测能力。  
2. **小步快跑，保持可回滚**：每个阶段可独立上线。  
3. **功能不变优先**：先做结构重组，再做行为变更。  
4. **风险前置**：安全问题（密码、鉴权、输入校验）优先。

## 2. 阶段计划

## 阶段 A：基线与可观测（1~2 周）

- 建立“重构基线”：
  - 固定本地开发环境（JDK/Maven/MySQL 版本、初始化脚本）。
  - 增加健康检查与关键日志字段。
- 测试补齐（最少可用）：
  - 为核心 Service（用户、项目、团队、产品）补充单元测试。
  - 为 REST 接口增加 MockMvc 集成测试（覆盖增删改查主流程）。
- 输出：
  - 测试可执行文档。
  - 第一版覆盖率报告（可先设 20% 目标）。

## 阶段 B：后端分层与公共能力抽取（2~3 周）

- 引入 DTO + 校验：
  - 请求对象从 `JSON` 动态取值改为强类型 DTO。
  - 使用参数校验注解替代手写判空。
- 统一异常与返回：
  - 引入全局异常处理（`@ControllerAdvice`）。
  - 统一错误码、错误消息、日志追踪 ID。
- 抽取可复用模块：
  - 分页查询模板。
  - 操作日志记录模板（替代每个 Service 手动拼装结果）。
- 输出：
  - 至少一个模块（建议 user）完成“端到端新结构示范”。

## 阶段 C：安全整改（并行推进，1~2 周）

- 密码体系升级：
  - 由 Base64 改为 BCrypt/Argon2。
  - 登录验证改为哈希比对。
  - 增加“渐进迁移策略”（老密码首次登录后自动升级）。
- 基础安全：
  - 补充接口鉴权与角色控制。
  - 统一输入过滤，避免注入/越权。
- 输出：
  - 安全基线清单 + 验收用例。

## 阶段 D：数据层与领域模型治理（2 周）

- Repository 命名与查询规范统一。
- 领域对象瘦身：实体不承载表现层逻辑（如 `toMap`）。
- 引入 MapStruct/手写 Mapper（Entity ↔ DTO）。
- 关键表加索引与唯一约束复核。

## 阶段 E：前端治理（2~4 周）

- 静态资源整理：
  - 清理重复/多版本 jQuery 与历史插件。
  - 按模块归档 JS/CSS。
- 前端接口层统一：
  - 封装请求方法、统一错误提示。
  - 去除页面脚本中的重复逻辑。
- 视情况选择：
  - 轻量化继续维护 jQuery 架构；或逐步迁移到 Vue/React（按业务价值评估）。

## 阶段 F：平台升级（3~6 周，独立里程碑）

- Spring Boot 1.5.x 升级到 2.7.x（再评估到 3.x）。
- JDK 8 升级到 17 LTS。
- 依赖升级并修复兼容性问题。
- 完成后再启用更高版本框架特性（如新版安全配置）。

## 3. 每一轮迭代的执行模板

每个模块都按以下顺序推进：

1. 画出现状调用链（Rest → Service → Repository）。
2. 写回归测试，锁定当前行为。
3. 引入 DTO + 参数校验。
4. 抽离公共逻辑（日志、分页、异常处理）。
5. 保持接口兼容并灰度验证。
6. 合并后监控 1~2 天再进行下一模块。

## 4. 第一批优先重构清单（建议按这个顺序）

1. **用户模块（SYUserRest/SYUserService）**：风险最高（密码、权限、输入）。
2. **登录模块（SYLoginRest/SYLoginService）**：与安全改造联动。
3. **项目/团队/产品模块**：模板化改造，提取共性。
4. **日志模块**：统一审计模型与查询能力。

## 5. 验收标准（DoD）

- 功能回归：核心接口回归通过率 100%。
- 测试覆盖：核心业务包覆盖率达到阶段目标（20%→40%→60%）。
- 安全：不再存在明文/可逆密码存储。
- 可维护性：单个 Service 类平均行数明显下降，重复代码减少。
- 文档：模块设计说明与接口说明同步更新。

## 6. 我们下一步怎么做

建议从 **阶段 A + 用户模块** 开始，第一迭代只做三件事：

1. 为用户模块补单测与接口测试。  
2. 把 `JSON` 入参改为 `UserCreateRequest/UserUpdateRequest`。  
3. 落地全局异常处理和统一错误响应。  

完成后你确认效果，我们再进入第二迭代（密码与鉴权改造）。

## 7. 当前重构进度（滚动更新）

> 更新时间：2026-04-02

### 已完成

- [x] 用户模块 REST 入参 DTO 化（新增 `UserCreateRequest` / `UserUpdateRequest` / `UserAdminPwdUpdateRequest`）
- [x] 用户模块 `@Valid` 参数校验接入
- [x] 全局参数校验异常处理（`GlobalExceptionHandler`）
- [x] `AjaxResult` 工厂方法落地（`success` / `failed` / `validationError` / `internalError`）
- [x] 用户模块服务层移除动态 JSON 入参依赖（改为 DTO）
- [x] 多个 Service/Rest 返回构造统一迁移到 `AjaxResult` 工厂方法
- [x] 用户模块基础单元/接口测试补齐（`SYUserServiceTest` / `SYUserRestValidationTest`）
- [x] 参数校验一致性增强：删除接口 `operator` 非空约束覆盖用户/项目/团队/产品/角色/分类模块
- [x] 全局异常处理增强：`ConstraintViolationException` / `MissingServletRequestParameterException` / `MethodArgumentTypeMismatchException` 统一返回结构化 400
- [x] 500 错误信息脱敏：异常详情不再回传客户端，统一返回通用内部错误消息（避免泄露内部实现细节）

### 进行中

- [x] 其余模块（项目/团队/产品/分类/角色）的 DTO 化与参数校验改造
- [x] 项目模块已落地 DTO 化入口：`/sy/project/add` + `/sy/project/update`（`ProjectCreateRequest` / `ProjectUpdateRequest` + `@Valid`）
- [x] 团队模块已落地 DTO 化入口：`/sy/team/add` + `/sy/team/update`（`TeamCreateRequest` / `TeamUpdateRequest` + `@Valid`）
- [x] 产品模块已落地 DTO 化入口：`/sy/product/add` + `/sy/product/update`（`ProductCreateRequest` / `ProductUpdateRequest` + `@Valid`）
- [x] 分类模块已落地 DTO 化入口：`/sy/classes/add` + `/sy/classes/update`（`ClassesCreateRequest` / `ClassesUpdateRequest` + `@Valid`）
- [x] 角色模块已落地 DTO 化入口：`/sy/role/add` + `/sy/role/update`（`RoleCreateRequest` / `RoleUpdateRequest` + `@Valid`）
- [x] Service 层重复模板（日志记录+结果返回）抽取公共组件

### 待开始

- [x] 密码存储从 Base64 迁移到 BCrypt（登录成功自动升级旧密码）
- [x] 鉴权与角色控制体系梳理（已收敛为用户/项目/团队/产品/角色/分类关键写操作管理员权限校验）
- [x] 平台升级评估完成（见 `docs/platform-upgrade-assessment.md`，建议两跳迁移：1.5->2.7->3.x）
- [x] Spring Boot 2.7 迁移分支起步（parent 升级到 2.7.18，Java 基线升级到 11）
- [ ] 升级执行前置清单落地（依赖扫描、JDK17 兼容性预检）
