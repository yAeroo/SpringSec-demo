package com.nexus.demo.SpringSecDemo.Repository;

import com.nexus.demo.SpringSecDemo.Model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    public Role findByName(String name);

    public Role findById(int id);
}
