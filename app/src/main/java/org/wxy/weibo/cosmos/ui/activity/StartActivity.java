package org.wxy.weibo.cosmos.ui.activity;

import android.os.CountDownTimer;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import org.wxy.weibo.cosmos.R;
import org.wxy.weibo.cosmos.sharepreferences.User;
import org.wxy.weibo.cosmos.ui.base.WidgetActivity;

public class StartActivity extends WidgetActivity {
    Times time;
    @Override
    protected int getLayoutID() {
        return R.layout.activity_start;
    }

    @Override
    protected void init() {
        super.init();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        time=new Times(1000,1000);
        Toast.makeText(this, "鲤鱼王使用了水溅跃", Toast.LENGTH_SHORT).show();
        time.start();
    }

    private class Times extends CountDownTimer{
        public Times(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            starActivity(MainActivity.class);
            finish();
        }
    }
}
