package com.scenemaxng.projector;
import java.util.ArrayList;

public class CompositeController extends SceneMaxBaseController {

    private ArrayList<ISceneMaxController> _controllers = new ArrayList<>();
    private int runningControllerIndex=0;

    public void add(SceneMaxBaseController c) {
        _controllers.add(c);
    }

    @Override
    public void init() {
        for(int i=0;i<_controllers.size();++i) {
            ISceneMaxController c = _controllers.get(i);
            c.setUIProxy(this._uiProxy);
            c.init();
        }
    }

    @Override
    public boolean run(float tpf) {

        boolean finished = _controllers.get(runningControllerIndex).run(tpf);

        if(finished) {

            runningControllerIndex++;
            if(runningControllerIndex < _controllers.size()) {
                return false;
            } else {
                return true; // no more controllers to run
            }

        }

        return false; // current controller not finished


    }

}
