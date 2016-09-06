package com.xht.android.companyhelp;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;

public class ArticleActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        String strUrlArt = getIntent().getStringExtra("article_url");
        if (savedInstanceState == null) {
            // First-time init; create fragment to embed in activity.
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            Fragment newFragment = ArticleFragment.newInstance(strUrlArt, null);
            ft.add(R.id.articleFragment, newFragment);
            ft.commit();
        }
    }
}
