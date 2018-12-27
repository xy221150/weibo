package org.wxy.weibo.cosmos.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.wxy.weibo.cosmos.Activity;
import org.wxy.weibo.cosmos.R;
import org.wxy.weibo.cosmos.bean.ThemeBean;

import java.util.List;

public class ThemeAdapter extends RecyclerView.Adapter<ThemeAdapter.ThemeHolder> implements View.OnClickListener {
    private List<ThemeBean> beans;
    private OnClickListener onClickListener;
    public ThemeAdapter(List<ThemeBean> beans) {
        this.beans = beans;
    }

    @NonNull
    @Override
    public ThemeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(Activity.mainActivity()).inflate(R.layout.item_theme,parent,false);
        view.setOnClickListener(this);
        return new ThemeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ThemeHolder holder, int i) {
        holder.view.setBackgroundColor(beans.get(i).getColor());
        holder.text.setText(beans.get(i).getName());
        holder.text.setTextColor(beans.get(i).getColor());
        holder.itemView.setTag(i);
    }

    @Override
    public int getItemCount() {
        return beans.size();
    }

    @Override
    public void onClick(View v) {
        onClickListener.OnClickListener((Integer) v.getTag());
    }

    public interface OnClickListener{
        void OnClickListener(int i);
    }

    public void OnClickListener(OnClickListener onClickListener){
        this.onClickListener=onClickListener;
    }

    class ThemeHolder extends RecyclerView.ViewHolder{
        private View view;
        private TextView text;
        public ThemeHolder(View v) {
            super(v);
            view=v.findViewById(R.id.view);
            text=v.findViewById(R.id.text);
        }
    }
}
