package com.project.dp130634.indoornavigation.location.bluetooth.util;

import com.lemmingapex.trilateration.NonLinearLeastSquaresSolver;
import com.lemmingapex.trilateration.TrilaterationFunction;
import com.project.dp130634.indoornavigation.location.Location;
import com.project.dp130634.indoornavigation.location.bluetooth.BeaconPacket;

import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer;
import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularMatrixException;

public class LocationCalculator {
    public static Location calculateLocation(BeaconPacket scanRecords[]) {
        if(scanRecords.length == 1) {
            return findLocationWithOneBeacon(scanRecords[0]);
        } else {
            double positions[][] = getPositions(scanRecords);
            double distances[] = getDistances(scanRecords);

            NonLinearLeastSquaresSolver solver = new NonLinearLeastSquaresSolver(new TrilaterationFunction(positions, distances), new LevenbergMarquardtOptimizer());
            LeastSquaresOptimizer.Optimum optimum = solver.solve();
            double centroid[] = optimum.getPoint().toArray();
            Location retVal = new Location();
            retVal.setX(centroid[0]);
            retVal.setY(centroid[1]);
            retVal.setZ(centroid[2]);

            try {
                RealVector standardDeviation = optimum.getSigma(0);
                retVal.setAccuracyX(standardDeviation.toArray()[0]);
                retVal.setAccuracyY(standardDeviation.toArray()[1]);
                retVal.setAccuracyZ(standardDeviation.toArray()[2]);
            } catch (SingularMatrixException e) {

            }
            retVal.setTimestamp(scanRecords[0].getTimestamp());
            for(int i = 1; i < scanRecords.length; i++) {
                if(scanRecords[i].getTimestamp() < retVal.getTimestamp()) {
                    retVal.setTimestamp(scanRecords[i].getTimestamp());
                }
            }
            return retVal;
        }
    }


    private static Location findLocationWithOneBeacon(BeaconPacket scr) {
        Location retVal = new Location();
        retVal.setX(scr.getBeacon().getLocation().getX());
        retVal.setY(scr.getBeacon().getLocation().getY());
        retVal.setZ(scr.getBeacon().getLocation().getZ());
        retVal.setTimestamp(scr.getTimestamp());
        retVal.setAccuracyX(scr.getDistance());
        retVal.setAccuracyY(scr.getDistance());
        retVal.setAccuracyZ(scr.getDistance());
        return retVal;
    }

    private static double[][] getPositions(BeaconPacket scanRecords[]) {
        double retVal[][] = new double[scanRecords.length][];

        for(int i = 0; i < scanRecords.length; i++){
            retVal[i] = new double[3];
            retVal[i][0] = scanRecords[i].getBeacon().getLocation().getX();
            retVal[i][1] = scanRecords[i].getBeacon().getLocation().getY();
            retVal[i][2] = scanRecords[i].getBeacon().getLocation().getZ();
        }

        return retVal;
    }

    private static double[] getDistances(BeaconPacket scanRecords[]) {
        double retVal[] = new double[scanRecords.length];

        for(int i = 0; i < scanRecords.length; i++) {
            retVal[i] = scanRecords[i].getDistance();
        }

        return retVal;
    }



//    private static Location findLocationWithTwoBeacons(BeaconPacket scr1, BeaconPacket scr2) {
//        double beaconDistance = locationDistance(scr1.getBeacon().getLocation(), scr2.getBeacon().getLocation());
//
//        if(beaconDistance < scr1.getDistance() || beaconDistance < scr2.getDistance()) {
//            //The device is further from a beacon than the other beacon
//            BeaconPacket closerBeacon = scr1.getDistance() < scr2.getDistance() ? scr1 : scr2;
//            BeaconPacket furtherBeacon = scr1.getDistance() > scr2.getDistance() ? scr1 : scr2;
//            return findLocationWithLineAndSphere(furtherBeacon, closerBeacon);
//        }
//
//        if(!rangesOverlap(scr1, scr2)) {
//            adjustRanges(scr1, scr2);
//        }
//        return findLocationWithDeviceBetweenTwoBeacons(scr1, scr2);
//    }
//
//    private static Location findLocationWithThreeBeacons(BeaconPacket scr1, BeaconPacket scr2, BeaconPacket scr3) {
//        Location retVal = new Location();
//        //Find out if there's a combination of two beacons where the device is further from a beacon than the other beacon
//        //If that's the case, do calculations with only two closer beacons
//        double beacon12Distance = locationDistance(scr1.getBeacon().getLocation(), scr2.getBeacon().getLocation());
//        if(beacon12Distance < scr1.getDistance() || beacon12Distance < scr2.getDistance()) {
//            //The device is further from a beacon than the other beacon
//            BeaconPacket closerBeacon = scr1.getDistance() < scr2.getDistance() ? scr1 : scr2;
//            return findLocationWithTwoBeacons(closerBeacon, scr3);
//        }
//
//        double beacon13Distance = locationDistance(scr1.getBeacon().getLocation(), scr3.getBeacon().getLocation());
//        if(beacon13Distance < scr1.getDistance() || beacon12Distance < scr3.getDistance()) {
//            //The device is further from a beacon than the other beacon
//            BeaconPacket closerBeacon = scr1.getDistance() < scr3.getDistance() ? scr1 : scr3;
//            BeaconPacket furtherBeacon = scr1.getDistance() > scr3.getDistance() ? scr1 : scr3;
//            return findLocationWithTwoBeacons(closerBeacon, scr2);
//        }
//
//        double beacon32Distance = locationDistance(scr3.getBeacon().getLocation(), scr2.getBeacon().getLocation());
//        if(beacon32Distance < scr3.getDistance() || beacon12Distance < scr2.getDistance()) {
//            //The device is further from a beacon than the other beacon
//            BeaconPacket closerBeacon = scr3.getDistance() < scr2.getDistance() ? scr3 : scr2;
//            return findLocationWithTwoBeacons(closerBeacon, scr1);
//        }
//
//        //else, we have a regular 3-beacon calculation.
//        //Adjust ranges so they all overlap
//        if(!rangesOverlap(scr1, scr2)) {
//            adjustRanges(scr1, scr2);
//        }
//        if(!rangesOverlap(scr2, scr3)) {
//            adjustRanges(scr2, scr3);
//        }
//        if(!rangesOverlap(scr1, scr3)) {
//            adjustRanges(scr1, scr3);
//        }
//        //find the three intersection planes between spheres
//        //find the intersection between the planes
//        return findPlaneIntersection(scr1, scr2, scr3);
//        //if the intersection is a line, find the point on the line closest to one of the beacons, and make that point the location
//        //if the intersection is a point, make that point the location
//    }
//
//    private static Location findPlaneIntersection(BeaconPacket scr1, BeaconPacket scr2, BeaconPacket scr3) {
//        Location retVal = new Location();
//        Plane plane12 = findOverlapPlane(scr1, scr2);
//        Plane plane13 = findOverlapPlane(scr1, scr3);
//        Plane plane23 = findOverlapPlane(scr2, scr3);
//        double a1 = plane12.a;
//        double b1 = plane12.b;
//        double c1 = plane12.c;
//        double d1 = plane12.d;
//        double a2 = plane13.a;
//        double b2 = plane13.b;
//        double c2 = plane13.c;
//        double d2 = plane13.d;
//        double a3 = plane23.a;
//        double b3 = plane23.b;
//        double c3 = plane23.c;
//        double d3 = plane23.d;
//        double D, Dx, Dy, Dz;
//
//        D = a1*b2*c3 - a1*b3*c2 - a2*b1*c2 + a2*b2*c1 + a3*b1*c2 - a3*b2*c1;
//        Dx = d1*b2*c3 - d1*b3*c2 - d2*b1*c2 + d2*b2*c1 + d3*b1*c2 - d3*b2*c1;
//        Dy = a1*d2*c3 - a1*d3*c2 - a2*d1*c2 + a2*d2*c1 + a3*d1*c2 - a3*d2*c1;
//        Dz = a1*b2*d3 - a1*b3*d2 - a2*b1*d2 + a2*b2*d1 + a3*b1*d2 - a3*b2*d1;
//
//        if(D != 0) {
//            retVal.setX(Dx/D);
//            retVal.setY(Dy/D);
//            retVal.setZ(Dz/D);
//            retVal.setAccuracyX(0);
//            retVal.setAccuracyY(0);
//            retVal.setAccuracyZ(0);
//            return retVal;
//        }
//        if(D == 0 && Dx == 0 && Dy == 0 && Dz == 0) {
//            if(equivalentEquations(plane12, plane13) && equivalentEquations(plane12, plane23)) {
//                throw new IllegalArgumentException("Three equivalent equations");
//            }
//            if(equivalentEquations(plane12, plane13)) {
//                plane13 = plane23;
//            }
//            return findLocationWithPlaneIntersections(plane12, plane13, scr1);
//        }
//        if(D == 0 && Dx != 0 || Dy != 0 || Dz != 0) {
//            throw new IllegalArgumentException("Inconsistent equation system.");
//        }
//        return null;
//    }
//
//    private static Location findLocationWithPlaneIntersections(Plane plane1, Plane plane2, BeaconPacket scr) {
//        Location retVal = new Location();
//        double a1 = plane1.a;
//        double b1 = plane1.b;
//        double c1 = plane1.c;
//        double d1 = plane1.d;
//        double a2 = plane2.a;
//        double b2 = plane2.b;
//        double c2 = plane2.c;
//        double d2 = plane2.d;
//        double x1 = scr.getBeacon().getLocation().getX();
//        double y1 = scr.getBeacon().getLocation().getY();
//        double z1 = scr.getBeacon().getLocation().getZ();
//
//        double t = (x1*b2*d1 - x1*b1*d2 + y1*a1*d2 - y1*a2*d1) / (x1*b1*c2 - x1*b2*c1 + y1*a2*c1 - y1*a1*c2 + z1*a1*b2 - z1*a2*b1);
//
//        double x = ((b1*c2 - b2*c1) / (a1*b2 - a2*b1)) * t + (b2*d1 - b1*d2) / (a1*b2 - a2*b1);
//        double y = ((a2*c1 - a1*c2) / (a1*b2 - a2*b1)) * t + (a1*d2 - a2*d1) / (a1*b2 - a2*b1);
//        double z = t;
//        retVal.setX(x);
//        retVal.setY(y);
//        retVal.setZ(z);
//
//        return retVal;
//    }
//
//    private static Location findLocationWithDeviceBetweenTwoBeacons(BeaconPacket scr1, BeaconPacket scr2) {
//        double r1 = scr1.getDistance();
//        double r2 = scr2.getDistance();
//        double d = locationDistance(scr1.getBeacon().getLocation(), scr2.getBeacon().getLocation());
//        double h = Math.sqrt(r1*r1 - ((Math.pow(r1*r1 - r2*r2 + d*d, 2)/(d*d))));
//        double x = Math.sqrt(r1*r1 - h*h);
//
//        double x1, x2, y1, y2, z1, z2;
//        x1 = scr1.getBeacon().getLocation().getX();
//        y1 = scr1.getBeacon().getLocation().getY();
//        z1 = scr1.getBeacon().getLocation().getZ();
//        x2 = scr2.getBeacon().getLocation().getX();
//        y2 = scr2.getBeacon().getLocation().getY();
//        z2 = scr2.getBeacon().getLocation().getZ();
//        double t = x / Math.sqrt(Math.pow(x1-x2, 2) + Math.pow(y1-y2, 2) + Math.pow(z1-z2, 2));
//
//        Location candidate1 = new Location();
//        candidate1.setX(x1 + (x1 - x2) * t);
//        candidate1.setY(y1 + (y1 - y2) * t);
//        candidate1.setZ(z1 + (z1 - z2) * t);
//
//        t = -t;
//        Location candidate2 = new Location();
//        candidate2.setX(x1 + (x1 - x2) * t);
//        candidate2.setY(y1 + (y1 - y2) * t);
//        candidate2.setZ(z1 + (z1 - z2) * t);
//
//        Location retVal = locationDistance(candidate1, scr2.getBeacon().getLocation()) < locationDistance(candidate2, scr2.getBeacon().getLocation()) ? candidate1 : candidate2;
//        retVal.setAccuracyX(h);
//        retVal.setAccuracyY(h);
//        retVal.setAccuracyZ(h);
//        retVal.setTimestamp(scr1.getTimestamp() > scr2.getTimestamp() ? scr2.getTimestamp() : scr1.getTimestamp());
//
//        return retVal;
//    }
//
//    private static double locationDistance(Location loc1, Location loc2) {
//        return Math.sqrt(Math.pow(loc1.getX() - loc2.getX(), 2) + Math.pow(loc1.getY() - loc2.getY(), 2) + Math.pow(loc1.getZ() - loc2.getZ(), 2));
//    }
//
//    private static boolean rangesOverlap(BeaconPacket scr1, BeaconPacket scr2) {
//        double beaconDistance = locationDistance(scr1.getBeacon().getLocation(), scr2.getBeacon().getLocation());
//        double rangeSum = scr1.getDistance() + scr2.getDistance();
//        return rangeSum > beaconDistance;
//    }
//
//    private static void adjustRanges(BeaconPacket scr1, BeaconPacket scr2) {
//        while(!rangesOverlap(scr1, scr2)) {
//            scr1.setRssi(scr1.getRssi() - 1);
//            scr2.setRssi(scr2.getRssi() - 1);
//        }
//    }
//
//    /**
//     * Function used when only two beacons are available, and one is further from the device than from the other beacon
//     * In other words: distance(Beacon1, Beacon2) < distance(Device, Beacon)
//     * This method gives inaccurate location. Beacons should be arranged in a manner so this function is rarely used
//     * */
//    private static Location findLocationWithLineAndSphere(BeaconPacket furtherBeacon, BeaconPacket closerBeacon) {
//        double r = closerBeacon.getDistance();
//        double x1, x2, y1, y2, z1, z2;
//        x1 = closerBeacon.getBeacon().getLocation().getX();
//        y1 = closerBeacon.getBeacon().getLocation().getY();
//        z1 = closerBeacon.getBeacon().getLocation().getZ();
//        x2 = furtherBeacon.getBeacon().getLocation().getX();
//        y2 = furtherBeacon.getBeacon().getLocation().getY();
//        z2 = furtherBeacon.getBeacon().getLocation().getZ();
//
//        double t = Math.sqrt(Math.pow(r, 2) / (x1*x1 - 2*x1*x2 + x2*x2 + y1*y1 - 2*y1*y2 + y2*y2 + z1*z1 - 2*z1*z2 + z2*z2));
//
//        Location candidate1 = new Location();
//        candidate1.setX(x1 + (x1 - x2)*t);
//        candidate1.setY(y1 + (y1 - y2)*t);
//        candidate1.setZ(z1 + (z1 - z2)*t);
//
//        t = -t;
//        Location candidate2 = new Location();
//        candidate2.setX(x1 + (x1 - x2)*t);
//        candidate2.setY(y1 + (y1 - y2)*t);
//        candidate2.setZ(z1 + (z1 - z2)*t);
//
//        Location retVal = locationDistance(candidate1, furtherBeacon.getBeacon().getLocation()) > locationDistance(candidate2, furtherBeacon.getBeacon().getLocation()) ? candidate1 : candidate2;
//        double accuracy = furtherBeacon.getDistance() - locationDistance(furtherBeacon.getBeacon().getLocation(), closerBeacon.getBeacon().getLocation()) - closerBeacon.getDistance();
//        retVal.setAccuracyX(accuracy);
//        retVal.setAccuracyY(accuracy);
//        retVal.setAccuracyZ(accuracy);
//        retVal.setTimestamp(furtherBeacon.getTimestamp() > closerBeacon.getTimestamp() ? closerBeacon.getTimestamp() : furtherBeacon.getTimestamp());
//
//        return retVal;
//    }
//
//    private static Plane findOverlapPlane(BeaconPacket scr1, BeaconPacket scr2) {
//        Plane retVal = new Plane();
//        double x0 = scr1.getBeacon().getLocation().getX();
//        double y0 = scr1.getBeacon().getLocation().getY();
//        double z0 = scr1.getBeacon().getLocation().getZ();
//        double x1 = scr2.getBeacon().getLocation().getX();
//        double y1 = scr2.getBeacon().getLocation().getY();
//        double z1 = scr2.getBeacon().getLocation().getZ();
//        double r0 = scr1.getDistance();
//        double r1 = scr2.getDistance();
//
//        retVal.a = 2 * (x1 - x0);
//        retVal.b = 2 * (y1 - y0);
//        retVal.c = 2 * (z1 - z0);
//        retVal.d = -(x0*x0 + y0*y0 + z0*z0 - r0*r0 - x1*x1 - y1*y1 - z1*z1 + r1*r1);
//
//        return retVal;
//    }
//
//    private static boolean equivalentEquations(Plane plane1, Plane plane2) {
//        double a1 = plane1.a;
//        double b1 = plane1.b;
//        double c1 = plane1.c;
//        double d1 = plane1.d;
//        double a2 = plane2.a;
//        double b2 = plane2.b;
//        double c2 = plane2.c;
//        double d2 = plane2.d;
//
//        if(a1 == 0 && b1 == 0 && c1 == 0 && d1 == 0 || a2 == 0 && b2 == 0 && c2 == 0 && d2 == 0) {
//            throw new IllegalArgumentException("Zero equation passed");
//        }
//
//        if(a1 == 0 && a2 != 0 || a1 != 0 && a2 == 0
//            || b1 == 0 && b2 != 0 || b1 != 0 && b2 == 0
//            || c1 == 0 && c2 != 0 || c1 !=0 && c2 == 0
//            || d1 == 0 && d2 != 0 || d1 !=0 && d2 ==0) {
//            return false;
//        }
//
//        double nonZeroCoefficient1 = 1;
//        double nonZeroCoefficient2 = 1;
//        if(a1 != 0) {
//            nonZeroCoefficient1 = a1;
//            nonZeroCoefficient2 = a2;
//        }
//        if(b1 != 0) {
//            nonZeroCoefficient1 = b1;
//            nonZeroCoefficient2 = b2;
//        }
//        if(c1 != 0) {
//            nonZeroCoefficient1 = c1;
//            nonZeroCoefficient2 = c2;
//        }
//        if(d1 != 0) {
//            nonZeroCoefficient1 = d1;
//            nonZeroCoefficient2 = d2;
//        }
//        double x = nonZeroCoefficient1 / nonZeroCoefficient2;
//
//        if(a1 != 0 && a1 / a2 != x) {
//            return false;
//        }
//        if(b1 != 0 && b1 / b2 != x) {
//            return false;
//        }
//        if(c1 != 0 && c1 / c2 != x) {
//            return false;
//        }
//        if(d1 != 0 && d1 / d2 != x) {
//            return false;
//        }
//
//        return true;
//    }
//
//    private static class Plane {
//        public double a, b, c, d;
//    }
}
