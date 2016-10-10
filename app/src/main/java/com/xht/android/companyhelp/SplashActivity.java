package com.xht.android.companyhelp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.xht.android.companyhelp.net.APIListener;
import com.xht.android.companyhelp.net.VolleyHelpApi;
import com.xht.android.companyhelp.util.AppInfoUtils;
import com.xht.android.companyhelp.util.LogHelper;

import org.json.JSONObject;

import java.io.File;

/**
 * Created by Administrator on 2016-9-29.
 */
public class SplashActivity extends Activity {
    private static final String TAG = "SplashActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spash_activity);

        ImageView spashImg= (ImageView) findViewById(R.id.spash_img);

        //开启动画
        AnimationSet set=new AnimationSet(false);

        /**
         * 透明渐变色
         */
        AlphaAnimation mAlphaAnimation=new AlphaAnimation(0, 1.0f);
        mAlphaAnimation.setDuration(2000);



        //添加动画
        set.addAnimation(mAlphaAnimation);

        /**
         * 开启动画
         */
        spashImg.startAnimation(set);
        set.setAnimationListener(new Animation.AnimationListener() {
            //动画开始时执行此方法
            @Override
            public void onAnimationStart(Animation animation) {

            }
            //动画重复执行此方法
            @Override
            public void onAnimationRepeat(Animation animation) {

            }
            //动画结束执行此方法
            @Override
            public void onAnimationEnd(Animation animation) {


                //检查版本更新
                checkVersion();

            }
        });
    }

    private void checkVersion() {
        VolleyHelpApi.getInstance().getCheckVersion(new APIListener() {

            @Override
            public void onResult(Object result) {
                LogHelper.i(TAG, "---版本更新的所有信息--" + result.toString());

                //获取当前版本号
                String appInfoName = AppInfoUtils.getAppInfoName(SplashActivity.this);
                int appInfoNumber = AppInfoUtils.getAppInfoNumber(SplashActivity.this);
                LogHelper.i(TAG,"---有新版本，下载更新"+appInfoName+"-"+appInfoNumber);


                JSONObject mJsonVersion= (JSONObject) result;
                String versionNum = mJsonVersion.optString("version");
                //服务器中的版本号
                Double versionNew=Double.parseDouble(versionNum);
                String downloadUrl = mJsonVersion.optString("downloadUrl");

                if (versionNew>appInfoNumber){
                    versionNew=versionNew/10.0;
                    LogHelper.i(TAG,"---versionNum：="+versionNew+"---"+downloadUrl);
                    LogHelper.i(TAG,"---有新版本，下载更新");
                    showDialogUpdate(versionNew+"","新的版本，修复文章bug",downloadUrl);
                }else{
                    startActivity(new Intent(SplashActivity.this,MainActivity.class));
                    finish();
                }
            }
            @Override
            public void onError(Object e) {

                startActivity(new Intent(SplashActivity.this,MainActivity.class));
                finish();
            }
        });
    }

    //弹出更新对话框showDialogUpdate("发现新版本:,请确认更新下载安装","更新日志：")
    private void showDialogUpdate(String versionName, String desc, final String downLoadUrl) {
        //弹出一个更新对话框
        final AlertDialog.Builder builder=new AlertDialog.Builder(SplashActivity.this);
        builder.setTitle("发现新版本:"+versionName+",请确认下载升级");
        builder.setMessage("更新日志："+desc);
        builder.setPositiveButton("立刻升级", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                App.getInstance().showToast("请稍等片刻...");
                //开源项目断点下载xUtils
                HttpUtils http=new HttpUtils();
                final File file=new File(Environment.getExternalStorageDirectory(),"xiaohoutai.apk");
                http.download(downLoadUrl, file.getAbsolutePath(), true, new RequestCallBack<File>(){
                    //下载失败
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {


                    }
                    //下载成功
                    @Override
                    public void onSuccess(ResponseInfo arg0) {
                        //下载成功，替换安装
                        App.getInstance().showToast("下载成功");
                        Intent intent=new Intent();
                        intent.setAction("android.intent.action.VIEW");
                        intent.addCategory("android.intent.category.DEFAULT");
                        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                        startActivity(intent);

                    }});
                dialog.dismiss();
                startActivity(new Intent(SplashActivity.this,MainActivity.class));
                finish();

            }
        });
        builder.setNegativeButton("下次再说", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(SplashActivity.this,MainActivity.class));
                finish();
                dialog.dismiss();
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

}
