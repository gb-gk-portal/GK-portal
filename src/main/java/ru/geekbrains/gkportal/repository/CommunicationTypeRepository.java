package ru.geekbrains.gkportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.geekbrains.gkportal.entity.CommunicationType;

import java.util.Optional;

@Repository
public interface CommunicationTypeRepository extends JpaRepository<CommunicationType, String> {
    Optional<CommunicationType> findByDescription(String description);
}
