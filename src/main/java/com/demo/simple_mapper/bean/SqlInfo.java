package com.demo.simple_mapper.bean;

import java.util.List;

/**
 * @since 2018年7月5日 下午3:31:49
 * @version 1.0.0
 * @author 黎明
 *
 */
public class SqlInfo {
	private String sqlParsed;
	private List<Object> parameterList;

	public String getSqlParsed() {
		return sqlParsed;
	}

	public void setSqlParsed(String sqlParsed) {
		this.sqlParsed = sqlParsed;
	}

	public List<Object> getParameterList() {
		return parameterList;
	}

	public void setParameterList(List<Object> parameterList) {
		this.parameterList = parameterList;
	}

	@Override
	public String toString() {
		return "SqlInfo [sqlParsed=" + sqlParsed + ", parameterList=" + parameterList + "]";
	}

}
