package com.servicedesk.service_desk.dtos;

import com.servicedesk.service_desk.models.TicketModel;
import com.servicedesk.service_desk.models.TicketStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class TicketDTO {
    private String description;
    private TicketStatus status;
    private String username;
    private LocalDateTime createdAt;
    private LocalDateTime closedAt;

    public TicketDTO(TicketModel newTicket) {
        this.description = newTicket.getDescription();
        this.status = newTicket.getStatus();
        this.username = newTicket.getUsername();
        this.createdAt = newTicket.getCreatedAt();
        this.closedAt = newTicket.getClosedAt();
    }



}
