package org.wxy.weibo.cosmos.ui.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.wxy.weibo.cosmos.R;
import org.wxy.weibo.cosmos.network.api.IComments;
import org.wxy.weibo.cosmos.sharepreferences.User;
import org.wxy.weibo.cosmos.ui.base.BaseFragment;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;

public class ToMeFragment extends BaseFragment {
    private PtrClassicFrameLayout ptr;
    private RecyclerView list;
    private int page=1;
    private IComments iComments;
    @Override
    protected int getLayoutID() {
        return R.layout.fragment_tome;
    }
    @Override
    protected void initView(View view) {
        super.initView(view);
        ptr=view.findViewById(R.id.ptr);
        list=view.findViewById(R.id.list);
    }

    @Override
    protected void init() {
        super.init();
        ToMe();
    }
    private void ToMe(){
        OkGo.<String>get("https://api.weibo.com/2/comments/to_me.json")
                .params("access_token",User.user().getToken())
                .params("page",page)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {

                    }
                });
    }
}
