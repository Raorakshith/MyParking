package com.example.myparking;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class EditProfile extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener{

    EditText name,ownname,contacts,p4w,p2w;
    double lats, longs;
    TextView frtm,totm,disptm;
    Button save,daybtn;
    Spinner days;
    FirebaseAuth auth;
    DatabaseReference dref,sRef;
    DatePickerDialog datePicker;
    int year;
    int month;
    int dayOfMonth;
    Calendar calendar;
    ImageButton back;
    List<String> listspin;
    ArrayAdapter<String> spinneradapter;
    String day;
    TimePickerDialog PickerDialog;
    private FusedLocationProviderClient client;
    private GoogleApiClient googleApiClient;
    private Location mlocation;
    private LocationManager locationManager;
    private LocationRequest locationRequest;
    int currentHour,currentHour1;
    int currentMinute,currentMinute1;
    String ampm,ampm1,starting,ending,lat,longo,address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        disptm=findViewById(R.id.displaytime);
        name = findViewById(R.id.place_name);
        ownname = findViewById(R.id.owner_name);
        contacts = findViewById(R.id.contact_details);
        p4w = findViewById(R.id.p4w_edit);
        p2w = findViewById(R.id.p2w_edit);
        save = findViewById(R.id.save_btn);
        days = findViewById(R.id.days_edit);
        back = findViewById(R.id.back_3);
        frtm=findViewById(R.id.fromclk);
        totm=findViewById(R.id.toclk);
        daybtn=findViewById(R.id.day_btn);
        auth = FirebaseAuth.getInstance();
        googleApiClient = new GoogleApiClient.Builder(EditProfile.this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        dref = FirebaseDatabase.getInstance().getReference("Users").child(auth.getCurrentUser().getUid());
        listspin=new ArrayList<String>();
        listspin.add("Sunday");
        listspin.add("Monday");
        listspin.add("Tuesday");
        listspin.add("Wednesday");
        listspin.add("Thursday");
        listspin.add("Friday");
        listspin.add("Saturday");
        spinneradapter=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,listspin);
        spinneradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        days.setAdapter(spinneradapter);
        days.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               day=parent.getItemAtPosition(position).toString();
                daybtn.setText("Save"+" "+day+" "+"Timings");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    if (name.getText().toString() != null && name.getText().toString().length() > 0 && name.getText().toString() != "") {
                        dref.child("name").setValue(name.getText().toString().trim());
                    }

                    if (ownname.getText().toString() != null && ownname.getText().toString().length() > 0 && ownname.getText().toString() != "") {
                        dref.child("ownername").setValue(ownname.getText().toString().trim());
                    }

                    if (contacts.getText().toString() != null && contacts.getText().toString().length() > 0 && contacts.getText().toString() != "") {
                        dref.child("contactinfo").setValue(contacts.getText().toString().trim());
                    }

                    if (p4w.getText().toString() != null && p4w.getText().toString().length() > 0 && p4w.getText().toString() != "") {
                        dref.child("4wheeler capacity").setValue(p4w.getText().toString().trim());
                    }

                    if (p2w.getText().toString() != null && p2w.getText().toString().length() > 0 && p2w.getText().toString() != "") {
                        dref.child("2wheeler capacity").setValue(p2w.getText().toString().trim());
                    }
                } catch (Exception e) {
                    Log.d("PROFILE", e.getMessage());
                }
                startActivity(new Intent(EditProfile.this,ShowParking.class));
            }
        });
        daybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateaccdays();
            }
        });
        frtm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar=Calendar.getInstance();
                currentHour=calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute=calendar.get(Calendar.MINUTE);
                PickerDialog = new TimePickerDialog(EditProfile.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if(hourOfDay>=12){
                            ampm="PM";
                        }else {
                            ampm="AM";
                        }
                        starting=String.format("%02d:%02d",hourOfDay,minute)+ampm;
//                        start.setText(String.format("%02d:%02d",hourOfDay,minute)+ampm);
                    }
                },currentHour,currentMinute,false);
                PickerDialog.show();
            }
        });
        totm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar=Calendar.getInstance();
                currentHour1=calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute1=calendar.get(Calendar.MINUTE);
                PickerDialog = new TimePickerDialog(EditProfile.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if(hourOfDay>=12){
                            ampm1="PM";
                        }else {
                            ampm1="AM";
                        }
                        ending=String.format("%02d:%02d",hourOfDay,minute)+ampm1;
                        disptm.setText(starting+"-"+ending);
//                        start.setText(String.format("%02d:%02d",hourOfDay,minute)+ampm1);
                    }
                },currentHour1,currentMinute1,false);
                PickerDialog.show();
            }
        });
    }
    private void updateaccdays(){
        sRef=FirebaseDatabase.getInstance().getReference(day).child(auth.getCurrentUser().getUid());
        try {

            if (name.getText().toString() != null && name.getText().toString().length() > 0 && name.getText().toString() != "") {
                sRef.child("name").setValue(name.getText().toString().trim());
            }

            if (ownname.getText().toString() != null && ownname.getText().toString().length() > 0 && ownname.getText().toString() != "") {
                sRef.child("ownername").setValue(ownname.getText().toString().trim());
            }

            if (contacts.getText().toString() != null && contacts.getText().toString().length() > 0 && contacts.getText().toString() != "") {
                sRef.child("contactinfo").setValue(contacts.getText().toString().trim());
            }

            if (p4w.getText().toString() != null && p4w.getText().toString().length() > 0 && p4w.getText().toString() != "") {
                sRef.child("4wheeler capacity").setValue(p4w.getText().toString().trim());
            }
            if (p2w.getText().toString() != null && p2w.getText().toString().length() > 0 && p2w.getText().toString() != "") {
                sRef.child("2wheeler capacity").setValue(p2w.getText().toString().trim());
            }
            if (day != null && day.length() > 0 && day != "") {
                sRef.child("day").setValue(day);
            }
            if (starting != null && starting.length() > 0 && starting != "") {
                sRef.child("opentime").setValue(starting);
                dref.child("Timings").child(day).child("starting").setValue(starting);
            }
            if (ending != null && ending.length() > 0 && ending != "") {
                sRef.child("closingtime").setValue(ending);
                dref.child("Timings").child(day).child("ending").setValue(ending);
            }
            if (lat != null && lat.length() > 0 && lat != "") {
                sRef.child("latitude").setValue(lat);
            }
            if (longo != null && longo.length() > 0 && longo != "") {
                sRef.child("longitude").setValue(longo);
            }
            if (address != null && address.length() > 0 && address != "") {
                sRef.child("parkingaddress").setValue(address);
            }
        } catch (Exception e) {
            Log.d("PROFILE", e.getMessage());
        }
    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //startLocationUpdates();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }
        mlocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (mlocation == null) {
            startLocationUpdates();
        }
        if (mlocation != null) {
            lats = mlocation.getLatitude();
            longs = mlocation.getLongitude();
             lat = String.valueOf(lats);
             longo = String.valueOf(longs);
            getAddress(lats, longs);
            //Toast.makeText(getContext(), "Location:"+lat+longo, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Location not updated", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            lats = location.getLatitude();
            longs = location.getLongitude();
             lat = String.valueOf(lats);
             longo = String.valueOf(longs);
            getAddress(lats, longs);
            //Toast.makeText(getContext(), "Location:"+lat+longo, Toast.LENGTH_SHORT).show();
        }
        //Toast.makeText(this, "Your current location is:" + location, Toast.LENGTH_SHORT).show();
    }

    protected void startLocationUpdates() {
        locationRequest = LocationRequest.create().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY).setInterval(1000).setFastestInterval(1000);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    public Address getAddress(double latitude, double longitude) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
             address = addresses.get(0).getLocality()+"-"+addresses.get(0).getFeatureName()+"-"+addresses.get(0).getSubAdminArea() + "-" + addresses.get(0).getPostalCode();
            //locationtext.setText(address);
            dref.child("latitude").setValue(String.valueOf(latitude));
            dref.child("longitude").setValue(String.valueOf(longitude));
            dref.child("parkingaddress").setValue(address);
            // Toast.makeText(getContext(), ""+addresses, Toast.LENGTH_SHORT).show();
            return addresses.get(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }
}