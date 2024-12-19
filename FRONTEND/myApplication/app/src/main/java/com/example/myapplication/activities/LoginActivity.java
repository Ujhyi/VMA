package com.example.myapplication.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class LoginActivity extends AppCompatActivity {

    EditText loginEmail, loginPassword;
    TextView moveToRegister;
    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        loginEmail = (EditText) findViewById(R.id.login_email);
        loginPassword = (EditText) findViewById(R.id.login_password);

        loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authentificateUser();
            }
        });

        moveToRegister = (TextView) findViewById(R.id.link_to_register);
        moveToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void authentificateUser() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://10.0.2.2:8080/api/v1/user/login";
        //String url = "http://localhost:9000/api/v1/user/login";

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("email",loginEmail.getText().toString());
        params.put("password", loginPassword.getText().toString());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int user_id = (int) response.get("user_id");
                    String username = (String) response.get("username");
                    String email = (String) response.get("email");
                    String school = (String) response.get("school");
                    String isic = (String) response.get("isic");

                    saveUserDataToPreferences(user_id,username, email, school, isic);

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("user_id", user_id);
                    intent.putExtra("username", username);
                    intent.putExtra("email", email);
                    intent.putExtra("school", school);
                    intent.putExtra("isic", isic);

                    startActivity(intent);
                    finish();

                } catch (JSONException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                String errorMessage = "Login Failed: " + error.getMessage();
                // If it's a network error, this can give you more details.
                if (error.networkResponse != null) {
                    errorMessage += ", Response Code: " + error.networkResponse.statusCode;
                }
                Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonObjectRequest);
    }

    private void saveUserDataToPreferences(int user_id, String username, String email, String school, String isic) {
        SharedPreferences sharedPreferences = getSharedPreferences("userDetails", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        System.out.println("username: " +  username + " email: " +  email + " school: " +  school + " isic: " +  isic);
        System.out.println(user_id);

        editor.putInt("user_id", user_id);
        editor.putString("username", username);
        editor.putString("email", email);
        editor.putString("school", school);
        editor.putString("isic", isic);
        editor.apply();
    }
}