package org.wxy.weibo.cosmos.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import org.wxy.weibo.cosmos.R;
import org.wxy.weibo.cosmos.sharepreferences.User;
import org.wxy.weibo.cosmos.ui.base.ActionbarActvity;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ShareActivity extends ActionbarActvity implements View.OnClickListener{
    private ImageView share;
    private EditText text;
    private ImageView img;
    List<String> permissionList = new ArrayList<>();
    String[] permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};
    List<Uri> uris = new ArrayList<>();
    private MsgThread msgThread;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==0)
            {

                share();
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContTitle("写微博");
    }

    @Override
    protected int getContLayoutID() {
        return R.layout.activity_share;
    }

    @Override
    protected void initView() {
        super.initView();
        share=findViewById(R.id.share);
        text=findViewById(R.id.text);
        img=findViewById(R.id.img);
    }

    @Override
    protected void init() {
        super.init();
        share.setOnClickListener(this);
        img.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.share:
                msgThread=new MsgThread(0);
                msgThread.run();
                break;
            case R.id.img:
                getPermission();
                break;
        }
    }
    //发送微博
    private void share(){

       if (uris.size()==0)//发送仅文字微博
       {
           OkGo.<String>post("https://api.weibo.com/2/statuses/share.json")
                   .params("access_token",User.user().getToken())
                   .params("status",text.getText().toString()+"http://www.mob.com/downloads/")
                   .execute(new StringCallback() {
                       @Override
                       public void onSuccess(Response<String> response) {
                           if (response.code()==200)
                           {
                               showToast("发送成功");
                               finish();
                           }
                           else
                               showToast("发送失败");
                       }
                   });
       }
       else//发送图文微博
       {
           File file=new File(getRealPathFromUri(this,uris.get(0)));
           OkGo.<String>post("https://api.weibo.com/2/statuses/share.json")
                   .isMultipart(true)
                   .params("access_token",User.user().getToken())
                   .params("status",text.getText().toString()+"http://www.mob.com/downloads/")
                   .params("pic", file)
                   .execute(new StringCallback() {
                       @Override
                       public void onSuccess(Response<String> response) {
                           if (response.code()==200)
                           {
                               showToast("发送成功");
                               finish();
                           }
                           else
                               showToast("发送失败");
                       }
                   });
       }
    }

    private void getPermission() {
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permissions[i]);//添加还未授予的权限
            }
        }
        if (permissionList.size() > 0) {//有权限没有通过，需要申请
            ActivityCompat.requestPermissions(this, permissions, 1);
        } else {
            //选择图片
            selectPic();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            showToast("请同意权限");
                            return;
                        }
                    }
                    //选择图片
                    selectPic();
                } else {
                    showToast("发生未知错误");

                }
                break;
            default:
        }
    }

   //选择图片
    private void selectPic() {
        Matisse.from(ShareActivity.this)
                .choose(MimeType.ofAll())
                .countable(true)
                .capture(true) //使用拍照功能
                .captureStrategy(new CaptureStrategy(true, "org.wxy.weibo.cosmos.provider")) //是否拍照功能，并设置拍照后图片的保存路径
                .maxSelectable(9)
                .gridExpectedSize(
                        getResources().getDimensionPixelSize(R.dimen.image_size_large))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                .thumbnailScale(0.85f)
                .imageEngine(new org.wxy.weibo.cosmos.utils.GlideEngine())
                .forResult(1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            uris = Matisse.obtainResult(data);
        }
    }
    public static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
    class MsgThread extends Thread{
        private int method;
        private Message message;
        public MsgThread(int method){
            this.method=method;
            message=new Message();
        }
        @Override
        public void run() {
            super.run();
            message.what=method;
            handler.sendMessage(message);
        }
    }
}
