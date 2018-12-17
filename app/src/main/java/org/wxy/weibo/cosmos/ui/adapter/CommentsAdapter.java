package org.wxy.weibo.cosmos.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.wxy.weibo.cosmos.Bean.CommentsShowBean;
import org.wxy.weibo.cosmos.Activity;
import org.wxy.weibo.cosmos.R;
import org.wxy.weibo.cosmos.ui.activity.UserShowActivity;
import org.wxy.weibo.cosmos.utils.GlideUtil;
import org.wxy.weibo.cosmos.utils.TimeUtils;
import org.wxy.weibo.cosmos.utils.WeiboContentUtil;
import org.wxy.weibo.cosmos.view.CircleImageView;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.Commentsholder> {
     private CommentsShowBean bean;
     public CommentsAdapter(CommentsShowBean bean){
         this.bean=bean;
     }
    @NonNull
    @Override
    public Commentsholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v=View.inflate(Activity.mainActivity(),R.layout.item_comments,null);
        return new Commentsholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Commentsholder holder, final int position) {
        holder.text.setText(WeiboContentUtil.Weibocontent(bean.getComments().get(position).getText(),holder.text));
        holder.name.setText(bean.getComments().get(position).getUser().getName());
        GlideUtil.load(Activity.mainActivity(),holder.img,bean.getComments().get(position).getUser().getAvatar_large());
        holder.time.setText(TimeUtils.convDate(bean.getComments().get(position).getCreated_at()));
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               new UserShowActivity().getId(Activity.mainActivity(),bean.getComments().get(position).getUser().getId()+"");
            }
        });
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new UserShowActivity().getId(Activity.mainActivity(),bean.getComments().get(position).getUser().getId()+"");
            }
        });
     }

    @Override
    public int getItemCount() {
        return bean.getComments().size();
    }

    class Commentsholder extends RecyclerView.ViewHolder{

        private TextView name;
        private TextView text;
        private TextView time;
        private CircleImageView img;
        public Commentsholder(View v) {
            super(v);
            time=v.findViewById(R.id.time);
            name=v.findViewById(R.id.name);
            text=v.findViewById(R.id.text);
            img=v.findViewById(R.id.img);
        }
    }
}
