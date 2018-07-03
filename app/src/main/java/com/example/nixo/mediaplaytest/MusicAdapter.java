package com.example.nixo.mediaplaytest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.ViewHolder> {

    private Context context;
    private ArrayList<String> list;
    ItemOnClickListener listener;

    public MusicAdapter(Context context, ArrayList<String> list,ItemOnClickListener listener){
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView num;
        TextView name;
        Button start;

        public ViewHolder(View itemView) {
            super(itemView);
            start = itemView.findViewById(R.id.start_music);
            name = itemView.findViewById(R.id.name);
            num = itemView.findViewById(R.id.number);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.music_item,parent,false);
        }

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.name.setText(list.get(position));
        holder.num.setText(position+"");
        holder.num.setSelected(true);
        holder.start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             listener.getMusicPosition(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}
