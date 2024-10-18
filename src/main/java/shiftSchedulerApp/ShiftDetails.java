package shiftSchedulerApp;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

public class ShiftDetails {
    HashMap<String, String[]> shiftInfo = new HashMap<String, String[]>();
    ArrayList<String[]> shiftFile = new ArrayList<String[]>(); //used to hold login details
    ArrayList<String[]> customShiftDetails = new ArrayList<String[]>();
    ArrayList<Shift> shifts = new ArrayList<>();
    String filePath = "C:\\Users\\liams\\Downloads\\shift-scheduler-project\\shift-scheduler-project\\src\\main\\java\\shiftSchedulerApp\\shiftDetails.txt"; //shiftDetails file path
    String newFilePath = "C:\\Users\\liams\\Downloads\\shift-scheduler-project\\shift-scheduler-project\\src\\main\\java\\shiftSchedulerApp\\shiftDetails2.txt"; //path to temp login details for deleting
    String configFilePath = "C:\\Users\\liams\\Downloads\\shift-scheduler-project\\shift-scheduler-project\\src\\main\\java\\shiftSchedulerApp\\config.txt";
    EmployeeScheduler scheduler = SchedulingProgram.getScheduler();
    ArrayList<String> config;

    public ShiftDetails() throws Exception {
        //EmployeeScheduler scheduler = SchedulingProgram.getScheduler();
        config = getShiftsConfig();

        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String line;

        //read shiftDetails.txt, add its values into an arraylist of arrays, 1 array per line
        while (((line = br.readLine()) != null)) {
            String[] values = line.split(",");
            shiftFile.add(values);
        }

        //add the details to the hashmap used for checking whether a login is valid
        /*for (int x = 0; x < shiftInfo.size(); x++) {
            if (!("".equals(shiftInfo.get(x)[0]))) { //fixes error with blank lines
                String[] shiftSplit = shiftFile.get(x)[0].split("-");
            }
        }*/

        br.close();
    }

    public void addShift(int[][][] shiftArray) {
        try {
            FileWriter fileWriter = new FileWriter(filePath, StandardCharsets.UTF_8, false);
            for (int i = 1;i < scheduler.getNumDays()+1; i++) { //i = days
                for (int x = 1; x < scheduler.getNumEmployees()+1; x++) {
                    for (int y = 1; y < scheduler.getNumShifts()+1; y++) {
                        if (shiftArray[x-1][i - 1][y-1] == 1) {
                            //write to file in format "group-day-time"
                            fileWriter.write(x + "-" + i + "-" + y + "\r\n");
                            //shifts.add(new Shift(x,i,y));
                            //for
                            //System.out.println(shifts.get(0).getGroup());
                            //loginInfo.put(username, new String[] {password,admin});
                        }
                    }
                }
            }
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addShiftDetails(String shift, String[] details) {
        try {
            FileWriter fileWriter = new FileWriter(filePath, StandardCharsets.UTF_8, true);
            //fileWriter.write(",info2");
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getShiftsConfig() throws Exception {
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

    public ArrayList<String[]> getShiftFile() {
        return this.shiftFile;
    }

    public ArrayList<Shift> getShifts() {
        return this.shifts;
    }


}
