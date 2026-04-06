# 护你周全（hLoveYQ）

一个**非前后端分离**的姨妈监测网页应用：Spring Boot + Thymeleaf + Spring Security + JPA。

## 运行环境

- JDK 17（本仓库内可用：`D:\javaInstall\jdk17`）
- Maven（已可用）
- MySQL 8（生产/正式数据）
- Node.js（仅用于构建 Tailwind CSS，可选）

## 本地启动（推荐两种方式）

### 方式 A：先用 H2（local profile）快速跑通页面

这会使用内存数据库，不依赖 MySQL，适合先看 UI/流程。

```powershell
cd D:\javaInstall\cursor_workspace\hLoveYQ\hLoveYQ
$env:JAVA_HOME="D:\javaInstall\jdk17"
$env:Path="$env:JAVA_HOME\bin;$env:Path"
mvn -DskipTests "-Dspring-boot.run.profiles=local" spring-boot:run
```

启动后访问：
- `http://localhost:8080/register`
- `http://localhost:8080/login`
- `http://localhost:8080/`（登录后）

### 方式 B：使用 MySQL（默认配置）

默认读取环境变量（避免把密码写进仓库）：
- `DB_URL`（可选）
- `DB_USERNAME`（默认 `root`）
- `DB_PASSWORD`（默认空）

示例：

```powershell
cd D:\javaInstall\cursor_workspace\hLoveYQ\hLoveYQ
$env:JAVA_HOME="D:\javaInstall\jdk17"
$env:Path="$env:JAVA_HOME\bin;$env:Path"

$env:DB_URL="jdbc:mysql://localhost:3306/auntie_tracker?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true&createDatabaseIfNotExist=true"
$env:DB_USERNAME="root"
$env:DB_PASSWORD="你的MySQL密码"

mvn -DskipTests spring-boot:run
```

> 如果你不想用 root，建议在 MySQL 里创建专用用户并授权数据库 `auntie_tracker`。

## Tailwind CSS（可选）

项目已内置一份可用的 `app.css`，你也可以用 Tailwind 重新构建：

```powershell
cd D:\javaInstall\cursor_workspace\hLoveYQ\hLoveYQ
npm install
npm run build
```

## PWA

已包含：
- `src/main/resources/static/manifest.webmanifest`
- `src/main/resources/static/service-worker.js`

生产环境记得使用 HTTPS 才能正常安装。

## 主要功能入口

- 注册：`/register`
- 登录：`/login`
- 仪表盘：`/`
- 记录：`/records`、`/records/new`
- 洞察：`/insights`
- 导出：`/export.csv`

