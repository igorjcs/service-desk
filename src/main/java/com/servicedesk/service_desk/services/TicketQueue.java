package com.servicedesk.service_desk.services;

import com.servicedesk.service_desk.dtos.TicketDTO;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class TicketQueue {
    private Queue<TicketDTO> queue = new LinkedList<>();

    // Adiciona novo ticket ao final da fila
    public void addTicket(TicketDTO ticket){
        queue.offer(ticket);
    }

    // Remove primeiro ticket da fila e retorna o proximo
    public TicketDTO getNextTicket(){
        return queue.poll();
    }

    // Retorna o primeiro ticket sem remover nada
    public TicketDTO peekNextTicket(){
        return queue.peek();
    }

    // Retorna se a fila esta vazia
    public boolean isEmpty(){
        return queue.isEmpty();
    }

    public void removeTicketById(UUID ticketId){
        queue.removeIf(ticketDTO -> ticketDTO.getId().equals(ticketId));
    }

    // Retorna a lista na ordem.
    public List<TicketDTO> getQueue(){
        return new ArrayList<>(queue);
    }

}
