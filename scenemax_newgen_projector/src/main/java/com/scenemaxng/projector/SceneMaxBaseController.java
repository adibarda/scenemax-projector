package com.scenemaxng.projector;
import com.scenemaxng.projector.ISceneMaxController;
import com.scenemaxng.projector.IUiProxy;

public class SceneMaxBaseController implements ISceneMaxController {

    protected IUiProxy _uiProxy;

    public void setUIProxy(IUiProxy p) {
        _uiProxy = p;
    }

    @Override
    public boolean run(float tpf) {
        return false;
    }

    @Override
    public void init() {

    }


}
