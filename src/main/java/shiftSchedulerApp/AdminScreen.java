package shiftSchedulerApp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

public class AdminScreen implements ActionListener {

    JFrame frame;
    JLabel label;
    JPanel panel;
    JPanel addUserPanel;
    JPanel removeUserPanel;
    JPanel homeScreenPanel;
    JPanel addLocationsPanel;
    JPanel shiftDetailsPanel;
    JPanel changeConfigPanel;
    JPanel requestsPanel;
    JScrollPane scrollPane;

    JButton logoutButton = new JButton("Logout");
    JButton scheduleButton = new JButton("Schedule");
    JButton addUserButton = new JButton("Add Users");
    JButton removeUserButton = new JButton("<html>Remove<br />&nbsp;&nbsp;Users</html>");
    JButton shiftDetailsButton = new JButton("<html>&nbsp;&nbsp;&nbsp;Assign<br />Locations</html>");
    JButton changeConfigButton = new JButton("<html>Change<br />&nbsp;Config</html>");
    JButton addLocationsButton = new JButton("<html>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Add<br />Locations</html>");
    JButton exitButton = new JButton("Exit");
    JButton confirmAddUserButton = new JButton("Confirm");
    JButton confirmRemoveUserButton = new JButton("Confirm");
    JButton confirmShiftDetailsButton = new JButton("Save");
    JButton confirmChangeConfigButton = new JButton("Confirm Changes");
    JButton displayShiftDetailsButton = new JButton("Display Details");
    JButton resetShiftDetailsButton = new JButton("Reset All");
    JButton requestsButton = new JButton("Requests");
    JButton confirmRequestsButton = new JButton("Confirm Request");
    JButton denyRequestsButton = new JButton("Deny Request");
    JButton confirmAddLocationButton = new JButton("Add Location");
    JButton shiftDetailsHelpButton = new JButton("Help");

    JCheckBox adminBox = new JCheckBox("Admin");
    JCheckBox officeBox = new JCheckBox("Is Main Office?");
    JComboBox shiftsComboBox = new JComboBox();
    JComboBox locationsComboBox = new JComboBox();
    JComboBox groupsComboBox = new JComboBox();
    JComboBox timesComboBox = new JComboBox();
    JComboBox userGroupComboBox = new JComboBox();
    JComboBox requestsComboBox = new JComboBox();
    JTable shiftsDetailsTable;

    JTextField usernameField = new JTextField();
    JPasswordField passwordField = new JPasswordField();
    JTextField removeUsernameField = new JTextField();
    JTextField shiftDetailsField = new JTextField();
    JTextField locationNameField = new JTextField();
    JTextField latitudeField = new JTextField();
    JTextField longitudeField = new JTextField();

    JLabel addUsersLabel = new JLabel("Add users");
    JLabel removeUsersLabel = new JLabel("Remove users");
    JLabel addLocationsLabel = new JLabel("Add available locations");
    JLabel addShiftDetailsLabel = new JLabel("Assign locations to the shifts");
    JLabel requestsLabel = new JLabel("Approve or deny time off requests");
    JLabel changeConfigLabel = new JLabel("Change the number of groups and shifts per day");

    JLabel homeScreenTitle = new JLabel("Welcome, ");
    JLabel homeScreenInfo = new JLabel("Select one of the sidebar tools to configure your application.");
    JLabel addUsernameLabel = new JLabel("Username:");
    JLabel addPasswordLabel = new JLabel("Password:");
    JLabel addUserMessageLabel = new JLabel();
    JLabel removeUsernameLabel = new JLabel("Username:");
    JLabel removeUserMessageLabel = new JLabel();
    JLabel addLocationMessageLabel = new JLabel();
    JLabel locationNameLabel = new JLabel("Location Name:");
    JLabel latitudeLabel = new JLabel("Latitude:");
    JLabel longitudeLabel = new JLabel("Longitude:");
    JLabel shiftsLabel = new JLabel("Shifts:");
    JLabel locationsLabel = new JLabel("Locations:");
    JLabel groupLabel = new JLabel("Groups:");
    JLabel shiftsNumLabel = new JLabel("Shifts:");
    JLabel employeeRequestsLabel = new JLabel("Employees:");
    JLabel changeConfigGroupsMessageLabel = new JLabel();
    JLabel changeConfigShiftsMessageLabel = new JLabel();
    JLabel shiftDetailsMessageLabel = new JLabel();
    JLabel requestsMessageLabel = new JLabel();

    LoginDetails loginDetails;
    ArrayList<String> details = new ArrayList<>();
    CardLayout cl = new CardLayout();
    JPanel panelCont = new JPanel();
    String[] columns;
    String[][] data;
    String username;
    HashMap<String,String[]> locationsValues;
    String[] args;

    String customLocationsFilePath = "C:\\Users\\liams\\Downloads\\shift-scheduler-project\\shift-scheduler-project\\src\\main\\java\\shiftSchedulerApp\\customLocations.txt";
    String locationCoordinatesFilePath = "C:\\Users\\liams\\Downloads\\shift-scheduler-project\\shift-scheduler-project\\src\\main\\java\\shiftSchedulerApp\\locationCoordinates.txt";
    String locationDetailsFilePath = "C:\\Users\\liams\\Downloads\\shift-scheduler-project\\shift-scheduler-project\\src\\main\\java\\shiftSchedulerApp\\locationDetails.txt";
    String configFilePath = "C:\\Users\\liams\\Downloads\\shift-scheduler-project\\shift-scheduler-project\\src\\main\\java\\shiftSchedulerApp\\shiftsConfig.txt";
    String requestsFilePath = "C:\\Users\\liams\\Downloads\\shift-scheduler-project\\shift-scheduler-project\\src\\main\\java\\shiftSchedulerApp\\removeRequests.txt";
    String newRequestsFilePath = "C:\\Users\\liams\\Downloads\\shift-scheduler-project\\shift-scheduler-project\\src\\main\\java\\shiftSchedulerApp\\newRemoveRequests.txt";
    String loginDetailsFilePath = "C:\\Users\\liams\\Downloads\\shift-scheduler-project\\shift-scheduler-project\\src\\main\\java\\shiftSchedulerApp\\loginDetails.txt";
    String officeFilePath = "C:\\Users\\liams\\Downloads\\shift-scheduler-project\\shift-scheduler-project\\src\\main\\java\\shiftSchedulerApp\\officeDetails.txt";

    public AdminScreen(String username) throws Exception {
        this.username = username;
        EmployeeScheduler scheduler = SchedulingProgram.getScheduler();
        ShiftDetails shiftDetails = new ShiftDetails();
        loginDetails = new LoginDetails();
        HashMap<String, String[]> loginInfo = loginDetails.getLoginInfo();
        ArrayList<String[]> shiftInfo = shiftDetails.getShiftFile();
        columns = new String[] {"Shift","Details"};
        data = new String[shiftInfo.size()][2];
        String x = "";
        homeScreenTitle.setText("Welcome, " + username + ".");
        args = new String[]{};

        for (int i = 0; i < shiftInfo.size(); i++) {
            x = Arrays.toString(shiftInfo.get(i));
            x = x.replace("[","");
            x = x.replace("]","");
            shiftsComboBox.addItem(x);
            details.add("Empty");
            data[i][0] = Arrays.toString(shiftInfo.get(i));
            data[i][1] = "Empty";
        }

        for (int i = 1; i < 11; i++) {
            groupsComboBox.addItem(i);
            userGroupComboBox.addItem(i);
            if (i < 6) {
                timesComboBox.addItem(i);
            }
        }

        locationsValues = getLocationsComboBoxValues();
        ArrayList<String> list = new ArrayList<String> (locationsValues.keySet());
        for (int i = 0; i < list.size(); i++) {
            locationsComboBox.addItem(list.get(i));
        }

        //String[] details = new String[shiftInfo.size()];

        frame = new JFrame("Admin Tools");
        label = new JLabel();
        panel = new JPanel();
        addUserPanel = new JPanel();
        removeUserPanel = new JPanel();
        homeScreenPanel = new JPanel();
        shiftDetailsPanel = new JPanel();
        addLocationsPanel = new JPanel();
        changeConfigPanel = new JPanel();
        requestsPanel = new JPanel();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800,500);
        frame.setLayout(new BorderLayout());
        frame.setVisible(true);

        //setting panel sizes/colours
        panel.setPreferredSize(new Dimension(100,500));
        panel.setBackground(java.awt.Color.lightGray);
        addUserPanel.setPreferredSize(new Dimension(700, 500));
        addUserPanel.setBackground(java.awt.Color.gray);
        removeUserPanel.setPreferredSize(new Dimension(700, 500));
        removeUserPanel.setBackground(java.awt.Color.gray);
        homeScreenPanel.setPreferredSize(new Dimension(700, 500));
        homeScreenPanel.setBackground(java.awt.Color.gray);
        shiftDetailsPanel.setPreferredSize(new Dimension(700, 500));
        shiftDetailsPanel.setBackground(java.awt.Color.gray);
        changeConfigPanel.setPreferredSize(new Dimension(700, 500));
        changeConfigPanel.setBackground(java.awt.Color.gray);
        requestsPanel.setPreferredSize(new Dimension(700, 500));
        requestsPanel.setBackground(java.awt.Color.gray);
        addLocationsPanel.setPreferredSize(new Dimension(700, 500));
        addLocationsPanel.setBackground(java.awt.Color.gray);


        //sidebar buttons
        logoutButton.setPreferredSize(new Dimension(100,40));
        logoutButton.setFocusable(false);
        logoutButton.addActionListener(this);
        scheduleButton.setPreferredSize(new Dimension(100,40));
        scheduleButton.setFocusable(false);
        scheduleButton.addActionListener(this);
        addUserButton.setPreferredSize(new Dimension(100,40));
        addUserButton.setFocusable(false);
        addUserButton.addActionListener(this);
        removeUserButton.setPreferredSize(new Dimension(100,40));
        removeUserButton.setFocusable(false);
        removeUserButton.addActionListener(this);
        removeUserButton.setHorizontalAlignment(SwingConstants.CENTER);
        addLocationsButton.setPreferredSize(new Dimension(100,40));
        addLocationsButton.setFocusable(false);
        addLocationsButton.addActionListener(this);
        addLocationsButton.setHorizontalAlignment(SwingConstants.CENTER);
        shiftDetailsButton.setPreferredSize(new Dimension(100,40));
        shiftDetailsButton.setFocusable(false);
        shiftDetailsButton.addActionListener(this);
        shiftDetailsButton.setHorizontalAlignment(SwingConstants.CENTER);
        //shiftDetailsButton.setFont(new Font("Serif",Font.BOLD,10));
        changeConfigButton.setPreferredSize(new Dimension(100,40));
        changeConfigButton.setFocusable(false);
        changeConfigButton.addActionListener(this);
        changeConfigButton.setHorizontalAlignment(SwingConstants.CENTER);

        requestsButton.setPreferredSize(new Dimension(100,40));
        requestsButton.setFocusable(false);
        requestsButton.addActionListener(this);
        exitButton.setPreferredSize(new Dimension(100,40));
        exitButton.setFocusable(false);
        exitButton.addActionListener(this);

        //main screen buttons
        //  home screen
        homeScreenTitle.setBounds(50,100,100,55);
        homeScreenTitle.setFont(new Font("Serif",Font.BOLD,20));
        homeScreenInfo.setBounds(200,50,200,55);
        homeScreenInfo.setFont(new Font("Serif",Font.BOLD,20));

        //  add users
        addUsersLabel.setBounds(300,30,300,35);
        addUsersLabel.setFont(new Font("Serif",Font.BOLD,23));
        confirmAddUserButton.setBounds(265,200,85,35);
        confirmAddUserButton.setFocusable(false);
        confirmAddUserButton.addActionListener(this);
        adminBox.setOpaque(false);
        adminBox.setBounds(375,95,150,50);
        adminBox.setFont(new Font(null,Font.BOLD,12));
        addUsernameLabel.setBounds(50,100,125,35);
        addUsernameLabel.setFont(new Font(null,Font.BOLD,16));
        addPasswordLabel.setBounds(50,150,125,35);
        addPasswordLabel.setFont(new Font(null,Font.BOLD,16));
        addUserMessageLabel.setBounds(150,250,500,100);
        addUserMessageLabel.setFont(new Font(null,Font.ITALIC,20));
        usernameField.setBounds(150,100,200,35);
        passwordField.setBounds(150,150,200,35);
        userGroupComboBox.setBounds(150,200,75,35);
        userGroupComboBox.setFocusable(false);
        userGroupComboBox.addActionListener(this);

        //  remove users
        removeUsersLabel.setBounds(300,30,300,35);
        removeUsersLabel.setFont(new Font("Serif",Font.BOLD,23));
        confirmRemoveUserButton.setBounds(265,150,85,35);
        confirmRemoveUserButton.setFocusable(false);
        confirmRemoveUserButton.addActionListener(this);
        removeUsernameLabel.setBounds(50,100,125,35);
        removeUsernameLabel.setFont(new Font(null,Font.BOLD,16));
        removeUserMessageLabel.setBounds(150,200,500,100);
        removeUserMessageLabel.setFont(new Font(null,Font.ITALIC,20));
        removeUsernameField.setBounds(150,100,200,35);

        //  add locations
        addLocationsLabel.setBounds(250,30,300,35);
        addLocationsLabel.setFont(new Font("Serif",Font.BOLD,23));
        confirmAddLocationButton.setBounds(225,250,125,35);
        confirmAddLocationButton.setFocusable(false);
        confirmAddLocationButton.addActionListener(this);
        officeBox.setOpaque(false);
        officeBox.setBounds(375,95,150,50);
        officeBox.setFont(new Font(null,Font.BOLD,12));

        locationNameField.setBounds(150,100,200,35);
        latitudeField.setBounds(150,150,200,35);
        longitudeField.setBounds(150,200,200,35);

        locationNameLabel.setBounds(25,100,125,35);
        latitudeLabel.setBounds(50,150,75,35);
        longitudeLabel.setBounds(50,200,100,35);
        locationNameLabel.setFont(new Font(null,Font.BOLD,16));
        latitudeLabel.setFont(new Font(null,Font.BOLD,16));
        longitudeLabel.setFont(new Font(null,Font.BOLD,16));

        addLocationMessageLabel.setBounds(150,300,500,100);
        addLocationMessageLabel.setFont(new Font(null,Font.ITALIC,20));

        // add shift details
        addShiftDetailsLabel.setBounds(230,30,300,35);
        addShiftDetailsLabel.setFont(new Font("Serif",Font.BOLD,23));
        shiftsComboBox.setBounds(150,100,100,35);
        shiftsComboBox.setFocusable(false);
        shiftsComboBox.addActionListener(this);
        locationsComboBox.setBounds(150,150,150,35);
        locationsComboBox.setFocusable(false);
        locationsComboBox.addActionListener(this);
        confirmShiftDetailsButton.setBounds(150,200,100,35);
        confirmShiftDetailsButton.setFocusable(false);
        confirmShiftDetailsButton.addActionListener(this);
        resetShiftDetailsButton.setBounds(275,200,100,35);
        resetShiftDetailsButton.setFocusable(false);
        resetShiftDetailsButton.addActionListener(this);
        displayShiftDetailsButton.setBounds(225,400,100,35);
        displayShiftDetailsButton.setFocusable(false);
        displayShiftDetailsButton.addActionListener(this);
        shiftDetailsHelpButton.setBounds(400,200,100,35);
        shiftDetailsHelpButton.setFocusable(false);
        shiftDetailsHelpButton.addActionListener(this);
        shiftsLabel.setBounds(70,100,125,35);
        shiftsLabel.setFont(new Font(null,Font.BOLD,16));
        locationsLabel.setBounds(50,150,125,35);
        locationsLabel.setFont(new Font(null,Font.BOLD,16));
        shiftDetailsMessageLabel.setBounds(150,250,500,100);
        shiftDetailsMessageLabel.setFont(new Font(null,Font.ITALIC,20));

        /*shiftDetailsField.setBounds(10,10,200,35);
        shiftsDetailsTable = new JTable(data,columns);
        shiftsDetailsTable.setDefaultEditor(Object.class,null); // stops cells being editable
        shiftsDetailsTable.setBounds(400,100,250,300);
        shiftsDetailsTable.setFont(new Font("Serif", Font.BOLD, 19));
        shiftsDetailsTable.setRowHeight(25);
        scrollPane = new JScrollPane(shiftsDetailsTable);*/
        //scrollPane.setVisible(true);

        //  config change
        changeConfigLabel.setBounds(120,30,550,35);
        changeConfigLabel.setFont(new Font("Serif",Font.BOLD,23));
        groupsComboBox.setBounds(150,100,100,35);
        groupsComboBox.setFocusable(false);
        groupsComboBox.addActionListener(this);
        timesComboBox.setBounds(150,150,100,35);
        timesComboBox.setFocusable(false);
        timesComboBox.addActionListener(this);
        confirmChangeConfigButton.setBounds(150,200,150,35);
        confirmChangeConfigButton.setFocusable(false);
        confirmChangeConfigButton.addActionListener(this);
        groupLabel.setBounds(60,100,125,35);
        groupLabel.setFont(new Font(null,Font.BOLD,16));
        shiftsNumLabel.setBounds(60,150,125,35);
        shiftsNumLabel.setFont(new Font(null,Font.BOLD,16));
        changeConfigGroupsMessageLabel.setBounds(150,250,500,100);
        changeConfigGroupsMessageLabel.setFont(new Font(null,Font.ITALIC,20));
        changeConfigShiftsMessageLabel.setBounds(150,300,500,100);
        changeConfigShiftsMessageLabel.setFont(new Font(null,Font.ITALIC,20));

        //  requests
        requestsLabel.setBounds(190,30,400,35);
        requestsLabel.setFont(new Font("Serif",Font.BOLD,23));
        employeeRequestsLabel.setBounds(50,100,125,35);
        employeeRequestsLabel.setFont(new Font(null,Font.BOLD,16));
        confirmRequestsButton.setBounds(150,150,150,35);
        confirmRequestsButton.setFocusable(false);
        confirmRequestsButton.addActionListener(this);
        denyRequestsButton.setBounds(150,200,150,35);
        denyRequestsButton.setFocusable(false);
        denyRequestsButton.addActionListener(this);
        requestsComboBox.setBounds(150,100,100,35);
        requestsComboBox.setFocusable(false);
        requestsComboBox.addActionListener(this);
        requestsMessageLabel.setBounds(150,300,500,100);
        requestsMessageLabel.setFont(new Font(null,Font.ITALIC,20));

        //adding components to panels
        panel.add(logoutButton);
        panel.add(scheduleButton);
        panel.add(addUserButton);
        panel.add(removeUserButton);
        panel.add(addLocationsButton);
        panel.add(shiftDetailsButton);
        panel.add(changeConfigButton);
        panel.add(requestsButton);
        panel.add(exitButton);

        //scheduleButton.setFont(new Font("Serif",Font.BOLD,15));
        //addLocationsButton.setFont(new Font("Serif",Font.BOLD,15));

        homeScreenPanel.add(homeScreenTitle);
        homeScreenPanel.add(homeScreenInfo);
        //homeScreenPanel.add(changeWeeksButton);

        addUserPanel.add(addUsersLabel);
        addUserPanel.add(adminBox);
        addUserPanel.add(confirmAddUserButton);
        addUserPanel.add(addUsernameLabel);
        addUserPanel.add(addPasswordLabel);
        addUserPanel.add(usernameField);
        addUserPanel.add(passwordField);
        addUserPanel.add(addUserMessageLabel);
        addUserPanel.add(userGroupComboBox);
        addUserPanel.setLayout(null);

        removeUserPanel.add(removeUsersLabel);
        removeUserPanel.add(confirmRemoveUserButton);
        removeUserPanel.add(removeUsernameLabel);
        removeUserPanel.add(removeUsernameField);
        removeUserPanel.add(removeUserMessageLabel);
        removeUserPanel.setLayout(null);

        addLocationsPanel.add(addLocationsLabel);
        addLocationsPanel.add(confirmAddLocationButton);
        addLocationsPanel.add(latitudeField);
        addLocationsPanel.add(longitudeField);
        addLocationsPanel.add(locationNameField);
        addLocationsPanel.add(locationNameLabel);
        addLocationsPanel.add(latitudeLabel);
        addLocationsPanel.add(longitudeLabel);
        addLocationsPanel.add(addLocationMessageLabel);
        addLocationsPanel.add(officeBox);
        addLocationsPanel.setLayout(null);

        shiftDetailsPanel.add(addShiftDetailsLabel);
        //shiftDetailsPanel.setLayout(new BorderLayout());
        shiftDetailsPanel.add(shiftsComboBox);
        shiftDetailsPanel.add(locationsComboBox);
        shiftDetailsPanel.add(confirmShiftDetailsButton);
        shiftDetailsPanel.add(resetShiftDetailsButton);
        //shiftDetailsPanel.add(shiftDetailsField);
        shiftDetailsPanel.add(shiftsLabel);
        shiftDetailsPanel.add(locationsLabel);
        shiftDetailsPanel.add(shiftDetailsMessageLabel);
        shiftDetailsPanel.add(shiftDetailsHelpButton);
        //shiftDetailsPanel.add(shiftsDetailsTable, BorderLayout.EAST);
        //shiftDetailsPanel.add(displayShiftDetailsButton, BorderLayout.EAST);
        //shiftDetailsPanel.add(scrollPane);
        shiftDetailsPanel.setLayout(null);

        changeConfigPanel.add(changeConfigLabel);
        changeConfigPanel.add(groupsComboBox);
        changeConfigPanel.add(timesComboBox);
        changeConfigPanel.add(confirmChangeConfigButton);
        changeConfigPanel.add(groupLabel);
        changeConfigPanel.add(shiftsNumLabel);
        changeConfigPanel.add(changeConfigGroupsMessageLabel);
        changeConfigPanel.add(changeConfigShiftsMessageLabel);
        changeConfigPanel.setLayout(null);

        requestsPanel.add(requestsLabel);
        requestsPanel.add(confirmRequestsButton);
        requestsPanel.add(employeeRequestsLabel);
        requestsPanel.add(denyRequestsButton);
        requestsPanel.add(requestsLabel);
        requestsPanel.add(requestsComboBox);
        requestsPanel.add(requestsMessageLabel);
        requestsPanel.setLayout(null);

        //adding panels to the frame
        frame.add(panel,BorderLayout.WEST);
        panelCont.setLayout(cl);
        panelCont.add(homeScreenPanel,"1");
        panelCont.add(addUserPanel,"2");
        panelCont.add(removeUserPanel,"3");
        panelCont.add(shiftDetailsPanel,"4");
        panelCont.add(changeConfigPanel,"5");
        panelCont.add(requestsPanel,"6");
        panelCont.add(addLocationsPanel,"7");
        cl.show(panelCont,"1");
        frame.add(panelCont);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == logoutButton) {
            //logout
            frame.dispose();
            LoginDetails loginDetails = null;
            try {
                loginDetails = new LoginDetails();
                LoginDisplay loginDisplay = new LoginDisplay(loginDetails.getLoginInfo());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        else if(e.getSource() == scheduleButton) {
            //schedule page
            frame.dispose();
            try {
                AfterLogin afterLogin = new AfterLogin(username,args);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        else if(e.getSource() == addUserButton) {
            addUserMessageLabel.setText("");
            cl.show(panelCont,"2");
        }

        else if(e.getSource() == confirmAddUserButton) {
            //add employees
            String username = usernameField.getText();
            String password = String.valueOf(passwordField.getPassword());
            String group = String.valueOf(userGroupComboBox.getSelectedIndex() + 1);
            if (username.equals("")) {
                addUserMessageLabel.setText("Enter a username");
            }
            else if (password.equals("")) {
                addUserMessageLabel.setText("Enter a password");
            }
            else {
                if (adminBox.isSelected()) {
                    loginDetails.addLoginInfo(username, password, "admin","0");
                } else {
                    loginDetails.addLoginInfo(username, password, "user",group);
                }
                addUserMessageLabel.setText("User '" + username + "' successfully added");
                usernameField.setText("");
                passwordField.setText("");
            }
        }

        else if(e.getSource() == removeUserButton) {
            //remove users
            removeUserMessageLabel.setText("");
            cl.show(panelCont,"3");
        }

        else if(e.getSource() == confirmRemoveUserButton) {
            String username = removeUsernameField.getText();
            if (username.equals("")) {
                removeUserMessageLabel.setText("Enter a username");
            }
            else {
                try {
                    loginDetails.removeLoginInfo(username);
                    removeUserMessageLabel.setText("User '" + username + "' successfully deleted");
                    removeUsernameField.setText("");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }


        else if(e.getSource() == addLocationsButton) {
            addLocationMessageLabel.setText("");
            longitudeField.setText("");
            latitudeField.setText("");
            locationNameField.setText("");
            cl.show(panelCont,"7");
        }

        else if(e.getSource() == confirmAddLocationButton){
            //write to locationCoordinates
            String name = locationNameField.getText();
            String latitude = latitudeField.getText();
            String longitude = longitudeField.getText();
            if (name.equals("")) {
                addLocationMessageLabel.setText("Enter a location name");
            }
            else if (latitude.equals("")) {
                addLocationMessageLabel.setText("Enter a latitude");
            }
            else if (longitude.equals("")) {
                addLocationMessageLabel.setText("Enter a longitude");
            }
            else {
                if (!officeBox.isSelected()) {
                    try {
                        File file = new File(locationCoordinatesFilePath);
                        FileWriter fileWriter = new FileWriter(locationCoordinatesFilePath, StandardCharsets.UTF_8, true);
                        if (file.length() == 0) {
                            fileWriter.write(name + "," + longitude + "," + latitude);
                        } else {
                            fileWriter.write("\n" + name + "," + longitude + "," + latitude);
                        }
                        fileWriter.close();
                    } catch (IOException error) {
                        System.out.println("An error occurred.");
                        error.printStackTrace();
                    }
                }
                else {
                    //write office
                    try {
                        File file = new File(officeFilePath);
                        FileWriter fileWriter = new FileWriter(officeFilePath, StandardCharsets.UTF_8, false);
                        fileWriter.write(name + "," + longitude + "," + latitude);
                        fileWriter.close();
                    } catch (IOException error) {
                        System.out.println("An error occurred.");
                        error.printStackTrace();
                    }
                }

                addLocationMessageLabel.setText("Location '" + name + "' successfully added");
            }
        }

        else if(e.getSource() == shiftDetailsButton) {
            //add shift details
            shiftDetailsMessageLabel.setText("");
            cl.show(panelCont,"4");
        }

        else if (e.getSource() == confirmShiftDetailsButton) {
            //get shiftNum of selected shift and add contents of details box to the array at that point
            int index = shiftsComboBox.getSelectedIndex();
            String shift = (String) shiftsComboBox.getSelectedItem();
            String location = (String) locationsComboBox.getSelectedItem();
            String xCoord = locationsValues.get(location)[0];
            String yCoord = locationsValues.get(location)[1];
            //get the group
            String selectedShift = (String) shiftsComboBox.getSelectedItem();
            String[] split = selectedShift.split("-");
            String selectedGroup = split[0];


            //write to locationDetails the name, coords and group

            //------------- having locations displayed on schedule ---------------
            details.add(index, location); //change 1 to the shiftNum

            //check if file exists, if not create it
            //write details to it, one per line
            try {
                File file = new File(customLocationsFilePath);
                FileWriter fileWriter = new FileWriter(customLocationsFilePath, StandardCharsets.UTF_8, false);
                if (file.createNewFile()) {
                    //write to it
                    for (int i = 0; i < details.size(); i++) {
                        fileWriter.write(details.get(i) + "\n");
                    }
                } else {
                    //write to it no append
                    for (int i = 0; i < details.size(); i++) {
                        fileWriter.write(details.get(i)+ "\n");
                    }
                }
                fileWriter.close();
            } catch (IOException error) {
                System.out.println("An error occurred.");
                error.printStackTrace();
            }

            try {
                File file = new File(locationDetailsFilePath);
                FileWriter fileWriter = new FileWriter(locationDetailsFilePath, StandardCharsets.UTF_8, true);
                if (file.length() == 0) {
                    fileWriter.write(location + "," + xCoord + "," + yCoord + "," + selectedGroup);
                }
                else {
                    fileWriter.write("\n" + location + "," + xCoord + "," + yCoord + "," + selectedGroup);
                }
                fileWriter.close();
            } catch (IOException error) {
                System.out.println("An error occurred.");
                error.printStackTrace();
            }

            shiftDetailsMessageLabel.setText("Location '" + location + "' assigned to shift " + shift);
        }

        else if (e.getSource() == resetShiftDetailsButton) {
            File file = new File(customLocationsFilePath);
            file.delete();

            //empty location details
            FileWriter fileWriter = null;
            try {
                ArrayList<String> officeDetails = getOfficeDetails();
                fileWriter = new FileWriter(locationDetailsFilePath, StandardCharsets.UTF_8, false);
                //fileWriter.write(officeDetails.get(0) + "," + officeDetails.get(1) + "," + officeDetails.get(2) + ",1");
                fileWriter.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            shiftDetailsMessageLabel.setText("Shift details successfully reset");
        }

        else if (e.getSource() == displayShiftDetailsButton) {
            int index = shiftsComboBox.getSelectedIndex();
            JOptionPane.showMessageDialog(frame, details.get(index));
        }

        else if (e.getSource() == shiftDetailsHelpButton) {
            JOptionPane.showMessageDialog(frame, "Use the drop-down boxes to select a shift, and a location for it to be assigned to.\n" +
                    "Shifts are in the format 'Group-Day-Time'.\n" +
                    "Select 'Reset All' to delete all current shift details.");
        }

        else if(e.getSource() == changeConfigButton) {
            changeConfigGroupsMessageLabel.setText("");
            changeConfigShiftsMessageLabel.setText("");
            cl.show(panelCont,"5");
        }

        else if(e.getSource() == confirmChangeConfigButton) {
            //take combobox inputs and write to file
            int numGroups = groupsComboBox.getSelectedIndex() + 1;
            int numShifts = timesComboBox.getSelectedIndex() + 1;
            try {
                FileWriter fileWriter = new FileWriter(configFilePath, StandardCharsets.UTF_8, false);
                fileWriter.write(Integer.toString(numGroups) + "," + Integer.toString(numShifts));
                fileWriter.close();
                changeConfigGroupsMessageLabel.setText("Group number set to: " + numGroups);
                changeConfigShiftsMessageLabel.setText("Number of shifts per day set to: " + numShifts);
            } catch (IOException error) {
                error.printStackTrace();
            }

            File file = new File(customLocationsFilePath);
            file.delete();

            args = new String[]{String.valueOf(numGroups), String.valueOf(numShifts)};
        }

        else if(e.getSource() == requestsButton) {
            requestsMessageLabel.setText("");
            try {
                ArrayList<String> requests = getRequestsComboBoxValues();
                for (int i = 0; i < requests.size(); i++) {
                    requestsComboBox.addItem(requests.get(i));
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            cl.show(panelCont,"6");
        }

        else if (e.getSource() == confirmRequestsButton) {

            username = (String) requestsComboBox.getSelectedItem();
            HashMap<String,String[]> loginInfo = loginDetails.getLoginInfo();
            String password = loginInfo.get(username)[0];

            //remove user from login
            try {
                loginDetails.removeLoginInfo(username);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            //add user back with group 0, so they are not included
            try {
                //loginDetails.addLoginInfo(username, getPassword(username), "user","0");
                loginDetails.addLoginInfo(username, password, "user","0");
                //HashMap<String,String[]> loginInfo = loginDetails.getLoginInfo();
                /*loginInfo.remove(username);
                loginInfo.put(username,loginInfo.get(username));
                loginDetails.setLoginInfo(loginInfo);*/

            } catch (Exception ex) {
                ex.printStackTrace();
            }

            //remove from requests file
            ArrayList<String> tempRequests = new ArrayList<>();
            BufferedReader br = null;
            String line = null;
            try {
                br = new BufferedReader(new FileReader(requestsFilePath));
                while (((line = br.readLine()) != null)) {
                    if (!Objects.equals(username, line)) {
                        tempRequests.add(line);
                    }
                }
                br.close();
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

            try {
                File newLoginDetails = new File(newRequestsFilePath);
                if (newLoginDetails.createNewFile()) {
                    System.out.println("File created");
                }
                else {
                    System.out.println("File not created");
                }
            } catch (IOException ex) {
                System.out.println("An error occurred");
                ex.printStackTrace();
            }

            //copy to temp file
            try {
                FileWriter fileWriter = new FileWriter(newRequestsFilePath, true);
                for (int z = 0; z < tempRequests.size(); z++) {
                    if (!("".equals(tempRequests.get(z)))) { //fixes error with blank lines
                        if (z == (tempRequests.size()-1)) {
                            fileWriter.write(tempRequests.get(z)); //don't add empty line if last one
                        }
                        else {
                            fileWriter.write(tempRequests.get(z) + "\n");
                        }
                    }
                }
                fileWriter.close();
            } catch (IOException error) {
                error.printStackTrace();
            }

            //delete original, rename temp file
            File originalFile = new File(requestsFilePath);
            originalFile.delete();
            File newFile = new File(newRequestsFilePath);
            newFile.renameTo(originalFile);

            requestsMessageLabel.setText("Request successfully granted");

        }

        else if (e.getSource() == denyRequestsButton) {
            //remove request from file
            username = (String) requestsComboBox.getSelectedItem();
            ArrayList<String> tempRequests = new ArrayList<>();
            BufferedReader br = null;
            String line = null;
            try {
                br = new BufferedReader(new FileReader(requestsFilePath));
                while (((line = br.readLine()) != null)) {
                    if (!Objects.equals(username, line)) {
                        tempRequests.add(line);
                    }
                }
                br.close();
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

            try {
                File newLoginDetails = new File(newRequestsFilePath);
                if (newLoginDetails.createNewFile()) {
                    System.out.println("File created");
                }
                else {
                    System.out.println("File not created");
                }
            } catch (IOException ex) {
                System.out.println("An error occurred");
                ex.printStackTrace();
            }

            //copy to temp file
            try {
                FileWriter fileWriter = new FileWriter(newRequestsFilePath, true);
                for (int z = 0; z < tempRequests.size(); z++) {
                    if (!("".equals(tempRequests.get(z)))) { //fixes error with blank lines
                        if (z == (tempRequests.size()-1)) {
                            fileWriter.write(tempRequests.get(z)); //don't add empty line if last one
                        }
                        else {
                            fileWriter.write(tempRequests.get(z) + "\n");
                        }
                    }
                }
                fileWriter.close();
            } catch (IOException error) {
                error.printStackTrace();
            }

            //delete original, rename temp file
            File originalFile = new File(requestsFilePath);
            originalFile.delete();
            File newFile = new File(newRequestsFilePath);
            newFile.renameTo(originalFile);

            requestsMessageLabel.setText("Request successfully denied");
        }

        else if(e.getSource() == exitButton) {
            frame.dispose();
        }

    }

    //get the password for a specific user
    public String getPassword(String username) throws Exception {
        HashMap<String, String[]> logins = loginDetails.getLoginInfo();

        String password = "a";
        BufferedReader br = new BufferedReader(new FileReader(loginDetailsFilePath));
        String line;

        return password;
    }

    public ArrayList<String> getRequestsComboBoxValues() throws IOException {
        ArrayList<String> requests = new ArrayList<>();
        //read requests file, get usernames
        BufferedReader br = new BufferedReader(new FileReader(requestsFilePath));
        String line;

        //read loginDetails.txt, add its values into an arraylist of arrays, 1 array per line
        while (((line = br.readLine()) != null)) {
            requests.add(line);
        }
        br.close();
        return requests;
    }

    public HashMap<String,String[]> getLocationsComboBoxValues() throws IOException {
        //ArrayList<String> locations = new ArrayList<>();
        HashMap<String, String[]> locations = new HashMap<String, String[]>();
        //read requests file, get usernames
        BufferedReader br = new BufferedReader(new FileReader(locationCoordinatesFilePath));
        String line;

        while (((line = br.readLine()) != null)) {
            String[] values = line.split(",");
            if (!Objects.equals(values[0], "Office")) {
                locations.put(values[0], new String[]{values[1], values[2]});
            }
        }
        br.close();
        return locations;
    }

    public ArrayList<String> getOfficeDetails() throws IOException {
        ArrayList<String> details = new ArrayList<>();
        //read requests file, get usernames
        BufferedReader br = new BufferedReader(new FileReader(officeFilePath));
        String line;

        //read officeDetails.txt
        while (((line = br.readLine()) != null)) {
            String[] values = line.split(",");
            details.add(values[0]);
            details.add(values[1]);
            details.add(values[2]);
        }
        br.close();
        return details;
    }
}