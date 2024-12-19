package com.example.myapplication.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SettingsActivity extends AppCompatActivity {

    TextView set_yourName, set_yourEmail, set_yourISIC, set_yourSchool;
    Button logout;
    BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);

        // BOTTOM NAVIGATION BAR;
        bottomNav = (BottomNavigationView) findViewById(R.id.bottom_navigation_bar);
        bottomNav.setSelectedItemId(R.id.nav_settings_activity);
        bottomNav.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {

                case R.id.nav_home_activity:
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                case R.id.nav_settings_activity:
                    return true;
                case R.id.nav_createEvent_activity:
                    startActivity(new Intent(getApplicationContext(), NewEventActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
            }
            return false;
        });


        set_yourName = (TextView) findViewById(R.id.your_name);
        set_yourEmail = (TextView) findViewById(R.id.your_email);
        set_yourISIC = (TextView) findViewById(R.id.your_isic);
        set_yourSchool = (TextView) findViewById(R.id.your_school);

        SharedPreferences sharedPreferences = getSharedPreferences("userDetails", MODE_PRIVATE);
        String first_name = sharedPreferences.getString("username", "N/A");
        String email = sharedPreferences.getString("email", "N/A");
        String school = sharedPreferences.getString("school", "N/A");
        String isic = sharedPreferences.getString("isic", "N/A");

        System.out.println("usernameS: " +  first_name + " emailS: " +  email + " schoolS: " +  school + " isicS: " +  isic);

        set_yourName.setText(first_name);
        set_yourEmail.setText(email);
        set_yourSchool.setText(school);
        set_yourISIC.setText(isic);

        // LOGOUT BUTTONS;
        logout = (Button) findViewById(R.id.log_out);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
            }
        });
    }

    private void logOut() {
        set_yourEmail.setText(null);
        set_yourName.setText(null);

        Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

    }
}