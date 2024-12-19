package com.example.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {


    Spinner spinner;
    String[] school = {"", "EUBA", "UK", "Pan-European University"};
    String selectedSchool;
    TextView moveToLogin;
    EditText username, email, password, isic;
    Button registrationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        username = (EditText) findViewById(R.id.username);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        isic = (EditText) findViewById(R.id.isic_number);


        registrationButton = (Button) findViewById(R.id.registration_button);
        // REGISTRATION BUTTON
        registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processFormFields();
            }
        });


        // MOVE TO LOGIN
        moveToLogin = (TextView) findViewById(R.id.link_to_login);
        moveToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toLogin();
            }
        });

        // SCHOOL SELECT OPTION
        spinner = findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(RegisterActivity.this, R.layout.style_spineer1, school);
        adapter.setDropDownViewResource(R.layout.style_spineer1);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSchool = parent.getItemAtPosition(position).toString();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void processFormFields()
    {
        RequestQueue queue = Volley.newRequestQueue(this);
        //String url = "http://10.0.2.2:9000/api/v1/user/register";
        String url = "http://10.0.2.2:8080/api/v1/user/register";
        //String url = "http://localhost:8080/api/v1/user/register";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equalsIgnoreCase("success"))
                {
                    username.setText(null);
                    email.setText(null);
                    password.setText(null);
                    isic.setText(null);

                }
                toLogin();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError", error.toString());
                Toast.makeText(RegisterActivity.this, "Register Un-Successfull", Toast.LENGTH_SHORT).show();
            }
        }){
          @Nullable
          @Override
          protected Map<String, String> getParams() throws AuthFailureError {
              Map<String, String> params = new HashMap<>();
              params.put("username", username.getText().toString());
              params.put("email", email.getText().toString());
              params.put("password", password.getText().toString());
              params.put("isic", isic.getText().toString());
              params.put("school", selectedSchool);
              return params;
          }
        };
        queue.add(stringRequest);
    }

    public void toLogin()
    {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}