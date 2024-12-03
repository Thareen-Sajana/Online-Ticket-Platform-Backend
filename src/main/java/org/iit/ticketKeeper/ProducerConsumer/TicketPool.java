package org.iit.ticketKeeper.ProducerConsumer;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.iit.ticketKeeper.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@ToString
public class TicketPool {


    private SessionService service;

    private List tickets;
    //private int totalTickets;
    private int ticketPoolCapacity;

    private Long sessionId;

    private int count;

    public TicketPool(SessionService service, int ticketPoolCapacity, Long sessionId) {
        //this.totalTickets = totalTickets;
        this.ticketPoolCapacity = ticketPoolCapacity;
        this.tickets = Collections.synchronizedList(new ArrayList<>());
        this.sessionId = sessionId;
        this.service = service;
        this.count = 1;
    }

    public synchronized void add() {
        while (tickets.size() >= ticketPoolCapacity){
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            notifyAll();
        }

        String add = "Ticket" + count++;
        tickets.add(add);
        System.out.println("Added : " + add);
    }

    public synchronized void remove() {
        while (tickets.isEmpty()){
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            notifyAll();
        }

        String removedElement = (String) tickets.remove(0);
        service.updateTotalTicket(sessionId, 1);
        System.out.println("Removed : " + removedElement);
        notifyAll();

    }


}
