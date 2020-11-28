package com.example.myparking;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;


public class ReAdapter extends RecyclerView.Adapter<ReAdapter.ViewHolder> {
    private List<ListItem> listItems;
    private Context context;
    DatabaseReference mRef;
    FirebaseAuth mAuth;
    public ReAdapter(Context context,List<ListItem> listItems) {
        this.listItems = listItems;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ListItem listItem=listItems.get(position);
        holder.parkingname.setText(listItem.getName());
        holder.parkingaddress.setText(listItem.getParkingaddress());
        holder.openingtime.setText(listItem.getOpentime());
        holder.closingtime.setText(listItem.getClosingtime());
        Picasso.with(context).load(listItem.getParkingimage()).into(holder.parkingprofile);
        holder.contactdetails.setText(listItem.getContactinfo());
//        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(context,"YouViewed"+listItem.getUser(),Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView parkingname;
        public TextView parkingaddress;
        public  TextView openingtime;
        public TextView closingtime;
        public ImageView parkingprofile;
        public TextView contactdetails;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parkingname=itemView.findViewById(R.id.parkingplacename);
            parkingaddress=itemView.findViewById(R.id.parkingplaceaddress);
            openingtime=itemView.findViewById(R.id.open_time_item);
            closingtime=itemView.findViewById(R.id.close_time_item);
            parkingprofile=itemView.findViewById(R.id.parkingimages);
            contactdetails=itemView.findViewById(R.id.contact_number);
        }
    }
}
