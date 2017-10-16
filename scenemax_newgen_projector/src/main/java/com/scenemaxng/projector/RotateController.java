package com.scenemaxng.projector;

public class RotateController extends SceneMaxBaseController{

    public String axis;
    public String numSign;
    public String num;
    public String targetVar;
    public String speed;


    private float passedTime = 0;
    private float targetTime=0;
    private float targetVal=0;
    private int axisNum = -1;
    private float direction = 1;


    @Override
    public void init() {
        targetTime = Float.parseFloat(speed);
        targetVal=Float.parseFloat(num);

        if(axis.equals("x")) {
            axisNum=1;
        } else if(axis.equals("y")) {
            axisNum=2;
        } else if(axis.equals("z")) {
            axisNum=3;
        }

        if(numSign.equals("-")) {
            direction=-1;
        }

    }

    @Override
    public boolean run(float tpf) {
        boolean finished = false;
        passedTime+=tpf;
        if(passedTime>=targetTime) {
            tpf=targetTime-passedTime;
            finished=true;
        }

        float rotateVal = tpf*targetVal/targetTime;
        _uiProxy.rotateModel(targetVar,axisNum,direction,rotateVal);

        return finished;
    }
}
