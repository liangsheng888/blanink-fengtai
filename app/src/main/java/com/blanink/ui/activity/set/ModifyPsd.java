package com.blanink.ui.activity.set;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blanink.R;
import com.blanink.ui.activity.BaseActivity;
import com.blanink.http.RetrofitFactory;
import com.blanink.bean.Response;
import com.blanink.utils.DialogLoadUtils;


import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/***
 * 修改密码
 */
public class ModifyPsd extends BaseActivity {

    @BindView(R.id.iv_last)
    TextView ivLast;
    @BindView(R.id.et_oldPsd)
    EditText etOldPsd;
    @BindView(R.id.et_newPsd)
    EditText etNewPsd;
    @BindView(R.id.et_nick)
    EditText etNick;
    @BindView(R.id.btn_ok)
    Button btnOk;
    @BindView(R.id.tender_manager)
    RelativeLayout tenderManager;
    @BindView(R.id.ll_psd)
    LinearLayout llPsd;
    @BindView(R.id.activity_modify_psd)
    RelativeLayout activityModifyPsd;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getSharedPreferences("DATA", MODE_PRIVATE);
        setContentView(R.layout.activity_modify_psd);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.iv_last, R.id.btn_ok})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_last:
                finish();
                break;
            case R.id.btn_ok:
                commitData();
                break;
        }
    }

    private void commitData() {
        //修改前先验证
        // http://localhost:8088/blanink-api/passwordUpdata?loginName=admin&newPassword=admin&id=2
        String oldPsd = etOldPsd.getText().toString().trim();
        String newPsd = etNewPsd.getText().toString().trim();
        String newPsdSecond = etNick.getText().toString().trim();
        if (TextUtils.isEmpty(oldPsd)) {
            Toast.makeText(this, "请输入旧密码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(newPsd)) {
            Toast.makeText(this, "请输入新密码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!oldPsd.equals(sp.getString("PASSWORD", null))) {
            Toast.makeText(this, "你输入的旧密码不正确", Toast.LENGTH_SHORT).show();
            return;

        }
        if (!newPsdSecond.equals(newPsd)) {
            Toast.makeText(this, "两次输入的新密码不一致", Toast.LENGTH_SHORT).show();
            return;
        }
        DialogLoadUtils.getInstance(ModifyPsd.this);
        DialogLoadUtils.showDialogLoad("操作进行中...");

        loadData(newPsd);
    }

    //提交密码
    private void loadData(String psd) {
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("id", sp.getString("USER_ID", null));
        mapParams.put("newPassword", psd);
        mapParams.put("loginName", sp.getString("LOGIN_NAME", null));

        RetrofitFactory.getApiService(this)
                .passwordUpdata(mapParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        DialogLoadUtils.dismissDialog();
                        Toast.makeText(ModifyPsd.this, "服务器异常", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(Response response) {
                        DialogLoadUtils.dismissDialog();
                        if (response.getErrorCode().equals("00000")) {
                            showDialog();
                        }else {
                            Toast.makeText(ModifyPsd.this, response.getReason(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void showDialog() {
        final AlertDialog alertDialog = new AlertDialog.Builder(ModifyPsd.this).create();
        alertDialog.show();
        alertDialog.setContentView(R.layout.dialog_custom_success);
        Window window = alertDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        window.setGravity(Gravity.CENTER);
        Display d = getWindowManager().getDefaultDisplay(); // 获取屏幕宽、高用
        //  lp.height = (int) (d.getHeight() * 0.6); // 高度设置为屏幕的0.6
        lp.width = (int) (d.getWidth() * 0.9); // 宽度设置为屏幕的1/2
        window.setWindowAnimations(R.style.dialogAnimation);
        window.setAttributes(lp);
        alertDialog.setCanceledOnTouchOutside(false);
        window.findViewById(R.id.tv_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                finish();
            }
        });
    }

}
