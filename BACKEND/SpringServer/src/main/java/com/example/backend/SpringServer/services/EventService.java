package com.example.backend.SpringServer.services;

import com.example.backend.SpringServer.models.Event;
import com.example.backend.SpringServer.models.EventDTO;
import com.example.backend.SpringServer.models.User;
import com.example.backend.SpringServer.repository.EventRepository;
import com.example.backend.SpringServer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventService {

    @Autowired
    EventRepository eventRepository;

    @Autowired
    UserRepository userRepository;

    public Event registerNewEventServiceMethod(String title, String location, String date,
                                               String description, int user_id, MultipartFile image) {
        Event newEvent = new Event();
        newEvent.setTitle(title);
        newEvent.setLocation(location);
        newEvent.setDate(date);
        newEvent.setDescription(description);

        User user = userRepository.findById(user_id).orElseThrow(() -> new RuntimeException("User not found"));
        newEvent.setUser(user);

        // Handle image upload
        if (image != null && !image.isEmpty()) {
            try {
                newEvent.setImage(image.getBytes()); // This can throw IOException
            } catch (IOException e) {
                throw new RuntimeException("Failed to process image", e); // Wrap the IOException in a RuntimeException
            }
        }

        // Save the event
        return eventRepository.save(newEvent);
    }

    /*
    public List<Event> getAllEvents() {
        return (List<Event>) eventRepository.findAll();
    }
     */
    /*
    public List<EventDTO> getAllEvents() {
        List<Event> events = (List<Event>) eventRepository.findAll();
        return events.stream()
                .map(event -> new EventDTO(
                        event.getEvent_id(),
                        event.getTitle(),
                        event.getLocation(),
                        event.getDate(),
                        event.getDescription(),
                        event.getLocation(),
                        event.getUser().getUsername().getBytes() // Get username from User object
                ))
                .collect(Collectors.toList());
    }
     */

    public List<EventDTO> getAllEvents() {
        List<Event> events = (List<Event>) eventRepository.findAll();
        return events.stream()
                .map(event -> {
                    String base64Image = event.getImage() != null ?
                            Base64.getEncoder().encodeToString(event.getImage()) : null;
                    return new EventDTO(
                            event.getEvent_id(),
                            event.getTitle(),
                            event.getLocation(),
                            event.getDate(),
                            event.getDescription(),
                            event.getUser().getUsername(),
                            base64Image // Pass the Base64 encoded string
                    );
                })
                .collect(Collectors.toList());
    }
}

