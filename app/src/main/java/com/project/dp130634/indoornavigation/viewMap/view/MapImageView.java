package com.project.dp130634.indoornavigation.viewMap.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.project.dp130634.indoornavigation.model.map.BluetoothBeacon;
import com.project.dp130634.indoornavigation.model.map.Checkpoint;
import com.project.dp130634.indoornavigation.model.map.Elevator;
import com.project.dp130634.indoornavigation.model.map.Level;
import com.project.dp130634.indoornavigation.model.map.Location;
import com.project.dp130634.indoornavigation.model.map.Obstacle;
import com.project.dp130634.indoornavigation.model.map.Point2d;
import com.project.dp130634.indoornavigation.model.map.PointOfInterest;
import com.project.dp130634.indoornavigation.model.map.Route;
import com.project.dp130634.indoornavigation.viewMap.model.Model;

public class MapImageView extends AppCompatImageView {

    public MapImageView(Context context) {
        super(context);
        paint = new Paint();
    }

    public MapImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
    }

    public MapImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //Level image
        Level.ImageContainer curLevelImage =  model.getCurrentLevel().getImage();
        if(curLevelImage != null && curLevelImage.image != null) {
            Rect imageDst = new Rect();
            //Bitmaps can't be drawn rotated by specifying rotated left right top and bottom coordinates.
            //Solution: "unrotate" coordinate mapper, map bitmap corners without rotation, then use canvas.rotate for rotation
            //After drawing bitmap, switch rotation back to coordinate mapper and unrotate canvas
            int rotation = coordinateMapper.getRotation();
            coordinateMapper.setRotation(0);
            imageDst.left = coordinateMapper.mapCoordinates(curLevelImage.firstCoordinate).x;
            imageDst.right = coordinateMapper.mapCoordinates(curLevelImage.lastCoordinate).x;
            imageDst.top = coordinateMapper.mapCoordinates(curLevelImage.firstCoordinate).y;
            imageDst.bottom = coordinateMapper.mapCoordinates(curLevelImage.lastCoordinate).y;
            canvas.rotate(rotation);
            canvas.drawBitmap(curLevelImage.image, null, imageDst, paint);
            canvas.rotate(-rotation);
            coordinateMapper.setRotation(rotation);
        }

        //Obstacles
        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.FILL);
        for(Obstacle cur : model.getCurrentLevel().getObstacles()) {
            Path polygon = new Path();
            Point firstPoint = coordinateMapper.
                    mapCoordinates(cur.getPoints()[0]);
            polygon.moveTo(firstPoint.x, firstPoint.y);

            for(Point2d curPoint : cur.getPoints()) {
                Point drawablePoint = coordinateMapper.
                        mapCoordinates(curPoint);
                polygon.lineTo(drawablePoint.x, drawablePoint.y);
            }
            polygon.lineTo(firstPoint.x, firstPoint.y);

            canvas.drawPath(polygon, paint);
        }

        //Route (current selected)
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4);
        Route currentRoute = model.getSelectedRoute();
        if(currentRoute != null) {
            Path polyline = new Path();
            Point firstPoint = coordinateMapper.
                    mapCoordinates(currentRoute.getCheckpoints()[0].getLocation());
            polyline.moveTo(firstPoint.x, firstPoint.y);

            for(Checkpoint cur : currentRoute.getCheckpoints()) {
                Point drawablePoint = coordinateMapper.mapCoordinates(cur.getLocation());
                polyline.lineTo(drawablePoint.x, drawablePoint.y);
            }

            canvas.drawPath(polyline, paint);
        }

        //Points of interest
        paint.setColor(Color.MAGENTA);
        paint.setStyle(Paint.Style.FILL);
        for(PointOfInterest cur : model.getCurrentLevel().getPointsOfInterest()) {
            Point center = coordinateMapper.mapCoordinates(cur.getLocation());
            canvas.drawCircle(center.x, center.y, 8, paint);
        }

        //Beacons (only while debugging navigation)
        paint.setColor(Color.CYAN);
        paint.setStyle(Paint.Style.FILL);
        for(BluetoothBeacon cur : model.getCurrentLevel().getBluetoothBeacons()) {
            Point center = coordinateMapper.mapCoordinates(new Point2d(cur.getLocation().getX(), cur.getLocation().getY()));
            canvas.drawCircle(center.x, center.y, 8, paint);
        }

        //Elevators (?)
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);
        for(Elevator cur : model.getMap().getElevators()) {
            Point elevatorPosition = coordinateMapper.mapCoordinates(cur.getLocation());
            Path elevatorPolygon = new Path();

            elevatorPolygon.moveTo(elevatorPosition.x - 8, elevatorPosition.y);
            elevatorPolygon.lineTo(elevatorPosition.x, elevatorPosition.y - 8);
            elevatorPolygon.lineTo(elevatorPosition.x + 8, elevatorPosition.y);
            elevatorPolygon.lineTo(elevatorPosition.x, elevatorPosition.y + 8);
            elevatorPolygon.lineTo(elevatorPosition.x - 8, elevatorPosition.y);

            canvas.drawPath(elevatorPolygon, paint);
        }

        //User position
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.FILL);
        Location myLocation = model.getMyLocation();
        if(myLocation != null) {
            Point2d center2d = new Point2d(myLocation.getX(), myLocation.getY());
            Point center = coordinateMapper.mapCoordinates(center2d);
            canvas.drawCircle(center.x, center.y, 16, paint);

            Point2d inaccuracy = new Point2d(myLocation.getAccuracyX(), myLocation.getAccuracyY());
            Point inaccLen = coordinateMapper.scaleLength(inaccuracy);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawCircle(center.x, center.y, inaccLen.x, paint);
        }
    }

    private Paint paint;
    private Model model;
    private CoordinateMapper coordinateMapper = CoordinateMapper.getInstance();
}
