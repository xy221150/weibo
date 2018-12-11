package org.wxy.weibo.cosmos.ui.fragment;

import android.annotation.SuppressLint;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.wxy.weibo.cosmos.Bean.Friendshipsbean;
import org.wxy.weibo.cosmos.R;
import org.wxy.weibo.cosmos.network.RetrofitHelper;
import org.wxy.weibo.cosmos.network.api.IFriendships;
import org.wxy.weibo.cosmos.sharepreferences.User;
import org.wxy.weibo.cosmos.ui.activity.FFActivity;
import org.wxy.weibo.cosmos.ui.activity.UserShowActivity;
import org.wxy.weibo.cosmos.ui.activity.adapter.FriendshipsAdapter;
import org.wxy.weibo.cosmos.ui.base.BaseFragment;

import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.header.MaterialHeader;
import in.srain.cube.views.ptr.util.PtrLocalDisplay;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("ValidFragment")
public class FFFragment extends BaseFragment {

    private in.srain.cube.views.ptr.PtrFrameLayout PtrFrameLayout;
    private RecyclerView recycler;
    private FriendshipsAdapter adapter;
    private IFriendships iFriendships;
    private Friendshipsbean bean;
    private String id;
    private String name;
    public FFFragment(String id,String name){
        this.id=id;
        this.name=name;
    }
    @Override
    protected int getLayoutID() {
        return R.layout.fragment_ff;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        PtrFrameLayout=view.findViewById(R.id.PtrFrameLayout);
        recycler=view.findViewById(R.id.recycler);
    }

    @Override
    protected void init() {
        super.init();
        setPtrFrameAttribute();
    }

    private void setPtrFrameAttribute(){
        // 头部阻尼系数
        PtrFrameLayout.setResistanceHeader(1.7f);
        // 底部阻尼系数
        PtrFrameLayout.setResistanceFooter(1.7f);
        // 默认1.2f，移动达到头部高度1.2倍时触发刷新操作
        PtrFrameLayout.setRatioOfHeaderHeightToRefresh(1.2f);
        // 头部回弹时间
        PtrFrameLayout.setDurationToCloseHeader(1000);
        // 底部回弹时间
        PtrFrameLayout.setDurationToCloseFooter(1000);
        // 释放刷新
        PtrFrameLayout.setPullToRefresh(false);
        // 释放时恢复到刷新状态的时间
        PtrFrameLayout.setDurationToBackHeader(200);
        PtrFrameLayout.setDurationToBackFooter(200);
        // Matrial风格头部的实现
        final MaterialHeader header = new MaterialHeader(getActivity());
        header.setPadding(0, PtrLocalDisplay.dp2px(15),0,0);
        PtrFrameLayout.setHeaderView(header);
        PtrFrameLayout.addPtrUIHandler(header);
        PtrFrameLayout.post(new Runnable() {
            @Override
            public void run() {
                // 进入界面自动刷新
                PtrFrameLayout.autoRefresh();

            }
        });
        PtrFrameLayout.setPtrHandler(new PtrDefaultHandler2() {
            // 加载更多开始会执行该方法
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {

                if (name.equals("friend")) {
                    friend(id);
                }
                if (name.equals("followers")) {
                    followers(id);
                }
            }

            // 刷新开始会执行该方法
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {

                if (name.equals("friend")) {
                    friend(id);
                }
                if (name.equals("followers")) {
                    followers(id);
                }
                // 用于关闭下拉刷新
                PtrFrameLayout.refreshComplete();
            }
        });


    }

    private void friend(String id){
        iFriendships= RetrofitHelper.create(IFriendships.class);
        iFriendships.friend(User.user().getToken(),id,0)
                .enqueue(new Callback<Friendshipsbean>() {
                    @Override
                    public void onResponse(Call<Friendshipsbean> call, Response<Friendshipsbean> response) {
                        bean=response.body();
                        if (bean.getUsers()!=null)
                        {
                            adapter=new FriendshipsAdapter(bean);
                            initRecycler(recycler,new LinearLayoutManager(getActivity()));
                            recycler.setAdapter(adapter);
                            adapter.setOnItemClickListener(new FriendshipsAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(int position) {
                                    new UserShowActivity().getName(getActivity(),bean.getUsers().get(position).getName());
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(Call<Friendshipsbean> call, Throwable t) {

                    }
                });
    }

    private void followers(String id){
        iFriendships= RetrofitHelper.create(IFriendships.class);
        iFriendships.followers(User.user().getToken(),id)
                .enqueue(new Callback<Friendshipsbean>() {
                    @Override
                    public void onResponse(Call<Friendshipsbean> call, Response<Friendshipsbean> response) {
                        bean=response.body();
                        if (bean!=null||bean.getUsers()!=null)
                        {
                            adapter=new FriendshipsAdapter(bean);
                            initRecycler(recycler,new LinearLayoutManager(getActivity()));
                            recycler.setAdapter(adapter);
                            adapter.setOnItemClickListener(new FriendshipsAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(int position) {
                                    new UserShowActivity().getName(getActivity(),bean.getUsers().get(position).getName());
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(Call<Friendshipsbean> call, Throwable t) {

                    }
                });
    }
}
