package com.example.SpringBootActiveMQ.bean;

import java.io.Serializable;

/**
 * @author zhongweichang
 * @email 15090552277@163.com
 * @date 2018/8/1 下午1:06
 */
public class User implements Serializable {

    private Long id;

    private String name;

    private String imageObj;

    private String message;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageObj() {
        return imageObj;
    }

    public void setImageObj(String imageObj) {
        this.imageObj = imageObj;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
