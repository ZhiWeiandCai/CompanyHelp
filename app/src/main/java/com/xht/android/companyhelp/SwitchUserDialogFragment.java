package com.xht.android.companyhelp;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.xht.android.companyhelp.provider.MyDatabaseManager;
import com.xht.android.companyhelp.util.LogHelper;

/**
 * Created by Administrator on 2016/9/27.
 */
public class SwitchUserDialogFragment extends DialogFragment{
    int mUId;
    int mNum;
    Button mYesBtn, mNoBtn;
    MainActivity mActivity;

    /**
     * Create a new instance of MyDialogFragment, providing "num"
     * as an argument.
     */
    static SwitchUserDialogFragment newInstance(int uId, int num) {
        SwitchUserDialogFragment f = new SwitchUserDialogFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("uid", uId);
        args.putInt("num", num);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUId = getArguments().getInt("uid");
        mNum = getArguments().getInt("num");
        LogHelper.i("sfsg=", "-------------------" + mUId);
        int style = DialogFragment.STYLE_NO_TITLE, theme = 0;
        setStyle(style, theme);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_switch_user_dialog, container, false);
        mYesBtn = (Button) view.findViewById(R.id.yesBtn);
        mNoBtn = (Button) view.findViewById(R.id.noBtn);
        mYesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearUserDataFromDB();
                Intent intent = new Intent(MyFragment.BRO_ACT_S2);
                getActivity().sendBroadcast(intent);
                mActivity.switchToActivity(LoginActivity.class, null, 0, false, false);
                dismiss();
            }
        });
        mNoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        mActivity = (MainActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    void clearUserDataFromDB() {
        Cursor cursor = mActivity.getContentResolver().query(MyDatabaseManager.MyDbColumns.CONTENT_URI, null, null, null, null);
        if (cursor == null || cursor.getCount() == 0) {

            return;
        }
        cursor.moveToFirst();
        int uidIndex = cursor.getColumnIndex(MyDatabaseManager.MyDbColumns.UID);
        int userNameIndex = cursor.getColumnIndex(MyDatabaseManager.MyDbColumns.NAME);
        int phoneIndex = cursor.getColumnIndex(MyDatabaseManager.MyDbColumns.PHONE);
        if (cursor.getInt(uidIndex) == mUId) {
            LogHelper.i("sfsg2222=", "------------------" + mUId);
            mActivity.getContentResolver().delete(MyDatabaseManager.MyDbColumns.CONTENT_URI,
                    null, null);
        }
    }

}
