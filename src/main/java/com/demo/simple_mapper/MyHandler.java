package com.demo.simple_mapper;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Proxy;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.demo.simple_mapper.bean.Mapper;
import com.demo.simple_mapper.bean.Property;

/**
 * @since 2018年6月29日 上午9:28:32
 * @version 1.0.0
 * @author
 *
 */
public class MyHandler implements InvocationHandler {
	/**
	 * 返回类型是否是List
	 */
	public static ThreadLocal<Boolean> isMany = new ThreadLocal<Boolean>() {
		@Override
		protected Boolean initialValue() {
			return false;
		}
	};

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
		Parameter[] parameters = method.getParameters();
		for (Parameter parameter : parameters) {
			System.out.println(parameter.getName());
		}
		return mapperMethod(method, args);
	}

	/**
	 * 真正的执行过程
	 */
	@SuppressWarnings("unchecked")
	private Object mapperMethod(Method method, Object[] args) {
		String name = method.getName();
		if (List.class.equals(method.getReturnType())) {
			isMany.set(true);
		}
		Property property = App.readValue;
		JdbcManager jdbcManager = new JdbcManager();
		// 只取第一个参数且必须为Map类型
		Map<String, Object> map = new HashMap();
		if (args != null) {
			map = (Map<String, Object>) args[0];
		}
		for (Mapper mapper : property.getMapper()) {
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
