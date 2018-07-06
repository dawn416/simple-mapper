# simple-mapper
类似mybatis的小型orm框架
使用jdk动态代理+反射+jdbc实现

#开发心得
开发注解方式时update和selectOne写了一堆代码，第一次就跑成功了，开心
反射获取方法返回类型的泛型不能从method.getReturnType中取，而应从method.getGenericReturnType中取
多线程环境下线程安全未测试，初始化时对配置类加了双检锁机制，配置信息对象的set方法还应该把作用域缩小


有没有人喜欢这样操作数据库
List<HashMap> list = DbManager.table("t_user").where("id=",1).list();