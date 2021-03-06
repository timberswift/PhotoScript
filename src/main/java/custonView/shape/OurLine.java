package custonView.shape;


import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Line;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;


public class OurLine extends Line {
    //直线的初始坐标
    private DoubleProperty x = new SimpleDoubleProperty(450);
    private DoubleProperty y = new SimpleDoubleProperty(200);

    private DoubleProperty x1 = new SimpleDoubleProperty(0);
    private DoubleProperty y1 = new SimpleDoubleProperty(0);
    private DoubleProperty x2 = new SimpleDoubleProperty(100);
    private DoubleProperty y2 = new SimpleDoubleProperty(0);


    private double deltaX, deltaY;

    private enum POINT {L, R, C};
    private POINT cursor;


    public OurLine(){
        super(10, 10, 30, 30);
        init();
    }


    private void init(){
        setStrokeWidth(3);
        endXProperty().bind(x1);
        endYProperty().bind(y1);
        startXProperty().bind(x2);
        startYProperty().bind(y2);
        layoutXProperty().bind(x);
        layoutYProperty().bind(y);

        setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                switchDrag(event);
            }
        });

        setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                switchPoint(event);
            }
        });

        setOnMousePressed(event -> {
            requestFocus();
            deltaX = event.getX();
            deltaY = event.getY();
        });



    }

    private void switchDrag(MouseEvent event){
        requestFocus();
        double dragX = event.getSceneX();
        double dragY = event.getSceneY();
        switch(cursor){
            case C:
                x.set(event.getSceneX()- deltaX - getParent().getLayoutX());
                y.set(event.getSceneY()- deltaY - getParent().getLayoutY());
                break;
            case L:
                x1.set(event.getX());
                y1.set(event.getY());
                break;
            case R:
                x2.set(event.getX());
                y2.set(event.getY());
                break;
        }

    }

    private void switchPoint(MouseEvent event){
        double tempX = event.getX();
        double tempY = event.getY();
        cursor = POINT.C;
        if((tempX - x1.getValue()) < 10 && (tempY - y1.getValue()) < 10){
            cursor = POINT.L;
            setCursor(Cursor.W_RESIZE);
            return ;
        }else if((x2.getValue()-tempX) < 10 && (y2.getValue() - tempY) < 10) {
            setCursor(Cursor.E_RESIZE);
            cursor = POINT.R;
            return ;
        }
        setCursor(Cursor.HAND);
    }

}

