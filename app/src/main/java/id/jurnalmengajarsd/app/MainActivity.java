
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
import android.widget.Toast;

public class MainActivity extends Activity {

    private WebView webView;
    private WebView printWebView;

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
            public void cetak(String html) {
                runOnUiThread(() -> {
                    try {
                        PrintManager manager =
                            (PrintManager) getSystemService(
                                Context.PRINT_SERVICE
                            );

                        if (manager == null) {
                            Toast.makeText(
                                MainActivity.this,
                                "Layanan cetak tidak tersedia",
                                Toast.LENGTH_LONG
                            ).show();
                            return;
                        }

                        printWebView = new WebView(MainActivity.this);

                        printWebView.setWebViewClient(
                            new WebViewClient() {
                                @Override
                                public void onPageFinished(
                                    WebView view, String url
                                ) {
                                    PrintDocumentAdapter adapter =
                                        view.createPrintDocumentAdapter(
                                            "Laporan-Jurnal-Mengajar-SD"
                                        );

                                    manager.print(
                                        "Laporan-Jurnal-Mengajar-SD",
                                        adapter,
                                        null
                                    );
                                }
                            }
                        );

                        String halaman =
                            "<html><head><meta charset='UTF-8'>" +
                            "<style>" +
                            "body{font-family:Arial;padding:15px;color:#202b3c}" +
                            "table{width:100%;border-collapse:collapse}" +
                            "th,td{border:1px solid #777;padding:7px;text-align:left}" +
                            "button,input,select,textarea,label{display:none!important}" +
                            "@page{margin:12mm}" +
                            "</style></head><body>" +
                            html +
                            "</body></html>";

                        printWebView.loadDataWithBaseURL(
                            null,
                            halaman,
                            "text/html",
                            "UTF-8",
                            null
                        );

                    } catch (Exception e) {
                        Toast.makeText(
                            MainActivity.this,
                            "Gagal membuka cetak: " + e.getMessage(),
                            Toast.LENGTH_LONG
                        ).show();
                    }
                });
            }

        }, "Android");

        webView.loadUrl("file:///android_asset/index.html");
    }
}

