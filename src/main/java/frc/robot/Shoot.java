package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import frc.util.LogitechButton;
import frc.util.LogitechF310;

public class Shoot{
    LogitechF310 driver, monkey;
    CANSparkMax rotate;
    TalonFX topShoot, botShoot;

    public Shoot(LogitechF310 driver, LogitechF310 monkey){
        try{
            this.driver = driver;
            this.monkey = monkey;
            rotate = new CANSparkMax(19,MotorType.kBrushless);
            topShoot = new TalonFX(24);
            botShoot = new TalonFX(25);
        }
        catch(Exception e){

        }   
    }
    
    public void teleopUpdate(){
        if(monkey.getRawButton(LogitechButton.LB) && monkey.getRawButton(LogitechButton.RB)){
            topShoot.set(ControlMode.PercentOutput,.5);
            botShoot.set(ControlMode.PercentOutput, .5);
        }
        
        switch(monkey.getPOV()){
            case 0: shooterRotateToPosition(0); break;
            case 90:shooterRotateToPosition(0); break;
            case 180: shooterRotateToPosition(0); break;
            case 270: shooterRotateToPosition(0); break;
            default: rotate.set(0); break;

        }
            
    
        
    }

    public void shooterRotateToPosition(int target){
        if(rotate.getEncoder().getPosition() > target + 5){
            rotate.set(.2);
        }
        else if(rotate.getEncoder().getPosition() < target - 5){
            rotate.set(-.2);
        }
        else{
            rotate.set(0);
        }
    }




}