package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;

@Disabled
public class TestEncoders {

    VhisolaHardware robot = new VhisolaHardware();

    public TestEncoders(){}

    //@Override
    public void runOpMode() {
    }

    public double forBackCount(){
        robot.bpd.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        robot.bpd.setMode(DcMotor.RunMode.RUN_USING_ENCODER);



        return 0;
    }

    public double sidewaysCount(){

        return 0;
    }

}
