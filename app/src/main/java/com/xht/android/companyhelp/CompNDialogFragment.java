package com.xht.android.companyhelp;

import android.app.Activity;
import android.app.DialogFragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.xht.android.companyhelp.net.APIListener;
import com.xht.android.companyhelp.net.VolleyHelpApi;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/26.
 */
public class CompNDialogFragment extends DialogFragment {

    int mUId;
    ListView mListView;
    private ArrayList<String> mCompNames = new ArrayList<>();

    static CompNDialogFragment newInstance(int compId) {
        CompNDialogFragment f = new CompNDialogFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("num", compId);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUId = getArguments().getInt("num");

        int style = DialogFragment.STYLE_NO_TITLE, theme = 0;
        setStyle(style, theme);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_comp_n_dialog, container, false);
        mListView = (ListView) view.findViewById(R.id.cn_l_view);
        getCompName();

        return view;
    }

    private void getCompName() {
        VolleyHelpApi.getInstance().getComListAndJiaGeofYeWu(mUId, new APIListener() {
            @Override
            public void onResult(Object result) {
                JSONArray tempJA = ((JSONObject) result).optJSONArray("companyName");
                if (tempJA != null) {
                    int compJALength = tempJA.length();
                    for (int i = 0; i < compJALength; i++) {
                        JSONObject temp = tempJA.optJSONObject(i);
                        mCompNames.add(temp.optString("name"));
                    }
                } else {
                    App.getInstance().showToast("没有数据");
                }

                CNAdapter adapter = new CNAdapter(mCompNames);
                mListView.setAdapter(adapter);

            }

            @Override
            public void onError(Object e) {
                App.getInstance().showToast(e.toString());
            }
        });
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private class CNAdapter extends ArrayAdapter<String> {

        public CNAdapter(ArrayList<String> items) {
            super(getActivity(), 0, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(android.R.layout.simple_list_item_1, parent, false);
                holder = new ViewHolder();
                holder.title = (TextView) convertView.findViewById(android.R.id.text1);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            String item = getItem(position);
            holder.title.setTextColor(Color.BLUE);
            holder.title.setText(item);
            return convertView;
        }
    }

    class ViewHolder {
        public TextView title;
    }
}
