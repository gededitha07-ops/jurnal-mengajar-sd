package id.jurnalmengajarsd.app;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebSettings;
import android.webkit.JavascriptInterface;
import android.webkit.WebViewClient;
import android.print.PrintManager;
import android.print.PrintDocumentAdapter;
import android.content.Context;

public class MainActivity extends Activity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        webView = new WebView(this);
        setContentView(webView);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);

        webView.setWebViewClient(new WebViewClient());

        webView.addJavascriptInterface(new Object() {
            @JavascriptInterface
            public void cetak() {
                runOnUiThread(() -> {
                    PrintManager manager =
                        (PrintManager) getSystemService(Context.PRINT_SERVICE);

                    PrintDocumentAdapter adapter =
                        webView.createPrintDocumentAdapter("Jurnal-Mengajar-SD");

                    manager.print("Jurnal-Mengajar-SD", adapter, null);
                });
            }
        }, "Android");

        webView.loadUrl("file:///android_asset/index.html");
    }
}
