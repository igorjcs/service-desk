package com.servicedesk.service_desk.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "TB_TICKET")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TicketModel implements Serializable {
    private static final long serialVerionUID = 1L;

    // Atributos

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false,unique = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column
    private TicketStatus status;

    @Column(nullable = false)
    private String username;

    private LocalDateTime createdAt;
    private LocalDateTime closedAt;

    public TicketModel(String description, TicketStatus status) {
        this.description = description;
        this.status = status;
        this.username = getUsername();
        this.createdAt = getCreatedAt();
        this.closedAt = getClosedAt();
    }
}
