package starter.graphical;

import java.util.ArrayList;

public class Model {

    enum ToolType {
        Select,
        Eraser,
        Line,
        Rect,
        Circle,
        Fill,
        None
    }

    enum Thickness {
        Light,
        Normal,
        Bold
    }

    enum LineStyle {
        Solid,
        Dash,
        Dot
    }

    static ToolType curTool = ToolType.None;
    static Thickness curThick = Thickness.Normal;
    static LineStyle curStyle = LineStyle.Solid;

    // all views of this model
    private ArrayList<IView> views = new ArrayList<IView>();

    // method that the views can use to register themselves with the Model
    // once added, they are told to update and get state from the Model
    public void addView(IView view) {
        views.add(view);
        view.updateView();
    }

    // the model uses this method to notify all of the Views that the data has changed
    // the expectation is that the Views will refresh themselves to display new data when appropriate
    private void notifyObservers() {
        for (IView view : this.views) {
            System.out.println("Model: notify View");
            view.updateView();
        }
    }
}
