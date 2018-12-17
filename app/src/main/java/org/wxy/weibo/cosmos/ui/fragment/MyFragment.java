package org.wxy.weibo.cosmos.ui.fragment;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;

import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.NormalListDialog;

import org.wxy.weibo.cosmos.R;
import org.wxy.weibo.cosmos.Bean.Home_timelinebean;
import org.wxy.weibo.cosmos.network.RetrofitHelper;
import org.wxy.weibo.cosmos.network.api.IStatuses;
import org.wxy.weibo.cosmos.sharepreferences.User;
import org.wxy.weibo.cosmos.ui.activity.ShareActivity;
import org.wxy.weibo.cosmos.ui.activity.ShowActivity;
import org.wxy.weibo.cosmos.ui.base.BaseFragment;
import org.wxy.weibo.cosmos.ui.adapter.MyWeiboRecyclerAdapter;
import org.wxy.weibo.cosmos.view.TopicScrollView;

import java.util.ArrayList;
import java.util.List;

import cc.shinichi.library.ImagePreview;
import cc.shinichi.library.bean.ImageInfo;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by wxy on 2018/7/4.
 */

public class MyFragment extends BaseFragment implements View.OnClickListener{
    private IStatuses statuses;
    private RecyclerView myWeibolist;
    private int feature=0;
    private int page=1;
    private Home_timelinebean bean;
    private MyWeiboRecyclerAdapter adapter;
    private PtrClassicFrameLayout mPtrClassicFrameLayout;
    private NormalListDialog dialog;
    private List<ImageInfo> list;
    private ImageInfo imageInfo;
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

        myWeibolist=view.findViewById(R.id.my_weibo_list);
        mPtrClassicFrameLayout=view.findViewById(R.id.my_ptrclassic);
        fabOpen=view.findViewById(R.id.fabOpen);
        fabShare=view.findViewById(R.id.fabShare);
        fabType=view.findViewById(R.id.fabType);
        topsoroll=view.findViewById(R.id.topsoroll);
        statuses=RetrofitHelper.create(IStatuses.class);
        ((DefaultItemAnimator)myWeibolist.getItemAnimator()).setSupportsChangeAnimations(false);
        myWeibolist.getItemAnimator().setChangeDuration(0);
    }

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
        myWeibolist.setAdapter(adapter);
        Fab();
        setPtrFrameAttribute();
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

    @SuppressLint("NewApi")
    private void Fab(){
        topsoroll.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(oldScrollY == 0) {//显示fab悬浮按钮
                    showFAB();
                    return;
                }
                //滚动过程中：ListView中最上面一个Item还是同一个Item
                if(isSameRow(scrollX)) {
                    int newScrollY = v.getScrollY();
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
                    if (scrollX > mPreviousFirstVisibleItem && FAB_VISIBLE == true) {  //向下滚动

                        FAB_VISIBLE = true;
                        showFAB();

                    } else if (scrollX < mPreviousFirstVisibleItem && FAB_VISIBLE == false) { //向上滚动
                        FAB_VISIBLE = false;
                        hideFAB();
                        fabType.setVisibility(View.GONE);
                        fabShare.setVisibility(View.GONE);
                        fabOpen.setImageResource(R.mipmap.ic_add);
                    }
                    mLastScrollY = getTopItemScrollY();
                    mPreviousFirstVisibleItem = scrollX;
                }
            }
        });
    }
    private void hideFAB() {//隐藏fab
        mHideFAB.start();
    }
    private void showFAB(){//显示fab
        mShowFAB.start();
    }
    private boolean isSameRow(int firstVisisbleItem){
        return mPreviousFirstVisibleItem == firstVisisbleItem;
    }
    private int getTopItemScrollY() {
        if (myWeibolist == null || myWeibolist.getChildAt(0) == null) return 0;
        View topChild = myWeibolist.getChildAt(0);
        return topChild.getTop();
    }
    public void setPtrFrameAttribute() {
        Ptr(mPtrClassicFrameLayout);
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
                dialog.dismiss();
                topsoroll.smoothScrollTo(0,0);
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
