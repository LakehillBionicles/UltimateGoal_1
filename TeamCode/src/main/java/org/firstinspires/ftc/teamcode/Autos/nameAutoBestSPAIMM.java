package org.firstinspires.ftc.teamcode.Autos;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.VhisolaHardware;

@Autonomous(name="THE auto")

//@Disabled

public class nameAutoBestSPAIMM extends AutonBAse {

    VhisolaHardware robot = new VhisolaHardware();

    @Override

    public void runOpMode() {

        startUp();

       // led.loop();  // trythisoutlater???

        waitForStart();

        while (opModeIsActive()) {

            telemetry.addData(">", "Let's gooooooooooooooooooooooooo");
            telemetry.update();

            visionStuffDetect();

            sleep(500);

            launcherLaunch(.74, 1000, 5);

            encoderDrive(.3, -13, -13, 5);

            stop();////////////////////////////////


        }
    }
}
