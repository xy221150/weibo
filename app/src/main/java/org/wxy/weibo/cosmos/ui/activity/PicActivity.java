package org.wxy.weibo.cosmos.ui.activity;

import android.content.Context;
import android.content.Intent;

import org.wxy.weibo.cosmos.Bean.basebean.PicUrlsBean;
import org.wxy.weibo.cosmos.R;
import org.wxy.weibo.cosmos.ui.base.WidgetActivity;

import java.io.Serializable;
import java.util.List;


public class PicActivity extends WidgetActivity {
    @Override
    protected int getLayoutID() {
        return R.layout.activity_pic;
    }

    @Override
    protected void initView() {
        super.initView();
    }

    @Override
    protected void init() {
        super.init();

    }
    public void IntentPic(Context context, List<PicUrlsBean> beans){
        Intent intent=new Intent(context,PicActivity.class);
        intent.putExtra("bean", (Serializable) beans);
        context.startActivity(intent);
    }
}
