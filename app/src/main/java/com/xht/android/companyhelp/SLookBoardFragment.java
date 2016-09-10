package com.xht.android.companyhelp;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.xht.android.companyhelp.model.Constants;
import com.xht.android.companyhelp.net.APIListener;
import com.xht.android.companyhelp.net.VolleyHelpApi;
import com.xht.android.companyhelp.util.LogHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SLookBoardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SLookBoardFragment extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static final String TAG = "SLookBoardFragment";
    ProgressDialog mProgDoal;
    GraphView mGraph;
    Spinner mCompNameSpinner, mWeiDuSpinner, mYearsSpinner, mMonthsSpinner;
    TextView mYearFeiYongTV, mYearFeiYong2TV, mSheBaoFeeTV, mSheBaoPNTV, mBaoSTV, mFaPFeeTV;
    LinearLayout mMingXi1, mMingXi2, mMingXi3;
    ServerLookBoardActivity mActivity;
    int mUId;
    String[] mWeiDus;   //维度种类
    String[] mYears;
    int mCurYear;   //当前年
    int mCurMonth;
    private int[] mCompIds;
    private String[] mCompNames;
    float[] graphBS;    //每个月或者季度的金额

    boolean mInitDataCompFlag;  //用于标识数据初始化完成，因为Spinner的onItemSelected最初会被调用

    public SLookBoardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SLookBoardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SLookBoardFragment newInstance(String param1, String param2) {
        SLookBoardFragment fragment = new SLookBoardFragment();
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
        mUId = mActivity.mUId;
        Resources res = getResources();
        mWeiDus = res.getStringArray(R.array.fwkb_wd);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_slook_board, container, false);
        mGraph = (GraphView) rootView.findViewById(R.id.graph);
        mCompNameSpinner = (Spinner) rootView.findViewById(R.id.spinner1);
        mWeiDuSpinner = (Spinner) rootView.findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> arrayAdapter2 = ArrayAdapter.createFromResource(getActivity(),
                R.array.fwkb_wd, android.R.layout.simple_spinner_item);
        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mWeiDuSpinner.setAdapter(arrayAdapter2);
        mYearsSpinner = (Spinner) rootView.findViewById(R.id.spinner3);
        mCompNameSpinner.setOnItemSelectedListener(this);
        mWeiDuSpinner.setOnItemSelectedListener(this);
        mYearsSpinner.setOnItemSelectedListener(this);
        mYearFeiYongTV = (TextView) rootView.findViewById(R.id.year_feiyong_tv);
        mMonthsSpinner = (Spinner) rootView.findViewById(R.id.spinner4);
        ArrayAdapter<CharSequence> arrayAdapter4 = ArrayAdapter.createFromResource(getActivity(),
                R.array.fwkb_months, android.R.layout.simple_spinner_item);
        arrayAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mMonthsSpinner.setAdapter(arrayAdapter4);
        mMingXi1 = (LinearLayout) rootView.findViewById(R.id.mingxi1);
        mMingXi2 = (LinearLayout) rootView.findViewById(R.id.mingxi2);
        mMingXi3 = (LinearLayout) rootView.findViewById(R.id.mingxi3);
        mMingXi1.setOnClickListener(this);
        mMingXi2.setOnClickListener(this);
        mMingXi3.setOnClickListener(this);
        mYearFeiYong2TV = (TextView) rootView.findViewById(R.id.year_feiyong_tv_t);
        mSheBaoFeeTV = (TextView) rootView.findViewById(R.id.shebaoFee);
        mSheBaoPNTV = (TextView) rootView.findViewById(R.id.shebaoPNum);
        mBaoSTV = (TextView) rootView.findViewById(R.id.baoshuiFee);
        mFaPFeeTV = (TextView) rootView.findViewById(R.id.fapiaoFee);
        getInitData();
        showLineGraph();
        return rootView;
    }

    private void getInitData() {
        createProgressDialog("获取数据中...");
        Calendar calendar = Calendar.getInstance();
        mCurYear = calendar.get(Calendar.YEAR);
        mCurMonth = calendar.get(Calendar.MONTH) + 1;
        int yearNums = mCurYear - 2016 + 1;
        mYears = new String[yearNums];
        for (int i = 0; i < yearNums; i++) {
            mYears[i] = "" + (2016 + i);
        }
        ArrayAdapter<CharSequence> arrayAdapter3 = new ArrayAdapter(getActivity(),
                android.R.layout.simple_spinner_item, mYears);
        arrayAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mYearsSpinner.setAdapter(arrayAdapter3);
        mYearsSpinner.setSelection(mCurYear - 2016);
        mMonthsSpinner.setSelection(mCurMonth - 1);
        LogHelper.i(TAG, "year-month=" + mCurYear + "-" + mCurMonth);
        VolleyHelpApi.getInstance().getInitDataSLB(mUId, mCurYear, mCurMonth, new APIListener() {
            @Override
            public void onResult(Object result) {
                JSONObject jsonObject = (JSONObject) result;
                try {
                    JSONObject compList = jsonObject.getJSONObject("companyList");
                    JSONArray compListJA = compList.getJSONArray("list");
                    int compListLength = compListJA.length();
                    mCompIds = new int[compListLength];
                    mCompNames = new String[compListLength];
                    for (int i = 0; i < compListLength; i++) {
                        JSONObject temp = compListJA.optJSONObject(i);
                        mCompIds[i] = temp.optInt("companyId");
                        mCompNames[i] = temp.optString("companyName");
                    }
                    compList = jsonObject.getJSONObject("baoshui");
                    compListJA = compList.getJSONArray("list5");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mInitDataCompFlag = true;
                ArrayAdapter<CharSequence> arrayAdapter;
                arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, mCompNames);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mCompNameSpinner.setAdapter(arrayAdapter);
                dismissProgressDialog();
            }

            @Override
            public void onError(Object e) {
                dismissProgressDialog();
                App.getInstance().showToast(e.toString());
                getActivity().finish();
            }
        });
    }

    private void createProgressDialog(String title) {
        if (mProgDoal == null) {
            mProgDoal = new ProgressDialog(getActivity());
        }
        mProgDoal.setTitle(title);
        mProgDoal.setIndeterminate(true);
        mProgDoal.setCancelable(false);
        mProgDoal.show();
    }

    /**
     * 隐藏mProgressDialog
     */
    private void dismissProgressDialog()
    {
        if(mProgDoal != null)
        {
            mProgDoal.dismiss();
            mProgDoal = null;
        }
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        mActivity = (ServerLookBoardActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void showLineGraph() {
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                //new DataPoint(0, 1),
                new DataPoint(1, 5985),
                new DataPoint(2, 3333),
                new DataPoint(3, 1456),
                new DataPoint(4, 6)
        });
        mGraph.addSeries(series);
        /*mGraph.setTitle("金额（元）/季度");
        mGraph.setTitleColor(Color.BLUE);
        mGraph.setTitleTextSize(18 * Constants.DENSITY);*/
        /*mGraph.getGridLabelRenderer().setVerticalAxisTitle("金额（元）");
        mGraph.getGridLabelRenderer().setVerticalAxisTitleTextSize(60);
        mGraph.getGridLabelRenderer().setVerticalAxisTitleColor(Color.BLUE);*/
        mGraph.getGridLabelRenderer().setHorizontalAxisTitle("季度");
        mGraph.getGridLabelRenderer().setHorizontalAxisTitleTextSize(18 * Constants.DENSITY);
        mGraph.getGridLabelRenderer().setHorizontalAxisTitleColor(Color.BLUE);
        mGraph.getViewport().setXAxisBoundsManual(true);
        mGraph.getViewport().setMinX(0.5);
        mGraph.getViewport().setMaxX(4.5);
        // legend
        series.setTitle("金额");
        mGraph.getLegendRenderer().setVisible(true);
        mGraph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
    }

    private void showBarGraph() {
        BarGraphSeries<DataPoint> series = new BarGraphSeries<DataPoint>(new DataPoint[] {
                //new DataPoint(0, 0),
                new DataPoint(1, 6432),
                new DataPoint(2, 3456),
                //new DataPoint(3, 4444),
                //new DataPoint(4, 3333),
                //new DataPoint(5, 0)
        });
        mGraph.addSeries(series);

        // styling
        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb((int) data.getX()*255/4, (int) Math.abs(data.getY()*255/6), 100);
            }
        });

        series.setSpacing(50);

        // draw values on top
        series.setDrawValuesOnTop(true);
        series.setValuesOnTopColor(Color.RED);
        //series.setValuesOnTopSize(50);
        mGraph.getGridLabelRenderer().setHorizontalAxisTitle("季度");
        mGraph.getGridLabelRenderer().setHorizontalAxisTitleTextSize(18 * Constants.DENSITY);
        mGraph.getGridLabelRenderer().setHorizontalAxisTitleColor(Color.BLUE);
        mGraph.getViewport().setXAxisBoundsManual(true);
        mGraph.getViewport().setMinX(0.5);
        mGraph.getViewport().setMaxX(4.5);
        // legend
        series.setTitle("金额");
        mGraph.getLegendRenderer().setVisible(true);
        mGraph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (!mInitDataCompFlag) return;
        switch (parent.getId()) {
            case R.id.spinner1:

                break;
            case R.id.spinner2:

                break;
            case R.id.spinner3:

                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mingxi1:
                showDialog(1);
                break;
            case R.id.mingxi2:

                break;
            case R.id.mingxi3:

                break;
        }
    }

    void showDialog(int which) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        switch (which) {
            case 1:
                // Create and show the dialog.
                DialogFragment newFragment = MyDialogFragment.newInstance(0);
                newFragment.show(ft, "dialog");
                break;
            case 2:

                break;
            case 3:

                break;
        }
    }
}
