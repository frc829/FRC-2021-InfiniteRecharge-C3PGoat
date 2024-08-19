package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;


import frc.util.LogitechAxis;
import frc.util.LogitechButton;
import frc.util.LogitechF310;

public class Shoot{
    LogitechF310 driver, monkey;
    CANSparkMax rotate;
    TalonFX topShoot, botShoot;
    double topSpeed = 0, botSpeed = 0;
    double targetTopSpeed = 0 , targetBottomSpeed = 0;


    NetworkTableInstance instance = NetworkTableInstance.getDefault();
    NetworkTable speedTable = instance.getTable("Shooter: Motor Speeds");
    NetworkTable angleTable = instance.getTable("Shooter: Angle");

    boolean isManual = true;


    public Shoot(LogitechF310 driver, LogitechF310 monkey){
        
        speedTable.getEntry("90inch - Top Speed:").setDouble(.4);
        speedTable.getEntry("90inch - Bottom Speed").setDouble(.5);

        speedTable.getEntry("150inch - Top Speed:").setDouble(.3);
        speedTable.getEntry("150inch - Bottom Speed").setDouble(.5);

        speedTable.getEntry("210inch - Top Speed:").setDouble(.3);
        speedTable.getEntry("210inch - Bottom Speed").setDouble(.6);

        speedTable.getEntry("270inch - Top Speed:").setDouble(.4);
        speedTable.getEntry("270inch - Bottom Speed").setDouble(.7);

        angleTable.getEntry("90inch Angle").setDouble(0);
        angleTable.getEntry("150inch Angle").setDouble(-27);
        angleTable.getEntry("210inch Angle").setDouble(-40);
        angleTable.getEntry("270inch Angle").setDouble(-55);


        try{
            this.driver = driver;
            this.monkey = monkey;
            rotate = new CANSparkMax(19,MotorType.kBrushless);
            topShoot = new TalonFX(24);
            botShoot = new TalonFX(25);
            rotate.getEncoder().setPosition(0);
            rotate.setIdleMode(IdleMode.kBrake);
        }
        catch(Exception e){

        }   
    }
    
   
    public void teleopUpdate(){
        

        
        double hoodPosition = rotate.getEncoder().getPosition();

        System.out.println(hoodPosition);

        
        
        if(monkey.getAxis(LogitechAxis.RY) > .2){
            isManual = true;
            targetTopSpeed = .4;
            targetBottomSpeed = .5;
            rotate.set(.2);
        }
        else if(monkey.getAxis(LogitechAxis.RY) < -.2){
            isManual = true;
            targetTopSpeed = .4;
            targetBottomSpeed = .5;
            rotate.set(-.2);
        }
        else{
            rotate.set(0);
        }


        switch(monkey.getPOV()){
            case 0: 
                isManual = false;
                rotate.setIdleMode(IdleMode.kCoast);
                shooterRotateToPosition(angleTable.getEntry("90inch Angle").getDouble(0));
                targetTopSpeed = speedTable.getEntry("90inch - Top Speed:").getDouble(.4);
                targetBottomSpeed = speedTable.getEntry("90inch - Bottom Speed").getDouble(.5);
                break;
            case 90:
                rotate.setIdleMode(IdleMode.kCoast);
                isManual = false;
                shooterRotateToPosition(angleTable.getEntry("150inch Angle").getDouble(-31));
                targetTopSpeed = speedTable.getEntry("150inch - Top Speed:").getDouble(.3);
                targetBottomSpeed = speedTable.getEntry("150inch - Bottom Speed").getDouble(.5);
                break;
            case 180: 
                rotate.setIdleMode(IdleMode.kCoast);
                isManual = false;
                shooterRotateToPosition(angleTable.getEntry("210inch Angle").getDouble(-40)); 
                targetTopSpeed = speedTable.getEntry("210inch - Top Speed:").getDouble(.3);
                targetBottomSpeed = speedTable.getEntry("210inch - Bottom Speed").getDouble(.6);
                break;
            case 270: 
                rotate.setIdleMode(IdleMode.kCoast);
                isManual = false;
                shooterRotateToPosition(angleTable.getEntry("270inch Angle").getDouble(-55)); 
                targetTopSpeed = speedTable.getEntry("270inch - Top Speed:").getDouble(.4);
                targetBottomSpeed = speedTable.getEntry("270inch - Bottom Speed").getDouble(.7);
                break;
            default:
                 rotate.setIdleMode(IdleMode.kBrake);
                break;

        }

        if(monkey.getRawButton(LogitechButton.LB) && monkey.getRawButton(LogitechButton.RB)){
            if(topSpeed<targetTopSpeed-.05){
                topSpeed+=.01;
            }
            else if(topSpeed>targetTopSpeed+.05){
                topSpeed-=.005;
            }

            if(botSpeed<targetBottomSpeed-.05){
                botSpeed+=.01;
            }
            else if(botSpeed>targetBottomSpeed+.05){
                botSpeed-=.005;
            }

            topShoot.set(ControlMode.PercentOutput, topSpeed);
            botShoot.set(ControlMode.PercentOutput, botSpeed);
        }
        else{
            topShoot.set(ControlMode.PercentOutput,0);
            botShoot.set(ControlMode.PercentOutput, 0);
        }
            
    
        
    }

    public void shooterRotateToPosition(double target){
        System.out.print(rotate.getEncoder().getPosition());
        if(rotate.getEncoder().getPosition() > target + 1.25){
            rotate.set(-.2);
        }
        else if(rotate.getEncoder().getPosition() < target - 1.25){
            rotate.set(.1);
        }
        else{
            rotate.set(0);
        }
    }




}