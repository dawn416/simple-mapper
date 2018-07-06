package com.demo.simple_mapper;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.demo.simple_mapper.annotation.Delete;
import com.demo.simple_mapper.annotation.Insert;
import com.demo.simple_mapper.annotation.Param;
import com.demo.simple_mapper.annotation.Select;
import com.demo.simple_mapper.annotation.Update;
import com.demo.simple_mapper.bean.MethodInfo;
import com.demo.simple_mapper.bean.SqlInfo;

/**
 * @since 2018年6月29日 上午9:28:32
 * @version 1.0.0
 * @author
 *
 */
public class MyHandler implements InvocationHandler {

	@SuppressWarnings("unchecked")
	public <T> T newInstance(Class<T> clz) {
		return (T) Proxy.newProxyInstance(clz.getClassLoader(), new Class[] { clz }, this);
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		// 如果是Object类的方法，则调用当前对象的方法
		if (Object.class.equals(method.getDeclaringClass())) {
			method.invoke(this, args);
		}
		MethodInfo analyzeMethod = analyzeMethod(method);
		System.out.println(analyzeMethod);
		return mapperMethod(analyzeMethod, args);
	}

	/**
	 * 真正的执行过程
	 */
	public Object mapperMethod(MethodInfo methodInfo, Object[] args) {
		String name = methodInfo.getMethodName();
		JdbcManager jdbcManager = new JdbcManager();
		if (args == null) {
			// 执行
			return dosomething(methodInfo, args, jdbcManager);
		} else if (methodInfo.getParamAnnos().size() == args.length) {
			// 执行
			return dosomething(methodInfo, args, jdbcManager);
		}
		throw new RuntimeException("注解个数与参数个数不匹配");
	}

	/**
	 * @param methodInfo
	 * @param args
	 * @param jdbcManager
	 * @return
	 */
	private Object dosomething(MethodInfo methodInfo, Object[] args, JdbcManager jdbcManager) {
		String statement = methodInfo.getExecuteSql();
		List<Object> sqlAttrList = new ArrayList<>();
		while (statement.indexOf("#{") >= 0) {
			String substring = statement.substring(statement.indexOf("#{") + 2, statement.indexOf("}"));
			statement = statement.replace("#{" + substring + "}", "?");
			// 是否能匹配
			boolean flag = false;
			for (int i = 0; i < methodInfo.getParamAnnos().size(); i++) {
				String str = methodInfo.getParamAnnos().get(i);
				if (str.equals(substring.trim())) {
					sqlAttrList.add(args[i]);
					flag = true;
					break;
				}
			}
			if (!flag) {
				throw new RuntimeException("找不到对应的注解内容");
			}
		}
		SqlInfo sqlInfo = new SqlInfo();
		sqlInfo.setSqlParsed(statement);
		sqlInfo.setParameterList(sqlAttrList);
		try {
			return jdbcManager.sqlExecuting(methodInfo, sqlInfo);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 分析注解方式
	 * 
	 * @param method
	 * @return
	 */
	public MethodInfo analyzeMethod(Method method) {
		MethodInfo methodInfo = new MethodInfo();
		Select select = method.getAnnotation(Select.class);
		if (select != null) {
			methodInfo.setSqlType(JdbcManager.SELECT);
			methodInfo.setExecuteSql(select.value());
		}
		Update update = method.getAnnotation(Update.class);
		if (update != null) {
			methodInfo.setSqlType(JdbcManager.UPDATE);
			methodInfo.setExecuteSql(update.value());
		}
		Delete delete = method.getAnnotation(Delete.class);
		if (delete != null) {
			methodInfo.setSqlType(JdbcManager.DELETE);
			methodInfo.setExecuteSql(delete.value());
		}
		Insert insert = method.getAnnotation(Insert.class);
		if (insert != null) {
			methodInfo.setSqlType(JdbcManager.INSERT);
			methodInfo.setExecuteSql(insert.value());
		}
		methodInfo.setMethodName(method.getName());
		Class<?> returnType = method.getReturnType();
		if (returnType != null) {
			if (returnType.isAssignableFrom(List.class)) {
				Type[] type = returnType.getGenericInterfaces();
				// 通过这个方法获取了一个Type对象，里面实际上包含了类的各种基本信息，如成员变量、方法、类名和泛型的信息...
				ParameterizedType genericReturnType = (ParameterizedType) method.getGenericReturnType();
				Class class1 = (Class) genericReturnType.getActualTypeArguments()[0];
				methodInfo.setResultType(class1);
				methodInfo.setMany(true);
			} else {
				methodInfo.setResultType(returnType);
				methodInfo.setMany(false);
			}
		}
		Parameter[] parameters = method.getParameters();
		List<String> list = new ArrayList<>();
		for (Parameter parameter : parameters) {
			Param annotation = parameter.getAnnotation(Param.class);
			if (annotation != null) {
				list.add(annotation.value());
			}
		}
		methodInfo.setParamAnnos(list);
		return methodInfo;

	}

}
