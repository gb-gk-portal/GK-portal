package ru.geekbrains.gkportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.geekbrains.gkportal.entities.Contact;

import java.util.UUID;

@Repository
public interface ContactRepository extends JpaRepository<Contact, String> {

}
