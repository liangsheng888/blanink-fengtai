package com.blanink.ui.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blanink.R;
import com.blanink.bean.Advertise;
import com.blanink.bean.DataSynRefreshEvent;
import com.blanink.bean.MenuControl;
import com.blanink.bean.VersionInfo;
import com.blanink.http.RetrofitFactory;
import com.blanink.ui.activity.MainActivity;
import com.blanink.ui.activity.ScanActivity;
import com.blanink.ui.activity.SeekActivity;
import com.blanink.ui.activity.WebViewUpdateActivity;
import com.blanink.ui.activity.WorkResponseHistory;
import com.blanink.ui.activity.aftersale.AfterSaleQueue;
import com.blanink.ui.activity.aftersale.MyAfterSale;
import com.blanink.ui.activity.bidtender.BidManage;
import com.blanink.ui.activity.bidtender.TenderManage;
import com.blanink.ui.activity.flow.FlowOrder;
import com.blanink.ui.activity.lastnext.LastFamilyManageCustomer;
import com.blanink.ui.activity.lastnext.NextFamilyManageCompanySupplierManage;
import com.blanink.ui.activity.order.ComeOrder;
import com.blanink.ui.activity.order.GoOrderPurchase;
import com.blanink.ui.activity.order.ReceiveGoods;
import com.blanink.ui.activity.report.ReportType;
import com.blanink.ui.activity.stock.StockManage;
import com.blanink.ui.activity.task.TaskResponseProcessQueue;
import com.blanink.ui.activity.task.WorkPlanProcessQueue;
import com.blanink.ui.adapter.GridSpacingItemDecoration;
import com.blanink.ui.adapter.MenuAdapter;
import com.blanink.ui.adapter.MyItemDecoration;
import com.blanink.ui.adapter.RecyclerItemClickListener;
import com.blanink.ui.adapter.TextAdapter;
import com.blanink.utils.BaseUrlUtils;
import com.blanink.utils.CommonUtil;
import com.blanink.utils.UrlPic;
import com.blanink.view.MyViewPager;
import com.google.zxing.integration.android.IntentIntegrator;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.blanink.http.RetrofitFactory.getApiService;

/**
 * Created by Administrator on 2016/12/24.
 * 任务
 */
public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";
    private static final int MENU_REFRESH = 7;
    @BindView(R.id.framgment_task_vp_advertise)
    MyViewPager framgmentTaskVpAdvertise;
    @BindView(R.id.et_seek)
    TextView etSeek;
    @BindView(R.id.tv_scan)
    TextView tvScan;
    @BindView(R.id.fl_seek_ll_seek)
    FrameLayout flSeekLlSeek;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.ll_load)
    LinearLayout llLoad;
    @BindView(R.id.loading_error_img)
    ImageView loadingErrorImg;
    @BindView(R.id.rl_load_fail)
    RelativeLayout rlLoadFail;
    @BindView(R.id.tv_not)
    TextView tvNot;
    @BindView(R.id.rl_not_data)
    RelativeLayout rlNotData;
    @BindView(R.id.tv_not_net)
    TextView tvNotNet;
    @BindView(R.id.rl_not_net)
    RelativeLayout rlNotNet;
    @BindView(R.id.fl_load)
    FrameLayout flLoad;
    private SharedPreferences sp;
    private MenuAdapter menuAdapter = null;
    List<String> menuList = null;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = View.inflate(getActivity(), R.layout.fragment_home, null);
        } else {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
        }
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getActivity().getWindow().getDecorView();
            //让应用主题内容占用系统状态栏的空间,注意:下面两个参数必须一起使用 stable 牢固的
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            //设置状态栏颜色为透明
            getActivity().getWindow().setStatusBarColor(Color.TRANSPARENT);

        }
        sp = getActivity().getSharedPreferences("DATA", getActivity().MODE_PRIVATE);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        menuList = new ArrayList<String>();
        initData();
        //二维码扫描
        tvScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 设置要扫描的条码类型，ONE_D_CODE_TYPES：一维码，QR_CODE_TYPES-二维码
                IntentIntegrator integrator = new IntentIntegrator(getActivity());
                integrator.setPrompt("请扫描"); //底部的提示文字，设为""可以置空
                integrator.setCameraId(0); //前置或者后置摄像头
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setBeepEnabled(false); //扫描成功的「哔哔」声，默认开启
                integrator.setCaptureActivity(ScanActivity.class);
                integrator.initiateScan();
            }
        });

    }


    private void initData() {
        loadMenuControl();
        etSeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSeek = new Intent(getActivity(), SeekActivity.class);
                intentSeek.putExtra("DIRECT", 0);
                startActivity(intentSeek);
            }
        });


        List<Advertise> picLis = new ArrayList<>();
        picLis.add(new Advertise(UrlPic.pic, UrlPic.baseUrl));
        picLis.add(new Advertise(UrlPic.pic2, UrlPic.baseUrl));
        picLis.add(new Advertise(UrlPic.pic3, UrlPic.baseUrl));
        picLis.add(new Advertise(UrlPic.pic4, UrlPic.baseUrl));
        picLis.add(new Advertise(UrlPic.pic5, UrlPic.baseUrl));

        //广搞轮播
        framgmentTaskVpAdvertise.pictureRoll(getActivity(), picLis);


        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                if ("招标管理".equals(menuList.get(position))) {
                    Intent intentTender = new Intent(getActivity(), TenderManage.class);
                    startActivity(intentTender);
                }
                if ("投标管理".equals(menuList.get(position))) {
                    Intent intentBid = new Intent(getActivity(), BidManage.class);
                    startActivity(intentBid);
                }
                if ("客户管理".equals(menuList.get(position))) {
                    Intent intentLast = new Intent(getActivity(), LastFamilyManageCustomer.class);
                    startActivity(intentLast);
                }
                if ("供应商管理".equals(menuList.get(position))) {
                    Intent intentNext = new Intent(getActivity(), NextFamilyManageCompanySupplierManage.class);
                    startActivity(intentNext);
                }
                if ("工作反馈".equals(menuList.get(position))) {
                    Intent intentTaskResponse = new Intent(getActivity(), TaskResponseProcessQueue.class);
                    startActivity(intentTaskResponse);
                }
                if ("任务分工".equals(menuList.get(position))) {
                    Intent intentworkPlan = new Intent(getActivity(), WorkPlanProcessQueue.class);
                    startActivity(intentworkPlan);
                }
                if ("采购管理".equals(menuList.get(position))) {
                    Intent itBuy = new Intent(getActivity(), GoOrderPurchase.class);
                    startActivity(itBuy);
                }
                if ("订单管理".equals(menuList.get(position))) {
                    Intent itOrder = new Intent(getActivity(), ComeOrder.class);
                    startActivity(itOrder);
                }
                if ("生产管理".equals(menuList.get(position))) {
                    Intent intentFlow = new Intent(getActivity(), FlowOrder.class);
                    startActivity(intentFlow);
                }
                if ("售后处理".equals(menuList.get(position))) {
                    Intent intent = new Intent(getActivity(), AfterSaleQueue.class);
                    startActivity(intent);

                }
                if ("我的售后".equals(menuList.get(position))) {
                    Intent intent = new Intent(getActivity(), MyAfterSale.class);
                    startActivity(intent);
                }
                if ("收货".equals(menuList.get(position))) {
                    Intent intent = new Intent(getActivity(), ReceiveGoods.class);
                    startActivity(intent);
                }
                if ("报表".equals(menuList.get(position))) {
                    Intent intent_table = new Intent(getActivity(), ReportType.class);
                    startActivity(intent_table);
                }
                if ("工作记录".equals(menuList.get(position))) {
                    Intent intent = new Intent(getActivity(), WorkResponseHistory.class);
                    startActivity(intent);
                }
                if ("仓库管理".equals(menuList.get(position))) {
                    Intent intent = new Intent(getActivity(), StockManage.class);
                    startActivity(intent);
                }
            }

        }));


    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshMenu(DataSynRefreshEvent event) {
        if (event.getCode() == MENU_REFRESH) {
            Log.e(TAG, event.getReason());
            menuList.clear();
            loadMenuControl();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (((MainActivity) getActivity()).getUpdate()) {

            }
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }


    //菜单配置
    public void loadMenuControl() {
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("id", sp.getString("USER_ID", null));
        getApiService(getActivity())
                .getMenu(mapParams)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MenuControl>() {
                    @Override
                    public void onCompleted() {
                         flLoad.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        flLoad.setVisibility(View.GONE);

                    }

                    @Override
                    public void onNext(MenuControl menuControl) {
                        menuList.addAll(menuControl.getResult());
                        Log.e("Home", "长度:" + menuList.size() + "------" + menuList.toString());
                        menuAdapter = new MenuAdapter(getActivity(), menuList);
                        if (recyclerView != null) {
                            recyclerView.removeAllViews();
                        }
                        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 5, GridLayoutManager.VERTICAL, false));
                        recyclerView.addItemDecoration(new GridSpacingItemDecoration(5, getResources().getDimensionPixelSize(R.dimen.padding_middle), true));//设置间隙
                        recyclerView.setNestedScrollingEnabled(false);
                        recyclerView.setAdapter(menuAdapter);
                        //获取版本
                        Map<String, Object> mapParams = new HashMap<String, Object>();
                        RetrofitFactory.getApiService(getActivity())
                                .getVersionInfo(mapParams)
                                .subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Subscriber<VersionInfo>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }

                                    @Override
                                    public void onNext(VersionInfo versionInfo) {
                                        if (CommonUtil.GetVersionCode(getActivity()) < versionInfo.getResult().getVersionCode()) {
                                            showDialog(versionInfo);

                                        }
                                    }
                                });
                    }

                });


    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void showDialog(VersionInfo versionName) {
        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.show();
        alertDialog.setContentView(R.layout.dialog_update);
        Window window = alertDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        window.setGravity(Gravity.CENTER);
        WindowManager windowManager = getActivity().getWindowManager();
        Display d = windowManager.getDefaultDisplay(); // 获取屏幕宽、高用
        lp.width = (int) (d.getWidth() * 0.9); // 宽度设置为屏幕的1/2
        window.setAttributes(lp);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        ((TextView) (window.findViewById(R.id.tv_version_name))).setText("发现新版本  " + versionName.getResult().getVersionName());
        RecyclerView recyclerView = (RecyclerView) (window.findViewById(R.id.recyclerView));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new MyItemDecoration(10));
        recyclerView.setAdapter(new TextAdapter(getActivity(), versionName.getResult().getMessageList()));
        window.findViewById(R.id.tv_ingnore).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();


            }
        });
        window.findViewById(R.id.tv_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                // String path="https://github.com/liangsheng888/blanink_apk/raw/master/blanink.apk";
                /*String appName= Environment.getExternalStorageDirectory()
                        + "/update.app";
                AppInnerDownUtils.downLoadApk(FlashActivity.this,path,appName);*/
                Intent intent = new Intent(getActivity(), WebViewUpdateActivity.class);
                intent.putExtra("url", BaseUrlUtils.UPDATE_URL);
                startActivity(intent);
            }
        });
    }


}




