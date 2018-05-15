package com.blanink.ui.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blanink.R;
import com.blanink.ui.activity.lastnext.ProductDetail;
import com.blanink.http.RetrofitFactory;
import com.blanink.bean.CompanyProduct;
import com.blanink.utils.CommonUtil;
import com.blanink.utils.GlideUtils;
import com.blanink.utils.StringToListUtils;
import com.blanink.view.UpLoadListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/2/9.
 * 下家 产品
 */
public class NextProduct extends Fragment {
    public String companyId;
    @BindView(R.id.lv_product)
    UpLoadListView lvProduct;
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
    @BindView(R.id.tv_not_net)
    TextView tvNotNet;
    @BindView(R.id.rl_not_net)
    RelativeLayout rlNotNet;
    @BindView(R.id.fl_load)
    FrameLayout flLoad;
    Unbinder unbinder;
    private List<CompanyProduct.Result.Row> rowList = new ArrayList<CompanyProduct.Result.Row>();
    private int pageNo = 1;
    private SparseArray<View> sparseArray;
    private Boolean isHasData = true;
    private MyAdapter adater;
    private LinearLayout ll_load;
    private RelativeLayout ll_load_fail;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            //更新UI
            lvProduct.completeRefresh(isHasData);
            if (adater != null)
                adater.notifyDataSetChanged();
        }

    };
    private SharedPreferences sp;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        companyId = getActivity().getIntent().getStringExtra("companyB.id");
        Log.e("partner", "NextProduct  companyId:" + companyId);
        sp = getActivity().getSharedPreferences("DATA", getActivity().MODE_PRIVATE);
        View view = View.inflate(getActivity(), R.layout.fragment_company_product, null);
        initView(view);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    private void initView(View view) {
        ll_load = ((LinearLayout) view.findViewById(R.id.ll_load));//加载
        ll_load_fail = ((RelativeLayout) view.findViewById(R.id.rl_load_fail));//加载失败
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getData();
        //加载失败 重新加载
        ll_load_fail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_load_fail.setVisibility(View.GONE);
                ll_load.setVisibility(View.VISIBLE);
                getData();
            }
        });
        lvProduct.setOnRefreshListener(new UpLoadListView.OnRefreshListener() {
            @Override
            public void onLoadingMore() {
                pageNo++;
                getData();
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
                pageNo=1;
                refreshData();
            }
        });

        //产品详情
        lvProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position < rowList.size()) {
                    CompanyProduct.Result.Row row = rowList.get(position);
                    Intent intent = new Intent(getActivity(), ProductDetail.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("ProductDetail", row);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
    }

    private void refreshData() {
        if (!CommonUtil.isConnected(getActivity())) {
            //判断网络是否连接
            ll_load.setVisibility(View.GONE);
            ll_load_fail.setVisibility(View.VISIBLE);
            Toast.makeText(getActivity(), "网络异常", Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("pageNo", pageNo + "");
        mapParams.put("company.id", companyId);
        mapParams.put("userId", sp.getString("USER_ID", null));
        mapParams.put("currentUser.id", sp.getString("USER_ID", null));
        RetrofitFactory.getApiService(getActivity())
                .productList(mapParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CompanyProduct>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        swipeRefreshLayout.setRefreshing(false);

                    }

                    @Override
                    public void onNext(CompanyProduct companyProduct) {
                        swipeRefreshLayout.setRefreshing(false);
                        if (companyProduct.result.rows.size()==0) {
                            Log.e("LastProduct", "没有多余的数据了:");
                            isHasData = false;
                        } else {
                            isHasData=true;
                            rowList.clear();
                            rowList.addAll(companyProduct.result.rows);

                        }
                        handler.sendEmptyMessage(0);
                    }
                });
    }

    public void getData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!CommonUtil.isConnected(getActivity())) {
                    //判断网络是否连接
                    ll_load.setVisibility(View.GONE);
                    ll_load_fail.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(), "网络异常", Toast.LENGTH_SHORT).show();
                    return;
                }
                Map<String, Object> mapParams = new HashMap<String, Object>();
                mapParams.put("pageNo", pageNo + "");
                mapParams.put("company.id", companyId);
                mapParams.put("userId", sp.getString("USER_ID", null));
                mapParams.put("currentUser.id", sp.getString("USER_ID", null));
                RetrofitFactory.getApiService(getActivity())
                        .productList(mapParams)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<CompanyProduct>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                ll_load.setVisibility(View.GONE);
                                ll_load_fail.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onNext(CompanyProduct companyProduct) {
                                ll_load.setVisibility(View.GONE);
                                if(companyProduct.result.total==0){
                                    rlNotData.setVisibility(View.VISIBLE);
                                }
                                if (companyProduct.result.total <= rowList.size()) {
                                    Log.e("LastProduct", "没有多余的数据了:");
                                    isHasData = false;
                                } else {
                                    rowList.addAll(companyProduct.result.rows);
                                    if (adater == null) {
                                        Log.e("LastProduct", "adater==null:");
                                        adater = new MyAdapter();
                                        lvProduct.setAdapter(adater);
                                    } else {
                                        Log.e("LastProduct", "adater!=null:");
                                        adater.notifyDataSetChanged();
                                    }
                                }
                                handler.sendEmptyMessage(0);
                            }
                        });
            }
        }, 1000);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
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
            Log.e("LastProduct", "getView");
            sparseArray = new SparseArray<View>();
            ViewHolder viewHolder = null;
            if (sparseArray.get(position, null) == null) {
                viewHolder = new ViewHolder();
                convertView = View.inflate(getActivity(), R.layout.item_product_queue, null);
                viewHolder.iv_product_picture = ((ImageView) convertView.findViewById(R.id.iv_product_picture));
                viewHolder.tv_product_name = (TextView) convertView.findViewById(R.id.tv_product_name);
                viewHolder.tv_address = (TextView) convertView.findViewById(R.id.tv_address);
                viewHolder.tv_high_price = (TextView) convertView.findViewById(R.id.tv_high_price);
                viewHolder.tv_down_price = (TextView) convertView.findViewById(R.id.tv_down_price);
                viewHolder.tv_specific_description = (TextView) convertView.findViewById(R.id.tv_specific_description);
                convertView.setTag(viewHolder);
                sparseArray.put(position, convertView);
            } else {
                convertView = sparseArray.get(position);
                viewHolder = (ViewHolder) convertView.getTag();
            }
            Log.e("LastProduct", rowList.toString());
            //x.image().bind(viewHolder.iv_product_picture,BaseUrlUtils.NET_URL+rowList.get(position).productPhotos);
            if (rowList.get(position).productPhotos != null) {
                GlideUtils.glideImageView(getActivity(), viewHolder.iv_product_picture, StringToListUtils.stringToList(rowList.get(position).productPhotos, "\\|").get(0), false);
            }

            viewHolder.tv_product_name.setText(rowList.get(position).productName);
            viewHolder.tv_high_price.setText(rowList.get(position).productPriceHighline);
            viewHolder.tv_down_price.setText(rowList.get(position).productPriceDownline);
            viewHolder.tv_specific_description.setText(rowList.get(position).productDescription);
            return convertView;
        }
    }

    static class ViewHolder {
        ImageView iv_product_picture;
        TextView tv_product_name;
        TextView tv_address;
        TextView tv_down_price;
        TextView tv_high_price;
        TextView tv_specific_description;
    }
}
