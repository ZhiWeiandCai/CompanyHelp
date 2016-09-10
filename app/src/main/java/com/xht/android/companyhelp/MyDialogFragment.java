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

import java.util.ArrayList;


/**
 * Activities that contain this fragment must implement the
 * Use the {@link MyDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyDialogFragment extends DialogFragment {
    int mNum;
    private ListView mListView;
    private ArrayList<PersonOfSheBao> mPSheBs = new ArrayList<>();

    /**
     * Create a new instance of MyDialogFragment, providing "num"
     * as an argument.
     */
    static MyDialogFragment newInstance(int num) {
        MyDialogFragment f = new MyDialogFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("num", num);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNum = getArguments().getInt("num");
        PersonOfSheBao temp = new PersonOfSheBao();
        temp.setmName("黄小忠");
        mPSheBs.add(temp);
        temp = new PersonOfSheBao();
        temp.setmName("黑酷子");
        mPSheBs.add(temp);
        int style = DialogFragment.STYLE_NO_TITLE, theme = 0;
        setStyle(style, theme);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_dialog, container, false);
        mListView = (ListView) view.findViewById(R.id.sb_l_view);
        SBAdapter adapter = new SBAdapter(mPSheBs);
        mListView.setAdapter(adapter);
        return view;
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
