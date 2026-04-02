# SYTRAVEL

SYTRAVEL 是一个旅游业务管理系统后端项目，当前升级分支已切换到 **Spring Boot 3.3.5 + Java 17 + JPA**（当前阶段目标为 Boot 3 稳定化与全量回归封板）。  
项目已完成一轮“可维护性 + 安全性”重构：请求 DTO 化、统一返回结构、全局参数校验异常处理、密码 BCrypt 迁移与权限守卫等。

---

## 主要功能模块

- 系统主页模块
- 旅游项目模块
- 旅游团队模块
- 产品分类模块
- 旅游产品模块
- 角色管理模块
- 信息监控模块
- 日志管理模块
- 系统用户模块

---

## 近期重构内容（已落地）

- Controller 入参由动态 JSON 迁移为强类型 DTO（`@Valid` + JSR-303）
- 统一 API 返回模型：`AjaxResult.success/failed/validationError/internalError`
- 全局异常处理：`GlobalExceptionHandler`
- 密码安全升级：Base64 -> BCrypt，支持登录时旧密码自动升级
- 操作日志/返回构造统一：`ResultBuilder` + `OperationResultSupport`
- 权限守卫：`PermissionGuard`（管理员关键写操作）
- 补充单元测试与 MockMvc 参数校验测试

详细计划见：

- `docs/refactor-plan.md`
- `docs/platform-upgrade-assessment.md`
- `docs/test-runbook.md`
- `docs/boot3-batch-freeze.md`

---

## 本地运行

### 1) 环境要求

- JDK 17（当前升级分支基线）
- Maven 3.6+
- MySQL（按项目配置准备库与账号）

### 2) 启动

```bash
mvn spring-boot:run
```

或：

```bash
./mvnw spring-boot:run
```

---

## 测试

```bash
mvn test
```

> 说明：在部分受限网络环境下，可能出现 Maven 访问中央仓库 403（父 POM 无法下载），需要可访问 Maven 仓库的网络或私有镜像仓库。

可选：如果你有公司 Maven 镜像，可先生成 settings 并导出环境变量，再执行测试：

```bash
./scripts/create-maven-settings.sh <你的可访问仓库地址> .mvn/settings-mirror.xml
export MAVEN_SETTINGS_FILE=$PWD/.mvn/settings-mirror.xml
./scripts/test-gate.sh smoke
```

仓库已内置 `.mvn/settings-mirror.xml`（阿里云公共镜像）；若你的网络可访问该地址可直接使用，不可访问时再替换为公司私有镜像。

CI 已配置在 `.github/workflows/ci.yml`，默认执行单元测试、控制器参数校验测试，并在主分支执行全量回归。

推荐使用统一测试入口脚本：

```bash
./scripts/test-gate.sh smoke
```

---

## 下一步

- 平台升级实施：Boot 1.5 -> 2.7 -> 3.x（两跳迁移）
- 运行 `./scripts/boot3-migration-plan.sh` 生成 Jakarta 迁移批次与热点文件清单
- 运行 `./scripts/boot3-task-board.sh` 生成按 owner 分配的迁移任务板
- 持续补齐核心业务回归测试与鉴权覆盖
- 前端静态资源与接口层进一步治理
