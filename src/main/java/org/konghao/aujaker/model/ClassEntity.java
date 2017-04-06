package org.konghao.aujaker.model;

import java.util.List;

/**
 * 类实体，用来自动生成类的基础，和具体的属性实体进行关联
 * @author konghao
 *
 */
public class ClassEntity {
	/**
	 * 类的id
	 */
	private int id;
	/**
	 * 类的名称
	 */
	private String className;
	/**
	 * 类的包名
	 */
	private String pkgName;
	/**
	 * 类的属性列表
	 */
	private List<PropertiesBaseEntity> props;
	/**
	 * 类的注释
	 */
	private String commet;
	/**
	 * 类的作者
	 */
	private String author;
	
	private String tableName;
	
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getPkgName() {
		return pkgName;
	}
	public void setPkgName(String pkgName) {
		this.pkgName = pkgName;
	}
	public List<PropertiesBaseEntity> getProps() {
		return props;
	}
	public void setProps(List<PropertiesBaseEntity> props) {
		this.props = props;
	}
	public String getCommet() {
		return commet;
	}
	public void setCommet(String commet) {
		this.commet = commet;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	@Override
	public String toString() {
		return "ClassEntity [id=" + id + ", className=" + className + ", pkgName=" + pkgName + ", props=" + props
				+ ", commet=" + commet + ", author=" + author + ", tableName=" + tableName + "]";
	}
}
