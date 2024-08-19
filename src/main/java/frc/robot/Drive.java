package frc.robot;

import com.revrobotics.CANSparkMax;

import com.revrobotics.CANSparkMaxLowLevel.MotorType;


import frc.util.LogitechAxis;
import frc.util.LogitechF310;

public class Drive{
    
    LogitechF310 driver, monkey;
    CANSparkMax leftBack, leftFront, rightBack, rightFront;
    public Drive(LogitechF310 driver, LogitechF310 monkey){
        
        try{
            this.driver = driver; 
            this.monkey = monkey;

          
            leftFront = new CANSparkMax(10, MotorType.kBrushless);
            rightFront = new CANSparkMax(11, MotorType.kBrushless);
            leftBack = new CANSparkMax(12, MotorType.kBrushless);
            rightBack = new CANSparkMax(13, MotorType.kBrushless);

            rightBack.setInverted(true);
            rightFront.setInverted(true);
        }
        catch(Exception e){

        }
    }
    
    public void teleopUpdate(){
        if(Math.abs(driver.getAxis(LogitechAxis.LY)) > .2 || Math.abs(driver.getAxis(LogitechAxis.RY)) > .2){
            leftBack.set(driver.getAxis(LogitechAxis.LY));
            leftFront.set(driver.getAxis(LogitechAxis.LY));
            rightBack.set(driver.getAxis(LogitechAxis.RY));
            rightFront.set(driver.getAxis(LogitechAxis.RY));
        }
        else{
            leftBack.set(0);
            leftFront.set(0);
            rightBack.set(0);
            rightFront.set(0);
        }
    }
}