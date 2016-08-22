package com.xht.android.companyhelp;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.xht.android.companyhelp.model.PersonOfSheBao;
import com.xht.android.companyhelp.net.APIListener;
import com.xht.android.companyhelp.net.VolleyHelpApi;
import com.xht.android.companyhelp.util.LogHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/16.
 * 社保服务
 */
public class SheBaoSActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "SheBaoSActivity";

    private int mUId;
    private ArrayList<PersonOfSheBao> mArrayList = new ArrayList<>();
    private PersonOfSheBaoAdapter mPersonOfSheBaoAdapter;
    private final int mItemRadioR1 = R.id.radioR1;
    private final int mItemRadioR2 = R.id.radioR2;
    private TextWatcher mTWatch1;   //监听姓名EditText
    private TextWatcher mTWatch2;   //监听身份证号EditText
    int index = -1;  //触摸listview的某项的索引
    private int mPrice;
    private int[] mCompIds;
    private String[] mCompNames;
    private int mSelectedCompId;

    private ProgressDialog mProgDoal;
    private Spinner mCompNameSpinner;
    private ImageButton mAddItemIBtn;
    private ListView mListView;
    private TextView mMHeJiTV;
    private Button mBookYuYue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shebaos);
        Bundle bundle = getIntent().getBundleExtra("uData");
        mUId = bundle.getInt("uid");
        TextView mCustomView = new TextView(this);
        mCustomView.setGravity(Gravity.CENTER);
        mCustomView.setText("下单预约-社保服务");
        final ActionBar aBar = getActionBar();
        aBar.setCustomView(mCustomView,
                new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        int change = ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_CUSTOM;
        aBar.setDisplayOptions(change);
        mArrayList.add(new PersonOfSheBao());
        initView();
        mAddItemIBtn.setOnClickListener(this);
        mPersonOfSheBaoAdapter = new PersonOfSheBaoAdapter(mArrayList);
        mListView.setAdapter(mPersonOfSheBaoAdapter);
        getComListAndJiaGe(mUId);
    }

    private void initView() {
        mCompNameSpinner = (Spinner) findViewById(R.id.spinner1);
        mAddItemIBtn = (ImageButton) findViewById(R.id.add_item);
        mListView = (ListView) findViewById(R.id.list_view);
        mMHeJiTV = (TextView) findViewById(R.id.shu_heji);
        mBookYuYue = (Button) findViewById(R.id.bookYuQue);
        mBookYuYue.setOnClickListener(this);
        mCompNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LogHelper.i("单击了某些选项", "position=" + position + "--gongsi=" + mCompNames[position]);
                mSelectedCompId = mCompIds[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 根据用户id获取公司列表和社保价格
     */
    private void getComListAndJiaGe(int uid) {
        createProgressDialog("价格获取中...");
        VolleyHelpApi.getInstance().getComListAndJiaGeofYeWu(uid, new APIListener() {
            @Override
            public void onResult(Object result) {
                JSONObject jiageJO;
                JSONArray companyJA;
                try {
                    jiageJO = ((JSONObject) result).getJSONObject("price");
                    mPrice = jiageJO.optInt("SheBao");
                    companyJA = ((JSONObject) result).optJSONArray("companyName");

                    int compJALength = companyJA.length();
                    mCompIds = new int[compJALength];
                    mCompNames = new String[compJALength];
                    for (int i = 0; i < compJALength; i++) {
                        JSONObject temp = companyJA.optJSONObject(i);
                        mCompIds[i] = temp.optInt("id");
                        mCompNames[i] = temp.optString("name");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dismissProgressDialog();
                mSelectedCompId = mCompIds[0];
                refleshJiaGeView();
            }

            @Override
            public void onError(Object e) {
                dismissProgressDialog();
                App.getInstance().showToast(e.toString());
                finish();
            }
        });
    }

    private void refleshJiaGeView() {
        mMHeJiTV.setText(String.format(getResources().getString(R.string.heji_yuanjiaofen), mPrice / 100.0f));
        ArrayAdapter<CharSequence> arrayAdapter = new ArrayAdapter<CharSequence>(SheBaoSActivity.this, android.R.layout.simple_spinner_item, mCompNames);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCompNameSpinner.setAdapter(arrayAdapter);
    }

    private void createProgressDialog(String title) {
        if (mProgDoal == null) {
            mProgDoal = new ProgressDialog(this);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_item:
                if (mArrayList.size() < 10) {
                    mArrayList.add(new PersonOfSheBao());
                    mPersonOfSheBaoAdapter.notifyDataSetChanged();
                } else {
                    App.getInstance().showToast("一次最多只能操作10个员工");
                }
                break;
            case R.id.bookYuQue:
                checkInfoComplete();
                break;
        }
    }

    /**
     * 检测所填信息完整性
     */
    private void checkInfoComplete() {
        for (PersonOfSheBao temp : mArrayList) {
            if (temp.getmName() == null || temp.getmName() == "" || temp.getmIdCard() == null || temp.getmIdCard() == "") {
                App.getInstance().showToast("请把信息填写完整...");
                return;
            }
        }
        postBookList();
    }

    /**
     * 获取用户所填的资料，封装成json
     */
    private void postBookList() {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("userId", mUId);
            jsonObj.put("CompanyId", mSelectedCompId);
            jsonObj.put("price", mPrice);
            JSONArray jA = new JSONArray();
            for (PersonOfSheBao temp : mArrayList) {
                JSONObject jo = new JSONObject();
                jo.put("yOrn", temp.isCheck());
                jo.put("peopleName", temp.getmName());
                jo.put("idcard", temp.getmIdCard());
                jA.put(jo);
            }
            jsonObj.put("peopleInfo", jA);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogHelper.i("打印社保服务的员工json--", jsonObj.toString());
        createProgressDialog("订单提交中...");
        VolleyHelpApi.getInstance().postDingDanSheBao(mUId, jsonObj, new APIListener() {
            @Override
            public void onResult(Object result) {
                LogHelper.i("订单提交成功", "2016-08-16");
                dismissProgressDialog();
                Bundle bundle = new Bundle();
                JSONObject tempJO = ((JSONObject) result).optJSONObject("entity");
                bundle.putString("shangpin", "社保服务");
                bundle.putString("bookListId", tempJO.optString("orderId"));
                bundle.putFloat("pay_money", mPrice);
                Intent intent = new Intent(SheBaoSActivity.this, PayOptActivity.class);
                intent.putExtra("booklistdata", bundle);
                SheBaoSActivity.this.startActivity(intent);
                SheBaoSActivity.this.finish();
            }

            @Override
            public void onError(Object e) {
                dismissProgressDialog();
                App.getInstance().showToast(e.toString());
            }
        });
    }

    private class PersonOfSheBaoAdapter extends ArrayAdapter<PersonOfSheBao> {

        public PersonOfSheBaoAdapter(ArrayList<PersonOfSheBao> items) {
            super(SheBaoSActivity.this, 0, items);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.personshebao_item, parent, false);
                holder = new ViewHolder();
                holder.rGroup = (RadioGroup) convertView.findViewById(R.id.rg1);
                holder.iButton = (ImageButton) convertView.findViewById(R.id.jian_item_btn);
                holder.et1 = (EditText) convertView.findViewById(R.id.shebaoren_name);
                holder.et2 = (EditText) convertView.findViewById(R.id.shebaoren_idcard);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            PersonOfSheBao item = getItem(position);
            holder.iButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteOneItem(position);
                }
            });
            holder.rGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch (checkedId) {
                        case mItemRadioR1:
                            mArrayList.get(position).setCheck(1);
                            break;
                        case mItemRadioR2:
                            mArrayList.get(position).setCheck(0);
                            break;
                    }
                }
            });
            if (item.isCheck() == 1) {
                holder.rGroup.check(mItemRadioR1);
            } else {
                holder.rGroup.check(mItemRadioR2);
            }
            holder.et1.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        index = position;
                    }
                    return false;
                }
            });
            holder.et2.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        index = position;
                    }
                    return false;
                }
            });
            holder.et1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    EditText et = (EditText) v;
                    if (mTWatch1 == null)
                    mTWatch1 = new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            mArrayList.get(index).setmName(s.toString());
                            /*for (int i = 0; i < mArrayList.size(); i++) {
                                LogHelper.i("姓名", i + "=" + mArrayList.get(i).getmName());
                            }*/
                        }
                    };
                    if (hasFocus) {

                        et.addTextChangedListener(mTWatch1);
                    } else {
                        et.removeTextChangedListener(mTWatch1);
                    }
                }
            });
            holder.et2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    EditText et = (EditText) v;
                    if (mTWatch2 == null)
                        mTWatch2 = new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                                mArrayList.get(index).setmIdCard(s.toString());
                                /*for (int i = 0; i < mArrayList.size(); i++) {
                                    LogHelper.i("身份证号", i + "=" + mArrayList.get(i).getmIdCard());
                                }*/
                            }
                        };
                    if (hasFocus) {

                        et.addTextChangedListener(mTWatch2);
                    } else {
                        et.removeTextChangedListener(mTWatch2);
                    }
                }
            });
            holder.et1.setText(mArrayList.get(position).getmName());
            holder.et2.setText(mArrayList.get(position).getmIdCard());

            return convertView;
        }

    }

    private void deleteOneItem(int position) {
        if (mArrayList.size() > 1) {
            mArrayList.remove(position);
            mPersonOfSheBaoAdapter.notifyDataSetChanged();
        } else {
            App.getInstance().showToast("只剩一项了");
        }
    }

    static class ViewHolder {
        RadioGroup rGroup;
        ImageButton iButton;
        EditText et1;
        EditText et2;
        int position;
    }
}
