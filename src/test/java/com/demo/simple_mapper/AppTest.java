package com.demo.simple_mapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.demo.simple_mapper.bean.Property;
import com.demo.simple_mapper.test.Area;
import com.demo.simple_mapper.test.AreaMapper;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AppTest {
	@Test
	public void test() throws IOException {
		ObjectMapper objm = new ObjectMapper();
		InputStream systemResourceAsStream = ClassLoader.getSystemResourceAsStream("json/final.json");
		App.readValue = objm.readValue(systemResourceAsStream, Property.class);
		systemResourceAsStream.close();

		MyHandler myHandler = new MyHandler();
		AreaMapper newInstance = myHandler.newInstance(AreaMapper.class);

		Map<String, Object> map = new HashMap<>();
		map.put("id", 2);
		map.put("name", " 发斯蒂芬");
		int updateById = newInstance.updateById(map);
		Assert.assertTrue(updateById == 1);
		Area selectById = newInstance.selectById(map);
		Assert.assertNotNull(selectById);
		List<Area> select = newInstance.select();
		Assert.assertTrue(select.size() == 3);
		map.put("id", 6);
		Area selectById6 = newInstance.selectById(map);
		Assert.assertNull(selectById6);
	}
}
