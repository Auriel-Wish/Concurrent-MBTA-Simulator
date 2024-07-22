import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Sim {
  public static void run_sim(MBTA mbta, Log log) {
    Train x;
    Passenger y;

    trainRunnable tr;
    trainRunnable.mbta = mbta;
    trainRunnable.log = log;

    passengerRunnable pr;
    passengerRunnable.mbta = mbta;
    passengerRunnable.log = log;

    Thread thread;
    List<Thread> allThreads = new ArrayList<>();
    for (Map.Entry<String, List<String>> line : mbta.lines.entrySet()) {
      x = Train.make(line.getKey());
      tr = new trainRunnable(x);
      thread = new Thread(tr);
      allThreads.add(thread);
    }
    for (Map.Entry<String, List<String>> line : mbta.trips.entrySet()) {
      y = Passenger.make(line.getKey());
      x = getPassengerLine(line.getValue(), mbta);
      if (x == null) {
        throw new RuntimeException("t is null for " + y);
      }
      pr = new passengerRunnable(y, x);
      thread = new Thread(pr);
      allThreads.add(thread);
    }

    for (Thread currThread : allThreads) {
      currThread.start();
    }

    for (Thread currThread : allThreads) {
      try {
        currThread.join();
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
  }

  public static Train getPassengerLine(List<String> route, MBTA mbta) {
    Train x = null;
    List<String> currRoute;
    for (Map.Entry<String, List<String>> line : mbta.lines.entrySet()) {
      currRoute = line.getValue();
      if (currRoute.contains(route.get(0)) && currRoute.contains(route.get(1))) {
        x = Train.make(line.getKey());
        break;
      }
    }

    return x;
  }

  public static void main(String[] args) throws Exception {
    if (args.length != 1) {
      System.out.println("usage: ./sim <config file>");
      System.exit(1);
    }

    MBTA mbta = new MBTA();
    mbta.loadConfig(args[0]);

    Log log = new Log();

    run_sim(mbta, log);

    String s = new LogJson(log).toJson();
    PrintWriter out = new PrintWriter("log.json");
    out.print(s);
    out.close();

    mbta.reset();
    mbta.loadConfig(args[0]);
    Verify.verify(mbta, log);
  }
}
