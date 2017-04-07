package com.justin.epicnews;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

/**
 * Created by lejus on 23/03/2017.
 */

public class ArticleActivity extends AppCompatActivity {

    //On charge le Fragment que l'on désire en question, c'est à dire l'articleFragment
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        //On ajoute le link et le titre à k'ArticleFragment grâce à la static factory method
        getFragmentManager().beginTransaction()
                .add(R.id.articleFragment, ArticleFragment.
                        create(getIntent().getStringExtra("link"),getIntent().getStringExtra("title")))
                .commit();
    }
}
