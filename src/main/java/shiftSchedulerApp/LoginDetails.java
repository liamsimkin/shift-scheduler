package shiftSchedulerApp;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class LoginDetails {

    HashMap<String, String[]> loginInfo = new HashMap<String, String[]>();
    ArrayList<String[]> logins = new ArrayList<String[]>(); //used to hold login details
    ArrayList<String[]> tempLogins = new ArrayList<String[]>(); //used to hold login details when deleting
    String filePath = "C:\\Users\\liams\\Downloads\\shift-scheduler-project\\shift-scheduler-project\\src\\main\\java\\shiftSchedulerApp\\loginDetails.txt"; //loginDetails file path
    String newFilePath = "C:\\Users\\liams\\Downloads\\shift-scheduler-project\\shift-scheduler-project\\src\\main\\java\\shiftSchedulerApp\\loginDetails2.txt"; //path to temp login details for deleting

    /**
     * reads and stores the login info from loginDetails.txt
     */
    public LoginDetails() throws Exception {

        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String line;

        //read loginDetails.txt, add its values into an arraylist of arrays, 1 array per line
        while (((line = br.readLine()) != null)) {
            String[] values = line.split(",");
            logins.add(values);
        }

        //add the details to the hashmap used for checking whether a login is valid
        for (int x = 0; x < logins.size(); x++) {
            if (!("".equals(logins.get(x)[0]))) { //fixes error with blank lines
                loginInfo.put((logins.get(x))[0], new String[]{logins.get(x)[1], logins.get(x)[2], logins.get(x)[3]});
            }
        }

        br.close();
    }

    protected HashMap getLoginInfo() {
        return loginInfo;
    }

    public void setLoginInfo(HashMap<String,String[]> newLoginInfo) {
        loginInfo = newLoginInfo;
    }

    /**
     * adds a login, saves it to loginDetails.txt
     * @param username desired username
     * @param password desired password
     * @param admin whether the user is an admin or user
     */
    protected void addLoginInfo(String username, String password, String admin, String group) {
        //loginInfo = getLoginInfo(); //if errors, check this, just added without testing

        try {
            FileWriter fileWriter = new FileWriter(filePath, true);
            //if current line is null, no \n
            fileWriter.write("\n" +username+","+password+","+admin+","+group);
            fileWriter.close();
            loginInfo.put(username, new String[] {password,admin,group});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * removes the login details for a specific user
     * @param username desired username
     * @throws Exception
     */
    protected void removeLoginInfo (String username) throws Exception {
        loginInfo = getLoginInfo();
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String line;
        tempLogins.clear(); //if not working, check as just added without checking

        //loop through file, remove from loginInfo if exists, add all other lines to tempLogins
        while (((line = br.readLine()) != null)) {
            String[] values = line.split(",");
            if (username.equals(values[0])) {
                loginInfo.remove(username);
            } else{
                tempLogins.add(values);
            }
        }
        br.close();

        //create temp file
        try {
            File newLoginDetails = new File(newFilePath);
            if (newLoginDetails.createNewFile()) {
                System.out.println("File created");
            }
            else {
                System.out.println("File not created");
            }
        } catch (IOException e) {
            System.out.println("An error occurred");
            e.printStackTrace();
        }

        //copy to temp file
        try {
            FileWriter fileWriter = new FileWriter(newFilePath, true);
            for (int z = 0; z < tempLogins.size(); z++) {
                if (!("".equals(tempLogins.get(z)[0]))) { //fixes error with blank lines
                    if (z == (tempLogins.size()-1)) {
                        fileWriter.write(tempLogins.get(z)[0] + "," + tempLogins.get(z)[1] + "," + tempLogins.get(z)[2] + "," + tempLogins.get(z)[3]); //don't add empty line if last one
                    }
                    else {
                        fileWriter.write(tempLogins.get(z)[0] + "," + tempLogins.get(z)[1] + "," + tempLogins.get(z)[2] + "," + tempLogins.get(z)[3] + "\n");
                    }
                    //fileWriter.write(tempLogins.get(z)[0] + "," + tempLogins.get(z)[1] + "," + tempLogins.get(z)[2] + "," + tempLogins.get(z)[3] + "\n");
                    loginInfo.remove(username);
                }
            }
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //delete original, rename temp file
        File originalFile = new File(filePath);
        originalFile.delete();
        File newFile = new File(newFilePath);
        newFile.renameTo(originalFile);
    }
}