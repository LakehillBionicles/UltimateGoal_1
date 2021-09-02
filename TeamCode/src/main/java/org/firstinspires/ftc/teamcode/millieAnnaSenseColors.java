package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;



@TeleOp
//Disabled
public class millieAnnaSenseColors extends LinearOpMode {

    ColorSensor eyeBalls;


    VhisolaHardware robot = new VhisolaHardware();

    @Override

    public void runOpMode() {

        new millieAnnaSenseColors();
        eyeBalls = hardwareMap.colorSensor.get("eyeBalls");
        waitForStart();

        while (opModeIsActive()) {
            telemetry.addData("red", eyeBalls.red());
            telemetry.addData("green", eyeBalls.green());
            telemetry.addData("blue", eyeBalls.blue());
            telemetry.update();


          //  while (eyeBalls.alpha() < 20) {
                //  encoderDrive(.6);
          //  }
          //  sleep(200);

          //  stop();///////////////////////////////////////////////

        }
    }

    public millieAnnaSenseColors() {
    }
}
