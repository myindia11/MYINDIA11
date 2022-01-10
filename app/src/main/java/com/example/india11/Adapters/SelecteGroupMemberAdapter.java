package com.example.india11.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.india11.Common;
import com.example.india11.Model.GroupModel;
import com.example.india11.Model.ReferralConnectionModel;
import com.example.india11.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SelecteGroupMemberAdapter extends RecyclerView.Adapter<SelecteGroupMemberAdapter.ViewHolder> {
    public Context context;
    private List<ReferralConnectionModel> referralConnectionModelList;
    DatabaseReference databaseReferencePlus,databaseReferenceMinus,databaseReference;
    String connectionName, connectionMobile, connectionPic, connectionUid;
    String groupId, groupName, groupDescription, groupPic,groupCreatedTime, groupAdmin,groupStatus;
    Integer groupSize;

    public SelecteGroupMemberAdapter(Context context, List<ReferralConnectionModel> referralConnectionModelList) {
        this.context = context;
        this.referralConnectionModelList = referralConnectionModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_groupmember_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ReferralConnectionModel referralConnectionModel = referralConnectionModelList.get(position);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReferenceMinus = FirebaseDatabase.getInstance().getReference();
        databaseReferencePlus = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("INDIA11Users").child(referralConnectionModelList.get(position).getConnectionUid())
                .child("UserInfo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String nickName = snapshot.child("nickName").getValue().toString();
                    holder.nickName.setText(nickName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.name.setText(referralConnectionModel.getConnectionName());
        Glide.with(context).load(referralConnectionModel.getConnectionPic()).into(holder.image);
        holder.plus.setOnClickListener(view -> {
            connectionName = referralConnectionModelList.get(position).getConnectionName();
            connectionMobile = referralConnectionModelList.get(position).getConnectionMobile();
            connectionPic = referralConnectionModelList.get(position).getConnectionPic();
            connectionUid = referralConnectionModelList.get(position).getConnectionUid();
            ReferralConnectionModel referralConnectionModel1 = new ReferralConnectionModel(connectionName, connectionMobile, connectionPic, connectionUid);
            databaseReferencePlus.child("ChatGroups").child(Common.groupId).child("GroupMembers").child(connectionUid)
                    .setValue(referralConnectionModel1);
            groupId = Common.groupId;
            groupName = Common.groupName;
            groupDescription = "";
            groupPic = Common.groupPic;
            groupCreatedTime = Common.groupCreation;
            groupAdmin = "";
            groupSize = 1;
            groupStatus = "NO";
            GroupModel groupModel = new GroupModel(groupId, groupName, groupDescription, groupPic,groupCreatedTime,
                    groupAdmin,groupStatus,groupSize);
            databaseReferenceMinus.child("INDIA11Users").child(referralConnectionModelList.get(position).getConnectionUid())
                    .child("Groups").child(groupId).setValue(groupModel);
            holder.minus.setVisibility(View.VISIBLE);
            holder.plus.setVisibility(View.GONE);
        });
        holder.minus.setOnClickListener(view -> {
            groupId = Common.groupId;
            connectionUid = referralConnectionModelList.get(position).getConnectionUid();
            databaseReferenceMinus.child("ChatGroups").child(Common.groupId).child("GroupMembers").child(connectionUid)
                    .removeValue();
            databaseReferencePlus.child("INDIA11Users").child(referralConnectionModelList.get(position).getConnectionUid())
                    .child("Groups").child(groupId).removeValue();
            holder.plus.setVisibility(View.VISIBLE);
            holder.minus.setVisibility(View.GONE);
        });

    }

    @Override
    public int getItemCount() {
        return referralConnectionModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView image;
        TextView name, nickName;
        ImageView plus, minus;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.identification_img);
            name = itemView.findViewById(R.id.player_name);
            nickName = itemView.findViewById(R.id.nick_name);
            plus = itemView.findViewById(R.id.plus);
            minus = itemView.findViewById(R.id.minus);
        }
    }
}
