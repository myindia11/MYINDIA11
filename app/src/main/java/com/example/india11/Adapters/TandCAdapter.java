package com.example.india11.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.india11.Model.TandCModel;
import com.example.india11.R;

import java.util.List;

public class TandCAdapter extends RecyclerView.Adapter<TandCAdapter.ViewHolder> {
    private Context context;
    private List<TandCModel> tandCModelList;

    public TandCAdapter(Context context, List<TandCModel> tandCModelList) {
        this.context = context;
        this.tandCModelList = tandCModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.tandc_item_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TandCModel tandCModel = tandCModelList.get(position);
        holder.head.setText(tandCModel.getHeading());
        holder.subHead.setText(tandCModel.getSubheading());

    }

    @Override
    public int getItemCount() {
        return tandCModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView head, subHead;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            head = itemView.findViewById(R.id.heading);
            subHead = itemView.findViewById(R.id.subHeading);
        }
    }
}
