# Simple-LMS

这是一个基于**Java swing**和 **mysql**的简易图书管理系统，利用**socket编程**，实现了C/S信息交互。

#### 优点:

 	1. 基于C/S模式，服务器和客户端分离，便于修改和维护，GUI界面尚可
 	2. 功能较为齐全，包括了图书信息添加、修改、删除、查询，还包括了用户信息的注册，修改，还包括了读书借阅功能
 	3. 设计了权限控制，可使同一个界面显示出不同用户可用的不同功能
 	4. 代码注释较齐全，能够快速进行更改和维护
 	5. 不同包之间分工明确,有较好的可读性

#### 缺点:

 	1. 客户端和服务器端通信时依靠readline()和ObjectStream实现，运行效率较低;安全性较低，容易被攻击，每次传输信息后都会关闭连接,资源开销大
 	2.  Server端使用了多线程(未使用线程池)实现，但Client端每一个按钮都未使用多线程，导致通信时，假设服务器掉线或其他情况，Client端会卡死（未使用多线程是因为GUI界面使用了JTable，大量删除和增加的情况下可能导致程序崩溃）
 	3. 部分界面如查询界面设计不够简洁,且未添加按图书类别查询和按出版日期查询等操作
 	4. images文件夹下的图片清晰度不够
 	5. borrow表设计不够简洁，导致查询的sql语句过长
 	6. 使用时服务器端IP必须固定,否则客户端无法连接服务器
