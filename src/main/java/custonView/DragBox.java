package custonView;/**
 * Created by FANGs on 2017/9/19.
 */

import custonView.shape.Circle;
import custonView.shape.Ellipse;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Bounds;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;

import java.io.Serializable;


public class DragBox extends Pane implements Serializable{

    private POINT cursor;
    private Node node;
    private Canvas canvas;
    private boolean isShitDown = false;
    private double mouseDragX,mouseDragY;
    private boolean isMousePress = false;
    private double miniLen = 21;
    private MainPane.requestChoose chooseListener;


    //leftTop,top,rightTop......Bottom,rightBottom,others
    private enum POINT {LT,T,RT,L,R,LB,B,RB,OT}

    //this view's position
    private DoubleProperty X = new SimpleDoubleProperty(450);
    private DoubleProperty Y = new SimpleDoubleProperty(200);
    private double deltaX,deltaY;

    //this view's height and width
    public DoubleProperty Width = new SimpleDoubleProperty(0);
    public DoubleProperty Height = new SimpleDoubleProperty(0);
    public DoubleProperty Radius = new SimpleDoubleProperty(0);

    public DragBox() {
        Width.set(100);
        Height.set(100);
        init();
    }

    private POINT switchPoint(MouseEvent event){

        double x = event.getX();
        double y = event.getY();
        double height = getPrefHeight();
        double width = getPrefWidth();

        cursor = POINT.OT;
        if(y < 10) {
            if (x < 10) cursor = POINT.LT;
            else if (x > width-10) cursor = POINT.RT;
            else cursor = POINT.T;
            return cursor;
        }else if(y > height-10){
            if(x < 10) cursor = POINT.LB;
            else if(x > width-10) cursor = POINT.RB;
            else cursor = POINT.B;
            return cursor;
        } else if(x < 10) cursor = POINT.L;
        else if(x > width-10) cursor = POINT.R;
        return cursor;
    }

    private void updateConner(){
        if(!isMousePress) return;
        double min;
        switch (cursor){
            case LT:
                double tempLTX = getLayoutX() - mouseDragX + getParent().getLayoutX();
                double tempLTY = getLayoutY() - mouseDragY + getParent().getLayoutY();
                if(isShitDown){
                    isShitDown = true;
                    if(getParent().getLayoutX() + tempLTX < tempLTY){
                        X.set(X.getValue() - tempLTX -getParent().getLayoutX());
                        Y.set(Y.getValue() - tempLTX -getParent().getLayoutX());
                        setPrefHeight(tempLTX + getPrefWidth());
                        setPrefWidth(tempLTX + getPrefWidth());
                    }else{
                        X.set(X.getValue() - tempLTY);
                        Y.set(Y.getValue() - tempLTY);
                        setPrefHeight(getPrefHeight() + tempLTY);
                        setPrefWidth(getPrefHeight() + tempLTY);
                    }

                }else{
                    X.set(X.getValue() - tempLTX -getParent().getLayoutX());
                    Y.set(Y.getValue() - tempLTY);
                    setPrefHeight(getPrefHeight() + tempLTY);
                    setPrefWidth(tempLTX + getPrefWidth());
                }
                setCursor(Cursor.NW_RESIZE);
                break;
            case T:
                double tempTY = getLayoutY() - mouseDragY + getParent().getLayoutY();
                Y.set(Y.getValue() - tempTY);
                setPrefHeight(getPrefHeight() + tempTY);

                if(isShitDown){
                    isShitDown = true;
                    setPrefWidth(getPrefHeight() + tempTY);
                }
                setCursor(Cursor.N_RESIZE);
                break;
            case RT:
                double tempRTX = mouseDragX - getLayoutX() - getParent().getLayoutX();
                double tempRTY = getLayoutY() - mouseDragY + getParent().getLayoutY();

                Y.set(Y.getValue() - tempRTY);

                if(isShitDown){
                    isShitDown = true;
                    setPrefHeight(getPrefHeight() + tempRTY);
                    setPrefWidth(getPrefHeight() + tempRTY);
                }else{
                    setPrefWidth(tempRTX);
                    setPrefHeight(getPrefHeight() + tempRTY);
                }
                setCursor(Cursor.NE_RESIZE);
                break;
            case L:
                double tempLX = getLayoutX() - mouseDragX + getParent().getLayoutX();


                if(isShitDown){
                    isShitDown = true;
                    X.set(X.getValue() - tempLX);
                    setPrefWidth(tempLX + getPrefWidth());
                    setPrefHeight(tempLX + getPrefWidth());
                }else{
                    X.set(X.getValue() - tempLX);
                    setPrefWidth(tempLX + getPrefWidth());
                }
                setCursor(Cursor.W_RESIZE);
                break;
            case R:
                double tempRX = mouseDragX - getLayoutX() - getParent().getLayoutX();
                setPrefWidth(tempRX);

                if(isShitDown){
                    isShitDown = true;
                    setPrefHeight(tempRX);
                }
                setCursor(Cursor.E_RESIZE);
                break;
            case LB:
                double tempLBY = mouseDragY - getLayoutY()- getParent().getLayoutY();

                double tempLBX = getLayoutX() - mouseDragX - getParent().getLayoutX();
                X.set(X.getValue() - tempLBX);

                if(isShitDown){
                    isShitDown = true;
                    setPrefWidth(tempLBX + getPrefWidth());
                    setPrefHeight(tempLBX + getPrefWidth());
                }else{
                    setPrefHeight(tempLBY);
                    setPrefWidth(tempLBX + getPrefWidth());
                }

                setCursor(Cursor.SW_RESIZE);
                break;
            case B:
                double tempBY = mouseDragY - getLayoutY()- getParent().getLayoutY();
                setPrefHeight(tempBY);

                if(isShitDown){
                    isShitDown = true;
                    setPrefWidth(tempBY);
                }
                setCursor(Cursor.S_RESIZE);
                break;
            case RB:
                double tempX = mouseDragX - getLayoutX() - getParent().getLayoutX();
                double tempY = mouseDragY - getLayoutY() - getParent().getLayoutY();

                if(isShitDown){
                    min = Math.min(tempX,tempY);
                    isShitDown = true;
                    setPrefWidth(min);
                    setPrefHeight(min);
                }else {
                    setPrefWidth(tempX);
                    setPrefHeight(tempY);
                }
                setCursor(Cursor.SE_RESIZE);
                break;
            default:
                X.set(mouseDragX-deltaX-getParent().getLayoutX());
                Y.set(mouseDragY-deltaY-getParent().getLayoutY());
                break;
        }
    }

    private void switchDrag(MouseEvent event){
        mouseDragX = event.getSceneX();
        mouseDragY = event.getSceneY();
        double min;
        switch (cursor){
            case LT:
                double tempLTX = getLayoutX() - event.getSceneX() + getParent().getLayoutX();
                double tempLTY = getLayoutY() - event.getSceneY() + getParent().getLayoutY();

                if(event.isShiftDown()){
                    isShitDown = true;
                    if(getParent().getLayoutX() + tempLTX < tempLTY){
                        if(tempLTX + getPrefWidth() < miniLen){
                            X.set(X.getValue() + getPrefWidth() - miniLen);
                            Y.set(Y.getValue() + getPrefWidth() - miniLen);
                            setPrefHeight(miniLen);
                            setPrefWidth(miniLen);
                        }else{
                            X.set(X.getValue() - tempLTX);
                            Y.set(Y.getValue() - tempLTX);
                            setPrefHeight(tempLTX + getPrefWidth());
                            setPrefWidth(tempLTX + getPrefWidth());
                        }
                    }else{
                        if(getPrefHeight() + tempLTY < miniLen){
                            X.set(X.getValue() + getPrefHeight() - miniLen);
                            Y.set(Y.getValue() + getPrefHeight() - miniLen);
                            setPrefHeight(miniLen);
                            setPrefWidth(miniLen);
                        }else{
                            setPrefHeight(getPrefHeight() + tempLTY);
                            Y.set(Y.getValue() - tempLTY);
                            setPrefWidth(getPrefWidth() + tempLTY);
                            X.set(X.getValue() - tempLTY);
                        }
                    }

                }else{
                    if(tempLTX + getPrefWidth() < miniLen){
                        X.set(X.getValue() + getPrefWidth() - miniLen);
                        setPrefWidth(miniLen);
                    }else{
                        X.set(X.getValue() - tempLTX);
                        setPrefWidth(tempLTX + getPrefWidth());
                    }
                    if(getPrefHeight() + tempLTY < miniLen){
                        Y.set(Y.getValue() +getPrefHeight() - miniLen);
                        setPrefHeight(miniLen);
                    }else {
                        Y.set(Y.getValue() - tempLTY);
                        setPrefHeight(getPrefHeight() + tempLTY);
                    }
                }
                setCursor(Cursor.NW_RESIZE);
                break;
            case T:
                double tempTY = getLayoutY() - event.getSceneY() + getParent().getLayoutY();

                if(getPrefHeight() + tempTY < miniLen){
                    Y.set(Y.getValue() + getPrefHeight() - miniLen);
                    setPrefHeight(miniLen);
                }else{
                    Y.set(Y.getValue() - tempTY);
                    setPrefHeight(getPrefHeight() + tempTY);
                }

                if(event.isShiftDown()){
                    isShitDown = true;
                    setPrefWidth(keepMiniLen(getPrefHeight() + tempTY));
                }

                setCursor(Cursor.N_RESIZE);
                break;
            case RT:
                double tempRTX = event.getSceneX() - getLayoutX() - getParent().getLayoutX();
                double tempRTY = getLayoutY() - event.getSceneY() + getParent().getLayoutY();


                if(event.isShiftDown()){
                    isShitDown = true;
                    if(getPrefHeight() + tempRTY < miniLen){
                        Y.set(Y.getValue() +(getPrefHeight() - miniLen));
                        setPrefHeight(miniLen);
                        setPrefWidth(miniLen);
                    }else {
                        Y.set(Y.getValue() - tempRTY);
                        setPrefHeight(getPrefHeight() + tempRTY);
                        setPrefWidth(getPrefHeight() + tempRTY);
                    }
                }else{
                    setPrefWidth(keepMiniLen(tempRTX));
                    if(getPrefHeight() + tempRTY < miniLen){
                        Y.set(Y.getValue() +(getPrefHeight() - miniLen));
                        setPrefHeight(miniLen);
                    }else {
                        Y.set(Y.getValue() - tempRTY);
                        setPrefHeight(getPrefHeight() + tempRTY);
                    }
                }
                setCursor(Cursor.NE_RESIZE);
                break;
            case L:
                double tempLX = getLayoutX() - event.getSceneX() + getParent().getLayoutX();


                if(event.isShiftDown()){
                    isShitDown = true;

                    if(tempLX + getPrefWidth() < miniLen){
                        X.set(X.getValue() + (getPrefWidth()-miniLen));
                        setPrefWidth(miniLen);
                        setPrefHeight(miniLen);

                    }else{
                        X.set(X.getValue() - tempLX);
                        setPrefWidth(tempLX + getPrefWidth());
                        setPrefHeight(tempLX + getPrefWidth());
                    }
                }else{
                    if(tempLX + getPrefWidth() < miniLen){
                        X.set(X.getValue() + (getPrefWidth()-miniLen));
                        setPrefWidth(miniLen);
                    }else{
                        X.set(X.getValue() - tempLX);
                        setPrefWidth(tempLX + getPrefWidth());
                    }
                }
                setCursor(Cursor.W_RESIZE);
                break;
            case R:
                double tempRX = event.getSceneX() - getLayoutX() - getParent().getLayoutX();
                setPrefWidth(keepMiniLen(tempRX));

                if(event.isShiftDown()){
                    isShitDown = true;
                    setPrefHeight(keepMiniLen(tempRX));
                }
                setCursor(Cursor.E_RESIZE);
                break;
            case LB:
                double tempLBY = event.getSceneY() - getLayoutY()- getParent().getLayoutY();
                double tempLBX = getLayoutX() - event.getSceneX() - getParent().getLayoutX();

                if(event.isShiftDown()){
                    isShitDown = true;

                    if(tempLBX + getPrefWidth() < miniLen){
                        X.set(X.getValue() + (getPrefWidth()-miniLen));
                        setPrefWidth(miniLen);
                        setPrefWidth(miniLen);
                    }else{
                        X.set(X.getValue() - tempLBX);
                        setPrefWidth(tempLBX + getPrefWidth());
                        setPrefHeight(tempLBX + getPrefWidth());
                    }
                }else{
                    setPrefHeight(keepMiniLen(tempLBY));
                    if(tempLBX + getPrefWidth() < miniLen){
                        X.set(X.getValue() + (getPrefWidth()-miniLen));
                        setPrefWidth(miniLen);
                    }else{
                        X.set(X.getValue() - tempLBX);
                        setPrefWidth(tempLBX + getPrefWidth());
                    }
                }

                setCursor(Cursor.SW_RESIZE);
                break;
            case B:
                double tempBY = event.getSceneY() - getLayoutY()- getParent().getLayoutY();
                setPrefHeight(keepMiniLen(tempBY));

                if(isShitDown){
                    isShitDown = true;
                    setPrefWidth(keepMiniLen(tempBY));
                }
                setCursor(Cursor.S_RESIZE);
                break;
            case RB:
                double tempX = event.getSceneX() - getLayoutX() - getParent().getLayoutX();
                double tempY = event.getSceneY() - getLayoutY() - getParent().getLayoutY();

                if(event.isShiftDown()){
                    min = Math.min(tempX,tempY);
                    isShitDown = true;
                    setPrefWidth(keepMiniLen(min));
                    setPrefHeight(keepMiniLen(min));
                }else {
                    setPrefWidth(keepMiniLen(tempX));
                    setPrefHeight(keepMiniLen(tempY));
                }
                setCursor(Cursor.SE_RESIZE);
                break;
            default:
                X.set(event.getSceneX()-deltaX-getParent().getLayoutX());
                Y.set(event.getSceneY()-deltaY-getParent().getLayoutY());
                setCursor(Cursor.CLOSED_HAND);
                break;
        }
    }

    private double keepMiniLen(double l){
        if(l < miniLen) {
            l = miniLen;
        }
        return l;
    }

    private void setCursor(MouseEvent event){
        switch (switchPoint(event)){
            case LT:
                setCursor(Cursor.NW_RESIZE);
                break;
            case T:
                setCursor(Cursor.N_RESIZE);
                break;
            case RT:
                setCursor(Cursor.NE_RESIZE);
                break;
            case L:
                setCursor(Cursor.W_RESIZE);
                break;
            case R:
                setCursor(Cursor.E_RESIZE);
                break;
            case LB:
                setCursor(Cursor.SW_RESIZE);
                break;
            case B:
                setCursor(Cursor.S_RESIZE);
                break;
            case RB:
                setCursor(Cursor.SE_RESIZE);
                break;
            default:
                setCursor(Cursor.HAND);
                break;
        }
    }

    public void setContentNode(Node node,OnBuildListener onBuildListener){
        if(onBuildListener!=null){
            onBuildListener.onBuild(node,this);
        }
        getChildren().add(node);
        this.node = node;


    }

    public void setSvgNode(String path,Paint fill,Paint stroke){
        this.node = new SVGPath();
        ((SVGPath)node).setContent(path);
        ((SVGPath)node).setFill(fill);
        ((SVGPath)node).setStroke(stroke);
        ((SVGPath)node).setStrokeWidth(5);
        ((SVGPath)node).setSmooth(true);

        Bounds bounds = node.boundsInLocalProperty().getValue();

        final double height = bounds.getHeight();
        final double width = bounds.getWidth();
        double S = 20;

        setPrefWidth(width+S);
        setPrefHeight(height+S);

        node.scaleYProperty().bind(heightProperty().
                subtract(S).
                divide(height));
        node.scaleXProperty().bind(widthProperty().
                subtract(S).
                divide(width));

        node.layoutXProperty().bind(widthProperty().
                subtract(S).
                divide(2).
                subtract(width/2).
                add(S/2).
                subtract(bounds.getMinX()));
        node.layoutYProperty().bind(heightProperty().
                subtract(S).
                divide(2).
                subtract(height/2).
                add(S/2)
                .subtract(bounds.getMinY()));
        getChildren().add(node);


    }

    public interface OnBuildListener{
        void onBuild(Node node, DragBox Parent);
    }

    private void init(){

        setPrefWidth(Width.get());
        setPrefHeight(Height.get());
        layoutXProperty().bind(X);
        layoutYProperty().bind(Y);

        Background background = new Background(new BackgroundFill(Paint.valueOf("#300"),null,null));
//        setBackground(background);


        layoutBoundsProperty().addListener((observable, oldValue, newValue) -> {
            double h = heightProperty().getValue();
            double w = widthProperty().getValue();

            Width.set(w);
            Height.set(h);
            Radius.set(Math.min(h/2,w/2));
        });


        setOnMouseMoved(this::setCursor);


        setOnMousePressed(event -> {
            if(chooseListener != null){
                chooseListener.request(this);
            }
            drawConner();
            deltaX = event.getX();
            deltaY = event.getY();
            isMousePress = true;
        });


        setOnMouseDragged(this::switchDrag);

        setOnMouseReleased(event -> {
            isMousePress = false;
            event.consume();
            setCursor(event);
        });

        canvas = new Canvas();

        canvas.heightProperty().addListener(observable -> {
            clearConner();
            drawConner();
        });
        canvas.widthProperty().addListener(observable -> {
            clearConner();
            drawConner();
        });
        canvas.widthProperty().bind(Width);
        canvas.heightProperty().bind(Height);
        getChildren().add(canvas);

        setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.SHIFT){
                isShitDown = true;
                updateConner();
            }
        });

        setOnKeyReleased(event -> {
            if(event.getCode() == KeyCode.SHIFT){
                isShitDown = false;
                updateConner();
            }
        });
    }

    private void drawConner(){
        double height = getPrefHeight();
        double width = getPrefWidth();
        GraphicsContext gc = canvas.getGraphicsContext2D();
        double whiteR = 12;
        double blueR = 10;
        double delta = (whiteR-blueR)/2;
        double lineStart = whiteR/2;

        gc.setStroke(Color.GREEN);
        gc.setLineWidth(2);

        gc.strokeLine(lineStart, lineStart, width-lineStart, lineStart);
        gc.strokeLine(lineStart, height-lineStart, width-lineStart, height-lineStart);
        gc.strokeLine(lineStart, lineStart, lineStart, height-lineStart);
        gc.strokeLine(width-lineStart, lineStart, width-lineStart, height-lineStart);


        gc.setFill(Color.WHITE);
        gc.fillOval(0,0,whiteR,whiteR);
        gc.fillOval(width-whiteR,0,whiteR,whiteR);
        gc.fillOval(0,height-whiteR,whiteR,whiteR);
        gc.fillOval(width-whiteR,height-whiteR, whiteR,whiteR);

        gc.setFill(Color.color(0.03,0.43,0.81));
        gc.fillOval(delta,delta,blueR,blueR);
        gc.fillOval(width-blueR-delta,delta,blueR,blueR);
        gc.fillOval(delta,height-blueR-delta,blueR,blueR);
        gc.fillOval(width-blueR-delta,height-blueR-delta,blueR,blueR);
    }

    private void clearConner(){
        double height = getPrefHeight();
        double width = getPrefWidth();
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0,0,width,height);
    }

    public void setNotChoosen(){
        clearConner();
    }
    public void setChoosen(){
        drawConner();
    }

    public void setChooseListener(MainPane.requestChoose listener){
        this.chooseListener = listener;
        this.chooseListener.request(this);
    }

    public Color paintFill=Color.TRANSPARENT,paintStroke=Color.BLACK;
    public double rotate=0,strokenWidth=5;

    public void setColor(Color value){
        this.paintFill = value;
        if(this.node instanceof Circle){
            ((Circle) this.node).setFill(value);
        }else if(this.node instanceof Ellipse){
            ((Ellipse) this.node).setFill(value);
        }else if(this.node instanceof Rectangle){
            ((Rectangle) this.node).setFill(value);
        }else if(this.node instanceof SVGPath){
            ((SVGPath) this.node).setFill(value);
        }
    }

    public void setLineColor(Color value){
        this.paintStroke = value;
        if(this.node instanceof Circle){
            ((Circle) this.node).setStroke(value);
        }else if(this.node instanceof Ellipse){
            ((Ellipse) this.node).setStroke(value);
        }else if(this.node instanceof Rectangle){
            ((Rectangle) this.node).setStroke(value);
        }else if(this.node instanceof SVGPath){
            ((SVGPath) this.node).setStroke(value);
        }
    }

    public void setLineWidth(double lineWidth){
        this.strokenWidth = lineWidth;
        if(this.node instanceof Circle){
            ((Circle) this.node).setStrokeWidth(lineWidth);
        }else if(this.node instanceof Ellipse){
            ((Ellipse) this.node).setStrokeWidth(lineWidth);
        }else if(this.node instanceof Rectangle){
            ((Rectangle) this.node).setStrokeWidth(lineWidth);
        }else if(this.node instanceof SVGPath){
            ((SVGPath) this.node).setStrokeWidth(lineWidth);
        }
    }

    public void setNodeRotate(double rotate){
        this.rotate = rotate;
        this.node.setRotate(rotate);
    }
}
