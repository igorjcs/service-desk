package com.servicedesk.service_desk.services;

import com.servicedesk.service_desk.dtos.TicketDTO;
import com.servicedesk.service_desk.models.TicketModel;
import com.servicedesk.service_desk.models.TicketStatus;
import com.servicedesk.service_desk.models.UserModel;
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

        TicketModel ticketModel = new TicketModel(ticket.getDescription(), TicketStatus.OPEN);
        ticketModel.setCreatedAt(LocalDateTime.now());
        ticketModel.setClosedAt(null);
        ticketModel.setUsername(username);

        ticketRepository.save(ticketModel);

        return new TicketDTO(ticketModel);
    }

    public boolean closeTicket(UUID id){
        Optional<TicketModel> optTicket = ticketRepository.findById(id);

        if (optTicket.isPresent()){
            TicketModel ticket = optTicket.get();
            if (!ticket.getStatus().equals(TicketStatus.CLOSED)){
                ticket.setStatus(TicketStatus.CLOSED);
                ticketRepository.save(ticket);
                return true;
            }
        }
        return false;
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

    public List<TicketDTO> findByStatus(TicketStatus status){
        List<TicketModel> tickets = ticketRepository.findByStatus(status);

        if (tickets.isEmpty()){
            return new ArrayList<>();
        }

        return tickets.stream()
                .map(TicketDTO::new)
                .toList();
    }

    public ResponseEntity<String> deleteTicket (UUID id){
        Optional<TicketModel> ticketOpt = ticketRepository.findById(id);

        if (ticketOpt.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Chamado nao existe");
        }

        TicketModel ticket = ticketOpt.get();

        ticketRepository.delete(ticket);

        return ResponseEntity.ok("Chamado deletado");
    }


}
