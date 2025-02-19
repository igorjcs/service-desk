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
    public HttpStatus getAll (){
        ticketService.getAll();

        return ResponseEntity.ok(HttpStatus.OK)
                .getBody();
    }

    // Endpoint para listar chamados por status
    @GetMapping("/list-status")
    @Operation(summary = "Listar os chamados", description = "Método para listar chamados por status")
    @ApiResponse(responseCode = "201", description = "Listados com sucesso")
    @ApiResponse(responseCode = "400", description = "Lista de chamados vazia")
    @ApiResponse(responseCode = "500", description = "Erro no servidor")
    public HttpStatus findByStatus (@RequestParam TicketStatus status){
        ticketService.findByStatus(status);
        return ResponseEntity.ok(HttpStatus.OK)
                .getBody();
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
    public ResponseEntity<String> deleteTicket(@PathVariable UUID ticketId){
        return ticketService.deleteTicket(ticketId);
    }

}
