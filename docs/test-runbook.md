# 测试执行手册（Test Runbook）

> 适用仓库：`SYTRAVEL`  
> 更新时间：2026-04-02

## 1. 目标

- 统一本项目的测试执行方式
- 明确最小可回归测试集
- 记录受限网络场景（Maven 403）下的处理建议

## 2. 测试分层

### 2.1 单元测试（Service/Common）

建议优先执行：

- `PasswordSupportTest`
- `SYLoginServiceTest`
- `SYUserServiceTest`
- `PermissionGuardTest`
- `SYWebsocketServiceTest`
- `GlobalExceptionHandlerTest`

命令示例：

```bash
mvn -q -Dtest=PasswordSupportTest,SYLoginServiceTest,SYUserServiceTest,PermissionGuardTest,SYWebsocketServiceTest,GlobalExceptionHandlerTest test
```

统一脚本：

```bash
./scripts/test-gate.sh unit
```

### 2.2 控制器参数校验测试（MockMvc）

建议执行：

- `SYUserRestValidationTest`
- `SYLoginRestValidationTest`
- `SYProjectRestValidationTest`
- `SYTeamRestValidationTest`
- `SYProductRestValidationTest`
- `SYRoleRestValidationTest`
- `SYClassesRestValidationTest`
- `SYLoggerRestValidationTest`

命令示例：

```bash
mvn -q -Dtest=SYLoginRestValidationTest,SYUserRestValidationTest,SYProjectRestValidationTest,SYTeamRestValidationTest,SYProductRestValidationTest,SYRoleRestValidationTest,SYClassesRestValidationTest,SYLoggerRestValidationTest test
```

统一脚本：

```bash
./scripts/test-gate.sh validation
```

## 3. 全量回归

```bash
mvn test
```

统一脚本：

```bash
./scripts/test-gate.sh full
```

## 4. 常见问题

### 4.1 Maven 仓库 403（父 POM 无法下载）

现象：

- `spring-boot-starter-parent:1.5.9.RELEASE` 下载失败
- `spring-boot-starter-parent:2.7.18` 下载失败
- Maven 输出 `status code: 403`

建议：

1. 切换到可访问 Maven Central 的网络环境执行测试  
2. 配置公司私有 Maven 镜像后再执行  
3. 在 CI 中预热依赖缓存，避免临时网络波动影响

补充：`scripts/test-gate.sh` 在检测到 `status code: 403` 时会输出明确的环境提示，便于快速定位为仓库访问问题而非业务代码失败。

### 4.2 403 场景的可落地解决方案（建议优先做）

如果你希望“后续能真实跑起来验证”，请先完成以下一次性配置：

```bash
./scripts/create-maven-settings.sh <你的可访问仓库地址> .mvn/settings-mirror.xml
export MAVEN_SETTINGS_FILE=$PWD/.mvn/settings-mirror.xml
```

然后用同一套 settings 执行：

```bash
./scripts/upgrade-precheck.sh --phase jdk17 --strict --report build/upgrade-precheck-report.txt
./scripts/test-gate.sh smoke
```

说明：

- `MAVEN_SETTINGS_FILE` 已被 `upgrade-precheck.sh` 与 `test-gate.sh` 支持。
- `<你的可访问仓库地址>` 建议填写公司 Nexus/Artifactory 的 Maven 代理地址（最稳妥）。
- 若你已经有 `~/.m2/settings.xml`，也可直接 `export MAVEN_SETTINGS_FILE=~/.m2/settings.xml`。

## 5. CI 建议

- PR 最低门禁：
  - 升级前预检（6）
  - 单元测试（2.1）
  - 控制器校验测试（2.2）
- 主分支门禁：
  - `mvn test` 全量

仓库已提供参考实现：`.github/workflows/ci.yml`。

## 6. 升级前预检（平台升级第一跳准备）

在当前执行阶段（Boot 2.7 + JDK17）先跑一次预检脚本，快速确认运行时基线和 `javax.*` 迁移规模：

```bash
./scripts/upgrade-precheck.sh --phase jdk17
```

CI 严格模式（用于门禁失败）：

```bash
./scripts/upgrade-precheck.sh --phase jdk17 --strict
```

输出报告文件（便于 CI 归档）：

```bash
./scripts/upgrade-precheck.sh --phase jdk17 --strict --report build/upgrade-precheck-report.txt
```

该脚本会输出：

- 本地 Java / Maven 版本
- `pom.xml` 中 `java.version` 与 Spring Boot Parent 版本
- `javax.*` import 的出现次数（用于评估后续 Boot 3/Jakarta 改造工作量）
- `javax.*` import 热点文件 Top N（用于升级分工和优先级排序）
- `javax` 分类计数（servlet/validation/persistence/websocket/annotation/other），用于下一阶段迁移分批策略

在 CI 中，预检结果会同时：

- 作为 artifact `upgrade-precheck-report` 上传
- 同步写入 GitHub Actions Job Summary，便于在 PR 页面快速查看
- 基于报告自动生成 `upgrade-execution-checklist` artifact（用于升级分工执行）
  - 清单包含热点风险等级和 owner 信息（默认来自 `config/upgrade-owners.map`）
  - CI 使用 `--fail-on-unmapped`，若热点未命中 owner 映射将直接失败
  - 清单会基于 `javax` 分类计数自动给出迁移批次建议（Batch A/B/C/D）
  - CI 会校验清单格式关键段（Phase 0 / owner / unmapped owners）避免生成异常内容
