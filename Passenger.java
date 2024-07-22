import java.util.HashMap;
import java.util.Map;

public class Passenger extends Entity {
  public static Map<String, Passenger> passengers = new HashMap<>();
  private Passenger(String name) { super(name); }

  public static Passenger make(String name) {
    if (passengers.containsKey(name)) {
      return passengers.get(name);
    }
    Passenger ret = new Passenger(name);
    passengers.put(name, ret);
    return ret;
  }
}
