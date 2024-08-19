package frc.robot;



//import edu.wpi.first.networktables.NetworkTableInstance;
import frc.util.LogitechF310;

public abstract class Auto{

    protected Drive drive;
    protected Shoot shooter;
    protected Intake intake;
   
    
    protected String name;
    protected int step;

    public LogitechF310 pilot = new LogitechF310(0);
    public LogitechF310 gunner = new LogitechF310(1);

    public Auto(String n, Drive d, Shoot s, Intake i){
        this.drive = d;
        this.shooter = s;
        this.intake = i;
       
        this.name = n;
        this.step = 0;
        
    }

    public abstract void execute();


    public int getStep(){
        return this.step;
    }

    public void nextStep(){
        System.out.println("**********NEXT STEP***********");
        this.step += 1;
    }

   

    public String getName(){
        return this.name;
    }

}