package com.project.dp130634.indoornavigation.location.bluetooth.util;

import com.lemmingapex.trilateration.NonLinearLeastSquaresSolver;
import com.lemmingapex.trilateration.TrilaterationFunction;
import com.project.dp130634.indoornavigation.location.bluetooth.BeaconPacket;
import com.project.dp130634.indoornavigation.location.bluetooth.BeaconPacketList;
import com.project.dp130634.indoornavigation.model.map.Location;

import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer;
import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularMatrixException;

public class LocationCalculator {
    public static Location calculateLocation(BeaconPacketList scanRecords[]) {
        if(scanRecords.length == 0) {
            return null;
        }
        if(scanRecords.length == 1) {
            return findLocationWithOneBeacon(scanRecords[0]);
        } else {
            double positions[][] = getPositions(scanRecords);
            double distances[] = getDistances(scanRecords);
            Location retVal = new Location();
            LeastSquaresOptimizer.Optimum optimum;

            try {
                NonLinearLeastSquaresSolver solver = new NonLinearLeastSquaresSolver(new TrilaterationFunction(positions, distances), new LevenbergMarquardtOptimizer());
                optimum = solver.solve();
                double centroid[] = optimum.getPoint().toArray();

                retVal.setX(centroid[0]);
                retVal.setY(centroid[1]);
                retVal.setZ(centroid[2]);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

            try {
                RealVector standardDeviation = optimum.getSigma(0);
                retVal.setAccuracyX(standardDeviation.toArray()[0]);
                retVal.setAccuracyY(standardDeviation.toArray()[1]);
                retVal.setAccuracyZ(standardDeviation.toArray()[2]);
            } catch (SingularMatrixException e) {
                retVal.setAccuracyX(0);
                retVal.setAccuracyY(0);
                retVal.setAccuracyZ(0);
                e.printStackTrace();
            } finally {
                retVal.setTimestamp(System.currentTimeMillis());
                return retVal;
            }
        }
    }


    private static Location findLocationWithOneBeacon(BeaconPacketList scr) {
        Location retVal = new Location();
        retVal.setX(scr.getBeacon().getLocation().getX());
        retVal.setY(scr.getBeacon().getLocation().getY());
        retVal.setZ(scr.getBeacon().getLocation().getZ());
        retVal.setTimestamp(System.currentTimeMillis());
        retVal.setAccuracyX(scr.calculateDistance());
        retVal.setAccuracyY(scr.calculateDistance());
        retVal.setAccuracyZ(scr.calculateDistance());
        return retVal;
    }

    private static double[][] getPositions(BeaconPacketList scanRecords[]) {
        double retVal[][] = new double[scanRecords.length][];

        for(int i = 0; i < scanRecords.length; i++){
            retVal[i] = new double[3];
            retVal[i][0] = scanRecords[i].getBeacon().getLocation().getX();
            retVal[i][1] = scanRecords[i].getBeacon().getLocation().getY();
            retVal[i][2] = scanRecords[i].getBeacon().getLocation().getZ();
        }

        return retVal;
    }

    private static double[] getDistances(BeaconPacketList scanRecords[]) {
        double retVal[] = new double[scanRecords.length];

        for(int i = 0; i < scanRecords.length; i++) {
            retVal[i] = scanRecords[i].calculateDistance();
        }

        return retVal;
    }
}
