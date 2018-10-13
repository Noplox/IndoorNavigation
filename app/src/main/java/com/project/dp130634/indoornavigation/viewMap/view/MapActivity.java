package com.project.dp130634.indoornavigation.viewMap.view;

import android.graphics.Point;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.ImageView;

import com.project.dp130634.indoornavigation.MainActivity;
import com.project.dp130634.indoornavigation.R;
import com.project.dp130634.indoornavigation.mapDownload.QrScanActivity;
import com.project.dp130634.indoornavigation.model.map.Map;
import com.project.dp130634.indoornavigation.viewMap.ViewInterface;
import com.project.dp130634.indoornavigation.viewMap.controller.Controller;
import com.project.dp130634.indoornavigation.viewMap.model.Model;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

public class MapActivity extends AppCompatActivity implements ViewInterface {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        File mapFile = (File) getIntent().getExtras().get(QrScanActivity.KEY_MAP_FILE);

        //String path = mapUri.getPath();
        try(InputStream fin = new FileInputStream(mapFile);
            ObjectInputStream ois = new ObjectInputStream(fin)) {
            Map map = (Map)ois.readObject();
            controller = new Controller(this, this);
            controller.setMap(map);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        TransformationHandler transformationHandler = new TransformationHandler(this);

        mapImageView = (MapImageView)findViewById(R.id.mapImageView);
        mapImageView.setModel(controller.getModel());
        mapImageView.setOnTouchListener(transformationHandler);

        int height = mapImageView.getHeight();
        int width = mapImageView.getWidth();
        CoordinateMapper.getInstance().setCanvasSize(new Point(width, height));
    }

    @Override
    public void refresh(Model model) {
        mapImageView.invalidate();
    }

    private Controller controller;
    private MapImageView mapImageView;
}
