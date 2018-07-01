package com.demo.simple_mapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.demo.simple_mapper.bean.Property;
import com.demo.simple_mapper.test.AreaMapper;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AppTest {
	@Test
	public void test() throws IOException {
		ObjectMapper objm = new ObjectMapper();
		InputStream systemResourceAsStream = ClassLoader.getSystemResourceAsStream("json/final.json");
		App.readValue = objm.readValue(systemResourceAsStream, Property.class);
		systemResourceAsStream.close();
		System.out.println(App.readValue);

		MyHandler myHandler = new MyHandler();
		AreaMapper newInstance = myHandler.newInstance(AreaMapper.class);
		Map<String, Object> map = new HashMap<>();
		map.put("id", 3);
		map.put("name", "lalala");
		int selectById = newInstance.updateById(map);
		System.out.println(selectById);
	}
}
