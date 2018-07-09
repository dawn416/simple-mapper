/**
 * Copyright(C) 2018 Zhejiang Fline Technology Co., Ltd. All rights reserved.
 *
 */
package com.demo.simple_mapper.bean;

import java.util.List;

/**
 * @since 2018年7月5日 下午3:36:04
 * @version 1.0.0
 * @author 黎明
 *
 */
public class MethodInfo {

	@Override
	public String toString() {
		return "MethodInfo [sqlType=" + sqlType + ", methodName=" + methodName + ", paramAnnos=" + paramAnnos
				+ ", resultType=" + resultType + ", many=" + many + ", executeSql=" + executeSql + "]";
	}

	private String sqlType;

	private String methodName;

	private List<String> paramAnnos;

	private Class<?> resultType;

	private boolean many;

	private String executeSql;

	public String getSqlType() {
		return sqlType;
	}

	public void setSqlType(String sqlType) {
		this.sqlType = sqlType;
	}

	public List<String> getParamAnnos() {
		return paramAnnos;
	}

	public void setParamAnnos(List<String> paramAnnos) {
		this.paramAnnos = paramAnnos;
	}

	public String getExecuteSql() {
		return executeSql;
	}

	public void setExecuteSql(String executeSql) {
		this.executeSql = executeSql;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public Class<?> getResultType() {
		return resultType;
	}

	public void setResultType(Class<?> resultType) {
		this.resultType = resultType;
	}

	public boolean isMany() {
		return many;
	}

	public void setMany(boolean many) {
		this.many = many;
	}

}
