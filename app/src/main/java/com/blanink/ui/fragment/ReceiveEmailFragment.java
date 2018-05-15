package com.blanink.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.text.Html;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blanink.R;
import com.blanink.ui.activity.lastnext.LastDeleteRelationIsAgree;
import com.blanink.ui.activity.lastnext.LastFamilyManageCustomerSupplierInvite;
import com.blanink.ui.activity.lastnext.NextDeleteRelationIsAgree;
import com.blanink.ui.activity.lastnext.NextFamilyManageInviteBecomeSupplier;
import com.blanink.ui.activity.notify.OtherNotifyDetail;
import com.blanink.http.RetrofitFactory;
import com.blanink.bean.DataSynRefreshEvent;
import com.blanink.bean.Notify;
import com.blanink.bean.Response;
import com.blanink.utils.DateUtils;
import com.blanink.utils.GlideUtils;
import com.blanink.view.UpLoadListView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/3/29.
 * 我的通知
 */
public class ReceiveEmailFragment extends Fragment {
    private static final int NOTIFY_REFRESH = 6;
    private static final String TAG = "ReceiveEmailFragment";
    private static final int NOTIFY_RECEIVE = 7;
    @BindView(R.id.lv)
    UpLoadListView lv;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
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
    @BindView(R.id.fl_load)
    FrameLayout flLoad;
    private SharedPreferences sp;
    private MyNotifyAdapter adapter;
    private List<Notify.ResultBean.RowsBean> resultBeanList = new ArrayList<>();
    private boolean isHasData = true;
    SparseArray<View> sparseArray;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            lv.completeRefresh(isHasData);

            if (adapter != null) {
                if (resultBeanList.size() == 0) {
                    rlNotData.setVisibility(View.VISIBLE);
                }
                if (adapter.getCount() == 0) {
                    rlNotData.setVisibility(View.VISIBLE);

                }
                adapter.notifyDataSetChanged();

            }

        }
    };
    private int pageNo = 1;
    private String type = "";
    /**
     * TextView选择框
     */
    private TextView mSelectTv;

    /**
     * popup窗口里的ListView
     */
    private ListView mTypeLv;

    /**
     * popup窗口
     */
    private PopupWindow typeSelectPopup;

    /**
     * 模拟的假数据
     */
    private List<String> testData = new ArrayList<>();

    /**
     * 数据适配器
     */
    private ArrayAdapter<String> testDataAdapter;
    private LinearLayout ll_header;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_my_notify, null);
        sp = getActivity().getSharedPreferences("DATA", Context.MODE_PRIVATE);
        sparseArray = new SparseArray<>();
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshData(DataSynRefreshEvent event) {
        if (event.getCode() == NOTIFY_REFRESH) {
            Log.e(TAG, event.getReason());
            pageNo = 1;
            RefreshData();
        }
    }

    private void initData() {
        testData.add("全部");
        testData.add("会议通告");
        testData.add("咨询沟通");
        testData.add("活动通告");
        testData.add("下家申请");
        testData.add("上家申请");
        testData.add("下家解除");
        testData.add("上家解除");
        testData.add("申请回复");
        loadNotifyFilter();
        View view = View.inflate(getActivity(), R.layout.layout_my_notify_header, null);
        lv.addHeaderView(view);
        ll_header = (LinearLayout) view.findViewById(R.id.ll_header);
        mSelectTv = (TextView) view.findViewById(R.id.tv_select_input);
        ll_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initSelectPopup();

                // 使用isShowing()检查popup窗口是否在显示状态
                if (typeSelectPopup != null && !typeSelectPopup.isShowing()) {
                    typeSelectPopup.showAsDropDown(ll_header, 0, 0);

                    mSelectTv.setTextColor(getResources().getColor(R.color.blue_text));
                    Drawable drawableRight = getResources().getDrawable(
                            R.drawable.last_blue);
                    mSelectTv.setCompoundDrawablesWithIntrinsicBounds(null, null, drawableRight,
                            null);
                }
            }
        });
        //刷新数据
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        swipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNo = 1;
                RefreshData();
            }
        });
        //加载更多
        lv.setOnRefreshListener(new UpLoadListView.OnRefreshListener() {
            @Override
            public void onLoadingMore() {
                pageNo++;
                loadNotifyFilter();
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        //详情
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //点击显示已读
                if (position < resultBeanList.size() + 1) {
                    if ("0".equals(resultBeanList.get(position - 1).getReadFlag())) {
                        EventBus.getDefault().post(new DataSynRefreshEvent(true, 9, "未读通知界更新"));
                        updateStatus(resultBeanList.get(position - 1).getId());
                        //updateItem(position);
                    }
                    Intent intent = null;
                    if ("6".equals(resultBeanList.get(position - 1).getType())) {
                        //上家申请
                        intent = new Intent(getActivity(), LastFamilyManageCustomerSupplierInvite.class);
                        //intent = new Intent(getActivity(), NotifyReplayDetail.class);

                    } else if ("5".equals(resultBeanList.get(position - 1).getType())) {
                        //下家申请
                        intent = new Intent(getActivity(), NextFamilyManageInviteBecomeSupplier.class);
                        // intent = new Intent(getActivity(), NotifyReplayDetail.class);

                    } else if ("7".equals(resultBeanList.get(position - 1).getType())) {
                        // 下家解除
                        intent = new Intent(getActivity(), NextDeleteRelationIsAgree.class);
                    } else if ("8".equals(resultBeanList.get(position - 1).getType())) {
                        //上家解除
                        intent = new Intent(getActivity(), LastDeleteRelationIsAgree.class);
                    } else {
                        intent = new Intent(getActivity(), OtherNotifyDetail.class);
                    }
                    intent.putExtra("referrenceObjectId", resultBeanList.get(position - 1).getReferrenceObjectId());
                    intent.putExtra("notifyId", resultBeanList.get(position - 1).getId());
                    intent.putExtra("content", resultBeanList.get(position - 1).getContent());
                    intent.putExtra("title", resultBeanList.get(position - 1).getTitle());
                    intent.putExtra("time", resultBeanList.get(position - 1).getCreateDate());
                    intent.putExtra("sender", resultBeanList.get(position - 1).getCreateBy().getName());
                    if (resultBeanList.get(position - 1).getCreateBy() != null && resultBeanList.get(position - 1).getCreateBy().getOffice() != null) {
                        intent.putExtra("company", resultBeanList.get(position - 1).getCreateBy().getOffice().getName());
                    }
                    intent.putExtra("readFlag", resultBeanList.get(position - 1).getReadFlag());
                    startActivity(intent);


                }
            }
        });
    }

    private void updateItem(int position) {
        /**第一个可见的位置**/
        int firstVisiblePosition = lv.getFirstVisiblePosition();
        /**最后一个可见的位置**/
        int lastVisiblePosition = lv.getLastVisiblePosition();

        /**在看见范围内才更新，不可见的滑动后自动会调用getView方法更新**/
        if (position >= firstVisiblePosition && position <= lastVisiblePosition) {
            /**获取指定位置view对象**/
            View view = lv.getChildAt(position - firstVisiblePosition);
            ViewHolder mViewHolder = (ViewHolder) view.getTag();
            mViewHolder.iv_not_red.setVisibility(View.GONE);
            adapter.getView(position, view, lv);
        }

    }

    private void RefreshData() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("currentUser.id", sp.getString("USER_ID", null));
        params.put("pageNo", pageNo + "");
        params.put("type", type);

        RetrofitFactory.getApiService(getActivity())
                .filter(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Notify>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        swipeRefreshLayout.setRefreshing(false);

                    }

                    @Override
                    public void onNext(Notify notify) {
                        swipeRefreshLayout.setRefreshing(false);
                        Log.e("ReceiveEmailFragment", notify.toString());
                        if (notify.getResult().getRows().size() == 0) {

                        } else {
                            isHasData = true;
                            rlNotData.setVisibility(View.GONE);
                            resultBeanList.clear();
                            resultBeanList.addAll(0, notify.getResult().getRows());
                            adapter.notifyDataSetChanged();
                        }
                        handler.sendEmptyMessage(0);
                    }
                });
    }

    //加载失败
    @OnClick(R.id.rl_load_fail)
    public void onClick() {
        rlLoadFail.setVisibility(View.GONE);
        llLoad.setVisibility(View.VISIBLE);
        loadNotifyFilter();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    public class MyNotifyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return resultBeanList.size();
        }

        @Override
        public Object getItem(int position) {
            return resultBeanList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (sparseArray.get(position, null) == null) {
                viewHolder = new ViewHolder();
                convertView = View.inflate(getActivity(), R.layout.item_my_notify, null);
                viewHolder.tv_company_name = (TextView) convertView.findViewById(R.id.tv_company_name);
                viewHolder.time = (TextView) convertView.findViewById(R.id.time);
                viewHolder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
                viewHolder.iv = (ImageView) convertView.findViewById(R.id.iv);
                viewHolder.iv_not_red = (ImageView) convertView.findViewById(R.id.iv_not_red);

                convertView.setTag(viewHolder);
                sparseArray.put(position, convertView);
            } else {
                convertView = sparseArray.get(position);
                viewHolder = (ViewHolder) convertView.getTag();
            }
            Notify.ResultBean.RowsBean rows = resultBeanList.get(position);
            //viewHolder.tv_title.setText(rows.getTitle());
            if (rows.getContent() != null) {
                viewHolder.tv_content.setText(Html.fromHtml(rows.getContent()));
            }
            viewHolder.time.setText(DateUtils.format(DateUtils.stringToDate(rows.getCreateDate())));
            if (rows.getCreateBy().getOffice() != null) {
                viewHolder.tv_company_name.setText(rows.getCreateBy().getOffice().getName());
            }
            if (rows.getCreateBy().getOffice() != null&&rows.getCreateBy().getOffice().getPhoto()!=null&&!"".equals(rows.getCreateBy().getOffice().getPhoto())) {
                GlideUtils.glideImageView(getActivity(), viewHolder.iv, rows.getCreateBy().getOffice().getPhoto(), true);
            }
            if ("0".equals(rows.getReadFlag())) {
                viewHolder.iv_not_red.setVisibility(View.VISIBLE);
            } else {
                viewHolder.iv_not_red.setVisibility(View.GONE);

            }
            return convertView;
        }
    }

    static class ViewHolder {
        TextView time;
        TextView tv_company_name;
        TextView tv_master;
        TextView tv_content;
        TextView ll_state;
        ImageView iv, iv_not_red;
    }

    public void loadFilter() {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("currentUser.id", sp.getString("USER_ID", null));
        params.put("pageNo", pageNo + "");
        params.put("type", type);

        RetrofitFactory.getApiService(getActivity())
                .filter(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Notify>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("ReceiveEmailFragment", e.toString());
                        llLoad.setVisibility(View.GONE);
                        rlLoadFail.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onNext(Notify notify) {
                        llLoad.setVisibility(View.GONE);
                        Log.e("ReceiveEmailFragment", notify.toString());
                        resultBeanList.clear();
                        resultBeanList.addAll(notify.getResult().getRows());
                        isHasData = true;
                        handler.sendEmptyMessage(0);
                    }
                });
    }
    public void loadNotifyFilter() {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("currentUser.id", sp.getString("USER_ID", null));
        params.put("pageNo", pageNo + "");
        params.put("type", type);

        RetrofitFactory.getApiService(getActivity())
                .filter(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Notify>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("ReceiveEmailFragment", e.toString());
                        llLoad.setVisibility(View.GONE);
                        rlLoadFail.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onNext(Notify notify) {
                        llLoad.setVisibility(View.GONE);
                        Log.e("ReceiveEmailFragment", notify.toString());
                        if (notify.getResult().getTotal() == 0) {
                            rlNotData.setVisibility(View.VISIBLE);
                        } else {
                            rlNotData.setVisibility(View.GONE);

                        }
                        if (notify.getResult().getRows().size() == 0) {
                            isHasData = false;
                        } else {
                            resultBeanList.addAll(notify.getResult().getRows());
                            if (adapter == null) {
                                adapter = new MyNotifyAdapter();
                                lv.setAdapter(adapter);
                            } else {
                                adapter.notifyDataSetChanged();
                            }
                        }
                        handler.sendEmptyMessage(0);
                    }
                });
    }

    /**
     * 初始化popup窗口
     */
    private void initSelectPopup() {
        mTypeLv = new ListView(getActivity());
        mTypeLv.setDivider(null);
        // 设置适配器
        testDataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.popup_text_item, R.id.tv_name, testData);
        View view=View.inflate(getActivity(),R.layout.layout_line,null);
        mTypeLv.addFooterView(view);
        mTypeLv.setAdapter(testDataAdapter);
        // 设置ListView点击事件监听
        mTypeLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 在这里获取item数据
                String value = testData.get(position);
                // 把选择的数据展示对应的TextView上
                mSelectTv.setText(value);

                switch (position) {
                    case 0:
                        pageNo = 1;
                        type = "";

                        break;
                    case 1:
                        pageNo = 1;
                        type = "1";

                        break;
                    case 2:
                        pageNo = 1;
                        type = "2";

                        break;
                    case 3:
                        pageNo = 1;
                        type = "3";

                        break;
                    case 4:
                        pageNo = 1;
                        type = "4";

                        break;
                    case 5:
                        pageNo = 1;
                        type = "5";

                        break;
                    case 6:
                        pageNo = 1;
                        type = "6";

                        break;
                    case 7:
                        pageNo = 1;
                        type = "7";

                        break;
                    case 8:
                        pageNo = 1;
                        type = "8";

                    case 9:
                        pageNo = 1;
                        type = "9";

                        break;
                }

                rlNotData.setVisibility(View.GONE);
                llLoad.setVisibility(View.VISIBLE);
                loadFilter();
                // 选择完后关闭popup窗口
                typeSelectPopup.dismiss();
        /*        mSelectTv.setTextColor(getResources().getColor(R.color.gray_text));
                Drawable drawableLeft = getResources().getDrawable(
                        R.drawable.last_gray);
                mSelectTv.setCompoundDrawablesWithIntrinsicBounds(null,null,drawableLeft,
                        null);*/
            }
        });

        typeSelectPopup = new PopupWindow(mTypeLv, ll_header.getWidth(), ActionBar.LayoutParams.WRAP_CONTENT, true);
        // 取得popup窗口的背景图片
        Drawable drawable = ContextCompat.getDrawable(getActivity(), R.color.colorWhite);
        typeSelectPopup.setBackgroundDrawable(drawable);
        typeSelectPopup.setFocusable(true);
        typeSelectPopup.setOutsideTouchable(true);
        typeSelectPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                // 关闭popup窗口
                typeSelectPopup.dismiss();
                mSelectTv.setTextColor(getResources().getColor(R.color.black_text));
                Drawable drawableRight = getResources().getDrawable(
                        R.drawable.last_gray);
                mSelectTv.setCompoundDrawablesWithIntrinsicBounds(null, null, drawableRight,
                        null);
            }
        });
    }
    private void updateStatus(String notifyId) {
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("currentUser.id", sp.getString("USER_ID", null));
        mapParams.put("id", notifyId);
        RetrofitFactory.getApiService(getActivity())
                .updateStatus(mapParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Response response) {

                    }
                });
    }
}
