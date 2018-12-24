package org.wxy.weibo.cosmos.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import org.wxy.weibo.cosmos.bean.ByMeBean;
import org.wxy.weibo.cosmos.Activity;
import org.wxy.weibo.cosmos.R;
import org.wxy.weibo.cosmos.utils.GlideUtil;
import org.wxy.weibo.cosmos.utils.TimeUtils;
import org.wxy.weibo.cosmos.utils.WeiboContentUtil;

public class ByMeAdapter extends RecyclerView.Adapter<ByToMeHolder> implements View.OnLongClickListener {
    private ByMeBean bean;
    private OnLongClick onLongClick;
    public ByMeAdapter(ByMeBean bean){
        this.bean=bean;
    }
    @NonNull
    @Override
    public ByToMeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=View.inflate(Activity.mainActivity(), R.layout.item_bytome,null);
        view.setOnLongClickListener(this);

        return new ByToMeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ByToMeHolder holder, int position) {
         holder.name.setText(bean.getComments().get(position).getUser().getName());
         holder.time.setText(TimeUtils.convDate(bean.getComments().get(position).getCreated_at()));
         holder.content.setText(WeiboContentUtil.Weibocontent(bean.getComments().get(position).getText(),holder.content));
         GlideUtil.load(Activity.mainActivity(),holder.img,bean.getComments().get(position).getUser().getAvatar_large());
         holder.itemView.setTag(position);
    }
    public void add(ByMeBean bean){
        this.bean.getComments().addAll(bean.getComments());
    }
    @Override
    public int getItemCount() {
        return bean.getComments().size();
    }

    @Override
    public boolean onLongClick(View v) {
        if (v!=null)
        {
            onLongClick.LongClick((Integer) v.getTag());
        }
        return false;
    }
    public interface OnLongClick{
        void LongClick(int i);
    }
    public void OnLongClick(OnLongClick onLongClick)
    {
       this.onLongClick=onLongClick;
    }
}
