package ru.pulsecore.user_service.service.role;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.pulsecore.user_service.domain.Role;
import ru.pulsecore.user_service.repository.RoleRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleService {

    private final RoleRepository roleRepository;

    private static  final String ROLE_USER="ROLE_USER";



    public Role findRoleUser(){
        return roleRepository.findByName(ROLE_USER);
    }



    public Role save (Role role){
        return roleRepository.save(role);
    }

    public Role findByName(String roleName){
        return roleRepository.findByName(roleName);
    }
}
