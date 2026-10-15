# 工程记录

## 分支整合

原 `main` 只有说明文件，源码位于 `master`。本轮把完整源码与说明放到默认展示分支，保留两个分支的历史，不删除旧分支。

## 命名与配置迁移

- `sky-take-out` → `mealflow`。
- `sky-common` / `sky-pojo` / `sky-server` → `mealflow-common` / `mealflow-model` / `mealflow-server`。
- Java 包、实体别名、Mapper XML namespace、Swagger 扫描同步迁移。
- 自定义配置前缀从 `sky.*` 调整为 `mealflow.*`，数据库凭据和 JWT 密钥改用环境变量。
- 原 `/admin/employee/**` 路由与 `token` 请求头保持兼容。
- 删除重复的 Web Starter 声明，修复云端 Maven 严格依赖校验失败。

## 验证边界

现有业务主要是员工登录与 JWT 鉴权。模型类、支付工具、Redis 依赖和订单状态常量不作为完整业务的交付证据。

当前密码直接比较、退出不撤销 Token，属于后续身份体系完善任务。订单处理、支付和经营分析应在各自链路实现并完成验证后再列为已完成。
