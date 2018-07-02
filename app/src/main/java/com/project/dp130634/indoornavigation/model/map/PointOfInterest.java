package com.project.dp130634.indoornavigation.model.map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class PointOfInterest extends MapElement implements Serializable {
    static final long serialVersionUID =-3169145537364315550L;
    private Point2d location;
    private String name;
    private String description;
    private transient Bitmap image;

    public PointOfInterest() {
    }

    public PointOfInterest(Point2d location, String name, String description, Bitmap image) {
        this.location = location;
        this.name = name;
        this.description = description;
        this.image = image;
    }

    public Point2d getLocation() {
        return location;
    }

    public void setLocation(Point2d location) {
        this.location = location;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        if(image != null) {
            out.writeInt(1);
            image.compress(Bitmap.CompressFormat.PNG, 100, out);
        }
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        final int imageCnt = in.readInt();
        if(imageCnt == 1) {
            BitmapFactory.decodeStream(in);
        }
    }

    @Override
    public String toString() {
        return "Point of interest \"" + name + '\"';
    }
    
    
}
