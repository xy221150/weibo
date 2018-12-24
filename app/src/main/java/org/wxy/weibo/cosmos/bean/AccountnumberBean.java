package org.wxy.weibo.cosmos.bean;

public class AccountnumberBean {
    private String name;
    private String token;
    private String uid;
    private String url;
    public AccountnumberBean(String name,String token,String uid,String url){
        this.name=name;
        this.token=token;
        this.uid=uid;
        this.url=url;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
