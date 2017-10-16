package com.scenemaxng.projector;

public class ModelAnimateController extends SceneMaxBaseController {

    public String speed;
    public String targetVar;

    //private float targetTime=0;
    private boolean animationStarted = false;

    private AppModelAnimationController controller;


    public String animationName = null;

    @Override
    public void init() {
        //targetTime = Float.parseFloat(speed);
        controller=new AppModelAnimationController();
    }

    @Override
    public boolean run(float tpf) {

        if(!animationStarted) {
            animationStarted=true;
            _uiProxy.animateModel(targetVar, animationName, speed, controller);
        }

        //System.out.println(animationName+" controller.animationFinished = "+controller.animationFinished);
        return controller.animationFinished;
    }

}
