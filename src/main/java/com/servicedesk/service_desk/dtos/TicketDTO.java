package com.servicedesk.service_desk.dtos;

import com.servicedesk.service_desk.models.TicketStatus;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class TicketDTO {
    private UUID id;
    private String description;
    private TicketStatus status;
    private UUID userId;
    private LocalDateTime createdAt;
    private LocalDateTime closedAt;

}
