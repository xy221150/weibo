package org.wxy.weibo.cosmos.ui.fragment.adapter;

import android.support.v7.widget.GridLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.wxy.weibo.cosmos.Bean.Home_timelinebean;
import org.wxy.weibo.cosmos.Bean.MentionsBean;
import org.wxy.weibo.cosmos.Bean.MentionswbBean;
import org.wxy.weibo.cosmos.Bean.basebean.Statusesbean;
import org.wxy.weibo.cosmos.MainActivity;
import org.wxy.weibo.cosmos.R;
import org.wxy.weibo.cosmos.ui.activity.UserShowActivity;
import org.wxy.weibo.cosmos.utils.DoubleUtil;
import org.wxy.weibo.cosmos.utils.GlideUtil;
import org.wxy.weibo.cosmos.utils.SourceUtlis;
import org.wxy.weibo.cosmos.utils.TimeUtils;
import org.wxy.weibo.cosmos.utils.WeiboContentUtil;
import org.wxy.weibo.cosmos.view.CircleImageView;


/**
 * Created by wxy on 2018/7/6.
 */

public class MentionswbAdapter extends RecyclerView.Adapter<MentionswbAdapter.MyWeiboHolder>implements View.OnClickListener{
    private MentionswbBean bean;
    private MyWeiboHolder holder;
    private View view;
    private LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(300,
            300);
    private onItemCilck onItemCilck;//Itme整体点击
    private onItemStatusCilck onItemStatusCilck;//Item转发内容点击
    private onItemRetweetedPicCilck onItemRetweetedPicCilck;//原微博图片点击
    public MentionswbAdapter(MentionswbBean bean){
        this.bean=bean;
    }
    @Override
    public MyWeiboHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view= LayoutInflater.from(MainActivity.mainActivity()).inflate(R.layout.item_mentionswb,parent,false);
        holder=new MyWeiboHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyWeiboHolder holder, final int i) {
        holder.weibo_list_screen_name.setText(bean.getStatuses().get(i).getUser().getScreen_name());
        holder.weibo_list_created_at.setText(TimeUtils.convDate(bean.getStatuses().get(i).getCreated_at()));
        holder.weibo_list_source.setText("来自:" + new SourceUtlis().SourceUtlis(bean.getStatuses().get(i).getSource()));
        GlideUtil.load(MainActivity.mainActivity(), holder.weibo_list_user_profile, bean.getStatuses().get(i).getUser().getAvatar_large());
        holder.weibo_list_text.setText(WeiboContentUtil.Weibocontent(bean.getStatuses().get(i).getText(), holder.weibo_list_text));
        holder.attitudes_count.setText(DoubleUtil.count(bean.getStatuses().get(i).getAttitudes_count()));
        holder.comments_count.setText(DoubleUtil.count(bean.getStatuses().get(i).getComments_count()));
        holder.reposts_count.setText(DoubleUtil.count(bean.getStatuses().get(i).getReposts_count()));
        if (null!=bean.getStatuses().get(i).getRetweeted_status()) {
            holder.weibo_list_retweeted_status_user_name.setText(WeiboContentUtil.Weibocontent(
                    "@"+bean.getStatuses().get(i).getRetweeted_status().getUser().getName()
                            +":"
                            +bean.getStatuses().get(i).getRetweeted_status().getText(),
                    holder.weibo_list_retweeted_status_user_name));
            if (bean.getStatuses().get(i).getRetweeted_status().getPic_ids().size()==0)
            {
                holder.weibo_list_retweeted_status_pic.setVisibility(View.GONE);
            }
            else
            {
                for (int j=0;j<bean.getStatuses().get(i).getRetweeted_status().getPic_ids().size();j++)
                {
                    final ImageView pic1=new ImageView(MainActivity.mainActivity());
                    pic1.setLayoutParams(params);
                    pic1.setScaleType(ImageView.ScaleType.FIT_XY);//使图片充满控件大小
                    GlideUtil.loadUrl(MainActivity.mainActivity(),pic1,"http://wx3.sinaimg.cn/bmiddle/"+bean.getStatuses().get(i).getRetweeted_status().getPic_ids().get(j));
                    holder.weibo_list_retweeted_status_pic.addView(pic1);
                    final int finalJ = j;
                    pic1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onItemRetweetedPicCilck.onItemRetweetedPicCilck(i, finalJ);
                        }
                    });
                }
            }
        } else
        {
            holder.weibo_list_retweeted_status.setVisibility(View.GONE);

        }
        holder.weibo_list_Post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new UserShowActivity().getId(MainActivity.mainActivity(),bean.getStatuses().get(i).getUser().getId()+"");
            }
        });
        holder.weibo_list_retweeted_status.setOnClickListener(this);
        holder.weibo_list_retweeted_status.setTag(i);
        holder.weibo.setOnClickListener(this);
        holder.weibo.setTag(i);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return bean.getStatuses().size();
    }

    public void add(MentionswbBean bean){
         this.bean.getStatuses().addAll(bean.getStatuses());
    }
    public MentionswbBean.StatusesBean getdata(int i){
        return bean.getStatuses().get(i);
    }
    @Override
    public void onClick(View v) {
       switch (v.getId())
       {
           case R.id.weibo:
               onItemCilck.onItemCilck((Integer) v.getTag());
               break;
           case R.id.weibo_list_retweeted_status:
               onItemStatusCilck.onItemStatusCilck((Integer) v.getTag());
               break;
       }
    }


    public class MyWeiboHolder extends RecyclerView.ViewHolder{
        private RelativeLayout weibo;
        private RelativeLayout weibo_list_Post;
        private TextView weibo_list_screen_name;
        private TextView weibo_list_created_at;
        private TextView weibo_list_source;
        private TextView weibo_list_text;
        private CircleImageView weibo_list_user_profile;
        private TextView weibo_list_retweeted_status_user_name;
        private GridLayout weibo_list_retweeted_status_pic;
        private RelativeLayout weibo_list_retweeted_status;
        private TextView reposts_count;
        private TextView comments_count;
        private TextView attitudes_count;
        public MyWeiboHolder(View itemView) {
            super(itemView);
            weibo_list_Post=itemView.findViewById(R.id.weibo_list_Post);
            weibo_list_screen_name=itemView.findViewById(R.id.weibo_list_screen_name);
            weibo_list_created_at=itemView.findViewById(R.id.weibo_list_created_at);
            weibo_list_source=itemView.findViewById(R.id.weibo_list_source);
            weibo_list_text=itemView.findViewById(R.id.weibo_list_text);
            weibo_list_user_profile=itemView.findViewById(R.id.weibo_list_user_profile);
            weibo_list_retweeted_status_user_name=itemView.findViewById(R.id.weibo_list_retweeted_status_user_name);
            weibo_list_retweeted_status_pic=itemView.findViewById(R.id.weibo_list_retweeted_status_pic);
            weibo_list_retweeted_status=itemView.findViewById(R.id.weibo_list_retweeted_status);
            attitudes_count=itemView.findViewById(R.id.attitudes_count);
            comments_count=itemView.findViewById(R.id.comments_count);
            reposts_count=itemView.findViewById(R.id.reposts_count);
            weibo=itemView.findViewById(R.id.weibo);
        }
    }

    public interface onItemCilck{
        void onItemCilck(int i);
    }
    public void onItemCilck(onItemCilck onItemCilck){
        this.onItemCilck=onItemCilck;
    }

    public interface onItemStatusCilck{
        void onItemStatusCilck(int i);
    }
    public void onItemStatusCilck(MentionswbAdapter.onItemStatusCilck onItemStatusCilck){
        this.onItemStatusCilck=onItemStatusCilck;
    }
    public interface onItemRetweetedPicCilck{
        void onItemRetweetedPicCilck(int i, int j);
    }
    public void onItemRetweetedPicCilck(MentionswbAdapter.onItemRetweetedPicCilck onItemRetweetedPicCilck){
        this.onItemRetweetedPicCilck=onItemRetweetedPicCilck;
    }
}
