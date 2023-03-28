package com.example.garbagesorting.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.garbagesorting.R;

/**
 * 被装饰类要和装饰类继承自同一父类
 */

public class MyAdapter extends BaseAdapter<String> {
    private Context mContext;

    public MyAdapter(Context context) {
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.findmodel, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((MyViewHolder) holder).bind(getDataSet().get(position));
    }


    static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_find;
        TextView tv_name;
        TextView tv_text;
        ImageView iv_touxiang;
        public MyViewHolder(View itemView) {
            super(itemView);


            iv_find=itemView.findViewById(R.id.iv_find);
            tv_name=itemView.findViewById(R.id.tv_name);
            tv_text=itemView.findViewById(R.id.tv_text);
            iv_touxiang=itemView.findViewById(R.id.iv_touxiang);
        }

        public void bind(CharSequence content) {
            tv_text.setText(content);
        }
    }

}
