
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

                        PrintDocumentAdapter adapter =
                            webView.createPrintDocumentAdapter(
                                "Jurnal-Mengajar-SD"
                            );

                        manager.print(
                            "Jurnal-Mengajar-SD",
                            adapter,
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

