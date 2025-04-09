# OpenCV 配置指南

## 问题描述

`ImageProcessor.java` 文件使用了OpenCV库进行图像处理，但在运行时可能会出现以下错误：

```
无法加载OpenCV库: no opencv_java in java.library.path
```

这是因为项目中缺少OpenCV的本地库文件或路径配置不正确。

## 解决方案

### 1. 添加Maven依赖

在`pom.xml`文件中添加OpenCV依赖：

```xml
<dependency>
    <groupId>org.openpnp</groupId>
    <artifactId>opencv</artifactId>
    <version>4.5.1-2</version>
</dependency>
```

### 2. 手动下载并配置OpenCV

如果Maven依赖无法解决问题，请按照以下步骤手动配置：

1. 从[OpenCV官网](https://opencv.org/releases/)下载适合您操作系统的OpenCV版本
2. 解压下载的文件
3. 将OpenCV的本地库文件添加到Java库路径中：

#### Windows系统：

- 将OpenCV的bin目录添加到系统环境变量PATH中
- 或者在运行Java程序时添加JVM参数：`-Djava.library.path=D:/path/to/opencv/bin`

#### Linux/Mac系统：

- 将OpenCV的lib目录添加到LD_LIBRARY_PATH环境变量中
- 或者在运行Java程序时添加JVM参数：`-Djava.library.path=/path/to/opencv/lib`

### 3. 使用IDE配置

如果您使用IntelliJ IDEA或Eclipse：

1. 在运行配置中添加VM选项：`-Djava.library.path=D:/path/to/opencv/bin`
2. 确保添加了OpenCV的JAR文件到项目依赖中

## 其他可能的问题

1. 确保输入图像文件存在且格式正确
2. 检查输出目录是否有写入权限
3. 如果图像分割效果不理想，可以调整代码中的参数：
   - 高斯模糊的核大小
   - Canny边缘检测的阈值
   - 轮廓过滤的面积和尺寸阈值

## 测试

修改后的`ImageProcessor.java`文件已经增加了更多的错误处理和日志输出，这将帮助您诊断问题。运行程序后，查看控制台输出以获取详细信息。