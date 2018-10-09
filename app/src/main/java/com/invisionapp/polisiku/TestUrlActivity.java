package com.invisionapp.polisiku;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.PermissionRequest;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

public class TestUrlActivity extends AppCompatActivity {

    private static final String TAG = "TestUrlActivity";
    WebView web1;
    EditText ed1;
    Button bt1;
    String Address;
    String add;
    ProgressBar pbar;
    LinearLayout lletUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_url);

        web1 = findViewById(R.id.webView1);
        ed1 = findViewById(R.id.editText1);
        bt1 = findViewById(R.id.button1);
        pbar = findViewById(R.id.progressBar1);
        lletUrl = findViewById(R.id.ll);
        pbar.setVisibility(View.GONE);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        setWebViewSettings(web1);



        bt1.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetJavaScriptEnabled")
            @Override
            public void onClick(View v) {
                Address = ed1.getText().toString();
                WebSettings webSetting = web1.getSettings();
//                webSetting.setBuiltInZoomControls(true);
                webSetting.setJavaScriptEnabled(true);
                webSetting.setLoadsImagesAutomatically(true);
//                pbar.setVisibility(View.VISIBLE);

                web1.setWebViewClient(new WebViewClient());


                web1.loadUrl(Address);



                web1.setWebChromeClient(new WebChromeClient() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onPermissionRequest(final PermissionRequest request) {
                        Log.d(TAG, "Request permissions: ");
                        for (String res : request.getResources()) {
                            Log.d(TAG, res);
                        }
                        TestUrlActivity.this.runOnUiThread(new Runnable() {
                            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                            @Override
                            public void run() {
                                request.grant(request.getResources());
                            }
                        });
                    }

                    @Override
                    public void onPermissionRequestCanceled(PermissionRequest request) {
                        Log.d(TAG, "onPermissionRequestCanceled");
                    }

                });
                ed1.setText(""); // clear
            }
        });
    }

    @SuppressLint({"SetJavaScriptEnabled", "ObsoleteSdkInt", "NewApi"})
    private void setWebViewSettings(WebView webView) {
        WebSettings settings = webView.getSettings();

        // Enable Javascript
        settings.setJavaScriptEnabled(true);

        settings.setDatabaseEnabled(true);
        settings.setMediaPlaybackRequiresUserGesture(false);

        // Use WideViewport and Zoom out if there is no viewport defined
        settings.setUseWideViewPort(false);
        settings.setLoadWithOverviewMode(false);

        // Enable pinch to zoom without the zoom buttons
        settings.setBuiltInZoomControls(false);

        // Allow use of Local Storage
        settings.setDomStorageEnabled(true);

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            // Hide the zoom controls for HONEYCOMB+
            settings.setDisplayZoomControls(false);
        }

        // Enable remote debugging via chrome://inspect
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(false);
        }

        webView.setWebViewClient(new android.webkit.WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                String message = "SSL Certificate error.";
                switch (error.getPrimaryError()) {
                    case SslError.SSL_UNTRUSTED:
                        message = "The certificate authority is not trusted";
                        break;
                    case SslError.SSL_EXPIRED:
                        message = "The certificate has expired";
                        break;
                    case SslError.SSL_NOTYETVALID:
                        message = "The certificate is not yet valid.";
                        break;
                    case SslError.SSL_IDMISMATCH:
                        message = "The cerificate ID is mismatch";
                        break;
                    case SslError.SSL_DATE_INVALID:
                        message = "The certificate date is invalid";
                        break;
                    case SslError.SSL_INVALID:
                        message = "The certificate is invalid";
                        break;
                }
                builder.setTitle("SSL Cerificate Error");
                builder.setMessage(message);
                builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        handler.proceed();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        handler.cancel();
                    }
                });
                Log.d(TAG, "onReceivedSslError " + message);
                final AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    public class WebViewClient extends android.webkit.WebViewClient
    {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {

            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
            pbar.setVisibility(View.VISIBLE);
            ed1.setText(""); // clear
            ed1.setText(view.getUrl()); // Set current url

        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            // TODO Auto-generated method stub
            view.loadUrl(url);
            return true;
        }
        @Override
        public void onPageFinished(WebView view, String url) {

            // TODO Auto-generated method stub

            super.onPageFinished(view, url);
            pbar.setVisibility(View.GONE);
            ed1.setText(""); // clear
            ed1.setText(view.getUrl()); // Set current url
            lletUrl.setVisibility(View.GONE);
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && web1.canGoBack()) {
            web1.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
