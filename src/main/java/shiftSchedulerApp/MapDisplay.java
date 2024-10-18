/**
 * Copyright 2019 Esri
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy
 * of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package shiftSchedulerApp;

import com.esri.arcgisruntime.geometry.CoordinateFormatter;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;

import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.geometry.Geometry;
import com.esri.arcgisruntime.symbology.TextSymbol;
import com.esri.arcgisruntime.tasks.networkanalysis.Route;
import com.esri.arcgisruntime.tasks.networkanalysis.RouteParameters;
import com.esri.arcgisruntime.tasks.networkanalysis.RouteResult;
import com.esri.arcgisruntime.tasks.networkanalysis.RouteTask;
import com.esri.arcgisruntime.tasks.networkanalysis.Stop;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;

import java.io.*;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;
import java.util.List;

import javafx.embed.swing.JFXPanel;

import com.esri.arcgisruntime.ArcGISRuntimeEnvironment;
import com.esri.arcgisruntime.mapping.BasemapStyle;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.MapView;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import javax.swing.*;

public class MapDisplay extends Application {

    private MapView mapView;
    private GraphicsOverlay graphicsOverlay;    //overlay for routing points
    private GraphicsOverlay graphicsOverlay2;
    private Graphic routeGraphic;
    private Graphic pointGraphic2;
    private Graphic textGraphic;
    private TextSymbol textSymbol;
    private SimpleMarkerSymbol simpleMarkerSymbol;
    private SimpleMarkerSymbol officeMarkerSymbol;
    private RouteTask routeTask;
    private RouteParameters routeParameters;
    private final ObservableList<Stop> routeStops = FXCollections.observableArrayList();
    private final ListView<String> directions = new ListView<>();
    private Label routeTimeLabel = new Label("Expected Route Time: ");
    private Label groupLabel = new Label("Select a group to display");
    private ArrayList<String[]> locationArray = new ArrayList<String[]>();
    private ArrayList<Point> pointsArray = new ArrayList<Point>();
    private ArrayList<Graphic> pointsGraphicsArray = new ArrayList<Graphic>();
    private ArrayList<Graphic> textGraphicsArray = new ArrayList<Graphic>();
    private final String coordinatesFilePath = "C:\\Users\\liams\\Downloads\\shift-scheduler-project\\shift-scheduler-project\\src\\main\\java\\shiftSchedulerApp\\locationDetails.txt";
    private final String officeFilePath = "C:\\Users\\liams\\Downloads\\shift-scheduler-project\\shift-scheduler-project\\src\\main\\java\\shiftSchedulerApp\\officeDetails.txt";
    private double routeTime = 0.0;
    private int routeTimeInt = 0;
    private BigDecimal routeTimeDecimal;
    private int textToggle = 0;
    private int groupNum = 0;

    public static void main(String[] args) {
        newMain(args);
    }

    public static void newMain(final String[] args){
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        //create a swing frame and add the fxpanel to it
        JFrame frame = new JFrame("Map Display");
        JFXPanel fxPanel = new JFXPanel();
        frame.add(fxPanel);
        frame.setSize(800,800);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        String yourApiKey = "AAPK012a1b7f524945ae9c09269e1385376073qBnQto9iUQe5YyPByarHt9PGsbpe01CiZC9fH7GtKrnGhGv-HJiLirYgeoQiqA";
        routeTask = new RouteTask("https://route-api.arcgis.com/arcgis/rest/services/World/Route/NAServer/Route_World");
        ArcGISRuntimeEnvironment.setApiKey(yourApiKey);

        //set up the map, add to panel
        StackPane stackPane = new StackPane();
        Scene scene = new Scene(stackPane);
        fxPanel.setScene(scene);

        ArcGISMap map = new ArcGISMap(BasemapStyle.ARCGIS_STREETS);

        mapView = new MapView();
        mapView.setMap(map);
        mapView.setViewpoint(new Viewpoint(53.453648, -2.370533, 18055.954822));
        stackPane.getChildren().add(mapView);

        //formatting
        directions.setMaxSize(400, 200);
        routeTimeLabel.setMaxSize(387,38);
        routeTimeLabel.setStyle("-fx-background-color: white;");
        routeTimeLabel.setFont(new Font(null, 19));
        groupLabel.setMaxSize(575,30);
        groupLabel.setStyle("-fx-background-color: lightgrey;");
        groupLabel.setFont(new Font(null, 19));

        //creating buttons
        Button button1 = new Button("1");
        Button button2 = new Button("2");
        Button button3 = new Button("3");
        Button button4 = new Button("4");
        Button button5 = new Button("5");
        Button button6 = new Button("6");
        Button button7 = new Button("7");
        Button button8 = new Button("8");
        Button button9 = new Button("9");
        Button button10 = new Button("10");
        Button scheduleButton = new Button("Schedule");
        Button toggleTextButton = new Button("Toggle Text");
        Button displayAllButton = new Button("Display All Locations");

        //adding to the StackPane
        stackPane.getChildren().add(button1);
        stackPane.getChildren().add(button2);
        stackPane.getChildren().add(button3);
        stackPane.getChildren().add(button4);
        stackPane.getChildren().add(button5);
        stackPane.getChildren().add(button6);
        stackPane.getChildren().add(button7);
        stackPane.getChildren().add(button8);
        stackPane.getChildren().add(button9);
        stackPane.getChildren().add(button10);
        stackPane.getChildren().add(scheduleButton);
        stackPane.getChildren().add(toggleTextButton);
        stackPane.getChildren().add(displayAllButton);
        stackPane.getChildren().add(directions);
        stackPane.getChildren().add(routeTimeLabel);
        stackPane.getChildren().add(groupLabel);

        button1.setMaxSize(57, 50);
        button2.setMaxSize(57, 50);
        button3.setMaxSize(57, 50);
        button4.setMaxSize(57, 50);
        button5.setMaxSize(57, 50);
        button6.setMaxSize(57, 50);
        button7.setMaxSize(57, 50);
        button8.setMaxSize(57, 50);
        button9.setMaxSize(57, 50);
        button10.setMaxSize(57, 50);
        scheduleButton.setMaxSize(220,80);
        toggleTextButton.setMaxSize(200,40);
        displayAllButton.setMaxSize(200,40);

        //set alignment for the buttons and directions
        StackPane.setAlignment(directions, Pos.TOP_LEFT);
        StackPane.setAlignment(routeTimeLabel,Pos.TOP_RIGHT);
        StackPane.setAlignment(groupLabel,Pos.BOTTOM_LEFT);
        StackPane.setAlignment(button1,Pos.BOTTOM_LEFT);
        StackPane.setAlignment(button2,Pos.BOTTOM_LEFT);
        StackPane.setAlignment(button3,Pos.BOTTOM_LEFT);
        StackPane.setAlignment(button4,Pos.BOTTOM_LEFT);
        StackPane.setAlignment(button5,Pos.BOTTOM_LEFT);
        StackPane.setAlignment(button6,Pos.BOTTOM_LEFT);
        StackPane.setAlignment(button7,Pos.BOTTOM_LEFT);
        StackPane.setAlignment(button8,Pos.BOTTOM_LEFT);
        StackPane.setAlignment(button9,Pos.BOTTOM_LEFT);
        StackPane.setAlignment(button10,Pos.BOTTOM_LEFT);
        StackPane.setAlignment(scheduleButton,Pos.BOTTOM_LEFT);
        StackPane.setAlignment(toggleTextButton,Pos.TOP_RIGHT);
        StackPane.setAlignment(displayAllButton,Pos.TOP_RIGHT);

        //move the buttons
        button2.setTranslateX(57);
        button3.setTranslateX(115);
        button4.setTranslateX(172);
        button5.setTranslateX(230);
        button6.setTranslateX(287);
        button7.setTranslateX(345);
        button8.setTranslateX(403);
        button9.setTranslateX(461);
        button10.setTranslateX(518);
        scheduleButton.setTranslateX(575);
        toggleTextButton.setTranslateX(-200);
        toggleTextButton.setTranslateY(38);
        displayAllButton.setTranslateX(0);
        displayAllButton.setTranslateY(38);

        groupLabel.setTranslateY(-50);

        button1.setStyle("-fx-font-size:20");
        button2.setStyle("-fx-font-size:20");
        button3.setStyle("-fx-font-size:20");
        button4.setStyle("-fx-font-size:20");
        button5.setStyle("-fx-font-size:20");
        button6.setStyle("-fx-font-size:20");
        button7.setStyle("-fx-font-size:20");
        button8.setStyle("-fx-font-size:20");
        button9.setStyle("-fx-font-size:20");
        button10.setStyle("-fx-font-size:20");
        toggleTextButton.setStyle("-fx-font-size:18");
        displayAllButton.setStyle("-fx-font-size:18");
        scheduleButton.setStyle("-fx-font-size:20");

        graphicsOverlay = new GraphicsOverlay();    //overlay for routing points
        graphicsOverlay2 = new GraphicsOverlay();   //needed to fix error of points disappearing after clicking, can't be on same graphicsOverlay

        mapView.getGraphicsOverlays().add(graphicsOverlay);
        mapView.getGraphicsOverlays().add(graphicsOverlay2);


        //add the office and locations to the locationsArray
        BufferedReader br = new BufferedReader(new FileReader(officeFilePath));
        String line;

        while (((line = br.readLine()) != null)) {
            String[] values = line.split(",");
            locationArray.add(values);
        }

        br = new BufferedReader(new FileReader(coordinatesFilePath));

        while (((line = br.readLine()) != null)) {
            String[] values = line.split(",");
            locationArray.add(values);
        }

        //setting up the points to be displayed
        SimpleMarkerSymbol simpleMarkerSymbol =
                new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.MEDIUMPURPLE, 18);
        //different colour for office
        SimpleMarkerSymbol officeMarkerSymbol =
                new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.RED, 18);
        SimpleLineSymbol blackOutlineSymbol =
                new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.BLACK, 2);

        simpleMarkerSymbol.setOutline(blackOutlineSymbol);
        officeMarkerSymbol.setOutline(blackOutlineSymbol);

        //loop through location array and add all coordinates
        for (int i = 0; i < locationArray.size(); i++) {
            //add a check here to see if it should be displayed
            pointsArray.add(new Point(Double.parseDouble((locationArray.get(i))[2]),Double.parseDouble((locationArray.get(i))[1]), SpatialReferences.getWgs84())); //longitude first as x,y
        }

        //add all points to map
        for (int i = 0; i < pointsArray.size(); i++) {
            if (i == 0){
                pointGraphic2 = new Graphic(pointsArray.get(i), officeMarkerSymbol);
            } else {
                pointGraphic2 = new Graphic(pointsArray.get(i), simpleMarkerSymbol);
            }
            pointsGraphicsArray.add(pointGraphic2);
            textSymbol = new TextSymbol(20, locationArray.get(i)[0],Color.RED, TextSymbol.HorizontalAlignment.CENTER, TextSymbol.VerticalAlignment.TOP);
            textGraphic = new Graphic(pointsArray.get(i), textSymbol);
            textGraphicsArray.add(textGraphic);

            graphicsOverlay2.getGraphics().add(pointsGraphicsArray.get(i));
            graphicsOverlay2.getGraphics().add(textGraphicsArray.get(i));
            configureText(textSymbol);
        }

        //button methods to display certain points and text
        EventHandler<ActionEvent> button1Event = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                for (int i = 1; i < pointsArray.size(); i++) {
                    if (Objects.equals(locationArray.get(i)[3], "1")) {
                        pointsGraphicsArray.get(i).setVisible(true);
                        textGraphicsArray.get(i).setVisible(true);
                    }
                    else {
                        pointsGraphicsArray.get(i).setVisible(false);
                        textGraphicsArray.get(i).setVisible(false);
                    }
                    pointsGraphicsArray.get(0).setVisible(true);
                    textGraphicsArray.get(0).setVisible(true);
                }
            }
        };

        EventHandler<ActionEvent> button2Event = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                for (int i = 1; i < pointsArray.size(); i++) {
                    if (Objects.equals(locationArray.get(i)[3], "2")) {
                        pointsGraphicsArray.get(i).setVisible(true);
                        textGraphicsArray.get(i).setVisible(true);
                    }
                    else {
                        pointsGraphicsArray.get(i).setVisible(false);
                        textGraphicsArray.get(i).setVisible(false);
                    }
                    pointsGraphicsArray.get(0).setVisible(true);
                    textGraphicsArray.get(0).setVisible(true);
                }
            }
        };

        EventHandler<ActionEvent> button3Event = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                for (int i = 1; i < pointsArray.size(); i++) {
                    if (Objects.equals(locationArray.get(i)[3], "3")) {
                        pointsGraphicsArray.get(i).setVisible(true);
                        textGraphicsArray.get(i).setVisible(true);
                    }
                    else {
                        pointsGraphicsArray.get(i).setVisible(false);
                        textGraphicsArray.get(i).setVisible(false);
                    }
                    pointsGraphicsArray.get(0).setVisible(true);
                    textGraphicsArray.get(0).setVisible(true);
                }
            }
        };

        EventHandler<ActionEvent> button4Event = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                for (int i = 1; i < pointsArray.size(); i++) {
                    if (Objects.equals(locationArray.get(i)[3], "4")) {
                        pointsGraphicsArray.get(i).setVisible(true);
                        textGraphicsArray.get(i).setVisible(true);
                    }
                    else {
                        pointsGraphicsArray.get(i).setVisible(false);
                        textGraphicsArray.get(i).setVisible(false);
                    }
                    pointsGraphicsArray.get(0).setVisible(true);
                    textGraphicsArray.get(0).setVisible(true);
                }
            }
        };

        EventHandler<ActionEvent> button5Event = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                for (int i = 1; i < pointsArray.size(); i++) {
                    if (Objects.equals(locationArray.get(i)[3], "5")) {
                        pointsGraphicsArray.get(i).setVisible(true);
                        textGraphicsArray.get(i).setVisible(true);
                    }
                    else {
                        pointsGraphicsArray.get(i).setVisible(false);
                        textGraphicsArray.get(i).setVisible(false);
                    }
                    pointsGraphicsArray.get(0).setVisible(true);
                    textGraphicsArray.get(0).setVisible(true);
                }
            }
        };

        EventHandler<ActionEvent> button6Event = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                for (int i = 1; i < pointsArray.size(); i++) {
                    if (Objects.equals(locationArray.get(i)[3], "6")) {
                        pointsGraphicsArray.get(i).setVisible(true);
                        textGraphicsArray.get(i).setVisible(true);
                    }
                    else {
                        pointsGraphicsArray.get(i).setVisible(false);
                        textGraphicsArray.get(i).setVisible(false);
                    }
                    pointsGraphicsArray.get(0).setVisible(true);
                    textGraphicsArray.get(0).setVisible(true);
                }
            }
        };

        EventHandler<ActionEvent> button7Event = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                for (int i = 1; i < pointsArray.size(); i++) {
                    if (Objects.equals(locationArray.get(i)[3], "7")) {
                        pointsGraphicsArray.get(i).setVisible(true);
                        textGraphicsArray.get(i).setVisible(true);
                    }
                    else {
                        pointsGraphicsArray.get(i).setVisible(false);
                        textGraphicsArray.get(i).setVisible(false);
                    }
                    pointsGraphicsArray.get(0).setVisible(true);
                    textGraphicsArray.get(0).setVisible(true);
                }
            }
        };

        EventHandler<ActionEvent> button8Event = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                for (int i = 1; i < pointsArray.size(); i++) {
                    if (Objects.equals(locationArray.get(i)[3], "8")) {
                        pointsGraphicsArray.get(i).setVisible(true);
                        textGraphicsArray.get(i).setVisible(true);
                    }
                    else {
                        pointsGraphicsArray.get(i).setVisible(false);
                        textGraphicsArray.get(i).setVisible(false);
                    }
                    pointsGraphicsArray.get(0).setVisible(true);
                    textGraphicsArray.get(0).setVisible(true);
                }
            }
        };

        EventHandler<ActionEvent> button9Event = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                for (int i = 1; i < pointsArray.size(); i++) {
                    if (Objects.equals(locationArray.get(i)[3], "9")) {
                        pointsGraphicsArray.get(i).setVisible(true);
                        textGraphicsArray.get(i).setVisible(true);
                    }
                    else {
                        pointsGraphicsArray.get(i).setVisible(false);
                        textGraphicsArray.get(i).setVisible(false);
                    }
                    pointsGraphicsArray.get(0).setVisible(true);
                    textGraphicsArray.get(0).setVisible(true);
                }
            }
        };

        EventHandler<ActionEvent> button10Event = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                for (int i = 1; i < pointsArray.size(); i++) {
                    if (Objects.equals(locationArray.get(i)[3], "10")) {
                        pointsGraphicsArray.get(i).setVisible(true);
                        textGraphicsArray.get(i).setVisible(true);
                    }
                    else {
                        pointsGraphicsArray.get(i).setVisible(false);
                        textGraphicsArray.get(i).setVisible(false);
                    }
                    pointsGraphicsArray.get(0).setVisible(true);
                    textGraphicsArray.get(0).setVisible(true);
                }
            }
        };

        EventHandler<ActionEvent> scheduleButtonEvent = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                try {
                    LoginDetails loginDetails = new LoginDetails();
                    LoginDisplay loginDisplay = new LoginDisplay(loginDetails.getLoginInfo());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        };

        EventHandler<ActionEvent> toggleTextButtonEvent = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                for (int i = 0; i < textGraphicsArray.size(); i++ ){
                    if (textToggle == 0) {
                        textGraphicsArray.get(i).setVisible(false);
                    }
                    else {
                        textGraphicsArray.get(i).setVisible(true);
                    }
                }
                if (textToggle == 0) {
                    textToggle = 1;
                }
                else {
                    textToggle = 0;
                }

                //to fix bug with all text displaying even if a specific group is selected, have a variable for which group
                //has been selected, and only show/hide text for that group
            }
        };

        EventHandler<ActionEvent> displayAllButtonEvent = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                for (int i = 0; i < pointsArray.size(); i++) {
                    pointsGraphicsArray.get(i).setVisible(true);
                    textGraphicsArray.get(i).setVisible(true);
                }
            }
        };

        button1.setOnAction(button1Event);
        button2.setOnAction(button2Event);
        button3.setOnAction(button3Event);
        button4.setOnAction(button4Event);
        button5.setOnAction(button5Event);
        button6.setOnAction(button6Event);
        button7.setOnAction(button7Event);
        button8.setOnAction(button8Event);
        button9.setOnAction(button9Event);
        button10.setOnAction(button10Event);
        scheduleButton.setOnAction(scheduleButtonEvent);
        toggleTextButton.setOnAction(toggleTextButtonEvent);
        displayAllButton.setOnAction(displayAllButtonEvent);

        ListenableFuture<RouteParameters> routeParametersFuture = routeTask.createDefaultParametersAsync();
        routeParametersFuture.addDoneListener(() -> {
            try {
                routeParameters = routeParametersFuture.get();
                routeParameters.setReturnDirections(true);
                directions.getItems().add("Click to add two points to the map to display a route.");

            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, e.toString());
                alert.show();
                e.printStackTrace();
            }
        });

        addStopsOnMouseClicked();

        routeStops.addListener((ListChangeListener<Stop>) e -> {
            // tracks the number of stops added to the map, and use it to create graphic geometry and symbol text
            int routeStopsSize = routeStops.size();
            // handle user interaction
            if (routeStopsSize == 0) {
                return;
            } else if (routeStopsSize == 1) {
                graphicsOverlay.getGraphics().clear();
                if (!directions.getItems().isEmpty())
                    directions.getItems().clear();
                directions.getItems().add("Click to add two points to the map to find a route.");
            }
            // create a blue circle symbol for the stop
            SimpleMarkerSymbol stopMarker = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.BLUE, 20);
            // get the stop's geometry
            Geometry routeStopGeometry = routeStops.get(routeStopsSize-1).getGeometry();

            graphicsOverlay.getGraphics().add(new Graphic(routeStopGeometry, stopMarker));

            if (routeStopsSize == 2) {
                // remove the mouse clicked event if two stops have been added
                mapView.setOnMouseClicked(null);
                routeParameters.setStops(routeStops);

                // get the route and display it
                ListenableFuture<RouteResult> routeResultFuture = routeTask.solveRouteAsync(routeParameters);
                routeResultFuture.addDoneListener(() -> {
                    try {
                        RouteResult result = routeResultFuture.get();
                        List<Route> routes = result.getRoutes();

                        if (!routes.isEmpty()) {
                            Route route = routes.get(0);
                            Geometry shape = route.getRouteGeometry();
                            routeGraphic = new Graphic(shape, new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.BLUE, 2));
                            graphicsOverlay.getGraphics().add(routeGraphic);

                            // calculate route time, display it
                            routeTime = route.getTotalTime();
                            BigDecimal bd = new BigDecimal(routeTime);
                            bd = bd.round(new MathContext(3));
                            routeTime = Math.round(bd.doubleValue());
                            routeTime = (int) routeTime;

                            routeTimeInt = (int) routeTime;
                            routeTimeDecimal = bd.subtract(new BigDecimal(routeTimeInt));

                            routeTimeLabel.setText("Expected Route Time: " + formatRouteTime(routeTimeInt));

                            // get the direction text for each maneuver and display it as a list in the UI
                            route.getDirectionManeuvers().forEach(step -> directions.getItems().add(step.getDirectionText()));
                            // reset stops and re-enable mapview interactions once the route task has completed
                            routeStops.clear();
                            addStopsOnMouseClicked();
                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });
            }
        });
    }

    /**
     * adds points when the mouse is clicked
     */
    private void addStopsOnMouseClicked() {
        mapView.setOnMouseClicked(event -> {
            if (event.isStillSincePress() && event.getButton() == MouseButton.PRIMARY) {
                Point2D mapPoint = new Point2D(event.getX(), event.getY());
                routeStops.add(new Stop(mapView.screenToLocation(mapPoint)));
            }
        });
    }

    /**
     * configures the text displayed on the map
     * @param text the TextSymbol to be configured
     */
    public void configureText (TextSymbol text) {
        text.setOutlineColor(Color.BLACK);
        text.setOutlineWidth(4);
    }

    public String formatRouteTime(int routeTime) {
        int hours = 0;
        String hoursString = "";
        String minutesString = routeTime + " minute(s)";
        if (routeTime > 59) {
            hours = routeTime / 60;
            hoursString = hours + " hour(s) ";
            for (int i = 0; i < hours; i++) {
                routeTime -=60;
            }
            minutesString = routeTime + " minute(s)";
            return (hoursString + " " + minutesString);
        }
        else {
            return minutesString;
        }
    }

    /**
     * Stops and releases all resources used in application.
     */
    @Override
    public void stop() {

        if (mapView != null) {
            mapView.dispose();
        }
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
