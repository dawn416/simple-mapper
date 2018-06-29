package com.demo.simple_mapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.demo.simple_mapper.bean.Property;
import com.demo.simple_mapper.test.Area;
import com.demo.simple_mapper.test.AreaMapper;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Hello world!
 *
 */
public class App {
	public static Property readValue;


	public static void main(String[] args) throws IOException {

		ObjectMapper objm = new ObjectMapper();
		InputStream systemResourceAsStream = ClassLoader.getSystemResourceAsStream("json/final.json");
		readValue = objm.readValue(systemResourceAsStream, Property.class);
		systemResourceAsStream.close();
		System.out.println(readValue);

		MyHandler myHandler = new MyHandler();
		AreaMapper newInstance = myHandler.newInstance(AreaMapper.class);
		List<Area> selectById = newInstance.selectById(1);
		System.out.println(selectById);

    }

}
