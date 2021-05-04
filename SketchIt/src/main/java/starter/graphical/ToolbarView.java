package starter.graphical;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.geometry.Insets;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static starter.graphical.Model.curTool;
import static starter.graphical.Model.curThick;
import static starter.graphical.Model.curStyle;

import static starter.graphical.CanvasView.Selected;
import static starter.graphical.CanvasView.curSelected;
import static starter.graphical.CanvasView.select_line;
import static starter.graphical.CanvasView.select_circle;
import static starter.graphical.CanvasView.select_rect;

public class ToolbarView extends GridPane implements IView {

    static double TOOLBAR_WIDTH = 380;
    static double TOOLBAR_HEIGHT = 960;

    // tools
    TilePane tools;
    Image image1 = new Image("select.png");
    ImageView imageView1 = new ImageView(image1);
    Button select = new Button("", imageView1);
    Image image2 = new Image("eraser.png");
    ImageView imageView2 = new ImageView(image2);
    Button eraser = new Button("", imageView2);
    Image image3 = new Image("line.jpg");
    ImageView imageView3 = new ImageView(image3);
    Button line = new Button("", imageView3);
    Image image4 = new Image("circle.png");
    ImageView imageView4 = new ImageView(image4);
    Button circle = new Button("", imageView4);
    Image image5 = new Image("rectangle.png");
    ImageView imageView5 = new ImageView(image5);
    Button rectangle = new Button("", imageView5);
    Image image6 = new Image("bucket.jpg");
    ImageView imageView6 = new ImageView(image6);
    Button fill = new Button("", imageView6);

    // styles
    TilePane style;
    Stage dialog;
    static ColorPicker lineColor = new ColorPicker();
    static ColorPicker fillColor = new ColorPicker();
    Button lc = new Button("Line Color", lineColor);
    Button fc = new Button("Fill Color", fillColor);
    ToolBar thickness = new ToolBar();
    Button light = new Button("—————");
    Button normal = new Button("—————");
    Button bold = new Button("—————");
    ToolBar lineStyle = new ToolBar();
    Button solid = new Button("—————");
    Button dash = new Button("- - - - - - - - -");
    Button dot = new Button(". . . . . . . . . .");

    ToolbarView(Model model) throws FileNotFoundException {
        this.setPrefSize(TOOLBAR_WIDTH, TOOLBAR_HEIGHT);

        // tools
        tools = new TilePane();
        imageView1.setFitWidth(90);
        imageView1.setFitHeight(90);
        imageView2.setFitWidth(90);
        imageView2.setFitHeight(90);
        imageView3.setFitWidth(90);
        imageView3.setFitHeight(90);
        imageView4.setFitWidth(90);
        imageView4.setFitHeight(90);
        imageView5.setFitWidth(90);
        imageView5.setFitHeight(90);
        imageView6.setFitWidth(90);
        imageView6.setFitHeight(90);

        // tools setting
        tools.getChildren().add(select);
        tools.getChildren().add(eraser);
        tools.getChildren().add(line);
        tools.getChildren().add(circle);
        tools.getChildren().add(rectangle);
        tools.getChildren().add(fill);
        tools.setPadding(new Insets(20, 0, 20, 70));
        tools.setPrefSize(TOOLBAR_WIDTH, TOOLBAR_HEIGHT/2);
        tools.setVgap(25);
        tools.setHgap(25);
        tools.setPrefColumns(2);
        tools.setStyle("-fx-background-color: DAE6F3;");

        // style
        style = new TilePane();
        lineColor.setValue(Color.valueOf("000000"));
        fillColor.setValue(Color.valueOf("ffffff"));
        light.setFont(Font.font("Times New Roman", FontWeight.EXTRA_LIGHT, 16));
        normal.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        bold.setFont(Font.font("Arial", FontWeight.BLACK, 16));
        thickness.setStyle("-fx-background-color: f7e9cd;");
        solid.setFont(Font.font("Times New Roman", FontWeight.NORMAL, 16));
        dash.setFont(Font.font("Arial", FontWeight.MEDIUM, 16));
        dot.setFont(Font.font("Arial", FontWeight.MEDIUM, 16));
        lineStyle.setStyle("-fx-background-color: f7e9cd;");

        // style setting
        thickness.getItems().add(light);
        thickness.getItems().add(normal);
        thickness.getItems().add(bold);
        lineStyle.getItems().add(solid);
        lineStyle.getItems().add(dash);
        lineStyle.getItems().add(dot);
        style.getChildren().add(lc);
        style.getChildren().add(fc);
        style.getChildren().add(thickness);
        style.getChildren().add(lineStyle);
        style.setPadding(new Insets(20, 20, 20, 20));
        style.setPrefSize(TOOLBAR_WIDTH, TOOLBAR_HEIGHT/2);
        style.setVgap(25);
        style.setHgap(25);
        style.setPrefColumns(1);
        style.setStyle("-fx-background-color: DAF3E9;");
        this.add(tools, 0,0);
        this.add(style, 0,1);
        // register with the model when we're ready to start receiving data
        model.addView(this);

        // Toolbar Button Handler
        select.setOnAction(e -> {
            TextArea text = new TextArea("Select Mode Activated\nQuit by pressing ESC on keyboard\nPress buttom below to close this window");
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
            dialog = new Stage();
            dialog.setScene(info);
            dialog.setTitle("Select Mode");
            dialog.setResizable(false);
            dialog.setAlwaysOnTop(true);
            dialog.show();

            ok.setOnAction(value ->  {
                dialog.close();;
            });

            cancel.setOnAction(value ->  {
                dialog.close();;
            });
            curTool = Model.ToolType.Select;
        });

        eraser.setOnAction(e -> {
            curTool = Model.ToolType.Eraser;
        });

        line.setOnAction(e -> {
            curTool = Model.ToolType.Line;
        });

        circle.setOnAction(e -> {
            curTool = Model.ToolType.Circle;
        });

        rectangle.setOnAction(e -> {
            curTool = Model.ToolType.Rect;
        });

        fill.setOnAction(e -> {
            curTool = Model.ToolType.Fill;
        });


        // Style Button Handler
        lineColor.setOnAction(e -> {
            if (curTool == Model.ToolType.Select && curSelected != Selected.None) {
                updateSelected();
            }
        });

        fillColor.setOnAction(e -> {
            if (curTool == Model.ToolType.Select && curSelected != Selected.None) {
                updateSelected();
            }
        });

        light.setOnAction(e -> {
            curThick = Model.Thickness.Light;
            if (curTool == Model.ToolType.Select && curSelected != Selected.None) {
                updateSelected();
            }
        });

        normal.setOnAction(e -> {
            curThick = Model.Thickness.Normal;
            if (curTool == Model.ToolType.Select && curSelected != Selected.None) {
                updateSelected();
            }
        });

        bold.setOnAction(e -> {
            curThick = Model.Thickness.Bold;
            if (curTool == Model.ToolType.Select && curSelected != Selected.None) {
                updateSelected();
            }
        });

        solid.setOnAction(e -> {
            curStyle = Model.LineStyle.Solid;
            if (curTool == Model.ToolType.Select && curSelected != Selected.None) {
                updateSelected();
            }
        });

        dash.setOnAction(e -> {
            curStyle = Model.LineStyle.Dash;
            if (curTool == Model.ToolType.Select && curSelected != Selected.None) {
                updateSelected();
            }
        });

        dot.setOnAction(e -> {
            curStyle = Model.LineStyle.Dot;
            if (curTool == Model.ToolType.Select && curSelected != Selected.None) {
                updateSelected();
            }
        });

        this.setOnKeyPressed(e-> {
            if (curTool == Model.ToolType.Select && e.getCode() == KeyCode.ESCAPE) {
                if (dialog.isShowing()) {
                    dialog.close();
                }
                curTool = Model.ToolType.None;
            }
        });

    }

    public void updateSelected() {
        switch (curSelected) {
            case Line:
                select_line.setStroke(lineColor.getValue());
                select_line.setFill(fillColor.getValue());
                switch (curThick) {
                    case Light:
                        select_line.setStrokeWidth(0.5);
                        break;
                    case Normal:
                        select_line.setStrokeWidth(1.5);
                        break;
                    case Bold:
                        select_line.setStrokeWidth(3);
                        break;
                }
                switch (curStyle) {
                    case Dash:
                        select_line.getStrokeDashArray().addAll(20d, 20d);
                        break;
                    case Dot:
                        select_line.getStrokeDashArray().addAll(2d, 21d);
                        break;
                    case Solid:
                        select_line.getStrokeDashArray().addAll(20d, 0d);
                        break;
                }
                break;
            case Circle:
                select_circle.setStroke(lineColor.getValue());
                select_circle.setFill(fillColor.getValue());
                switch (curThick) {
                    case Light:
                        select_circle.setStrokeWidth(0.5);
                        break;
                    case Normal:
                        select_circle.setStrokeWidth(1.5);
                        break;
                    case Bold:
                        select_circle.setStrokeWidth(3);
                        break;
                }
                switch (curStyle) {
                    case Dash:
                        select_circle.getStrokeDashArray().addAll(20d, 20d);
                        break;
                    case Dot:
                        select_circle.getStrokeDashArray().addAll(2d, 21d);
                        break;
                    case Solid:
                        select_circle.getStrokeDashArray().addAll(20d, 0d);
                        break;
                }
                break;
            case Rect:
                select_rect.setStroke(lineColor.getValue());
                select_rect.setFill(fillColor.getValue());
                switch (curThick) {
                    case Light:
                        select_rect.setStrokeWidth(0.5);
                        break;
                    case Normal:
                        select_rect.setStrokeWidth(1.5);
                        break;
                    case Bold:
                        select_rect.setStrokeWidth(3);
                        break;
                }
                switch (curStyle) {
                    case Dash:
                        select_rect.getStrokeDashArray().addAll(20d, 20d);
                        break;
                    case Dot:
                        select_rect.getStrokeDashArray().addAll(2d, 21d);
                        break;
                    case Solid:
                        select_rect.getStrokeDashArray().addAll(20d, 0d);
                        break;
                }
                break;
        }
    }

    // When notified by the model that things have changed,
    // update to display the new value
    public void updateView() {
        //System.out.println("View: updateView");
    }
}
