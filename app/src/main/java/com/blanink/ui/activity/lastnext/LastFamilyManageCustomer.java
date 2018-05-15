package com.blanink.ui.activity.lastnext;

import android.animation.Animator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blanink.R;
import com.blanink.ui.activity.BaseActivity;
import com.blanink.ui.activity.MainActivity;
import com.blanink.http.RetrofitFactory;
import com.blanink.bean.ManyCustomer;
import com.blanink.utils.CheckNet;
import com.blanink.utils.GlideUtils;
import com.blanink.view.RefreshListView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

//上家管理 客户管理
public class LastFamilyManageCustomer extends BaseActivity implements Animator.AnimatorListener {

    private static final int BACK_TASK = 0;
    private TextView iv_customer_manage_last;
    private ImageView iv_add;
    private RefreshListView lv_customer_queue;
    private SharedPreferences sp;
    private List<ManyCustomer.Result.Customer> customers = new ArrayList<ManyCustomer.Result.Customer>();
    private ManyCustomer lmc;
    private MyAdapter adapter;
    private Integer pageNo = 1;//当前页
    private SparseArray<View> viewArray = null;//    /* 稀疏数组：用于缓存已显示过的View */
    private boolean mIsTitleHide = false;
    private boolean mIsAnim = false;
    private float lastX = 0;
    private float lastY = 0;
    Boolean hasData = true;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            //更新UI
            lv_customer_queue.completeRefresh(hasData);
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            } else {
                rl_not_data.setVisibility(View.VISIBLE);
            }
        }

        ;
    };
    private LinearLayout ll_load;
    private RelativeLayout ll_load_fail;
    private LinearLayout ll_title;
    private RelativeLayout rl_seek;
    private RelativeLayout rl_load;
    private RelativeLayout rl_not_data;
    private EditText et_seek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last_family_manage_customer);
        sp = getSharedPreferences("DATA", MODE_PRIVATE);
        initView();
        initData();
    }


    private void initView() {
        iv_customer_manage_last = ((TextView) findViewById(R.id.iv_customer_manage_last));
        lv_customer_queue = ((RefreshListView) findViewById(R.id.lv));
        iv_add = ((ImageView) findViewById(R.id.iv_add));
        ll_load = ((LinearLayout) findViewById(R.id.ll_load));//加载
        ll_load_fail = ((RelativeLayout) findViewById(R.id.rl_load_fail));//加载失败
        ll_title = ((LinearLayout) findViewById(R.id.ll_title));
        rl_seek = ((RelativeLayout) findViewById(R.id.rl_seek));
        rl_load = ((RelativeLayout) findViewById(R.id.rl_load));
        rl_not_data = ((RelativeLayout) findViewById(R.id.rl_not_data));
    }

    private void initData() {
        getData();
        addSeekHeader();
        ll_load_fail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_load_fail.setVisibility(View.GONE);
                ll_load.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getData();
                    }
                }, 1500);
            }
        });
        iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showPopupWindow(v);
                Intent intent = new Intent(LastFamilyManageCustomer.this, LastFamilyNewAddMatche.class);
                startActivity(intent);
            }
        });

        //跳转到客户详情界面
        lv_customer_queue.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("Last", "点击position:" + position);
                if (position > 0 && position < customers.size() + 2) {
                    Intent intent = new Intent(LastFamilyManageCustomer.this, LastCustomerDetail.class);
                    String customerId = customers.get(position - 2).getId();
                    intent.putExtra("companyId", customerId);
                    intent.putExtra("companyA.id", customers.get(position - 2).getCompanyA().getId());
                    intent.putExtra("companyName", customers.get(position - 2).getCompanyA().getName());
                    intent.putExtra("customerState", customers.get(position - 2).getCompanyA().getCreateCompanyBy() == null ? true : false);
                    intent.putExtra("companyType", customers.get(position - 2).getCompanyA().serviceType);
                    intent.putExtra("type", customers.get(position - 2).getType());
                    startActivity(intent);
                }
            }
        });
        //下拉刷新上拉加载
        lv_customer_queue.setOnRefreshListener(new RefreshListView.OnRefreshListener() {
            @Override
            public void onPullRefresh() {
                requestDataFromServer(false);
            }

            @Override
            public void onLoadingMore() {
                //需要联网请求服务器的数据，然后更新UI
                pageNo++;
                Log.e("Last", "pageNo" + pageNo + "");
                requestDataFromServer(true);
            }
        });

        //返回
        iv_customer_manage_last.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intentBackTask = new Intent(this, MainActivity.class);
        intentBackTask.putExtra("DIRECT", BACK_TASK);
        startActivity(intentBackTask);
    }

    // 服务器访问数据库
    private void requestDataFromServer(final boolean isLoadingMore) {

        if (isLoadingMore) {
            getData();
        } else {
            getRefresh();
        }
    }

    private void getRefresh() {

        if (!CheckNet.isNetWorkConnected(LastFamilyManageCustomer.this)) {
            ll_load.setVisibility(View.GONE);
            ll_load_fail.setVisibility(View.VISIBLE);
        } else {
            Map<String, Object> mapParams = new HashMap<String, Object>();
            mapParams.put("userId", sp.getString("USER_ID", "NULL"));
            mapParams.put("pageNo", pageNo + "");
            RetrofitFactory.getApiService(this)
                    .list(mapParams)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<ManyCustomer>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            ll_load.setVisibility(View.GONE);
                            ll_load_fail.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onNext(ManyCustomer manyCustomer) {
                            ll_load.setVisibility(View.GONE);
                            lmc = manyCustomer;
                            if (lmc.getResult().getTotal() >= customers.size()) {
                                hasData = false;
                            } else {
                                hasData = true;
                                int oldIndex = customers.size();
                                customers.addAll(lmc.getResult().getRows());
                                if (adapter == null) {
                                    Log.e("Last", "adapter==null");
                                    adapter = new MyAdapter();
                                    lv_customer_queue.setAdapter(adapter);
                                } else {
                                    Log.e("Last", "adapter!=null");
                                    adapter.notifyDataSetChanged();
                                    lv_customer_queue.setSelection(oldIndex - 1);
                                }
                            }
                            //在UI线程更新UI
                            handler.sendEmptyMessage(0);
                        }
                    });
        }

    }

    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {
        rl_seek.setVisibility(View.VISIBLE);
        if (mIsTitleHide) {
            rl_seek.setVisibility(View.GONE);
        }
        mIsAnim = false;
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }

    public static class ViewHolder {
        public TextView tv_scope;//公司主营
        public TextView tv_name;//公司名称
        public TextView tv_state;//实有 虚拟
        public TextView tv_apply_address;//地址
        public TextView tv_master;//负责人
        public TextView tv_phone;//电话
        public TextView tv_honest;//公司信誉
        public TextView tv_company_apply_remark;//自评
        public TextView tv_company_apply_remark_other;//他评
        ImageView iv;

    }

    public class MyAdapter extends BaseAdapter {
        public MyAdapter() {
            Log.e("Last", "customers ：");
        }

        @Override
        public int getCount() {
            return customers.size();
        }

        @Override
        public Object getItem(int position) {
            return customers.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            viewArray = new SparseArray<View>();
            ViewHolder viewHolder = null;
            Log.e("Last", "getView  customers ：" + customers.size() + "");
            if (viewArray.get(position, null) == null) {
                Log.e("Last", "getView  convertView ==null");
                viewHolder = new ViewHolder();
                convertView = View.inflate(LastFamilyManageCustomer.this, R.layout.item_customer_queue, null);
                viewHolder.tv_scope = (TextView) convertView.findViewById(R.id.tv_scope);
                viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                viewHolder.tv_state = (TextView) convertView.findViewById(R.id.tv_state);
                viewHolder.tv_state.setTag(position);
                viewHolder.tv_master = (TextView) convertView.findViewById(R.id.tv_master);
                viewHolder.tv_phone = (TextView) convertView.findViewById(R.id.tv_phone);
                viewHolder.tv_honest = (TextView) convertView.findViewById(R.id.tv_honest);
                viewHolder.tv_company_apply_remark = (TextView) convertView.findViewById(R.id.tv_company_apply_remark);
                viewHolder.tv_company_apply_remark_other = (TextView) convertView.findViewById(R.id.tv_company_apply_other_remark);
                viewHolder.tv_apply_address = (TextView) convertView.findViewById(R.id.tv_apply_address);
                viewHolder.iv = (ImageView) convertView.findViewById(R.id.iv);

                convertView.setTag(viewHolder);
                viewArray.put(position, convertView);
            } else {
                Log.e("Last", "getView  convertView !=null");
                // 从缓存Map取出View
                convertView = viewArray.get(position);
                viewHolder = (ViewHolder) convertView.getTag();
            }
            ManyCustomer.Result.Customer customer = customers.get(position);
            Log.e("Last", " getView 上家管理 type:" + customer.type);
            viewHolder.tv_scope.setText(customer.companyA.getScope());
            viewHolder.tv_master.setText(customer.companyA.getMaster());
            viewHolder.tv_name.setText(customer.companyA.getName());
            if (customer.companyA.getArea() != null) {
                viewHolder.tv_apply_address.setText(customer.companyA.getArea().getName());
            }
            viewHolder.tv_master.setText(customer.companyA.getMaster());
            viewHolder.tv_phone.setText(customer.companyA.getPhone());
            viewHolder.tv_state.setText(customer.companyA.getCreateCompanyBy() == null ? "实有" : "虚拟");
            Log.e("Last", "position:" + position + "Tag position" + viewHolder.tv_state.getTag());
            if (customer.companyA.getCreateCompanyBy() != null) {
                viewHolder.tv_state.setTextColor(getResources().getColor(R.color.colorBlue));
            }
            DecimalFormat df = new DecimalFormat("0.0");
            viewHolder.tv_honest.setText(df.format((customers.get(position).getCompanyA().reviewOthers + customers.get(position).companyA.reviewSelf) / 2.0));
            viewHolder.tv_company_apply_remark.setText(customers.get(position).companyA.reviewSelf + "");
            viewHolder.tv_company_apply_remark_other.setText(customers.get(position).getCompanyA().reviewOthers + "");
            if (customers.get(position).getCompanyA() != null && customers.get(position).getCompanyA().photo != null && !customers.get(position).getCompanyA().equals("")) {
                GlideUtils.glideImageView(LastFamilyManageCustomer.this, viewHolder.iv, customers.get(position).getCompanyA().photo, true);
            }
            return convertView;
        }

    }

    private void getData() {
        if (!CheckNet.isNetWorkConnected(LastFamilyManageCustomer.this)) {
            ll_load.setVisibility(View.GONE);
            ll_load_fail.setVisibility(View.VISIBLE);
        } else {
            Map<String, Object> mapParams = new HashMap<String, Object>();
            mapParams.put("userId", sp.getString("USER_ID", "NULL"));
            mapParams.put("pageNo", pageNo + "");
            RetrofitFactory.getApiService(this)
                    .list(mapParams)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<ManyCustomer>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            ll_load.setVisibility(View.GONE);
                            ll_load_fail.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onNext(ManyCustomer manyCustomer) {
                            rl_load.setVisibility(View.GONE);
                            ll_load.setVisibility(View.GONE);
                            lmc = manyCustomer;
                            if (lmc.getResult().getTotal() == customers.size()) {
                                hasData = false;

                            } else {
                                int oldIndex = customers.size();
                                customers.addAll(lmc.getResult().getRows());
                                if (adapter == null) {
                                    Log.e("Last", "adapter==null");
                                    adapter = new MyAdapter();
                                    lv_customer_queue.setAdapter(adapter);
                                } else {
                                    Log.e("Last", "adapter!=null");
                                    adapter.notifyDataSetChanged();
                                }
                            }
                            //在UI线程更新UI
                            handler.sendEmptyMessage(0);
                        }
                    });

        }


    }


    @Override
    protected void onStart() {
        super.onStart();
        et_seek.clearFocus();
    }

    /* @Override
     public boolean dispatchTouchEvent(MotionEvent event) {
         super.dispatchTouchEvent(event);
         if (mIsAnim) {
             return false;
         }
         final int action = event.getAction();

         float x = event.getX();
         float y = event.getY();

         switch (action) {
             case MotionEvent.ACTION_DOWN:
                 lastY = y;
                 lastX = x;
                 return false;
             case MotionEvent.ACTION_MOVE:
                 float dY = Math.abs(y - lastY);
                 float dX = Math.abs(x - lastX);
                 boolean down = y > lastY ? true : false;
                 lastY = y;
                 lastX = x;
                 if (dX < 4 && dY > 4 && !mIsTitleHide && !down) {
                     ObjectAnimator animator1 = ObjectAnimator.ofFloat(ll_title, "translationY", 0.0F, -0.5f);
                     animator1.setInterpolator(new AccelerateDecelerateInterpolator());
                     animator1.setDuration(100);
                     animator1.start();
                     animator1.addListener(this);
                 } else if (dX < 4 && dY > 4 && mIsTitleHide && down) {
                     ObjectAnimator animator1 = ObjectAnimator.ofFloat(ll_title, "translationY", -0.5f, 0.0F);
                     animator1.setInterpolator(new AccelerateDecelerateInterpolator());
                     animator1.setDuration(100);
                     animator1.start();
                     animator1.addListener(this);
                 } else {
                     return false;
                 }
                 mIsTitleHide = !mIsTitleHide;
                 mIsAnim = true;
                 break;
             default_load:
                 return false;
         }
         return false;
     }
 */
    public void addSeekHeader() {
        View view = View.inflate(LastFamilyManageCustomer.this, R.layout.layout_header_seek_suppler, null);
        et_seek = ((EditText) view.findViewById(R.id.et_seek));
        lv_customer_queue.addHeaderView(view);
        et_seek.clearFocus();
        et_seek.setCursorVisible(false);
        et_seek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_seek.setFocusable(true);
                et_seek.setCursorVisible(true);
            }
        });
        //设立焦点改变监听事件
        et_seek.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    //跳到搜索界面
                    Log.e("HomeFragment", "焦点:" + hasFocus);
                    et_seek.setCursorVisible(true);
                    Intent intent = new Intent(LastFamilyManageCustomer.this, LastMyCustomerSeek.class);
                    intent.putExtra("flag", "1");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    et_seek.setCursorVisible(false);
                }
            }
        });
    }
}
