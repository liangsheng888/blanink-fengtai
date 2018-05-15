package com.blanink.ui.activity.task;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blanink.R;
import com.blanink.ui.activity.BaseActivity;
import com.blanink.ui.adapter.ProcessAdapter;
import com.blanink.bean.ListProcess;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListProcessActivity extends BaseActivity {


    @BindView(R.id.come_order_tv_seek)
    TextView comeOrderTvSeek;
    @BindView(R.id.iv_last)
    TextView ivLast;
    @BindView(R.id.come_order)
    RelativeLayout comeOrder;
    @BindView(R.id.ll_bottom)
    LinearLayout llBottom;
    @BindView(R.id.listView)
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_process_acitvity);
        ButterKnife.bind(this);
        initData();

    }

    private void initData() {
        String json = getIntent().getStringExtra("processJson");
        final List<ListProcess.ResultBean> listProcesses = new Gson().fromJson(json, new TypeToken<List<ListProcess.ResultBean>>() {
        }.getType());
        listView.setAdapter(new ProcessAdapter(this, listProcesses));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if("3".equals(listProcesses.get(position).getType())){
                    //发货
                    startActivity(ScanDeliverResponse.getIntent(ListProcessActivity.this,getIntent().getStringExtra("orderProductId"),listProcesses.get(position).getId()));
                }else{
                    startActivity(ScanTaskResponseActivity.getIntent(ListProcessActivity.this,getIntent().getStringExtra("orderProductId"),listProcesses.get(position).getId()));
                }
            }
        });
        ivLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public static Intent getIntent(Context context, String orderProductId, String json) {
        Intent intent = new Intent(context, ListProcessActivity.class);
        intent.putExtra("orderProductId", orderProductId);
        intent.putExtra("processJson", json);
        return intent;
    }
}
