package org.firstinspires.ftc.teamcode.Autos;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.VhisolaHardware;

@Autonomous

@Disabled

public class praticeAuto extends AutonBAse{

    VhisolaHardware robot = new VhisolaHardware();


    @Override

    public void runOpMode() {

        startUp();

        waitForStart();

        while (opModeIsActive()) {

            telemetry.addData("--","hopefully this works");
            telemetry.update();


            setWobbleDown();

           stop();

            encoderDrive(0.4, 12, 12, 10); //move forward about 1 foot

            encoderDrive(0.4, -12, -12, 10); //move backwards about 1 foot

            sideways(0.4, 12, 12,10); //move sideways hopefully right

            sideways(0.4, -12, -12, 10); //move sideways hopefully left

            encoderDrive(0.4, 12, 0, 10); //should turn to the left a little bit ???????

            encoderDrive(0.4, -20, 20,10); //should turn to the right a decent amount of numbers

            stop();


        }

    }

  }
