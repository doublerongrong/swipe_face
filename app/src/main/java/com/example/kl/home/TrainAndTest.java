package com.example.kl.home;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.util.List;

public class TrainAndTest extends AppCompatActivity {

    private Button test ;
    private int REQUEST_CODE_CHOOSE = 69;
    public List<Uri> result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_and_test);
        test = (Button)findViewById(R.id.buttonTest);

        test.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){


                Matisse.from(TrainAndTest.this)
                        .choose(MimeType.ofAll())//图片类型
                        .countable(false)//true:选中后显示数字;false:选中后显示对号
                        .maxSelectable(9)//可选的最大数
                        .capture(true)//选择照片时，是否显示拍照
                        .captureStrategy(new CaptureStrategy(true, "com.example.kl.home.fileprovider"))//参数1 true表示拍照存储在共有目录，false表示存储在私有目录；参数2与 AndroidManifest中authorities值相同，用于适配7.0系统 必须设置
                        .imageEngine(new GlideEngine())//图片加载引擎
                        .theme(R.style.Matisse_Zhihu)
                        .forResult(REQUEST_CODE_CHOOSE);//REQUEST_CODE_CHOOSE自定義


            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            UploadPhoto upload = new UploadPhoto();
            result = Matisse.obtainResult(data);
            upload.uploadMultipart(this,result);
            //uploadMultipart(this);
        }
    }
}
