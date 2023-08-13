package ru.netology.cloudserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import ru.netology.cloudserver.entity.Role;

@Service
public interface RoleRepository extends JpaRepository<Role, Long> {
}
