package com.example.administrator.sapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

/**
 * 主程序
 * Created by Kingfeng on 2018/4/16.
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final String URL = "http://b2b2.stlintl.com.cn:999/appserver/upload/";

    private final static int SCANNIN_GREQUEST_CODE = 1;

    private static final int UPDATE_RESULTS = 1;

    private ImageView imgShow ;

    private String imgImageId;

    private String inputSearchs = "";

    private TextView buttonSearch;

    private EditText inputSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgShow = (ImageView) findViewById(R.id.image_show);
        buttonSearch = (TextView) findViewById(R.id.button_search);
        inputSearch = (EditText) findViewById(R.id.input_search);
        findViewById(R.id.button_qrcode).setOnClickListener(this);

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputSearchs = inputSearch.getText().toString().trim().toUpperCase();
                inputSearchs = URL + inputSearchs + ".jpg";
                refreshImages();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_qrcode:
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, ScanActivity.class);
                startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String QRCodeValues;
        switch (requestCode) {
            case SCANNIN_GREQUEST_CODE:
                if(resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    QRCodeValues = bundle.getString("result").trim().toUpperCase();
                    inputSearchs = URL + QRCodeValues + ".jpg";
                    inputSearch.setText("");
                    refreshImages();
                }
                else {
                    Glide.with(this).load(R.drawable.error).into(imgShow);
                }

                break;
        }
    }

    private void refreshImages() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initImages(inputSearchs);
                    }
                });
            }
        }).start();
    }

    private void initImages(String imgImageId) {
        clearCache(this);
        //Glide.with(this).load(imgImageId).into(imgShow);
        //Glide.with(this).load(imgImageId).placeholder(R.drawable.loading).into(imgShow);
        Glide.with(this).load(imgImageId).error(R.drawable.error).into(imgShow);
    }

    /**
     * 清除缓存
     */
    public void clearCache(final Context context){
        clearMemoryCache(context);
        new Thread(new Runnable() {
            @Override
            public void run() {
                clearDiskCache(context);
            }
        }).start();
    }

    /**
     * 清除内存缓存
     */
    public void clearMemoryCache(Context context){
        Glide.get(context).clearMemory();
    }

    /**
     * 清除磁盘缓存
     */
    public void clearDiskCache(Context context){
        Glide.get(context).clearDiskCache();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
