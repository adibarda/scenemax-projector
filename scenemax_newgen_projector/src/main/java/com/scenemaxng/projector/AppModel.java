package com.scenemaxng.projector;
import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.scene.Spatial;



public class AppModel {
    public Spatial model;
    private AnimChannel channel;
    private AnimControl control;

    public AppModel(Spatial m) {
        model=m;
    }

    public AnimChannel getChannel() {
        if(channel==null) {
            channel = control.createChannel();
        }
        
        return channel;
    }

    public AnimControl getAnimControl() {
        if(control==null) {
            control = model.getControl(AnimControl.class);
        }

        return control;
    }
}
