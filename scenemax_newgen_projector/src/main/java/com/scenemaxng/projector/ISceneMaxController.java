package com.scenemaxng.projector;

public interface ISceneMaxController {

    void setUIProxy(IUiProxy p);
    boolean run(float tpf);
    void init();

}
