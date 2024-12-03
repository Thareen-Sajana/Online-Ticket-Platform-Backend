package org.iit.ticketKeeper.ProducerConsumer;

public class Producer implements Runnable{

    private TicketPool pool;
    private int totalTickets;

    private int ticketReleaseRate;

    public Producer (TicketPool pool, int totalTickets, int ticketReleaseRate){
        this.pool = pool;
        this.totalTickets = totalTickets;
        this.ticketReleaseRate = ticketReleaseRate;
    }
    @Override
    public void run() {
        for (int i = 0; i < totalTickets; i++) {
            pool.add();
            try {
                Thread.sleep(ticketReleaseRate);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
