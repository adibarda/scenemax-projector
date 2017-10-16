package com.scenemaxng.projector;

public interface IUiProxy {
    int loadModel(String name, String resourcePath);
    int loadSprite(String name, String resourcePath);
    int registerController(ISceneMaxController c);

    void rotateModel(String targetVar, int axisNum, float direction, float rotateVal);
    void animateModel(String targetVar, String animationName, String speed, AppModelAnimationController animEventListener);


}
