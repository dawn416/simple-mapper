package com.demo.simple_mapper;

import java.io.IOException;
import java.io.InputStream;

import com.demo.simple_mapper.bean.Property;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Hello world!
 *
 */
public class App {

	// 由于指令重排（还没搞懂），只有在volatile的修饰下，双检锁才有效
	public static volatile Property readValue;

	// 读取配置文件应该加双检锁保证多线程安全，并保证性能
	public static MyHandler init() throws JsonParseException, JsonMappingException, IOException {

		if (App.readValue == null) {
			ObjectMapper objm = new ObjectMapper();
			synchronized (App.class) {
				if (App.readValue == null) {
					InputStream systemResourceAsStream = ClassLoader.getSystemResourceAsStream("json/final.json");
					App.readValue = objm.readValue(systemResourceAsStream, Property.class);
					System.out.println(App.readValue);
					systemResourceAsStream.close();
				}
			}
		}

		return new MyHandler();
	}

}
