package com.example.myapplication.activities;
import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.Response;
import com.example.myapplication.EventAdapter;

import com.example.myapplication.models.EventDTO;
import com.example.myapplication.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_STORAGE = 100;
    RecyclerView recyclerView;
    BottomNavigationView bottomNav;
    private EventAdapter eventAdapter;
    private List<EventDTO> eventList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // BOTTOM NAVIGATION BAR;
        bottomNav = (BottomNavigationView) findViewById(R.id.bottom_navigation_bar);
        bottomNav.setSelectedItemId(R.id.nav_home_activity);
        bottomNav.setOnItemSelectedListener(item -> {

            switch (item.getItemId()){

                case R.id.nav_home_activity:
                    loadEvents();
                    return true;
                case R.id.nav_settings_activity:
                    startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                case R.id.nav_createEvent_activity:
                    startActivity(new Intent(getApplicationContext(), NewEventActivity.class));
                    overridePendingTransition(0,0);
                    return true;
            }
            return false;
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventList = new ArrayList<>();
        eventAdapter = new EventAdapter(eventList, this);
        recyclerView.setAdapter(eventAdapter);
        loadEvents();
    }
    private void loadEvents() {
        String url = "http://10.0.2.2:8080/api/v1/events";
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    Log.d(TAG, "Response: " + response.toString());
                    parseEvents(response);
                },
                error -> Log.e(TAG, "Volley Error: " + error.toString())
        );

        requestQueue.add(jsonArrayRequest);
    }

    private void parseEvents(JSONArray response) {
        for (int i = 0; i < response.length(); i++) {
            try {
                JSONObject eventJson = response.getJSONObject(i);
                String username = "Unknown User";

                if (eventJson.has("user") && !eventJson.isNull("user")) {
                    JSONObject userJson = eventJson.getJSONObject("user");
                    if (userJson.has("username")) {
                        username = userJson.getString("username");
                    }
                } else if (eventJson.has("username")) {
                    username = eventJson.getString("username");
                }

                EventDTO event = new EventDTO(
                        eventJson.getInt("event_id"),
                        eventJson.getString("title"),
                        eventJson.getString("location"),
                        eventJson.getString("date"),
                        eventJson.getString("description"),
                        eventJson.getString("image"),
                        username
                );
                eventList.add(event);

            } catch (JSONException e) {
                Log.e(TAG, "Error parsing event JSON: " + e.getMessage(), e);
            }
        }
        eventAdapter.notifyDataSetChanged();
    }
}