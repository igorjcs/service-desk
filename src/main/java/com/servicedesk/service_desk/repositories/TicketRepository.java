package com.servicedesk.service_desk.repositories;

import com.servicedesk.service_desk.dtos.TicketDTO;
import com.servicedesk.service_desk.models.TicketModel;
import com.servicedesk.service_desk.models.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TicketRepository extends JpaRepository<TicketModel, UUID> {
    List<TicketDTO> findByStatus(TicketStatus status);

    TicketDTO findByDescription(String description);

    boolean existsByDescriptionAndStatus (String description, TicketStatus status);

}
