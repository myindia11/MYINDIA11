package com.example.india11.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.india11.Model.ReferralConnectionModel;
import com.example.india11.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupMembersAdapter extends RecyclerView.Adapter<GroupMembersAdapter.ViewHolder> {
    public Context context;
    private List<ReferralConnectionModel> referralConnectionModelList;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    public GroupMembersAdapter(Context context, List<ReferralConnectionModel> referralConnectionModelList) {
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
        databaseReference.child("INDIA11Users").child(referralConnectionModelList.get(position).getConnectionUid())
                .child("UserInfo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String image = snapshot.child("profilePic").getValue().toString();
                    Glide.with(context).load(image).into(holder.image);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.name.setText(referralConnectionModel.getConnectionName());
        holder.nickName.setText(referralConnectionModel.getConnectionMobile());
        holder.layout.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        return referralConnectionModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView image;
        TextView name, nickName;
        LinearLayout layout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.identification_img);
            name = itemView.findViewById(R.id.player_name);
            nickName = itemView.findViewById(R.id.nick_name);
            layout = itemView.findViewById(R.id.lay);
        }
    }
}
