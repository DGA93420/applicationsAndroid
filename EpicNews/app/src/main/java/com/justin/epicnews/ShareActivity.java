package com.justin.epicnews;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by lejus on 27/03/2017.
 */

public class ShareActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        String text = getIntent().getStringExtra(Intent.EXTRA_TEXT);
        ((TextView)findViewById(R.id.textView)).setText(text);

    }
}
