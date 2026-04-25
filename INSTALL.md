# 安装指南

## 前置要求

- Java 17 或更高版本
- OPENAI_MODE_API_KEY 环境变量（或在使用时通过 `-k` 参数指定）

## 快速开始（无需安装）

直接运行项目中的脚本：

### Linux/macOS
```bash
./bin/ai-chat --help
```

### Windows
```cmd
bin\ai-chat.bat --help
```

## 安装方式

### 方式一：使用分发包（推荐）

#### 1. 编译并打包
```bash
mvn clean package
```

编译完成后，在 `target/` 目录下会生成：
- `spring-ai-alibaba-demo-1.0.0-dist.zip`
- `spring-ai-alibaba-demo-1.0.0-dist.tar.gz`

#### 2. 解压分发包
```bash
# Linux/macOS
tar -xzf target/spring-ai-alibaba-demo-1.0.0-dist.tar.gz
cd spring-ai-alibaba-demo-1.0.0

# Windows (PowerShell)
Expand-Archive target/spring-ai-alibaba-demo-1.0.0-dist.zip
cd spring-ai-alibaba-demo-1.0.0
```

#### 3. 安装到系统

##### Linux/macOS
```bash
# 方式 A: 链接到 /usr/local/bin
sudo ln -s $(pwd)/bin/ai-chat /usr/local/bin/ai-chat

# 方式 B: 复制到 /usr/local/bin
sudo cp bin/ai-chat /usr/local/bin/
sudo cp spring-ai-alibaba-demo-1.0.0.jar /usr/local/bin/

# 方式 C: 添加到 PATH (在 ~/.bashrc 或 ~/.zshrc 中添加)
export PATH="$PATH:/path/to/spring-ai-alibaba-demo-1.0.0/bin"
```

##### Windows
```cmd
# 方式 A: 添加到 PATH 环境变量
# 1. 右键 "此电脑" -> "属性" -> "高级系统设置" -> "环境变量"
# 2. 在 "系统变量" 中找到 "Path"，点击"编辑"
# 3. 点击"新建"，添加: C:\path\to\spring-ai-alibaba-demo-1.0.0\bin
# 4. 点击"确定"保存

# 方式 B: 复制到系统目录（需要管理员权限）
copy bin\ai-chat.bat C:\Windows\System32\
copy spring-ai-alibaba-demo-1.0.0.jar C:\Windows\System32\
```

### 方式二：直接使用 JAR

#### 1. 编译
```bash
mvn clean package
```

#### 2. 运行
```bash
java -jar target/spring-ai-alibaba-demo-1.0.0.jar --help
```

#### 3. 创建别名（可选）
```bash
# Linux/macOS (添加到 ~/.bashrc 或 ~/.zshrc)
alias ai-chat='java -jar /path/to/spring-ai-alibaba-demo-1.0.0.jar'

# Windows (PowerShell, 添加到 $PROFILE)
function ai-chat { java -jar C:\path\to\spring-ai-alibaba-demo-1.0.0.jar $args }
```

## 配置 API Key

### 方式一：环境变量（推荐）
```bash
# Linux/macOS
export OPENAI_MODE_API_KEY="your-api-key-here"

# Windows (CMD)
set OPENAI_MODE_API_KEY=your-api-key-here

# Windows (PowerShell)
$env:OPENAI_MODE_API_KEY="your-api-key-here"
```

### 方式二：命令行参数
```bash
ai-chat -k "your-api-key-here" -m "你好"
```

## 验证安装

```bash
ai-chat --help
```

## 卸载

```bash
# Linux/macOS
sudo rm -f /usr/local/bin/ai-chat

# Windows
# 从 PATH 环境变量中移除，或删除 C:\Windows\System32\ai-chat.bat
```
