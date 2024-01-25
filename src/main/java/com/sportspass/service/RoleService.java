package com.sportspass.service;

import com.sportspass.model.Role;

public interface RoleService {

    Role findByName(String name);

    void save(Role role);
}