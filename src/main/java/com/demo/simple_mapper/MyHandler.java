package com.demo.simple_mapper;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.demo.simple_mapper.annotation.Param;
import com.demo.simple_mapper.annotation.Select;
import com.demo.simple_mapper.annotation.Update;
import com.demo.simple_mapper.bean.Mapper;
import com.demo.simple_mapper.bean.MethodInfo;
import com.demo.simple_mapper.bean.Property;

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
		return mapperMethod(method, args);
	}

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
		methodInfo.setMethodName(method.getName());
		Class<?> returnType = method.getReturnType();
		if (returnType != null) {
			if (returnType.isAssignableFrom(List.class)) {
				Type[] genericInterfaces = returnType.getGenericInterfaces();
				Class<? extends Type> class1 = genericInterfaces[0].getClass();
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

	/**
	 * 真正的执行过程
	 */
	@SuppressWarnings("unchecked")
	private Object mapperMethod(Method method, Object[] args) {
		String name = method.getName();
		boolean many = false;
		if (List.class.equals(method.getReturnType())) {
			many = true;
		}
		Property property = App.readValue;
		JdbcManager jdbcManager = new JdbcManager();
		// 只取第一个参数且必须为Map类型
		Map<String, Object> map = new HashMap<>();
		if (args != null) {
			map = (Map<String, Object>) args[0];
		}
		for (Mapper mapper : property.getMapper()) {
			mapper.setMany(many);
			if (name.equals(mapper.getId())) {
				try {
					return jdbcManager.sqlPrepare(mapper, map);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

}
