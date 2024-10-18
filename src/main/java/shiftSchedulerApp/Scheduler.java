package shiftSchedulerApp;

import com.google.ortools.Loader;
import com.google.ortools.sat.CpModel;
import com.google.ortools.sat.CpSolver;
import com.google.ortools.sat.CpSolverSolutionCallback;
import com.google.ortools.sat.CpSolverStatus;
import com.google.ortools.sat.LinearExpr;
import com.google.ortools.sat.LinearExprBuilder;
import com.google.ortools.sat.Literal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public interface Scheduler {
    
    int numDays = 0;
    int numShifts = 0;

    /**
     * initialises the scheduler with the given parameters
     * @param x the number of employees ###############################
     * @param days number of days
     * @param shifts number of shifts per day
     */
    void initialiseParams(int x, int days, int shifts);

}