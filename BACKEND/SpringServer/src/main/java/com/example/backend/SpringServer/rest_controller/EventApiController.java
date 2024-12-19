package com.example.backend.SpringServer.rest_controller;

import com.example.backend.SpringServer.models.Event;
import com.example.backend.SpringServer.models.EventDTO;
import com.example.backend.SpringServer.repository.UserRepository;
import com.example.backend.SpringServer.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
@RestController
@RequestMapping("/api/v1")
public class EventApiController {


    @Autowired
    EventService eventService;

    @PostMapping("/event/register")
    public ResponseEntity<String> registerNewEvent(
            @RequestParam("user_id") int user_id,
            @RequestParam("title") String title,
            @RequestParam("location") String location,
            @RequestParam("date") String date,
            @RequestParam("description") String description,
            @RequestParam("image") MultipartFile image) {

        Event newEvent = eventService.registerNewEventServiceMethod(title, location, date, description, user_id, image);
        return new ResponseEntity<>("Event Registration Successful", HttpStatus.OK);
    }

    /*
    @GetMapping("/events")
    public ResponseEntity<List<Event>> getAllEvents() {
        List<Event> events = eventService.getAllEvents();
        return ResponseEntity.ok(events);
    }
     */

    @GetMapping("/events")
    public ResponseEntity<List<EventDTO>> getAllEvents() {
        List<EventDTO> events = eventService.getAllEvents();
        return ResponseEntity.ok(events);
    }
}


