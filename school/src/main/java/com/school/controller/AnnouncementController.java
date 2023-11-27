package com.school.controller;

import com.school.entities.Announcement;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/announcement")
public class AnnouncementController {
	@Autowired
    private EntityManager entityManager;
	
    @GetMapping("/getAnnouncement")
    public List<Announcement> getAnnouncements() {
        LocalDate today = LocalDate.now();
        return entityManager.createQuery(
                "SELECT a FROM Announcement a WHERE a.duedate >= :today",
                Announcement.class)
                .setParameter("today", today)
                .getResultList();
    }

    @GetMapping("/getAnnouncementById/{id}")
    public Announcement getAnnouncementById(@PathVariable Integer id) {
        return entityManager.find(Announcement.class, id);
    }

    @PostMapping("/createAnnouncement")
    @Transactional
    public ResponseEntity<Announcement> createAnnouncement(@RequestBody Announcement announcement) {
        entityManager.persist(announcement);
        return ResponseEntity.status(HttpStatus.CREATED).body(announcement);
    }

    @PutMapping("/updateAnnouncement/{id}")
    @Transactional
    public Announcement updateAnnouncement(@PathVariable Integer id, @RequestBody Announcement announcement) {
        Announcement existingAnnouncement = entityManager.find(Announcement.class, id);
        if (existingAnnouncement != null) {
            existingAnnouncement.setTitle(announcement.getTitle());
            existingAnnouncement.setMessage(announcement.getMessage());
            existingAnnouncement.setCategories(announcement.getCategories());
            existingAnnouncement.setAnnouncementdate(announcement.getAnnouncementdate());
            existingAnnouncement.setDuedate(announcement.getDuedate());
            existingAnnouncement.setTarget(announcement.getTarget());
            entityManager.merge(existingAnnouncement);
        }
        return existingAnnouncement;
    }

    @DeleteMapping("/deleteAnnouncement/{id}")
    @Transactional
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAnnouncement(@PathVariable Integer id) {
        Announcement existingAnnouncement = entityManager.find(Announcement.class, id);
        if (existingAnnouncement != null) {
            entityManager.remove(existingAnnouncement);
        }
    }
    
   
    @GetMapping("/target")
    public List<Announcement> getAnnouncementsByTarget(
            @RequestParam(name = "standard", required = false) String standard,
            @RequestParam(name = "section", required = false) String section,
            @RequestParam(name = "usertype", required = false) String usertype) {
        String query = "SELECT a FROM Announcement a WHERE 1=1";
        if (standard != null) {
            query += " AND a.target LIKE '%" + standard + "%'";
        }
        if (section != null) {
            query += " AND a.target LIKE '%" + section + "%'";
        }
        if (usertype != null) {
            query += " AND a.target LIKE '%" + usertype + "%'";
        }
        TypedQuery<Announcement> typedQuery = entityManager.createQuery(query, Announcement.class);
        return typedQuery.getResultList();
    }


}