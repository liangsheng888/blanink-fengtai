package com.blanink.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.blanink.ui.activity.Jpush.JpushComeOrderProductDetail;
import com.blanink.ui.activity.Jpush.JpushGoOrderProductDetail;
import com.blanink.ui.activity.Jpush.JpushTaskResponseDetail;
import com.blanink.ui.activity.MainActivity;
import com.blanink.bean.DataSynRefreshEvent;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * <p>
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "JPush";
    private String CUSTOMER_DELETE = "0";//上家解除
    private String CUSTOMER_APPLAY = "1";    //上家申请
    private String SUPPLER_DELETE = "2";//下家解除
    private String SUPPLER_APPLAY = "3";//下家申请
    private String TASK_ALLOCATION = "4";//任务分配
    private String TASK_RESPONSE = "5";//任务反馈
    private String NOTIFY_RESPONE = "6";//通知回复
    private String BID_ACCORD = "7";//投标合作
    private String REPULSE_A = "8";//打回甲方
    private String REJECT_B = "9";//拒绝生产
    private String DELIVER = "10";//发货
    private String RECEIVE_GOODS = "11";//收货
    private String FLOW_PUBLISH = "12";//流程发布
    private String BID_NOTIFY = "13";//投标

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.e(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.e(TAG, "[MyReceiver] 接收Registration Id : " + regId);
            //send the Registration Id to your server...

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.e(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            //processCustomMessage(context, bundle);

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.e(TAG, "[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.e(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
            //通知刷新
            EventBus.getDefault().post(new DataSynRefreshEvent(true, 9, "更新未读通知数"));
            EventBus.getDefault().post(new DataSynRefreshEvent(true, 6, "我的通知刷新"));

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.e(TAG, "[MyReceiver] 用户点击打开了通知");
            //打开自定义的Activity
            Map<String, String> map = new HashMap<String, String>();
            for (String key : bundle.keySet()) {
                if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                    if (bundle.getString(JPushInterface.EXTRA_EXTRA).isEmpty()) {
                        continue;
                    }
                    try {
                        JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                        Iterator<String> it = json.keys();
                        while (it.hasNext()) {
                            String myKey = it.next().toString();
                            map.put(myKey, json.getString(myKey));
                        }

                    } catch (JSONException e) {
                    }

                }
            }
            Log.e(TAG,map.toString());
            Intent i = null;

            String type = map.get("type");
            String orderProductId = map.get("orderProductId");
            String orderId = map.get("orderId");
            Log.e(TAG, "type:" + type + ",orderProductId:" + orderProductId + ",orderId:" + orderId);
            if (CUSTOMER_DELETE.equals(type)) {
                i = new Intent(context, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("DIRECT", 2);
                context.startActivity(i);
            } else if (CUSTOMER_APPLAY.equals(type)) {
                i = new Intent(context, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("DIRECT", 2);
                context.startActivity(i);
            } else if (SUPPLER_DELETE.equals(type)) {
                i = new Intent(context, MainActivity.class);
                i.putExtra("DIRECT", 2);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            } else if (SUPPLER_APPLAY.equals(type)) {
                i = new Intent(context, MainActivity.class);
                i.putExtra("DIRECT", 2);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            } else if (NOTIFY_RESPONE.equals(type)) {
                i = new Intent(context, MainActivity.class);
                i.putExtra("DIRECT", 2);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
            //跳转到产品详情
            else if (REPULSE_A.equals(type)) {
                i = new Intent(context, JpushComeOrderProductDetail.class);
                Log.e(TAG, "跳转到来单产品详情");
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("orderProductId", orderProductId);
                i.putExtra("orderId", orderId);
                context.startActivity(i);
            }
            //跳转到产品详情
            else if (REJECT_B.equals(type)||FLOW_PUBLISH.equals(type)) {
                i = new Intent(context, JpushGoOrderProductDetail.class);
                Log.e(TAG, "跳转到去单产品详情");
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("orderProductId", orderProductId);
                i.putExtra("orderId", orderId);
                context.startActivity(i);

            } else if(TASK_ALLOCATION.equals(type)){
                //任务分配
                i = new Intent(context, JpushTaskResponseDetail.class);
                Log.e(TAG, "跳转到去单产品详情");
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("flowId", map.get("flowId"));
                i.putExtra("processId", map.get("processId"));
                i.putExtra("workPlanId",map.get("workPlanId"));
                context.startActivity(i);

            }


        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.e(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.e(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
        } else {
            Log.e(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
                    Log.i(TAG, "This message has no Extra data");
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next().toString();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " + json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }

	/*//send msg to MainActivity
	private void processCustomMessage(Context context, Bundle bundle) {
		if (MainActivity.isForeground) {
			String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
			String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
			Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
			msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
			if (!CommonUtil.isEmpty(extras)) {
				try {
					JSONObject extraJson = new JSONObject(extras);
					if (extraJson.length() > 0) {
						msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
					}
				} catch (JSONException e) {

				}

			}
			context.sendBroadcast(msgIntent);
		}
	}*/
}
