package org.wxy.weibo.cosmos.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.wxy.weibo.cosmos.Bean.CommentsShowBean;
import org.wxy.weibo.cosmos.Bean.ShowBean;
import org.wxy.weibo.cosmos.R;
import org.wxy.weibo.cosmos.network.RetrofitHelper;
import org.wxy.weibo.cosmos.network.api.IComments;
import org.wxy.weibo.cosmos.network.api.IStatuses;
import org.wxy.weibo.cosmos.sharepreferences.User;
import org.wxy.weibo.cosmos.ui.activity.adapter.CommentsAdapter;
import org.wxy.weibo.cosmos.ui.base.ActionbarActvity;
import org.wxy.weibo.cosmos.utils.GlideUtil;
import org.wxy.weibo.cosmos.utils.SourceUtlis;
import org.wxy.weibo.cosmos.utils.TimeUtils;
import org.wxy.weibo.cosmos.utils.WeiboContentUtil;
import org.wxy.weibo.cosmos.view.CircleImageView;

import java.util.ArrayList;
import java.util.List;

import cc.shinichi.library.ImagePreview;
import cc.shinichi.library.bean.ImageInfo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowActivity extends ActionbarActvity {
    private RelativeLayout weibo_list_Post;
    private TextView weibo_list_screen_name;
    private TextView weibo_list_created_at;
    private TextView weibo_list_source;
    private TextView weibo_list_text;
    private CircleImageView weibo_list_user_profile;
    private GridLayout weibo_list_pic;
    private TextView weibo_list_retweeted_status_user_name;
    private GridLayout weibo_list_retweeted_status_pic;
    private RelativeLayout weibo_list_retweeted_status;
    private RecyclerView Recyclerlist;
    private List<ImageInfo> list;
    private ImageInfo imageInfo;
    private long id;
    private ImageView send;
    private EditText text;
    private CommentsAdapter adapter;
    private LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(300,
            300);
    private int page=1;
    private MsgThread msgThread;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==0)
            {

                send(id);
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContTitle("详情");
    }

    @Override
    protected int getContLayoutID() {
        return R.layout.activity_show;
    }

    @Override
    protected void initView() {
        super.initView();
        weibo_list_Post=findViewById(R.id.weibo_list_Post);
        weibo_list_screen_name=findViewById(R.id.weibo_list_screen_name);
        weibo_list_created_at=findViewById(R.id.weibo_list_created_at);
        weibo_list_source=findViewById(R.id.weibo_list_source);
        weibo_list_text=findViewById(R.id.weibo_list_text);
        weibo_list_user_profile=findViewById(R.id.weibo_list_user_profile);
        weibo_list_pic=findViewById(R.id.weibo_list_pic);
        weibo_list_retweeted_status_user_name=findViewById(R.id.weibo_list_retweeted_status_user_name);
        weibo_list_retweeted_status_pic=findViewById(R.id.weibo_list_retweeted_status_pic);
        weibo_list_retweeted_status=findViewById(R.id.weibo_list_retweeted_status);
        Recyclerlist=findViewById(R.id.list);
        text=findViewById(R.id.text);
        send=findViewById(R.id.send);
    }

    @Override
    protected void init() {
        super.init();
        initRecycler(Recyclerlist,new LinearLayoutManager(this));
        Intent intent=getIntent();
        id=intent.getLongExtra("id",0);
        data(id);
        commentsshow(id);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msgThread=new MsgThread(0);
                msgThread.run();
            }
        });
    }

    @Override
    protected void initdata() {
        super.initdata();
    }
    private void data(long id){
        IStatuses iStatuses=RetrofitHelper.create(IStatuses.class);
        iStatuses.show(User.user().getToken(),id)
                .enqueue(new Callback<ShowBean>() {
                    @Override
                    public void onResponse(Call<ShowBean> call, Response<ShowBean> response) {
                          if (response.body()!=null) {
                              final ShowBean bean = response.body();
                              GlideUtil.load(ShowActivity.this, weibo_list_user_profile, response.body().getUser().getAvatar_large());
                              weibo_list_screen_name.setText(bean.getUser().getScreen_name());
                              weibo_list_created_at.setText(TimeUtils.convDate(bean.getCreated_at()));
                              weibo_list_source.setText("来自:" + new SourceUtlis().SourceUtlis(bean.getSource()));
                              weibo_list_text.setText(WeiboContentUtil.Weibocontent(bean.getText(), weibo_list_text));
                              if (bean.getPic_urls().size() == 0) {
                                  weibo_list_pic.setVisibility(View.GONE);
                              } else {
                                  for (int j = 0; j < bean.getPic_urls().size(); j++) {
                                      final ImageView pic = new ImageView(ShowActivity.this);

                                      pic.setLayoutParams(params);
                                      pic.setScaleType(ImageView.ScaleType.FIT_XY);//使图片充满控件大小
                                      GlideUtil.loadUrl(ShowActivity.this, pic, bean.getPic_urls().get(j).getThumbnail_pic());
                                      weibo_list_pic.addView(pic);
                                      final int finalJ = j;
                                      pic.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               list=new ArrayList<>();
                                               for (int n=0;n<bean.getPic_urls().size();n++)
                                               {
                                                   imageInfo=new ImageInfo();
                                                   imageInfo.setThumbnailUrl("http://wx1.sinaimg.cn/large/"+bean.getPic_urls().get(n).getThumbnail_pic().substring(32));
                                                   imageInfo.setOriginUrl("http://wx1.sinaimg.cn/large/"+bean.getPic_urls().get(n).getThumbnail_pic().substring(32));
                                                   list.add(n,imageInfo);
                                               }
                                               ImagePreview.getInstance().setContext(ShowActivity.this).setImageInfoList(list).setIndex(finalJ).start();
                                           }
                                       });
                                  }

                              }
                              if (null != bean.getRetweeted_status()) {
                                  weibo_list_retweeted_status.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          new ShowActivity().Intent(ShowActivity.this,bean.getRetweeted_status().getId());
                                      }
                                  });
                                  weibo_list_retweeted_status_user_name.setText(WeiboContentUtil.
                                              Weibocontent("@" + bean.getRetweeted_status().getUser().getName()
                                                  + ":"
                                                  + bean.getRetweeted_status().getText(),
                                      weibo_list_retweeted_status_user_name));
                                  if (bean.getRetweeted_status().getPic_urls().size() == 0) { weibo_list_retweeted_status_pic.setVisibility(View.GONE);
                                  } else {
                                      for (int j = 0; j < bean.getRetweeted_status().getPic_urls().size(); j++) {
                                          final ImageView pic1 = new ImageView(ShowActivity.this);
                                          pic1.setLayoutParams(params);
                                          pic1.setScaleType(ImageView.ScaleType.FIT_XY);//使图片充满控件大小
                                          GlideUtil.loadUrl(ShowActivity.this, pic1,
                                                  bean.getRetweeted_status().getPic_urls().get(j).getThumbnail_pic());
                                          weibo_list_retweeted_status_pic.addView(pic1);
                                          final int finalJ = j;
                                          pic1.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View v) {
                                                  list=new ArrayList<>();
                                                  for (int n=0;n<bean.getRetweeted_status().getPic_urls().size();n++)
                                                  {
                                                      imageInfo=new ImageInfo();
                                                      imageInfo.setThumbnailUrl("http://wx1.sinaimg.cn/large/"+bean.getRetweeted_status().getPic_urls().get(n).getThumbnail_pic().substring(32));
                                                      imageInfo.setOriginUrl("http://wx1.sinaimg.cn/large/"+bean.getRetweeted_status().getPic_urls().get(n).getThumbnail_pic().substring(32));
                                                      list.add(n,imageInfo);
                                                  }
                                                  ImagePreview.getInstance().setContext(ShowActivity.this).setImageInfoList(list).setIndex(finalJ).start();
                                              }
                                          });
                                      }
                                  }
                              }
                              else
                                  {

                                  weibo_list_retweeted_status.setVisibility(View.GONE);

                                  }
                              weibo_list_Post.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View view) {
                                          new UserShowActivity().getId(ShowActivity.this, bean.getUser().getId() + "");
                                      }
                                  });
                              } else {
                                  showToast("访问错误");
                                  finish();
                              }
                          }

                    @Override
                    public void onFailure(Call<ShowBean> call, Throwable t) {

                    }
                });
    }

    private void commentsshow(long id){
        IComments iComments=RetrofitHelper.create(IComments.class);
        iComments.show(User.user().getToken(),
                id,page).enqueue(new Callback<CommentsShowBean>() {
            @Override
            public void onResponse(Call<CommentsShowBean> call, Response<CommentsShowBean> response) {
                     adapter=new CommentsAdapter(response.body());
                     Recyclerlist.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<CommentsShowBean> call, Throwable t) {

            }
        });
    }

    private void send(final long id){
       if (!text.getText().equals("")&&text.getText().toString()!=""||text.getText().toString().length()>140)
       {
           OkGo.<String>post("https://api.weibo.com/2/comments/create.json")
                   .params("access_token",User.user().getToken())
                   .params("comment",text.getText().toString())
                   .params("id",id)
                   .execute(new StringCallback() {
                       @Override
                       public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                                  if (response.code()==200)
                                  {
                                      showToast("发送成功");
                                      text.setText("");
                                      commentsshow(id);
                                  }
                                  else
                                  { showToast("发送失败"); }
                       }
                   });
       }
       else
       {
           showToast("发送失败");
       }

    }
    public void Intent(Context context, long id){
        Intent intent=new Intent(context,ShowActivity.class);
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
            message.what=method;
            handler.sendMessage(message);
        }
    }
}
