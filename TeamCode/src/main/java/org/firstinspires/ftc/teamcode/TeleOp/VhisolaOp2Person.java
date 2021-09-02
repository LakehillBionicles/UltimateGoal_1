package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcontroller.external.samples.LEDClass;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.internal.system.Deadline;
import org.firstinspires.ftc.teamcode.VhisolaHardware;

import java.util.concurrent.TimeUnit;

////////////////////////////////////WHAT CONTROLS DO DRIVERS WANT???? ARE THESE GOOD
@TeleOp(name = "2 Drivers")

//@Disabled

public class VhisolaOp2Person extends LinearOpMode {

    //NEEDED??????????????????????????????
    // private Blinker controlHub;

    VhisolaHardware robot =  new VhisolaHardware();
    LEDClass led = new LEDClass();

    public ElapsedTime runtime = new ElapsedTime();

    static final double COUNTS_PER_MOTOR_REV = 560; ///22.9
    static final double DRIVE_GEAR_REDUCTION = .27;     // This is < 1.0 if geared UP  .385 true
    static final double WHEEL_DIAMETER_INCHES = 3.0;     // For figuring circumference    2.953!!!!!!!!!!!!!!!!
    static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);



    @Override
    public void runOpMode(){

        //DO I NEED THIS??????????????????
        // controlHub = hardwareMap.get(Blinker.class, "Control Hub Portal");
        robot.init(hardwareMap);


        robot.fpd.setDirection(DcMotorSimple.Direction.FORWARD);
        robot.bpd.setDirection(DcMotorSimple.Direction.FORWARD);
        robot.fsd.setDirection(DcMotorSimple.Direction.REVERSE);
        robot.bsd.setDirection(DcMotorSimple.Direction.REVERSE);


        robot.fpd.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.bpd.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.fsd.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.bsd.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

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
            //led.loop();


            //telemetry.addData("robot.fpd pos:", robot.fpd.getCurrentPosition());
            //telemetry.update();

            telemetry.addData("sideways movement pos %7d :%7d", (robot.bpd.getCurrentPosition()/ robot.COUNTS_PER_INCH_BE));
            telemetry.addData("for/back movement pos %7d :%7d", (-robot.bsd.getCurrentPosition()/ robot.COUNTS_PER_INCH_BE));
            telemetry.addData("the reset button are the triggers", "");
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

        }else if (gamepad1.dpad_left){
           robot.fpd.setPower(.4);
           robot.bpd.setPower(.4);
           robot.fsd.setPower(-.4);
           robot.bsd.setPower(-.4);

        }else if (gamepad1.dpad_right){
            robot.fpd.setPower(-.4);
            robot.bpd.setPower(-.4);
            robot.fsd.setPower(.4);
            robot.bsd.setPower(.4);


        }

    }    //gamepad 1  -- no controller change

    public void wobbleHand(){
        if(gamepad2.x) {                                   //open servo
            robot.blueFruit.setPosition(robot.openBlue);
            robot.greyFruit.setPosition(robot.openGrey);

        }else if(gamepad2.b) {                              //closed servo
            robot.blueFruit.setPosition(robot.closedBlue);
            robot.greyFruit.setPosition(robot.closedGrey);
        }

    }  //gamepad 2

    public void wobbleLifter(){
        if(gamepad2.y){                                 //lift hopefully
            robot.wobbleMotor.setPower(robot.wobbleSpeed);
        } else if(gamepad2.a){                               //placed down hopefully
            robot.wobbleMotor.setPower(-(robot.wobbleSpeed));
        } else {
            robot.wobbleMotor.setPower(0.0);
        }

    }  //gamepad 2

    public void intakeMaki(){

        if(gamepad2.dpad_up){                                  //eat
            robot.intakeTop.setPower(robot.intakeTopSpeed);
            robot.intakeBottom.setPower(robot.intakeSpeed);
        } else if(gamepad2.dpad_down){                        //spit out
            robot.intakeTop.setPower(-(robot.intakeTopSpeed));
            robot.intakeBottom.setPower(-(robot.intakeSpeed));
        } else {
            robot.intakeTop.setPower(0.0);
            robot.intakeBottom.setPower(0.0);
        }

    }  //gamepad 2

    public void slice(){
        if(gamepad1.right_bumper || gamepad1.left_bumper) {
            robot.butterKnife.setPosition(robot.butterShoot);
            //handleGamepad();
            sleep(50);

        } else{
            robot.butterKnife.setPosition(robot.butterStop);
        }
    }  //gamepad1

    public void shoot(){
        if(gamepad2.right_trigger > 0){
            encoderLaunchy(.74, 900, 10 );

        }else {
            robot.shooty.setPower(0);

        }
    }   //gamepad 2

    public boolean encoderLaunchy(double speed, double inches, double timeoutS) {  //move wobble motor up down!!!!!!!!!!FIND BOUNDSSSSSSSS

        int newTarget = 0;



        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newTarget = robot.shooty.getCurrentPosition() + (int) (inches * COUNTS_PER_INCH);


            robot.shooty.setTargetPosition(newTarget);


            int NT = (int) (newTarget * 0.981); //fp



            // Turn On RUN_TO_POSITION
            robot.shooty.setMode(DcMotor.RunMode.RUN_TO_POSITION);



            // reset the timeout time and start motion.
            runtime.reset();
            robot.shooty.setPower(Math.abs(speed));

            //fruitShootMove();


            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    // ( front_star_wheel.isBusy() && front_port_wheel.isBusy()))
                    ((Math.abs(robot.shooty.getCurrentPosition()) < Math.abs(NT)))) {

                // Display it for the driver.
                telemetry.addData("Path1", "Running to %7d", newTarget);
                telemetry.addData("Path2", "Running at %7d",
                        robot.shooty.getCurrentPosition());

                telemetry.update();
                //fruitShootMove();
                return true;
            }
            //fruitShootMove();

            // Stop all motion;
            robot.shooty.setPower(0);


            // Turn off RUN_TO_POSITION
            robot.shooty.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            robot.shooty.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            sleep(250);   // optional pause after each move
            return false;
        }
        return false;
    }

    public void resetBECounters(){
        if(gamepad1.dpad_down || gamepad1.dpad_up){
            robot.bpd.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            robot.bsd.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            robot.bpd.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.bsd.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }

    }

    public void gamepadLocal(){

        telemetry.addData("gamepad1 X;", gamepad1.right_stick_x );
        telemetry.addData("gamepad1 Y;", gamepad1.right_stick_y );
        telemetry.update();

    }

    public void getDistance() {
        telemetry.addData("cm: ", robot.plsWork.getDistance(DistanceUnit.CM));

        if (robot.plsWork.getDistance(DistanceUnit.CM) < 6.3) { //3 rings --> 6

            robot.led.setPattern(RevBlinkinLedDriver.BlinkinPattern.GREEN);

        } else if (robot.plsWork.getDistance(DistanceUnit.CM) < 7.7) { //2 rings --> 8.2

            robot.led.setPattern(RevBlinkinLedDriver.BlinkinPattern.YELLOW);

        } else if (robot.plsWork.getDistance(DistanceUnit.CM) < 8.7) { //1 ring  9.9

            robot.led.setPattern(RevBlinkinLedDriver.BlinkinPattern.RED);

        } else {
            robot.led.setPattern(RevBlinkinLedDriver.BlinkinPattern.WHITE);
        }
    } //lights and distance sensors

}





