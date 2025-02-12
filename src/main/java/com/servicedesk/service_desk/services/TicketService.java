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
import org.springframework.stereotype.Service;

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

    public ResponseEntity<String> createTicket (TicketDTO ticket){
        boolean ticketExist = ticketRepository.existsByDescriptionAndStatus(ticket.getDescription(), ticket.getStatus());
        if (ticketExist){
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Ja existe um chamado aberto com essa mesma descricao");
        }

        Optional<UserModel> userIsCreating = userRepository.findById(ticket.getUserId());
        if (userIsCreating.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Usuario nao logado");
        }

        UserModel user = userIsCreating.get();

        TicketModel newTicket = new TicketModel();
        newTicket.setDescription(ticket.getDescription());
        newTicket.setStatus(ticket.getStatus());
        newTicket.setCreatedAt(ticket.getCreatedAt());
        newTicket.setUser(user);

        ticketRepository.save(newTicket);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Chamado aberto com sucesso");

    }

    public ResponseEntity<List<TicketDTO>> findByStatus(TicketStatus status){
        List<TicketDTO> tickets = ticketRepository.findByStatus(status);

        if (tickets.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(tickets);
        }

        return ResponseEntity.ok(tickets);
    }

    public ResponseEntity<String> deleteTicket (UUID ticketId, UUID userId){
        Optional<TicketModel> ticketOpt = ticketRepository.findById(ticketId);

        // Verifica se chamado existe pelo ID passado
        if (ticketOpt.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Chamado nao existe");
        }

        // Guarda o chamado e o usuário criador do chamado
        TicketModel ticket = ticketOpt.get();
        UserModel ticketUser = ticket.getUser();

        // Verifica se o usuário passado por ID existe
        Optional<UserModel> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Usuario nao encontrado");
        }

        // Guarda usuário passado por ID (caso exista)
        UserModel user = userOpt.get();

        // Verifica se usuário é criador do chamado ou um ADMIN
        if (user.getRole() == UserRole.ADMIN || user.equals(ticketUser)){
            ticketRepository.delete(ticket);
            return ResponseEntity.ok("Chamado deletado");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Voce nao tem permissao para apagar esse chamado");
        }


    }


}
