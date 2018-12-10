package org.wxy.weibo.cosmos.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import org.wxy.weibo.cosmos.Bean.ShowBean;
import org.wxy.weibo.cosmos.Bean.Userbean;
import org.wxy.weibo.cosmos.R;
import org.wxy.weibo.cosmos.ui.base.ActionbarActvity;
import org.wxy.weibo.cosmos.utils.GlideUtil;
import org.wxy.weibo.cosmos.utils.TimeUtils;
import org.wxy.weibo.cosmos.view.CircleImageView;

import java.io.Serializable;

public class UserAcitivty extends ActionbarActvity {
    private CircleImageView img;
    private TextView name;
    private TextView friends;
    private TextView followers;
    private TextView autograph;
    private TextView address;
    private TextView sex;
    private TextView registrationtime;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContTitle("个人详情");
    }

    @Override
    protected int getContLayoutID() {
        return R.layout.activity_uesr;
    }

    @Override
    protected void initView() {
        super.initView();
        img=findViewById(R.id.img);
        name=findViewById(R.id.name);
        friends=findViewById(R.id.friends);
        followers=findViewById(R.id.followers);
        autograph=findViewById(R.id.autograph);
        address=findViewById(R.id.address);
        sex=findViewById(R.id.sex);
        registrationtime=findViewById(R.id.registrationtime);
    }

    @Override
    protected void init() {
        super.init();
        Intent intent=getIntent();
        Userbean userbean=(Userbean) intent.getSerializableExtra("user");
        GlideUtil.load(this,img,userbean.getAvatar_large());
        name.setText(userbean.getName());
        friends.setText("关注 "+userbean.getFriends_count());
        followers.setText("粉丝 "+userbean.getFavourites_count());
        autograph.setText(userbean.getDescription()+" ");
        address.setText(userbean.getLocation()+" ");
        sex.setText(sex(userbean.getGender()));
        registrationtime.setText(TimeUtils.convDate1(userbean.getCreated_at()) +" ");
    }
    private String sex(String str){
        String sex=null;
        switch (str)
        {
            case "m":
                sex="男";
                break;
            case "f":
                sex="女";
                break;
            case "n":
                sex="未知";
                break;
                default:
        }
        return sex;
    }
    public void IntentUser(Context context, Userbean userbean){
        Intent intent=new Intent(context,UserAcitivty.class);
        intent.putExtra("user",(Serializable) userbean);
        context.startActivity(intent);
    }
}
