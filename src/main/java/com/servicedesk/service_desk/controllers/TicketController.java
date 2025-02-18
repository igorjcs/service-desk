package com.servicedesk.service_desk.controllers;

import com.servicedesk.service_desk.dtos.TicketDTO;
import com.servicedesk.service_desk.models.TicketModel;
import com.servicedesk.service_desk.models.TicketStatus;
import com.servicedesk.service_desk.services.TicketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController (TicketService ticketService){
        this.ticketService = ticketService;
    }

    @GetMapping("/list")
    public HttpStatus getAll (){
        ticketService.getAll();

        return ResponseEntity.ok(HttpStatus.OK)
                .getBody();
    }

    // Endpoint para listar chamados por status
    @GetMapping("/list-status")
    public ResponseEntity<List<TicketDTO>> findByStatus (@RequestParam TicketStatus status){
        ticketService.findByStatus(status);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ticketService.findByStatus(status).getBody());
    }

    @PostMapping("/create")
    public ResponseEntity<TicketDTO> createTicket(@RequestBody TicketDTO ticket){
        TicketDTO createdTicket = ticketService.createTicket(ticket);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTicket);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTicket(@PathVariable UUID ticketId,
                                               @PathVariable UUID userId){
        return ticketService.deleteTicket(ticketId, userId);
    }

}
