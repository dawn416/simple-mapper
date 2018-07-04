package com.demo.simple_mapper.bean;

import java.util.List;

/**
 * @since 2018年6月29日 上午9:33:39
 * @version 1.0.0
 * @author 
 *
 */
public class Property {
	private String namespace;
	private List<Mapper> mapper;

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public List<Mapper> getMapper() {
		return mapper;
	}

	public void setMapper(List<Mapper> mapper) {
		this.mapper = mapper;
	}

	@Override
	public String toString() {
		return "Property [namespace=" + namespace + ", mapper=" + mapper + "]";
	}

}
