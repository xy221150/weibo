package org.wxy.weibo.cosmos.ui.activity.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.wxy.weibo.cosmos.Bean.AccountnumberBean;
import org.wxy.weibo.cosmos.Activity;
import org.wxy.weibo.cosmos.R;
import org.wxy.weibo.cosmos.utils.GlideUtil;
import org.wxy.weibo.cosmos.view.CircleImageView;

import java.util.List;

public class AccountnumberAdapter extends RecyclerView.Adapter<AccountnumberAdapter.AccountnumberHolder> implements View.OnClickListener {
    private List<AccountnumberBean> bean;
    public AccountnumberAdapter(List<AccountnumberBean> bean){
       this.bean=bean;
    }
    private OnClickListener onClickListener;
    @NonNull
    @Override
    public AccountnumberHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=View.inflate(Activity.mainActivity(),R.layout.item_accoutnumber,null);
        return new AccountnumberHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountnumberHolder holder, int position) {
        holder.text.setText(bean.get(position).getName());
        GlideUtil.load(Activity.mainActivity(),holder.img,bean.get(position).getUrl());
        holder.linear.setOnClickListener(this);
        holder.linear.setTag(position);
    }

    @Override
    public int getItemCount() {
        return bean.size();
    }

    @Override
    public void onClick(View v) {
       if (v!=null)
       {
           onClickListener.ClickListener((Integer) v.getTag());
       }
    }

    class AccountnumberHolder extends RecyclerView.ViewHolder{
        private TextView text;
        private CircleImageView img;
        private LinearLayout linear;
        public AccountnumberHolder(View v) {
            super(v);
            linear=v.findViewById(R.id.linear);
            text=v.findViewById(R.id.name);
            img=v.findViewById(R.id.img);
        }
    }
   public interface OnClickListener{
        void ClickListener(int i);
   }
   public void OnClickListener(OnClickListener onClickListener){
       this.onClickListener=onClickListener;
   }
}
