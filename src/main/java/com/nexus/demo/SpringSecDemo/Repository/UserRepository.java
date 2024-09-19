package com.nexus.demo.SpringSecDemo.Repository;

import com.nexus.demo.SpringSecDemo.Model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<AppUser, Integer> {
    public AppUser findByUsername(String username);
}
