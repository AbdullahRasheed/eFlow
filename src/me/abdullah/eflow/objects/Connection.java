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

public class Connection {

    private boolean selected = false;

    private Point point;
    private Circle backgroundCircle;
    private Circle fillCircle;
    private Circle overlayCircle;
    public Connection(Point point){
        this.point = point;

        Text label = point.getLabel();
        AnchorPane pane = point.getPane();

        double x = centerX(label);
        double y = centerY();
        Circle circle = new Circle(x, y, 10);
        Circle fill = new Circle(x, y, circle.getRadius() - 1, Color.WHITE);
        Circle overlay = new Circle(x, y, 10, Color.TRANSPARENT);
        overlay.setOnMouseEntered(getMouseEnteredEvent(fill, circle));
        overlay.setOnMouseExited(getMouseExitedEvent(fill, circle));
        overlay.setOnMousePressed(getOnMousePressed());
        this.backgroundCircle = circle;
        this.fillCircle = fill;
        this.overlayCircle = overlay;

        pane.getChildren().add(circle);
        pane.getChildren().add(fill);
        pane.getChildren().add(overlay);
    }

    private double centerX(Text label){
        return label.getLayoutX() + point.getSize().getWidth() + 30;
    }

    private double centerY(){
        return point.getSize().getMinY() + point.getSize().getHeight()/2;
    }

    public Point getPoint(){
        return point;
    }

    private EventHandler<MouseEvent> getMouseEnteredEvent(Circle fill, Circle circle){
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                new Transition() {
                    {
                        setCycleDuration(Duration.seconds(0.1));
                        setInterpolator(Interpolator.EASE_IN);
                    }

                    double radius = fill.getRadius();
                    @Override
                    protected void interpolate(double frac) {
                        if(isSelected()) return;
                        double fillRadius = (1-frac) * radius;
                        fill.setRadius(fillRadius);
                    }
                }.play();
            }
        };
    }

    private EventHandler<MouseEvent> getMouseExitedEvent(Circle fill, Circle circle){
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                new Transition() {
                    {
                        setCycleDuration(Duration.seconds(0.1));
                        setInterpolator(Interpolator.EASE_IN);
                    }
                    double radius = fill.getRadius();
                    @Override
                    protected void interpolate(double frac) {
                        if(isSelected()) return;
                        double fillRadius = frac * (circle.getRadius() - 1 - radius);
                        fill.setRadius(radius + fillRadius);
                    }
                }.play();
            }
        };
    }

    private EventHandler<MouseEvent> getOnMousePressed(){
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                for (Point p : point.getColumn().getPoints()) {
                    p.getConnection().setSelected(false);
                }
                setSelected(true);
            }
        };
    }

    public void setSelected(boolean b){
        this.selected = b;
        if(b) fillCircle.setRadius(0);
        else {
            fillCircle.setRadius(overlayCircle.getRadius()-1);
            return;
        }

        for (Column column : Controller.getColumns()) {
            for (Point p : column.getPoints()) {
                if(p.equals(this.getPoint())) continue;
                if(p.getConnection().isSelected()) {
                    connect(p.getConnection());
                }
            }
        }
    }

    public void connect(Connection c){
        double startX = this.overlayCircle.getCenterX();
        double startY = this.overlayCircle.getCenterY();
        double endX = c.getOverlayCircle().getCenterX();
        double endY = c.getOverlayCircle().getCenterY();
        double x1 = (endX - startX)/2 + startX;
        double y1 = (endY - startY)/4 + startY;
        double x2 = x1;
        double y2 = y1 + (endY - startY)/4;
        CubicCurve curve = new CubicCurve(startX, startY, x1, y1, x2, y2, endX, endY);
        curve.setStrokeWidth(1);
        curve.setStroke(Color.BLACK);
        curve.setFill(null);
        point.getPane().getChildren().add(curve);

        c.setSelected(false);
        setSelected(false);
    }

    public Circle getOverlayCircle(){
        return overlayCircle;
    }

    public boolean isSelected(){
        return selected;
    }

    public void updatePositions(){
        Text label = point.getLabel();
        double x = centerX(label);
        double y = centerY();
        backgroundCircle.setCenterX(x);
        backgroundCircle.setCenterY(y);

        overlayCircle.setCenterX(x);
        overlayCircle.setCenterY(y);

        fillCircle.setCenterX(x);
        fillCircle.setCenterY(y);
    }

    public void setVisibile(boolean b){
        backgroundCircle.setVisible(b);
        overlayCircle.setVisible(b);
        fillCircle.setVisible(b);
    }
}
