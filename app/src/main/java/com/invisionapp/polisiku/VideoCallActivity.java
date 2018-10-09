package com.invisionapp.polisiku;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.webkit.PermissionRequest;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class VideoCallActivity extends AppCompatActivity {

    private static final String TAG = "VideoCallActivity";

    String url_test = "https://invision.io/espresso/index.html";
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_call);

        webView = findViewById(R.id.web_view_VideoCall);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        if (webView != null){

            setWebViewSettings(webView);
            webView.loadUrl(url_test);
            webView.setWebChromeClient(new WebChromeClient() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onPermissionRequest(final PermissionRequest request) {
                    Log.d(TAG, "Request permissions: ");
                    for (String res : request.getResources()) {
                        Log.d(TAG, res);
                    }
                    VideoCallActivity.this.runOnUiThread(new Runnable() {
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

        } else {
            Intent intent = new Intent(this, TestUrlActivity.class);
            startActivity(intent);

            Toast.makeText(this, "Log pindah ke Url Test", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "PindahAct: "+ " : Log pindah ke Url Test");
        }
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

        webView.setWebViewClient(new WebViewClient() {
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
}
