package org.wxy.weibo.cosmos.ui.fragment.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import org.wxy.weibo.cosmos.Bean.ByMeBean;
import org.wxy.weibo.cosmos.Bean.ToMeBean;
import org.wxy.weibo.cosmos.MainActivity;
import org.wxy.weibo.cosmos.R;
import org.wxy.weibo.cosmos.utils.GlideUtil;
import org.wxy.weibo.cosmos.utils.TimeUtils;
import org.wxy.weibo.cosmos.utils.WeiboContentUtil;

public class ToMeAdapter extends RecyclerView.Adapter<ByToMeHolder> {
    private ToMeBean bean;
    public ToMeAdapter(ToMeBean bean){
        this.bean=bean;
    }
    @NonNull
    @Override
    public ByToMeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=View.inflate(MainActivity.mainActivity(), R.layout.item_bytome,null);
        return new ByToMeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ByToMeHolder holder, int position) {
          holder.content.setText(WeiboContentUtil.Weibocontent(bean.getComments().get(position).getText(),holder.content));
          holder.name.setText(bean.getComments().get(position).getUser().getName());
          holder.time.setText(TimeUtils.convDate(bean.getComments().get(position).getCreated_at()));
          GlideUtil.load(MainActivity.mainActivity(),holder.img,bean.getComments().get(position).getUser().getAvatar_large());

    }

    @Override
    public int getItemCount() {
        return bean.getComments().size();
    }

    public void add(ToMeBean bean){
        this.bean.getComments().addAll(bean.getComments());
    }
}