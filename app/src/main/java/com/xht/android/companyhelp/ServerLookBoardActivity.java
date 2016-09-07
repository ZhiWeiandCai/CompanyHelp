package com.xht.android.companyhelp;

import android.os.Bundle;
import android.app.Activity;

import com.xht.android.companyhelp.util.LogHelper;

public class ServerLookBoardActivity extends Activity {

    private static final String TAG = "ServerLookBroadActivity";
    public int mUId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        mUId = bundle.getInt("uid");
        LogHelper.i(TAG, "mUid=" + mUId);
        setContentView(R.layout.activity_server_look_board);

    }

}
