package org.wxy.weibo.cosmos.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.wxy.weibo.cosmos.R;
import org.wxy.weibo.cosmos.view.CircleImageView;

public class ByToMeHolder extends RecyclerView.ViewHolder {
    public CircleImageView img;
    public TextView name;
    public TextView time;
    public TextView content;
    public ByToMeHolder(View v) {
        super(v);
        img=v.findViewById(R.id.img);
        name=v.findViewById(R.id.name);
        time=v.findViewById(R.id.time);
        content=v.findViewById(R.id.content);
    }
}
