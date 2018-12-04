package org.wxy.weibo.cosmos.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.NormalListDialog;

import org.wxy.weibo.cosmos.R;
import org.wxy.weibo.cosmos.Bean.Home_timelinebean;
import org.wxy.weibo.cosmos.Bean.Userbean;
import org.wxy.weibo.cosmos.network.RetrofitHelper;
import org.wxy.weibo.cosmos.network.api.IStatuses;
import org.wxy.weibo.cosmos.network.api.IUser;
import org.wxy.weibo.cosmos.sharepreferences.User;
import org.wxy.weibo.cosmos.ui.activity.FriendsActivity;
import org.wxy.weibo.cosmos.ui.activity.ShowActivity;
import org.wxy.weibo.cosmos.ui.activity.UserAcitivty;
import org.wxy.weibo.cosmos.ui.base.BaseFragment;
import org.wxy.weibo.cosmos.ui.fragment.adapter.MyWeiboRecyclerAdapter;
import org.wxy.weibo.cosmos.utils.GlideUtil;
import org.wxy.weibo.cosmos.view.CircleImageView;
import org.wxy.weibo.cosmos.view.VerticalScrollView;

import java.util.ArrayList;
import java.util.List;

import cc.shinichi.library.ImagePreview;
import cc.shinichi.library.bean.ImageInfo;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.header.MaterialHeader;
import in.srain.cube.views.ptr.util.PtrLocalDisplay;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by wxy on 2018/7/4.
 */

public class MyFragment extends BaseFragment implements VerticalScrollView.ScrollViewListener,View.OnClickListener{
    private TextView type;
    private VerticalScrollView mVerticalScrollView;
    private LinearLayout mToolbar;
    private LinearLayout friends;
    private LinearLayout followers;
    private CircleImageView mToolbar_Profile_image;
    private TextView mToolbar_Name;
    private RelativeLayout mHead;
    private TextView mName;
    private TextView mFollowers_count;
    private TextView mFriends_count;
    private CircleImageView mProfile_image;
    private IUser user;
    private IStatuses statuses;
    private RecyclerView myWeibolist;
    private int feature=0;
    private int page=1;
    private int height;
    private MyWeiboRecyclerAdapter adapter;
    private PtrClassicFrameLayout mPtrClassicFrameLayout;
    private Home_timelinebean bean=new Home_timelinebean();
    private NormalListDialog dialog;
    private List<ImageInfo> list;
    private ImageInfo imageInfo;
    private Userbean userbean;
    private MsgThread msgThread;
    private static final int REFRESH=1;//下拉重新加载
    private static final int LOADMORE=0;//上拉加载更多
    private static final int AUTO=-1;//进入自动刷新
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what)
            {
                case REFRESH:
                    page=1;
                    Statusers();
                    // 用于关闭下拉刷新
                    mPtrClassicFrameLayout.refreshComplete();
                    break;
                case LOADMORE:
                    page=page+1;
                    Statusers();
                    mPtrClassicFrameLayout.refreshComplete();
                    break;
                case AUTO:
                    mPtrClassicFrameLayout.autoRefresh();
                    Statusers();
                    break;
                default:
            }
        }
    };
    @Override
    protected int getLayoutID() {
        return R.layout.fragment_my;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        mToolbar=view.findViewById(R.id.my_toolbar);
        mToolbar_Profile_image=view.findViewById(R.id.my_toolbar_image);
        mToolbar_Name=view.findViewById(R.id.my_toolbar_name);
        mVerticalScrollView=view.findViewById(R.id.my_verticalscrollview);
        mProfile_image=view.findViewById(R.id.my_profile_image);
        mHead=view.findViewById(R.id.my_head);
        mName=view.findViewById(R.id.my_name);
        mFollowers_count=view.findViewById(R.id.my_followers_count);
        mFriends_count=view.findViewById(R.id.my_friends_count);
        myWeibolist=view.findViewById(R.id.my_weibo_list);
        type=view.findViewById(R.id.type);
        mPtrClassicFrameLayout=view.findViewById(R.id.my_ptrclassic);
        friends=view.findViewById(R.id.friends);
        followers=view.findViewById(R.id.followers);
        user= RetrofitHelper.create(IUser.class);
        statuses=RetrofitHelper.create(IStatuses.class);
        ((DefaultItemAnimator)myWeibolist.getItemAnimator()).setSupportsChangeAnimations(false);
        myWeibolist.getItemAnimator().setChangeDuration(0);
    }

    @Override
    protected void init() {
        super.init();
        adapter=new MyWeiboRecyclerAdapter(bean);
        myWeibolist.setAdapter(adapter);
    }

    @Override
    protected void initdata() {
        super.initdata();
        User();
        initListeners();
        setPtrFrameAttribute();
        type.setOnClickListener(this);
        friends.setOnClickListener(this);
        followers.setOnClickListener(this);
        mProfile_image.setOnClickListener(this);
    }
    private void Statusers(){
        statuses.getuser_timeline(User.user().getToken(),User.user().getUid(),null,15,page,feature).
                enqueue(new Callback<Home_timelinebean>() {
                    @Override
                    public void onResponse(Call<Home_timelinebean> call, Response<Home_timelinebean> response) {
                       bean=response.body();
                        if (bean!=null) {
                            myWeibolist.setVisibility(View.VISIBLE);
                            //页码为1时适配器实例化,
                            if (page == 1) {
                                adapter = new MyWeiboRecyclerAdapter(bean);
                                adapter.setHasStableIds(true);//解决数据错乱
                            }
                            else if (page > 1){ // 页码大于1添加到适配器
                                adapter.add(bean);
                                adapter.notifyItemChanged(adapter.getItemCount());
                            }
                            if (bean.getStatuses().size() == 0)
                            {
                                showToast("到底了");
                            }
                            initRecycler(myWeibolist,new LinearLayoutManager(getActivity()));
                            myWeibolist.setAdapter(adapter);
                            myWeibolist.setNestedScrollingEnabled(false);
                            if (mPtrClassicFrameLayout.isRefreshing())
                            {
                                mPtrClassicFrameLayout.refreshComplete();
                            }
                            adapter.onItemCilck(new MyWeiboRecyclerAdapter.onItemCilck() {
                                @Override
                                public void onItemCilck(int i) {
                                    new ShowActivity().Intent(getActivity(),adapter.getdata(i).getId());
                                }
                            });
                            adapter.onItemStatusCilck(new MyWeiboRecyclerAdapter.onItemStatusCilck() {
                                @Override
                                public void onItemStatusCilck(int i) {
                                    new ShowActivity().Intent(getActivity(),adapter.getdata(i).getRetweeted_status().getId());
                                }
                            });
                            adapter.onItemPicCilck(new MyWeiboRecyclerAdapter.onItemPicCilck() {
                                @Override
                                public void onItemPicCilck(int i, int j) {
                                    list=new ArrayList<>();
                                    for (int n=0;n<adapter.getdata(i).getPic_urls().size();n++)
                                    {
                                        imageInfo=new ImageInfo();
                                        imageInfo.setThumbnailUrl("http://wx1.sinaimg.cn/large/"+adapter.getdata(i).getPic_urls().get(n).getThumbnail_pic().substring(32));
                                        imageInfo.setOriginUrl("http://wx1.sinaimg.cn/large/"+adapter.getdata(i).getPic_urls().get(n).getThumbnail_pic().substring(32));
                                        list.add(n,imageInfo);
                                    }
                                    ImagePreview.getInstance().setContext(getActivity()).setImageInfoList(list).setIndex(j).start();
                                }
                            });
                            adapter.onItemRetweetedPicCilck(new MyWeiboRecyclerAdapter.onItemRetweetedPicCilck() {
                                @Override
                                public void onItemRetweetedPicCilck(int i, int j) {
                                    list=new ArrayList<>();
                                    for (int n=0;n<adapter.getdata(i).getRetweeted_status().getPic_urls().size();n++)
                                    {
                                        imageInfo=new ImageInfo();
                                        imageInfo.setThumbnailUrl("http://wx1.sinaimg.cn/large/"+adapter.getdata(i).getRetweeted_status().getPic_urls().get(n).getThumbnail_pic().substring(32));
                                        imageInfo.setOriginUrl("http://wx1.sinaimg.cn/large/"+adapter.getdata(i).getRetweeted_status().getPic_urls().get(n).getThumbnail_pic().substring(32));
                                        list.add(n,imageInfo);
                                    }
                                    ImagePreview.getInstance().setContext(getActivity()).setImageInfoList(list).setIndex(j).start();
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(Call<Home_timelinebean> call, Throwable t) {

                    }
                });
    }

    private void User(){
        user.getuser(User.user().getToken(),User.user().getUid(),null)
                .enqueue(new Callback<Userbean>() {
                    @Override
                    public void onResponse(Call<Userbean> call, Response<Userbean> response) {
                        userbean=response.body();
                       if (userbean==null)
                       {
                           showToast("获取数据失败");
                       }
                       else
                       {
                           User.user().setName(userbean.getScreen_name());
                           GlideUtil.load(getActivity(),mProfile_image,userbean.getAvatar_large());
                           GlideUtil.load(getActivity(),mToolbar_Profile_image,userbean.getAvatar_large());
                           mName.setText(userbean.getScreen_name());
                           mToolbar_Name.setText(userbean.getScreen_name());
                           mFollowers_count.setText(userbean.getFollowers_count());
                           mFriends_count.setText(userbean.getFriends_count());
                       }
                    }
                    @Override
                    public void onFailure(Call<Userbean> call, Throwable t) {}});
    }



    public void setPtrFrameAttribute() {
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
        mPtrClassicFrameLayout.post(new Runnable() {
            @Override
            public void run() {
                msgThread=new MsgThread(AUTO);
                msgThread.run();
            }
        });
        mPtrClassicFrameLayout.setPtrHandler(new PtrDefaultHandler2() {
            // 加载更多开始会执行该方法
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                msgThread=new MsgThread(LOADMORE);
                msgThread.run();
            }

            // 刷新开始会执行该方法
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                msgThread=new MsgThread(REFRESH);
                msgThread.run();
            }
        });
    }

    private void initListeners() {

        ViewTreeObserver vto = mHead.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mToolbar.getViewTreeObserver().removeGlobalOnLayoutListener(
                        this);
                height = mHead.getHeight();
                mVerticalScrollView.setScrollViewListener(MyFragment.this);
            }
        });
    }

    @Override
    public void onScrollChanged(VerticalScrollView scrollView, int x, int y, int oldx, int oldy) {
        if (y <= 0) {//设置标题的背景颜色
            mToolbar.setBackgroundColor(Color.argb((int) 0, 66,168,228));
            mToolbar.setVisibility(View.GONE);
        } else if (y > 0 && y <= height) { //滑动距离小于banner图的高度时，设置背景和字体颜色颜色透明度渐变
            float scale = (float) y / height;
            float alpha = (255 * scale);
            mToolbar.setVisibility(View.VISIBLE);
            mToolbar.setBackgroundColor(Color.argb((int) alpha, 66,168,228));
        } else {    //滑动到banner下面设置普通颜色
            mToolbar.setBackgroundColor(Color.parseColor("#42A8E4"));
        }
    }

    //选择微博类型
    private void Dialog(){
        final String[] strings={"全部微博","原创微博","图片","视频","音乐"};
        dialog=new NormalListDialog(getActivity(),strings)
                .itemTextColor(Color.parseColor("#42A8E4"))
                .title("选择类型")
                .titleBgColor(Color.parseColor("#42A8E4"))
                .titleTextColor(Color.parseColor("#FEFFFF"));
        dialog.show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                feature=position;
                setPtrFrameAttribute();
                type.setText(strings[position]);
                mVerticalScrollView.smoothScrollTo(0,0);
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.type:
                Dialog();
                break;
            case R.id.friends:
                Intent intent=new Intent(getActivity(),FriendsActivity.class);
                intent.putExtra("friendships","friend");
                intent.putExtra("id",User.user().getUid());
                startActivity(intent);
                break;
            case R.id.followers:
                Intent intent1=new Intent(getActivity(),FriendsActivity.class);
                intent1.putExtra("friendships","followers");
                intent1.putExtra("id",User.user().getUid());
                startActivity(intent1);
                break;
            case R.id.my_profile_image:
                if (userbean!=null)
                    new UserAcitivty().IntentUser(getActivity(),userbean);
                else
                    showToast("失败");
                break;
        }
    }
    class MsgThread extends Thread{
        private int method;
        private Message message;
        public MsgThread(int method){
            this.method=method;
            message=new Message();
        }
        @Override
        public void run() {
            super.run();
            if (method==REFRESH)
            {
                message.what=method;
                handler.sendMessage(message);
                return;
            }
            if (method==LOADMORE)
            {
                message.what=method;
                handler.sendMessage(message);
                return;
            }
            if (method==AUTO)
            {
                message.what=method;
                handler.sendMessage(message);
                return;
            }
        }
    }
}
