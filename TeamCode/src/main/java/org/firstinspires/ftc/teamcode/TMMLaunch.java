package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp

//@Disabled
public class TMMLaunch extends LinearOpMode {

        public DcMotor m1;
        public DcMotor m2;

        //public Servo butterKnife;


    public void runOpMode() {
        m1 = hardwareMap.get(DcMotor.class, "m1");
        m2 = hardwareMap.get(DcMotor.class, "m2");
        //butterKnife = hardwareMap.get(Servo.class, "butterKnife");


        m1.setDirection(DcMotorSimple.Direction.REVERSE);
        m2.setDirection(DcMotorSimple.Direction.FORWARD);



        m1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        m2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        telemetry.addData("Status:", "Run time dudes");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {

            shoot();
            //slice();

        }
    }
    public void shoot(){
        if(gamepad1.a) {   //shoot
            m1.setPower(1);
            m2.setPower(1);
        }
            else if(gamepad1.b){  //shoot at .9
                m1.setPower(0.9);
                m2.setPower(0.9);
            }

            else if(gamepad1.x){ // shot at 80
                m1.setPower(0.8);
                m2.setPower(0.8);
            }
            else if(gamepad1.y){ //shot at 70
                m1.setPower(0.7);
                m2.setPower(0.7);
            }

        else{           //standby
            m1.setPower(0.0);
            m2.setPower(0.0);

        }

    }

   /* public void slice(){

        if(gamepad1.b && butterKnife.getPosition()!=0.50){   //slice

            butterKnife.setPosition(0.50);

        }else if(gamepad1.x && butterKnife.getPosition()!=0.1){  //reset
            butterKnife.setPosition(0.1);

        }

    }*/

}
