#aujaker 自动代码生成系统使用说明
###aujaker 自动生成项目可使用三种方式
1. 在线创建项目并生成
2. 通过配置xml文件上传生成
3. 通过上传excel文件生成  
---
###使用任意一种方式生成项目后即可下载项目  

* 下载的文件包含可进行二次开发的项目源文件和可直接运行的jar文件  
![图片加载中](https://zqinsheng.github.io/aujaker_img/09.png)  
![图片加载中](https://zqinsheng.github.io/aujaker_img/10.png)  

* jar文件的使用方法（在安装了java环境的电脑上运行）  

    打开命令提示符，找到生成的jar文件所在的目录（例如在D盘下）  
    ![图片加载中](https://zqinsheng.github.io/aujaker_img/12.png)  
    输入命令：**java -jar**+生成的jar文件的名称，按回车运行 

    运行之后可查看端口号  
    ![图片加载中](https://zqinsheng.github.io/aujaker_img/13.png)  

    在浏览器地址栏中输入**localhost:+端口号**  
    ![图片加载中](https://zqinsheng.github.io/aujaker_img/14.png)  

    之后就可看到生成的管理页面，可对项目中的实体类进行增、删、改、查操作  
    ![图片加载中](https://zqinsheng.github.io/aujaker_img/15.png)  

---

##三种方式的使用说明

###1.在线创建项目
* 在aujaker主页左上角点击“在线创建项目”  
![图片加载中](https://zqinsheng.github.io/aujaker_img/01.png)  


* 填写maven的配置信息,填写完成之后点击暂存 
![图片加载中](https://zqinsheng.github.io/aujaker_img/02.png)  


* 填写数据库的配置信息(暂时提供mysql和sqlite3)，填写完成之后点击暂存    
![图片加载中](https://zqinsheng.github.io/aujaker_img/03.png) 


* 根据项目需求创建相应的实体类，填写实体类的信息，可创建多个实体类    
![图片加载中](https://zqinsheng.github.io/aujaker_img/04.png)  

* 填写实体的属性  
![图片加载中](https://zqinsheng.github.io/aujaker_img/05.png)  

* 可创建多个实体类    
![图片加载中](https://zqinsheng.github.io/aujaker_img/06.png)  
 
	填写完相应的信息后，点击**预览项目框架**，确认无误之后即可生成项目（生成的时候根据项目大小可能需要一些时间，请等待一会儿 ~）  
	当生成按钮变为生成完成后，即可点击下载生成的项目  
![图片加载中](https://zqinsheng.github.io/aujaker_img/08.png)
![图片加载中](https://zqinsheng.github.io/aujaker_img/07.png)  

###2.配置xml文件上传生成项目

####配置xml模板中的信息，上传完成之后即可生成项目，xml模板如下：
```xml
<aujaker>
    <maven groupId="org.konghao.aujaker" artifactId="helloAujaker"/>

    <database type="mysql" name="msg_2017">
        <username>root</username>
        <password>123456</password>
        <url>jdbc:mysql://localhost:3306/aujaker</url>
        <driver>com.mysql.jdbc.Driver</driver> 
    </database>

     <model>
        <class className="Student" tableName="t_stu" comment="学生信息" author="ynkonghao" classShowName="学生">
            <properties>
                <prop name="id" columnName="id" type="int" isLob="false" comment="学生id" isPk="true" pkType="0"/>
                <prop name="name" type="String" comment="学生名称" />
                <prop name="sfzh" type="String" comment="身份证号"/>
                <prop name="cid" type="Integer" comment="班级id"/>
                <prop name="createDate" columnName="create_date" type="java.util.Date" comment="创建日期"/>
            </properties>
        </class>
     </model>
</aujaker>
```
###xml模板各个标签的说明
---
* **``<maven/>``**  

    对maven进行相关的配置,包含maven的配置信息,**必须要填写**

```xml
<maven groupId="org.konghao.aujaker" artifactId="helloAujaker"/>
```

| ``<maven>``中的属性名称         | 说明           |
| ------------- |:-------------:|
| groupId      | 生成项目的包名 |
| artifactId    | 项目名称      |


---

* **``<database></database>``**  
  包含数据库的配置信息，**必须要填写**
```xml
    <database type="mysql" name="msg_2017">
        <username>root</username>
        <password>123456</password>
        <url>jdbc:mysql://localhost:3306/aujaker</url>
        <driver>com.mysql.jdbc.Driver</driver>
    </database>
```

| ``<database>中的属性名称``         | 说明           | 备注  |
| ------------- |:-------------:| ------------- |
| type      | 项目使用的数据库的类型 |使用**mysql**就填写type:mysql      |
| name    | 项目使用的数据库名称      |数据库的名称      |

| ``<database>``中的节点名称         | 说明           | 备注  |
| ------------- |:-------------:| ------------- |
| ``<username></username>``      | 数据库的用户名 |如果使用**sqlite3**可不必填写    |
| ``<password></password>``    | 数据库的密码      |如果使用**sqlite3**可不必填写      |
| ``<url></url>``    | 数据库所使用的连接字符串      |例如**mysql**为jdbc:mysql://localhost:3306/aujaker      |
| ``<driver></driver>``    | 数据库所使用的连接驱动      |例如**mysql**为com.mysql.jdbc.Driver      |

---
* **``<model></mdoel>``**  
项目中所有的实体类信息，**可根据项目需要填写多个class**
```xml
    <model>
     <class className="Student" tableName="t_stu" comment="学生信息" author="ynkonghao" classShowName="学生">
            <properties>
                <prop name="id" columnName="id" type="int" isLob="false" comment="学生id" isPk="true" pkType="0"/>
                <prop name="name" type="String" comment="学生名称" />
                <prop name="idcard" type="String" comment="身份证号"/>
                <prop name="cid" type="Integer" comment="班级id"/>
                <prop name="createDate" columnName="create_date" type="java.util.Date" comment="创建日期"/>
            </properties>
     </class>

     <class className="Classroom" tableName="t_classroom" comment="班级信息" author="ynkonghao" classShowName="班级">
            <properties>
                <prop name="id" columnName="id" type="int" isLob="false" comment="班级id" isPk="true" pkType="0"/>
                <prop name="grade" type="Integer" comment="年级" />
                <prop name="name" type="String" comment="班级名称"/>
            </properties>
     </class>
    </model>
```
---
* **``<class></class>``**  
    实体类的信息，**可根据项目需要填写**
```xml
<class className="Student" tableName="t_stu" comment="学生信息" author="ynkonghao" classShowName="学生">
            <properties>
                <prop name="id" columnName="id" type="int" isLob="false" comment="学生id" isPk="true" pkType="0"/>
                <prop name="name" type="String" comment="学生名称" />
                <prop name="idcard" type="String" comment="身份证号"/>
                <prop name="cid" type="Integer" comment="班级id"/>
                <prop name="createDate" columnName="create_date" type="java.util.Date" comment="创建日期"/>
            </properties>
</class>
```

| ``<class>中的属性名称``         | 说明           | 备注  | 是否必须  |
| ------------- |:-------------:| ------------- | ------------- |
| className      | 实体类的名称 |例如，学生类：Student      |  必须填写    |
| tableName    | 数据库中的表名      |例如：t_stu     | 必须填写     |
| comment    | 实体类注释     |例如：Student``/*学生信息*/``      |   可选择填写   |
| author    | 注释作者      |例如：@author zhangsan      |  可选择填写    |
| classShowName    | 页面上显示的名称      |      |  必须填写    |

---
* **``<properties></properties>``**  

    实体类的属性，**可根据项目需要填写**

```xml
<class className="Student" tableName="t_stu" comment="学生信息" author="ynkonghao" classShowName="学生">
    <properties>
        <prop name="id" columnName="id" type="int" isLob="false" comment="学生id" isPk="true" pkType="0"/>
        <prop name="name" type="String" comment="学生名称" />
        <prop name="idcard" type="String" comment="身份证号"/>
        <prop name="cid" type="Integer" comment="班级id"/>
        <prop name="createDate" columnName="create_date" type="java.util.Date" comment="创建日期"/>
     </properties>
</class>
```

| ``<prop>中的属性名称``         | 说明           | 备注  | 是否必须  |
| ------------- |:-------------:| ------------- | ------------- |
| name      | 属性名称 |例如，学生姓名：name      |  必须填写    |
| columnName    | 数据库表中的字段名      |createDate使用create_date    | 根据字段名称填写     |
| type    | 属性类型     |例如：String,Integer,double,boolean     |   必须填写   |
| isLob    | 是否为二进制      |填写true或false      |  可根据字段填写    |
| comment    | 注释信息      |  name``/*姓名*/  ``  |  根据需要填写    |
| isPk    | 是否为主键      |  填写true或false，id为主键可写isPk="true"    |  必须填写    |
| pkType    | 主键类型      |  Integer,UUID    |  必须填写,0表示int,1表示uuid    |

---




