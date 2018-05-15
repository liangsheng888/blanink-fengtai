package com.blanink.ui.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.blanink.R;
import com.blanink.bean.ListProcess;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者：Created by liangshen on 2017/12/22 0022 15:44
 * 邮箱：liangshen6868@163.com
 */
public class ProcessAdapter extends BaseAdapter {
    private Context context;
    private List<ListProcess.ResultBean> listProcesses;
    private SparseArray<View> sparseArray;
    public ProcessAdapter(Context context, List<ListProcess.ResultBean> listProcesses) {
        this.context = context;
        this.listProcesses = listProcesses;
    }

    @Override
    public int getCount() {
        return listProcesses.size();
    }

    @Override
    public Object getItem(int position) {
        return listProcesses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        sparseArray=new SparseArray<>();
        if(sparseArray.get(position,null)==null){
            convertView=View.inflate(context, R.layout.item_task_response_queue, null);
            viewHolder=new ViewHolder(convertView);
            convertView.setTag(viewHolder);
            sparseArray.put(position,convertView);
        }else {
            convertView=sparseArray.get(position);
            viewHolder=(ViewHolder) convertView.getTag();
        }
        viewHolder.tvProcess.setText(listProcesses.get(position).getName());
        if ("3".equals(listProcesses.get(position).getType())) {
            viewHolder.tvState.setText("发货");
        }else{
            viewHolder.tvState.setText("反馈");
        }
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.tv_process)
        TextView tvProcess;
        @BindView(R.id.tv_state)
        TextView tvState;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
