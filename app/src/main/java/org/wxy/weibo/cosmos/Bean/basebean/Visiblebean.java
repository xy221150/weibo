package org.wxy.weibo.cosmos.Bean.basebean;

import java.io.Serializable;

/**
 * Created by wxy on 2018/7/4.
 */

public class Visiblebean implements Serializable {
    private int type = 0;
    private int list_id = 0;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getList_id() {
        return list_id;
    }

    public void setList_id(int list_id) {
        this.list_id = list_id;
    }
}
