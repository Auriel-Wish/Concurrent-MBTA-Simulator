import java.util.List;
import java.util.Map;

public class trainRunnable implements Runnable {
    public static MBTA mbta;
    public static Log log;
    public Train t;

    public trainRunnable(Train t) {
        this.t = t;
    }

    public void run() {
        while (!mbta.simEnd()) {
            synchronized (Train.class) {
                synchronized (t) {
                    try {
                        trainMove(t);
                        t.notifyAll();
                        Train.class.notifyAll();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    void trainMove(Train t) throws InterruptedException {
        // Rule 2
        List<String> trainRoute = mbta.lines.get(t.toString());
        Station currStation = mbta.trainPlacements.get(t);
        Station nextStation;

        if (mbta.trainDirections.get(t).equals(Direction.FORWARD)) {
            Station lastStation = Station.make(trainRoute.get(trainRoute.size() - 1));
            if (currStation != lastStation) {
                int index = trainRoute.indexOf(currStation.toString());
                nextStation = Station.make(trainRoute.get(index + 1));
                checkStationAvailability(mbta, nextStation, t);
            } else {
                nextStation = Station.make(trainRoute.get(trainRoute.size() - 2));
                checkStationAvailability(mbta, nextStation, t);
                mbta.trainDirections.put(t, Direction.BACKWARD);
            }
        } else {
            Station firstStation = Station.make(trainRoute.get(0));
            if (currStation != firstStation) {
                int index = trainRoute.indexOf(currStation.toString());
                nextStation = Station.make(trainRoute.get(index - 1));
                checkStationAvailability(mbta, nextStation, t);
            } else {
                nextStation = Station.make(trainRoute.get(1));
                checkStationAvailability(mbta, nextStation, t);
                mbta.trainDirections.put(t, Direction.FORWARD);
            }
        }

        mbta.trainPlacements.put(t, nextStation);
        log.train_moves(t, currStation, nextStation);
    }

    void checkStationAvailability(MBTA mbta, Station nextStation, Train t) throws InterruptedException {
        // Rule 3
        for (Map.Entry<Train, Station> trainMap : mbta.trainPlacements.entrySet()) {
            Train currTrain = trainMap.getKey();
            if (!currTrain.equals(t)) {
                while (nextStation == mbta.trainPlacements.get(currTrain)) {
                    // What to do about interrupted exception??
                    Train.class.wait();
                }
            }
        }
    }
}
