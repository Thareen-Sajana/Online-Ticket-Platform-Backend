package org.iit.ticketKeeper.ProducerConsumer;

public class Consumer implements Runnable{

    private TicketPool pool;
    private int size;
    private int customerReleaseRate;

    public Consumer (TicketPool pool, int size, int customerReleaseRate){
        this.pool = pool;
        this.size = size;
        this.customerReleaseRate = customerReleaseRate;
    }
    @Override
    public void run() {
        for (int i = 0; i < size; i++) {
            pool.remove();
            try {
                Thread.sleep(customerReleaseRate);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
