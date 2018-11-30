package org.wxy.weibo.cosmos.ui.activity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Network;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.Response;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import org.wxy.weibo.cosmos.Bean.ShareBean;
import org.wxy.weibo.cosmos.BuildConfig;
import org.wxy.weibo.cosmos.R;
import org.wxy.weibo.cosmos.network.RetrofitHelper;
import org.wxy.weibo.cosmos.network.api.IStatuses;
import org.wxy.weibo.cosmos.sharepreferences.User;
import org.wxy.weibo.cosmos.ui.base.ActionbarActvity;
import org.wxy.weibo.cosmos.utils.NetCallBack;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class ShareActivity extends ActionbarActvity implements View.OnClickListener{
    private ImageView share;
    private EditText text;
    private ImageView img;
    private  Bitmap bitmap;
    private byte[] b;
    List<String> permissionList = new ArrayList<>();
    String[] permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};
    List<Uri> uris = new ArrayList<>();
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
                share();
                break;
            case R.id.img:
                getPermission();
                break;
        }
    }
    //发送微博
    private void share(){
//        b=new byte[1024];
////        b=getBytesByBitmap(bitmap);
//        Log.d("TAG", "share: "+ conver2HexStr(getRealPathFromUri(uris.get(0)).getBytes()));
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
//        IStatuses iStatuses=RetrofitHelper.create(IStatuses.class);
//        iStatuses.share(User.user().getToken(),"11"+"http://www.mob.com/downloads/",
//                RetrofitHelper.getbody(conver2HexStr(getRealPathFromUri(uris.get(0)).getBytes())))
//                .enqueue(new Callback<ShareBean>() {
//                    @Override
//                    public void onResponse(Call<ShareBean> call, retrofit2.Response<ShareBean> response) {
//
//                    }
//
//                    @Override
//                    public void onFailure(Call<ShareBean> call, Throwable t) {
//
//                    }
//                });
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

    /**
     * 选择图片
     */
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
//            adapter = new PicSelectedAdapter(this,uris);
//            gridView.setAdapter(adapter);
        }
    }

//    public byte[] getBytesByBitmap(Bitmap bitmap) {
//        ByteBuffer buffer = ByteBuffer.allocate(bitmap.getByteCount());
//        Log.d("TAG", "getBytesByBitmap: "+buffer.array());
//        return buffer.array();
//    }
//    public static String conver2HexStr(byte [] b)
//    {
//        StringBuffer result = new StringBuffer();
//        for(int i = 0;i<b.length;i++)
//        {
//            result.append(Long.toString(b[i] & 0xff, 2));
//        }
//        return result.toString().substring(0, result.length()-1);
//    }
//
//    public static byte[] conver2HexToByte(String hex2Str)
//    {
//        String [] temp = hex2Str.split(",");
//        byte [] b = new byte[temp.length];
//        for(int i = 0;i<b.length;i++)
//        {
//            b[i] = Long.valueOf(temp[i], 2).byteValue();
//        }
//        return b;
//    }
//    public String getRealPathFromUri(Uri contentUri) {
//        Cursor cursor = null;
//        try {
//            String[] proj = { MediaStore.Images.Media.DATA };
//            cursor = this.getContentResolver().query(contentUri, proj, null, null, null);
//            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//            cursor.moveToFirst();
//            return cursor.getString(column_index);
//        } finally {
//            if (cursor != null) {
//                cursor.close();
//            }
//        }
//    }
}
