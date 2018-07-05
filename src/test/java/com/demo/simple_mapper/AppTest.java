package com.demo.simple_mapper;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.demo.simple_mapper.annotation.Param;
import com.demo.simple_mapper.annotation.Select;
import com.demo.simple_mapper.annotation.Update;
import com.demo.simple_mapper.test.Area;
import com.demo.simple_mapper.test.AreaMapper;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class AppTest {


	@Test
	public void test() throws IOException, NoSuchMethodException, SecurityException {

		MyHandler myHandler = App.init();

		AreaMapper areaMapper = myHandler.newInstance(AreaMapper.class);

		Map<String, Object> map = new HashMap<>();
		map.put("id", 2);
		map.put("name", "发斯蒂芬");
		// int updateById = areaMapper.updateById("发斯蒂芬",2);
		// Assert.assertTrue(updateById == 1);
		Area selectById = areaMapper.selectById(map);
		Assert.assertNotNull(selectById);
		Area select = areaMapper.select();
		// System.out.println(select.size());
		// Assert.assertTrue(select.size() == 3);
		map.put("id", 6);
		Area selectById6 = areaMapper.selectById(map);
		Assert.assertNull(selectById6);
	}

	@Test
	public void test1() throws JsonParseException, JsonMappingException, IOException {

		// MyHandler myHandler = App.init();
		// AreaMapper areaMapper = myHandler.newInstance(AreaMapper.class);
		Class<AreaMapper> clz = AreaMapper.class;
		Method[] methods = clz.getMethods();
		for (Method method : methods) {
			System.out.println(method.getName());
			Class<?> returnType = method.getReturnType();
			// Annotation[][] parameterAnnotations =
			// method.getParameterAnnotations();
			Parameter[] parameters = method.getParameters();
			for (Parameter parameter : parameters) {
				Param annotation = parameter.getAnnotation(Param.class);
				if (annotation != null) {
					System.out.println(annotation.value());
				}
			}
			if (returnType != null) {
				if(returnType.isAssignableFrom(List.class)){
					Type[] genericInterfaces = returnType.getGenericInterfaces();
					Class<? extends Type> class1 = genericInterfaces[0].getClass();
					System.out.println(class1.getName());
				}else{
					System.out.println(returnType.getName());
				}
			}

			Select select = method.getAnnotation(Select.class);
			if (select != null) {
				System.out.println(select.value());
			}
			Update update = method.getAnnotation(Update.class);
			if (update != null) {
				System.out.println(update.value());
			}

		}
	}
}
