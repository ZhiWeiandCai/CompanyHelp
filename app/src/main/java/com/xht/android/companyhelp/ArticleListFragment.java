package com.xht.android.companyhelp;

import java.util.ArrayList;

import com.xht.android.companyhelp.model.Article;
import com.xht.android.companyhelp.net.APIListener;
import com.xht.android.companyhelp.net.VolleyHelpApi;
import com.xht.android.companyhelp.util.LogHelper;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

public class ArticleListFragment extends Fragment {
    private static final String TAG = "ArticleListFragment";

    private ListView mListView;
    private ArrayList<Article> mArticles = new ArrayList<>();
    private int witchF;

    public ArticleListFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        witchF = bundle.getInt("witchF", 0);
        fetchItemTask(witchF);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_articlelist, container, false);
        mListView = (ListView) view.findViewById(R.id.articleListV);
        setupAdapter();
        return view;
    }

    private void setupAdapter() {
        if (getActivity() == null || mListView == null) {
            return;
        }
        if (mArticles != null) {
            mListView.setAdapter(new ArticleAdapter(mArticles));
        } else {
            mListView.setAdapter(null);
        }
    }

    /**
     * 向服务器获取这类文章
     *
     * @param witch 类别
     */
    private void fetchItemTask(int witch) {
        VolleyHelpApi.getInstance().getArticleItems(witch, new APIListener() {
            @Override
            public void onResult(Object result) {
                LogHelper.i(TAG, result.toString());
                setupAdapter();
            }

            @Override
            public void onError(Object e) {
                App.getInstance().showToast(e.toString());
            }
        });
    }

    private class ArticleAdapter extends ArrayAdapter<Article> {

        public ArticleAdapter(ArrayList<Article> items) {
            super(getActivity(), 0, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.article_item, parent, false);
            }
            Article item = getItem(position);
            ImageView imageView = (ImageView) convertView.findViewById(R.id.artPic);
            imageView.setImageResource(R.mipmap.phone_sym_img);
            return convertView;
        }

    }

}
