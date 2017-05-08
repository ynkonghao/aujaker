package org.konghao.aujaker.dto;

import java.util.List;

/**
 * Created by 钟述林 393156105@qq.com on 2017/5/5 10:10.
 */
public class ClsDto {

    private String className;

    private String tableName;

    private String classShowName;

    private String author;

    @Override
    public boolean equals(Object obj) {
        ClsDto dto = (ClsDto) obj;
        return dto.getClassName().equalsIgnoreCase(this.getClassName()) || dto.getTableName().equalsIgnoreCase(this.getTableName());
//        return super.equals(obj);
    }

    @Override
    public String toString() {
        return "ClsDto{" +
                "className='" + className + '\'' +
                ", tableName='" + tableName + '\'' +
                ", classShowName='" + classShowName + '\'' +
                ", author='" + author + '\'' +
                ", propertyDtoList=" + propertyDtoList +
                '}';
    }

    private List<PropertyDto> propertyDtoList;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getClassShowName() {
        return classShowName;
    }

    public void setClassShowName(String classShowName) {
        this.classShowName = classShowName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public List<PropertyDto> getPropertyDtoList() {
        return propertyDtoList;
    }

    public void setPropertyDtoList(List<PropertyDto> propertyDtoList) {
        this.propertyDtoList = propertyDtoList;
    }
}
