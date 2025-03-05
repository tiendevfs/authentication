package com.example.authentication.Repository;

import com.example.authentication.Model.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByCode(String code);

    List<Role> findAllByCodeIn(List<String> codes);
}
