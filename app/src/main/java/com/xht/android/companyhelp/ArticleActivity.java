package com.xht.android.companyhelp;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;

public class ArticleActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        TextView mCustomView = new TextView(this);
        mCustomView.setGravity(Gravity.CENTER);
        mCustomView.setTextSize(18);
        mCustomView.setText("文章详情");
        final ActionBar aBar = getActionBar();
        int newGravity = Gravity.CENTER_HORIZONTAL;
        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.gravity = lp.gravity & ~Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK | newGravity;
        aBar.setCustomView(mCustomView, lp);
        int change = ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_CUSTOM;
        aBar.setDisplayOptions(change);
        String strUrlArt = getIntent().getStringExtra("article_url");
        if (savedInstanceState == null) {
            // First-time init; create fragment to embed in activity.
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            Fragment newFragment = ArticleFragment.newInstance(strUrlArt, null);
            ft.add(R.id.articleFragment, newFragment);
            ft.commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
