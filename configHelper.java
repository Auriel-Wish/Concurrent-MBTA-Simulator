import com.google.gson.Gson;

import java.util.List;
import java.util.Map;

public class configHelper {
    public Map<String, List<String>> lines;
    public Map<String, List<String>> trips;

    public static void main(String[] args) {
        MBTA mbta = new MBTA();

        mbta.loadConfig("sample.json");
    }
}
