package org.wxy.weibo.cosmos.ui.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.wxy.weibo.cosmos.Bean.MentionswbBean;
import org.wxy.weibo.cosmos.R;
import org.wxy.weibo.cosmos.network.RetrofitHelper;
import org.wxy.weibo.cosmos.network.api.IStatuses;
import org.wxy.weibo.cosmos.sharepreferences.User;
import org.wxy.weibo.cosmos.ui.activity.ShowActivity;
import org.wxy.weibo.cosmos.ui.base.BaseFragment;
import org.wxy.weibo.cosmos.ui.fragment.adapter.MentionsAdapter;
import org.wxy.weibo.cosmos.ui.fragment.adapter.MentionswbAdapter;

import java.util.ArrayList;
import java.util.List;

import cc.shinichi.library.ImagePreview;
import cc.shinichi.library.bean.ImageInfo;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import retrofit2.Call;
import retrofit2.Callback;

public class MentionswbFragment extends BaseFragment {
    private PtrClassicFrameLayout ptr;
    private RecyclerView list;
    private MentionswbAdapter adapter;
    private IStatuses iStatuses;
    private int page=1;
    private List<ImageInfo> imgs;
    private ImageInfo imageInfo;
    @Override
    protected int getLayoutID() {
        return R.layout.fragment_mentionswb;
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
        iStatuses=RetrofitHelper.create(IStatuses.class);
        iStatuses.mentions(User.user().getToken(),1)
                .enqueue(new Callback<MentionswbBean>() {
                    @Override
                    public void onResponse(Call<MentionswbBean> call, retrofit2.Response<MentionswbBean> response) {
                        MentionswbBean bean=response.body();
                        if (response.code()==200&&bean.getStatuses()!=null&&bean.getStatuses().size()>0)
                        {
                            if (page==1)
                            {
                                adapter=new MentionswbAdapter(bean);
                                adapter.setHasStableIds(true);//解决数据错乱
                            }
                            else if (page>1)
                            {
                                adapter.add(bean);
                                adapter.notifyItemChanged(adapter.getItemCount());
                            }
                            list.setAdapter(adapter);
                            initRecycler(list,new LinearLayoutManager(getActivity()));
                            adapter.onItemRetweetedPicCilck(new MentionswbAdapter.onItemRetweetedPicCilck() {
                                @Override
                                public void onItemRetweetedPicCilck(int i, int j) {
                                    imgs=new ArrayList<>();
                                    for (int n=0;n<adapter.getdata(i).getRetweeted_status().getPic_ids().size();n++)
                                    {
                                        imageInfo=new ImageInfo();
                                        imageInfo.setThumbnailUrl("http://wx1.sinaimg.cn/large/"+adapter.getdata(i).getRetweeted_status().getPic_ids().get(n));
                                        imageInfo.setOriginUrl("http://wx1.sinaimg.cn/large/"+adapter.getdata(i).getRetweeted_status().getPic_ids().get(n));
                                        imgs.add(imageInfo);
                                    }
                                    ImagePreview.getInstance().setContext(getActivity()).setImageInfoList(imgs).setIndex(j).start();
                                }
                            });
                            adapter.onItemCilck(new MentionswbAdapter.onItemCilck() {
                                @Override
                                public void onItemCilck(int i) {
                                    new ShowActivity().Intent(getActivity(),adapter.getdata(i).getId());
                                }
                            });
                            adapter.onItemStatusCilck(new MentionswbAdapter.onItemStatusCilck() {
                                @Override
                                public void onItemStatusCilck(int i) {
                                    new ShowActivity().Intent(getActivity(),adapter.getdata(i).getRetweeted_status().getId());
                                }
                            });
                        }
                        else
                        {
                            showToast("下面没了");
                        }
                        if (ptr.isRefreshing())
                        {
                            ptr.refreshComplete();
                        }
                    }

                    @Override
                    public void onFailure(Call<MentionswbBean> call, Throwable t) {

                    }
                });
    }
}
