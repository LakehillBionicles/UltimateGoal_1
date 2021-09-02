package org.firstinspires.ftc.teamcode.Autos;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.VhisolaHardware;

@Autonomous

@Disabled

public class TwoWobble extends AutonBAse {

    VhisolaHardware robot = new VhisolaHardware();

    @Override

    public void runOpMode() {

        startUp();

        waitForStart();

        while (opModeIsActive()) {

            telemetry.addData(">", "Let's gooooooooooooooooooooooooo");
            telemetry.update();
            int numRings = AltVision();

            if(numRings == 1) {
                telemetry.addData("Single", "&&&&&&&&&&&&&&&&&");
                telemetry.update();
                sleep(500);

                encoderDrive(.35 , -10, -10, 10); //backward to align with box

                sideways(.4, 112, 112, 10); //move to box    might need to move around ring, but its not really a deal i dont think

                encoderWobbleMove(.95, -30, 1.5);
                //encoderWobbleMove(.7, -15, 1.0);// arm put wobble down

                wobbleFruitOpenSesame();     //open finger
                sleep(900);

                sideways(.4, -36, -36, 10); // go back to start

                encoderDrive(.35 , 10, 10, 10); // move back

                grabSecondWobble(); // see below function

                sideways(.4, 112, 112, 10); //move to box    might need to move around ring, but its not really a deal i dont think

                encoderDrive(.35 , -10, -10, 10); // move over to get arm in box

                encoderDrive(.3 , -5, 0, 10); //fix

                setWobbleDown();

            } else if(numRings == 4) {
                telemetry.addData("Quad", "!!!!!!!!!!!!!!!!!!!!!!!!!");
                telemetry.update();
                sleep(500);

                encoderDrive(.5, 9, 9, 20);  //forward to align with box

                sideways(.5, 144, 144, 15);  //move to box

                sideways(.5, 4, 0, 20);  //fix

                encoderWobbleMove(.95, -30, 1.5);
                //encoderWobbleMove(.7, -15, 1.0);// arm put wobble down

                wobbleFruitOpenSesame();     //open finger
                sleep(900);

                sideways(.5, -144, -144, 15); // move back to start

                encoderDrive(.5,-9,-9,20); //backwards, back to start


                grabSecondWobble();                                         // see below function


                sideways(.4, 144, 144, 15);  //move to box

                sideways(.4, 3.5, 0, 20);  //fix

                //encoderDrive(.4, 9, 9, 20);  //move over, align with box

                setWobbleDown();

                sideways(.4, -55, -55, 20); // park

            } else if (numRings == 0) {
                telemetry.addData("No Ring", "ybdwhiojsuqwdbvehbofdcwinjmnqbhwpidoqmka");
                telemetry.update();
                sleep(500);

                encoderDrive(.5, 10, 10, 20); //forward align with box

                sideways(.45, 66.5, 71.5, 20);   // travel to box

                sideways(.5, 5, 0, 20);  //fix

                encoderWobbleMove(.95, -30, 1.5);
                //encoderWobbleMove(.7, -15, 1.0);// arm put wobble down

                wobbleFruitOpenSesame();     //open finger
                sleep(900);

                sideways(.5,-66.5,-71.5,20); //move back to start

                grabSecondWobble();

                encoderDrive(.45, 10, 10, 20); //forward align with box

                sideways(.45, 66.5, 71.5, 20);   // travel to box

                sideways(.5, 5, 0, 20);  //fix

                setWobbleDown();

                encoderDrive(.4,-20,-20,10); // move towards center

                sideways(.4, 15, 15, 20);   // park
            }


            stop();
        }
    }

    public void grabSecondWobble(){

        sleep(500);

        encoderDrive(.2,38,-38,10); //turn 90 to the right

        stop();

        sideways(.4,8.5,8.5,10); //move up to wobblegoal

        //encoderWobbleMove(.95, -25, 1.5); //move arm down

        encoderDrive(.2,-8.5,8.5,10); //turn to the left to grab wobblegoal

        wobbleFruitClose(); //grab 2nd wobblegoal
        sleep(1000);

        encoderWobbleMove(.8, 40, 1.5);  // pull arm back up
        sleep(1000);

        stop();

        //sideways(.4,-5,-5,10); //move back from wobblegoal

        encoderDrive(.2,-30, 30,10); //turn 90 to the left


    }
}
