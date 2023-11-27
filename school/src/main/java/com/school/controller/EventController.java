 package com.school.controller;

import java.util.Collections;


import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.school.entities.*;
import com.school.service.EventService;

import jakarta.persistence.EntityNotFoundException;



@RestController
@RequestMapping("/api")
public class EventController {
    @Autowired
    private EventService competitionService;
    @GetMapping("/events/")
    public List<Events> getAllCompetitions() {
        return competitionService.getAllCompetitions();
    }
    
    @GetMapping("/getbyid/{id}")
    public ResponseEntity<Events> getCompetitionById(@PathVariable Long id) {
        Events competition = competitionService.getCompetitionById(id);     
        return ResponseEntity.ok(competition);
    }
    
    @GetMapping("/keyvalue/")
    public Map<String, List<Events>> getSortedEventsByStandardAndStandardValue(
        @RequestParam(name = "standard", required = false) String standard,
        @RequestParam(name = "section", required = false) String section,
        @RequestParam(name = "usertype", required = false) String usertype) {
        List<Events> events = competitionService.getAllCompetitions();
        List<Events> filteredEvents = events.stream()
                .filter(event -> {
                    String eventStandard = event.getOptions();
                    boolean match = true;
                    if (standard != null) {
                        match = match && eventStandard != null && eventStandard.contains(standard);
                    }
                    if (section != null) {
                        match = match && eventStandard != null && eventStandard.contains(section);
                    }
                    if (usertype != null) {
                        match = match && eventStandard != null && eventStandard.contains(usertype);
                    }
                    return match;
                })
                .collect(Collectors.toList());
        Collections.sort(filteredEvents, Comparator.comparing(Events::getOptions));
        String str = "{\"standard\":\"" + standard + "\",\"section\":\"" + section + "\",\"usertype\":\"" + usertype + "\"}";
        Map<String, List<Events>> obj = new HashMap<>();
        obj.put(str, filteredEvents);
        return obj;
    }
    
    @PostMapping("/addevent")
    public ResponseEntity<?> addCompetition(@RequestBody Events events) {
        if ( events.getName() == null || events.getDescription() == null
                || events.getFee() == null || events.getOptions() == null
                || events.getEnd_date() == null)  {
            return ResponseEntity.badRequest().body("Invalid input parameters");
        }
        Events savedCompetition = competitionService.saveCompetition(events);
        if (savedCompetition == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save the competition");
        }
        return ResponseEntity.ok(savedCompetition);
    }
    
    @PutMapping("/updateevent/{id}")
    public ResponseEntity<Events> updateCompetition(@PathVariable Long id, @RequestBody Events events) {
        Events existingCompetition = competitionService.getCompetitionById(id);
        existingCompetition.setName(events.getName());
        existingCompetition.setDescription(events.getDescription());
        existingCompetition.setFee(events.getFee());
        existingCompetition.setOptions(events.getOptions());
        existingCompetition.setStart_date(events.getStart_date());
        existingCompetition.setEnd_date(events.getEnd_date());
        existingCompetition.setLocation(events.getLocation());
        existingCompetition.setCategory(events.getCategory());
        existingCompetition.setAttendee_limit(events.getAttendee_limit());
        existingCompetition.setRegistration_deadline(events.getRegistration_deadline());
        existingCompetition.setOrganizer(events.getOrganizer());
        existingCompetition.setContact_person(events.getContact_person() );
        existingCompetition.setContact_email(events.getContact_email());
        Events updatedCompetition = competitionService.saveCompetition(existingCompetition);
        return ResponseEntity.ok(updatedCompetition);
    }
    
    @DeleteMapping("/deleteevent/{id}")
    public ResponseEntity<?> deleteCompetition(@PathVariable Long id) {
        try {
            competitionService.deleteCompetition(id);
            return ResponseEntity.ok("Deleted Successfully");
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete the competition");
        }
    
    }
} 
