package fau.amoracen.covid_19update.ui.homeActivity.news;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.Objects;

import fau.amoracen.covid_19update.R;
import fau.amoracen.covid_19update.data.NewsArticles;

public class NewsActivity extends AppCompatActivity {

    private WebView webView;
    private NewsArticles.Articles article;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_activity);

        /*Get Data*/
        Intent intent = getIntent();
        article = (NewsArticles.Articles) Objects.requireNonNull(intent.getExtras()).getSerializable("News");
        if (article == null) finish();
        webView = findViewById(R.id.webView);
        webView.setInitialScale(1);
        webView.setWebChromeClient(new WebChromeClient());
        webView.getSettings().setAllowFileAccess(true);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        webView.loadUrl(article.getUrl());
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
