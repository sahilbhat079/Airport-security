package customthreads;

import java.util.Random;

public class SecurityOfficerThread extends  Thread {
    private final int officerId;
    private final AirportSecurity area;

    public SecurityOfficerThread(int Id,AirportSecurity area){
        this.officerId=Id;
        this.area=area;
        setName("Officer - "+Id);
    }


    @Override
    public void run() {
        while(true){
            //if the queue is empty and the exit status of area is true return
            if(area.shutdown && area.Queue.isEmpty()){
                System.out.println("==== [EXIT] " + getName() + " completed all scans. Exiting.");
            return;
            }

            try{
                //getting passengers from the Queue and check
                PassengerThread p=area.Queue.take(); //blocking operation since queue is thread safe

                //now the security guard try to get lock on the scan lock
                synchronized (area.scanLock){
                    System.out.println("\u001B[33m**** [SCANNING] " + getName() + " scanning " + p.getName() + "...\u001B[0m");
                    //simulate some time it take
                    Thread.sleep(1000 + new Random().nextInt(500));
//                    thread will sleep but the lock is not released
                }
//                this will mark the current thread is scanned
                p.markScanned();
            } catch( InterruptedException E){
                System.out.println(getName() +"  interrupted" );
                return;
            }




        }
    }
}
