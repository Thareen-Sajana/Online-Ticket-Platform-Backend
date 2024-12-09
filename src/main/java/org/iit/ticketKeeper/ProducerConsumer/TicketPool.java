package org.iit.ticketKeeper.ProducerConsumer;

import lombok.ToString;
import org.iit.ticketKeeper.dto.ReportResponse;
import org.iit.ticketKeeper.service.SessionService;
import org.iit.ticketKeeper.service.WebSocketService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@ToString
public class TicketPool {


    private SessionService sessionService;

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
        this.sessionService = service;
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
        ReportResponse reportResponse = new ReportResponse();
        reportResponse.setCurrentPoolCapacity(tickets.size());
        reportResponse.setSessionId(sessionId);
        sessionService.reportData(sessionId, reportResponse);
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
        sessionService.updateTotalTicket(sessionId, 1);
        sessionService.updateTotalTicket(String.valueOf(sessionId));
        System.out.println("Removed : " + removedElement);

        ReportResponse reportResponse = new ReportResponse();
        reportResponse.setCurrentPoolCapacity(tickets.size());
        reportResponse.setSessionId(sessionId);
        sessionService.reportData(sessionId, reportResponse);
        notifyAll();

    }


}
