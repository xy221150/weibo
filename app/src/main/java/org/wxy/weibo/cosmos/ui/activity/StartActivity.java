package org.wxy.weibo.cosmos.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import org.wxy.weibo.cosmos.R;
import org.wxy.weibo.cosmos.sharepreferences.User;
import org.wxy.weibo.cosmos.ui.base.WidgetActivity;

public class StartActivity extends AppCompatActivity {
    Times time;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Toast.makeText(this, "鲤鱼王使用了水溅跃", Toast.LENGTH_SHORT).show();
        time=new Times(1000,2500);
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
            Intent intent=new Intent(StartActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
