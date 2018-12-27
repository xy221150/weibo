package org.wxy.weibo.cosmos.ui.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import org.wxy.weibo.cosmos.R;
import org.wxy.weibo.cosmos.ui.activity.UserShowActivity;
import org.wxy.weibo.cosmos.utils.ThemeUtil;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.header.MaterialHeader;
import in.srain.cube.views.ptr.util.PtrLocalDisplay;

/**
 * Created by Administrator on 2018/4/11.
 */
@SuppressLint("NewApi")
public abstract class BaseActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    private boolean isDestroyed=false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);//恢复状态栏白色字体
        Theme(ThemeUtil.getTheme(this));
    }

    public void Theme(int i){
        switch (i)
        {
            case 0:
                setTheme(R.style.AppThemeNoActionBar);
                getWindow().setStatusBarColor(this.getColor(R.color.colorPrimaryDark));
                break;
            case 1:
                setTheme(R.style.BiLiPinkTheme);
                getWindow().setStatusBarColor(this.getColor(R.color.biliPink));
                break;
            case 2:
                setTheme(R.style.CloudRedTheme);
                getWindow().setStatusBarColor(this.getColor(R.color.cloudRed));
                break;
            case 3:
                setTheme(R.style.GayPurpleTheme);
                getWindow().setStatusBarColor(this.getColor(R.color.gayPurple));
                break;
            case 4:
                setTheme(R.style.SeaBlueTheme);
                getWindow().setStatusBarColor(this.getColor(R.color.seaBlue));
                break;
            case 5:
                setTheme(R.style.GrassGreenTheme);
                getWindow().setStatusBarColor(this.getColor(R.color.grassGreen));
                break;
            case 6:
                setTheme(R.style.CoffeeBrownTheme);
                getWindow().setStatusBarColor(this.getColor(R.color.coffeeBrown));
                break;
            case 7:
                setTheme(R.style.LemonOrangeTheme);
                getWindow().setStatusBarColor(this.getColor(R.color.lemonOrange));
                break;
            case 8:
                setTheme(R.style.GreyTheme);
                getWindow().setStatusBarColor(this.getColor(R.color.Grey));
                break;
            case 9:
                setTheme(R.style.NightActionbarTheme);
                getWindow().setStatusBarColor(this.getColor(R.color.nightActionbar));
                break;
        }
    }

    public void showToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
    protected void initRecycler(RecyclerView recyclerView, RecyclerView.LayoutManager layoutManager){
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(layoutManager);
    }
    public void showToast(@StringRes int context){
        Toast.makeText(this, context, Toast.LENGTH_SHORT).show();
    }
    public void showProgressDialog(){
        if (progressDialog==null)
        {
            progressDialog=new ProgressDialog(this).show(this,"","正在加载",true,false);
            return;
        }
        else
        {
            progressDialog.setTitle("");
            progressDialog.setMessage("正在加载");
            progressDialog.show();
            return;
        }
    }

    public void Ptr(PtrClassicFrameLayout mPtrClassicFrameLayout){
        // 头部阻尼系数
        mPtrClassicFrameLayout.setResistanceHeader(1.7f);
        // 底部阻尼系数
        mPtrClassicFrameLayout.setResistanceFooter(1.7f);
        // 默认1.2f，移动达到头部高度1.2倍时触发刷新操作
        mPtrClassicFrameLayout.setRatioOfHeaderHeightToRefresh(1.2f);
        // 头部回弹时间
        mPtrClassicFrameLayout.setDurationToCloseHeader(1000);
        // 底部回弹时间
        mPtrClassicFrameLayout.setDurationToCloseFooter(1000);
        // 释放刷新
        mPtrClassicFrameLayout.setPullToRefresh(false);
        // 释放时恢复到刷新状态的时间
        mPtrClassicFrameLayout.setDurationToBackHeader(200);
        mPtrClassicFrameLayout.setDurationToBackFooter(200);
        // Matrial风格头部的实现
        final MaterialHeader header = new MaterialHeader(this);
        header.setPadding(0, PtrLocalDisplay.dp2px(15),0,0);
        mPtrClassicFrameLayout.setHeaderView(header);
        mPtrClassicFrameLayout.addPtrUIHandler(header);
    }

    public void Ptr(PtrFrameLayout mPtrClassicFrameLayout){
        // 头部阻尼系数
        mPtrClassicFrameLayout.setResistanceHeader(1.7f);
        // 底部阻尼系数
        mPtrClassicFrameLayout.setResistanceFooter(1.7f);
        // 默认1.2f，移动达到头部高度1.2倍时触发刷新操作
        mPtrClassicFrameLayout.setRatioOfHeaderHeightToRefresh(1.2f);
        // 头部回弹时间
        mPtrClassicFrameLayout.setDurationToCloseHeader(1000);
        // 底部回弹时间
        mPtrClassicFrameLayout.setDurationToCloseFooter(1000);
        // 释放刷新
        mPtrClassicFrameLayout.setPullToRefresh(false);
        // 释放时恢复到刷新状态的时间
        mPtrClassicFrameLayout.setDurationToBackHeader(200);
        mPtrClassicFrameLayout.setDurationToBackFooter(200);
        // Matrial风格头部的实现
        final MaterialHeader header = new MaterialHeader(this);
        header.setPadding(0, PtrLocalDisplay.dp2px(15),0,0);
        mPtrClassicFrameLayout.setHeaderView(header);
        mPtrClassicFrameLayout.addPtrUIHandler(header);
    }

    public void closeprogress(){
        if (progressDialog!=null&&progressDialog.isShowing())
            progressDialog.dismiss();
    }
    public void showProgressDialog(String mag){
        showProgressDialog();
        progressDialog.setMessage(mag);
    }
    public void starActivity(Class<?extends Activity> cls)
    {
        Intent intent=new Intent(this,cls);
        startActivity(intent);
    }
    public void starActivityofRul(Class<?extends Activity> cls,int Result)
    {
        Intent intent=new Intent();
        intent.setClass(this,cls);
        startActivityForResult(intent,Result);
    }
    private BaseActivity getThis(){
        return this;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        closeprogress();
        isDestroyed=true;
    }
    @Override
    public boolean isDestroyed(){
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.JELLY_BEAN_MR1)
        {
            return super.isDestroyed();
        }
        return isDestroyed();
    }
}
