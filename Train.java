import java.util.HashMap;
import java.util.Map;

public class Train extends Entity {
  public static Map<String, Train> trains = new HashMap<>();
  private Train(String name) { super(name); }
  public static Train make(String name) {
    if (trains.containsKey(name)) {
      return trains.get(name);
    }
    Train ret = new Train(name);
    trains.put(name, ret);
    return ret;
  }
}
