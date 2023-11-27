package com.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.school.entities.Announcement;

public interface AnnouncementRepository extends JpaRepository<Announcement, Integer>{

}
