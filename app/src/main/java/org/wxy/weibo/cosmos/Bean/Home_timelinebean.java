package org.wxy.weibo.cosmos.Bean;

import org.wxy.weibo.cosmos.Bean.basebean.Adbean;
import org.wxy.weibo.cosmos.Bean.basebean.Statusesbean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wxy on 2018/7/4.
 */

public class Home_timelinebean {
    public List<Statusesbean> statuses=new ArrayList<>();
    public List<Adbean> ad=new ArrayList<>();
    public int total_number;

    public List<Statusesbean> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<Statusesbean> statuses) {
        this.statuses = statuses;
    }

    public List<Adbean> getAd() {
        return ad;
    }

    public void setAd(List<Adbean> ad) {
        this.ad = ad;
    }

    public int getTotal_number() {
        return total_number;
    }

    public void setTotal_number(int total_number) {
        this.total_number = total_number;
    }
}
