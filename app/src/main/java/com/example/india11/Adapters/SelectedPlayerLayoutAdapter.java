package com.example.india11.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.india11.PreviewAdapters.AllPreviewModel;
import com.example.india11.R;

import java.util.List;

public class SelectedPlayerLayoutAdapter extends RecyclerView.Adapter<SelectedPlayerLayoutAdapter.ViewHolder> {
    public Context context;
    private List<AllPreviewModel> selectedPlayersModelList;

    public SelectedPlayerLayoutAdapter(Context context, List<AllPreviewModel> selectedPlayersModelList) {
        this.context = context;
        this.selectedPlayersModelList = selectedPlayersModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.player_preview_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AllPreviewModel selectedPlayersModel = selectedPlayersModelList.get(position);
        holder.name.setText(selectedPlayersModel.getPlayerName());

    }

    @Override
    public int getItemCount() {
        return selectedPlayersModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView playerImg;
        TextView name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            playerImg = itemView.findViewById(R.id.playerImg);
            name = itemView.findViewById(R.id.player_name);
        }
    }
}
