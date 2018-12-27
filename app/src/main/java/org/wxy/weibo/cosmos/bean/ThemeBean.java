package org.wxy.weibo.cosmos.bean;

public class ThemeBean {
    private String name;

    private int color;

    public ThemeBean(String name, int color) {
        this.name = name;
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
