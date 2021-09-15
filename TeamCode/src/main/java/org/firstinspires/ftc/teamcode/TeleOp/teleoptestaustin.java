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


        }

    }


  
   public void drive(){
       if(gamepad1.right_stick_x < 0.2 && gamepad1.right_stick_x > -0.2 && gamepad1.right_stick_y < -0.2) {   //forward
           robot.fpd.setPower(gamepad1.right_stick_y);
           robot.bpd.setPower(gamepad1.right_stick_y);
           robot.fsd.setPower(gamepad1.right_stick_y);
           robot.bsd.setPower(gamepad1.right_stick_y);
           telemetry.addData("for","");

       } else if(gamepad1.right_stick_x > -0.2 && gamepad1.right_stick_x < 0.2 && gamepad1.right_stick_y > 0.2){   //backwards
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
          
       } else if ((gamepad1.right_stick_x < -0.5 && gamepad1.right_stick_y < -0.5) ) {  //dianganol front left

           robot.fsd.setPower((gamepad1.right_stick_x+gamepad1.right_stick_y)/2);
           robot.bpd.setPower((gamepad1.right_stick_x+gamepad1.right_stick_y)/2);
           
           telemetry.addData("port","");
          } else if ((gamepad1.right_stick_x > 0.5 && gamepad1.right_stick_y < -0.5) ) {  //dianganol front right

           robot.fpd.setPower((gamepad1.right_stick_x+gamepad1.right_stick_y)/2);
           robot.bsd.setPower((gamepad1.right_stick_x+gamepad1.right_stick_y)/2);
           
           telemetry.addData("port","");
        } else if ((gamepad1.right_stick_x < -0.5 && gamepad1.right_stick_y < 0.5) ) {  //dianganol back right

           robot.fsd.setPower((-gamepad1.right_stick_x+gamepad1.right_stick_y)/2);
           robot.bpd.setPower((-gamepad1.right_stick_x+gamepad1.right_stick_y)/2);
           
           telemetry.addData("port","");
             } else if ((gamepad1.right_stick_x < 0.5 && gamepad1.right_stick_y < 0.5) ) {  //dianganol back left

           robot.fpd.setPower((-gamepad1.right_stick_x+gamepad1.right_stick_y)/2);
           robot.bsd.setPower((-gamepad1.right_stick_x+gamepad1.right_stick_y)/2);
           
           telemetry.addData("port","");
           
           
