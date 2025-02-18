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

        if (ticket.getUsers() == null || ticket.getUsers().getId() == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuario nao pode ser nulo");
        }

        Optional<UserModel> userIsCreating = userRepository.findById(ticket.getUsers().getId());
        if (userIsCreating.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario nao encontrado");
        }

        UserModel user = userIsCreating.get();

        TicketModel ticketModel= new TicketModel();
        ticketModel.setDescription(ticket.getDescription());
        ticketModel.setStatus(ticket.getStatus());
        ticketModel.setUsers(user);
        ticketModel.setCreatedAt(LocalDateTime.now());

        ticketRepository.save(ticketModel);

        return new TicketDTO(ticketModel);

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

    public ResponseEntity<String> deleteTicket (UUID ticketId, UUID userId){
        Optional<TicketModel> ticketOpt = ticketRepository.findById(ticketId);

        // Verifica se chamado existe pelo ID passado
        if (ticketOpt.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Chamado nao existe");
        }

        // Guarda o chamado e o usuário criador do chamado
        TicketModel ticket = ticketOpt.get();
        UserModel ticketUser = ticket.getUsers();

        // Verifica se o usuário passado por ID existe
        Optional<UserModel> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Usuario nao encontrado");
        }

        // Guarda usuário passado por ID (caso exista)
        UserModel user = userOpt.get();

        // Verifica se usuário é criador do chamado ou um ADMIN
        if (user.getRole() == UserRole.ADMIN || user.equals(ticketUser.getId())){
            ticketRepository.delete(ticket);
            return ResponseEntity.ok("Chamado deletado");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Voce nao tem permissao para apagar esse chamado");
        }


    }


}
