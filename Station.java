import java.util.HashMap;
import java.util.Map;

public class Station extends Entity {
  public static Map<String, Station> stations = new HashMap<>();

  private Station(String name) { super(name); }

  public static Station make(String name) {
    if (stations.containsKey(name)) {
      return stations.get(name);
    }
    Station ret = new Station(name);
    stations.put(name, ret);
    return ret;
  }
}
