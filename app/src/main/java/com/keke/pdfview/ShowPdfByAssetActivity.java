package com.keke.pdfview;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

import com.github.barteksc.pdfviewer.PDFView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author Luffy
 */
public class ShowPdfByAssetActivity extends Activity {
    @BindView(R.id.pdfView)
    PDFView pdfView;
    @BindView(R.id.ivBack)
    ImageView ivBack;
    private Unbinder bind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_pdf_by_asset);
        bind = ButterKnife.bind(this);
        initViewsAndBaseDate();
    }

    private void initViewsAndBaseDate() {
        ShowPdfUtil.getInstance().showAssetPdf("LoanContract.pdf",pdfView);
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
