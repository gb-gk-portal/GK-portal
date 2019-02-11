package ru.geekbrains.gkportal.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.geekbrains.gkportal.entity.Role;
import ru.geekbrains.gkportal.exception.RoleNotFoundException;
import ru.geekbrains.gkportal.repository.RoleRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Service
public class RoleService {

    private static final String DEFAULT_ROLE_DESCRIPTION = "user";

    private RoleRepository roleRepository;

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role save(Role role) {
        return roleRepository.save(role);
    }

    public Role findRoleByDescription(String description) throws Throwable {
        return roleRepository.findRoleByDescription(description).orElseThrow((Supplier<Throwable>) () ->
                new RoleNotFoundException(description));
    }

    public Role getDefaultRole() throws Throwable {
        return findRoleByDescription(DEFAULT_ROLE_DESCRIPTION);
    }

    public List<Role> getDefaultRoleList() throws Throwable {
        List<Role> roleList = new ArrayList<>();
        roleList.add(getDefaultRole());
        return roleList;
    }

}
