package com.demo.simple_mapper;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.junit.Test;

import com.demo.simple_mapper.annotation.Param;
import com.demo.simple_mapper.annotation.Select;
import com.demo.simple_mapper.annotation.Update;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class AppTest {


	@Test
	public void test() throws IOException, NoSuchMethodException, SecurityException {

		MyHandler myHandler = App.init();

		AreaMapper areaMapper = myHandler.newInstance(AreaMapper.class);

		Area selectById = areaMapper.selectById(1);
		System.out.println(selectById);
		int updateById = areaMapper.updateById("马马密码", 1);
		System.out.println(updateById);
		List<Area> select = areaMapper.select();
		System.out.println(select);
		int deleteById = areaMapper.deleteById(60);
		System.out.println(deleteById);
		int insert = areaMapper.insert("艰苦艰苦");
		System.out.println(insert);
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
					// Type type = ((ParameterizedType)
					// returnType.getGenericSuperclass()).getActualTypeArguments()[0];
					// Type[] genericInterfaces =
					// returnType.getGenericInterfaces();
					// Class<? extends Type> class1 =
					// genericInterfaces[0].getClass();
					System.out.println(
							"泛型类型：" + ((ParameterizedType) method.getGenericReturnType()).getActualTypeArguments()[0]);
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
