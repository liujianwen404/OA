# OA（wx Everything-is-object）
本系统基于开源的后台系统ruoyi，在此基础上使用最新的微服务技术springcloud alibaba进行服务化，使用nacos做统一的配置与注册中心，dubbo进行服务调用，gateway做服务网关，目前已完善的模块有：
1.系统管理功能：实现对用户，角色，菜单，部门，岗位，系统日志等功能的管理维护
2.系统监控：实现了在线用户管理，后台服务监控功能
3.系统档案管理：包含了员工档案管理功能，员工劳动合同管理功能，通过导入表格统一维护员工数据
4.系统报表管理：实现了分布统计报表功能，实时分析统计各部门人数，以及转正，离职，异动等状态的员工数据
5.系统培训管理：实时管理培训计划，与知识文档
6.系统流程中心：整合Activiti工作流，使系统可以实现自动化流程建模与开发，已完成出勤休假，人员管理等十几个人事相关的流程开发
7.系统考勤管理：通过对考勤组，班次，假期等管理，实现了对考勤人员及考勤规则的设置，以及对h5端员工打卡数据的管理维护，同时还可以实时获取钉钉考勤数据，实现了双端数据的同步管理
8.系统工作计划：实现了对工作内容的记录与备忘，钉钉定时通知提醒
9.统一认证登录：鉴于公司内部系统繁多，账号管理复杂，因此使用OA系统做统一的认证登录。基本流程为：在OA配置可登录的系统信息，在客服或CRM,WMS等系统点击OA登录时，跳转到OA登录界面，在OA登录界面输入账号密码，OA后台认证通过后，分析跳转链接，然后将认证通过的用户基本信息通过JWT回调跳转到初始系统，初始系统完成授权等工作后自动重定向到授权后的页面。内部系统各自维护对应的角色权限映射关系，实现了所有内部系统的员工账号与角色在一处控制，多处使用。

此外还引入了redis做分布式缓存，还考虑使用mongodb做知识文档的管理（知识文档与通知公告涉及到的内容多样，形式不固定，因此使用文档型的nosql比较合适）

启动步骤：先启动nacos，建好配置文件，依次gateway，admin,web-api即可



