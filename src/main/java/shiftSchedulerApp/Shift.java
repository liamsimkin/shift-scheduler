package shiftSchedulerApp;

public class Shift {
    private int group;
    private int day;
    private int time;
    private String location;
    //private PointsCoordinates coordinates;

    public Shift(int group, int day, int time) {
        EmployeeScheduler scheduler = SchedulingProgram.getScheduler();
        int[][][] shiftArray = SchedulingProgram.getShiftArray();

        this.group = group;
        this.day = day;
        this.time = time;

    }

    //public void setCoordinates(PointsCoordinates coordinates) {
        //this.coordinates = coordinates;
    //}

    public void setGroup(int group) {
        this.group = group;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    //public PointsCoordinates getCoordinates() {
    //    return this.coordinates;
    //}

    public int getGroup() {
        return this.group;
    }

    public int getDay() {
        return this.day;
    }

    public int getTime() {
        return this.time;
    }

    public String getLocation() {
        return this.location;
    }
}
