package org.wxy.weibo.cosmos.ui.activity.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.wxy.weibo.cosmos.Bean.CommentsShowBean;
import org.wxy.weibo.cosmos.MainActivity;
import org.wxy.weibo.cosmos.R;
import org.wxy.weibo.cosmos.ui.activity.UserShowActivity;
import org.wxy.weibo.cosmos.utils.GlideUtil;
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
        View v=View.inflate(MainActivity.mainActivity(),R.layout.item_comments,null);
        return new Commentsholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Commentsholder holder, final int position) {
        holder.text.setText(WeiboContentUtil.Weibocontent(bean.getComments().get(position).getText(),holder.text));
        holder.name.setText(bean.getComments().get(position).getUser().getName());
        GlideUtil.load(MainActivity.mainActivity(),holder.img,bean.getComments().get(position).getUser().getAvatar_large());
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               new UserShowActivity().getId(MainActivity.mainActivity(),bean.getComments().get(position).getUser().getId()+"");
            }
        });
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new UserShowActivity().getId(MainActivity.mainActivity(),bean.getComments().get(position).getUser().getId()+"");
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
        private CircleImageView img;
        public Commentsholder(View v) {
            super(v);
            name=v.findViewById(R.id.name);
            text=v.findViewById(R.id.text);
            img=v.findViewById(R.id.img);
        }
    }
}
