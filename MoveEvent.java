import java.util.*;

public class MoveEvent implements Event {
  public final Train t; public final Station s1, s2;
  public MoveEvent(Train t, Station s1, Station s2) {
    this.t = t; this.s1 = s1; this.s2 = s2;
  }
  public boolean equals(Object o) {
    if (o instanceof MoveEvent e) {
      return t.equals(e.t) && s1.equals(e.s1) && s2.equals(e.s2);
    }
    return false;
  }
  public int hashCode() {
    return Objects.hash(t, s1, s2);
  }
  public String toString() {
    return "Train " + t + " moves from " + s1 + " to " + s2;
  }
  public List<String> toStringList() {
    return List.of(t.toString(), s1.toString(), s2.toString());
  }
  public void replayAndCheck(MBTA mbta) {
    // Rule 2
    List<String> trainRoute = mbta.lines.get(t.toString());
    Station currStation = mbta.trainPlacements.get(t);
    MBTA.throwErrorIfNotEqual((Object) s1, (Object) currStation);
    Station nextStation;

    if (mbta.trainDirections.get(t).equals(Direction.FORWARD)) {
      Station lastStation = Station.make(trainRoute.get(trainRoute.size() - 1));
      if (currStation != lastStation) {
        int index = trainRoute.indexOf(s1.toString());
        nextStation = Station.make(trainRoute.get(index + 1));
      }
      else {
        nextStation = Station.make(trainRoute.get(trainRoute.size() - 2));
        mbta.trainDirections.put(t, Direction.BACKWARD);
      }
    }
    else {
      Station firstStation = Station.make(trainRoute.get(0));
      if (currStation != firstStation) {
        int index = trainRoute.indexOf(s1.toString());
        nextStation = Station.make(trainRoute.get(index - 1));
      }
      else {
        nextStation = Station.make(trainRoute.get(1));
        mbta.trainDirections.put(t, Direction.FORWARD);
      }
    }
    MBTA.throwErrorIfNotEqual((Object) s2, (Object) nextStation);

    // Rule 3
    for (Map.Entry<String, Train> train : Train.trains.entrySet()) {
      Train currTrain = train.getValue();
      if (!currTrain.equals(t)) {
        Station otherTrainStation = mbta.trainPlacements.get(currTrain);
        MBTA.throwErrorIfEqual((Object) s1, (Object) otherTrainStation);
        MBTA.throwErrorIfEqual((Object) s2, (Object) otherTrainStation);
      }
    }

    mbta.trainPlacements.put(t, s2);
  }
}