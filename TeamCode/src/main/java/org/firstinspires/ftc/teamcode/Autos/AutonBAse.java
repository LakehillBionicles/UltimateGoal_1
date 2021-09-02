package org.firstinspires.ftc.teamcode.Autos;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcontroller.external.samples.LEDClass;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.teamcode.VhisolaHardware;

import java.util.List;

public class AutonBAse extends LinearOpMode {

    VhisolaHardware robot = new VhisolaHardware();
    //LEDClass led = new LEDClass();


    public ElapsedTime runtime = new ElapsedTime();

    static final double COUNTS_PER_MOTOR_REV = 560; ///22.9
    static final double DRIVE_GEAR_REDUCTION = (.39);     // This is < 1.0 if geared UP  .385 true
    static final double WHEEL_DIAMETER_INCHES = 2.95;     // For figuring circumference    2.953!!!!!!!!!!!!!!!!
    static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);

    public static final String TFOD_MODEL_ASSET = "UltimateGoal.tflite";
    public static final String LABEL_FIRST_ELEMENT = "Quad";
    public static final String LABEL_SECOND_ELEMENT = "Single";
    public String pineapple = "";
    int blub = 0;


    public double fruitSpeed = .28;

    private double spedAdjust = .15;
    private int boundBE = 5;



    public static final String VUFORIA_KEY =
            "Ae3RG1L/////AAABmVYRV0p8GU09m+QhproaeElLfsYiWMINNfBok2ejh+YHfQkb72DM5G+LFLzcN0Bk8CZGcNo0s+fPdjCkgMfOOc6v6cNWtSrwmiEArmUJbgsyxqgkfLNszYiQzWLGjYcC/ZkGYIZum/AENGK6cYC0AVXr15L1Irr9u2Ab2krwwiv59xRtfXLuy9fsrxvbwWLBxJl7XZ4fnp22L9v9DkWAqcTzwSgevalxPgOOiH6Ric1aWhSUUQGsOaD9VX72OU6UJT82/O83xZISIWvLYUqzrasgqMVdJUbkCtIF9KQcmS+JlLZ2xz7ix2NMMIdP45R5Cu1xML3e/ebZHlU26EuuBhTGZOmhJHwmCVTwcXXikKnB";

    /*** {@link #vuforia} is the variable we will use to store our instance of the Vuforia
     * localization engine.*/
    public VuforiaLocalizer vuforia;


    /*** {@link #tfod} is the variable we will use to store our instance of the TensorFlow Object
     * Detection engine.*/
    public TFObjectDetector tfod;

    public AutonBAse() {}  //constructor

    @Override
    public void runOpMode() {
        startUp();
    }

    public void startUp() {
        robot.init(hardwareMap);

        setMotorDirDrive();


        robot.fsd.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.bsd.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.fpd.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.bpd.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        telemetry.addData("Status", "Resetting Encoders");    //
        telemetry.update();

        robot.fpd.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.fsd.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.bpd.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.bsd.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        robot.fsd.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.fpd.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.bsd.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.bpd.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Send telemetry message to indicate successful Encoder reset
        telemetry.addData("Path0", "Starting at %7d :%7d",
                robot.fpd.getCurrentPosition(),
                robot.bsd.getCurrentPosition());
        telemetry.update();

        initVuforia();
        initTfod();

        if (tfod != null) {
            tfod.activate();

            // The TensorFlow software will scale the input images from the camera to a lower resolution.
            // This can result in lower detection accuracy at longer distances (> 55cm or 22").
            // If your target is at distance greater than 50 cm (20") you can adjust the magnification value
            // to artificially zoom in to the center of image.  For best results, the "aspectRatio" argument
            // should be set to the value of the images used to create the TensorFlow Object Detection model
            // (typically 1.78 or 16/9).

            // Uncomment the following line if you want to adjust the magnification and/or the aspect ratio of the input images.*SARAHSARAHSSARAHSARAHSARAHMAKI***********************************************************************************
            tfod.setZoom(2.5, 16/9);
        }

        encoderWobbleMove(.93, 1, 1);

        /** Wait for the game to begin */
        telemetry.addData(">", "Press Play to start op mode");
        telemetry.update();


    }

    public void visionStuffDetect() {
        pineapple = "";

        //while (opModeIsActive()) {    //change modifier so it can kick???????
        if (tfod != null && pineapple.equals("")) {
            // getUpdatedRecognitions() will return null if no new information is available since
            // the last time that call was made.
            List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
            if (updatedRecognitions != null) {

                telemetry.addData("# Object Detected", updatedRecognitions.size());      //size!!!!!!!!!!!!!!!! is the determinant for wht box path to take????VALUESSS???????????????
                // step through the list of recognitions and display boundary info.
                int i = 0;
                for (Recognition recognition : updatedRecognitions) {
                    telemetry.addData(String.format("label (%d)", i), recognition.getLabel());
                    telemetry.addData(String.format("  left,top (%d)", i), "%.03f , %.03f",
                            recognition.getLeft(), recognition.getTop());
                    telemetry.addData(String.format("  right,bottom (%d)", i), "%.03f , %.03f",
                            recognition.getRight(), recognition.getBottom());
                    pineapple = recognition.getLabel();
                    sleep(500);  //just added

                }

                // blub = updatedRecognitions.size();

                telemetry.update();


            }
        }

        testVision();
    //}



    if(tfod !=null)

    {
        tfod.shutdown();
    }
}

    public void testVision(){

        if(pineapple.equals("Single")){   //1   //FIXXXXXXXXX
            telemetry.addData("Single", "&&&&&&&&&&&&&&&&&");
            telemetry.update();
            sleep(500);


            sideways(.3, 76, 76, 10); //move to box    might need to move around ring, but its not really a deal i dont think

            encoderDrive(.35 , -5, -5, 10); // move over to get arm in box

            encoderDrive(.3 , -4.3, 0, 10); //fix

            setWobbleDown();

            sideways(.4, -16, -16, 10); // go behind line

            encoderDrive(.2, -15.2, 15.2, 10);  //spin

            //sideways(.5, -13.5, -13.5, 10); // get over lil doggie


            return;



        }else if(pineapple.equals("Quad")){  //4

            telemetry.addData("Quad", "!!!!!!!!!!!!!!!!!!!!!!!!!");
            telemetry.update();
            sleep(500);

            encoderDrive(.35, 5.5, 5.5, 20);  //move over, align with box

            sideways(.32, 95, 95, 15);  //move to box

            sideways(.3, 2.5, 0, 20);  //fix

            setWobbleDown();

            sideways(.32, -36, -36, 20); // move behind line

            encoderDrive(.4, -13.8, 13.8, 10);  //spin SPIN

            sideways(.3, -20.5, -20.5, 20); //align with goAL

            return;

        }else{   //none  used to be else if blub ==0

            telemetry.addData("No Ring", "ybdwhiojsuqwdbvehbofdcwinjmnqbhwpidoqmka");
            telemetry.update();
            sleep(500);


            encoderDrive(.35, 7, 7, 20); //forward align with box

            sideways(.3, 54, 54, 20);   // travel to box

            sideways(.3, 2.3, 0, 20);  //fix

            setWobbleDown();

            encoderDrive(.2, -15, 15, 10);  //spin

            sideways(.3, -19, -19, 20); //align with goal

            return;


        }

    }

    public void shootVelo(double velocity){
        double velo = velocity;
        robot.shooty.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        robot.shooty.setVelocity(velo);


    }

    public void encoderDrive(double speed, double leftInches, double rightInches, double timeoutS) {  //forward back and turn with encoder, always do 3 less

        int newLeftTarget = 0;
        int newRightTarget = 0;

        setMotorDirDrive();


        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newLeftTarget = robot.fpd.getCurrentPosition() + (int) ((leftInches) * this.COUNTS_PER_INCH);
            newRightTarget = robot.fsd.getCurrentPosition() + (int) ((rightInches) * this.COUNTS_PER_INCH);

            robot.fpd.setTargetPosition(newLeftTarget);
            robot.fsd.setTargetPosition(newRightTarget);
            robot.bpd.setTargetPosition(newLeftTarget);
            robot.bsd.setTargetPosition(newRightTarget);


            int NT1 = (int) (newLeftTarget * 0.981); //fp
            int NT2 = (int) (newRightTarget * 0.981); //fp


            // Turn On RUN_TO_POSITION
            robot.fpd.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.fsd.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.bpd.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.bsd.setMode(DcMotor.RunMode.RUN_TO_POSITION);


            // reset the timeout time and start motion.
            runtime.reset();
            robot.fpd.setPower(Math.abs(speed));
            robot.fsd.setPower(Math.abs(speed));
            robot.bsd.setPower(Math.abs(speed));
            robot.bpd.setPower(-Math.abs(speed));


            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.


            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    // ( front_star_wheel.isBusy() && front_port_wheel.isBusy()))
                    ((Math.abs(robot.fpd.getCurrentPosition()) < Math.abs(NT1)) || (Math.abs(robot.fsd.getCurrentPosition()) < Math.abs(NT2)))) {

                // Display it for the driver.
                telemetry.addData("Path1", "Running to %7d :%7d", newLeftTarget, newRightTarget);
                telemetry.addData("Path2", "Running at %7d :%7d",
                        robot.fpd.getCurrentPosition(),
                        robot.fsd.getCurrentPosition());


                telemetry.update();
            }

            // Stop all motion;
            robot.fpd.setPower(0);
            robot.fsd.setPower(0);
            robot.bsd.setPower(0);
            robot.bpd.setPower(0);


            // Turn off RUN_TO_POSITION
            robot.fpd.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.fsd.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.bsd.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.bpd.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


            robot.fpd.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            robot.fsd.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            robot.bpd.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            robot.bsd.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


            sleep(250);   // optional pause after each move
        }
    }

    public void driveBE(double speed, double leftInches, double rightInches, double timeoutS) {  //forward back and turn with encoder

        int newLeftTarget = 0;
        int newRightTarget = 0;

        setMotorDirDrive();


        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newLeftTarget = robot.fpd.getCurrentPosition() + (int) (leftInches * COUNTS_PER_INCH);
            newRightTarget = robot.fsd.getCurrentPosition() + (int) (rightInches * COUNTS_PER_INCH);

            robot.fpd.setTargetPosition(newLeftTarget);
            robot.fsd.setTargetPosition(newRightTarget);
            robot.bpd.setTargetPosition(newLeftTarget);
            robot.bsd.setTargetPosition(newRightTarget);


            int NT1 = (int) (newLeftTarget * 0.981); //fp
            int NT2 = (int) (newRightTarget * 0.981); //fp


            // Turn On RUN_TO_POSITION
            robot.fpd.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.fsd.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.bpd.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.bsd.setMode(DcMotor.RunMode.RUN_TO_POSITION);


            // reset the timeout time and start motion.
            runtime.reset();
            robot.fpd.setPower(Math.abs(speed));
            robot.fsd.setPower(Math.abs(speed));
            robot.bsd.setPower(Math.abs(speed));
            robot.bpd.setPower(-Math.abs(speed));


            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.


            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    // ( front_star_wheel.isBusy() && front_port_wheel.isBusy()))
                    ((Math.abs(robot.fpd.getCurrentPosition()) < Math.abs(NT1)) || (Math.abs(robot.fsd.getCurrentPosition()) < Math.abs(NT2)))) {

                // Display it for the driver.
                telemetry.addData("Path1", "Running to %7d :%7d", newLeftTarget, newRightTarget);
                telemetry.addData("Path2", "Running at %7d :%7d",
                        robot.fpd.getCurrentPosition(),
                        robot.fsd.getCurrentPosition());

                telemetry.addData("Bore Encoders", "Position %7d", robot.bpd.getCurrentPosition());

                correctForBack(speed);

                /*ways to do this-- add try to correct along the way
                 *                -- only use bore through encoders
                 *                -- use a correct move sideways to account for sideways deviation
                 *                -- temporarily halt the motor on the side of deviation
                 *
                 * */

                    telemetry.update();
                }
            }

            // Stop all motion;
            robot.fpd.setPower(0);
            robot.fsd.setPower(0);
            robot.bsd.setPower(0);
            robot.bpd.setPower(0);


            // Turn off RUN_TO_POSITION
            robot.fpd.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.fsd.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            //robot.bsd.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            //robot.bpd.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


            robot.fpd.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            robot.fsd.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
           // robot.bpd.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
           // robot.bsd.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


            sleep(250);   // optional pause after each move
        }

    public void sideways(double speed, double frontInches, double backInches, double timeoutS){ //sideways encoder    //negative is left maybbbeeee hopefully
        int newFrontTarget = 0;
        int newBackTarget = 0;

        setMotorDirSide();

        // Ensure that the opmode is still active
        if (opModeIsActive()) {



            // Determine new target position, and pass to motor controller
            newFrontTarget = robot.fsd.getCurrentPosition() + (int) (frontInches * this.COUNTS_PER_INCH);
            newBackTarget = robot.bsd.getCurrentPosition() + (int) (backInches * this.COUNTS_PER_INCH);

            robot.fpd.setTargetPosition(newFrontTarget);
            robot.fsd.setTargetPosition(newFrontTarget);
            robot.bpd.setTargetPosition(newBackTarget);
            robot.bsd.setTargetPosition(newBackTarget);


            int NT1 = (int) (newFrontTarget * 0.981); //fs
            int NT2 = (int) (newBackTarget * 0.981); //bs


            // Turn On RUN_TO_POSITION
            robot.fpd.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.fsd.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.bpd.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.bsd.setMode(DcMotor.RunMode.RUN_TO_POSITION);


            // reset the timeout time and start motion.
            runtime.reset();
            robot.fpd.setPower(Math.abs(speed));  //-
            robot.fsd.setPower(Math.abs(speed));
            robot.bsd.setPower(Math.abs(speed));  //-
            robot.bpd.setPower(Math.abs(speed));


            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    // ( front_star_wheel.isBusy() && front_port_wheel.isBusy()))
                    ((Math.abs(robot.fsd.getCurrentPosition()) < Math.abs(NT1)) || (Math.abs(robot.bsd.getCurrentPosition()) < Math.abs(NT2)))) { // took this out for now || (Math.abs(robot.bsd.getCurrentPosition()) < Math.abs(NT2))

                // Display it for the driver.
                telemetry.addData("Path1", "Running to %7d :%7d", newFrontTarget, newBackTarget);
                telemetry.addData("Path2", "Running at %7d :%7d",
                        robot.fsd.getCurrentPosition(),
                        robot.bsd.getCurrentPosition());

                telemetry.update();
            }

            // Stop all motion;
            robot.fpd.setPower(0);
            robot.fsd.setPower(0);
            robot.bsd.setPower(0);
            robot.bpd.setPower(0);


            // Turn off RUN_TO_POSITION
            robot.fpd.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.fsd.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.bsd.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.bpd.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


            robot.fpd.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            robot.fsd.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            robot.bpd.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            robot.bsd.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


            sleep(250);   // optional pause after each move
        }


    }

    public void sidewaysBE(double speed, double frontInches, double backInches, double timeoutS){ //sideways encoder    //negative is left maybbbeeee hopefully
        int newFrontTarget = 0;
        int newBackTarget = 0;

        setMotorDirSide();

        // Ensure that the opmode is still active
        if (opModeIsActive()) {


            // Determine new target position, and pass to motor controller
            newFrontTarget = robot.fsd.getCurrentPosition() + (int) (frontInches * COUNTS_PER_INCH);
            newBackTarget = robot.bsd.getCurrentPosition() + (int) (backInches * COUNTS_PER_INCH);

            robot.fpd.setTargetPosition(newFrontTarget);
            robot.fsd.setTargetPosition(newFrontTarget);
            robot.bpd.setTargetPosition(newBackTarget);
            robot.bsd.setTargetPosition(newBackTarget);


            int NT1 = (int) (newFrontTarget * 0.981); //fs
            int NT2 = (int) (newBackTarget * 0.981); //bs


            // Turn On RUN_TO_POSITION
            robot.fpd.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.fsd.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.bpd.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.bsd.setMode(DcMotor.RunMode.RUN_TO_POSITION);


            // reset the timeout time and start motion.
            runtime.reset();
            robot.fpd.setPower(Math.abs(speed));  //-
            robot.fsd.setPower(Math.abs(speed));
            robot.bsd.setPower(Math.abs(speed));  //-
            robot.bpd.setPower(Math.abs(speed));


            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    // ( front_star_wheel.isBusy() && front_port_wheel.isBusy()))
                    ((Math.abs(robot.fsd.getCurrentPosition()) < Math.abs(NT1)) || (Math.abs(robot.fpd.getCurrentPosition()) < Math.abs(NT2)))) {

                // Display it for the driver.
                telemetry.addData("Path1", "Running to %7d :%7d", newFrontTarget, newBackTarget);
                telemetry.addData("Path2", "Running at %7d :%7d",
                        robot.fsd.getCurrentPosition(),
                        robot.bsd.getCurrentPosition());
                telemetry.addData("Correction", "Position %4d ", boundBE);

                correctSideways(speed);

                telemetry.update();
            }

            // Stop all motion;
            robot.fpd.setPower(0);
            robot.fsd.setPower(0);
            robot.bsd.setPower(0);
            robot.bpd.setPower(0);


            // Turn off RUN_TO_POSITION
            robot.fpd.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.fsd.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.bsd.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.bpd.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


            robot.fpd.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            robot.fsd.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            robot.bpd.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            robot.bsd.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


            sleep(250);   // optional pause after each move
        }


    }

    public void encoderWobbleMove(double speed, double inches, double timeoutS) {  //move wobble motor up down!!!!!!!!!!FIND BOUNDSSSSSSSS

        int newTarget = 0;

        setMotorDirDrive();


        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newTarget = robot.wobbleMotor.getCurrentPosition() + (int) (inches * COUNTS_PER_INCH);


            robot.wobbleMotor.setTargetPosition(newTarget);


            int NT = (int) (newTarget * 0.901); //fp



            // Turn On RUN_TO_POSITION
            robot.wobbleMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);



            // reset the timeout time and start motion.
            runtime.reset();
            robot.wobbleMotor.setPower(Math.abs(speed));




            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    // ( front_star_wheel.isBusy() && front_port_wheel.isBusy()))
                    ((Math.abs(robot.wobbleMotor.getCurrentPosition()) < Math.abs(NT)))) {

                // Display it for the driver.
                telemetry.addData("Path1", "Running to %7d", newTarget);
                telemetry.addData("Path2", "Running at %7d",
                        robot.wobbleMotor.getCurrentPosition());

                telemetry.update();
            }

            // Stop all motion;
            robot.wobbleMotor.setPower(0);



            // Turn off RUN_TO_POSITION
            robot.wobbleMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


            robot.wobbleMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);



            sleep(250);   // optional pause after each move
        }
    }

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
            //shootVelo(speed);

            robot.shooty.setPower(speed);

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
            fruitShootMove();

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

    public void correctForBack(double speed){

         double sped = (speed - spedAdjust);  //adjusted speed

        if(robot.bpd.getCurrentPosition()>boundBE) { // too much port???

            while (robot.bpd.getCurrentPosition()>boundBE) {  //correct while its bad

                //decrease port side motor speed for adjustments, at spEd
                robot.bpd.setPower(sped);
                robot.fpd.setPower(sped);

            }

            //put port side motor speed back where it should be, at spEEd
            robot.bpd.setPower(speed);
            robot.fpd.setPower(speed);

        }else if(robot.bpd.getCurrentPosition()< -boundBE) {   //too much star???

            while (robot.bpd.getCurrentPosition()< -boundBE) {

                //decrease star side motor speed for adjustments, at spEd
                robot.bsd.setPower(sped);
                robot.fsd.setPower(sped);

            }

            //put star side motor speed back where it should be, at spEEd
            robot.bsd.setPower(sped);
            robot.fsd.setPower(sped);

        }
    }

    public void correctSideways(double speed){

         double sped = (speed - spedAdjust);

        if(robot.bsd.getCurrentPosition()>boundBE) { // too much forward side

            while (robot.bsd.getCurrentPosition()>boundBE) {
                //decrease front side motor speed for adjustments, at spEd
                robot.fpd.setPower(sped);
                robot.fsd.setPower(sped);

            }

            //put front side motor speed back where it should be, at spEEd
            robot.fsd.setPower(speed);
            robot.fpd.setPower(speed);

        }else if(robot.bsd.getCurrentPosition()< -boundBE) {   //too much back side

            while (robot.bsd.getCurrentPosition()< -boundBE) {
                //decrease back side motor speed for adjustments, at spEd
                robot.bsd.setPower(sped);
                robot.bpd.setPower(sped);

            }

            //put back side motor speed back where it should be, at spEEd
            robot.bsd.setPower(sped);
            robot.bpd.setPower(sped);

        }
    }

    public void fruitShootMove(){
        for(double i = 0; i < 3; i++){
            sleep(1300);

            fruitSpeed += .035;

            robot.butterKnife.setPosition(robot.butterShoot);
            sleep(1300);

            //move over
            //sideways(fruitSpeed, 1.1, -1.1, 20); //align with pwrst

            robot.butterKnife.setPosition(robot.butterStop);


        }
        sleep(500);
        return;

    }

    public void wobbleFruitClose(){
        robot.blueFruit.setPosition(robot.closedBlue);
        robot.greyFruit.setPosition(robot.closedGrey);

    }

    public void wobbleFruitOpenSesame(){
        robot.blueFruit.setPosition(robot.openBlue);
        robot.greyFruit.setPosition(robot.openGrey);
    }

    public void setWobbleDown(){
        encoderWobbleMove(.95, -8.75, 2);
        sleep(100);
        encoderWobbleMove(.7, -8.5, 1.1);// arm put wobble down

        wobbleFruitOpenSesame();     //open finger
        sleep(900);

        encoderWobbleMove(.8, 52, 1.5);  // pull arm back up
        sleep(500);

    }

    public void launcherLaunch(double sped, double dist, double tO){
        while (encoderLaunchy(sped, dist, tO)){

            fruitShootMove();

            return;

        }
    }

    public void stopLauncher(){
        robot.shooty.setPower(0);

        robot.butterKnife.setPosition(robot.butterStop);
        sleep(100);


    }

    public void setMotorDirSide(){
        robot.fsd.setDirection(DcMotorSimple.Direction.REVERSE);
        robot.bsd.setDirection(DcMotorSimple.Direction.FORWARD);
        robot.fpd.setDirection(DcMotorSimple.Direction.REVERSE);
        robot.bpd.setDirection(DcMotorSimple.Direction.FORWARD);

    }

    public void setMotorDirDrive(){
        robot.fsd.setDirection(DcMotorSimple.Direction.FORWARD);
        robot.bsd.setDirection(DcMotorSimple.Direction.FORWARD);
        robot.fpd.setDirection(DcMotorSimple.Direction.REVERSE);
        robot.bpd.setDirection(DcMotorSimple.Direction.REVERSE);

    }

    public void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraName = hardwareMap.get(WebcamName.class, "eyes");
        //parameters.webcamCalibrationFiles("");
        //parameters.camera.createCaptureSession();
        //parameters.cameraMonitorFeedback();

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the TensorFlow Object Detection engine.
    }

    public void initTfod() {
        /**
         * Initialize the TensorFlow Object Detection engine.
         */
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minResultConfidence = 0.8f;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_FIRST_ELEMENT, LABEL_SECOND_ELEMENT);
    }

    public int AltVision() {
        String rings = "";
        //while (opModeIsActive()) {
        if (tfod != null && rings.equals("")) {
            // getUpdatedRecognitions() will return null if no new information is available since
            // the last time that call was made.
            List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
            if (updatedRecognitions != null) {
                telemetry.addData("# Object Detected", updatedRecognitions.size());      //size!!!!!!!!!!!!!!!! is the determinant for wht box path to take????VALUESSS???????????????
                // step through the list of recognitions and display boundary info.
                int i = 0;
                for (Recognition recognition : updatedRecognitions) {
                    telemetry.addData(String.format("label (%d)", i), recognition.getLabel());
                    telemetry.addData(String.format("  left,top (%d)", i), "%.03f , %.03f",
                            recognition.getLeft(), recognition.getTop());
                    telemetry.addData(String.format("  right,bottom (%d)", i), "%.03f , %.03f",
                            recognition.getRight(), recognition.getBottom());
                    rings = recognition.getLabel();
                    sleep(500);  //just added

                }
                telemetry.update();
            }
        }
        if(tfod !=null){tfod.shutdown();}
        if (rings.equals("Single")) {return 1;}
        else if (rings.equals("Quad")) {return 4;}
        else {return 0;}
    }

}
