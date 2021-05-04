package starter.graphical;

import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;

import java.awt.*;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import static starter.graphical.Model.curTool;
import static starter.graphical.Model.curThick;
import static starter.graphical.Model.curStyle;
import static starter.graphical.ToolbarView.lineColor;
import static starter.graphical.ToolbarView.fillColor;

class CanvasView extends Pane implements IView {

    static double CANVAS_WIDTH = 900;
    static double CANVAS_HEIGHT = 960;
    Pane pane = this;

    Line line = new Line();
    Circle circle = new Circle();
    Rectangle rect = new Rectangle();

    static Line select_line;
    static Circle select_circle;
    static Rectangle select_rect;
    Point start = new Point();
    Boolean resize = false;

    enum Selected {
        None,
        Line,
        Circle,
        Rect
    }

    static Selected curSelected = Selected.None;

    CanvasView(Model model) {
        this.setPrefSize(CANVAS_WIDTH, CANVAS_HEIGHT);
        //Canvas canvas = new Canvas();
        //GraphicsContext gc = canvas.getGraphicsContext2D();

        pane.setOnDragDetected(e -> pane.startFullDrag());
        this.addEventHandler(MouseEvent.ANY, this::handle);
        // register with the model when we're ready to start receiving data
        model.addView(this);
    }


    // When notified by the model that things have changed,
    // update to display the new value
    public void updateView() {
        // System.out.println("View: updateView");
    }

    public void canvas_clear() {
        pane.getChildren().clear();
    }

    private void handle(MouseEvent event) {
        switch (curTool) {
            case Line:
                if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
                    line = new Line();
                    double startX = event.getX();
                    double startY = event.getY();

                    line.setStartX(startX);
                    line.setStartY(startY);
                    line.setEndX(startX);
                    line.setEndY(startY);
                    line.setStroke(lineColor.getValue());
                    line.setFill(fillColor.getValue());
                    switch (curThick) {
                        case Bold:
                            line.setStrokeWidth(3);
                            break;
                        case Light:
                            line.setStrokeWidth(0.5);
                            break;
                        case Normal:
                            line.setStrokeWidth(1.5);
                            break;
                    }
                    switch (curStyle) {
                        case Dash:
                            line.getStrokeDashArray().addAll(20d, 20d);
                            break;
                        case Dot:
                            line.getStrokeDashArray().addAll(2d, 21d);
                            break;
                        case Solid:
                            line.getStrokeDashArray().addAll(20d, 0d);
                    }
                    this.getChildren().add(line);
                } else if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
                    double deltaX = event.getX();
                    double deltaY = event.getY();
                    if (deltaX < 0) {
                        deltaX = 0;
                    } else if (deltaX > CANVAS_WIDTH) {
                        deltaX = CANVAS_WIDTH;
                    }
                    if (deltaY < 0) {
                        deltaY = 0;
                    } else if (deltaY > CANVAS_HEIGHT) {
                        deltaY = CANVAS_HEIGHT;
                    }
                    line.setEndX(deltaX);
                    line.setEndY(deltaY);
                } else if (event.getEventType() == MouseEvent.MOUSE_RELEASED) {
                    double endX = event.getX();
                    double endY = event.getY();
                    if (endX < 0) {
                        endX = 0;
                    } else if (endX > CANVAS_WIDTH) {
                        endX = CANVAS_WIDTH;
                    }
                    if (endY < 0) {
                        endY = 0;
                    } else if (endY > CANVAS_WIDTH) {
                        endY = CANVAS_WIDTH;
                    }
                    line.setEndX(endX);
                    line.setEndY(endY);

                    Line L = line;
                    L.setOnMouseClicked(new EventHandler <MouseEvent>() {
                        public void handle(MouseEvent event) {
                            switch (curTool) {
                                case Fill:
                                    L.setFill(fillColor.getValue());
                                    break;
                                case Eraser:
                                    pane.getChildren().remove(L);
                                    break;
                                case Select:
                                    pane.getChildren().remove(L);
                                    pane.getChildren().add(L);
                                    curSelected = Selected.Line;
                                    select_line = L;
                                    lineColor.setValue((javafx.scene.paint.Color) L.getStroke());
                                    fillColor.setValue((javafx.scene.paint.Color) L.getFill());
                                    double curX = event.getX();
                                    double curY = event.getY();
                                    double dis = sqrt(pow(curX-L.getEndX(), 2) + pow(curY-L.getEndY(), 2));
                                    if (dis <= 5) {
                                        resize = true;
                                    } else {
                                        resize = false;
                                    }
                                    break;
                            }
                            event.consume();
                        }
                    });

                    L.setOnMouseDragEntered(new EventHandler <MouseEvent>() {
                        public void handle(MouseEvent event) {
                            if(curTool == Model.ToolType.Eraser) {
                                pane.getChildren().remove(L);
                            }
                            event.consume();
                        }
                    });

                    L.setOnMouseDragged(new EventHandler <MouseEvent>() {
                        public void handle(MouseEvent event) {
                            switch (curTool) {
                                case Select:
                                    curSelected = Selected.Line;
                                    select_line = L;

                                    double curX = event.getX();
                                    double curY = event.getY();
                                    double dx = curX - L.getStartX();
                                    double dy = curY - L.getStartY();
                                    double endX = L.getEndX() + dx;
                                    double endY = L.getEndY() + dy;
                                    if (resize) {
                                        L.setEndX(curX);
                                        L.setEndY(curY);
                                    } else {
                                        if (curX >= 0 && curX <= CANVAS_WIDTH && endX >= 0 && endX <= CANVAS_WIDTH) {
                                            L.setStartX(curX);
                                            L.setEndX(endX);
                                        }
                                        if (curY >= 0 && curY <= CANVAS_WIDTH && endY >= 0 && endY <= CANVAS_WIDTH) {
                                            L.setStartY(curY);
                                            L.setEndY(endY);
                                        }
                                    }
                                    start.setLocation(curX, curY);
                                    break;
                            }
                            event.consume();
                        }
                    });

                    L.setOnMouseDragReleased(new EventHandler <MouseEvent>() {
                        public void handle(MouseEvent event) {
                            switch (curTool) {
                                case Select:
                                    curSelected = Selected.Line;
                                    select_line = L;

                                    double curX = event.getX();
                                    double curY = event.getY();
                                    double dx = curX - L.getStartX();
                                    double dy = curY - L.getStartY();
                                    double endX = L.getEndX() + dx;
                                    double endY = L.getEndY() + dy;
                                    if (curX >= 0 && curX<= CANVAS_WIDTH && endX >= 0 && endX <= CANVAS_WIDTH) {
                                        L.setStartX(curX);
                                        L.setEndX(endX);
                                    }
                                    if (curY >= 0 && curY<= CANVAS_WIDTH && endY >= 0 && endY <= CANVAS_WIDTH) {
                                        L.setStartY(curY);
                                        L.setEndY(endY);
                                    }
                                    start.setLocation(curX, curY);
                                    break;
                            }
                            event.consume();
                        }

                    });
                }
                break;
            case Circle:
                if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
                    circle = new Circle();
                    double startX = event.getX();
                    double startY = event.getY();

                    circle.setCenterX(startX);
                    circle.setCenterY(startY);
                    circle.setRadius(0);
                    circle.setStroke(lineColor.getValue());
                    circle.setFill(fillColor.getValue());
                    switch (curThick) {
                        case Bold:
                            circle.setStrokeWidth(3);
                            break;
                        case Light:
                            circle.setStrokeWidth(0.5);
                            break;
                        case Normal:
                            circle.setStrokeWidth(1.5);
                            break;
                    }
                    switch (curStyle) {
                        case Dash:
                            circle.getStrokeDashArray().addAll(20d, 20d);
                            break;
                        case Dot:
                            circle.getStrokeDashArray().addAll(2d, 21d);
                            break;
                        case Solid:
                            circle.getStrokeDashArray().addAll(20d, 0d);
                    }
                    this.getChildren().add(circle);
                } else if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
                    double deltaX = event.getX();
                    double deltaY = event.getY();
                    double x_distance = deltaX - circle.getCenterX();
                    double y_distance = deltaY - circle.getCenterY();
                    double radius = sqrt(pow(x_distance, 2) + pow(y_distance, 2));
                    if (circle.getCenterX() - radius < 0) {
                        radius = circle.getCenterX() - 0;
                    } else if (circle.getCenterX() + radius > CANVAS_WIDTH) {
                        radius = CANVAS_WIDTH - circle.getCenterX();
                    }
                    if (circle.getCenterY() - radius < 0) {
                        radius = circle.getCenterY() - 0;
                    } else if (circle.getCenterY() + radius > CANVAS_HEIGHT) {
                        radius = CANVAS_HEIGHT - circle.getCenterY();
                    }
                    circle.setRadius(radius);
                } else if (event.getEventType() == MouseEvent.MOUSE_RELEASED) {
                    double endX = event.getX();
                    double endY = event.getY();
                    double x_distance = endX - circle.getCenterX();
                    double y_distance = endY - circle.getCenterY();
                    double radius = sqrt(pow(x_distance, 2) + pow(y_distance, 2));
                    if (circle.getCenterX() - radius < 0) {
                        radius = circle.getCenterX() - 0;
                    } else if (circle.getCenterX() + radius > CANVAS_WIDTH) {
                        radius = CANVAS_WIDTH - circle.getCenterX();
                    }
                    if (circle.getCenterY() - radius < 0) {
                        radius = circle.getCenterY() - 0;
                    } else if (circle.getCenterY() + radius > CANVAS_HEIGHT) {
                        radius = CANVAS_HEIGHT - circle.getCenterY();
                    }
                    circle.setRadius(radius);

                    Circle C = circle;
                    C.setOnMouseClicked(new EventHandler <MouseEvent>() {
                        public void handle(MouseEvent event) {
                            switch (curTool) {
                                case Fill:
                                    C.setFill(fillColor.getValue());
                                    break;
                                case Eraser:
                                    pane.getChildren().remove(C);
                                    break;
                                case Select:
                                    pane.getChildren().remove(C);
                                    pane.getChildren().add(C);
                                    curSelected = Selected.Circle;
                                    select_circle = C;
                                    lineColor.setValue((javafx.scene.paint.Color) C.getStroke());
                                    fillColor.setValue((javafx.scene.paint.Color) C.getFill());
                                    start.setLocation(event.getX(), event.getY());
                                    double curX = event.getX();
                                    double curY = event.getY();
                                    double dis = sqrt(pow(curX-C.getCenterX(), 2) + pow(curY-C.getCenterY(), 2));
                                    if ((7/8)*C.getRadius() <= dis && dis <= (9/8)*C.getRadius() && dis >= 5) {
                                        resize = true;
                                    } else {
                                        resize = false;
                                    }
                                    break;
                            }
                            event.consume();
                        }
                    });

                    C.setOnMouseDragEntered(new EventHandler <MouseEvent>() {
                        public void handle(MouseEvent event) {
                            switch (curTool) {
                                case Eraser:
                                    pane.getChildren().remove(C);
                                    break;
                                case Select:
                                    start.setLocation(event.getX(), event.getY());
                                    break;
                            }
                            event.consume();
                        }
                    });

                    C.setOnMouseDragged(new EventHandler <MouseEvent>() {
                        public void handle(MouseEvent event) {
                            switch (curTool) {
                                case Select:
                                    curSelected = Selected.Circle;
                                    select_circle = C;

                                    double curX = event.getX();
                                    double curY = event.getY();
                                    double dis = sqrt(pow(curX-C.getCenterX(), 2) + pow(curY-C.getCenterY(), 2));
                                    if (resize) {
                                        C.setRadius(dis);
                                    } else {
                                        if (curX - C.getRadius() >= 0 && curX + C.getRadius() <= CANVAS_WIDTH) {
                                            C.setCenterX(curX);
                                        }
                                        if (curY - C.getRadius() >= 0 && curY + C.getRadius() <= CANVAS_HEIGHT) {
                                            C.setCenterY(curY);
                                        }
                                    }
                                    start.setLocation(curX, curY);
                                    break;
                            }
                            event.consume();
                        }
                    });

                    C.setOnMouseDragReleased(new EventHandler <MouseEvent>() {
                        public void handle(MouseEvent event) {
                            switch (curTool) {
                                case Select:
                                    double curX = event.getX();
                                    double curY = event.getY();
                                    double dX = curX - start.getX();
                                    double dY = curY - start.getY();
                                    C.setCenterX(C.getCenterX() + dX);
                                    C.setCenterY(C.getCenterY() + dY);
                                    start.setLocation(event.getX(), event.getY());
                            }
                            event.consume();
                        }

                    });

                }
                break;
            case Rect:
                if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
                    rect = new Rectangle();
                    double startX = event.getX();
                    double startY = event.getY();

                    rect.setX(startX);
                    rect.setY(startY);
                    rect.setWidth(0);
                    rect.setHeight(0);
                    rect.setStroke(lineColor.getValue());
                    rect.setFill(fillColor.getValue());
                    switch (curThick) {
                        case Bold:
                            rect.setStrokeWidth(3);
                            break;
                        case Light:
                            rect.setStrokeWidth(0.5);
                            break;
                        case Normal:
                            rect.setStrokeWidth(1.5);
                            break;
                    }
                    switch (curStyle) {
                        case Dash:
                            rect.getStrokeDashArray().addAll(20d, 20d);
                            break;
                        case Dot:
                            rect.getStrokeDashArray().addAll(2d, 21d);
                            break;
                        case Solid:
                            rect.getStrokeDashArray().addAll(20d, 0d);
                    }
                    this.getChildren().add(rect);
                } else if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
                    double deltaX = event.getX();
                    double deltaY = event.getY();
                    if (deltaX < 0) {
                        deltaX = 0;
                    }
                    if (deltaY < 0) {
                        deltaY = 0;
                    }
                    if (deltaX < rect.getX()) {
                        double temp;
                        temp = rect.getX();
                        rect.setX(deltaX);
                        deltaX = temp;
                    }
                    if (deltaY < rect.getY()) {
                        double temp;
                        temp = rect.getY();
                        rect.setY(deltaY);
                        deltaY = temp;
                    }

                    double rect_width = deltaX - rect.getX();
                    double rect_height = deltaY - rect.getY();
                    if (rect.getX() + rect_width > CANVAS_WIDTH) {
                        rect_width = CANVAS_WIDTH - rect.getX();
                    }
                    if (rect.getX() + rect_width > CANVAS_HEIGHT) {
                        rect_height = CANVAS_HEIGHT - rect.getY();
                    }
                    rect.setWidth(rect_width);
                    rect.setHeight(rect_height);
                } else if (event.getEventType() == MouseEvent.MOUSE_RELEASED) {
                    double endX = event.getX();
                    double endY = event.getY();
                    if (endX < 0) {
                        endX = 0;
                    }
                    if (endY < 0) {
                        endY = 0;
                    }
                    if (endX < rect.getX()) {
                        double temp;
                        temp = rect.getX();
                        rect.setX(endX);
                        endX = temp;
                    }
                    if (endY < rect.getY()) {
                        double temp;
                        temp = rect.getY();
                        rect.setY(endY);
                        endY = temp;
                    }

                    double rect_width = endX - rect.getX();
                    double rect_height = endY - rect.getY();
                    if (rect.getX() + rect_width > CANVAS_WIDTH) {
                        rect_width = CANVAS_WIDTH - rect.getX();
                    }
                    if (rect.getX() + rect_width > CANVAS_HEIGHT) {
                        rect_height = CANVAS_HEIGHT - rect.getY();
                    }
                    rect.setWidth(rect_width);
                    rect.setHeight(rect_height);

                    Rectangle R = rect;
                    R.setOnMouseClicked(new EventHandler <MouseEvent>() {
                        public void handle(MouseEvent event) {
                            switch (curTool) {
                                case Fill:
                                    R.setFill(fillColor.getValue());
                                    break;
                                case Eraser:
                                    pane.getChildren().remove(R);
                                    break;
                                case Select:
                                    pane.getChildren().remove(R);
                                    pane.getChildren().add(R);
                                    curSelected = Selected.Rect;
                                    select_rect = R;
                                    lineColor.setValue((javafx.scene.paint.Color) R.getStroke());
                                    fillColor.setValue((javafx.scene.paint.Color) R.getFill());
                                    double curX = event.getX();
                                    double curY = event.getY();
                                    double disCorner = sqrt(pow(R.getHeight(), 2) + pow(R.getWidth(), 2));
                                    double disX = curX - R.getX();
                                    double disY = curY - R.getY();
                                    double dis = sqrt(pow(disX, 2) + pow(disY, 2));
                                    if ((7/8)*disCorner <= dis && dis <= (9/8)*disCorner && dis >= 5) {
                                        resize = true;
                                    } else {
                                        resize = false;
                                    }
                                    break;
                            }
                            event.consume();
                        }
                    });

                    R.setOnMouseDragEntered(new EventHandler <MouseEvent>() {
                        public void handle(MouseEvent event) {
                            switch (curTool) {
                                case Eraser:
                                    pane.getChildren().remove(R);
                                    break;
                                case Select:
                                    start.setLocation(event.getX(), event.getY());
                                    break;
                            }
                            event.consume();
                        }
                    });

                    R.setOnMouseDragged(new EventHandler <MouseEvent>() {
                        public void handle(MouseEvent event) {
                            switch (curTool) {
                                case Select:
                                    curSelected = Selected.Rect;
                                    select_rect = R;

                                    double curX = event.getX();
                                    double curY = event.getY();
                                    if (resize) {
                                        double disX = curX - R.getX();
                                        double disY = curY - R.getY();
                                        R.setWidth(disX);
                                        R.setHeight(disY);
                                    } else {
                                        if (curX >= 0 && curX + R.getWidth() <= CANVAS_WIDTH) {
                                            R.setX(curX);
                                        }
                                        if (curY >= 0 && curY + R.getHeight() <= CANVAS_HEIGHT) {
                                            R.setY(curY);
                                        }
                                    }
                                    start.setLocation(curX, curY);
                                    break;
                            }
                            event.consume();
                        }
                    });

                    R.setOnMouseDragReleased(new EventHandler <MouseEvent>() {
                        public void handle(MouseEvent event) {
                            switch (curTool) {
                                case Select:
                                    curSelected = Selected.Rect;
                                    select_rect = R;

                                    double curX = event.getX();
                                    double curY = event.getY();
                                    double dx = curX - start.getX();
                                    double dy = curY  - start.getY();
                                    if (R.getX()+dx >= 0 && R.getX()+dx <= CANVAS_WIDTH) {
                                        R.setX(R.getX()+dx);
                                    }
                                    if (R.getY()+dy >= 0 && R.getY()+dy <= CANVAS_WIDTH) {
                                        R.setY(R.getY()+dy);
                                    }
                                    start.setLocation(curX, curY);
                                    break;
                            }
                            event.consume();
                        }

                    });

                }
                break;
        }
    }
}
