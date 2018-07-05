package com.demo.simple_mapper.bean;

/**
 * @since 2018年6月29日 上午9:36:09
 * @version 1.0.0
 * @author 
 *
 */
public class Mapper {
	private String id;
	private String type;
	private String statement;
	private String resultType;
	private boolean many;
	private Class<?> returnType;

	public Class<?> getReturnType() {
		return returnType;
	}

	public void setReturnType(Class<?> returnType) {
		this.returnType = returnType;
	}

	public boolean isMany() {
		return many;
	}

	public void setMany(boolean many) {
		this.many = many;
	}

	public String getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Mapper [id=" + id + ", type=" + type + ", statement=" + statement + ", resultType=" + resultType
				+ ", many=" + many + "]";
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatement() {
		return statement;
	}

	public void setStatement(String statement) {
		this.statement = statement;
	}

	public String getResultType() {
		return resultType;
	}

	public void setResultType(String resultType) {
		this.resultType = resultType;
	}
}
