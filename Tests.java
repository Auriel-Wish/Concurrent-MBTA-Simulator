import static org.junit.Assert.*;
import org.junit.*;
import java.util.List;

public class Tests {
//  @Test public void class_test_verify_trip_complete() {
//    // Setup
//    MBTA new_mbta = new MBTA();
//    Log log = new Log();
//
//    Station oakgrove = Station.make("Oak Grove");
//    Station maldencenter = Station.make("Malden Center");
//    Station wellington = Station.make("Wellington");
//    Station assembly = Station.make("Assembly");
//    Passenger p = Passenger.make("Terve");
//    Train orange = Train.make("orange");
//
//    new_mbta.addLine("orange", List.of("Oak Grove", "Malden Center", "Wellington", "Assembly"));
//    new_mbta.addJourney("Terve", List.of("Oak Grove", "Wellington", "Malden Center"));
//
//    // Verification
//    Log events = new Log(List.of(
//            new BoardEvent(p, orange, oakgrove),
//            new MoveEvent(orange, oakgrove, maldencenter),
//            new MoveEvent(orange, maldencenter, wellington),
//            new DeboardEvent(p, orange, wellington),
//            new MoveEvent(orange, wellington, assembly),
//            new MoveEvent(orange, assembly, wellington),
//            new BoardEvent(p, orange, wellington),
//            new MoveEvent(orange, wellington, maldencenter),
//            new DeboardEvent(p, orange, maldencenter)
//            ));
//
//    Verify.verify(new_mbta, events);
//  }
//
//  @Test public void test2() {
//    // Setup
//    MBTA new_mbta = new MBTA();
//    Log log = new Log();
//
//    Station s1 = Station.make("s1");
//    Station s2 = Station.make("s2");
//    Station s3 = Station.make("s3");
//    Station s4 = Station.make("s4");
//    Passenger p1 = Passenger.make("p1");
//    Passenger p2 = Passenger.make("p2");
//    Train t1 = Train.make("t1");
//
//    new_mbta.addLine("t1", List.of("s1", "s2", "s3", "s4"));
//    new_mbta.addJourney("p1", List.of("s2", "s3"));
//    new_mbta.addJourney("p2", List.of("s1", "s3", "s2"));
//
//    Log events = new Log(List.of(
//            new BoardEvent(p2, t1, s1),
//            new MoveEvent(t1, s1, s2),
//            new BoardEvent(p1, t1, s2),
//            new MoveEvent(t1, s2, s3),
//            new DeboardEvent(p2, t1, s3),
//            new DeboardEvent(p1, t1, s3),
//            new MoveEvent(t1, s3, s4),
//            new MoveEvent(t1, s4, s3),
//            new BoardEvent(p2, t1, s3)
//    ));
//
//    try {
//      Verify.verify(new_mbta, events);
//      throw new RuntimeException();
//    } catch (Exception e) {}
//  }

  @Test public void test3() {
    MBTA mbta = new MBTA();

    Train a = Train.make("a");
    Train d = Train.make("d");
    Station A = Station.make("A");
    Station B = Station.make("B");
    Station C = Station.make("C");
    Station J = Station.make("J");
    Station K = Station.make("K");
    Station L = Station.make("L");
    Passenger Alice = Passenger.make("Alice");
    Passenger Bob = Passenger.make("Bob");

    mbta.addLine("a", List.of("A", "B", "C"));
    mbta.addLine("d", List.of("J", "K", "L"));
    mbta.addJourney("Alice", List.of("A", "B"));
    mbta.addJourney("Bob", List.of("J", "L"));

    Log events = new Log(List.of(
      new BoardEvent(Alice, a, A),
      new MoveEvent(a, A, B),
      new DeboardEvent(Alice, a, B),
      new MoveEvent(a, B, C),
      new MoveEvent(d, J, K),
      new MoveEvent(d, K, L)
    ));

    try {
      Verify.verify(mbta, events);
      throw new RuntimeException();
    } catch (Exception e) {}
  }
}
