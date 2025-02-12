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
    private TicketStatus status = TicketStatus.OPEN; // Padrao "aberto".

    // OneToMany
    @Column(nullable = false)
    private UserModel user;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime closedAt;
}
