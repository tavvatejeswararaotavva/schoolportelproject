package com.school.controller;
import java.util.ArrayList;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.school.entities.*;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/api/permissions")
public class PermissionController {
    
  
    
    @Autowired
    private EntityManager entityManager;

    @GetMapping("/GetPermissions")
    public List<Permission> getAllPermissions() {
        TypedQuery<Permission> query = entityManager.createQuery("SELECT p FROM Permission p", Permission.class);
        List<Permission> permissions = query.getResultList();
        return permissions;
    }
    
    @GetMapping("/GetScreens")
    public List<Screen> getAllScreens() {
        TypedQuery<Screen> query = entityManager.createQuery("SELECT s FROM Screen s", Screen.class);
        List<Screen> screens = query.getResultList();
        return screens;
    }
   
    @GetMapping("/GetScreens/{name}")
    public ResponseEntity<Permission> getPermissionByName(@PathVariable(value = "name") String permissionName) {
        Permission permission = entityManager.createQuery("SELECT p FROM Permission p WHERE p.name = :name", Permission.class)
                                           .setParameter("name", permissionName)
                                           .getSingleResult();
        if (permission == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(permission);
    }

    @PostMapping("/AddPermissions")
    @Transactional
    public Permission createPermission( @RequestBody Permission permission) {
        List<Screen> screens = new ArrayList<>();
        for (Screen screen : permission.getScreen()) {
            Screen managedScreen = entityManager.find(Screen.class, screen.getId());
            screens.add(managedScreen);
        }
        permission.setScreen(screens);
        entityManager.persist(permission);
        entityManager.flush();
        return permission;
    }

    @PutMapping("/UpdatePermissions/{id}")
    @Transactional
    public Permission updatePermission(@PathVariable(value = "id") int id,  @RequestBody Permission permissionDetails) {
        Permission permission = entityManager.find(Permission.class, id);
        if (permission == null) {
            throw new RuntimeException("Id is not valid");
        }
        List<Screen> screens = new ArrayList<>();
        for (Screen screen : permissionDetails.getScreen()) {
            Screen managedScreen = entityManager.find(Screen.class, screen.getId());
            screens.add(managedScreen);
        }
        permission.setName(permissionDetails.getName());
        permission.setScreen(screens);
        entityManager.merge(permission);
        entityManager.flush();
        return permission;
    }  
    @DeleteMapping("/RemovePermissions/{id}")
    @Transactional
    public ResponseEntity<Void> deletePermission(@PathVariable(value = "id") int permissionId) {
        Permission permission = entityManager.find(Permission.class, permissionId);
        if (permission == null) {
            return ResponseEntity.notFound().build();
        }
        entityManager.remove(permission);
        return ResponseEntity.ok().build();
    }
}
    
  