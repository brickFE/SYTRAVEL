# 测试执行手册（Test Runbook）

> 适用仓库：`SYTRAVEL`  
> 更新时间：2026-04-01

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

命令示例：

```bash
mvn -q -Dtest=SYLoginRestValidationTest,SYUserRestValidationTest,SYProjectRestValidationTest,SYTeamRestValidationTest,SYProductRestValidationTest,SYRoleRestValidationTest,SYClassesRestValidationTest test
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

## 5. CI 建议

- PR 最低门禁：
  - 单元测试（2.1）
  - 控制器校验测试（2.2）
- 主分支门禁：
  - `mvn test` 全量

仓库已提供参考实现：`.github/workflows/ci.yml`。
