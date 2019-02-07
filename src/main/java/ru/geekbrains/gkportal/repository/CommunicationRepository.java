package ru.geekbrains.gkportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.geekbrains.gkportal.entity.Communication;
import ru.geekbrains.gkportal.entity.CommunicationType;

@Repository
public interface CommunicationRepository extends JpaRepository<Communication, String> {
    Communication findCommunicationByCommunicationTypeAndIdentify(CommunicationType communicationType, String identify);

}
