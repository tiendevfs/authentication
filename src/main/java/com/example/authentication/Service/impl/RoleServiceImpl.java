package com.example.authentication.Service.impl;

import com.example.authentication.Model.Entity.Role;
import com.example.authentication.Repository.RoleRepository;
import com.example.authentication.Service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    @Override
    public List<Role> findAllByCodes(List<String> codes) {
        return roleRepository.findAllByCodeIn(codes);
    }
}
