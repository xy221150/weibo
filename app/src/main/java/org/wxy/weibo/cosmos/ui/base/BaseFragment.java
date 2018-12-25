package org.wxy.weibo.cosmos.ui.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.header.MaterialHeader;
import in.srain.cube.views.ptr.util.PtrLocalDisplay;

/**
 * Created by wxy on 2018/4/21.
 */

public abstract class BaseFragment extends Fragment {
    View view;
    ProgressDialog progressDialog;
    private boolean isDestroyed=false;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(getLayoutID(),null);
        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);//恢复状态栏白色字体
        initView(view);
        init();
        initdata();
        return view;

    }
    protected abstract int getLayoutID();
    protected void initView(View view){
        this.view=view;
    }
    protected void init(){}
    protected void initdata(){}


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
        final MaterialHeader header = new MaterialHeader(getActivity());
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
        final MaterialHeader header = new MaterialHeader(getActivity());
        header.setPadding(0, PtrLocalDisplay.dp2px(15),0,0);
        mPtrClassicFrameLayout.setHeaderView(header);
        mPtrClassicFrameLayout.addPtrUIHandler(header);
    }

    public void showToast(String msg){
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }
    public void showToast(@StringRes int context){
        Toast.makeText(getActivity(), context, Toast.LENGTH_SHORT).show();
    }
    public void showProgressDialog(){
        if (progressDialog==null)
        {
            progressDialog=new ProgressDialog(getActivity()).show(getActivity(),"","正在加载",true,false);
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
    public void closeprogress(){
        if (null!=progressDialog&&progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }
    public void showProgressDialog(String mag){
        showProgressDialog();
        progressDialog.setMessage(mag);
    }
    public void starActivity(Class<?extends Activity> cls)
    {
        Intent intent=new Intent(getActivity(),cls);
        startActivity(intent);
    }
    public void starActivityofRul(Class<?extends Activity> cls, int Result)
    {
        Intent intent=new Intent();
        intent.setClass(getContext(),cls);
        startActivityForResult(intent,Result);
    }
    protected void initRecycler(RecyclerView recyclerView, RecyclerView.LayoutManager layoutManager){
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(layoutManager);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        closeprogress();
        isDestroyed=true;
    }
}
