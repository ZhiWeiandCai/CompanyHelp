package com.xht.android.companyhelp;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.xht.android.companyhelp.model.HuoWu;
import com.xht.android.companyhelp.util.LogHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FaPiao1Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FaPiao1Fragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";
    private static final String ARG_PARAM5 = "param5";
    private static final String ARG_PARAM6 = "param6";
    private static final String ARG_PARAM7 = "param7";
    private static final String ARG_PARAM8 = "param8";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private int[] mParam2;
    private String[] mParam3;
    private String[] mParam4;
    private String[] mParam5;
    private String[] mParam6;
    private String[] mParam7;
    private String[] mParam8;

    private static final String TAG = "FaPiao1Fragment";

    FaPiaoActivity mFaPiaoActivity;
    private ArrayList<HuoWu> mHuoWus = new ArrayList<>();
    private ListView mListView;
    private HuoWuAdapter mAdapter;
    private EditText mJNEtX, mJNshEt, mJAddEt, mJPEt, mJKHHEt, mJKHHHEt,
            mYNEt, mYNshEt, mYAddEt, mYPEt, mYKHHEt, mYKHHHEt;
    private ImageButton mAddHWLItemIBtn;
    private Spinner mJNEt;
    private int mPosiFlag;

    DecimalFormat df = new DecimalFormat("0.00");//保留两位小数

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
    public static FaPiao1Fragment newInstance(String param1, int[] param2, String[] param3, String[] param4,
            String[] param5, String[] param6, String[] param7, String[] param8) {
        FaPiao1Fragment fragment = new FaPiao1Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putIntArray(ARG_PARAM2, param2);
        args.putStringArray(ARG_PARAM3, param3);
        args.putStringArray(ARG_PARAM4, param4);
        args.putStringArray(ARG_PARAM5, param5);
        args.putStringArray(ARG_PARAM6, param6);
        args.putStringArray(ARG_PARAM7, param7);
        args.putStringArray(ARG_PARAM8, param8);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getIntArray(ARG_PARAM2);
            mParam3 = getArguments().getStringArray(ARG_PARAM3);
            mParam4 = getArguments().getStringArray(ARG_PARAM4);
            mParam5 = getArguments().getStringArray(ARG_PARAM5);
            mParam6 = getArguments().getStringArray(ARG_PARAM6);
            mParam7 = getArguments().getStringArray(ARG_PARAM7);
            mParam8 = getArguments().getStringArray(ARG_PARAM8);
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
        mJNEtX = (EditText) headView.findViewById(R.id.jia_c_et_x);
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
        mJNEt = (Spinner) headView.findViewById(R.id.jia_c_et);
        ArrayAdapter<CharSequence> arrayAdapter;
        if (mParam3 != null) {
            arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, mParam3);
        } else {
            arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, new String[]{"没有可选公司"});
        }
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mJNEt.setAdapter(arrayAdapter);
        mJNEt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                LogHelper.i("spinner1-公司区域", mJNEt.getSelectedItem().toString());
                mPosiFlag = position;
                if (mParam3 != null) {
                    updateJiaInfo();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });
        if (mParam3 == null)
            mJNEt.setVisibility(View.GONE);
        mAddHWLItemIBtn = (ImageButton) headView.findViewById(R.id.add_item);
        mAddHWLItemIBtn.setOnClickListener(this);
        mListView.addHeaderView(headView);
        mAdapter = new HuoWuAdapter(mHuoWus);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        return view;
    }

    private void updateJiaInfo() {
        if (!mParam3[mPosiFlag].equals("null"))
            mJNEtX.setText(mParam3[mPosiFlag]);
        else mJNEtX.setText("");
        if (!mParam4[mPosiFlag].equals("null"))
            mJNshEt.setText(mParam4[mPosiFlag]);
        else mJNshEt.setText("");
        if (!mParam5[mPosiFlag].equals("null"))
            mJAddEt.setText(mParam5[mPosiFlag]);
        else mJAddEt.setText("");
        if (!mParam6[mPosiFlag].equals("null"))
            mJPEt.setText(mParam6[mPosiFlag]);
        else mJPEt.setText("");
        if (!mParam7[mPosiFlag].equals("null"))
            mJKHHEt.setText(mParam7[mPosiFlag]);
        else mJKHHEt.setText("");
        if (!mParam8[mPosiFlag].equals("null"))
            mJKHHHEt.setText(mParam8[mPosiFlag]);
        else mJKHHHEt.setText("");
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), HuoWuActivity.class);
        intent.putExtra("whichItem", position - 1);
        intent.putExtra("hwname", mHuoWus.get(position - 1).getmName());
        intent.putExtra("guige", mHuoWus.get(position - 1).getmXingHao());
        intent.putExtra("danwei", mHuoWus.get(position - 1).getmDanWei());
        intent.putExtra("shul", mHuoWus.get(position - 1).getmShuLiang());
        intent.putExtra("danjia", mHuoWus.get(position - 1).getmDanJia());
        intent.putExtra("jine", mHuoWus.get(position - 1).getmJinE());
        getActivity().startActivityForResult(intent, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == -1) {
                Bundle bundle = intent.getBundleExtra("huowuData");
                int posi = bundle.getInt("wItem");
                mHuoWus.get(posi).setmName(bundle.getString("hw1"));
                mHuoWus.get(posi).setmXingHao(bundle.getString("hw2"));
                mHuoWus.get(posi).setmDanWei(bundle.getString("hw3"));
                mHuoWus.get(posi).setmShuLiang(bundle.getInt("hw4"));
                mHuoWus.get(posi).setmDanJia(bundle.getDouble("hw5"));
                mHuoWus.get(posi).setmJinE(bundle.getDouble("hw6"));
                mAdapter.notifyDataSetChanged();
            }
        }
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
                holder.nameEt = (TextView) convertView.findViewById(R.id.i_hwName_et);
                holder.guixingEt = (TextView) convertView.findViewById(R.id.guigexinghao_et);
                holder.danweiEt = (TextView) convertView.findViewById(R.id.danwei_et);
                holder.shuliangEt = (TextView) convertView.findViewById(R.id.shuliang_et);
                holder.danjiaEt = (TextView) convertView.findViewById(R.id.danjia_et);
                holder.jineEt = (TextView) convertView.findViewById(R.id.je_buhanshui_et);
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
            holder.nameEt.setText(huoWu.getmName());
            holder.guixingEt.setText(huoWu.getmXingHao());
            holder.danweiEt.setText(huoWu.getmDanWei());
            holder.shuliangEt.setText("" + huoWu.getmShuLiang());
            BigDecimal bd = new BigDecimal(huoWu.getmDanJia());
            holder.danjiaEt.setText(df.format(bd));
            bd = new BigDecimal(huoWu.getmJinE());
            holder.jineEt.setText(df.format(bd));

            return convertView;
        }
    }

    static class ViewHolder {
        TextView nameEt;
        TextView guixingEt;
        TextView danweiEt;
        TextView shuliangEt;
        TextView danjiaEt;
        TextView jineEt;
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

    public JSONObject postJsonData() {
        for (HuoWu temp : mHuoWus) {
            if (temp.getmName() == null || temp.getmName() == "" || temp.getmXingHao() == null || temp.getmXingHao() == ""
                    || temp.getmDanWei() == null || temp.getmDanWei() == "" || temp.getmShuLiang() == 0
                    || temp.getmDanJia() == 0d || temp.getmJinE() == 0d) {
                App.getInstance().showToast("请把信息填写完整...");
                return null;
            }
        }
        JSONObject jsonObj = new JSONObject();
        if (mJNEtX.getText() == null || mJNEtX.getText().toString().isEmpty()) {
            App.getInstance().showToast("请把信息填写完整...");
            return null;
        }
        if (mJNshEt.getText() == null || mJNshEt.getText().toString().isEmpty()) {
            App.getInstance().showToast("请把信息填写完整...");
            return null;
        }
        if (mJAddEt.getText() == null || mJAddEt.getText().toString().isEmpty()) {
            App.getInstance().showToast("请把信息填写完整...");
            return null;
        }
        if (mJPEt.getText() == null || mJPEt.getText().toString().isEmpty()) {
            App.getInstance().showToast("请把信息填写完整...");
            return null;
        }
        if (mJKHHEt.getText() == null || mJKHHEt.getText().toString().isEmpty()) {
            App.getInstance().showToast("请把信息填写完整...");
            return null;
        }
        if (mJKHHHEt.getText() == null || mJKHHHEt.getText().toString().isEmpty()) {
            App.getInstance().showToast("请把信息填写完整...");
            return null;
        }
        if (mYNEt.getText() == null || mYNEt.getText().toString().isEmpty()) {
            App.getInstance().showToast("请把信息填写完整...");
            return null;
        }
        if (mYNshEt.getText() == null || mYNshEt.getText().toString().isEmpty()) {
            App.getInstance().showToast("请把信息填写完整...");
            return null;
        }
        if (mYAddEt.getText() == null || mYAddEt.getText().toString().isEmpty()) {
            App.getInstance().showToast("请把信息填写完整...");
            return null;
        }
        if (mYPEt.getText() == null || mYPEt.getText().toString().isEmpty()) {
            App.getInstance().showToast("请把信息填写完整...");
            return null;
        }
        if (mYKHHEt.getText() == null || mYKHHEt.getText().toString().isEmpty()) {
            App.getInstance().showToast("请把信息填写完整...");
            return null;
        }
        if (mYKHHHEt.getText() == null || mYKHHHEt.getText().toString().isEmpty()) {
            App.getInstance().showToast("请把信息填写完整...");
            return null;
        }
        try {
            JSONArray jA = new JSONArray();
            for (HuoWu temp : mHuoWus) {
                JSONObject jo = new JSONObject();
                jo.put("HuoWuName", temp.getmName());
                jo.put("HuoWuHao", temp.getmXingHao());
                jo.put("HuoWuDanWei", temp.getmDanWei());
                jo.put("HuoWuShuLiang", temp.getmShuLiang());
                jo.put("HuoWuDanJia", temp.getmDanJia());
                jo.put("HuoWuJinE", temp.getmJinE());
                jA.put(jo);
            }
            if (mParam3 != null)
                jsonObj.put("jiaCId", mParam2[mPosiFlag]);
            else
                jsonObj.put("jiaCId", 0);
            jsonObj.put("jiaName", mJNEtX.getText().toString());
            jsonObj.put("jiaNSH", mJNshEt.getText().toString());
            jsonObj.put("jiaADD", mJAddEt.getText().toString());
            jsonObj.put("jiaPhone", mJPEt.getText().toString());
            jsonObj.put("jiaKHH", mJKHHEt.getText().toString());
            jsonObj.put("jiaKHCH", mJKHHHEt.getText().toString());
            jsonObj.put("YiName", mYNEt.getText().toString());
            jsonObj.put("YiNSH", mYNshEt.getText().toString());
            jsonObj.put("YiADD", mYAddEt.getText().toString());
            jsonObj.put("YiPhone", mYPEt.getText().toString());
            jsonObj.put("YiKHH", mYKHHEt.getText().toString());
            jsonObj.put("YiKHCH", mYKHHHEt.getText().toString());
            jsonObj.put("huowuInfo", jA);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObj;
    }
}
