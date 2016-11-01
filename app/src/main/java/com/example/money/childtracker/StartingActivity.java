package com.example.money.childtracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class StartingActivity extends AppCompatActivity {
      private EditText mName;
      private EditText mContact;
      private EditText mRouteNo;
      private EditText mDriverId;
      private EditText mBusNo;
      private Button mSubmitButton;

      @Override
      protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_starting);
            mName = (EditText) findViewById(R.id.driver_name);
            mContact = (EditText) findViewById(R.id.contact_no);
            mRouteNo = (EditText) findViewById(R.id.route_no);
            mDriverId = (EditText) findViewById(R.id.driver_id);
            mBusNo = (EditText) findViewById(R.id.bus_no);
            mSubmitButton = (Button) findViewById(R.id.submit_button);

//            TextView driverOnDuty = (TextView) findViewById(R.id.driver_on_duty_textview);

//            driverOnDuty.setTypeface(EasyFonts.caviarDreams(this));
//            mSubmitButton.setTypeface(EasyFonts.caviarDreams(this));
//        mName.setTypeface(EasyFonts.caviarDreams(this));
//        mContact.setTypeface(EasyFonts.caviarDreams(this));
//        mRouteNo.setTypeface(EasyFonts.caviarDreams(this));
//        mDriverId.setTypeface(EasyFonts.caviarDreams(this));
//        mBusNo.setTypeface(EasyFonts.caviarDreams(this));

            mSubmitButton.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                        String name = mName.getText().toString().trim();
                        String contact = mContact.getText().toString().trim();
                        String routeNo = mRouteNo.getText().toString().trim();
                        String id = mDriverId.getText().toString().trim();
                        String busNo = mBusNo.getText().toString().trim();

                        if (name.isEmpty() || contact.isEmpty() || routeNo.isEmpty() || id.isEmpty() || busNo.isEmpty()) {
                              Toast.makeText(StartingActivity.this, "Please Fill all the details", Toast.LENGTH_SHORT).show();
                        } else if (name.length() >20) {
                              Toast.makeText(StartingActivity.this, "Name Must be less than 20 characters", Toast.LENGTH_SHORT).show();
                        } else if (contact.length() > 11) {
                              Toast.makeText(StartingActivity.this, "Please Enter correct Contact Number", Toast.LENGTH_SHORT).show();
                        } else {
                              Intent intent = ControlActivity.getMyIntent(StartingActivity.this, name, contact, id, routeNo, busNo);
                              StartingActivity.this.startActivity(intent);
                        }

                  }
            });
      }
}
