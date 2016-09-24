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

import com.xht.android.companyhelp.model.PersonOfSheBao;
import com.xht.android.companyhelp.net.APIListener;
import com.xht.android.companyhelp.net.VolleyHelpApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Activities that contain this fragment must implement the
 * Use the {@link MyDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyDialogFragment extends DialogFragment {
    int mCompId;
    int mYear;
    int mNum;
    private ListView mListView;
    private ArrayList<PersonOfSheBao> mPSheBs = new ArrayList<>();

    /**
     * Create a new instance of MyDialogFragment, providing "num"
     * as an argument.
     */
    static MyDialogFragment newInstance(int compId, int year, int month) {
        MyDialogFragment f = new MyDialogFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("cid", compId);
        args.putInt("year", year);
        args.putInt("num", month);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCompId = getArguments().getInt("cid");
        mYear = getArguments().getInt("year");
        mNum = getArguments().getInt("num");

        int style = DialogFragment.STYLE_NO_TITLE, theme = 0;
        setStyle(style, theme);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_dialog, container, false);
        mListView = (ListView) view.findViewById(R.id.sb_l_view);
        getSBPeople();

        return view;
    }

    private void getSBPeople() {
        VolleyHelpApi.getInstance().getSBPeople(mCompId, mYear, mNum, new APIListener() {
            @Override
            public void onResult(Object result) {
                JSONArray tempJA = ((JSONObject) result).optJSONArray("entity");
                JSONObject tempJO;
                if (tempJA != null) {
                    int len = tempJA.length();
                    for (int i = 0; i < len; i ++) {
                        try {
                            tempJO = tempJA.getJSONObject(i);
                            PersonOfSheBao temp = new PersonOfSheBao();
                            temp.setmName(tempJO.getString("personName"));
                            mPSheBs.add(temp);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    SBAdapter adapter = new SBAdapter(mPSheBs);
                    mListView.setAdapter(adapter);
                } else {
                    App.getInstance().showToast("没有数据");
                }

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

    private class SBAdapter extends ArrayAdapter<PersonOfSheBao> {

        public SBAdapter(ArrayList<PersonOfSheBao> items) {
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
            PersonOfSheBao item = getItem(position);
            holder.title.setTextColor(Color.BLUE);
            holder.title.setText(item.getmName());
            return convertView;
        }
    }

    class ViewHolder {
        public TextView title;
    }
}
