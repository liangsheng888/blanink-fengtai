package com.blanink.ui.activity.bidtender;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.blanink.R;
import com.blanink.ui.activity.BaseActivity;
import com.blanink.http.RetrofitFactory;
import com.blanink.bean.DataSynRefreshEvent;
import com.blanink.bean.TenderAndBid;
import com.blanink.utils.CommonUtil;
import com.blanink.utils.DateUtils;
import com.blanink.view.UpLoadListView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/***
 * 招标管理
 */
public class TenderManage extends BaseActivity {
    private static final int REQUEST_CODE = 200;
    @BindView(R.id.tender_manager_iv_last)
    TextView tenderManagerIvLast;
    @BindView(R.id.tv_add)
    TextView tvAdd;
    @BindView(R.id.tender_manager)
    RelativeLayout tenderManager;
    @BindView(R.id.sp_sort)
    Spinner spSort;
    @BindView(R.id.sp_expire)
    Spinner spExpire;
    @BindView(R.id.rl_sort)
    RelativeLayout rlSort;
    @BindView(R.id.lv_tender)
    UpLoadListView lvTender;
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
    @BindView(R.id.activity_tender_manage)
    RelativeLayout activityTenderManage;
    @BindView(R.id.ll)
    LinearLayout ll;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.tv_not_net)
    TextView tvNotNet;
    @BindView(R.id.rl_not_net)
    RelativeLayout rlNotNet;
    private SharedPreferences sp;
    private int pageNo = 1;
    private List<TenderAndBid.Result.Row> rowList = new ArrayList<>();
    private boolean isHasData = true;
    private MyAdapter myAdapter;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            lvTender.completeRefresh(isHasData);
            if (myAdapter != null) {
                myAdapter.notifyDataSetChanged();
            } else {
                rlNotData.setVisibility(View.VISIBLE);
            }
        }
    };

    private String sort = "1";
    private String expire = "";
    private SparseArray<View> viewArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tender_manage);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        sp = getSharedPreferences("DATA", MODE_PRIVATE);
        initView();
        initData();
    }


    private void initView() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDataRefresh(DataSynRefreshEvent event) {
        if ("11".equals(event.getCode())) {
            pageNo = 1;
            sortRefrsh(sort, expire);
        }
    }

    private void initData() {
        sort(sort, expire);
        //重新加载
        tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TenderManage.this, TenderPublish.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
        rlLoadFail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlLoadFail.setVisibility(View.GONE);
                llLoad.setVisibility(View.VISIBLE);

                sort(sort, expire);

            }
        });
        tenderManagerIvLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //返回
                finish();
            }
        });

        //
        lvTender.setOnRefreshListener(new UpLoadListView.OnRefreshListener() {

            @Override
            public void onLoadingMore() {
                pageNo++;
                sort(sort, expire);
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
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
                sortRefrsh(sort, expire);

            }
        });
        //发布招标
        rlNotData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TenderManage.this, TenderPublish.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
        //详情
        lvTender.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position < rowList.size()) {
                    Intent intent = new Intent(TenderManage.this, TenderBidQueue.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("TenderBid", rowList.get(position));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
        spSort.setAdapter(new ArrayAdapter<String>(TenderManage.this, R.layout.spanner_item, new String[]{"选择排序方式", "按创建时间升序", "按创建时间降序", "按到期时间升序", "按到期时间降序"}));
        spSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("MyTender", "sort position:" + position);
                pageNo = 1;

                switch (position) {
                    case 0:
                        sort = "";
                        rowList.clear();
                        llLoad.setVisibility(View.VISIBLE);
                        sort(sort, expire);
                        break;
                    case 1:
                        rowList.clear();
                        llLoad.setVisibility(View.VISIBLE);
                        sort = "1";
                        sort(sort, expire);
                        break;
                    case 2:
                        rowList.clear();
                        llLoad.setVisibility(View.VISIBLE);
                        sort = "2";
                        sort(sort, expire);
                        break;
                    case 3:
                        rowList.clear();
                        llLoad.setVisibility(View.VISIBLE);
                        sort = "3";
                        sort(sort, expire);
                        break;
                    case 4:
                        rowList.clear();
                        llLoad.setVisibility(View.VISIBLE);
                        sort = "4";
                        sort(sort, expire);
                        break;

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spExpire.setAdapter(new ArrayAdapter<String>(TenderManage.this, R.layout.spanner_item, new String[]{"筛选方式", "未到期", "已到期"}));

        spExpire.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("MyTender", "expire position:" + position);
                pageNo = 1;
                switch (position) {
                    case 0:
                        expire = "";
                        rowList.clear();
                        llLoad.setVisibility(View.VISIBLE);
                        sort(sort, expire);
                        break;
                    case 1:
                        rowList.clear();
                        llLoad.setVisibility(View.VISIBLE);
                        expire = "1";
                        sort(sort, expire);
                        break;
                    case 2:
                        rowList.clear();
                        llLoad.setVisibility(View.VISIBLE);
                        expire = "2";
                        sort(sort, expire);
                        break;
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

    }

    private void sort(final String sort, String expire) {
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("userId", sp.getString("USER_ID", null));
        mapParams.put("pageNo", pageNo + "");
        mapParams.put("sort", sort);
        mapParams.put("expire", expire);
        RetrofitFactory.getApiService(this)
                .myInviteBidSort(mapParams)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TenderAndBid>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        llLoad.setVisibility(View.GONE);
                        rlLoadFail.setVisibility(View.VISIBLE);
                        pageNo--;
                    }

                    @Override
                    public void onNext(TenderAndBid tenderAndBid) {
                        rlNotData.setVisibility(View.GONE);
                        llLoad.setVisibility(View.GONE);
                        if (tenderAndBid.getResult().total <= rowList.size()) {
                            isHasData = false;
                        } else {
                            rowList.addAll(tenderAndBid.getResult().rows);
                            if (myAdapter == null) {
                                myAdapter = new MyAdapter();
                                lvTender.setAdapter(myAdapter);
                            } else {
                                myAdapter.notifyDataSetChanged();
                            }
                        }
                        handler.sendEmptyMessage(0);//发送消息通知更新界面
                    }
                });
    }

    private void sortRefrsh(final String sort, String expire) {
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("userId", sp.getString("USER_ID", null));
        mapParams.put("pageNo", pageNo + "");
        mapParams.put("sort", sort);
        mapParams.put("expire", expire);
        RetrofitFactory.getApiService(this)
                .myInviteBidSort(mapParams)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TenderAndBid>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        swipeRefreshLayout.setRefreshing(false);

                    }

                    @Override
                    public void onNext(TenderAndBid tenderAndBid) {
                        swipeRefreshLayout.setRefreshing(false);
                        if (tenderAndBid.getResult().total <= rowList.size()) {
                        } else {
                            rowList.clear();
                            rowList.addAll(tenderAndBid.getResult().rows);
                        }
                        handler.sendEmptyMessage(0);//发送消息通知更新界面
                    }
                });
    }

    public class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return rowList.size();
        }

        @Override
        public Object getItem(int position) {
            return rowList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            viewArray = new SparseArray<>();
            ViewHolder viewHolder = null;
            if (viewArray.get(position, null) == null) {
                viewHolder = new ViewHolder();
                convertView = View.inflate(TenderManage.this, R.layout.item_my_publish_tender, null);
                viewHolder.bid_num = (TextView) convertView.findViewById(R.id.tv_bid_num);
                viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
                viewHolder.tv_purchase_num = (TextView) convertView.findViewById(R.id.tv_purchase_num);
                viewHolder.tv_single_price = (TextView) convertView.findViewById(R.id.tv_single_price);
                viewHolder.tv_useful_time = (TextView) convertView.findViewById(R.id.tv_end_date);
                viewHolder.tv_single_price = (TextView) convertView.findViewById(R.id.tv_single_price);
                viewHolder.tv_total = (TextView) convertView.findViewById(R.id.tv_total);
                viewHolder.tv_first_pay = (TextView) convertView.findViewById(R.id.tv_first_pay);
                viewHolder.iv_out_of_date = (ImageView) convertView.findViewById(R.id.iv_out_of_date);
                convertView.setTag(viewHolder);
                viewArray.put(position, convertView);
            } else {
                convertView = viewArray.get(position);
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.bid_num.setText(rowList.get(position).bidList.size() + "");
            viewHolder.tv_title.setText(rowList.get(position).title);
            viewHolder.tv_purchase_num.setText(rowList.get(position).count + "");
            Integer count = rowList.get(position).count;
            viewHolder.tv_single_price.setText(rowList.get(position).targetPrice);
            viewHolder.tv_first_pay.setText(rowList.get(position).downPayment + "%");
            viewHolder.tv_useful_time.setText(DateUtils.format(DateUtils.stringToDate(rowList.get(position).createDate)));
            viewHolder.tv_total.setText(count * Double.parseDouble(rowList.get(position).targetPrice) + "元");
            //设置失效显示
            if (CommonUtil.compare_date(rowList.get(position).expireDate, CommonUtil.dateToString(new Date(System.currentTimeMillis()))) < 0) {
                viewHolder.iv_out_of_date.setVisibility(View.VISIBLE);
            }
            return convertView;
        }
    }

    static class ViewHolder {
        public TextView bid_num;
        public TextView tv_title;
        public TextView tv_purchase_num;
        public TextView tv_single_price;
        public TextView tv_useful_time;
        public TextView tv_total;
        public TextView tv_first_pay;
        public ImageView iv_out_of_date;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Log.e("@@@", "刷新界面");
            rowList.clear();
            pageNo = 1;
            sort(sort, expire);
        }
    }
}
