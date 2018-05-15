package com.blanink.ui.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.blanink.R;
import com.blanink.http.RetrofitFactory;
import com.blanink.bean.TenderAndBid;
import com.blanink.ui.activity.bidtender.BidSeekTender;
import com.blanink.ui.activity.bidtender.TenderDetail;
import com.blanink.utils.CommonUtil;
import com.blanink.utils.DateUtils;
import com.blanink.utils.GlideUtils;
import com.blanink.view.UpLoadListView;

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

/**
 * Created by Administrator on 2017/7/4.
 */
public class FragmentBid extends Fragment {

    private static final int BACK_TASK = 0;
    @BindView(R.id.lv_tender_info_queue)
    UpLoadListView lvTenderInfoQueue;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.sp_sort)
    Spinner spSort;
    @BindView(R.id.sp_expire)
    Spinner spExpire;
    @BindView(R.id.ll_seek)
    LinearLayout llSeek;
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
    @BindView(R.id.activity_bid_accord_with_tender)
    RelativeLayout activityBidAccordWithTender;
    private MyAdapter myAdapter;
    private SharedPreferences sp;
    private boolean isHasData = true;
    private List<TenderAndBid.Result.Row> rowList = new ArrayList<>();
    private String sort = "1";
    private String expire = "";
    private int pageNo = 1;
    private SparseArray<View> viewSparseArray;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            lvTenderInfoQueue.completeRefresh(isHasData);
            if (myAdapter != null) {
                myAdapter.notifyDataSetChanged();
            } else {

            }
        }
    };
    private Spinner sp_sort;
    private Spinner sp_expire;
    private EditText et_seek;
    private TextView tv_seek;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_bid, null);
        sp = getActivity().getSharedPreferences("DATA", getActivity().MODE_PRIVATE);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();

    }

    private void initData() {
        sort(sort, expire);
        addHeaderView();
        //重新加载
        rlLoadFail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlLoadFail.setVisibility(View.GONE);
                llLoad.setVisibility(View.VISIBLE);
                sort(sort, expire);

            }
        });

        //刷新 加载更多
        lvTenderInfoQueue.setOnRefreshListener(new UpLoadListView.OnRefreshListener() {

            @Override
            public void onLoadingMore() {
                pageNo++;
                sort(sort, expire);
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                Log.e("BID", "firstVisibleItem:" + firstVisibleItem + ",visibleItemCount:" + visibleItemCount);
                if (firstVisibleItem >= 1) {
                    llSeek.setVisibility(View.VISIBLE);
                } else {
                    llSeek.setVisibility(View.GONE);
                }

            }
        });

     /*   WaveSwipeHeader waveSwipeHeader = new WaveSwipeHeader(getActivity());
        waveSwipeHeader.setColorSchemeColors(Color.WHITE, Color.WHITE);
        waveSwipeHeader.setPrimaryColors(getResources().getColor(R.color.colorOrange));
        smartRefreshLayout.setRefreshHeader(waveSwipeHeader);
        smartRefreshLayout.setEnableRefresh(false);
        smartRefreshLayout.setEnableLoadmore(false);//是否开启加上拉加载功能（默认true）
        smartRefreshLayout.setEnableHeaderTranslationContent(false);//拖动Header的时候是否同时拖动内容（默认true）
        smartRefreshLayout.setDisableContentWhenRefresh(true);//是否在刷新的时候禁止内容的一切手势操作（默认false）*/
        //刷新
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

        //招标详情
        lvTenderInfoQueue.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position < rowList.size() + 2) {
                    TenderAndBid.Result.Row row = rowList.get(position - 2);
                    Intent intent = new Intent(getActivity(), TenderDetail.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("TenderDetail", row);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }

            }
        });

        //搜索
        et_seek.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Intent intent = new Intent(getActivity(), BidSeekTender.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });
        tv_seek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BidSeekTender.class);
                startActivity(intent);
            }
        });
        et_seek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_seek.setFocusable(true);
            }
        });
        spSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pageNo = 1;
                switch (position) {
                    case 0:
                        break;
                    case 1:
                        rowList.clear();
                        llLoad.setVisibility(View.VISIBLE);
                        sort = "1";
                        sortFilter(sort, expire);
                        break;
                    case 2:
                        rowList.clear();
                        llLoad.setVisibility(View.VISIBLE);
                        sort = "2";
                        sortFilter(sort, expire);
                        break;
                    case 3:
                        rowList.clear();
                        llLoad.setVisibility(View.VISIBLE);
                        sort = "3";
                        sortFilter(sort, expire);
                        break;
                    case 4:
                        rowList.clear();
                        llLoad.setVisibility(View.VISIBLE);
                        sort = "4";
                        sortFilter(sort, expire);
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sp_sort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pageNo = 1;
                switch (position) {
                    case 0:
                        break;
                    case 1:
                        rowList.clear();
                        llLoad.setVisibility(View.VISIBLE);
                        sort = "1";
                        sortFilter(sort, expire);
                        break;
                    case 2:
                        rowList.clear();
                        llLoad.setVisibility(View.VISIBLE);
                        sort = "2";
                        sortFilter(sort, expire);
                        break;
                    case 3:
                        rowList.clear();
                        llLoad.setVisibility(View.VISIBLE);
                        sort = "3";
                        sortFilter(sort, expire);
                        break;
                    case 4:
                        rowList.clear();
                        llLoad.setVisibility(View.VISIBLE);
                        sort = "4";
                        sortFilter(sort, expire);
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spExpire.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pageNo = 1;
                switch (position) {
                    case 0:
                        expire = "";
                        break;
                    case 1:
                        rowList.clear();
                        llLoad.setVisibility(View.VISIBLE);
                        expire = "1";
                        sortFilter(sort, expire);
                        break;
                    case 2:
                        rowList.clear();
                        llLoad.setVisibility(View.VISIBLE);
                        expire = "2";
                        sortFilter(sort, expire);
                        break;
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sp_expire.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pageNo = 1;
                switch (position) {
                    case 0:
                        expire = "";
                        break;
                    case 1:
                        rowList.clear();
                        llLoad.setVisibility(View.VISIBLE);
                        expire = "1";
                        sortFilter(sort, expire);
                        break;
                    case 2:
                        rowList.clear();
                        llLoad.setVisibility(View.VISIBLE);
                        expire = "2";
                        sortFilter(sort, expire);
                        break;
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        et_seek.clearFocus();//清除焦点
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
            viewSparseArray = new SparseArray<>();
            ViewHolder viewHolder = null;
            if (viewSparseArray.get(position, null) == null) {
                viewHolder = new ViewHolder();
                convertView = View.inflate(getActivity(), R.layout.item_my_tender, null);
                viewHolder.tv_company = (TextView) convertView.findViewById(R.id.tv_company);
                viewHolder.tv_tag = (TextView) convertView.findViewById(R.id.tv_tag);
                viewHolder.tv_purchase_num = (TextView) convertView.findViewById(R.id.tv_tender_purchase_num);
                viewHolder.tv_single_price = (TextView) convertView.findViewById(R.id.tv_single_price);
                viewHolder.tv_useful_time = (TextView) convertView.findViewById(R.id.tv_useful_time);
                viewHolder.tv_single_price = (TextView) convertView.findViewById(R.id.tv_single_price);
                viewHolder.tv_note_content = (TextView) convertView.findViewById(R.id.tv_note_content);
                viewHolder.tv_first_pay = (TextView) convertView.findViewById(R.id.tv_first_pay);
                viewHolder.tv_publish = (TextView) convertView.findViewById(R.id.tv_publish);
                viewHolder.iv_out_of_date = (ImageView) convertView.findViewById(R.id.iv_out_of_date);
                viewHolder.iv = (ImageView) convertView.findViewById(R.id.iv);

                viewSparseArray.put(position, convertView);
                convertView.setTag(viewHolder);
            } else {
                convertView = viewSparseArray.get(position);
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.tv_company.setText(rowList.get(position).inviteCompany.name);
            viewHolder.tv_tag.setText(rowList.get(position).title);
            viewHolder.tv_purchase_num.setText(rowList.get(position).count + "");
            viewHolder.tv_single_price.setText(rowList.get(position).targetPrice);
            viewHolder.tv_first_pay.setText(rowList.get(position).downPayment + "%");
            viewHolder.tv_note_content.setText(Html.fromHtml(rowList.get(position).remarks));
            viewHolder.tv_useful_time.setText(CommonUtil.dateToString(CommonUtil.stringToDate(rowList.get(position).expireDate)));
            viewHolder.tv_publish.setText(DateUtils.format(CommonUtil.stringToDate(rowList.get(position).createDate)));
            //设置失效显示
            if (CommonUtil.compare_date(rowList.get(position).expireDate, CommonUtil.dateToString(new Date(System.currentTimeMillis()))) < 0) {
                viewHolder.iv_out_of_date.setVisibility(View.VISIBLE);
            }
            GlideUtils.glideImageView(getActivity(), viewHolder.iv, rowList.get(position).inviteCompany.photo, true);

            return convertView;
        }
    }

    static class ViewHolder {
        public TextView tv_company;
        public TextView tv_tag;
        public TextView tv_purchase_num;
        public TextView tv_single_price;
        public TextView tv_useful_time;
        public TextView tv_note_content;
        public TextView tv_attachment;
        public TextView tv_first_pay;
        public TextView tv_publish;
        public ImageView iv_out_of_date;
        public ImageView iv;
    }

    //排序
    private void sort(final String sort, String expire) {
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("userId", sp.getString("USER_ID", null));
        mapParams.put("pageNo", pageNo + "");
        mapParams.put("sort", sort);
        mapParams.put("expire", expire);
        RetrofitFactory.getApiService(getActivity())
                .inviteBidSort(mapParams)
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
                        llLoad.setVisibility(View.GONE);
                        swipeRefreshLayout.setRefreshing(false);
                        if (tenderAndBid.getResult().total <= rowList.size()) {
                            isHasData = false;
                        } else {
                            rowList.addAll(tenderAndBid.getResult().rows);
                            if (myAdapter == null) {
                                myAdapter = new MyAdapter();
                                lvTenderInfoQueue.setAdapter(myAdapter);
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
        RetrofitFactory.getApiService(getActivity())
                .inviteBidSort(mapParams)
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
                        rowList.clear();
                        rowList.addAll(tenderAndBid.getResult().rows);
                        isHasData=true;
                        handler.sendEmptyMessage(0);//发送消息通知更新界面
                    }
                });
    }

    private void sortFilter(final String sort, String expire) {
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("userId", sp.getString("USER_ID", null));
        mapParams.put("pageNo", pageNo + "");
        mapParams.put("sort", sort);
        mapParams.put("expire", expire);
        RetrofitFactory.getApiService(getActivity())
                .inviteBidSort(mapParams)
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
                        llLoad.setVisibility(View.GONE);
                        if (tenderAndBid.getResult().rows.size() == 0) {
                            rlNotData.setVisibility(View.VISIBLE);

                        } else {
                            rlNotData.setVisibility(View.GONE);
                        }
                        isHasData = true;
                        rowList.addAll(0, tenderAndBid.getResult().rows);


                        handler.sendEmptyMessage(0);//发送消息通知更新界面
                    }
                });
    }

    public void addHeaderView() {
        View view = View.inflate(getActivity(), R.layout.layout_bid_header, null);
        View viewCategory = View.inflate(getActivity(), R.layout.item_bid_seek_category, null);
        sp_sort = ((Spinner) viewCategory.findViewById(R.id.sp_sort));
        sp_expire = ((Spinner) viewCategory.findViewById(R.id.sp_expire));
        et_seek = (EditText) view.findViewById(R.id.et_seek);
        tv_seek = ((TextView) view.findViewById(R.id.tv_seek));
        lvTenderInfoQueue.addHeaderView(view);
        lvTenderInfoQueue.addHeaderView(viewCategory);
    }
}
