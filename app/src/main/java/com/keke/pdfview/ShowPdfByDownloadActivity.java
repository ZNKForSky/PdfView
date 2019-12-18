package com.keke.pdfview;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.Window;

import com.github.barteksc.pdfviewer.PDFView;
import com.hjq.toast.ToastUtils;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.BufferedSink;
import okio.Okio;
import okio.Sink;

/**
 * @author Luffy
 */
public class ShowPdfByDownloadActivity extends Activity {
    @BindView(R.id.pdfView)
    PDFView pdfView;
    private Unbinder bind;
    /**
     * 申请权限的请求码
     */
    private static final int REQUEST_CODE_EXTERNAL_STORAGE = 0x0001;
    /**
     * 需要读写权限
     */
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    /**
     * pdf文档的Url
     */
    private String mPdfUrl;
    /**
     * 最终需要加载的是File类型的pdf
     */
    private File pdfFile;
    /**
     * 下载文件完成的标识
     */
    private final int DOWNLOAD_SUCCESSFUL = 200;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == DOWNLOAD_SUCCESSFUL) {
                ShowPdfUtil.getInstance().showPdfByDownloadFile(pdfFile, pdfView);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_show_pdf);
        bind = ButterKnife.bind(this);
        initViewsAndBaseDate();
        checkPerMission();
    }

    /**
     * 初始化View和基本数据
     */
    private void initViewsAndBaseDate() {
        Intent intent = getIntent();
        assert intent != null;
        mPdfUrl = intent.getStringExtra("url");
    }

    /**
     * 检测权限
     */
    private void checkPerMission() {
        //M版本之后才需要申请权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int permission = ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
            //无权限时申请权限
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // We don't have permission so prompt the user
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE,
                        REQUEST_CODE_EXTERNAL_STORAGE);
            } else {
                downloadPdf();
            }
        } else {
            downloadPdf();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (REQUEST_CODE_EXTERNAL_STORAGE == requestCode && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            downloadPdf();
        } else {
            //判断是否勾选了“禁止后不再提示”选框
            boolean showRequestPermission = ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0]);
            if (!showRequestPermission) {
                ToastUtils.show("您已禁止提醒授权，如需正常使用，请到应用权限管理界面进行授权");
            }
        }
    }

    /**
     * 开始下载PDF
     */
    private void downloadPdf() {
        //应用缓存路径
        String cacheUrl = getCacheDir().getAbsolutePath();
        //文件名称
        String pdfName = mPdfUrl.substring(mPdfUrl.lastIndexOf("/") + 1);
        pdfFile = new File(cacheUrl, pdfName);
        if (pdfFile.exists()) {
            ShowPdfUtil.getInstance().showPdfByDownloadFile(pdfFile, pdfView);
        } else {
            Request request = new Request.Builder()
                    .url(mPdfUrl)
                    .build();

            new OkHttpClient().newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    // 下载失败
                    ToastUtils.show("下载失败，请重试");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Sink sink;
                    BufferedSink bufferedSink = null;
                    try {
                        if (!pdfFile.exists()) {
                            Log.e("Luffy", "onResponse: no file");
                            boolean newFile = pdfFile.createNewFile();
                            if (newFile) {
                                sink = Okio.sink(pdfFile);
                                bufferedSink = Okio.buffer(sink);
                                if (response.body() != null) {
                                    bufferedSink.writeAll(response.body().source());
                                }
                                bufferedSink.close();
                                Message message = Message.obtain();
                                message.what = DOWNLOAD_SUCCESSFUL;
                                handler.sendMessage(message);
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (bufferedSink != null) {
                            bufferedSink.close();
                        }
                    }
                }
            });
        }
    }

    @OnClick(R.id.ivBack)
    public void onViewClicked() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind.unbind();
    }
}
