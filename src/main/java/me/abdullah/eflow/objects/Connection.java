package me.abdullah.eflow.objects;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurve;
import javafx.scene.text.Text;
import javafx.util.Duration;
import me.abdullah.eflow.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Connection {

    // Whether or not the connection is selected
    private boolean selected = false;

    // List of shared connections
    private Map<Connection, CubicCurve> connections = new HashMap<>();

    private Point point;
    private Circle backgroundCircle;
    private Circle fillCircle;
    private Circle overlayCircle;

    /**
     * Initializes the point's connection
     * @param point The point to add a connection to
     */
    public Connection(Point point){
        this.point = point;

        // Using the point's information
        Text label = point.getLabel();
        AnchorPane pane = point.getPane();

        // Defining the circles
        double x = centerX(label);
        double y = centerY();
        Circle circle = new Circle(x, y, 10);
        Circle fill = new Circle(x, y, circle.getRadius() - 1, Color.WHITE);
        Circle overlay = new Circle(x, y, 10, Color.TRANSPARENT);

        // Animations
        overlay.setOnMouseEntered(getMouseEnteredEvent(fill, circle));
        overlay.setOnMouseExited(getMouseExitedEvent(fill, circle));
        overlay.setOnMousePressed(getOnMousePressed());

        // Defining class variables
        this.backgroundCircle = circle;
        this.fillCircle = fill;
        this.overlayCircle = overlay;

        //Adding the connection to the pane
        pane.getChildren().add(circle);
        pane.getChildren().add(fill);
        pane.getChildren().add(overlay);
    }

    /**
     * What the center X of the circles should be at any given point
     * @param label Text to base off of (the point)
     * @return The calculated center X
     */
    private double centerX(Text label){
        return label.getLayoutX() + point.getSize().getWidth() + 30;
    }

    /**
     * What the center Y of the circles should be at any given point
     * @return The calculated center Y
     */
    private double centerY(){
        return point.getSize().getMinY() + point.getSize().getHeight()/2;
    }

    /**
     * @return The connection's point
     */
    public Point getPoint(){
        return point;
    }

    public Map<Connection, CubicCurve> getConnections(){
        return connections;
    }

    /**
     * Mouse entered animation
     * @param fill Hollow circle
     * @param circle Main circle
     * @return The EventHandler to for the MouseEnteredEvent
     */
    private EventHandler<MouseEvent> getMouseEnteredEvent(Circle fill, Circle circle){
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                new Transition() {
                    {
                        // 0.1 second animation
                        setCycleDuration(Duration.seconds(0.1));
                        // Rate of change of 'frac' is negative
                        setInterpolator(Interpolator.EASE_IN);
                    }

                    double radius = fill.getRadius();
                    @Override
                    protected void interpolate(double frac) {
                        // Don't animate if it is selected
                        if(isSelected()) return;
                        // Goes from initial radius to 0
                        double fillRadius = (1-frac) * radius;
                        fill.setRadius(fillRadius);
                    }
                }.play();
            }
        };
    }

    /**
     * Mouse exited animation
     * @param fill Hollow circle
     * @param circle Main circle
     * @return The EventHandler to for the MouseExitedEvent
     */
    private EventHandler<MouseEvent> getMouseExitedEvent(Circle fill, Circle circle){
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                new Transition() {
                    {
                        // 0.1 second animation
                        setCycleDuration(Duration.seconds(0.1));
                        // Rate of change of 'frac' is negative
                        setInterpolator(Interpolator.EASE_IN);
                    }
                    double radius = fill.getRadius();
                    @Override
                    protected void interpolate(double frac) {
                        // Don't animate if it is selected
                        if(isSelected()) return;
                        // Goes from initial radius to main circle radius - 1
                        double fillRadius = frac * (circle.getRadius() - 1 - radius);
                        fill.setRadius(radius + fillRadius);
                    }
                }.play();
            }
        };
    }

    /**
     * Mouse Pressed Event
     * @return EventHandler to run on MousePressed
     */
    private EventHandler<MouseEvent> getOnMousePressed(){
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                // Unselects any other connection in the given column (can't flow two points from the same speech)
                for (Point p : point.getColumn().getPoints()) {
                    p.getConnection().setSelected(false);
                }
                // Selects this point
                setSelected(true);
            }
        };
    }

    /**
     * Selects or Deselects this connection
     * @param b Select/Deselect
     */
    public void setSelected(boolean b){
        // Sets selection state
        this.selected = b;
        // If selected, fill in the circle, otherwise make it hollow again
        if(b) fillCircle.setRadius(0);
        else {
            fillCircle.setRadius(overlayCircle.getRadius()-1);
            return;
        }

        // Checks for a selected connection from another column to flow to
        for (Column column : Controller.getCurrentPage().getColumns()) {
            for (Point p : column.getPoints()) {
                if(p.equals(this.getPoint())) continue;
                if(p.getConnection().isSelected()) {
                    connect(p.getConnection());
                }
            }
        }
    }

    /**
     * Connects this connection with another connection
     * @param c Connection to flow to
     */
    public void connect(Connection c){
        // Checks for existing connections
        if(getConnections().containsKey(c)){
            getPoint().getPane().getChildren().remove(getConnections().get(c));
            getConnections().remove(c);
            c.getConnections().remove(this);

            c.setSelected(false);
            setSelected(false);
            return;
        }

        // Defining pivot points for flow
        Connection left = c.getPoint().getLabel().getLayoutX() > this.getPoint().getLabel().getLayoutX() ? this : c;
        Connection right = left == this ? c : this;
        double startX = right.getPoint().getLabel().getLayoutX() - 5;
        double startY = right.getOverlayCircle().getCenterY();
        double endX = left.getOverlayCircle().getCenterX();
        double endY = left.getOverlayCircle().getCenterY();
        double x1 = (endX - startX)/2 + startX;
        double y1 = startY;
        double x2 = x1;
        double y2 = endY;
        // Defining the curve's attributes
        CubicCurve curve = new CubicCurve(startX, startY, x1, y1, x2, y2, endX, endY);
        curve.setStrokeWidth(1);
        curve.setStroke(Color.BLACK);
        curve.setFill(null);
        // Adds the curve
        point.getPane().getChildren().add(curve);

        // Stores the connection in the points
        getConnections().put(c, curve);
        c.getConnections().put(this, curve);

        // Deselects the two points
        c.setSelected(false);
        setSelected(false);
    }

    public void certainConnect(Connection c){
        // Defining pivot points for flow
        Connection left = c.getPoint().getLabel().getLayoutX() > this.getPoint().getLabel().getLayoutX() ? this : c;
        Connection right = left == this ? c : this;
        double startX = right.getPoint().getLabel().getLayoutX() - 5;
        double startY = right.getOverlayCircle().getCenterY();
        double endX = left.getOverlayCircle().getCenterX();
        double endY = left.getOverlayCircle().getCenterY();
        double x1 = (endX - startX)/2 + startX;
        double y1 = startY;
        double x2 = x1;
        double y2 = endY;

        if(!getConnections().containsKey(c)) {
            CubicCurve curve = new CubicCurve(startX, startY, x1, y1, x2, y2, endX, endY);
            curve.setStrokeWidth(1);
            curve.setStroke(Color.BLACK);
            curve.setFill(null);

            point.getPane().getChildren().add(curve);

            // Stores the connection in the points
            getConnections().put(c, curve);
            c.getConnections().put(this, curve);
        }

        // Deselects the two points
        c.setSelected(false);
        setSelected(false);
    }

    /**
     * @return The overlay circle, used for events
     */
    public Circle getOverlayCircle(){
        return overlayCircle;
    }

    /**
     * @return If this connection is selected
     */
    public boolean isSelected(){
        return selected;
    }

    /**
     * Updates the position of the connection
     */
    public void updatePositions(){
        // Defining label and centerX/centerY
        Text label = point.getLabel();
        double x = centerX(label);
        double y = centerY();

        // Setting new positions
        backgroundCircle.setCenterX(x);
        backgroundCircle.setCenterY(y);

        overlayCircle.setCenterX(x);
        overlayCircle.setCenterY(y);

        fillCircle.setCenterX(x);
        fillCircle.setCenterY(y);
    }

    /**
     * Changes the visibility of the connection
     * @param b Visible/Invisible
     */
    public void setVisibile(boolean b){
        // Changing visibility of the individual circles
        backgroundCircle.setVisible(b);
        overlayCircle.setVisible(b);
        fillCircle.setVisible(b);
    }

    List<int[]> queueList = new ArrayList<>();
    public void queueConnection(int[] coords){
        queueList.add(coords);
    }

    public void runQueues(){
        List<Column> columns = point.getColumn().getPage().getColumns();
        for (int[] coords : queueList) {
            certainConnect(columns.get(coords[0]).getPoints().get(coords[1]).getConnection());
        }
        queueList.clear();
    }
}
