import com.google.gson.Gson;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class MBTA {
  Map<String, List<String>> lines = new HashMap<>();
  Map<String, List<String>> trips = new HashMap<>();
  Map<Train, Station> trainPlacements = new HashMap<>();
  Map<Passenger, Station> passengersAtStations = new HashMap<>();
  Map<Train, List<Passenger>> passengersOnboard = new HashMap<>();
  Map<Train, Direction> trainDirections = new HashMap<>();

  // Creates an initially empty simulation
  public MBTA() { }

  // Adds a new transit line with given name and stations
  public void addLine(String name, List<String> stations) {
    lines.put(name, stations);
    Train t = Train.make(name);
    trainDirections.put(t, Direction.FORWARD);
    trainPlacements.put(t, Station.make(stations.get(0)));
    passengersOnboard.put(t, new ArrayList<>());
  }

  // Adds a new planned journey to the simulation
  public void addJourney(String name, List<String> stations) {
    trips.put(name, stations);
    passengersAtStations.put(Passenger.make(name), Station.make(stations.get(0)));
  }

  // Return normally if initial simulation conditions are satisfied, otherwise
  // raises an exception
  public void checkStart() {
    for (Map.Entry<String, List<String>> line : lines.entrySet()) {
      Train currTrain = Train.make(line.getKey());
      Station currFirstStation = Station.make(line.getValue().get(0));
      if (trainPlacements.get(currTrain) != currFirstStation) {
        throw new RuntimeException();
      }
    }

    for (Map.Entry<String, List<String>> line : trips.entrySet()) {
      checkIndex(0, line);
    }
  }

  // Return normally if final simulation conditions are satisfied, otherwise
  // raises an exception
  public void checkEnd() {
    for (Map.Entry<String, List<String>> line : trips.entrySet()) {
      checkIndex(line.getValue().size() - 1, line);
    }
  }

  public void checkIndex(int index, Map.Entry<String, List<String>> line) {
    Passenger currPassenger = Passenger.make(line.getKey());
    Station currLastStation = Station.make(line.getValue().get(index));
    if (passengersAtStations.get(currPassenger) != currLastStation) {
      throw new RuntimeException();
    }
  }

  public boolean simEnd() {
    for (Map.Entry<String, List<String>> line : trips.entrySet()) {
      if (!line.getValue().isEmpty()) {
        return false;
      }
    }

    return true;
  }

  // reset to an empty simulation
  public void reset() {
    // FIX THIS
    lines.clear();
    trips.clear();
    trainPlacements.clear();
    passengersAtStations.clear();
    passengersOnboard.clear();
  }

  // adds simulation configuration from a file
  public void loadConfig(String filename) {
    try {
      String json = new String(Files.readAllBytes(Paths.get(filename)));
      Gson gson = new Gson();
      configHelper c = gson.fromJson(json, configHelper.class);

      for (Map.Entry<String, List<String>> line : c.lines.entrySet()) {
        for (String s : line.getValue()) {
          Station.make(s);
        }
        addLine(line.getKey(), line.getValue());
      }

      for (Map.Entry<String, List<String>> trip : c.trips.entrySet()) {
        addJourney(trip.getKey(), trip.getValue());
      }
    }
    catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  public static void throwErrorIfNotEqual(Object a, Object b) {
    if (!a.equals(b)) {
      throw new RuntimeException();
    }
  }
  public static void throwErrorIfEqual(Object a, Object b) {
    if (a.equals(b)) {
      throw new RuntimeException();
    }
  }
}
