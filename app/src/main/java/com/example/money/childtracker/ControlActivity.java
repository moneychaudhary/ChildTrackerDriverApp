package com.example.money.childtracker;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;

public class ControlActivity extends AppCompatActivity {
      private final static String EXTRA_NAME = "name";
      private final static String EXTRA_CONTACT = "contact";
      private final static String EXTRA_ID = "id";
      private final static String EXTRA_ROUTE_NO = "routeno";
      private final static String EXTRA_BUS_NO = "busno";
      private final static String DATE = "date";
      private final static String TIME = "time";
      private Button mStartButton;
      private Button mLogoutButton;
      private TextView mShowLocation;
      private BroadcastReceiver mBroadcastReceiver;
      private DatabaseReference mDatabase;
      private DatabaseReference mData;
      private StorageReference mStorage;
      private String mName;
      private String mContact;
      private String mId;
      private String mRoute;
      private String mBusNo;
      private ProgressDialog mProgressDialog;
      private boolean flag = true;
      private Intent mIntent;

      public static Intent getMyIntent(Context context, String name, String contact, String id, String routeNo, String busNo) {
            Intent intent = new Intent(context, ControlActivity.class);
            intent.putExtra(EXTRA_NAME, name);
            intent.putExtra(EXTRA_CONTACT, contact);
            intent.putExtra(EXTRA_ID, id);
            intent.putExtra(EXTRA_ROUTE_NO, routeNo);
            intent.putExtra(EXTRA_BUS_NO, busNo);
            return intent;
      }

      @Override
      protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_control);

            mProgressDialog = new ProgressDialog(this);

            mDatabase = FirebaseDatabase.getInstance().getReference().child("driver");
            mStorage = FirebaseStorage.getInstance().getReference();

            mShowLocation = (TextView) findViewById(R.id.show_location);
            mStartButton = (Button) findViewById(R.id.start_button);
            mLogoutButton = (Button) findViewById(R.id.logout_button);

//            mShowLocation.setTypeface(EasyFonts.caviarDreams(this));
//            mStartButton.setTypeface(EasyFonts.caviarDreams(this));
//            mLogoutButton.setTypeface(EasyFonts.caviarDreams(this));

            mProgressDialog.setCanceledOnTouchOutside(false);
            Intent intent = getIntent();

            Calendar cal = Calendar.getInstance();
            int dd = cal.get(Calendar.DATE);
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int minute = cal.get(Calendar.MINUTE);
            int hour = cal.get(Calendar.HOUR);
            String ampm = DateUtils.getAMPMString(cal.get(Calendar.AM_PM));
            final String time = hour + ":" + minute + "" + ampm;
            ;
            final String date = dd + "/" + month + "/" + year;

            mName = intent.getStringExtra(EXTRA_NAME);
            mContact = intent.getStringExtra(EXTRA_CONTACT);
            mId = intent.getStringExtra(EXTRA_ID);
            mRoute = intent.getStringExtra(EXTRA_ROUTE_NO);
            mBusNo = intent.getStringExtra(EXTRA_BUS_NO);

            mProgressDialog.setMessage("Loading.......");
            mProgressDialog.show();


            mStorage.child("conatus.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                  @Override
                  public void onSuccess(Uri uri) {
                        mData = mDatabase.push();
                        mData.child(EXTRA_NAME).setValue(mName);
                        mData.child(EXTRA_CONTACT).setValue(mContact);
                        mData.child(EXTRA_ROUTE_NO).setValue(mRoute);
                        mData.child(EXTRA_BUS_NO).setValue(mBusNo);
                        mData.child(EXTRA_ID).setValue(mId);
                        mData.child(TIME).setValue(time);
                        mData.child(DATE).setValue(date);
                        mData.child("Long").setValue("0");
                        mData.child("lat").setValue("0");
                        mData.child("status").setValue("0");
                        mProgressDialog.dismiss();
                        if (!checkPermissions())
                              enableButtons();

                  }
            });


            ;
            if (mBroadcastReceiver == null) {
                  mBroadcastReceiver = new BroadcastReceiver() {
                        @Override
                        public void onReceive(Context context, Intent intent) {
                              double longitude = intent.getDoubleExtra("long", 0);
                              double latitude = intent.getDoubleExtra("lat", 0);
                              mShowLocation.setText("Longitude : " + longitude + "\nLatitude : " + latitude);
                            mData.child("status").setValue("1");
                            mData.child("Long").setValue(String.valueOf(longitude));
                              mData.child("lat").setValue(String.valueOf(latitude));

                        }
                  };
            }
            registerReceiver(mBroadcastReceiver, new IntentFilter("locationChanged"));


      }

      private boolean checkPermissions() {
            if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                  requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION}, 500);
                  return true;
            }
            return false;
      }

      @Override
      public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            if (requestCode == 500) {
                  enableButtons();
            }
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);

      }

      private void enableButtons() {
            mStartButton.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                         mIntent = new Intent(getApplicationContext(), GpsService.class);
                        if (flag) {
                              mShowLocation.setText("Fetching...");
                              startService(mIntent);
                              mStartButton.setText("PAUSE");
                        } else {
                              mShowLocation.setText("Paused..!");
                              if(mIntent!=null){
                                    stopService(mIntent);
                                    mIntent=null;
                              }
                              mData.child("status").setValue("0");
                              mStartButton.setText("START");
                        }
                        flag = !flag;
                  }
            });

            mLogoutButton.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                        if(mIntent!=null)
                              stopService(mIntent);
                        mData.child("status").setValue("0");
                        ControlActivity.this.finish();
                  }
            });
      }

      @Override
      protected void onDestroy() {
            super.onDestroy();
            if(mIntent!=null)
            stopService(mIntent);
            mData.child("status").setValue("0");
            if (mBroadcastReceiver != null)
                  unregisterReceiver(mBroadcastReceiver);
      }
}
