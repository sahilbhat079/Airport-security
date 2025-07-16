import customthreads.AirportSecurity;
import customthreads.GatewayManagerThread;
import customthreads.PassengerThread;
import customthreads.SecurityOfficerThread;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws  InterruptedException {

        int totalPassengers = 15;
        int maxQueueSize = 10;

        //shared area
        AirportSecurity area = new AirportSecurity(totalPassengers, maxQueueSize);

        //gate manager thread
        Thread gateManager = new GatewayManagerThread(area);
        gateManager.start();


        //Security officer
        SecurityOfficerThread officer1 = new SecurityOfficerThread(1, area);
        SecurityOfficerThread officer2 = new SecurityOfficerThread(2, area);
        officer1.start();
//        officer2.start();

        List<PassengerThread> passengers = new ArrayList<>();

        for (int i = 1; i <= totalPassengers; i++) {
            PassengerThread p = new PassengerThread(i, area);
            p.start();
            passengers.add(p);
            Thread.sleep(new Random().nextInt(500));
        }

        gateManager.join();
        officer1.join();

        System.out.println("all passenger cleared the checks");

    }





}