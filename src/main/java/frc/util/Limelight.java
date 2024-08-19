package frc.util;

import edu.wpi.first.networktables.NetworkTableInstance;

public class Limelight{
    public static double getLimelight(String args){
        return NetworkTableInstance.getDefault().getTable("limelight").getEntry(args).getDouble(0);
    }

    public static void changeCamera(int camMode, int ledMode){
        NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").getDouble(ledMode);
        NetworkTableInstance.getDefault().getTable("limelight").getEntry("camMode").getDouble(camMode);
    }

    public static double getV(){
        return getLimelight("tv");
    }

    public static double getX(){
        return getLimelight("tx");
    }

    public static double getY(){
        return getLimelight("ty");
    }
}