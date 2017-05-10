package com.ingeniatest.gistsproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.ingeniatest.gistsproject.adapter.GistAdapterRecyclerView;
import com.ingeniatest.gistsproject.model.Gist;
import com.squareup.picasso.Picasso;

public class FileViewerActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_viewer);

        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);

        Bundle bundle = getIntent().getExtras();
        int value = bundle.getInt("Key");

        Gist gist = GistAdapterRecyclerView.getGists().get(value);
        if (gist != null){
            if (gist.getFiles() != null){
                if (gist.getFiles().getFiles().size() != 0) {

                    webView.loadUrl(gist.getFiles().getFiles().get(0).getRawUrl());
                    webView.setWebViewClient(new WebViewClient(){
                        @Override
                        public boolean shouldOverrideUrlLoading(WebView view, String url){
                            view.loadUrl(url);
                            return true;
                        }
                    });
                }
            }
        }
        showToolbar(getResources().getString(R.string.toolbar_title_filevieweractivity), false);
    }

    public void showToolbar(String title, boolean upButton){
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);
    }
}

