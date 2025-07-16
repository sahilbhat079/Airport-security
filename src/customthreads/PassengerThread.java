package customthreads;

import java.sql.SQLOutput;

//we could have also used the interface runnable and then use the executors to perform the task
//buy that we can achieve reusability of threads;
public class PassengerThread extends Thread{
    private final int id;
    private final AirportSecurity area;

    // for every passenger two things are necessary security check and gateOpen
    private  boolean scanned=false;
    private  boolean gateOpened=false;

    public PassengerThread(int id,AirportSecurity area){
        this.id=id;
        this.area=area;
        setName("Passenger-"+id);
    }

    public synchronized void markScanned(){
        scanned=true;
        notify();
    }

    public  synchronized  void markGateOpen(){
        gateOpened=true;
        notify();
    }


    @Override
    public void run() {
        try{
            System.out.println("\u001B[32m>>> [ARRIVAL] " + getName() + " arrived at airport.\u001B[0m");
            //if in case queue is full it blocks here
            area.Queue.put(this);

            //this thread will wait until security check and marked true
            synchronized (this){
                while(!scanned)wait();
            }

            //if the thread passed the security check it will wait for the gate pass
            synchronized (area.gateLock){
//                get added in the gate pass line that where only batch of 5 are passed
                area.readyPassengers.add(this);
                if(area.readyPassengers.size()==5){
                    //this thread will wait and notify other threads
                    // that are monitoring the gate lock
                    area.gateLock.notifyAll();
                }

            }

            //this thread will wait until gate-way check and marked true
            synchronized (this){
                while(!gateOpened)wait();
            }
            System.out.println("=====> [PASSED] " + getName() + " passed through the GATE.\n");

            //new not letting the passenger go before the increase count so that that will be used by the
            //gateway manager to verify the passed passenger and let other do there work
            synchronized (area.passLock){
                area.passedCount++;
                area.passLock.notifyAll();
                if(area.passedCount==area.TOTAL_PASSENGERS){
                    area.passLock.notify();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }





    }
}
