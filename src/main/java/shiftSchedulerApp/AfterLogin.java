package shiftSchedulerApp;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

public class AfterLogin implements ActionListener {

    JFrame frame;
    JLabel label;
    JLabel usernameLabel;
    JLabel adminLabel;
    JLabel groupLabel;
    JLabel userImageLabel;
    JLabel employeesLabel;
    JLabel requestsMessageLabel;
    JTextArea employeesTextArea;
    JTable scheduleTable;
    JScrollPane scrollPane;
    JPanel panel;
    JPanel panel2;
    JPanel schedulePanel;
    JPanel testPanel;
    JPanel profilePanel;
    String columns[];
    String[][] data;
    String[][] dataCopy;
    JButton logoutButton = new JButton("Logout");
    JButton adminButton = new JButton("Admin");
    JButton scheduleButton = new JButton("Schedule");
    JButton testButton = new JButton("Test");
    JButton profileButton = new JButton("Profile");
    JButton removeRequestButton = new JButton("Request removal");
    JButton exitButton = new JButton("Exit");
    String customLocationsFilePath = "C:\\Users\\liams\\Downloads\\shift-scheduler-project\\shift-scheduler-project\\src\\main\\java\\shiftSchedulerApp\\customLocations.txt";
    String userImageFilePath = "C:\\Users\\liams\\Downloads\\shift-scheduler-project\\shift-scheduler-project\\userPfp.png";
    String defaultLocationsFilePath = "C:\\Users\\liams\\Downloads\\shift-scheduler-project\\shift-scheduler-project\\src\\main\\java\\shiftSchedulerApp\\defaultLocations.txt";
    String loginDetailsFilePath = "C:\\Users\\liams\\Downloads\\shift-scheduler-project\\shift-scheduler-project\\src\\main\\java\\shiftSchedulerApp\\loginDetails.txt";
    String requestsFilePath = "C:\\Users\\liams\\Downloads\\shift-scheduler-project\\shift-scheduler-project\\src\\main\\java\\shiftSchedulerApp\\removeRequests.txt";
    int custom;
    String username;
    String shiftTime = "";
    ArrayList<String> defaultLocations = new ArrayList<>();
    String[] daysArray = {"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};
    CardLayout cl = new CardLayout();
    JPanel panelCont = new JPanel();
    Image image = Toolkit.getDefaultToolkit().getImage(userImageFilePath);
    //ArrayList<Shift> shifts;


    public AfterLogin(String username, String[] args) throws Exception {
        this.username = username;
        SchedulingProgram schedulingProgram = new SchedulingProgram();
        SchedulingProgram.main(args);

        ShiftDetails shiftDetails = new ShiftDetails();
        ArrayList<Shift> shifts = shiftDetails.getShifts();
        EmployeeScheduler scheduler = SchedulingProgram.getScheduler();
        int[][][] shiftArray = SchedulingProgram.getShiftArray();
        int numDays = scheduler.getNumDays();

        frame = new JFrame("Schedule");
        label = new JLabel();
        usernameLabel = new JLabel();
        adminLabel = new JLabel();
        groupLabel = new JLabel();
        userImageLabel = new JLabel();
        employeesLabel = new JLabel();
        requestsMessageLabel = new JLabel();
        employeesTextArea = new JTextArea();
        panel = new JPanel();
        schedulePanel = new JPanel();
        testPanel = new JPanel();
        profilePanel = new JPanel();
        data = new String[scheduler.getNumEmployees()][scheduler.getNumDays()+1];
        dataCopy = new String[scheduler.getNumEmployees()][scheduler.getNumDays()+1];
        columns = new String[] {"Group","Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

        for(int p = 0; p < scheduler.getNumEmployees(); p++) { //add employee names
            data[p][0] = "Group " + (p+1);
            data[p][0] = "Group " + (p+1);
        }

        //adding crosses to the table
        for (int i = 1;i < scheduler.getNumDays()+1; i++) { //i = days
            for (int x = 0; x < scheduler.getNumEmployees(); x++) {
                for (int y = 0; y < scheduler.getNumShifts(); y++) {
                    if (shiftArray[x][i - 1][y] == 1) {
                        data[x][i] = "X";
                        dataCopy[x][i] = String.valueOf(y);
                    }
                }
            }
        }

        //format table add it to panel
        scheduleTable = new JTable(data,columns);
        scheduleTable.setDefaultEditor(Object.class,null); // stops cells being editable
        scheduleTable.setBounds(0,0,600,500);
        scheduleTable.setFont(new Font("Serif", Font.BOLD, 19));
        scheduleTable.setRowHeight(30);
        scrollPane = new JScrollPane(scheduleTable);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800,500);
        frame.setLayout(new BorderLayout());
        frame.setVisible(true);

        //format panels
        panel.setPreferredSize(new Dimension(100,500));
        panel.setBackground(Color.lightGray);
        testPanel.setPreferredSize(new Dimension(600,500));
        testPanel.setBackground(Color.gray);
        profilePanel.setPreferredSize(new Dimension(600,500));
        profilePanel.setBackground(Color.gray);

        //format buttons
        logoutButton.setPreferredSize(new Dimension(100,40));
        logoutButton.setFocusable(false);
        logoutButton.addActionListener(this);
        adminButton.setPreferredSize(new Dimension(100,40));
        adminButton.setFocusable(false);
        adminButton.addActionListener(this);
        scheduleButton.setPreferredSize(new Dimension(100,40));
        scheduleButton.setFocusable(false);
        scheduleButton.addActionListener(this);
        testButton.setPreferredSize(new Dimension(100,40));
        testButton.setFocusable(false);
        testButton.addActionListener(this);
        profileButton.setPreferredSize(new Dimension(100,40));
        profileButton.setFocusable(false);
        profileButton.addActionListener(this);
        exitButton.setPreferredSize(new Dimension(100,40));
        exitButton.setFocusable(false);
        exitButton.addActionListener(this);
        removeRequestButton.setPreferredSize(new Dimension(100,40));
        removeRequestButton.setFocusable(false);
        removeRequestButton.addActionListener(this);

        //usernameLabel.setBounds(125,150,500,100);
        usernameLabel.setBounds(50,30,500,55);
        usernameLabel.setFont(new Font("Serif",Font.BOLD,23));
        adminLabel.setBounds(50,50,500,100);
        adminLabel.setFont(new Font("Serif",Font.BOLD,20));
        groupLabel.setBounds(50,80,500,100);
        groupLabel.setFont(new Font("Serif",Font.BOLD,20));
        employeesLabel.setBounds(50,220,600,100);
        employeesLabel.setFont(new Font("Serif",Font.BOLD,20));
        employeesTextArea.setBounds(50,220,600,100);
        employeesTextArea.setFont(new Font("Serif",Font.BOLD,20));
        employeesTextArea.setEditable(false);
        employeesTextArea.setOpaque(false);
        employeesTextArea.setCursor(null);
        employeesTextArea.setFocusable(false);
        requestsMessageLabel.setBounds(300,125,500,100);
        requestsMessageLabel.setFont(new Font(null,Font.ITALIC,20));
        //userImageLabel.setBounds(400,150,200,200);

        removeRequestButton.setBounds(50,160,200,35);
        removeRequestButton.setFocusable(false);

        panel.add(logoutButton);
        panel.add(adminButton);
        panel.add(scheduleButton);
        panel.add(profileButton);
        panel.add(exitButton);

        profilePanel.add(usernameLabel);
        profilePanel.add(adminLabel);
        profilePanel.add(groupLabel);
        profilePanel.add(employeesTextArea);
        profilePanel.add(removeRequestButton);
        profilePanel.add(requestsMessageLabel);
        profilePanel.setLayout(null);

        panel2 = new JPanel();
        panel2.setPreferredSize(new Dimension(600,500));
        schedulePanel.setPreferredSize(new Dimension(600,500));
        testPanel.setPreferredSize(new Dimension(600,500));
        scrollPane.setPreferredSize(new Dimension(688,600));
        panel2.add(scrollPane);

        frame.add(panel,BorderLayout.WEST);

        panelCont.setLayout(cl);
        panelCont.add(panel2,"1");
        panelCont.add(profilePanel,"4");
        cl.show(panelCont,"1");
        frame.add(panelCont);

        custom = customLocations();
        defaultLocations = getDefaultLocations();

        //allows user to click on the cells in the table to display info
        scheduleTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    ArrayList<String[]> shiftInfo = shiftDetails.getShiftFile();
                    int row = scheduleTable.rowAtPoint(evt.getPoint());
                    int col = scheduleTable.columnAtPoint(evt.getPoint());

                    for (int i = 0; i < shiftInfo.size(); i++) {
                        String[] shiftSplit = shiftInfo.get(i)[0].split("-");

                        if (Objects.equals(shiftSplit[0], String.valueOf(row + 1))) { //if the clicked cell matches with a group
                            if (Objects.equals(shiftSplit[1], String.valueOf(col))) {
                                if (Objects.equals((String.valueOf(Integer.parseInt(shiftSplit[2])-1)), String.valueOf(dataCopy[row][col]))) { //focus on this,dupe data?
                                    if (Objects.equals(shiftSplit[2], "1")) {
                                        shiftTime = "07:00-15:00";
                                    }
                                    else if (Objects.equals(shiftSplit[2], "2")){
                                        shiftTime = "11:00-20:00";
                                    }
                                    else {
                                        shiftTime = "15:00-00:00";
                                    }
                                    if (custom == 0) {  //if there is no custom file, assign some default locations to the shifts
                                        //System.out.println(i);
                                        JOptionPane.showMessageDialog(frame,defaultLocations.get(i) + "\nTime: " + shiftTime + "\nGroup: " + (row+1) + "\nDay: " + daysArray[col-1]);
                                    }
                                    else {
                                        try { //display custom locations and shift info
                                            ArrayList<String> customLocations = getCustomLocations();
                                            JOptionPane.showMessageDialog(frame,customLocations.get(i) + "\nTime: " + shiftTime + "\nGroup: " + (row+1) + "\nDay: " + daysArray[col-1]);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                }
                            }
                        }
                    }
                }
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == logoutButton) {
            //logout
            System.out.println("LOGOUT");
            frame.dispose();
            LoginDetails loginDetails = null;
            try {
                loginDetails = new LoginDetails();
                LoginDisplay loginDisplay = new LoginDisplay(loginDetails.getLoginInfo());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        else if(e.getSource() == adminButton) {
            //admin page
            System.out.println("ADMIN PAGE");
            //if user isn't admin, alert
            try {
                if (isAdmin(username)) {
                    frame.dispose();
                    LoginDetails loginDetails = null;
                    try {
                        loginDetails = new LoginDetails();
                        AdminScreen adminScreen = new AdminScreen(username);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                else {
                    JOptionPane.showMessageDialog(frame, "This account does not have admin privileges");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        else if(e.getSource() == scheduleButton) {
            //admin page
            System.out.println("TEST PAGE");
            cl.show(panelCont,"1");
        }

        else if(e.getSource() == testButton) {
            //admin page
            System.out.println("TEST PAGE");
            cl.show(panelCont,"3");
        }

        else if(e.getSource() == profileButton) {
            //profile page
            requestsMessageLabel.setText("");
            ArrayList<String> currentLogin = new ArrayList<>();
            BufferedImage userImage = null;
            Image image = null;
            try {
                currentLogin = getCurrentLogin();
                userImage = ImageIO.read(new File(userImageFilePath));
                image = userImage.getScaledInstance(200,200,Image.SCALE_DEFAULT);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            usernameLabel.setText("Hello, " + currentLogin.get(0));
            adminLabel.setText("Account type: " + currentLogin.get(2));
            if (Objects.equals(currentLogin.get(3), "0")) {
                groupLabel.setText("Group: no group");
            }
            else {
                groupLabel.setText("Group: " + currentLogin.get(3));
            }
            try {
                if (Objects.equals(currentLogin.get(2), "admin")) {
                    String admins = getOtherAdmins(username);
                    employeesTextArea.setText("Other admins: \n" + admins);
                }
                else {
                    if (Objects.equals(currentLogin.get(3), "0")) {
                    }
                    else {
                        String employees = getGroupEmployees(username, currentLogin.get(3));
                        employeesLabel.setText("Employees in your group: " + employees);
                        employeesTextArea.setText("Employees in your group: \n" + employees);
                    }
                }

            } catch (IOException ex) {
                ex.printStackTrace();
            }
            cl.show(panelCont,"4");

        }

        else if(e.getSource() == exitButton) {
            frame.dispose();
        }

        else if(e.getSource() == removeRequestButton) {
            //admin page
            //write to file with the username and whatever
            //add to removeRequests their username
            File requestsFile = new File(requestsFilePath);
            try {
                FileWriter fileWriter = new FileWriter(requestsFilePath, true);
                //if current line is null, no \n
                if (requestsFile.length() == 0) {
                    fileWriter.write(username);
                }
                else {
                    fileWriter.write("\n" +username);
                }
                fileWriter.close();
            } catch (IOException error) {
                error.printStackTrace();
            }

            requestsMessageLabel.setText("Request made successfully");
        }

    }

    /**
     * used for determining whether there is a file containing custom locations
     * @return true or false
     */
    public int customLocations() {  //if a file detailing which shifts are where exists, return 1
        File file = new File(customLocationsFilePath);
        if (file.isFile()) {
            return 1;
        }
        else {
            return 0;
        }
    }

    /**
     * gets the default locations for use when there are no custom locations set
     * @return an arraylist of the default locations
     * @throws IOException .
     */
    public ArrayList<String> getDefaultLocations() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(defaultLocationsFilePath));
        String line;
        ArrayList<String> defaultShifts = new ArrayList<>();

        while (((line = br.readLine()) != null)) {
            String[] values = line.split(",");
            if (!Objects.equals(values[0], "Office")) {
                defaultShifts.add(values[0]);
            }
        }
        br.close();
        return defaultShifts;
    }

    /**
     * gets the custom locations for the shifts
     * @return an arraylist of the custom locations
     * @throws IOException .
     */
    public ArrayList<String> getCustomLocations() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(customLocationsFilePath));
        String line;
        ArrayList<String> customShifts = new ArrayList<>();

        while (((line = br.readLine()) != null)) {
            String[] values = line.split(",");
            if (!Objects.equals(values[0], "Office")) {
                customShifts.add(values[0]);
            }
        }
        br.close();
        return customShifts;
    }

    /**
     * gets the login info for the current user
     * @return login info
     * @throws IOException .
     */
    public ArrayList<String> getCurrentLogin() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(loginDetailsFilePath));
        String line;
        ArrayList<String> currentLogin = new ArrayList<>();

        while (((line = br.readLine()) != null)) {
            String[] values = line.split(",");
            if (Objects.equals(values[0], username)) {
                currentLogin.add(values[0]);
                currentLogin.add(values[1]);
                currentLogin.add(values[2]);
                currentLogin.add(values[3]);
            }
        }
        br.close();
        return currentLogin;
    }

    /**
     * for determining whether the user is an admin
     * @param username the username
     * @return boolean saying whether user is admin
     * @throws IOException .
     */
    public boolean isAdmin(String username) throws IOException {
        boolean admin = false;
        BufferedReader br = new BufferedReader(new FileReader(loginDetailsFilePath));
        String line;

        while (((line = br.readLine()) != null)) {
            String[] values = line.split(",");
            if (Objects.equals(values[0], username)) {
                if (Objects.equals(values[2], "admin")) {
                    admin = true;
                }
            }
        }
        br.close();
        return admin;
    }

    /**
     * returns the other employees in the user's group
     * @param username the current login
     * @param group the user's group
     * @return a string containing the other employees
     * @throws IOException .
     */
    public String getGroupEmployees(String username, String group) throws IOException {
        ArrayList<String> employees = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(loginDetailsFilePath));
        String line;
        String emps = "";

        while (((line = br.readLine()) != null)) {
            String[] values = line.split(",");
            if (Objects.equals(values[3], group) && (!Objects.equals(values[0],username))) {
                employees.add(values[0]);
            }
        }
        br.close();

        for (int i = 0; i < employees.size(); i++) {
            if (i == employees.size() - 1) {
                emps += employees.get(i);
            }
            else if ((i % 4 == 0) && (i != 0)){
                emps += employees.get(i) + ",\n";
            }
            else {
                emps += employees.get(i) + ", ";
            }
        }
        return emps;
    }

    /**
     * returns a string containing the other admins
     * @param username the current login
     * @return other admins
     * @throws IOException .
     */
    public String getOtherAdmins (String username) throws IOException {
        ArrayList<String> admins = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(loginDetailsFilePath));
        String line;
        String admins1 = "";

        while (((line = br.readLine()) != null)) {
            String[] values = line.split(",");
            if (Objects.equals(values[2], "admin") && (!Objects.equals(values[0], username))) {
                admins.add(values[0]);
            }
        }

        br.close();

        for (int i = 0; i < admins.size(); i++) {
            if (i == admins.size() - 1) {
                admins1 += admins.get(i);
            }
            else if ((i % 4 == 0) && (i != 0)){
                admins1 += admins.get(i) + ",\n";
            }
            else {
                admins1 += admins.get(i) + ", ";
            }
        }
        return admins1;
    }
}