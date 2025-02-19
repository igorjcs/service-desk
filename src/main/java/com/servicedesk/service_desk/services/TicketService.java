package com.servicedesk.service_desk.services;

import com.servicedesk.service_desk.dtos.TicketDTO;
import com.servicedesk.service_desk.models.TicketModel;
import com.servicedesk.service_desk.models.TicketStatus;
import com.servicedesk.service_desk.models.UserModel;
import com.servicedesk.service_desk.models.UserRole;
import com.servicedesk.service_desk.repositories.TicketRepository;
import com.servicedesk.service_desk.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TicketService {
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    public TicketService(TicketRepository ticketRepository, UserRepository userRepository) {
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
    }

    public TicketDTO createTicket (TicketDTO ticket){
        boolean ticketExist = ticketRepository.existsByDescriptionAndStatus(ticket.getDescription(), ticket.getStatus());
        if (ticketExist){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ja existe um chamado com essa descricao");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        UserModel user = userRepository.findByUsernameOpt(username)
                .orElseThrow( () ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario nao encontraod")
                );

        TicketModel ticketModel = new TicketModel(ticket.getDescription(), TicketStatus.OPEN, user);
        ticketModel.setCreatedAt(LocalDateTime.now());

        ticketRepository.save(ticketModel);

        return new TicketDTO(ticketModel);

        // Modelo antigo.
//        if (ticket.getUsers() == null || ticket.getUsers().getId() == null){
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuario nao pode ser nulo");
//        }
//
//        Optional<UserModel> userIsCreating = userRepository.findById(ticket.getUsers().getId());
//        if (userIsCreating.isEmpty()){
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario nao encontrado");
//        }
//
//        UserModel user = userIsCreating.get();
//
//        TicketModel ticketModel= new TicketModel(ticket.getDescription(), TicketStatus.OPEN, user);
//        ticketModel.setCreatedAt(LocalDateTime.now());
//
//        ticketRepository.save(ticketModel);
//
//        return new TicketDTO(ticketModel);

    }

    public List<TicketDTO> getAll(){
        List<TicketModel> tickets = ticketRepository.findAll();
        List<TicketDTO> ticketsDTOs = new ArrayList<>();

        if (tickets.isEmpty()){
            return new ArrayList<>();
        }

        for (TicketModel ticket : tickets){
            ticketsDTOs.add(new TicketDTO(ticket));
        }
        return ticketsDTOs;
    }

    public ResponseEntity<List<TicketDTO>> findByStatus(TicketStatus status){
        List<TicketModel> tickets = ticketRepository.findByStatus(status);

        if (tickets.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(new ArrayList<>());
        }

        List<TicketDTO> ticketDTOs = tickets.stream()
                .map(ticket -> new TicketDTO(ticket))
                .toList();

        return ResponseEntity.ok(ticketDTOs);
    }

    public ResponseEntity<String> deleteTicket (UUID ticketId){
        Optional<TicketModel> ticketOpt = ticketRepository.findById(ticketId);

        if (ticketOpt.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Chamado nao existe");
        }

        TicketModel ticket = ticketOpt.get();

        ticketRepository.delete(ticket);

        return ResponseEntity.ok("Chamado deletado");
    }


}
