package com.blanink.ui.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blanink.R;
import com.blanink.ui.activity.WebViewActivity;
import com.blanink.ui.activity.AdviceResponse;
import com.blanink.ui.activity.MyApplication;
import com.blanink.ui.activity.SysNotify;
import com.blanink.ui.activity.my.MyProfile;
import com.blanink.ui.activity.set.PersonSet;
import com.blanink.http.RetrofitFactory;
import com.blanink.bean.LoginResult;
import com.blanink.ui.jshare.uitool.ShareBoard;
import com.blanink.ui.jshare.uitool.ShareBoardlistener;
import com.blanink.ui.jshare.uitool.SnsPlatform;
import com.blanink.utils.BaseUrlUtils;
import com.blanink.utils.GlideUtils;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jiguang.share.android.api.JShareInterface;
import cn.jiguang.share.android.api.PlatActionListener;
import cn.jiguang.share.android.api.Platform;
import cn.jiguang.share.android.api.ShareParams;
import cn.jiguang.share.qqmodel.QQ;
import cn.jiguang.share.qqmodel.QZone;
import cn.jiguang.share.wechat.Wechat;
import cn.jiguang.share.wechat.WechatFavorite;
import cn.jiguang.share.wechat.WechatMoments;
import cn.jiguang.share.weibo.SinaWeibo;
import cn.jiguang.share.weibo.SinaWeiboMessage;
import me.iwf.photopicker.PhotoPreview;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

//import com.blanink.ui.activity.ezvideo.EZCameraListActivity;

/**
 * Created by Administrator on 2016/12/24.
 * 我的
 */
public class MineFragment extends Fragment {

    @BindView(R.id.iv_user_photo)
    ImageView ivUserPhoto;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.rl_photo)
    RelativeLayout rlPhoto;
    @BindView(R.id.tv_profile)
    TextView tvProfile;
    @BindView(R.id.rl_profile)
    RelativeLayout rlProfile;
    @BindView(R.id.tv_finance)
    TextView tvFinance;
    @BindView(R.id.rl_finance)
    RelativeLayout rlFinance;
    @BindView(R.id.tv_feed_back)
    TextView tvFeedBack;
    @BindView(R.id.rl_response)
    RelativeLayout rlResponse;
    @BindView(R.id.tv_after_sale)
    TextView tvAfterSale;
    @BindView(R.id.rl_after_sale)
    RelativeLayout rlAfterSale;
    @BindView(R.id.tv_set)
    TextView tvSet;
    @BindView(R.id.rl_set)
    RelativeLayout rlSet;
    @BindView(R.id.ll_content)
    LinearLayout llContent;
    @BindView(R.id.tv_company_name)
    TextView tvCompanyName;
/*    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;*/
    private SharedPreferences sp;
    private ArrayList<String> arrayList = new ArrayList<>();
    LoginResult loginResult;
    private int mAction = Platform.ACTION_SHARE;
    private ShareBoard mShareBoard;
    private ProgressDialog progressDialog;
    private String TAG="FlowProgressDetail";
    public static String share_url = "http://base.blanink.com:8080/blanink-home/phoneForm.html";
    public static String share_text = "空行互联是一个提升小企业协同的O2O产业集群平台";
    public static String share_title = " 欢迎使用空行互联";
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String toastMsg = (String) msg.obj;
            Toast.makeText(getActivity(), toastMsg, Toast.LENGTH_SHORT).show();
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    };
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e("Mine", "onCreateView");
        sp = getActivity().getSharedPreferences("DATA", Context.MODE_PRIVATE);
        View view = View.inflate(getActivity(), R.layout.fragment_mine, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("请稍候");
        Log.e("Mine", "onActivityCreated");

    }

    private void initData() {
        ivUserPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (arrayList.size() > 0) {
                    PhotoPreview.builder().
                            setPhotos(arrayList).
                            setShowDeleteButton(false)
                            .setShowToolbar(false)
                            .start(getActivity());
                }
            }
        });
    }


    @OnClick({R.id.rl_profile, R.id.rl_finance, R.id.rl_response, R.id.rl_after_sale, R.id.rl_set,R.id.rl_share})
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            //个人资料

            case R.id.rl_profile:
                intent = new Intent(getActivity(), MyProfile.class);
                startActivity(intent);

                break;
            case R.id.rl_finance:
                //系统通知
                intent = new Intent(getActivity(), SysNotify.class);
                startActivity(intent);

                break;

            case R.id.rl_response:
                //意见反馈
                intent = new Intent(getActivity(), AdviceResponse.class);
                startActivity(intent);

                break;
            case R.id.rl_after_sale:
                //关于我们

             intent =new Intent(getContext(),WebViewActivity.class);
                intent.putExtra("url", BaseUrlUtils.WEB_URL);
                startActivity(intent);

                break;
            case R.id.rl_set:
                //设置
                intent = new Intent(getActivity(), PersonSet.class);
                startActivity(intent);

                break;
           case R.id.rl_share:
               //分享好友
               mAction = Platform.ACTION_SHARE;
               showBroadView();
                break;
        }

    }

    public void loadUserInfoFromSever() {
        // http://localhost:8088/blanink-api/customer/user?userId=fec25c7f7634448581e21876ef517c57
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", sp.getString("USER_ID", null));
        RetrofitFactory.getApiService(getActivity())
                .user(params)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LoginResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(LoginResult loginResult) {
                        tvUserName.setText(loginResult.getResult().name);
                        tvCompanyName.setText(loginResult.getResult().getCompany().getName());
                        if (loginResult.getResult().photo != null && !loginResult.getResult().photo.equals("")) {
                            GlideUtils.glideImageView(getActivity(), ivUserPhoto, loginResult.getResult().photo, true);
                        }
                        arrayList.clear();
                        arrayList.add(loginResult.getResult().photo);
                    }
                });
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.e("Mine", "onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.e("Mine", "onAttach");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e("Mine", "onStart");
        loadUserInfoFromSever();
        initData();
    }

    @Override
    public void onResume() {
        Log.e("Mine", "onResume");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.e("Mine", "onPause");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.e("Mine", "onStop");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        Log.e("Mine", "onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("Mine", "onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    private void showBroadView() {
        if (mShareBoard == null) {
            mShareBoard = new ShareBoard(getActivity());
            List<String> platforms = JShareInterface.getPlatformList();
            if (platforms != null) {
                Iterator var2 = platforms.iterator();
                while (var2.hasNext()) {
                    String temp = (String) var2.next();
                    SnsPlatform snsPlatform = createSnsPlatform(temp);
                    mShareBoard.addPlatform(snsPlatform);
                }
            }
            mShareBoard.setShareboardclickCallback(mShareBoardlistener);
        }
        mShareBoard.show();
    }
    public  SnsPlatform createSnsPlatform(String platformName) {
        String mShowWord = platformName;
        String mIcon = "";
        String mGrayIcon = "";
        String mKeyword = platformName;
        if (Wechat.Name.equals(platformName)) {
            mIcon = "jiguang_socialize_wechat";
            mGrayIcon = "jiguang_socialize_wechat";
            mShowWord = "jiguang_socialize_text_weixin_key";
        } else if (WechatMoments.Name.equals(platformName)) {
            mIcon = "jiguang_socialize_wxcircle";
            mGrayIcon = "jiguang_socialize_wxcircle";
            mShowWord = "jiguang_socialize_text_weixin_circle_key";

        } else if (WechatFavorite.Name.equals(platformName)) {
            mIcon = "jiguang_socialize_wxfavorite";
            mGrayIcon = "jiguang_socialize_wxfavorite";
            mShowWord = "jiguang_socialize_text_weixin_favorite_key";

        } else if (SinaWeibo.Name.equals(platformName)) {
            mIcon = "jiguang_socialize_sina";
            mGrayIcon = "jiguang_socialize_sina";
            mShowWord = "jiguang_socialize_text_sina_key";
        } else if (SinaWeiboMessage.Name.equals(platformName)) {
            mIcon = "jiguang_socialize_sina";
            mGrayIcon = "jiguang_socialize_sina";
            mShowWord = "jiguang_socialize_text_sina_msg_key";
        } else if (QQ.Name.equals(platformName)) {
            mIcon = "jiguang_socialize_qq";
            mGrayIcon = "jiguang_socialize_qq";
            mShowWord = "jiguang_socialize_text_qq_key";

        } else if (QZone.Name.equals(platformName)) {
            mIcon = "jiguang_socialize_qzone";
            mGrayIcon = "jiguang_socialize_qzone";
            mShowWord = "jiguang_socialize_text_qq_zone_key";
        }
        return ShareBoard.createSnsPlatform(mShowWord, mKeyword, mIcon, mGrayIcon, 0);
    }
    private ShareBoardlistener mShareBoardlistener = new ShareBoardlistener() {
        @Override
        public void onclick(SnsPlatform snsPlatform, String platform) {
            Log.e("jshare",snsPlatform.toString());
            Log.e("jshare",platform);

            switch (mAction) {
                case Platform.ACTION_SHARE:
                    progressDialog.show();
                    //这里以分享链接为例
                    ShareParams shareParams = new ShareParams();
                    shareParams.setTitle(share_title);
                    shareParams.setText(share_text);
                    shareParams.setShareType(Platform.SHARE_WEBPAGE);
                    shareParams.setUrl(share_url);
                    shareParams.setImagePath(MyApplication.ImagePath);
                    JShareInterface.share(platform, shareParams, mShareListener);
                    break;
                default:
                    break;
            }
        }
    };
    private PlatActionListener mShareListener = new PlatActionListener() {
        @Override
        public void onComplete(Platform platform, int action, HashMap<String, Object> data) {
            if (handler != null) {
                Message message = handler.obtainMessage();
                message.obj = "分享成功";
                handler.sendMessage(message);
            }
        }

        @Override
        public void onError(Platform platform, int action, int errorCode, Throwable error) {
            Log.e(TAG, "error:" + errorCode + ",msg:" + error);
            if (handler != null) {
                Message message = handler.obtainMessage();
                if(errorCode==10014||errorCode==40009){
                    message.obj = "请去安装客户端进行分享" ;
                }else{
                message.obj = "分享失败:" + error.getMessage() + "---" + errorCode;
                }
                handler.sendMessage(message);
            }
        }

        @Override
        public void onCancel(Platform platform, int action) {
            if (handler != null) {
                Message message = handler.obtainMessage();
                message.obj = "分享取消";
                handler.sendMessage(message);
            }
        }
    };
}
