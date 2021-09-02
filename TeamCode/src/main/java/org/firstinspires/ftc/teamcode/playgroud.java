package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Autos.AutonBAse;
import org.firstinspires.ftc.teamcode.VhisolaHardware;

import java.util.List;

@Autonomous

//@Disabled
public class playgroud extends AutonBAse {


    VhisolaHardware robot = new VhisolaHardware();   ///alll groos get rid of ta

    String pine = "beep";

    // private ElapsedTime runtime = new ElapsedTime();


    @Override
    public void runOpMode() {

        startUp();


        waitForStart();


        while (opModeIsActive()) {
            telemetry.addData(">", "Let's gooooooooooooooooooooooooo");
            telemetry.update();

            launcherLaunch(.675, 1000, 7); //fixie??? power

            stop();

            encoderDrive(.35, 7, 7, 20); //forward align with box

            sideways(.3, 50, 50, 20);   // travel to box

            sideways(.3, 2, 0, 20);  //fix

            setWobbleDown();

            encoderDrive(.2, -16.5, 16.5, 10);  //spin

            sideways(.3, -38, -38, 20); //align with pwrst

            stop();

            launcherLaunch(.675, 1000, 7); //fixie??? power

            encoderDrive(.6, -12,-12,6);

            stop();


        }
    }
}



