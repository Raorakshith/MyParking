package com.example.myparking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ShowParking extends AppCompatActivity {
ImageView bck;
SearchView searchView;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter,adapter1;
    private List<ListItem> listItems;
    DatabaseReference mRef;
    TextView display;
    String currentday;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_parking);
        currentday=new SimpleDateFormat("EEEE", Locale.ENGLISH).format(System.currentTimeMillis());
        display=findViewById(R.id.parkingavailability);
        searchView=findViewById(R.id.searchbar);
        recyclerView=findViewById(R.id.recyclerview);
        bck=findViewById(R.id.back_4);
        display.setText("Parking Availability: "+currentday);
        bck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        listItems = new ArrayList<>();
        mRef= FirebaseDatabase.getInstance().getReference(currentday);
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    ListItem com = postSnapshot.getValue(ListItem.class);
                    listItems.add(com);
                }
                adapter = new ReAdapter(ShowParking.this, listItems);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ShowParking.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                search(newText);
                return true;
            }
        });
    }

    private void search(String string){

        ArrayList<ListItem> myList= new ArrayList<>();
        for (ListItem object:listItems){
            if(object.getName().toLowerCase().contains(string.toLowerCase())){
                myList.add(object);
            }
        }
        adapter1=new ReAdapter(ShowParking.this,myList);
        recyclerView.setAdapter(adapter1);
    }
}