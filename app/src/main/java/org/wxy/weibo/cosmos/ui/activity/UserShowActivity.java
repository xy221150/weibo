package org.wxy.weibo.cosmos.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.NormalListDialog;

import org.wxy.weibo.cosmos.R;
import org.wxy.weibo.cosmos.bean.Home_timelinebean;
import org.wxy.weibo.cosmos.bean.Userbean;
import org.wxy.weibo.cosmos.network.RetrofitHelper;
import org.wxy.weibo.cosmos.network.api.IStatuses;
import org.wxy.weibo.cosmos.network.api.IUser;
import org.wxy.weibo.cosmos.sharepreferences.User;
import org.wxy.weibo.cosmos.ui.base.WidgetActivity;
import org.wxy.weibo.cosmos.ui.adapter.MyWeiboRecyclerAdapter;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserShowActivity extends WidgetActivity implements VerticalScrollView.ScrollViewListener,View.OnClickListener{
    private TextView type;
    private VerticalScrollView mVerticalScrollView;
    private LinearLayout mToolbar;
    private CircleImageView mToolbar_Profile_image;
    private TextView mToolbar_Name;
    private LinearLayout friends;
    private LinearLayout followers;
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
    private String id;
    private String name;
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
    protected void initView() {
        super.initView();
        mToolbar=findViewById(R.id.my_toolbar);
        mToolbar_Profile_image=findViewById(R.id.my_toolbar_image);
        mToolbar_Name=findViewById(R.id.my_toolbar_name);
        mVerticalScrollView=findViewById(R.id.my_verticalscrollview);
        mProfile_image=findViewById(R.id.my_profile_image);
        mHead=findViewById(R.id.my_head);
        mName=findViewById(R.id.my_name);
        mFollowers_count=findViewById(R.id.my_followers_count);
        mFriends_count=findViewById(R.id.my_friends_count);
        myWeibolist=findViewById(R.id.my_weibo_list);
        type=findViewById(R.id.type);
        mPtrClassicFrameLayout=findViewById(R.id.my_ptrclassic);
        friends=findViewById(R.id.friends);
        followers=findViewById(R.id.followers);
        user= RetrofitHelper.create(IUser.class);
        statuses=RetrofitHelper.create(IStatuses.class);
        ((DefaultItemAnimator)myWeibolist.getItemAnimator()).setSupportsChangeAnimations(false);
        myWeibolist.getItemAnimator().setChangeDuration(0);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_usershow;
    }

    @Override
    protected void init() {
        super.init();
        adapter=new MyWeiboRecyclerAdapter(bean,UserShowActivity.this);
        myWeibolist.setAdapter(adapter);
        Intent intent=getIntent();
        id=intent.getStringExtra("id");
        name=intent.getStringExtra("name");
        Log.d("--------->", "init: "+id);
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
        if (!TextUtils.isEmpty(id))
        {
            statuses.getuser_timeline(User.user().getToken(),id,null,15,page,feature).
                    enqueue(new Callback<Home_timelinebean>() {
                        @Override
                        public void onResponse(Call<Home_timelinebean> call, Response<Home_timelinebean> response) {
                            bean=response.body();
                            if (bean!=null) {
                                myWeibolist.setVisibility(View.VISIBLE);
                                if (page == 1) {
                                    adapter = new MyWeiboRecyclerAdapter(bean,UserShowActivity.this);
                                }
                                else {
                                    adapter.add(bean);
                                }
                                if (bean.getStatuses().size() == 0)
                                {
                                    showToast("到底了");
                                }
                                myWeibolist.setAdapter(adapter);
                                myWeibolist.setHasFixedSize(true);
                                myWeibolist.setNestedScrollingEnabled(false);
                                initRecycler(myWeibolist,new LinearLayoutManager(UserShowActivity.this));
                                if (mPtrClassicFrameLayout.isRefreshing())
                                {
                                    mPtrClassicFrameLayout.refreshComplete();
                                }
                                adapter.onItemCilck(new MyWeiboRecyclerAdapter.onItemCilck() {
                                    @Override
                                    public void onItemCilck(int i) {
                                        new ShowActivity().Intent(UserShowActivity.this,adapter.getdata(i).getId());
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
                                        ImagePreview.getInstance().setContext(UserShowActivity.this).setImageInfoList(list).setIndex(j).start();
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
                                        ImagePreview.getInstance().setContext(UserShowActivity.this).setImageInfoList(list).setIndex(j).start();
                                    }
                                });
                            }
                        }

                        @Override
                        public void onFailure(Call<Home_timelinebean> call, Throwable t) {

                        }
                    });
        }
        else if (!TextUtils.isEmpty(name))
        {
            statuses.getuser_timeline(User.user().getToken(),null,name,15,page,feature).
                    enqueue(new Callback<Home_timelinebean>() {
                        @Override
                        public void onResponse(Call<Home_timelinebean> call, Response<Home_timelinebean> response) {
                            bean=response.body();
                            if (bean!=null) {
                                myWeibolist.setVisibility(View.VISIBLE);
                                if (page == 1) {
                                    adapter = new MyWeiboRecyclerAdapter(bean,UserShowActivity.this);
                                }
                                else {
                                    adapter.add(bean);
                                }
                                if (bean.getStatuses().size() == 0)
                                {
                                    showToast("到底了");
                                }
                                myWeibolist.setAdapter(adapter);
                                myWeibolist.setHasFixedSize(true);
                                myWeibolist.setNestedScrollingEnabled(false);
                                initRecycler(myWeibolist,new LinearLayoutManager(UserShowActivity.this));
                                if (mPtrClassicFrameLayout.isRefreshing())
                                {
                                    mPtrClassicFrameLayout.refreshComplete();
                                }
                                adapter.onItemCilck(new MyWeiboRecyclerAdapter.onItemCilck() {
                                    @Override
                                    public void onItemCilck(int i) {
                                        new ShowActivity().Intent(UserShowActivity.this,adapter.getdata(i).getId());
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
                                        ImagePreview.getInstance().setContext(UserShowActivity.this).setImageInfoList(list).setIndex(j).start();
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
                                        ImagePreview.getInstance().setContext(UserShowActivity.this).setImageInfoList(list).setIndex(j).start();
                                    }
                                });
                            }
                        }

                        @Override
                        public void onFailure(Call<Home_timelinebean> call, Throwable t) {

                        }
                    });
        }
    }
    private void User(){
         if (!TextUtils.isEmpty(id))
         {
             user.getuser(User.user().getToken(),id,null)
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
                                 GlideUtil.load(UserShowActivity.this,mProfile_image,userbean.getAvatar_large());
                                 GlideUtil.load(UserShowActivity.this,mToolbar_Profile_image,userbean.getAvatar_large());
                                 mName.setText(userbean.getScreen_name());
                                 mToolbar_Name.setText(userbean.getScreen_name());
                                 mFollowers_count.setText(userbean.getFollowers_count());
                                 mFriends_count.setText(userbean.getFriends_count());
                             }
                         }
                         @Override
                         public void onFailure(Call<Userbean> call, Throwable t) {}});
         }
         else if (!TextUtils.isEmpty(name))
         {
             user.getuser(User.user().getToken(),null,name+"")
                     .enqueue(new Callback<Userbean>() {
                         @Override
                         public void onResponse(Call<Userbean> call, Response<Userbean> response) {
                             Userbean userbean=response.body();
                             if (userbean==null)
                             {
                                 showToast("获取数据失败");
                             }
                             else
                             {
                                 User.user().setName(userbean.getScreen_name());
                                 GlideUtil.load(UserShowActivity.this,mProfile_image,userbean.getAvatar_large());
                                 GlideUtil.load(UserShowActivity.this,mToolbar_Profile_image,userbean.getAvatar_large());
                                 mName.setText(userbean.getScreen_name());
                                 mToolbar_Name.setText(userbean.getScreen_name());
                                 mFollowers_count.setText(userbean.getFollowers_count());
                                 mFriends_count.setText(userbean.getFriends_count());
                                 id=userbean.getId()+"";
                             }
                         }
                         @Override
                         public void onFailure(Call<Userbean> call, Throwable t) {}});
         }
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
    private void initListeners() {

        ViewTreeObserver vto = mHead.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mToolbar.getViewTreeObserver().removeGlobalOnLayoutListener(
                        this);
                height = mHead.getHeight();
                mVerticalScrollView.setScrollViewListener(UserShowActivity.this);
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
        dialog=new NormalListDialog(UserShowActivity.this,strings)
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
                Intent intent=new Intent(this,FFActivity.class);
                intent.putExtra("friendships","friend");
                intent.putExtra("id",id);
                startActivity(intent);
                break;
            case R.id.followers:
                Intent intent1=new Intent(this,FFActivity.class);
                intent1.putExtra("friendships","followers");
                intent1.putExtra("id",id);
                startActivity(intent1);
                break;

            case R.id.my_profile_image:
                if (userbean!=null)
                    new UserAcitivty().IntentUser(this,userbean);
                else
                    showToast("失败");
                break;
        }
    }

    public void getName(Context context, String name){
        Intent intent=new Intent(context,UserShowActivity.class);
        intent.putExtra("name",name);
        context.startActivity(intent);
    }
    public void getId(Context context, String id){
        Intent intent=new Intent(context,UserShowActivity.class);
        intent.putExtra("id",id);
        context.startActivity(intent);
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
