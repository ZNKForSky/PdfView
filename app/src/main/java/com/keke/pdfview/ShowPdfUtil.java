package com.keke.pdfview;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.hjq.toast.ToastUtils;

import java.io.File;
import java.io.InputStream;

/**
 * @author : Luffy Harris
 * e-mail : 744423651@qq.com
 * phone  : 13002903389
 * date   : 2019/12/12-17:46
 * desc   : 应用内打开PDF文档的工具类，依赖 AndroidPdfViewer这个库，github链接：https://github.com/barteksc/AndroidPdfViewer
 * version: 1.0
 */
@SuppressWarnings("all")
public class ShowPdfUtil implements OnLoadCompleteListener, OnErrorListener {

    private ShowPdfUtil() {
    }

    private static class ShowPdfUtilBuilder {
        private static final ShowPdfUtil SHOW_PDF_UTIL = new ShowPdfUtil();

    }

    public static ShowPdfUtil getInstance() {
        return ShowPdfUtilBuilder.SHOW_PDF_UTIL;
    }


    public void showPdfByInputStream(InputStream inputStream, PDFView pdfView) {
        setConfigurator(pdfView.fromStream(inputStream));
    }

    public void showAssetPdf(String assetPath, PDFView pdfView) {
        setConfigurator(pdfView.fromAsset(assetPath));
    }

    /**
     * 查看下载的PDF
     */
    public void showPdfByDownloadFile(File pdfFile, PDFView pdfView) {
        setConfigurator(pdfView.fromFile(pdfFile));
    }

    private void setConfigurator(PDFView.Configurator configurator) {
        configurator.defaultPage(0)
                .onLoad(this)
                .onError(this)
                .enableAnnotationRendering(true)
                .load();
    }

    @Override
    public void loadComplete(int nbPages) {
        ToastUtils.show("加载文件成功");
    }

    @Override
    public void onError(Throwable t) {
        ToastUtils.show("加载文件失败，请重试");
    }
}
