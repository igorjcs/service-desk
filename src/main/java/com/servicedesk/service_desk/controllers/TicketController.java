package com.servicedesk.service_desk.controllers;

import com.servicedesk.service_desk.config.SecurityConfig;
import com.servicedesk.service_desk.dtos.TicketDTO;
import com.servicedesk.service_desk.models.TicketStatus;
import com.servicedesk.service_desk.services.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tickets")
@Tag(name = "Service-desk", description = "Programa de criação de chamados")
@SecurityRequirement(name = SecurityConfig.SECURITY)
public class TicketController {

    private final TicketService ticketService;

    public TicketController (TicketService ticketService){
        this.ticketService = ticketService;
    }

    @GetMapping("/list")
    @Operation(summary = "Listar os chamados", description = "Método para listar chamados")
    @ApiResponse(responseCode = "201", description = "Listados com sucesso")
    @ApiResponse(responseCode = "400", description = "Lista de chamados vazia")
    @ApiResponse(responseCode = "500", description = "Erro no servidor")
    public ResponseEntity<List<TicketDTO>> getAll (){
        List<TicketDTO> tickets = ticketService.getAll();

        return ResponseEntity.ok(tickets);
    }

    // Endpoint para listar chamados por status
    @GetMapping("/list-status")
    @Operation(summary = "Listar os chamados por status", description = "Método para listar chamados por status")
    @ApiResponse(responseCode = "201", description = "Listados com sucesso")
    @ApiResponse(responseCode = "204", description = "Lista de chamados com esse status vazia")
    @ApiResponse(responseCode = "500", description = "Erro no servidor")
    public ResponseEntity<List<TicketDTO>> findByStatus (@RequestParam TicketStatus status){
        List<TicketDTO> tickets = ticketService.findByStatus(status);

        if (tickets.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(tickets);
    }

    @PostMapping("/create")
    @Operation(summary = "Criar um chamado", description = "Método para criar chamados")
    @ApiResponse(responseCode = "201", description = "Chamado criado com sucesso")
    @ApiResponse(responseCode = "400", description = "Chamado ja existente")
    @ApiResponse(responseCode = "500", description = "Erro no servidor")
    public ResponseEntity<TicketDTO> createTicket(@RequestBody TicketDTO ticket){
        TicketDTO createdTicket = ticketService.createTicket(ticket);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTicket);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar chamados passando ID", description = "Método para deletar chamados")
    public ResponseEntity<String> deleteTicket(@PathVariable("id") UUID id){
        return ticketService.deleteTicket(id);
    }

}
