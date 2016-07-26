package com.xht.android.companyhelp;

import java.util.ArrayList;

import com.xht.android.companyhelp.model.Article;

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
	
	private ListView mListView;
	private ArrayList<Article> mArticles = new ArrayList<>();
	
	public ArticleListFragment() {
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_articlelist, container, false);
		mListView = (ListView) view.findViewById(R.id.articleListV);
		return view;
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
			ImageView imageView = (ImageView)convertView.findViewById(R.id.artPic);
            imageView.setImageResource(R.mipmap.phone_sym_img);
			return convertView;		
		}
		
	}

}
