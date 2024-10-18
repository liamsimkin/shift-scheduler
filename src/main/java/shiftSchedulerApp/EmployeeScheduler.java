package shiftSchedulerApp;
import com.google.ortools.Loader;
import com.google.ortools.sat.CpModel;
import com.google.ortools.sat.CpSolver;
import com.google.ortools.sat.CpSolverSolutionCallback;
import com.google.ortools.sat.CpSolverStatus;
import com.google.ortools.sat.LinearExpr;
import com.google.ortools.sat.LinearExprBuilder;
import com.google.ortools.sat.Literal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class EmployeeScheduler implements Scheduler{

    private int numEmployees = 0;
    private int numShifts = 0;
    private int numDays = 0;

    private int[] allEmployees;
    private int[] allDays;
    private int[] allShifts;

    @Override
    public void initialiseParams(int numEmployees, int numDays, int numShifts) {
        this.numEmployees = numEmployees;
        this.numDays = numDays;
        this.numShifts = numShifts;

        this.allEmployees = IntStream.range(0, numEmployees).toArray();
        this.allDays = IntStream.range(0, numDays).toArray();
        this.allShifts = IntStream.range(0, numShifts).toArray();
    }

    public int getNumEmployees() {
        return numEmployees;
    }

    public int getNumShifts(){
        return numShifts;
    }

    public int getNumDays(){
        return numDays;
    }

    public int[] getAllEmployees(){
        return allEmployees;
    }

    public int[] getAllShifts(){
        return allShifts;
    }

    public int[] getAllDays(){
        return allDays;
    }

    /*public void routeTest() throws IOException {
        RoutesSettings routesSettings = RoutesSettings.newBuilder()
                .setHeaderProvider(() -> {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("X-Goog-FieldMask", "*");
                    return headers;
                }).build();
        RoutesClient routesClient = RoutesClient.create(routesSettings);

        ComputeRoutesResponse response = routesClient.computeRoutes(ComputeRoutesRequest.newBuilder()
                .setOrigin(Waypoint.newBuilder().setPlaceId("ChIJeRpOeF67j4AR9ydy_PIzPuM").build())
                .setDestination(Waypoint.newBuilder().setPlaceId("ChIJG3kh4hq6j4AR_XuFQnV0_t8").build())
                .setRoutingPreference(RoutingPreference.TRAFFIC_AWARE)
                .setTravelMode(RouteTravelMode.DRIVE).build());
        System.out.println("Response: " + response.toString());
    }*/
}