package frc.robot;

import com.analog.adis16470.frc.ADIS16470_IMU;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import frc.util.Limelight;
import frc.util.LogitechAxis;
import frc.util.LogitechButton;
import frc.util.LogitechF310;

public class Drive{
    
    LogitechF310 driver, monkey;
    CANSparkMax leftBack, leftFront, rightBack, rightFront;
    boolean speedCalculated = false;
    ADIS16470_IMU gyro;

    long lastPolarShift;
    boolean driveInverted = false;
    public Drive(LogitechF310 driver, LogitechF310 monkey, ADIS16470_IMU gyro){
        
        try{
            this.driver = driver; 
            this.monkey = monkey;
            this.gyro = gyro;

            gyro.reset();
            leftFront = new CANSparkMax(11, MotorType.kBrushless);
            rightFront = new CANSparkMax(13, MotorType.kBrushless);
            leftBack = new CANSparkMax(10, MotorType.kBrushless);
            rightBack = new CANSparkMax(12, MotorType.kBrushless);
          
            // leftFront = new CANSparkMax(13, MotorType.kBrushless);
            // rightFront = new CANSparkMax(11, MotorType.kBrushless);
            // leftBack = new CANSparkMax(12, MotorType.kBrushless);
            // rightBack = new CANSparkMax(10, MotorType.kBrushless);

            // leftFront.setInverted(true);
            // rightBack.setInverted(true);
            // rightFront.setInverted(true);

            leftFront.setInverted(true);
            leftBack.setInverted(true);
            rightBack.setInverted(false);
            rightFront.setInverted(false);
            resetEncoders();

            lastPolarShift = System.currentTimeMillis();
        }
        catch(Exception e){

        }
    }

    public void shiftPolar() {
		if (System.currentTimeMillis() - lastPolarShift > 1500) {
			leftFront.setInverted(!leftFront.getInverted());
			leftBack.setInverted(!leftBack.getInverted());
			
			rightBack.setInverted(!rightBack.getInverted());
			rightFront.setInverted(!rightFront.getInverted());
            
            driveInverted = !driveInverted;
            lastPolarShift = System.currentTimeMillis();
            
		}
	}

    
    public void teleopUpdate(){


        if (driver.getRawButton(LogitechButton.START)) {
			shiftPolar();
		}


        //manual maxspeed to slow the drive down.  
        //double maxSpeed = 0.5;
        double left = 0;
        double right = 0;
        
        if(Math.abs(driver.getAxis(LogitechAxis.LY)) > .2 || Math.abs(driver.getAxis(LogitechAxis.RY)) > .2){            
            
            if(!driveInverted){
                left = driver.getAxis(LogitechAxis.LY);
                right = driver.getAxis(LogitechAxis.RY);
            }
            else{
                left = driver.getAxis(LogitechAxis.RY);
                right = driver.getAxis(LogitechAxis.LY);
            }

            /*
            if(Math.abs(left) > maxSpeed) {
                if(Math.signum(left) == -1)
                    left = -maxSpeed;
                else
                    left = maxSpeed;
            }
            if(Math.abs(right) > maxSpeed) {
                if(Math.signum(right) == -1)
                    right = -maxSpeed;
                else
                    right = maxSpeed;
            }
            */

            left *= .6;
            right*= .6; // change this to change the speed

            leftBack.set(left);
            leftFront.set(left);
            rightBack.set(right);
            rightFront.set(right);

            

        }        
        else{
            leftBack.set(0);
            leftFront.set(0);
            rightBack.set(0);
            rightFront.set(0);
        }

        if(driver.getRawButton(LogitechButton.A)){
            autoAim();
        }
    }

    public boolean driveToDistance(double speed, double distance){
        double encoderCounts = (13.2/(Math.PI * 9))*distance;
        if(Math.abs(leftBack.getEncoder().getPosition()) <= encoderCounts-.05){
            //System.out.println(brThrust.getEncoder().getPosition());
            moveForward(speed);
            return false;
        }
        else{
            resetEncoders();
            stopAllMotors();
            return true;
        }

    }

    public void resetEncoders(){
        leftBack.getEncoder().setPosition(0);
        leftFront.getEncoder().setPosition(0);
        rightBack.getEncoder().setPosition(0);
        rightFront.getEncoder().setPosition(0);
    }

    public void stopAllMotors() {
        moveForward(0);
    }

    public void moveForward(double x) {
        leftFront.set(-x);
        leftBack.set(-x);
        rightFront.set(-x);
        rightBack.set(-x);
    }

    public void brakeMode() {
        leftBack.setIdleMode(IdleMode.kBrake);
        leftFront.setIdleMode(IdleMode.kBrake);
        rightBack.setIdleMode(IdleMode.kBrake);
        rightFront.setIdleMode(IdleMode.kBrake);
    }

    public void coastMode() {
        leftBack.setIdleMode(IdleMode.kCoast);
        leftFront.setIdleMode(IdleMode.kCoast);
        rightBack.setIdleMode(IdleMode.kCoast);
        rightFront.setIdleMode(IdleMode.kCoast);
    }

    
    double right = 0;
    double rightRadius = 0;
    double rotationalSpeed = 0;
    public boolean circleRight(double left, double leftRadius){
        if(!speedCalculated){
             rightRadius = leftRadius -23;
             rotationalSpeed = left/(2*Math.PI*leftRadius);
             right = rotationalSpeed*2*Math.PI;
            speedCalculated = true;
        }
       
        double leftDistance = 2*Math.PI*leftRadius;
        double rightDistance = 2*Math.PI*rightRadius;

        double ratio = (leftRadius-23)/leftRadius; 

        
        double leftEncoderCounts = (13.2/(Math.PI * 9))*leftDistance;
        double rightEncoderCounts = (13.2/(Math.PI * 9))*rightDistance;
        
        if(Math.abs(leftBack.getEncoder().getPosition()) <= leftEncoderCounts){
            leftFront.set(-left);
            leftBack.set(-left);
            
            double leftDisplacement = Math.abs(leftBack.getEncoder().getPosition()) / (13.2/(Math.PI * 9)) ;
            double displacementRatio = leftDisplacement/leftDistance;

            double rightTargetEncoder = rightEncoderCounts*displacementRatio;
             

            System.out.println("Encoder Position: " + Math.abs(rightBack.getEncoder().getPosition()) + " Target: " + rightTargetEncoder);
            if(Math.abs(rightBack.getEncoder().getPosition()) <= rightTargetEncoder){
                right += .005;
                
                //System.out.println("Right Forward!");
            }
            else{
                
                right -= .05;
               //System.out.println("Right Back!");
            }

            rightBack.set(-right);
            rightBack.set(-right);
            
            
            return false;
        }
        else{
            speedCalculated = false;
            resetEncoders();
            stopAllMotors();
            return true;
        }

    }


    public void turnRight(double x) {
        leftFront.set(-x);
        leftBack.set(-x);
        rightFront.set(x);
        rightBack.set(x);
    }

    public void turnLeft(double x) {
        leftFront.set(x);
        leftBack.set(x);
        rightFront.set(-x);
        rightBack.set(-x);
    }


    long lastTurn = 1000000000;
    public boolean driveToAngle(double targetAngle){
        System.out.println(gyro.getAngle() + "   -   " + targetAngle);
        double outputSpeed = .2;
        //double outputSpeed = .1 + .2*Math.abs(gyro.getAngle()-startGyro-relativeTarget)/10;
        // if(!isTurning){
        //     startGyro = gyro.getAngle();
        //     isTurning = true;
        // }
        if(gyro.getAngle()-3>targetAngle){
            turnRight(outputSpeed);
            lastTurn = System.currentTimeMillis();
            return true;
        }
        else if(gyro.getAngle()+3<targetAngle){
            turnLeft(outputSpeed);
            lastTurn = System.currentTimeMillis();
            return true;
        }
        else{
            stopAllMotors();
            if(System.currentTimeMillis() - lastTurn > 500){
                //isTurning = true;
                return true;
            }
            return false;
        }
        

    }
    public void autoAim() {
        //double outputSpeed = -(3.822)*Math.pow((Math.abs(Limelight.getX())*Math.PI/180), 3)+.1;
        double outputSpeed = .06;
            if(Limelight.getV() == 1){
                if(Limelight.getX() > -2){
                    turnRight(outputSpeed);
                }
                else if(Limelight.getX() < -2.25){
                    turnLeft(outputSpeed);
                }
                else{
                    stopAllMotors();
                }
            }
    }
    
}
