#myproj

## 1. MYSQL 中文解决方案
  1.1 windows解决方案一
        设置MySQL数据库默认编码my.ini文件中，default-character-set=utf8   (注意是utf8中间没有-)
         [mysql]
         default-character-set=utf8
         [mysqld]
         default-character-set=utf8

  1.2 Linux 解决方案
        创建数据库是用utf8： create database myproj  charset utf8;
        建表是用utf8.对于hibernat设置连接用utf8：   <beans:property name="url" value="jdbc:mysql://localhost:3306/mydata?useUnicode=true&amp;characterEncoding=UTF-8" />

## 2. 运行和调试
### 2.1. 调试
      mvnDebug spring-boot:run 或者
      java -Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=8000,suspend=n
       -jar target/*.jar

