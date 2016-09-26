package com.xht.android.companyhelp;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.xht.android.companyhelp.model.BSMxXiang;
import com.xht.android.companyhelp.net.APIListener;
import com.xht.android.companyhelp.net.VolleyHelpApi;
import com.xht.android.companyhelp.util.LogHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Activities that contain this fragment must implement the
 * Use the {@link MyDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyDialogFragment2 extends DialogFragment {
    int mCompId;
    int mYear;
    int mNum;
    private ListView mListView;
    private ArrayList<BSMxXiang> mBSMxXiangs = new ArrayList<>();

    /**
     * Create a new instance of MyDialogFragment, providing "num"
     * as an argument.
     */
    static MyDialogFragment2 newInstance(int compId, int year, int month) {
        MyDialogFragment2 f = new MyDialogFragment2();

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
        View view = inflater.inflate(R.layout.fragment_2_my_dialog, container, false);
        mListView = (ListView) view.findViewById(R.id.sb_l_view);
        getSBPeople();

        return view;
    }

    private void getSBPeople() {
        VolleyHelpApi.getInstance().getBSMX(mCompId, mYear, mNum, new APIListener() {
            @Override
            public void onResult(Object result) {
                JSONObject tempJO = ((JSONObject) result).optJSONObject("shui");
                JSONArray tempJA = tempJO.optJSONArray("list").optJSONArray(0);

                if (tempJA != null) {
                    LogHelper.i("报税明细", tempJA.toString());
                    int len = tempJA.length();
                    double tempD;
                    try {
                        tempD = tempJA.getDouble(1);
                        BSMxXiang temp = new BSMxXiang();
                        temp.setmMXXiang("增值税");
                        temp.setmFee(tempD);
                        mBSMxXiangs.add(temp);
                        tempD = tempJA.getDouble(3);
                        BSMxXiang temp2 = new BSMxXiang();
                        temp2.setmMXXiang("企业所得税");
                        temp2.setmFee(tempD);
                        mBSMxXiangs.add(temp2);
                        tempD = tempJA.getDouble(5);
                        BSMxXiang temp3 = new BSMxXiang();
                        temp3.setmMXXiang("文化建设税");
                        temp3.setmFee(tempD);
                        mBSMxXiangs.add(temp3);
                        tempD = tempJA.getDouble(7);
                        BSMxXiang temp4 = new BSMxXiang();
                        temp4.setmMXXiang("个税");
                        temp4.setmFee(tempD);
                        mBSMxXiangs.add(temp4);
                        tempD = tempJA.getDouble(9);
                        BSMxXiang temp5 = new BSMxXiang();
                        temp5.setmMXXiang("通用申报");
                        temp5.setmFee(tempD);
                        mBSMxXiangs.add(temp5);
                        tempD = tempJA.getDouble(11);
                        BSMxXiang temp6 = new BSMxXiang();
                        temp6.setmMXXiang("收入");
                        temp6.setmFee(tempD);
                        mBSMxXiangs.add(temp6);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    SBAdapter adapter = new SBAdapter(mBSMxXiangs);
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

    private class SBAdapter extends ArrayAdapter<BSMxXiang> {

        public SBAdapter(ArrayList<BSMxXiang> items) {
            super(getActivity(), 0, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.bsmx_item, parent, false);
                holder = new ViewHolder();
                holder.title = (TextView) convertView.findViewById(R.id.tv1);
                holder.fee = (TextView) convertView.findViewById(R.id.tv2);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            BSMxXiang item = getItem(position);
            holder.title.setText(item.getmMXXiang());
            holder.fee.setText("" + item.getmFee());
            return convertView;
        }
    }

    class ViewHolder {
        public TextView title;
        public TextView fee;
    }
}
