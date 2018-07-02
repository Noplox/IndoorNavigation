package com.project.dp130634.indoornavigation.viewMap.view;

import android.app.Activity;
import android.graphics.Point;
import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.almeros.android.multitouch.MoveGestureDetector;
import com.almeros.android.multitouch.RotateGestureDetector;

public class TransformationHandler implements View.OnTouchListener {

    public TransformationHandler(Activity activity) {
        coordinateMapper = CoordinateMapper.getInstance();

        scaleGestureDetector  = new ScaleGestureDetector(activity, new ScaleListener());
        scaleFactor = (float)coordinateMapper.getZoomPercent() / 100.0f;

        rotateGestureDetector = new RotateGestureDetector(activity, new RotationListener());
        rotationDegrees = coordinateMapper.getRotation();

        moveGestureDetector = new MoveGestureDetector(activity, new MoveListener());
        translation = new Point(0,0);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        scaleGestureDetector.onTouchEvent(event);
        coordinateMapper.setZoomPercent(scaleFactor * 100);

        rotateGestureDetector.onTouchEvent(event);
        coordinateMapper.setRotation(Math.round(rotationDegrees));

        moveGestureDetector.onTouchEvent(event);
        coordinateMapper.translate(translation);

        v.performClick();
        return true;
    }

    private CoordinateMapper coordinateMapper;

    private ScaleGestureDetector scaleGestureDetector;
    private float scaleFactor;
    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scaleFactor *= detector.getScaleFactor();
            return true;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            return super.onScaleBegin(detector);
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            super.onScaleEnd(detector);
        }
    }

    private RotateGestureDetector rotateGestureDetector;
    private float rotationDegrees;
    private class RotationListener extends RotateGestureDetector.SimpleOnRotateGestureListener {
        @Override
        public boolean onRotate(RotateGestureDetector detector) {
            rotationDegrees -= detector.getRotationDegreesDelta();
            return true;
        }

        @Override
        public boolean onRotateBegin(RotateGestureDetector detector) {
            return super.onRotateBegin(detector);
        }

        @Override
        public void onRotateEnd(RotateGestureDetector detector) {
            super.onRotateEnd(detector);
        }
    }

    private MoveGestureDetector moveGestureDetector;
    private final Point translation;
    private class MoveListener extends MoveGestureDetector.SimpleOnMoveGestureListener {
        @Override
        public boolean onMove(MoveGestureDetector detector) {
            PointF d = detector.getFocusDelta();
            translation.x = Math.round(d.x);
            translation.y = Math.round(d.y);
            return true;
        }

        @Override
        public boolean onMoveBegin(MoveGestureDetector detector) {
            return super.onMoveBegin(detector);
        }

        @Override
        public void onMoveEnd(MoveGestureDetector detector) {
            super.onMoveEnd(detector);
        }
    }
}
