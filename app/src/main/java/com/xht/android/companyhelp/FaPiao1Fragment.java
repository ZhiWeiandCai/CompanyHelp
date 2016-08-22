package com.xht.android.companyhelp;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.xht.android.companyhelp.model.HuoWu;
import com.xht.android.companyhelp.util.LogHelper;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FaPiao1Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FaPiao1Fragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static final String TAG = "FaPiao1Fragment";

    FaPiaoActivity mFaPiaoActivity;
    private ArrayList<HuoWu> mHuoWus = new ArrayList<>();
    private ListView mListView;
    private HuoWuAdapter mAdapter;
    private EditText mJNEt, mJNshEt, mJAddEt, mJPEt, mJKHHEt, mJKHHHEt,
            mYNEt, mYNshEt, mYAddEt, mYPEt, mYKHHEt, mYKHHHEt;
    private ImageButton mAddHWLItemIBtn;

    public FaPiao1Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FaPiao1Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FaPiao1Fragment newInstance(String param1, String param2) {
        FaPiao1Fragment fragment = new FaPiao1Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mHuoWus.add(new HuoWu());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fa_piao1, container, false);
        mListView = (ListView) view.findViewById(R.id.list_view_fp);
        View headView = inflater.inflate(R.layout.headviewoflistview, mListView, false);
        mJNEt = (EditText) headView.findViewById(R.id.jia_c_et);
        mJNshEt = (EditText) headView.findViewById(R.id.nsh_et);
        mJAddEt = (EditText) headView.findViewById(R.id.add_et);
        mJPEt = (EditText) headView.findViewById(R.id.phone_et);
        mJKHHEt = (EditText) headView.findViewById(R.id.jia_c_kfh_et);
        mJKHHHEt = (EditText) headView.findViewById(R.id.jia_c_kfhao_et);
        mYNEt = (EditText) headView.findViewById(R.id.yi_c_et);
        mYNshEt = (EditText) headView.findViewById(R.id.yi_nsh_et);
        mYAddEt = (EditText) headView.findViewById(R.id.yi_add_et);
        mYPEt = (EditText) headView.findViewById(R.id.yi_phone_et);
        mYKHHEt = (EditText) headView.findViewById(R.id.yi_c_kfh_et);
        mYKHHHEt = (EditText) headView.findViewById(R.id.yi_c_kfhao_et);
        mAddHWLItemIBtn = (ImageButton) headView.findViewById(R.id.add_item);
        mAddHWLItemIBtn.setOnClickListener(this);
        mListView.addHeaderView(headView);
        mAdapter = new HuoWuAdapter(mHuoWus);
        mListView.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mFaPiaoActivity = (FaPiaoActivity) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private class HuoWuAdapter extends ArrayAdapter<HuoWu> {

        public HuoWuAdapter(ArrayList<HuoWu> items) {
            super(getActivity(), 0, items);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.huowu_item, parent, false);
                holder = new ViewHolder();
                holder.nameEt = (EditText) convertView.findViewById(R.id.i_hwName_et);
                holder.guixingEt = (EditText) convertView.findViewById(R.id.guigexinghao_et);
                holder.danweiEt = (EditText) convertView.findViewById(R.id.danwei_et);
                holder.shuliangEt = (EditText) convertView.findViewById(R.id.shuliang_et);
                holder.danjiaEt = (EditText) convertView.findViewById(R.id.danjia_et);
                holder.jineEt = (EditText) convertView.findViewById(R.id.je_buhanshui_et);
                holder.jianIBtn = (ImageButton) convertView.findViewById(R.id.jian_huowu);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            HuoWu huoWu = getItem(position);
            holder.jianIBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteOneItem(position);
                }
            });
            LogHelper.i("ghi", "posi=" + position);
            return convertView;
        }
    }

    static class ViewHolder {
        EditText nameEt;
        EditText guixingEt;
        EditText danweiEt;
        EditText shuliangEt;
        EditText danjiaEt;
        EditText jineEt;
        ImageButton jianIBtn;
        int position;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_item:
                if (mHuoWus.size() < 5) {
                    mHuoWus.add(new HuoWu());
                    mAdapter.notifyDataSetChanged();
                } else {
                    App.getInstance().showToast("一次最多只能操作5个货物");
                }
                break;
        }
    }

    private void deleteOneItem(int position) {
        if (mHuoWus.size() > 1) {
            mHuoWus.remove(position);
            mAdapter.notifyDataSetChanged();
        } else {
            App.getInstance().showToast("只剩一项了");
        }
    }
}
