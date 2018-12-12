package org.wxy.weibo.cosmos.ui.fragment;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;


import org.wxy.weibo.cosmos.Bean.ByMeBean;
import org.wxy.weibo.cosmos.R;
import org.wxy.weibo.cosmos.network.RetrofitHelper;
import org.wxy.weibo.cosmos.network.api.IComments;
import org.wxy.weibo.cosmos.sharepreferences.User;
import org.wxy.weibo.cosmos.ui.base.BaseFragment;
import org.wxy.weibo.cosmos.ui.fragment.adapter.ByMeAdapter;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.header.MaterialHeader;
import in.srain.cube.views.ptr.util.PtrLocalDisplay;
import retrofit2.Call;
import retrofit2.Response;

public class ByMeFragment extends BaseFragment {
    private PtrClassicFrameLayout ptr;
    private RecyclerView list;
    private int page=1;
    private IComments iComments;
    private ByMeAdapter adapter;
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
                    ByMe(page);
                    // 用于关闭下拉刷新
                    ptr.refreshComplete();
                    break;
                case LOADMORE:
                    page=page+1;
                    ByMe(page);
                    break;
                case AUTO:
                    ptr.autoRefresh();
                    ByMe(page);
                    break;
                default:
            }
        }
    };
    @Override
    protected int getLayoutID() {
        return R.layout.fragment_byme;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        ptr=view.findViewById(R.id.ptr);
        list=view.findViewById(R.id.list);
    }

    @Override
    protected void init() {
        super.init();
        setPtrFrameAttribute();
    }
    private void setPtrFrameAttribute() {
        Ptr(ptr);
        ptr.post(new Runnable() {
            @Override
            public void run() {
                // 进入界面自动刷新
                msgThread=new MsgThread(AUTO);
                msgThread.run();
            }
        });
        ptr.setPtrHandler(new PtrDefaultHandler2() {
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
    private void ByMe(final int page){
       iComments=RetrofitHelper.create(IComments.class);
       iComments.byme(User.user().getToken(),page)
               .enqueue(new retrofit2.Callback<ByMeBean>() {
                   @Override
                   public void onResponse(Call<ByMeBean> call, Response<ByMeBean> response) {
                       if (response.code()==200&&response.body().getComments()!=null&&response.body().getComments().size()>0)
                       {
                           if (page==1)
                           {
                               adapter=new ByMeAdapter(response.body());
                               adapter.setHasStableIds(true);//解决数据错乱
                           }
                           else if (page>1)
                           {
                               adapter.add(response.body());
                               adapter.notifyItemChanged(adapter.getItemCount());
                           }
                           list.setAdapter(adapter);
                           initRecycler(list,new LinearLayoutManager(getActivity()));
                           adapter.OnLongClick(new ByMeAdapter.OnLongClick() {
                               @Override
                               public void LongClick(int i) {
                                   showToast(i+"");
                               }
                           });
                       }
                       else
                       {
                           showToast("下面没了");
                       }
                       if (ptr.isRefreshing())
                       {
                           ptr.refreshComplete();
                       }
                   }

                   @Override
                   public void onFailure(Call<ByMeBean> call, Throwable t) {
                        showToast(t.getMessage());
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
