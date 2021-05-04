package starter.graphical;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.Optional;

public class SketchIt extends Application {
    static boolean saved = false;
    static double WINDOW_WIDTH = 1280;
    static double WINDOW_HEIGHT = 960;
    final String DELIMITER = ",";
    final String ENDL = "\n";
    CanvasView canvasView;
    ToolbarView toolbarView;

    @Override
    public void start(Stage stage) throws Exception {

        // top section: menu bar
        Menu FILE = new Menu("File");
        MenuItem NEW = new MenuItem("New");
        MenuItem LOAD = new MenuItem("Load");
        MenuItem SAVE = new MenuItem("Save");
        MenuItem QUIT = new MenuItem("Quit");
        FILE.getItems().add(NEW);
        FILE.getItems().add(LOAD);
        FILE.getItems().add(SAVE);
        FILE.getItems().add(QUIT);
        Menu HELP = new Menu("Help");
        MenuItem ABOUT = new MenuItem("About");
        HELP.getItems().add(ABOUT);
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(FILE);
        menuBar.getMenus().add(HELP);
        VBox vbox = new VBox(menuBar);
        NEW.setOnAction(e -> {
            if (saved == false) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.OK, ButtonType.NO, ButtonType.CANCEL);
                alert.setTitle("New SketchIt");
                alert.setHeaderText("Discarding current drawing");
                alert.setContentText("Do you wish to save it ?");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK){
                    saved = true;
                    save(stage);
                    canvasView.canvas_clear();
                    saved = false;
                } else if (result.get() == ButtonType.NO) {
                    canvasView.canvas_clear();
                    saved = false;
                } else {}
            } else {
                canvasView.canvas_clear();
                saved = false;
            }
        });

        LOAD.setOnAction(e -> {
            if (saved == false) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.OK, ButtonType.NO, ButtonType.CANCEL);
                alert.setTitle("Loading SketchIt");
                alert.setHeaderText("Discarding current drawing");
                alert.setContentText("Do you wish to save it ?");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK){
                    saved = true;
                    save(stage);
                    canvasView.canvas_clear();
                    load(stage);
                    saved = false;
                } else if (result.get() == ButtonType.NO) {
                    canvasView.canvas_clear();
                    load(stage);
                    saved = false;
                } else {}
            } else {
                load(stage);
            }
        });

        SAVE.setOnAction(e -> {
            saved = true;
            save(stage);
        });

        QUIT.setOnAction(e -> {
            if (saved == false) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.OK, ButtonType.NO, ButtonType.CANCEL);
                alert.setTitle("Exiting SketchIt");
                alert.setHeaderText("Drawing not saved");
                alert.setContentText("Do you wish to save it before leaving?");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    saved = true;
                    save(stage);
                    Platform.exit();
                } else if (result.get() == ButtonType.NO) {
                    Platform.exit();
                } else {}
            } else {
                Platform.exit();
            }
        });

        ABOUT.setOnAction(e -> {
            TextArea text = new TextArea("SketchIt\n" + "Author: Yilin Zhang\n" + "Y2785ZHA");
            text.setWrapText(true);
            text.setPrefWidth(280);
            text.setPrefHeight(125);
            text.relocate(10, 10);
            text.setEditable(false);

            Button ok = new Button("Ok");
            ok.setPrefWidth(75);
            ok.relocate(130, 155);

            Button cancel = new Button("Cancel");
            cancel.setPrefWidth(75);
            cancel.relocate(215, 155);

            Scene info = new Scene(new Pane(
                    text, ok, cancel), 300, 200);
            Stage dialog = new Stage();
            dialog.setScene(info);
            dialog.setTitle("About");
            dialog.setResizable(false);
            dialog.setAlwaysOnTop(true);
            dialog.show();

            ok.setOnAction(value ->  {
                dialog.close();;
            });

            cancel.setOnAction(value ->  {
                dialog.close();;
            });
        });

        // create and initialize the Model to hold our counter
        Model model = new Model();

        // create each view, and tell them about the model
        // the views will register themselves with the model
        canvasView = new CanvasView(model);
        toolbarView = new ToolbarView(model);

        BorderPane border = new BorderPane();
        border.setTop(vbox);
        border.setLeft(toolbarView);
        border.setCenter(canvasView);
        // Add border to a scene (and the scene to the stage)
        Scene scene = new Scene(border, WINDOW_WIDTH, WINDOW_HEIGHT);
        stage.setScene(scene);
        stage.setMaxWidth(1920);
        stage.setMaxHeight(1440);
        stage.setMinWidth(640);
        stage.setMinHeight(480);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void save(Stage stage) {
        // add file chooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save");
        File target = fileChooser.showSaveDialog(stage);
        if (target == null) {
            return;
        }

        FileWriter file = null;
        BufferedWriter writer = null;

        try {
            file = new FileWriter(target);
            writer = new BufferedWriter(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            for (Node node: canvasView.getChildren()) {
                try {

                    if (node instanceof Line) {
                        Line L = (Line) node;
                        writer.write(
                                "Line" + DELIMITER +
                                        L.getStartX() + DELIMITER + L.getStartY() + DELIMITER + L.getEndX() + DELIMITER +
                                        L.getEndY() + DELIMITER + L.getStroke() + DELIMITER + L.getFill() + DELIMITER +
                                        L.getStrokeWidth() + DELIMITER + L.getStrokeDashArray().get(0) + DELIMITER +
                                        L.getStrokeDashArray().get(1)+ ENDL
                        );
                    } else if (node instanceof Circle) {
                        Circle C = (Circle) node;
                        writer.write(
                                "Circle" + DELIMITER +
                                        C.getCenterX() + DELIMITER + C.getCenterY() + DELIMITER + C.getRadius() + DELIMITER +
                                        C.getStroke() + DELIMITER + C.getFill() + DELIMITER + C.getStrokeWidth() + DELIMITER +
                                        C.getStrokeDashArray().get(0) + DELIMITER + C.getStrokeDashArray().get(1)+ ENDL
                        );
                    } else if (node instanceof Rectangle) {
                        Rectangle R = (Rectangle) node;
                        writer.write(
                                "Rectangle" + DELIMITER +
                                        R.getX() + DELIMITER + R.getY() + DELIMITER + R.getWidth() + DELIMITER +
                                        R.getHeight()+ DELIMITER + R.getStroke() + DELIMITER + R.getFill() + DELIMITER +
                                        R.getStrokeWidth() + DELIMITER + R.getStrokeDashArray().get(0) + DELIMITER +
                                        R.getStrokeDashArray().get(1)+ ENDL
                        );
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            writer.close();
            file.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void load(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save");
        File target = fileChooser.showOpenDialog(stage);
        if (target == null) {
            return;
        }

        FileReader file = null;
        BufferedReader reader = null;
        String[] values;

        // open input file
        try {
            file = new FileReader(target);
            reader = new BufferedReader(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // read and process lines one at a time
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                // DELIMITER separates values on a row
                values = line.split(DELIMITER);
                // do something with values here e.g. print them
                String shapeType = values[0];
                switch (shapeType) {
                    case "Line":
                        Line L = new Line();
                        L.setStartX(Double.parseDouble(values[1]));
                        L.setStartY(Double.parseDouble(values[2]));
                        L.setEndX(Double.parseDouble(values[3]));
                        L.setEndY(Double.parseDouble(values[4]));
                        L.setStroke(Paint.valueOf(values[5]));
                        //L.setFill(Paint.valueOf(values[6]));
                        L.setStrokeWidth(Double.parseDouble(values[7]));
                        L.getStrokeDashArray().addAll(Double.valueOf(values[8]), Double.parseDouble(values[9]));
                        canvasView.getChildren().add(L);
                        break;
                    case "Circle":
                        Circle C = new Circle();
                        C.setCenterX(Double.parseDouble(values[1]));
                        C.setCenterY(Double.parseDouble(values[2]));
                        C.setRadius(Double.parseDouble(values[3]));
                        C.setStroke(Paint.valueOf(values[4]));
                        C.setFill(Paint.valueOf(values[5]));
                        C.setStrokeWidth(Double.parseDouble(values[6]));
                        C.getStrokeDashArray().addAll(Double.valueOf(values[7]), Double.parseDouble(values[8]));
                        canvasView.getChildren().add(C);
                        break;
                    case "Rectangle":
                        Rectangle R = new Rectangle();
                        R.setX(Double.parseDouble(values[1]));
                        R.setY(Double.parseDouble(values[2]));
                        R.setWidth(Double.parseDouble(values[3]));
                        R.setHeight(Double.parseDouble(values[4]));
                        R.setStroke(Paint.valueOf(values[5]));
                        R.setFill(Paint.valueOf(values[6]));
                        R.setStrokeWidth(Double.parseDouble(values[7]));
                        R.getStrokeDashArray().addAll(Double.valueOf(values[8]), Double.parseDouble(values[9]));
                        canvasView.getChildren().add(R);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

