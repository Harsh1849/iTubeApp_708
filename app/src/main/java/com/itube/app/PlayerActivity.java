package com.itube.app;

import static com.itube.app.Extensions.VIDEO_URL;
import static com.itube.app.Extensions.adjustFullScreen;
import static com.itube.app.Extensions.isConnect;
import static com.itube.app.Extensions.showToast;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.itube.app.databinding.ActivityPlayerBinding;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlayerActivity extends AppCompatActivity {

    private ActivityPlayerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityPlayerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        adjustFullScreen(binding.getRoot());

        init();
    }

    private void init() {
        if (getIntent() != null) {
            String videoUrl = getIntent().getStringExtra(VIDEO_URL);
            Log.e("TAG", "init videoUrl : " + videoUrl);

            String videoId = extractYoutubeId(videoUrl);
            if (videoId != null) {
                // Do something with the videoId
                Log.e("Video ID", videoId);
                if (isConnect(this)) {
                    setUpWebView(videoId);
                }
            } else {
                // Invalid URL or Video ID not found
                showToast(this, "Enable to load video");
            }
        }
    }

    private String extractYoutubeId(String youtubeUrl) {
        String videoId = null;
        try {
            String regex = "^(?:https?://)?(?:www\\.)?(?:youtube\\.com/watch\\?v=|youtu\\.be/)([^&\\n#]+)";
            Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(youtubeUrl);
            if (matcher.find()) {
                videoId = matcher.group(1);
            }
        } catch (Exception ex) {
            Log.e("TAG", "extractYoutubeId: " + ex.getMessage());
        }
        return videoId;
    }

    private void setUpWebView(String videoID) {
        // Enable JavaScript
        WebSettings webSettings = binding.webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Enable autoplay
//        binding.webView.setWebChromeClient(new WebChromeClient());
        binding.webView.setWebChromeClient(webChromeClient);

        // Load YouTube video
        binding.webView.loadDataWithBaseURL(null, getYouTubeHTML(videoID), "text/html", "utf-8", null);

        // Add JavaScript interface to communicate with Android code
        binding.webView.addJavascriptInterface(new JavaScriptInterface(), "Android");
    }

    private String getYouTubeHTML(String videoId) {
        return "<html><body>" +
                "<div id=\"player\"></div>" +
                "<script>" +
                "function onYouTubeIframeAPIReady() {" +
                "    player = new YT.Player('player', {" +
                "        height: '250'," +
                "        width: '100% !important'," +
                "        videoId: '" + videoId + "'," +
                "        playerVars: {" +
                "            'playsinline': 1" +
                "        }" +
                "    });" +
                "}" +
                "</script>" +
                "<script src=\"https://www.youtube.com/iframe_api\"></script>" +
                "</body></html>";
    }


    WebChromeClient webChromeClient = new WebChromeClient() {
        private View mCustomView;
        private CustomViewCallback mCustomViewCallback;
        private int mOriginalOrientation;
        private int mOriginalSystemUiVisibility;

        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            if (mCustomView != null) {
                onHideCustomView();
                return;
            }
            mCustomView = view;
            mOriginalSystemUiVisibility = getWindow().getDecorView().getSystemUiVisibility();
            mOriginalOrientation = getRequestedOrientation();
            mCustomViewCallback = callback;
            ((FrameLayout) getWindow().getDecorView()).addView(
                    mCustomView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                            FrameLayout.LayoutParams.MATCH_PARENT));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_FULLSCREEN);
        }

        @Override
        public void onHideCustomView() {
            ((FrameLayout) getWindow().getDecorView()).removeView(mCustomView);
            mCustomView = null;
            getWindow().getDecorView().setSystemUiVisibility(mOriginalSystemUiVisibility);
            setRequestedOrientation(mOriginalOrientation);
            mCustomViewCallback.onCustomViewHidden();
            mCustomViewCallback = null;
        }
    };

    public class JavaScriptInterface {
        @JavascriptInterface
        public void onVideoFinished() {
            // Handle video finished event
        }
    }

    @Override
    protected void onDestroy() {
        if (binding.webView != null) {
            binding.webView.destroy();
        }
        super.onDestroy();
    }
}