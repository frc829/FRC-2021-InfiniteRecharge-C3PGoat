package frc.autos;



import frc.robot.*;

public class Course extends Auto{

    public Course(Drive d, Shoot s, Intake i){
        super("Course", d, s, i);
    }

    @Override
    public void execute() {
        switch(this.getStep()){
            case 0: {
                if(this.drive.driveToDistance(.2, 180)){
                    this.nextStep();
                }
                break;
            }
            case 1: {
                if(!this.drive.driveToAngle(-120)){
                    this.drive.resetEncoders();
                    
                    this.nextStep();
                }
                break;

            }
            case 2: {
                if(this.drive.driveToDistance(.3,36)){
                    this.nextStep();
                }
                break;

            }
            case 3: {
                if(!this.drive.driveToAngle(-135)){
                    this.drive.resetEncoders();
                    
                    this.nextStep();
                }
                break;

            }
            case 4: {
                if(this.drive.driveToDistance(.3,36)){
                    this.nextStep();
                }
                break;

            }
            case 5: {
                if(!this.drive.driveToAngle(-135)){
                    this.drive.resetEncoders();
                    
                    this.nextStep();
                }
                break;

            }
            case 7: {
                if(this.drive.driveToDistance(.3,36)){
                    this.nextStep();
                }
                break;

            }
            case 8: {
                if(this.drive.driveToDistance(.3,36)){
                    this.nextStep();
                }
                break;

            }
            default:{
                this.drive.stopAllMotors();
            }
        }
    }
}