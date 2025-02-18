package com.servicedesk.service_desk.dtos;

import com.servicedesk.service_desk.models.TicketModel;
import com.servicedesk.service_desk.models.TicketStatus;
import com.servicedesk.service_desk.models.UserModel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@Getter
public class TicketDTO {
    private UUID id;
    private String description;
    private TicketStatus status = TicketStatus.OPEN; // padrao aberto
    private UserModel users;
    private LocalDateTime createdAt;
    private LocalDateTime closedAt;

    public TicketDTO(TicketModel newTicket) {
        this.id = newTicket.getId();
        this.description = newTicket.getDescription();
        this.status = newTicket.getStatus();
        this.users = newTicket.getUsers();
        this.createdAt = newTicket.getCreatedAt();
        this.closedAt = newTicket.getClosedAt();
    }

    public void setUserId(UUID userId){
        this.users = new UserModel();
        this.users.setId(userId);
    }

}
