import java.util.*;

public class DeboardEvent implements Event {
  public final Passenger p; public final Train t; public final Station s;
  public DeboardEvent(Passenger p, Train t, Station s) {
    this.p = p; this.t = t; this.s = s;
  }
  public boolean equals(Object o) {
    if (o instanceof DeboardEvent e) {
      return p.equals(e.p) && t.equals(e.t) && s.equals(e.s);
    }
    return false;
  }
  public int hashCode() {
    return Objects.hash(p, t, s);
  }
  public String toString() {
    return "Passenger " + p + " deboards " + t + " at " + s;
  }
  public List<String> toStringList() {
    return List.of(p.toString(), t.toString(), s.toString());
  }
  public void replayAndCheck(MBTA mbta) {
    Station currStation = mbta.trainPlacements.get(t);
    MBTA.throwErrorIfNotEqual((Object) s, (Object) currStation);

    List<String> stops = mbta.trips.get(p.toString());
    stops = new ArrayList<>(stops);
    stops.remove(0);
    mbta.trips.put(p.toString(), stops);
    Station destination = Station.make(stops.get(0));
    MBTA.throwErrorIfNotEqual((Object) s, (Object) destination);

    mbta.passengersAtStations.put(p, s);
    List<Passenger> ps = mbta.passengersOnboard.get(t);
    ps.remove(p);
    mbta.passengersOnboard.put(t, ps);
  }
}