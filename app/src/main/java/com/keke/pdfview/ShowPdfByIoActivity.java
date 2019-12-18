package com.keke.pdfview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.github.barteksc.pdfviewer.PDFView;
import com.hjq.toast.ToastUtils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author Luffy
 */
public class ShowPdfByIoActivity extends Activity {
    private static final String TAG = "MainActivity";
    private PDFView mPdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_show_pdf_by_io);
        initViewsAndBaseDate();
    }

    private void initViewsAndBaseDate() {
        Intent intent = getIntent();
        assert intent!=null;
        String mPdfUrl = intent.getStringExtra("url");
        mPdfView = findViewById(R.id.pdfView);
        findViewById(R.id.ivBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        asynDownFile(mPdfUrl);
    }

    /**
     * 异步下载文件
     *
     * @param url 文件的url
     */
    private void asynDownFile(String url) {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtils.show("加载合同失败,请重试！");
                Log.d(TAG, "onFailure: e == " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) {
                Log.d(TAG, "onResponse: response == " + response);
                if (response.body() != null) {
                    ShowPdfUtil.getInstance().showPdfByInputStream(response.body().byteStream(), mPdfView);
                }
            }
        });
    }
}
