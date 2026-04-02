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

命令示例：

```bash
mvn -q -Dtest=PasswordSupportTest,SYLoginServiceTest,SYUserServiceTest,PermissionGuardTest test
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
- Maven 输出 `status code: 403`

建议：

1. 切换到可访问 Maven Central 的网络环境执行测试  
2. 配置公司私有 Maven 镜像后再执行  
3. 在 CI 中预热依赖缓存，避免临时网络波动影响

补充：`scripts/test-gate.sh` 在检测到 `status code: 403` 时会输出明确的环境提示，便于快速定位为仓库访问问题而非业务代码失败。

## 5. CI 建议

- PR 最低门禁：
  - 升级前预检（6）
  - 单元测试（2.1）
  - 控制器校验测试（2.2）
- 主分支门禁：
  - `mvn test` 全量

仓库已提供参考实现：`.github/workflows/ci.yml`。

## 6. 升级前预检（平台升级第一跳准备）

在执行 `Boot 1.5 -> 2.7 + JDK17` 之前，先跑一次预检脚本，快速确认当前运行时基线和 `javax.*` 迁移规模：

```bash
./scripts/upgrade-precheck.sh
```

CI 严格模式（用于门禁失败）：

```bash
./scripts/upgrade-precheck.sh --strict
```

输出报告文件（便于 CI 归档）：

```bash
./scripts/upgrade-precheck.sh --strict --report build/upgrade-precheck-report.txt
```

该脚本会输出：

- 本地 Java / Maven 版本
- `pom.xml` 中 `java.version` 与 Spring Boot Parent 版本
- `javax.*` import 的出现次数（用于评估后续 Boot 3/Jakarta 改造工作量）
- `javax.*` import 热点文件 Top N（用于升级分工和优先级排序）

在 CI 中，预检结果会同时：

- 作为 artifact `upgrade-precheck-report` 上传
- 同步写入 GitHub Actions Job Summary，便于在 PR 页面快速查看
- 基于报告自动生成 `upgrade-execution-checklist` artifact（用于升级分工执行）
  - 清单包含热点风险等级和 owner 占位，便于升级分工落地
