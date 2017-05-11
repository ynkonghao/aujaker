package org.konghao.aujaker.dto;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 钟述林 393156105@qq.com on 2017/5/11 10:46.
 * 项目DTO对象，用于解决用户在创建时刷新页面后仍然可下载
 */
public class ItemDto {

    private String date;

    private String path;

    private String status;

    private String dateLong;

    private String name;

    private String type;

    @Override
    public boolean equals(Object obj) {
        ItemDto dto = (ItemDto) obj;
        return dto.getName().equals(this.getName());
    }

    public ItemDto() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.date = sdf.format(new Date());
        this.dateLong = System.currentTimeMillis()+"";
        this.status = "0";
    }

    public ItemDto(String type, String name, String status) {
        this();
        this.type = type;
        this.status = status;
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateLong() {
        return dateLong;
    }

    public void setDateLong(String dateLong) {
        this.dateLong = dateLong;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
