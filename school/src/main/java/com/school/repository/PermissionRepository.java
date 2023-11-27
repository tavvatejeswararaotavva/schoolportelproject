package com.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.school.entities.Permission;

public interface PermissionRepository extends JpaRepository<Permission, Integer> {

}
