package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.VhisolaHardware;


@TeleOp

//@Disabled

public class VhisolaOp extends LinearOpMode {

    VhisolaHardware robot =  new VhisolaHardware();

    //private double power = .5;

    @Override
    public void runOpMode(){

        robot.init(hardwareMap);

          //ALREADY IN HWMAP????
        robot.fpd.setDirection(DcMotorSimple.Direction.FORWARD);
        robot.bpd.setDirection(DcMotorSimple.Direction.FORWARD);
        robot.fsd.setDirection(DcMotorSimple.Direction.REVERSE);
        robot.bsd.setDirection(DcMotorSimple.Direction.REVERSE);


        robot.fpd.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.bpd.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.fsd.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.bsd.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        //robot.bpd.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //robot.bsd.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        telemetry.addData("Status:", "Run time dudes");
        telemetry.update();

        waitForStart();

        while(opModeIsActive()){

            drive();
            gamepadLocal();
            spin();
            wobbleLifter();
            wobbleHand();
            intakeMaki();
            slice();
            shoot();
            resetBECounters();
            getDistance();


            telemetry.addData("sideways movement pos: ", (robot.bpd.getCurrentPosition()/ robot.COUNTS_PER_INCH_BE));
            telemetry.addData("for/back movement pos: ", (-robot.bsd.getCurrentPosition()/ robot.COUNTS_PER_INCH_BE));
            telemetry.addData("the reset button is dpad up or down", "");
            telemetry.addData("cm: ", robot.plsWork.getDistance(DistanceUnit.CM)); //add to telemetry update
            telemetry.update();


        }

    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////

   public void drive(){
       if(gamepad1.right_stick_x < 0.2 && gamepad1.right_stick_x > -0.2 && gamepad1.right_stick_y < -0.5) {   //forward
           robot.fpd.setPower(gamepad1.right_stick_y);
           robot.bpd.setPower(gamepad1.right_stick_y);
           robot.fsd.setPower(gamepad1.right_stick_y);
           robot.bsd.setPower(gamepad1.right_stick_y);
           telemetry.addData("for","");

       } else if(gamepad1.right_stick_x > -0.2 && gamepad1.right_stick_x < 0.2 && gamepad1.right_stick_y > 0.5){   //backwards
           robot.fpd.setPower(gamepad1.right_stick_y);
           robot.bpd.setPower(gamepad1.right_stick_y);
           robot.fsd.setPower(gamepad1.right_stick_y);
           robot.bsd.setPower(gamepad1.right_stick_y);
           telemetry.addData("back","");


       } else if (gamepad1.right_stick_x > 0.5 && (gamepad1.right_stick_y > -0.25 && gamepad1.right_stick_y < 0.25) ){  //star


           robot.fsd.setPower(gamepad1.right_stick_x);
           robot.bsd.setPower(-gamepad1.right_stick_x);
           robot.fpd.setPower(-gamepad1.right_stick_x);
           robot.bpd.setPower(gamepad1.right_stick_x);

           telemetry.addData("star","");

       } else if ((gamepad1.right_stick_x < -0.5 && gamepad1.right_stick_y > -0.25 && gamepad1.right_stick_y < 0.25) ) {  //port

           robot.fsd.setPower(gamepad1.right_stick_x);
           robot.bsd.setPower(-gamepad1.right_stick_x);
           robot.fpd.setPower(-gamepad1.right_stick_x);
           robot.bpd.setPower(gamepad1.right_stick_x);

           telemetry.addData("port","");

        /*}else if((gamepad1.right_stick_x > -1 && gamepad1.right_stick_x < -0.2) && (gamepad1.right_stick_y < -.25 && gamepad1.right_stick_y > -1)){    //front port (left)
                robot.fpd.setPower(0.0);
                robot.bpd.setPower(gamepad1.right_stick_y);
                robot.bsd.setPower(0.0);
                robot.fsd.setPower(gamepad1.right_stick_y);
            telemetry.addData("fp","");

            }else if(gamepad1.right_stick_x > 0.2 && gamepad1.right_stick_x < 1 && gamepad1.right_stick_y > -1 && gamepad1.right_stick_y < -0.25){  //front star (right)
                robot.fpd.setPower(gamepad1.right_stick_y);
                robot.bpd.setPower(0.0);
                robot.bsd.setPower(gamepad1.right_stick_y);
                robot.fsd.setPower(0.0);
            telemetry.addData("fs","");


            }else if(gamepad1.right_stick_x > -1 && gamepad1.right_stick_x < -0.25  && gamepad1.right_stick_y > 0.25 && gamepad1.right_stick_y < 1){  //back port
                robot.fpd.setPower(gamepad1.right_stick_y);
                robot.bpd.setPower(0.0);
                robot.bsd.setPower(gamepad1.right_stick_y);
                robot.fsd.setPower(0.0);
            telemetry.addData("bp","");



            }else if(gamepad1.right_stick_x > 0.2 && gamepad1.right_stick_x < 1 && gamepad1.right_stick_y < 1 && gamepad1.right_stick_y > 0.25){   //back star
                robot.fpd.setPower(0.0);
                robot.bpd.setPower(gamepad1.right_stick_y);
                robot.bsd.setPower(0.0);
                robot.fsd.setPower(gamepad1.right_stick_y);
                telemetry.addData("bs","");

*/
       } else{
           robot.fpd.setPower(0.0);
           robot.bpd.setPower(0.0);
           robot.fsd.setPower(0.0);
           robot.bsd.setPower(0.0);

       }

       telemetry.update();

   }    //gamepad 1   -- no controller change

    public void gamepadLocal(){

        telemetry.addData("gamepad1 X;", gamepad1.right_stick_x );
        telemetry.addData("gamepad1 Y;", gamepad1.right_stick_y );
        telemetry.update();

    }

    public void spin(){

        if(gamepad1.left_stick_x > 0.86 && gamepad1.left_stick_y > -0.5 && gamepad1.left_stick_y < 0.5){    //clockwise
            robot.fpd.setPower(-gamepad1.left_stick_x);
            robot.bpd.setPower(-gamepad1.left_stick_x);
            robot.fsd.setPower(gamepad1.left_stick_x);
            robot.bsd.setPower(gamepad1.left_stick_x);


        }else if(gamepad1.left_stick_x < -0.86 && gamepad1.left_stick_y > -0.5  && gamepad1.left_stick_y < 0.5 ){    //counterclockwise
            robot.fpd.setPower(-gamepad1.left_stick_x);
            robot.bpd.setPower(-gamepad1.left_stick_x);
            robot.fsd.setPower(gamepad1.left_stick_x);
            robot.bsd.setPower(gamepad1.left_stick_x);

        }

    }

    public void wobbleHand(){
        if(gamepad1.x) {                                   //open servo
            robot.blueFruit.setPosition(robot.openBlue);
            robot.greyFruit.setPosition(robot.openGrey);

        }else if(gamepad1.b) {                              //closed servo
            robot.blueFruit.setPosition(robot.closedBlue);
            robot.greyFruit.setPosition(robot.closedGrey);
        }

    }

    public void wobbleLifter(){
        if(gamepad1.y){                                 //lift hopefully
           robot.wobbleMotor.setPower(robot.wobbleSpeed);
        } else if(gamepad1.a){                               //placed down hopefully
            robot.wobbleMotor.setPower(-(robot.wobbleSpeed));
        } else {
            robot.wobbleMotor.setPower(0.0);
        }

    }

    public void intakeMaki(){

        if(gamepad1.left_trigger != 0){                                 //eat
            robot.intakeTop.setPower(robot.intakeTopSpeed);
            robot.intakeBottom.setPower(robot.intakeSpeed);
        } else if(gamepad1.right_trigger != 0){                               //spit out
            robot.intakeTop.setPower(-(robot.intakeTopSpeed));
            robot.intakeBottom.setPower(-(robot.intakeSpeed));
        } else {
            robot.intakeTop.setPower(0.0);
            robot.intakeBottom.setPower(0.0);
        }



    }

    public void slice(){
        if( gamepad1.right_bumper) {
            robot.butterKnife.setPosition(robot.butterShoot);

            sleep(50);

        } else{
            robot.butterKnife.setPosition(robot.butterStop);
        }
    }

    public void shoot(){
        if(gamepad1.left_bumper){
            //robot.shooty.setVelocity(robot.shootVelo);
            robot.shooty.setPower(.74); //.72
        }
        else{
            robot.shooty.setPower(0);
        }

    }

    public void resetBECounters(){
        if(gamepad1.dpad_down || gamepad1.dpad_up){
            robot.bpd.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            robot.bsd.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            robot.bpd.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.bsd.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }

    }

    public void getDistance()
    {
        telemetry.addData("cm: ", robot.plsWork.getDistance(DistanceUnit.CM));

        if (robot.plsWork.getDistance(DistanceUnit.CM) < 6.3) { //3 rings 5-6

            robot.led.setPattern(RevBlinkinLedDriver.BlinkinPattern.GREEN);

        } else if (robot.plsWork.getDistance(DistanceUnit.CM) < 7.7) { //2 rings 7.3 - 8

            robot.led.setPattern(RevBlinkinLedDriver.BlinkinPattern.YELLOW);

        } else if (robot.plsWork.getDistance(DistanceUnit.CM) < 8.7) { //1 rings  8-9

            robot.led.setPattern(RevBlinkinLedDriver.BlinkinPattern.RED);

        } else {
            robot.led.setPattern(RevBlinkinLedDriver.BlinkinPattern.WHITE);
        }
    }

}

