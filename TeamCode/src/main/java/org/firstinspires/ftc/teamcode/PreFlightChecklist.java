package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorSimple;


//what does this do

//pre game check if all components work properly (motor servo together)
//telemetry reminders???????????? at end

@TeleOp

//@Disabled
public class PreFlightChecklist extends VhisolaHardware {

    VhisolaHardware robot =  new VhisolaHardware();

    //private ElapsedTime runtime = new ElapsedTime();

    @Override

    public void runOpMode(){


        robot.init(hardwareMap);

        robot.fpd.setDirection(DcMotorSimple.Direction.REVERSE);
        robot.bpd.setDirection(DcMotorSimple.Direction.REVERSE);
        robot.fsd.setDirection(DcMotorSimple.Direction.FORWARD);
        robot.bsd.setDirection(DcMotorSimple.Direction.FORWARD);

        telemetry.addData("Status", "ready to go 'wahoo'- mario");    //
        telemetry.update();

        waitForStart();


        while(opModeIsActive()){

            if(gamepad1.right_trigger!=0){

                driveAnna();
            /*}if else(something){
                something
              */
            }else{
                powerOffMillie();

            }


            //servo.setPosition(.0);

        }


    }

    public void driveAnna(){ //
        fpd.setPower(.5);
        fsd.setPower(.5);
        bpd.setPower(.5);
        bsd.setPower(.5);

    }

    public void powerOffMillie(){
        fpd.setPower(0);
        fsd.setPower(0);
        bpd.setPower(0);
        bsd.setPower(0);

    }


}
