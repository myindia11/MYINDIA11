package com.example.india11.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.india11.BottomNavigation.Chat;
import com.example.india11.Common;
import com.example.india11.Contests.ContestsDetails;
import com.example.india11.Model.GroupModel;
import com.example.india11.R;

import org.w3c.dom.Text;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AvailableGroupAdapter extends RecyclerView.Adapter<AvailableGroupAdapter.ViewHolder> {
    public Context context;
    private List<GroupModel> groupModelList;

    public AvailableGroupAdapter(Context context, List<GroupModel> groupModelList) {
        this.context = context;
        this.groupModelList = groupModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.available_group_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GroupModel groupModel = groupModelList.get(position);
        holder.name.setText(groupModel.getGroupName());
        holder.gid.setText(groupModel.getGroupId());
        Glide.with(context).load(groupModel.getGroupPic()).into(holder.image);
        holder.itemView.setOnClickListener(view -> {
            Common.groupId = groupModelList.get(position).getGroupId();
            Common.groupName = groupModelList.get(position).getGroupName();
            Common.groupPic = groupModelList.get(position).getGroupPic();
            AppCompatActivity activity = (AppCompatActivity) view.getContext();
            Fragment chat = new Chat();
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.container, chat)
                    .addToBackStack(null).commit();
        });

    }

    @Override
    public int getItemCount() {
        return groupModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView image;
        TextView name, gid;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.grp_pic);
            name = itemView.findViewById(R.id.grp_name);
            gid = itemView.findViewById(R.id.grp_id);
        }
    }
}
