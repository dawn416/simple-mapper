/**
 * Copyright(C) 2018 Zhejiang Fline Technology Co., Ltd. All rights reserved.
 *
 */
package com.demo.simple_mapper;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
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

	@SuppressWarnings("unchecked")
	public <T> T newInstance(Class<T> clz) {
		return (T) Proxy.newProxyInstance(clz.getClassLoader(), new Class[] { clz }, this);
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

		if (Object.class.equals(method.getDeclaringClass())) {
			method.invoke(this, args);
		}

		return mapperMethod(method, args);
	}

	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	private Object mapperMethod(Method method, Object[] args) {
		String name = method.getName();
		Property property = App.readValue;
		JdbcManager jdbcManager = new JdbcManager();
		Map<String, Object> map = (Map<String, Object>) args[0];
		for (Mapper mapper : property.getMapper()) {
			if (name.equals(mapper.getId())) {
				if ("select".equals(mapper.getType())) {
					return jdbcManager.selectExecute(mapper, map);
				}
				if ("update".equals(mapper.getType())) {

				}
				if ("delete".equals(mapper.getType())) {

				}
			}
		}
		return null;
	}

}
