package me.home.bangumi.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;

@Entity
public class Index {

    @Id
    private long id;
    private String name_cn;
    private String name;
    private String img_url;
    private String date;
    private String category;
    private String bangumi_id;
    private String info;

    public Index(long id) {
        this.id = id;
    }

    @Generated
    public Index(long id, String name_cn, String name, String img_url, String bangumi_id, String info, String category, String date) {
        this.id = id;
        this.name_cn = name_cn;
        this.name = name;
        this.img_url = img_url;
        this.bangumi_id = bangumi_id;
        this.info = info;
        this.category = category;
        this.date = date;
    }

    @Generated
    public Index(Long id) {
    }

    @Generated
    public Index() {

    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {

        return date;
    }

    public void setName_cn(String name_cn) {
        this.name_cn = name_cn;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public void setBangumi_id(String bangumi_id) {
        this.bangumi_id = bangumi_id;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Generated
    public String getName_cn() {
        return name_cn;
    }

    public String getName() {
        return name;
    }

    public String getImg_url() {
        return img_url;
    }

    public String getBangumi_id() {
        return bangumi_id;
    }

    public String getInfo() {
        return info;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {

        return category;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
