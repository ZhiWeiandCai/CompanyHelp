package com.xht.android.companyhelp;

import android.os.Bundle;
import android.app.Activity;

public class ServerLookBoardActivity extends Activity {

    private static final String TAG = "ServerLookBroadActivity";
    public int mUId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_look_board);
        Bundle bundle = getIntent().getExtras();
        mUId = bundle.getInt("uid");
    }

}
