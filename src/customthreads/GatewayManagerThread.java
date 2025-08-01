package customthreads;

import java.util.ArrayList;
import java.util.List;

public class GatewayManagerThread extends Thread{
    private final AirportSecurity area;

    public GatewayManagerThread(AirportSecurity area){
        this.area=area;
        setName("GateManager");
    }

    @Override
    public void run() {
        while(true){
            List<PassengerThread> group;

            //if the gate lock is acquired make it wait until the group of 5 members are available
            synchronized (area.gateLock) {
                while (area.readyPassengers.size() < 5) {
                    try {
                        System.out.println("\n===============================================");
                        System.out.println("=== [WAITING] " + getName() + " waiting for group of 5 passengers. ===");
                        System.out.println("===============================================\n");
                        area.gateLock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                group = new ArrayList<>(area.readyPassengers.subList(0, 5));
                area.readyPassengers.subList(0,5).clear();
                System.out.println("\u001B[31m\n===============================");
                System.out.println(">>>>>> [GATE OPEN] Group of 5 ready. Gate is opening...");
                System.out.println("===============================\n\u001B[0m");
            }


            for(PassengerThread p:group){
                p.markGateOpen();
            }

            //not letting the gateway manager thread to go util all passed
            synchronized (area.passLock){
                int expected=area.passedCount+5;
                while(area.passedCount < expected){
                    try{
                        area.passLock.wait();
                    }
                    catch (InterruptedException e){
                        System.out.println("[INTERRUPTED] " + getName() + " was interrupted while waiting for passengers to pass.");
                        return;
                    }
                }
            }



            synchronized (area.shutdownLock){
                area.totalProcessed+=5;
                if(area.totalProcessed>=area.TOTAL_PASSENGERS){
                    synchronized (area.passLock){
                        while(area.passedCount<area.TOTAL_PASSENGERS){
                            try {
                               area.passLock.wait();
                            }
                            catch (InterruptedException e){
                                e.printStackTrace();
                            }
                        }
                    }
                    area.shutdown=true;
                    System.out.print("GateManager finished ");
                    return;
                }
            }

        }
    }
}
