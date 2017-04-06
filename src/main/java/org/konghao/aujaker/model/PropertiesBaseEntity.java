package org.konghao.aujaker.model;

/**
 * 类中的属性
 * @author konghao
 *
 */
public class PropertiesBaseEntity {
	/**
	 * 属性的主键
	 */
	private int id;
	/**
	 * 属性名称
	 */
	private String name;
	/**
	 * 数据库字段名称
	 */
	private String columnName;
	/**
	 * 属性类型
	 * java.lang
	 * java.util.Date等
	 */
	private String type;
	/**
	 * 是否是文本数据
	 */
	private boolean isLob;
	/**
	 * 属性的注释
	 */
	private String commet;
	/**
	 * 是否是主键,0表示不是，1表示是
	 */
	private boolean isPk;
	/**
	 * 主键类型，0表示int，1表示uuid
	 */
	private int pkType;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCommet() {
		return commet;
	}
	public void setCommet(String commet) {
		this.commet = commet;
	}
	
	public boolean isPk() {
		return isPk;
	}
	public void setPk(boolean isPk) {
		this.isPk = isPk;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public boolean isLob() {
		return isLob;
	}
	public void setLob(boolean isLob) {
		this.isLob = isLob;
	}
	public int getPkType() {
		return pkType;
	}
	public void setPkType(int pkType) {
		this.pkType = pkType;
	}
}
