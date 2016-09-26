package com.xht.android.companyhelp;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
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

    Spinner mCompNameSpinner, mWeiDuSpinner, mYearsSpinner, mMonthsSpinner, mSpinnerX2, mSpinnerX3;
    TextView mYearFeiYongTV, mYearFeiYong2TV, mSheBaoFeeTV, mSheBaoPNTV, mBaoSTV, mFaPFeeTV, mJXSE;
    LinearLayout mMingXi1, mMingXi2, mMingXi3;
    ServerLookBoardActivity mActivity;
    int mUId;
    String[] mWeiDus;   //维度种类
    String[] mYears;
    int mCurYear;   //当前年
    int mCurMonth;
    private int[] mCompIds;
    private String[] mCompNames;
    private int mFlagCompP; //标记某公司的是小规模还是一般纳税人
    double[] graphBS;    //每个月或者季度的金额
    double[] mJinXiangShui;
    double[] mKPSES;
    double[] mSheBaoMonth = new double[12];
    int[] mSheBaoMP = new int[12];
    ArrayAdapter<CharSequence> mJiDiAAdapter;   //季度的Adapter
    ArrayAdapter<CharSequence> arrayAdapter4;   //月份的Adapter
    double mYearFee;   //本年总费用
    double mYShuiE;     //本年税额
    double mYSheBao;    //本年社保

    private static final String[] mKeyBSMonth = {"list1", "list2", "list3", "list4", "list5", "list6",
        "list7", "list8", "list9", "list10", "list11", "list12"};
    boolean mInitDataCompFlag;  //用于标识数据初始化完成，因为Spinner的onItemSelected最初会被调用

    GraphView mGraph;
    LineGraphSeries<DataPoint> series;
    Handler mHandler = new Handler();

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
        arrayAdapter4 = ArrayAdapter.createFromResource(getActivity(),
                R.array.fwkb_months, android.R.layout.simple_spinner_item);
        arrayAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mMonthsSpinner.setAdapter(arrayAdapter4);
        mMonthsSpinner.setOnItemSelectedListener(this);
        mJiDiAAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.fwkb_jds, android.R.layout.simple_spinner_item);
        mJiDiAAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //报税金额的选择spinner
        mSpinnerX2 = (Spinner) rootView.findViewById(R.id.spinner_x2);
        mSpinnerX2.setOnItemSelectedListener(this);
        //发票金额的选择spinner
        mSpinnerX3 = (Spinner) rootView.findViewById(R.id.spinner_x3);
        mSpinnerX3.setOnItemSelectedListener(this);
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
        mJXSE = (TextView) rootView.findViewById(R.id.jxseFee);
        getInitData();
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
                    JSONArray compListJA = compList.optJSONArray("list");
                    if (compListJA == null) {
                        dismissProgressDialog();
                        App.getInstance().showToast("没有公司数据");
                        getActivity().finish();
                        return;
                    }
                    int compListLength = compListJA.length();
                    mCompIds = new int[compListLength];
                    mCompNames = new String[compListLength];
                    for (int i = 0; i < compListLength; i++) {
                        JSONObject temp = compListJA.optJSONObject(i);
                        mCompIds[i] = temp.optInt("companyId");
                        mCompNames[i] = temp.optString("companyName");
                    }
                    JSONObject tempKPSE = jsonObject.getJSONObject("kaiPiaoShui");
                    compList = jsonObject.getJSONObject("baoshui");
                    compListJA = compList.optJSONArray("list5");
                    if (compListJA == null) {
                        mFlagCompP = 0;
                        graphBS = new double[4];
                        mKPSES = new double[4];
                        for (int i = 0; i < 4; i++) {
                            graphBS[i] = compList.optJSONArray(mKeyBSMonth[i]).optDouble(0, 0);
                            mYShuiE += graphBS[i];
                            mKPSES[i] = tempKPSE.optJSONArray(mKeyBSMonth[i]).optDouble(0, 0);
                        }

                        mSpinnerX2.setAdapter(mJiDiAAdapter);
                        mSpinnerX3.setAdapter(mJiDiAAdapter);
                        if (1 <= mCurMonth && mCurMonth <= 3) {
                            mSpinnerX2.setSelection(0);
                            reFleshUIMonthBS(0);
                            mSpinnerX3.setSelection(0);
                            reFleshUIMonthFP(0);
                        } else if (4 <= mCurMonth && mCurMonth <= 6) {
                            mSpinnerX2.setSelection(1);
                            reFleshUIMonthBS(1);
                            mSpinnerX3.setSelection(1);
                            reFleshUIMonthFP(1);
                        } else if (7 <= mCurMonth && mCurMonth <= 9) {
                            mSpinnerX2.setSelection(2);
                            reFleshUIMonthBS(2);
                            mSpinnerX3.setSelection(2);
                            reFleshUIMonthFP(2);
                        } else if (10 <= mCurMonth && mCurMonth <= 12) {
                            mSpinnerX2.setSelection(3);
                            reFleshUIMonthBS(3);
                            mSpinnerX3.setSelection(3);
                            reFleshUIMonthFP(3);
                        }

                    } else {
                        mFlagCompP = 1;
                        JSONObject temp = jsonObject.getJSONObject("jinXiangshui");
                        mKPSES = new double[12];
                        graphBS = new double[12];
                        mJinXiangShui = new double[12];
                        for (int i = 0; i < 12; i++) {
                            graphBS[i] = compList.optJSONArray(mKeyBSMonth[i]).optDouble(0, 0);
                            mJinXiangShui[i] = temp.optJSONArray(mKeyBSMonth[i]).optDouble(0, 0);
                            mYShuiE += graphBS[i];
                            mKPSES[i] = tempKPSE.optJSONArray(mKeyBSMonth[i]).optDouble(0, 0);
                        }
                        mSpinnerX2.setAdapter(arrayAdapter4);
                        mSpinnerX2.setSelection(mCurMonth - 1);
                        reFleshUIMonthBS(mCurMonth - 1);
                        mSpinnerX3.setAdapter(arrayAdapter4);
                        mSpinnerX3.setSelection(mCurMonth - 1);
                        reFleshUIMonthFP(mCurMonth - 1);
                    }
                    compList = jsonObject.getJSONObject("yuFenShebao");
                    for (int i = 0; i < 12; i++) {
                        mSheBaoMonth[i] = compList.optJSONArray(mKeyBSMonth[i]).getJSONArray(0).optDouble(1, 0);
                        mSheBaoMP[i] = compList.optJSONArray(mKeyBSMonth[i]).getJSONArray(0).optInt(0, 0);
                        mYSheBao += mSheBaoMonth[i];
                    }
                    mYearFee = jsonObject.getJSONObject("yearShui").optJSONArray("list").getJSONObject(0).getDouble("sum");
                    LogHelper.i(TAG, "mYearFee=" + mYearFee + "---------------------------");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                ArrayAdapter<CharSequence> arrayAdapter;
                arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, mCompNames);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mCompNameSpinner.setAdapter(arrayAdapter);
                showLineGraph(graphBS);
                reFleshUI();
                reFleshUI(mCurMonth - 1);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dismissProgressDialog();
                        mInitDataCompFlag = true;
                    }
                }, 300);
            }

            @Override
            public void onError(Object e) {
                dismissProgressDialog();
                App.getInstance().showToast(e.toString());
                getActivity().finish();
            }
        });
    }

    /**
     * 刷新年-费用-税额-社保
     */
    private void reFleshUI() {
        mYearFeiYongTV.setText(String.format(getResources().getString(R.string.heji_yuanjiaofen), mYearFee));
        mYearFeiYong2TV.setText(String.format(getResources().getString(R.string.heji_yuanjiaofen), mYShuiE)
                + "/" + String.format(getResources().getString(R.string.heji_yuanjiaofen), mYSheBao));
    }

    /**
     * 刷新-社保-每月
     */
    private void reFleshUI(int which) {
        mSheBaoFeeTV.setText(String.format(getResources().getString(R.string.heji_yuanjiaofen), mSheBaoMonth[which]));
        mSheBaoPNTV.setText("" + mSheBaoMP[which]);
    }

    /**
     * 刷新-报税-每月
     */
    private void reFleshUIMonthBS(int which) {
        mBaoSTV.setText(String.format(getResources().getString(R.string.heji_yuanjiaofen), graphBS[which]));
    }

    /**
     * 刷新-发票税额and进项税额-每月
     */
    private void reFleshUIMonthFP(int which) {
        if (mFlagCompP == 1) {
            mJXSE.setText(String.format(getResources().getString(R.string.heji_yuanjiaofen), mJinXiangShui[which]));
        } else {
            mJXSE.setText(String.format(getResources().getString(R.string.heji_yuanjiaofen), 0f));
        }
        mFaPFeeTV.setText(String.format(getResources().getString(R.string.heji_yuanjiaofen), mKPSES[which]));
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

    private void showLineGraph(double[] datas) {
        if (datas == null) {
            return;
        }
        int length = datas.length;
        DataPoint[] values;
        if (length == 4) {
            values = new DataPoint[]{
                    new DataPoint(1, datas[0]),
                    new DataPoint(2, datas[1]),
                    new DataPoint(3, datas[2]),
                    new DataPoint(4, datas[3])
            };
            series = new LineGraphSeries<>(values);
            mGraph.getGridLabelRenderer().setHorizontalAxisTitle("季度");
            mGraph.getViewport().setXAxisBoundsManual(true);
            mGraph.getViewport().setMinX(0.5);
            mGraph.getViewport().setMaxX(4.5);
        } else {
            values = new DataPoint[]{
                    new DataPoint(1, datas[0]),
                    new DataPoint(2, datas[1]),
                    new DataPoint(3, datas[2]),
                    new DataPoint(4, datas[3]),
                    new DataPoint(5, datas[4]),
                    new DataPoint(6, datas[5]),
                    new DataPoint(7, datas[6]),
                    new DataPoint(8, datas[7]),
                    new DataPoint(9, datas[8]),
                    new DataPoint(10, datas[9]),
                    new DataPoint(11, datas[10]),
                    new DataPoint(12, datas[11])
            };
            series = new LineGraphSeries<>(values);
            mGraph.getGridLabelRenderer().setHorizontalAxisTitle("月份");
            mGraph.getViewport().setXAxisBoundsManual(true);
            mGraph.getViewport().setMinX(1);
            mGraph.getViewport().setMaxX(12);
        }
        mGraph.addSeries(series);
        /*mGraph.setTitle("金额（元）/季度");
        mGraph.setTitleColor(Color.BLUE);
        mGraph.setTitleTextSize(18 * Constants.DENSITY);*/
        /*mGraph.getGridLabelRenderer().setVerticalAxisTitle("金额（元）");
        mGraph.getGridLabelRenderer().setVerticalAxisTitleTextSize(60);
        mGraph.getGridLabelRenderer().setVerticalAxisTitleColor(Color.BLUE);*/

        mGraph.getGridLabelRenderer().setHorizontalAxisTitleTextSize(18 * Constants.DENSITY);
        mGraph.getGridLabelRenderer().setHorizontalAxisTitleColor(Color.BLUE);

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
                switchData(mCompIds[position], mCurYear, mCurMonth);
                break;
            case R.id.spinner2:
                if (position == 0) {

                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showLineGraphS2(genelateDataP(graphBS));
                        }
                    }, 300);
                } else if (position == 1) {
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showLineGraphS2(genelateDataP(mSheBaoMonth));
                        }
                    }, 300);

                }
                break;
            case R.id.spinner3:
                mCurYear = Integer.parseInt((String) mYearsSpinner.getSelectedItem());
                switchData(mCompIds[mCompNameSpinner.getSelectedItemPosition()], mCurYear, mCurMonth);
                break;
            case R.id.spinner4:
                reFleshUI(position);
                break;
            case R.id.spinner_x2:
                reFleshUIMonthBS(position);
                break;
        }
    }

    /**
     * 选择年份或公司时
     */
    private void switchData(int compId, int year, int yue) {
        mYShuiE = 0;
        mYSheBao = 0;
        VolleyHelpApi.getInstance().getDataSLB(compId, year, yue, new APIListener() {
            @Override
            public void onResult(Object result) {
                JSONObject jsonObject = (JSONObject) result;
                JSONObject compList;
                JSONArray compListJA;
                try {
                    JSONObject tempKPSE = jsonObject.getJSONObject("kaiPiaoshui");
                    compList = jsonObject.getJSONObject("baoshui");
                    compListJA = compList.optJSONArray("list5");
                    if (compListJA == null) {
                        graphBS = new double[4];
                        mKPSES = new double[4];
                        for (int i = 0; i < 4; i++) {
                            graphBS[i] = compList.optJSONArray(mKeyBSMonth[i]).optDouble(0, 0);
                            mYShuiE += graphBS[i];
                            mKPSES[i] = tempKPSE.optJSONArray(mKeyBSMonth[i]).optDouble(0, 0);
                        }
                        mSpinnerX2.setAdapter(mJiDiAAdapter);
                        mSpinnerX3.setAdapter(mJiDiAAdapter);
                        if (1 <= mCurMonth && mCurMonth <= 3) {
                            mSpinnerX2.setSelection(0);
                            reFleshUIMonthBS(0);
                            mSpinnerX3.setSelection(0);
                            reFleshUIMonthFP(0);
                        } else if (4 <= mCurMonth && mCurMonth <= 6) {
                            mSpinnerX2.setSelection(1);
                            reFleshUIMonthBS(1);
                            mSpinnerX3.setSelection(1);
                            reFleshUIMonthFP(1);
                        } else if (7 <= mCurMonth && mCurMonth <= 9) {
                            mSpinnerX2.setSelection(2);
                            reFleshUIMonthBS(2);
                            mSpinnerX3.setSelection(2);
                            reFleshUIMonthFP(2);
                        } else if (10 <= mCurMonth && mCurMonth <= 12) {
                            mSpinnerX2.setSelection(3);
                            reFleshUIMonthBS(3);
                            mSpinnerX3.setSelection(3);
                            reFleshUIMonthFP(3);
                        }

                    } else {
                        JSONObject temp = jsonObject.getJSONObject("jinXiangshui");
                        mKPSES = new double[12];
                        graphBS = new double[12];
                        mJinXiangShui = new double[12];
                        for (int i = 0; i < 12; i++) {
                            graphBS[i] = compList.optJSONArray(mKeyBSMonth[i]).optDouble(0, 0);
                            mJinXiangShui[i] = temp.optJSONArray(mKeyBSMonth[i]).optDouble(0, 0);
                            mYShuiE += graphBS[i];
                            mKPSES[i] = tempKPSE.optJSONArray(mKeyBSMonth[i]).optDouble(0, 0);
                        }
                        mSpinnerX2.setAdapter(arrayAdapter4);
                        mSpinnerX2.setSelection(mCurMonth - 1);
                        reFleshUIMonthBS(mCurMonth - 1);
                        mSpinnerX3.setAdapter(arrayAdapter4);
                        mSpinnerX3.setSelection(mCurMonth - 1);
                        reFleshUIMonthFP(mCurMonth - 1);
                    }
                    compList = jsonObject.getJSONObject("shebao");
                    for (int i = 0; i < 12; i++) {
                        mSheBaoMonth[i] = compList.optJSONArray(mKeyBSMonth[i]).getJSONArray(0).optDouble(1, 0);
                        mSheBaoMP[i] = compList.optJSONArray(mKeyBSMonth[i]).getJSONArray(0).optInt(0, 0);
                        mYSheBao += mSheBaoMonth[i];
                    }
                    mYearFee = jsonObject.getJSONObject("yearShui").optJSONArray("list").getJSONObject(0).getDouble("sum");
                    LogHelper.i(TAG, "mYearFee=" + mYearFee + "---------------------------");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (mWeiDuSpinner.getSelectedItemPosition() == 0) {
                    showLineGraphS2(genelateDataP(graphBS));
                } else if (mWeiDuSpinner.getSelectedItemPosition() == 1) {
                    showLineGraphS2(genelateDataP(mSheBaoMonth));
                }
                mMonthsSpinner.setSelection(mCurMonth - 1);
                reFleshUI();
                reFleshUI(mCurMonth - 1);
                dismissProgressDialog();
            }

            @Override
            public void onError(Object e) {
                dismissProgressDialog();
                App.getInstance().showToast(e.toString());
            }
        });
    }

    private void showLineGraphS2(DataPoint[] datas) {
        int length = datas.length;
        if (length == 4) {
            series.resetData(datas);
            mGraph.getGridLabelRenderer().setHorizontalAxisTitle("季度");
            mGraph.getViewport().setMinX(0.5);
            mGraph.getViewport().setMaxX(4.5);
        } else {
            mGraph.getGridLabelRenderer().setHorizontalAxisTitle("月份");
            mGraph.getViewport().setMinX(1);
            mGraph.getViewport().setMaxX(12);
            series.resetData(datas);


            LogHelper.i(TAG, "--------resetData 后--------");

        }
        LogHelper.i(TAG, "--------invalidate 前--------");
//        mGraph.invalidate();
    }

    private DataPoint[] genelateDataP(double[] datas) {
        int length = datas.length;
        DataPoint[] values;
        if (length == 4) {
            values = new DataPoint[] {
                    new DataPoint(1, datas[0]),
                    new DataPoint(2, datas[1]),
                    new DataPoint(3, datas[2]),
                    new DataPoint(4, datas[3])
            };
        } else  {
            values = new DataPoint[] {
                    new DataPoint(1, datas[0]),
                    new DataPoint(2, datas[1]),
                    new DataPoint(3, datas[2]),
                    new DataPoint(4, datas[3]),
                    new DataPoint(5, datas[4]),
                    new DataPoint(6, datas[5]),
                    new DataPoint(7, datas[6]),
                    new DataPoint(8, datas[7]),
                    new DataPoint(9, datas[8]),
                    new DataPoint(10, datas[9]),
                    new DataPoint(11, datas[10]),
                    new DataPoint(12, datas[11])
            };
        }
        return values;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mingxi1:
                showDialog(1, mCompIds[mCompNameSpinner.getSelectedItemPosition()],
                        mCurYear, mMonthsSpinner.getSelectedItemPosition() + 1);
                break;
            case R.id.mingxi2:
                showDialog(2, mCompIds[mCompNameSpinner.getSelectedItemPosition()],
                        mCurYear, mSpinnerX2.getSelectedItemPosition() + 1);
                break;
            case R.id.mingxi3:

                break;
        }
    }

    void showDialog(int which, int cid, int year, int month) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        switch (which) {
            case 1:
                // Create and show the dialog.
                DialogFragment newFragment = MyDialogFragment.newInstance(cid,year,month);
                newFragment.show(ft, "dialog");
                break;
            case 2:
                DialogFragment newFragment2 = MyDialogFragment2.newInstance(cid,year,month);
                newFragment2.show(ft, "dialog2");
                break;
            case 3:

                break;
        }
    }
}
