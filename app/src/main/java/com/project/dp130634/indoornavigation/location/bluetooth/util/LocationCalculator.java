package com.project.dp130634.indoornavigation.location.bluetooth.util;

import android.support.annotation.Nullable;

import com.lemmingapex.trilateration.NonLinearLeastSquaresSolver;
import com.lemmingapex.trilateration.TrilaterationFunction;
import com.project.dp130634.indoornavigation.location.bluetooth.BeaconPacketList;
import com.project.dp130634.indoornavigation.model.map.Location;

import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer;
import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.jetbrains.annotations.NotNull;

public class LocationCalculator {

    private static double positions[][];
    private static double distances[];
    private static int validScanRecords;

    @Nullable
    public synchronized static Location calculateLocation(@NotNull BeaconPacketList scanRecords[]) {

        filterScanRecords(scanRecords);

        if(validScanRecords == 0) {
            return null;
        }
        if(validScanRecords == 1) {
            return findLocationWithOneBeacon();
        } else {
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

    private static void filterScanRecords(BeaconPacketList[] scanRecords) {
        double dirtyDistances[] = getDistances(scanRecords);
        double dirtyPositions[][] = getPositions(scanRecords);

        validScanRecords = 0;
        for (double distance : dirtyDistances) {
            if (!Double.isNaN(distance)) {
                validScanRecords++;
            }
        }

        distances = new double[validScanRecords];
        positions = new double[validScanRecords][];

        int j = 0;
        for(int i = 0; i < dirtyDistances.length; i++) {
            if( !Double.isNaN(dirtyDistances[i])) {
                distances[j] = dirtyDistances[i];
                positions[j] = dirtyPositions[i];
                j++;
            }
        }
    }


    private static Location findLocationWithOneBeacon() {
        Location retVal = new Location();
        retVal.setX(positions[0][0]);
        retVal.setY(positions[0][1]);
        retVal.setZ(positions[0][2]);
        retVal.setTimestamp(System.currentTimeMillis());
        retVal.setAccuracyX(distances[0]);
        retVal.setAccuracyY(distances[0]);
        retVal.setAccuracyZ(distances[0]);
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
