package org.wxy.weibo.cosmos.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.wxy.weibo.cosmos.Activity;
import org.wxy.weibo.cosmos.R;
import org.wxy.weibo.cosmos.Bean.Friendshipsbean;
import org.wxy.weibo.cosmos.utils.GlideUtil;
import org.wxy.weibo.cosmos.view.CircleImageView;

public class FriendshipsAdapter extends RecyclerView.Adapter<FriendshipsAdapter.FriendshipsHolder>{
    private Friendshipsbean bean;
    private OnItemClickListener mItemClickListener;
    public FriendshipsAdapter(Friendshipsbean bean){
        this.bean=bean;
    }
    @Override
    public FriendshipsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=View.inflate(Activity.mainActivity(),R.layout.item_friends,null);
        return new FriendshipsHolder(view);
    }

    @Override
    public void onBindViewHolder(FriendshipsHolder holder, final int position) {
        if (bean.getUsers().get(position).getAvatar_large()!=null)
            GlideUtil.load(Activity.mainActivity(),holder.avatar,bean.getUsers().get(position).getAvatar_large());

        holder.name.setText(bean.getUsers().get(position).getScreen_name());
        if (bean.getUsers().get(position).getDescription()!="")
            holder.status.setText(bean.getUsers().get(position).getDescription());
        else
            holder.status.setText("什么都没写");
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemClickListener.onItemClick(position);
            }
        });
    }


    @Override
    public int getItemCount() {
        return bean.getUsers().size();
    }


    class FriendshipsHolder extends RecyclerView.ViewHolder{
        private CircleImageView avatar;
        private TextView name;
        private TextView status;
        private LinearLayout item;
        public FriendshipsHolder(View itemView) {
            super(itemView);
            avatar=itemView.findViewById(R.id.avatar);
            name=itemView.findViewById(R.id.name);
            status=itemView.findViewById(R.id.status);
            item=itemView.findViewById(R.id.item);
        }
    }
    public interface OnItemClickListener{//点击事件
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

}
