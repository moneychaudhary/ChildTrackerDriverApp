package com.example.money.childtracker;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;


public class Splashh extends AppCompatActivity {

      @Override
      protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_splash);

            TextView textView = (TextView) findViewById(R.id.splash_textView);
//            textView.setTypeface(EasyFonts.caviarDreams(this));

            TextView textView1 = (TextView) findViewById(R.id.splash_textView1);
//            textView1.setTypeface(EasyFonts.caviarDreams(this));


            new Handler().postDelayed(new Runnable() {
                  @Override
                  public void run() {
                        Intent intent = new Intent(Splashh.this, StartingActivity.class);
                        Splashh.this.startActivity(intent);
                        Splashh.this.finish();
                  }
            }, 2000);
      }
}
