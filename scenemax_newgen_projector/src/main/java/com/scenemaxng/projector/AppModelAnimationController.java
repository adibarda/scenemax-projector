package com.scenemaxng.projector;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.LoopMode;


public class AppModelAnimationController implements AnimEventListener {

    public boolean animationFinished = false;
    private String animationName = null;


    @Override
    public void onAnimCycleDone(AnimControl animControl, AnimChannel animChannel, String animName) {

        if (animName.equals(animationName)) {
            System.out.println("Finished running animation: "+animationName);
            animationFinished = true;
        }
    }

    @Override
    public void onAnimChange(AnimControl animControl, AnimChannel animChannel, String s) {

    }

    public void animate(AppModel m, String animationName, String speed) {

        this.animationName = animationName;
        AnimControl control = m.getAnimControl();
        control.addListener(this);

        AnimChannel channel = m.getChannel();
        channel.setAnim(animationName);
        channel.setLoopMode(LoopMode.DontLoop);
        Float animSpeed = Float.parseFloat(speed);
        channel.setSpeed(animSpeed);

        System.out.println("Start animation "+animationName+" at speed of "+speed);
    }

}
