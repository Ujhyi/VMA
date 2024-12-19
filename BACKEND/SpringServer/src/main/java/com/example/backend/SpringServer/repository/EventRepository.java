package com.example.backend.SpringServer.repository;

import com.example.backend.SpringServer.models.Event;
import com.example.backend.SpringServer.models.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EventRepository extends CrudRepository<Event, Integer> {

    List<Event> findByUser(User user);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO EVENTS(title, location, date, description, user_id, image) VALUES (:title, :location, :date, :description, :user_id, :image)", nativeQuery = true)
    int registerNewEvent(@Param("title") String title,
                         @Param("location") String location,
                         @Param("date") String date,
                         @Param("description") String description,
                         @Param("user_id")  String user_id,
                         @Param("image") byte[] image);

}

