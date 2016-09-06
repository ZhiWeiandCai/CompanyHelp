package com.xht.android.companyhelp;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SLookBoardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SLookBoardFragment extends Fragment {
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
    ServerLookBoardActivity mActivity;
    int mUId;
    String[] mWeiDus;   //维度种类
    String[] mYears;

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
        getInitData();
        showBarGraph();
        return rootView;
    }

    private void getInitData() {
        createProgressDialog("获取数据中...");
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        LogHelper.i(TAG, "year-month=" + year + "-" + month);
        VolleyHelpApi.getInstance().getInitDataSLB(mUId, new APIListener() {
            @Override
            public void onResult(Object result) {
                dismissProgressDialog();
            }

            @Override
            public void onError(Object e) {
                dismissProgressDialog();
                App.getInstance().showToast(e.toString());
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
                new DataPoint(0, 1),
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
        mGraph.getGridLabelRenderer().setHorizontalAxisTitle("季度");
        mGraph.getGridLabelRenderer().setVerticalAxisTitleTextSize(60);
        mGraph.getGridLabelRenderer().setVerticalAxisTitleColor(Color.BLUE);
        mGraph.getGridLabelRenderer().setHorizontalAxisTitleTextSize(60);
        mGraph.getGridLabelRenderer().setHorizontalAxisTitleColor(Color.BLUE);*/
        mGraph.getViewport().setXAxisBoundsManual(true);
        mGraph.getViewport().setMinX(0);
        mGraph.getViewport().setMaxX(4);
        // legend
//        series.setTitle("foo");
//        graph.getLegendRenderer().setVisible(true);
//        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.BOTTOM);
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

}
