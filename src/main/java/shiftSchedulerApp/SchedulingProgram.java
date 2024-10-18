package shiftSchedulerApp;
import com.google.ortools.Loader;
import com.google.ortools.sat.CpModel;
import com.google.ortools.sat.CpSolver;
import com.google.ortools.sat.CpSolverSolutionCallback;
import com.google.ortools.sat.CpSolverStatus;
import com.google.ortools.sat.LinearExpr;
import com.google.ortools.sat.LinearExprBuilder;
import com.google.ortools.sat.Literal;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;


public class SchedulingProgram {

  private static String afterLoginText = "";
  private static int[][][] shiftArray;
  private static ArrayList<String> config;
  public static EmployeeScheduler scheduler = new EmployeeScheduler();
  private final static String configFilePath = "C:\\Users\\liams\\Downloads\\shift-scheduler-project\\shift-scheduler-project\\src\\main\\java\\shiftSchedulerApp\\shiftsConfig.txt";
  private static int numEmployees;
  private static int numShifts;
  private static int numDays;


  public static void main(String[] args) throws Exception {
    actualMain(args);
  }

  public static void actualMain(String[] args) throws Exception {
    Loader.loadNativeLibraries();
    final int solutionLimit = 1;
    ShiftDetails shiftDetails = new ShiftDetails();
    System.out.println(args.length);

    if (args.length == 0) {
      numEmployees = 7;
      numDays = 7;
      numShifts = 3;
    }
    else {
      numEmployees = Integer.valueOf(args[0]);
      numShifts = Integer.valueOf(args[1]);
      numDays = 7;
    }

    shiftArray = new int[numEmployees][numDays][numShifts];
    // 0,0,0 = 1st group, 1st day, 1st shift, 0 = not work, 1 = work

    scheduler.initialiseParams(numEmployees, numDays, numShifts);

    // Creates the model.
    CpModel model = new CpModel();

    // Creates shift variables.
    // shifts[(n, d, s)]: employee 'n' works shift 's' on day 'd'.
    Literal[][][] shifts = new Literal[scheduler.getNumEmployees()][scheduler.getNumDays()][scheduler.getNumShifts()];
    for (int e : scheduler.getAllEmployees()) {
      for (int d : scheduler.getAllDays()) {
        for (int s : scheduler.getAllShifts()) {
          shifts[e][d][s] = model.newBoolVar("shifts_e" + e + "d" + d + "s" + s);
        }
      }
    }

    // Each shift is assigned to exactly one group in the schedule period.
    for (int d : scheduler.getAllDays()) {
      for (int s : scheduler.getAllShifts()) {
        List<Literal> employees = new ArrayList<>();
        for (int e : scheduler.getAllEmployees()) {
          employees.add(shifts[e][d][s]);
        }
        model.addExactlyOne(employees);
      }
    }

    // Each group works at most one shift per day.
    for (int e : scheduler.getAllEmployees()) {
      for (int d : scheduler.getAllDays()) {
        List<Literal> work = new ArrayList<>();
        for (int s : scheduler.getAllShifts()) {
          work.add(shifts[e][d][s]);
        }
        model.addAtMostOne(work);
      }
    }

    // Try to distribute the shifts evenly, so that each group works
    // minShiftsPerEmployee shifts. If this is not possible, because the total
    // number of shifts is not divisible by the number of employees, some employees will
    // be assigned one more shift.
    int minShiftsPerEmployee = (scheduler.getNumShifts() * scheduler.getNumDays()) / scheduler.getNumEmployees();
    int maxShiftsPerEmployee;
    if ((scheduler.getNumShifts() * scheduler.getNumDays()) % scheduler.getNumEmployees() == 0) {
      maxShiftsPerEmployee = minShiftsPerEmployee;
    } else {
      maxShiftsPerEmployee = minShiftsPerEmployee + 1;
    }
    for (int e : scheduler.getAllEmployees()) {
      LinearExprBuilder shiftsWorked = LinearExpr.newBuilder();
      for (int d : scheduler.getAllDays()) {
        for (int s : scheduler.getAllShifts()) {
          shiftsWorked.add(shifts[e][d][s]);
        }
      }
      model.addLinearConstraint(shiftsWorked, minShiftsPerEmployee, maxShiftsPerEmployee);
    }

    CpSolver solver = new CpSolver();
    solver.getParameters().setLinearizationLevel(0);
    // Tell the solver to enumerate all solutions.
    solver.getParameters().setEnumerateAllSolutions(true);

    // Display the first five solutions.
    //final int solutionLimit = 5; changed up top
    class VarArraySolutionPrinterWithLimit extends CpSolverSolutionCallback {
      public VarArraySolutionPrinterWithLimit(
              int[] allEmployees, int[] allDays, int[] allShifts, Literal[][][] shifts, int limit) {
        solutionCount = 0;
        this.allEmployees = allEmployees;
        this.allDays = allDays;
        this.allShifts = allShifts;
        this.shifts = shifts;
        solutionLimit = limit;
      }

      @Override
      public void onSolutionCallback() {
        System.out.printf("Solution #%d:%n", solutionCount);
        SchedulingProgram.afterLoginText += "Solution "+ solutionCount + "<br/>";
        for (int d : allDays) {
          System.out.printf("Day %d%n", d);
          SchedulingProgram.afterLoginText += "Day " + d + "<br/>";
          for (int e : allEmployees) {
            boolean isWorking = false;
            for (int s : allShifts) {
              if (booleanValue(shifts[e][d][s])) {
                isWorking = true;
                System.out.printf("  Employee %d work shift %d%n", e, s);
                shiftArray[e][d][s] = 1;
                SchedulingProgram.afterLoginText += "Employee " +e+ " work shift "+s + "<br/>";
              }
            }
            if (!isWorking) {
              System.out.printf("  Employee %d does not work%n", e);
              shiftArray[e][d][0] = 0;
              shiftArray[e][d][1] = 0;
              shiftArray[e][d][2] = 0;
              SchedulingProgram.afterLoginText += "Employee " +e+ " does not work" + "<br/>";
            }
          }
        }
        solutionCount++;
        if (solutionCount >= solutionLimit) {
          System.out.printf("Stop search after %d solutions%n", solutionLimit);
          SchedulingProgram.afterLoginText += "Stop search after "+solutionLimit+ " solutions" + "<br/>";
          stopSearch();
        }
      }

      public int getSolutionCount() {
        return solutionCount;
      }

      private int solutionCount;
      private final int[] allEmployees;
      private final int[] allDays;
      private final int[] allShifts;
      private final Literal[][][] shifts;
      private final int solutionLimit;
    }

    VarArraySolutionPrinterWithLimit cb =
            new VarArraySolutionPrinterWithLimit(scheduler.getAllEmployees(), scheduler.getAllDays(), scheduler.getAllShifts(), shifts, solutionLimit);

    // Creates a solver and solves the model.
    CpSolverStatus status = solver.solve(model, cb);
    /* Print the status and statistics
    System.out.println("Status: " + status);
    System.out.println(cb.getSolutionCount() + " solutions found.");

    // Statistics.
    System.out.println("Statistics");
    System.out.printf("  conflicts: %d%n", solver.numConflicts());
    System.out.printf("  branches : %d%n", solver.numBranches());
    System.out.printf("  wall time: %f s%n", solver.wallTime());

    //System.out.println(afterLoginText);
    //System.out.println(shiftArray[0][0])

    for (int i = 1;i < scheduler.getNumDays()+1; i++) { //i = days
      for (int x = 1; x < scheduler.getNumEmployees()+1; x++) {
        for (int y = 1; y < scheduler.getNumShifts()+1; y++) {
          if (shiftArray[x-1][i - 1][y-1] == 1) {
            System.out.println(x + "-" + i + "-" + y);
            //write to file

          }
        }
      }
    }*/
    //ShiftDetails shiftDetails = new ShiftDetails();
    shiftDetails.addShift(shiftArray);
  }

  public static String getAfterLoginText() {
    return afterLoginText;
  }

  public static int[][][] getShiftArray() {
    return shiftArray;
  }

  public static EmployeeScheduler getScheduler(){ return scheduler;}

  public static ArrayList<String> getShiftsConfig() throws Exception {
    ArrayList<String> config = new ArrayList<>();
    BufferedReader br = new BufferedReader(new FileReader(configFilePath));
    String line;
    int i = 0;

    //read config file, add its values into an arraylist of arrays, 1 array per line
    while (((line = br.readLine()) != null)) {
      String[] values = line.split(",");
      config.add(values[0]);
      config.add(values[1]);
    }

    br.close();
    return config;
  }

  public SchedulingProgram() {}
}