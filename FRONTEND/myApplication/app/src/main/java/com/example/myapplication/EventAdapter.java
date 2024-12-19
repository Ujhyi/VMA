package com.example.myapplication;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.models.EventDTO;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    //private List<Event> eventList;
    private List<EventDTO> eventList;
    private Context context;

    public EventAdapter(List<EventDTO> eventList, Context context) {
        //this.eventList = eventList;
        this.eventList = eventList;
        this.context = context;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_item, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        EventDTO event = eventList.get(position);

        holder.title.setText(event.getTitle());
        holder.location.setText(event.getLocation());
        holder.date.setText(event.getDate());


        String base64ImageData = event.getImage();
        System.out.println("URLLL: " + base64ImageData);

        byte[] decodedString = Base64.decode(base64ImageData, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        holder.imageBanner.setImageBitmap(decodedByte);

        holder.username.setText(String.valueOf(event.getUsername()));



    }

    private Bitmap decodeBase64ToBitmap(String base64Str) {
        try {
            // Clean the input string (remove any backslashes)
            base64Str = base64Str.replace("\\", "");

            // Check if the Base64 string is empty or malformed
            if (base64Str.isEmpty()) {
                Log.e(TAG, "Base64 string is empty");
                return null;
            }

            // Decode the Base64 string
            byte[] decodedBytes = Base64.decode(base64Str, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        } catch (Exception e) {
            Log.e(TAG, "Failed to decode Base64 image: " + e.getMessage());
            return null; // Return null if decoding fails
        }
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView title, location, date, description, username;
        ImageView imageBanner, descButton;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.event_item_title);
            location = itemView.findViewById(R.id.event_item_location);
            date = itemView.findViewById(R.id.event_item_date);
            username = itemView.findViewById(R.id.event_created_by_username);
            imageBanner = itemView.findViewById(R.id.event_banner);
            //descButton = itemView.findViewById(R.id.move_to_details);


           // description = itemView.findViewById(R.id.description);
        }
    }
}
