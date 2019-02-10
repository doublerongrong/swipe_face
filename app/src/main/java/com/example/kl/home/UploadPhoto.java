package com.example.kl.home;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.util.List;


public class UploadPhoto {
    private final String url = "http://192.168.128.148:8080/ProjectApi/api/FaceApi/TrainFace";
    private final String parameterName = "photo";
    public void uploadMultipart(final Context context,List<Uri> result) {

        try {
            String uploadId =
                    new MultipartUploadRequest(context, url)/**這裡還要抓使用者id在後面+*/
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