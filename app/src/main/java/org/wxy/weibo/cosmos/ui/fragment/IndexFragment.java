package org.wxy.weibo.cosmos.ui.fragment;


import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;


import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.NormalListDialog;

import org.wxy.weibo.cosmos.R;
import org.wxy.weibo.cosmos.Bean.Home_timelinebean;
import org.wxy.weibo.cosmos.network.RetrofitHelper;
import org.wxy.weibo.cosmos.network.api.IStatuses;
import org.wxy.weibo.cosmos.sharepreferences.User;
import org.wxy.weibo.cosmos.ui.activity.AccountnumberActivity;
import org.wxy.weibo.cosmos.ui.activity.ShareActivity;
import org.wxy.weibo.cosmos.ui.activity.ShowActivity;
import org.wxy.weibo.cosmos.ui.base.BaseFragment;
import org.wxy.weibo.cosmos.ui.fragment.adapter.MyWeiboRecyclerAdapter;
import org.wxy.weibo.cosmos.view.TopicScrollView;

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

public class IndexFragment extends BaseFragment implements View.OnClickListener{
    private RecyclerView mWeiboRecycler;
    private IStatuses statuses;
    private MyWeiboRecyclerAdapter adapter;
    private PtrClassicFrameLayout mPtrClassicFrameLayout;//上拉加载下拉刷新
    private Home_timelinebean bean=new Home_timelinebean();
    private int page=1;//页码
    private int feature=0;//微博类型
    private FloatingActionButton fabOpen;//fab
    private FloatingActionButton fabType;
    private FloatingActionButton fabShare;
    private AnimatorSet mHideFAB;//隐藏fab
    private AnimatorSet mShowFAB;//显示fab
    private TopicScrollView topsoroll;
    private boolean FAB_VISIBLE = true;
    private int mPreviousFirstVisibleItem;
    private int mLastScrollY;
    private int mScrollThreshold = 2;
    private NormalListDialog dialog;
    private List<ImageInfo> list;//图片数组
    private ImageInfo imageInfo;
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
                    date();
                    // 用于关闭下拉刷新
                    mPtrClassicFrameLayout.refreshComplete();
                    break;
                case LOADMORE:
                    page=page+1;
                    date();
                    break;
                case AUTO:
                    mPtrClassicFrameLayout.autoRefresh();
                    date();
                    break;
                    default:
            }
        }
    };
    @Override
    protected int getLayoutID() {
        return R.layout.fragment_index;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        mWeiboRecycler=view.findViewById(R.id.homepage_weibo_list);
        mPtrClassicFrameLayout=view.findViewById(R.id.homepage_ptrclassic);
        fabOpen=view.findViewById(R.id.fabOpen);
        fabShare=view.findViewById(R.id.fabShare);
        fabType=view.findViewById(R.id.fabType);
        topsoroll=view.findViewById(R.id.topsoroll);
    }


    @SuppressLint("NewApi")
    @Override
    protected void init() {
        super.init();
        mHideFAB=(AnimatorSet) AnimatorInflater.loadAnimator(getActivity(),R.animator.scroll_hide_fab);
        mShowFAB=(AnimatorSet)AnimatorInflater.loadAnimator(getActivity(),R.animator.scroll_show_fab);
        mHideFAB.setTarget(fabOpen);
        mShowFAB.setTarget(fabOpen);
        fabOpen.setOnClickListener(this);
        fabShare.setOnClickListener(this);
        fabType.setOnClickListener(this);
    }
    @Override
    protected void initdata() {
        super.initdata();
        Fab();
        adapter=new MyWeiboRecyclerAdapter(bean);
        mWeiboRecycler.setAdapter(adapter);
        setPtrFrameAttribute();
    }

    @SuppressLint("NewApi")
    private void Fab(){
        topsoroll.setOnScrollChangeListener(new View.OnScrollChangeListener() {//滑动事件
            @Override
            public void onScrollChange(View view, int firstVisibleItem, int i1, int i2, int totalItemCount) {
                if(totalItemCount == 0) {//显示fab悬浮按钮
                    showFAB();
                    return;
                }
                //滚动过程中：ListView中最上面一个Item还是同一个Item
                if(isSameRow(firstVisibleItem)) {
                    int newScrollY = view.getScrollY();
                    boolean isExceedThreshold = Math.abs(mLastScrollY - newScrollY) > mScrollThreshold;
                    if (isExceedThreshold) {
                        if (mLastScrollY > newScrollY && FAB_VISIBLE == false) {
                            FAB_VISIBLE = true;
                            showFAB();
                        } else if(mLastScrollY < newScrollY && FAB_VISIBLE == true){
                            FAB_VISIBLE = false;
                            hideFAB();
                            fabType.setVisibility(View.GONE);
                            fabShare.setVisibility(View.GONE);
                            fabOpen.setImageResource(R.mipmap.ic_add);
                        }
                    }
                    mLastScrollY = newScrollY;
                } else {
                    if (firstVisibleItem > mPreviousFirstVisibleItem && FAB_VISIBLE == true){  //向下滚动

                        FAB_VISIBLE = true;
                        showFAB();

                    } else if(firstVisibleItem < mPreviousFirstVisibleItem && FAB_VISIBLE == false){ //向上滚动
                        FAB_VISIBLE = false;
                        hideFAB();
                        fabType.setVisibility(View.GONE);
                        fabShare.setVisibility(View.GONE);
                        fabOpen.setImageResource(R.mipmap.ic_add);
                    }
                    mLastScrollY = getTopItemScrollY();
                    mPreviousFirstVisibleItem = firstVisibleItem;
                }
            }
        });
    }
   //上拉刷新下拉加载
    private void setPtrFrameAttribute() {
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
                // 进入界面自动刷新
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
   //首页数据
    private void date(){
        statuses=RetrofitHelper.create(IStatuses.class);
        statuses.gethome_timeline(User.user().getToken(),15,page,feature).
                enqueue(new Callback<Home_timelinebean>() {
                    @Override
                    public void onResponse(Call<Home_timelinebean> call, Response<Home_timelinebean> response) {
                        final Home_timelinebean bean =response.body();
                        if (bean!=null)
                        {
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
                            ((SimpleItemAnimator)mWeiboRecycler.getItemAnimator()).setSupportsChangeAnimations(false);//解决页面闪烁
                            mWeiboRecycler.setAdapter(adapter);//添加适配器
                            mWeiboRecycler.setNestedScrollingEnabled(false);//禁止自身滚动
                            initRecycler(mWeiboRecycler,new LinearLayoutManager(getActivity()));
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
                        if (mPtrClassicFrameLayout.isRefreshing())
                        {
                            mPtrClassicFrameLayout.refreshComplete();
                        }
                    }

                    @Override
                    public void onFailure(Call<Home_timelinebean> call, Throwable t) {

                    }
                });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.fabOpen:
                if (fabShare.getVisibility()==View.GONE&&fabType.getVisibility()==View.GONE)
                {
                    fabOpen.setImageResource(R.mipmap.ic_off);
                    fabShare.setVisibility(View.VISIBLE);
                    fabType.setVisibility(View.VISIBLE);
                }
                else
                {
                    fabOpen.setImageResource(R.mipmap.ic_add);
                    fabShare.setVisibility(View.GONE);
                    fabType.setVisibility(View.GONE);
                }
                break;
            case R.id.fabType:
                Dialog();
                break;
            case R.id.fabShare:
                starActivity(ShareActivity.class);//发送微博
                break;
        }
    }

    private boolean isSameRow(int firstVisisbleItem){
        return mPreviousFirstVisibleItem == firstVisisbleItem;
    }

    private int getTopItemScrollY() {
        if (mWeiboRecycler == null || mWeiboRecycler.getChildAt(0) == null) return 0;
        View topChild = mWeiboRecycler.getChildAt(0);
        return topChild.getTop();
    }

    private void hideFAB() {//隐藏fab
        mHideFAB.start();
    }
    private void showFAB(){//显示fab
        mShowFAB.start();
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
                 topsoroll.smoothScrollTo(0,0);
                 dialog.dismiss();
             }
         });
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
