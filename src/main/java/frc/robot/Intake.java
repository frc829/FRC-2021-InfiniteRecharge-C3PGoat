package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import frc.util.LogitechButton;
import frc.util.LogitechF310;

      
public class Intake{
    LogitechF310 driver, monkey;
    TalonSRX intake, vBrush; 
    CANSparkMax lBrush, rBrush;

    public Intake(LogitechF310 driver, LogitechF310 monkey){
        try {
            this.driver = driver;
            this.monkey = monkey;
    
            intake = new TalonSRX(20);
            vBrush = new TalonSRX(21);
            lBrush = new CANSparkMax(22,MotorType.kBrushless);
            rBrush = new CANSparkMax(23,MotorType.kBrushless);
        } catch (Exception e) {
            
        }
    }

    public void teleopUpdate(){
        if(monkey.getRawButton(LogitechButton.A)){
            intake.set(ControlMode.PercentOutput, .2);
        }
        else{
            intake.set(ControlMode.PercentOutput, 0);
        }

        if(monkey.getRawButton(LogitechButton.B)){
            vBrush.set(ControlMode.PercentOutput, .2);
            lBrush.set(.2);
            rBrush.set(.2);
        }
        else if(monkey.getRawButton(LogitechButton.Y)){
            vBrush.set(ControlMode.PercentOutput, -.2);
        }
        else{
            vBrush.set(ControlMode.PercentOutput, 0);
        }


        if(monkey.getRawButton(LogitechButton.X)){
            lBrush.set(.2);
            rBrush.set(.2);
        }
        else{
            lBrush.set(0);
            rBrush.set(0);
        }



    }
    



}