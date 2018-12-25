package org.wxy.weibo.cosmos.ui.activity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.github.javiersantos.bottomdialogs.BottomDialog;

import org.wxy.weibo.cosmos.Activity;
import org.wxy.weibo.cosmos.bean.Userbean;
import org.wxy.weibo.cosmos.R;
import org.wxy.weibo.cosmos.network.RetrofitHelper;
import org.wxy.weibo.cosmos.network.api.IUser;
import org.wxy.weibo.cosmos.sharepreferences.User;
import org.wxy.weibo.cosmos.ui.base.BaseFragment;
import org.wxy.weibo.cosmos.ui.base.WidgetActivity;
import org.wxy.weibo.cosmos.ui.fragment.FFFragment;
import org.wxy.weibo.cosmos.ui.fragment.IndexFragment;
import org.wxy.weibo.cosmos.ui.fragment.MyFragment;
import org.wxy.weibo.cosmos.ui.fragment.NoticeFragment;
import org.wxy.weibo.cosmos.utils.GlideUtil;
import org.wxy.weibo.cosmos.view.CircleImageView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends WidgetActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private BottomDialog.Builder builder;
    private CircleImageView imageView;
    private TextView name;
    private IndexFragment indexfragment;
    private MyFragment myFragment;
    private NoticeFragment noticeFragment;
    private FFFragment ffFragment;
    private BaseFragment basefragment;
    private FrameLayout frameLayout;
    private Userbean userbean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // 透明状态栏
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (User.user().getToken()==null||User.user().getToken().equals(""))
            Login();
    }

    @Override
    protected void initView() {
        super.initView();
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("最新微博");
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        drawer = findViewById(R.id.drawer_layout);
        View view=navigationView.getHeaderView(0);
        imageView=view.findViewById(R.id.imageView);
        name=view.findViewById(R.id.name);
        frameLayout=findViewById(R.id.fl_main);
    }

    @Override
    protected void init() {
        super.init();
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        builder = new BottomDialog.Builder(this);
        indexfragment=new IndexFragment();//实例化fragment
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fl_main, indexfragment)
                .commit();
        basefragment=indexfragment;
    }

    @Override
    protected void initdata() {
        super.initdata();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    starActivity(AccountnumberActivity.class);
            }
        });
        User();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {//左侧栏是开启状态，则关闭
            drawer.closeDrawer(GravityCompat.START);
        } else if(basefragment!=indexfragment){//不是主页fragment则返回主页fragment
            toolbar.setTitle("最新微博");
            switchFragment(indexfragment);
            basefragment=indexfragment;
        }else {
            showExit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {//搜索栏
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setIconified(true);
        searchView.setQueryHint("暂不支持");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {//回车搜索
                showToast("暂不支持");
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {//左侧栏item点击事件
        int id = item.getItemId();

        if (id == R.id.nav_index) {
            toolbar.setTitle("最新微博");
            switchFragment(indexfragment);
            basefragment=indexfragment;
        } else if (id == R.id.nav_notice) {
            noticeFragment=new NoticeFragment();
            toolbar.setTitle("通知");
           switchFragment(noticeFragment);
            basefragment=noticeFragment;
        } else if (id == R.id.nav_my) {
            myFragment=new MyFragment();
            toolbar.setTitle("我的微博");
            switchFragment(myFragment);
            basefragment=myFragment;
        } else if (id == R.id.nav_friend) {
            ffFragment=new FFFragment(User.user().getUid(),"friend");
            toolbar.setTitle("我的关注");
            switchFragment(ffFragment);
            basefragment=ffFragment;
        }else if (id == R.id.nav_followers) {
            ffFragment=new FFFragment(User.user().getUid(),"followers");
            toolbar.setTitle("我的粉丝");
            switchFragment(ffFragment);
            basefragment=ffFragment;
        } else if (id == R.id.nav_setting) {
              starActivity(SettingActivity.class);
        } else if (id == R.id.nav_user) {
             if (userbean!=null)
                 new UserAcitivty().IntentUser(this,userbean);
        } else if (id == R.id.nav_end) {
            showExit();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void switchFragment(BaseFragment to) {//切换界面
        if (basefragment != null) {

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if (to.isAdded()) {//如果to==true隐藏当前的fragment，显示下一个,否则隐藏当前的fragment，添加下一个到Activity中;
                transaction.hide(basefragment).show(to).commit();
            } else {
                transaction.hide(basefragment).add(R.id.fl_main, to).commit();
            }

        }
    }

    private void showExit() {//退出提示
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示")
                .setMessage("您确定要退出吗？")
                .setCancelable(true)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Activity.getStack().finishAllActivity();
                    }
                })
                .setNegativeButton("取消",null)
                .create()
                .setCanceledOnTouchOutside(false);
        builder.show();
    }

    public void Login()//登录提示
    {
        final BottomDialog.Builder dialog=new BottomDialog.Builder(this);
        dialog.setTitle("欢迎")
                .setContent("授权后显示内容")
                .setPositiveText("授权")
                .setNegativeText("取消")
                .setNegativeTextColor(R.color.white)
                .setNegativeTextColorResource(R.color.colorPrimary)
                .setPositiveBackgroundColorResource(R.color.colorPrimary)
                .setPositiveTextColorResource(android.R.color.white)
                .onPositive(new BottomDialog.ButtonCallback() {
                    @Override
                    public void onClick(BottomDialog bottomDialog) {
                        starActivity(AccountnumberActivity.class);
                    }
                })
                .onNegative(new BottomDialog.ButtonCallback() {
                    @Override
                    public void onClick( BottomDialog bottomDialog) {
                        bottomDialog.dismiss();
                    }
                })
                .show();
    }

    private void User(){
        if (User.user().getToken()!=null&&!User.user().getToken().equals(""))
        {
            IUser iUser=RetrofitHelper.create(IUser.class);
            iUser.getuser(User.user().getToken(),User.user().getUid(),null)
                    .enqueue(new Callback<Userbean>() {
                        @Override
                        public void onResponse(Call<Userbean> call, Response<Userbean> response) {
                            userbean=response.body();
                            if (userbean!=null)
                            {
                                GlideUtil.load(MainActivity.this,imageView,userbean.getAvatar_large());
                                name.setText(userbean.getName());
                            }
                        }

                        @Override
                        public void onFailure(Call<Userbean> call, Throwable t) {

                        }
                    });
        }
    }
}
