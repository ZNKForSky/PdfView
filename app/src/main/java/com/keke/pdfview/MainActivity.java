package com.keke.pdfview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author Luffy Hurris
 */
public class MainActivity extends Activity {
    String mPdfUrl = "http://happyleasing.cn/NEWGA/base/20190628//b0cca3b23927467cbd0eee8421a5fa0a.pdf";
    private Unbinder bind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        bind = ButterKnife.bind(this);

    }

    @OnClick({R.id.ivBack, R.id.btn_load_pdf_by_io, R.id.btn_load_pdf_by_download, R.id.btn_load_pdf_by_asset})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.btn_load_pdf_by_io:
                skipToTargetActivity(ShowPdfByIoActivity.class);
                break;
            case R.id.btn_load_pdf_by_download:
                skipToTargetActivity(ShowPdfByDownloadActivity.class);
                break;
            case R.id.btn_load_pdf_by_asset:
                skipToTargetActivity(ShowPdfByAssetActivity.class);
                break;
            default:
                break;
        }
    }

    private void skipToTargetActivity(Class cls) {
        Intent intent = new Intent(MainActivity.this, cls);
        intent.putExtra("url", mPdfUrl);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind.unbind();
    }
}
