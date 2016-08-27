package com.xht.android.companyhelp;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.xht.android.companyhelp.util.LogHelper;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FaPiao3Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FaPiao3Fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3= "param3";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private int[] mParam2;
    private String[] mParam3;

    private EditText mCompNameET;
    private Spinner mJNEt;
    private int mPosiFlag;

    public FaPiao3Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FaPiao3Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FaPiao3Fragment newInstance(String param1, int[] param2, String[] param3) {
        FaPiao3Fragment fragment = new FaPiao3Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putIntArray(ARG_PARAM2, param2);
        args.putStringArray(ARG_PARAM3, param3);
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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fa_piao3, container, false);
        mCompNameET = (EditText) view.findViewById(R.id.jia_c_et_x);
        mJNEt = (Spinner) view.findViewById(R.id.jia_c_et);
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
                    updateView();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });
        if (mParam3 == null)
            mJNEt.setVisibility(View.GONE);
        return view;
    }

    private void updateView() {
        mCompNameET.setText(mParam3[mPosiFlag]);
    }

    public JSONObject postJsonData() {
        if (mCompNameET.getText() == null || mCompNameET.getText().toString().isEmpty()) {
            App.getInstance().showToast("请把信息填写完整...");
            return null;
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("companyName", mCompNameET.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

}
