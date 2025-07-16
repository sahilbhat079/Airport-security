package customthreads;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class AirportSecurity {
    //this area is shared with threads and every thread uses there turn to proceed

    //it will have total passengers and max queue size and all the lock for different areas
    public final int TOTAL_PASSENGERS;
    public final int MAX_QUEUE_SIZE;


    // a blocking  queue that is thread safe and blocks if empty or full and resumes from there
    public final BlockingQueue<PassengerThread> Queue;

    //three locks we could have used only one or a static so that only one thread can access the area
    public final Object scanLock=new Object();
    public final Object gateLock =new Object();
    public final Object shutdownLock=new Object();

    // a list that hold all passengers passed the security check
    public final List<PassengerThread> readyPassengers=new ArrayList<>();

    //variable to check all proceed or not and shutdown
    public volatile boolean shutdown =false;
    public int totalProcessed =0;

    //constructor
    public AirportSecurity(int TOTAL_PASSENGERS, int MAX_QUEUE_SIZE) {
        this.TOTAL_PASSENGERS = TOTAL_PASSENGERS;
        this.MAX_QUEUE_SIZE = MAX_QUEUE_SIZE;
        this.Queue=new ArrayBlockingQueue<>(MAX_QUEUE_SIZE);
    }

}
