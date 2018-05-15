package com.blanink.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blanink.R;

import java.util.List;

/**
 * 作者：Created by liangshen on 2018/1/12 0012 15:16
 * 邮箱：liangshen6868@163.com
 */
public class TextAdapter extends RecyclerView.Adapter<TextAdapter.DashboardViewHolder> {
    private Context context;
    private List<String> textList;

    public TextAdapter(Context context, List<String> textList) {
        this.context = context;
        this.textList = textList;
    }

    @Override
    public DashboardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_text, parent, false);
        return new DashboardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DashboardViewHolder holder, final int position) {
         holder.textView.setText(textList.get(position));
        
    }

    @Override
    public int getItemCount() {
        return textList.size();
    }

    public static class DashboardViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public DashboardViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.tv_name);
        }
    }
}
