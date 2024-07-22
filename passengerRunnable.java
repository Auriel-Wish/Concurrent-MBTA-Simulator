import java.util.ArrayList;
import java.util.List;

public class passengerRunnable implements Runnable {
    public static MBTA mbta;
    public static Log log;
    public Passenger p;
    public Train t;

    public passengerRunnable(Passenger p, Train t) {
        this.p = p;
        this.t = t;
    }

    public void run() {
        Station currStation;
        Station passengerStation;
        while (!mbta.trips.get(p.toString()).isEmpty()) {
            Train temp = t;
            synchronized (temp) {
                currStation = mbta.trainPlacements.get(t);

                if (mbta.passengersAtStations.containsKey(p) && !((mbta.trips.get(p.toString())).isEmpty())) {
                    passengerStation = mbta.passengersAtStations.get(p);
                    passengerBoard(currStation, passengerStation);
                } else if ((mbta.passengersOnboard.get(t)).contains(p)) {
                    passengerStation = Station.make(mbta.trips.get(p.toString()).get(0));
                    passengerDeboard(currStation, passengerStation);
                }
                else {
                    throw new RuntimeException("Passenger is not at a station AND not on a train");
                }
                temp.notifyAll();
            }
        }
    }

    void passengerBoard(Station currStation, Station passengerStation) {
        if (passengerStation.equals(currStation)) {
            mbta.passengersAtStations.remove(p);
            List<Passenger> passengerList = mbta.passengersOnboard.get(t);
            passengerList.add(p);
            mbta.passengersOnboard.put(t, passengerList);
            List<String> stops = mbta.trips.get(p.toString());
            stops.remove(0);
            mbta.trips.put(p.toString(), stops);
            log.passenger_boards(p, t, passengerStation);
        }
    }

    void passengerDeboard(Station currStation, Station passengerNextStation) {
//        System.out.println(currStation.toString());
//        System.out.println(passengerNextStation.toString());
        if (currStation.equals(passengerNextStation)) {
//            List<String> stops = mbta.trips.get(p.toString());
//            stops = new ArrayList<>(stops);
//
//            stops.remove(0);
//            mbta.trips.put(p.toString(), stops);
            mbta.passengersAtStations.put(p, currStation);
            List<Passenger> ps = mbta.passengersOnboard.get(t);
            ps.remove(p);
            mbta.passengersOnboard.put(t, ps);
            log.passenger_deboards(p, t, passengerNextStation);

            if (mbta.trips.get(p.toString()).size() > 1) {
                t = Sim.getPassengerLine(mbta.trips.get(p.toString()), mbta);
            }

            if (mbta.trips.get(p.toString()).size() == 1) {
                mbta.trips.put(p.toString(), new ArrayList<>());
            }
        }
    }
}
