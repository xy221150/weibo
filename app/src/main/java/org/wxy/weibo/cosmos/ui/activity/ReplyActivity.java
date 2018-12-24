package org.wxy.weibo.cosmos.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.wxy.weibo.cosmos.bean.ReplyBean;
import org.wxy.weibo.cosmos.R;
import org.wxy.weibo.cosmos.network.RetrofitHelper;
import org.wxy.weibo.cosmos.network.api.IComments;
import org.wxy.weibo.cosmos.sharepreferences.User;
import org.wxy.weibo.cosmos.ui.base.ActionbarActvity;

import retrofit2.Call;
import retrofit2.Callback;

public class ReplyActivity extends ActionbarActvity {
    private TextView text;
    private ImageView share;
    private IComments iComments;
    @Override
    protected int getContLayoutID() {
        return R.layout.activity_reply;
    }

    @Override
    protected void initView() {
        super.initView();
        text=findViewById(R.id.text);
        share=findViewById(R.id.share);
    }

    @Override
    protected void init() {
        super.init();
        final Intent intent=getIntent();
        setContTitle("回复-"+intent.getStringExtra("name"));
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reply(intent.getLongExtra("cid",0),
                        intent.getLongExtra("id",0));
            }
        });
    }
    private void reply(long cid,long id){
        iComments=RetrofitHelper.create(IComments.class);
        iComments.reply(User.user().getToken(),cid,id,text.getText().toString())
                .enqueue(new Callback<ReplyBean>() {
                    @Override
                    public void onResponse(Call<ReplyBean> call, retrofit2.Response<ReplyBean> response) {
                        if (response.code()==200)
                            showToast("回复成功");
                        else
                            showToast("回复失败");
                        finish();
                    }

                    @Override
                    public void onFailure(Call<ReplyBean> call, Throwable t) {

                    }
                });
    }
    public void ReplyActivity(Context context, String name, long cid, long id)
    {
        Intent intent=new Intent(context,ReplyActivity.class);
        intent.putExtra("name",name);
        intent.putExtra("cid",cid);
        intent.putExtra("id",id);
        context.startActivity(intent);
    }
}
