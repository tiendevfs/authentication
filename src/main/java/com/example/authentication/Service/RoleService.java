package com.example.authentication.Service;

import com.example.authentication.Model.Entity.Role;

import java.util.List;

public interface RoleService {
    List<Role> findAllByCodes(List<String> codes);
}
