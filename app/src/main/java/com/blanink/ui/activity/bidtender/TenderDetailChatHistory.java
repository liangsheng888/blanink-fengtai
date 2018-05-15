package com.blanink.ui.activity.bidtender;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blanink.R;
import com.blanink.ui.activity.BaseActivity;
import com.blanink.ui.adapter.ChatMsgViewAdapter;
import com.blanink.http.RetrofitFactory;
import com.blanink.bean.ChatMsgEntity;
import com.blanink.bean.Response;
import com.blanink.bean.TalkHistoryLog;
import com.blanink.view.KeyMapDialogFlow;
import com.blanink.view.UpLoadListView;

import java.text.SimpleDateFormat;
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
 * 投标详情 历史回复记录
 */
public class TenderDetailChatHistory extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.btn_back)
    TextView btnBack;
    @BindView(R.id.tv_company_name)
    TextView tvCompanyName;
    @BindView(R.id.rl_layout)
    RelativeLayout rlLayout;
    @BindView(R.id.et_sendmessage)
    TextView etSendmessage;
    @BindView(R.id.rl_bottom)
    RelativeLayout rlBottom;
    @BindView(R.id.listview)
    UpLoadListView listview;
    private SharedPreferences sp;
    private int pageNo = 1;
    private Button mBtnSend;// 发送btn
    private TextView mBtnBack;// 返回btn
    private TextView mEditTextContent;
    private UpLoadListView mListView;
    private ChatMsgViewAdapter mAdapter;// 消息视图的Adapter
    private List<ChatMsgEntity> mDataArrays = new ArrayList<ChatMsgEntity>();// 消息对象数组
    private TalkHistoryLog oldTalkHistoryLog;
    private String bidId;
    private String inviteBidId;
    private boolean isHasData = true;//判断是否有数据
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mListView.completeRefresh(isHasData);
            if (mAdapter != null)
                mAdapter.notifyDataSetChanged();
        }
    };
    private String contString;
    private String createBy;
    private KeyMapDialogFlow keyMapDialogFlow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_main);
        ButterKnife.bind(this);
        sp = getSharedPreferences("DATA", MODE_PRIVATE);
        receivedBidDetail();
        initView();
        initData();
    }

    private void receivedBidDetail() {
        Intent intent = getIntent();
        bidId = intent.getStringExtra("bid.id");
        inviteBidId = intent.getStringExtra("inviteBid.id");
        createBy = intent.getStringExtra("createBy");
        Log.e("BidDetailChatHistory", "createBy:" + createBy);
    }

    private void initView() {
        mListView = (UpLoadListView) findViewById(R.id.listview);
        mBtnBack = (TextView) findViewById(R.id.btn_back);
        mBtnBack.setOnClickListener(this);
        mEditTextContent = (TextView) findViewById(R.id.et_sendmessage);


    }

    private void initData() {
        mEditTextContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 keyMapDialogFlow=new KeyMapDialogFlow("去沟通", new KeyMapDialogFlow.SendBackListener() {
                    @Override
                    public void sendBack(String inputText) {
                        ChatMsgEntity entity = new ChatMsgEntity();
                        entity.setName(sp.getString("NAME", null));
                        entity.setDate(getDate());
                        entity.setMessage(inputText);
                        entity.setMsgType(false);
                        mDataArrays.add(0, entity);
                        mAdapter.notifyDataSetChanged();// 通知ListView，数据已发生改变
                        mEditTextContent.setText("");// 清空编辑框数据
                        mListView.setSelection(0);// 发送一条消息时，ListView显示选择最后一项
                        loadMessagetoServer(inputText);
                    }
                });
                keyMapDialogFlow.show(getSupportFragmentManager(),"dialog");
            }
        });
        tvCompanyName.setText(getIntent().getStringExtra("companyName"));
        loadChatLogFromServer();
        mListView.setOnRefreshListener(new UpLoadListView.OnRefreshListener() {
            @Override
            public void onLoadingMore() {
                pageNo++;
                loadChatLogFromServer();
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    public void loadChatLogFromServer() {
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("userId", sp.getString("USER_ID", null));
        mapParams.put("bid.id", bidId);
        mapParams.put("inviteBid.id", inviteBidId);
        mapParams.put("pageNo", pageNo + "");
        RetrofitFactory.getApiService(this)
                .bidReturn(mapParams)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TalkHistoryLog>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(TalkHistoryLog talkHistoryLog) {
                        Log.e("BidDetailChatHistory", "talkHistoryLog:" + talkHistoryLog.toString());
                        oldTalkHistoryLog=talkHistoryLog;
                        if (talkHistoryLog.result.total <= mDataArrays.size()) {
                            isHasData = false;
                        } else {
                            for (TalkHistoryLog.Result.Row row :
                                    talkHistoryLog.result.rows) {
                                ChatMsgEntity  entity = new ChatMsgEntity();
                                if (row.createBy.id.equals(sp.getString("USER_ID", null))) {
                                    entity.setName(sp.getString("NAME", ""));
                                    entity.setMsgType(false);// 自己发送的消息
                                } else {
                                    entity.setName(row.createBy.name);
                                    entity.setMsgType(true);
                                }
                                Log.e("BidDetailChatHistory", "receiverId:" + row.receiveUser.id + "userId:" + sp.getString("USER_ID", null));
                                entity.setMessage(row.message);
                                entity.setDate(row.createDate);
                                mDataArrays.add(entity);
                            }
                        }
                        Log.e("BidDetailChatHistory", "mDataArrays:" + mDataArrays.toString());
                        if (mAdapter == null) {
                            mAdapter = new ChatMsgViewAdapter(TenderDetailChatHistory.this, mDataArrays);
                            mListView.setAdapter(mAdapter);
                        } else {
                            mAdapter.notifyDataSetChanged();
                        }
                        handler.sendEmptyMessage(0);

                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:// 返回按钮点击事件
                finish();// 结束,实际开发中，可以返回主界面
                break;
        }
    }

    /**
     * 发送消息
     */
    private void send() {
        contString = mEditTextContent.getText().toString();
        if (contString.length() > 0) {

        }

    }

    /**
     * 发送消息时，获取当前事件
     *
     * @return 当前时间
     */
    private String getDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return format.format(new Date());
    }
    public void loadMessagetoServer(String inputText) {
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("userId", sp.getString("USER_ID", null));
        mapParams.put("currentUser.id", sp.getString("USER_ID", null));
        mapParams.put("inviteBid.id", inviteBidId);
        mapParams.put("bid.id", bidId);
        if (oldTalkHistoryLog.result.rows.size() == 0) {
            mapParams.put("receiveUser", createBy);
        } else {
            mapParams.put("receiveUser", oldTalkHistoryLog.result.rows.get(0).receiveUser.id);
            Log.e("BidDetailChatHistory", "senddata:[" + "createBy：" + oldTalkHistoryLog.result.rows.get(0).receiveUser.id + "]");
        }

        mapParams.put("message", inputText);
        RetrofitFactory.getApiService(this)
                .bidAddReturn(mapParams)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        keyMapDialogFlow.hideProgressdialog();
                    }

                    @Override
                    public void onNext(Response response) {
                        keyMapDialogFlow.hideProgressdialog();
                        keyMapDialogFlow.dismiss();
                    }
                });
    }

}
