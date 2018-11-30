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
        initView(view);
        init();
        initdata();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // 透明状态栏
            Window window = getActivity().getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            getActivity().getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        return view;

    }
    protected abstract int getLayoutID();
    protected void initView(View view){
        this.view=view;
    }
    protected void init(){}
    protected void initdata(){}

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
