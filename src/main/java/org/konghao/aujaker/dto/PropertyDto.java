package org.konghao.aujaker.dto;

/**
 * Created by 钟述林 393156105@qq.com on 2017/5/5 10:07.
 */
public class PropertyDto {

    private String name;

    private String type;

    private String columnName;

    private String comment;

    private String isLob;

    private String isPk;

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

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getIsLob() {
        return isLob;
    }

    public void setIsLob(String isLob) {
        this.isLob = isLob;
    }

    public String getIsPk() {
        return isPk;
    }

    public void setIsPk(String isPk) {
        this.isPk = isPk;
    }

    @Override
    public String toString() {
        return "PropertyDto{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", columnName='" + columnName + '\'' +
                ", comment='" + comment + '\'' +
                ", isLob='" + isLob + '\'' +
                ", isPk='" + isPk + '\'' +
                '}';
    }
}
