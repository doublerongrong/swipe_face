package com.example.kl.home;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import net.gotev.uploadservice.MultipartUploadRequest;


import java.util.List;

/*
在Bt設置點擊後加入
Matisse.from(MainActivity.this)
                        .choose(MimeType.ofAll())//图片类型
                        .countable(false)//true:选中后显示数字;false:选中后显示对号
                        .maxSelectable(9)//可选的最大数
                        .capture(true)//选择照片时，是否显示拍照
                        .captureStrategy(new CaptureStrategy(true, "com.example.kl.home.fileprovider"))//参数1 true表示拍照存储在共有目录，false表示存储在私有目录；参数2与 AndroidManifest中authorities值相同，用于适配7.0系统 必须设置
                        .imageEngine(new GlideEngine())//图片加载引擎
                        .theme(R.style.Matisse_Zhihu)
                        .forResult(REQUEST_CODE_CHOOSE);//REQUEST_CODE_CHOOSE自定義
onActivityResult裡加入
if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            UploadPhoto upload = new UploadPhoto();
            result = Matisse.obtainResult(data);
            upload.uploadMultipart(this,result);
        }
* */

public class UploadPhoto {
    private final String uri = "http://192.168.1.10:8080/ProjectApi/api/FaceApi/TrainFace/";
    //http://localhost:8080/ProjectApi/api/FaceApi/TrainFace/
    private final String parameterName = "photos";
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();//抓現在登入user
    String email = user.getEmail();//抓user.email
    String [] uriEmailArray = email.split("@");
    String uriEmail = uriEmailArray[0];

    public void uploadMultipart(final Context context, List<Uri> result) {

        try {
            String uploadId =
                    new MultipartUploadRequest(context, uri+uriEmail)
                            // starting from 3.1+, you can also use content:// URI string instead of absolute file
                            .addFileToUpload(result.get(0).toString(), parameterName)
                            //.setNotificationConfig(new UploadNotificationConfig())
                            .setMaxRetries(2)
                            .startUpload();


        } catch (Exception exc) {
            Log.e("AndroidUploadService", exc.getMessage(), exc);
        }
    }

}