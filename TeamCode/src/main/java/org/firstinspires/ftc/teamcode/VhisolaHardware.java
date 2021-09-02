package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;

import org.firstinspires.ftc.robotcore.internal.system.Deadline;

//@Disabled

public class VhisolaHardware extends LinearOpMode {
    /* Public OpMode members. */
    public DcMotor fsd = null;
    public DcMotor fpd = null;
    public DcMotor bpd = null;
    public DcMotor bsd = null;

    public DcMotor wobbleMotor = null;
    public Servo blueFruit = null;
    public Servo greyFruit = null;

    public DcMotor intakeTop = null;
    public DcMotor intakeBottom = null; //inverse of intakeTop

    public Servo butterKnife = null;
    public DcMotorEx shooty = null;

    public DistanceSensor plsWork = null;
    public RevBlinkinLedDriver led = null;


    public static final double openBlue       =  0.7; //does work for butterknife????????????????????????????????????
    public static final double closedBlue     =  .2;
    public static final double openGrey       =  .5; //does work for butterknife????????????????????????????????????
    public static final double closedGrey     =  1.0;
    public static final double midPos    =  .4;

    public static final double wobbleSpeed = .6;

    public static final double intakeSpeed = 1.0;
    public static final double intakeTopSpeed = 1.0;

    public static final double shootVelo = 7.5;   // the max velo was 8.3??
    public static final double butterShoot = .6;   //.52
    public static final double butterStop = .35;

    /*public static final double ARM_UP_POWER    =  0.45 ;
    public static final double ARM_DOWN_POWER  = -0.45 ;*/

    private ElapsedTime runtime = new ElapsedTime();

    static final double COUNTS_PER_MOTOR_REV = 580;
    static final double DRIVE_GEAR_REDUCTION = (10/26.0);     // This is < 1.0 if geared UP
    static final double WHEEL_DIAMETER_INCHES = 3;     // For figuring circumference
    static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);

    static final double COUNTS_PER_MOTOR_REV_BE = 8192;
    static final double DRIVE_GEAR_REDUCTION_BE = 1.0;     // This is < 1.0 if geared UP
    static final double WHEEL_DIAMETER_INCHES_BE = 3.5;     // For figuring circumference
    public static final double COUNTS_PER_INCH_BE = (COUNTS_PER_MOTOR_REV_BE * DRIVE_GEAR_REDUCTION_BE) /
            (WHEEL_DIAMETER_INCHES_BE * 3.1415);

    public final double ninetyTurn = (2 * Math.PI * 7.25);  //what is the radius of our wheels

    /* local OpMode members. */
    HardwareMap hwMap = null;
    private ElapsedTime time = new ElapsedTime();

    /* Constructor */
    public VhisolaHardware() {
    }


    @Override
    public void runOpMode() {
    }

    /* Initialize standard Hardware interfaces */
    //@Override
    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        // Define and Initialize Motors
        fpd = hwMap.get(DcMotor.class, "fpd");
        fsd = hwMap.get(DcMotor.class, "fsd");
        bpd = hwMap.get(DcMotor.class, "bpd");
        bsd = hwMap.get(DcMotor.class, "bsd");

        wobbleMotor = hwMap.get(DcMotor.class, "wobbleMotor");
        blueFruit = hwMap.get(Servo.class, "blueFruit");
        greyFruit = hwMap.get(Servo.class, "greyFruit");

        intakeTop = hwMap.get(DcMotor.class, "intakeTop");
        intakeBottom = hwMap.get(DcMotor.class, "intakeBottom");

        shooty = hwMap.get(DcMotorEx.class, "shooty");
        butterKnife = hwMap.get(Servo.class, "butterKnife");
        plsWork = hwMap.get(DistanceSensor.class, "plsWork");
        led = hwMap.get(RevBlinkinLedDriver.class, "led");

        //set direction of rotations
        fsd.setDirection(DcMotorSimple.Direction.FORWARD);
        bsd.setDirection(DcMotorSimple.Direction.FORWARD);
        fpd.setDirection(DcMotorSimple.Direction.REVERSE);
        bpd.setDirection(DcMotorSimple.Direction.REVERSE);

        wobbleMotor.setDirection(DcMotorSimple.Direction.FORWARD);

        intakeTop.setDirection(DcMotorSimple.Direction.FORWARD);
        intakeBottom.setDirection(DcMotorSimple.Direction.FORWARD);
        shooty.setDirection(DcMotorSimple.Direction.FORWARD);

        // Set all motors to zero power
        fpd.setPower(0);
        bpd.setPower(0);
        fsd.setPower(0);
        bsd.setPower(0);

        wobbleMotor.setPower(0);
        shooty.setPower(0);
        intakeTop.setPower(0);
        intakeBottom.setPower(0);



        blueFruit.setPosition(closedBlue);
        greyFruit.setPosition(closedGrey);
        butterKnife.setPosition(butterStop); ///should be right now??? check maybe not right??? probably not right??????????????????????????????????



        // Set all motors to run without encoders.
        // May want to use RUN_USING_ENCODERS if encoders are installed.
        fpd.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        fsd.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        bpd.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        bsd.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        wobbleMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        intakeTop.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        intakeBottom.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        shooty.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        ///////////////////////

        fpd.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);   //new, could mess soomrthing up, but should be fine
        fsd.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        bpd.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        bsd.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //wobbleMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        intakeTop.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intakeBottom.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        shooty.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Define and initialize ALL installed servos.
        //leftClaw  = hwMap.get(Servo.class, "left_hand");
        //rightClaw = hwMap.get(Servo.class, "right_hand");
        //leftClaw.setPosition(MID_SERVO);
        //rightClaw.setPosition(MID_SERVO);
    }

}
