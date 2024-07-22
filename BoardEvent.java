import java.util.*;

public class BoardEvent implements Event {
  public final Passenger p; public final Train t; public final Station s;
  public BoardEvent(Passenger p, Train t, Station s) {
    this.p = p; this.t = t; this.s = s;
  }
  public boolean equals(Object o) {
    if (o instanceof BoardEvent e) {
      return p.equals(e.p) && t.equals(e.t) && s.equals(e.s);
    }
    return false;
  }
  public int hashCode() {
    return Objects.hash(p, t, s);
  }
  public String toString() {
    return "Passenger " + p + " boards " + t + " at " + s;
  }
  public List<String> toStringList() {
    return List.of(p.toString(), t.toString(), s.toString());
  }
  public void replayAndCheck(MBTA mbta) {
    Station currStation = mbta.passengersAtStations.get(p);
    MBTA.throwErrorIfNotEqual((Object) s, (Object) currStation);

    currStation = mbta.trainPlacements.get(t);
    MBTA.throwErrorIfNotEqual((Object) s, (Object) currStation);

    mbta.passengersAtStations.remove(p);
    List<Passenger> passengerList = mbta.passengersOnboard.get(t);
    passengerList.add(p);
    mbta.passengersOnboard.put(t, passengerList);
  }
}
