package com.example.myapplication.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.example.myapplication.EventApiService;
import com.example.myapplication.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CreateEventActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_READ_EXTERNAL_STORAGE = 101;

    private ImageView pickedImage;
    private EditText location, title, date, description;
    private Button addEvent;
    private Uri selectedImageUri;
    private File imageFile;
    BottomNavigationView bottomNav;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        // Check for read storage permission
        checkPermissions();

        // Initialize UI components
        initializeUIComponents();

        // Image Picker Click
        pickedImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*"); // Set the type to images only
            pickImages.launch(intent); // Launch the image picker
        });

        /*
        // BOTTOM NAVIGATION BAR;
        bottomNav = (BottomNavigationView) findViewById(R.id.bottom_navigation_bar);
        bottomNav.setSelectedItemId(R.id.nav_home_activity);
        bottomNav.setOnItemSelectedListener(item -> {

            switch (item.getItemId()){

                case R.id.nav_home_activity:
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                case R.id.nav_settings_activity:
                    return true;
                case R.id.nav_createEvent_activity:
                    startActivity(new Intent(getApplicationContext(), CreateEventActivity.class));
                    overridePendingTransition(0,0);
                    return true;
            }
            return false;
        });

         */

        // Creating Event
        addEvent.setOnClickListener(v -> {
            // Get user_id from SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences("userDetails", MODE_PRIVATE);
            int userId = sharedPreferences.getInt("user_id", -1); // Default to -1 if not found

            if (userId == -1) {
                Toast.makeText(this, "User ID not found. Please log in again.", Toast.LENGTH_SHORT).show();
                return; // Early exit if user ID is invalid
            }

            createNewEvent(String.valueOf(userId));
        });
    }

    // Image picker launcher
    private final ActivityResultLauncher<Intent> pickImages = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    selectedImageUri = result.getData().getData();
                    pickedImage.setImageURI(selectedImageUri);
                    imageFile = new File(getRealPathFromURI(selectedImageUri));
                    Log.d("PickedImage", "Selected URI: " + selectedImageUri);
                    Log.d("PickedImage", "Image file path: " + (imageFile != null ? imageFile.getAbsolutePath() : "null"));
                }
            });

    // Check for permissions
    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_READ_EXTERNAL_STORAGE);
        }
    }

    // Handle permission request results
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Get real path from URI
    private String getRealPathFromURI(Uri uri) {
        String filePath = null;

        if (DocumentsContract.isDocumentUri(this, uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
            String[] split = docId.split(":");
            String type = split[0];

            Uri contentUri = null;
            if ("image".equals(type)) {
                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            }

            String selection = MediaStore.Images.Media._ID + "=?";
            String[] selectionArgs = new String[]{split[1]};

            Cursor cursor = getContentResolver().query(contentUri, null, selection, selectionArgs, null);
            if (cursor != null) {
                int columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    filePath = cursor.getString(columnIndex);
                }
                cursor.close();
            }
        } else {
            String[] projection = {MediaStore.Images.Media.DATA};
            try (Cursor cursor = getContentResolver().query(uri, projection, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    filePath = cursor.getString(columnIndex);
                }
            } catch (Exception e) {
                e.printStackTrace(); // Log error if any
            }
        }

        if (filePath == null) {
            filePath = uri.getPath(); // Fallback for file URIs
        }

        return filePath;
    }

    // Create new event
    private void createNewEvent(String user_id) {
        String eventTitle = title.getText().toString();
        String eventLocation = location.getText().toString();
        String eventDate = date.getText().toString();
        String eventDescription = description.getText().toString();

        if (eventTitle.isEmpty() || eventLocation.isEmpty() || eventDate.isEmpty() || eventDescription.isEmpty() || imageFile == null) {
            Toast.makeText(this, "Please fill all fields and select an image.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Make API call to create event
        uploadEventToServer(Integer.valueOf(user_id), eventTitle, eventLocation, eventDate, eventDescription, imageFile);
    }

    private void initializeUIComponents() {
        pickedImage = findViewById(R.id.pick_image);
        title = findViewById(R.id.event_title);
        location = findViewById(R.id.event_location);
        date = findViewById(R.id.event_date);
        description = findViewById(R.id.event_description);
        addEvent = findViewById(R.id.create_button);
    }

    private void uploadEventToServer(Integer user_id, String title, String location, String date, String description, File imageFile) {
        if (!imageFile.exists()) {
            Toast.makeText(this, "Image file does not exist!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create Retrofit instance
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/api/v1/")
                //.baseUrl("http://localhost:8080/api/v1/")// Update as necessary
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EventApiService apiService = retrofit.create(EventApiService.class);

        // Prepare request body
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imageFile);
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", imageFile.getName(), requestFile);

        RequestBody userIdPart = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(user_id));
        RequestBody titlePart = RequestBody.create(MediaType.parse("text/plain"), title);
        RequestBody locationPart = RequestBody.create(MediaType.parse("text/plain"), location);
        RequestBody datePart = RequestBody.create(MediaType.parse("text/plain"), date);
        RequestBody descriptionPart = RequestBody.create(MediaType.parse("text/plain"), description);

        // Call API
        Call<ResponseBody> call = apiService.createEvent(userIdPart, titlePart, locationPart, datePart, descriptionPart, imagePart);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CreateEventActivity.this, "Event created successfully!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    //Log.e("CreateEventActivity", "Error: " + response.errorBody().string());
                    Toast.makeText(CreateEventActivity.this, "Failed to create event!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(CreateEventActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("CreateEventActivity", "Network error: " + t.getMessage());
            }
        });
    }
}